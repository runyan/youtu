package com.maoxiong.youtu.pool.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.initializer.Initializer;
import com.maoxiong.youtu.pool.RequestPool;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.LogUtil;

/**
 * 
 * @author yanrun
 *
 */
public class DefaultRequestPool implements RequestPool {
	
	private final AtomicInteger threadSequance = new AtomicInteger();
	private static final int MAX_THREAD_NUM = 50;
	private static final Set<String> THREAD_SET = new HashSet<>(MAX_THREAD_NUM);
	
	private ExecutorService threadPool;
	private ThreadLocal<ExecutorService> poolLocal = new ThreadLocal<>();
	private ThreadLocal<Boolean> executeLocal = new ThreadLocal<>();
	private ThreadLocal<Set<RequestWrapper>> requestLocal = new ThreadLocal<>();
	private Set<RequestWrapper> requestSet;
	private static volatile boolean isClosed;
	private static volatile Map<String, ExecutorService> exsitPools;
	
	private int size;
	
	private DefaultRequestPool() {
		Initializer.initCheck();
		exsitPools = new ConcurrentHashMap<>(16);
	}
	
	enum SingletonHolder {
		/**
		 * instance
		 */
		INSTANCE; 
		
		private DefaultRequestPool pool;
		
		SingletonHolder() {
			pool = new DefaultRequestPool();
		}
		
		public DefaultRequestPool getRequestPool() {
			return pool;
		}
	}
	
	public static DefaultRequestPool getInstace() {
		return SingletonHolder.INSTANCE.getRequestPool();
	}
	
	@Override
	public void addRequest(Client requestClient, CallBack callback) {
		checkBeforeAdd();
		Objects.requireNonNull(requestClient, "null requestClient");
		Objects.requireNonNull(callback, "null callback");
		RequestWrapper wrapper = wrapRequest(requestClient.getRequest(), requestClient, callback);
		boolean added = requestSet.add(wrapper);
		String currentThreadName = Thread.currentThread().getName();
		if(added) {
			size = requestSet.size();
			LogUtil.info("added request: {} for {} total {}, for {}", 
					wrapper, currentThreadName, size + (size == 1 ? " request" : " requests"), currentThreadName);
		}
	}
	
	@Override
	public void addRequestsByMap(Map<Client, CallBack> requestMap) {
		checkBeforeAdd();
		Objects.requireNonNull(requestMap, "requestMap is null");
		if(requestMap.isEmpty()) {
			LogUtil.info("nothing to add");
			return ;
		}
		int mapSize = requestMap.size();
		if(mapSize == 1) {
			Entry<Client, CallBack> mapEntry = requestMap.entrySet().iterator().next();
			addRequest(mapEntry.getKey(), mapEntry.getValue());
		} else {
			List<RequestWrapper> wrapperList = new ArrayList<>(mapSize);
			requestMap.forEach((client, callback) -> {
				RequestWrapper wrapper = wrapRequest(client.getRequest(), client, callback);
				wrapperList.add(wrapper);
				wrapper = null;
			});
			boolean added = requestSet.addAll(wrapperList);
			if(added) {
				String currentThreadName = Thread.currentThread().getName();
				size = requestSet.size();
				LogUtil.info("added {} for {}, total {} for {}", 
						mapSize + (mapSize == 1 ? " request" : " requests") + ": " + 
						wrapperList.toString(), currentThreadName, size + (size == 1 ? " request" : " requests"), currentThreadName);
			}
		}
	}
	
	private void checkBeforeAdd() {
		if(isClosed) {
			throw new IllegalStateException("cannot add request to a closed pool");
		}
		String currentThreadName = Thread.currentThread().getName();
		THREAD_SET.add(currentThreadName);
		int activeThreadNum = THREAD_SET.size();
		int maxThreadNum = MAX_THREAD_NUM;
		if(activeThreadNum > maxThreadNum) {
			throw new RuntimeException("cannot add more requests, max active thread number allowed: " + maxThreadNum
					+ ", current thread number: " + activeThreadNum);
		}
		requestSet = getRequestSet();
	}
	
	@Override
	public void execute() {
		String currentThreadName = Thread.currentThread().getName();
		requestSet = getRequestSet();
		size = requestSet.size();
		threadPool = Optional.ofNullable(poolLocal.get()).orElseGet(() -> {
			ExecutorService initPool = new ThreadPoolExecutor(size, 50, 500, TimeUnit.MILLISECONDS, 
					new ArrayBlockingQueue<Runnable>(100), (r) -> new Thread(r, "ThreadPool thread: "  + threadSequance.incrementAndGet()));
			poolLocal.set(initPool);
			return initPool;
		});
		boolean isExecuting = Optional.ofNullable(executeLocal.get()).orElseGet(() -> {
			Boolean initExecuting = false;
			executeLocal.set(initExecuting);
			return initExecuting;
		});
		if(isExecuting) {
			LogUtil.warn("abort execute for {}, request pool is executing, should not call execute more than once", currentThreadName);
			return ;
		}
		if(threadPool.isShutdown() || threadPool.isTerminated() || isClosed) {
			throw new IllegalStateException("pool is already shut down");
		}
		exsitPools.put(currentThreadName, threadPool);
		if(requestSet.isEmpty()) {
			LogUtil.warn("nothing to execute");
			return ;
		}
		executeLocal.set(true);
		List<CompletableFuture<Void>> futureList = new ArrayList<>(size);
		CompletableFuture<Void> future;
		for(RequestWrapper wrapper : requestSet) {
			future = CompletableFuture.runAsync(new Runnable() {

				@Override
				public void run() {
					try {
						wrapper.getRequestClient().execute(wrapper.getCallback());
					} catch (Exception e) {
						e.printStackTrace();
						LogUtil.error("error while executing: {}", wrapper.getRequest());
					}
				}
				
			}, threadPool);
			futureList.add(future);
		}
		CompletableFuture<Void> completedFutures = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[size]));
		try {
			completedFutures.get(10, TimeUnit.MINUTES);
		} catch(TimeoutException e) {
			e.printStackTrace();
			LogUtil.error("execute timeout");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			LogUtil.error("execute exception");
		} finally {
			executeLocal.set(false);
			requestSet.clear();
			THREAD_SET.remove(currentThreadName);
		}
	}
	
	@Override
	public void cancel() {
		cleanUp();
	}
	
	@Override
	public void close() {
		cleanUp();
		THREAD_SET.clear();
		isClosed = true;
		poolLocal.remove();
		requestLocal.remove();
		executeLocal.remove();
	}
	
	@Override
	public int size() {
		return THREAD_SET.size();
	}

	@Override
	public boolean isEmpty() {
		return THREAD_SET.size() == 0;
	}
	
	@Override
	public boolean isClosed() {
		return isClosed;
	}
	
	private void cleanUp() {
		exsitPools.forEach((threadName, pool) -> {
			if(null != pool) {
				if(!pool.isShutdown() && !pool.isTerminated()) {
					pool.shutdown();
				}
			}
		});
		exsitPools.clear();
	}
	
	private Set<RequestWrapper> getRequestSet() {
		return Optional.ofNullable(requestLocal.get()).orElseGet(() -> {
			Set<RequestWrapper> initSet = Collections.synchronizedSet(new HashSet<>());
			requestLocal.set(initSet);
			return initSet;
		});
	}
	
	private RequestWrapper wrapRequest(Request request, Client requestClient, CallBack callback) {
		synchronized (request) {
			RequestWrapper wrapper;
			request =  Optional.ofNullable(requestClient.getRequest())
					.orElseThrow(() -> new RuntimeException("have not set request for client:" + requestClient.getClass().getName()));
			wrapper = new RequestWrapper();
			wrapper.wrap(request, requestClient, callback);
			return wrapper;
		}
	}
	
	private class RequestWrapper implements Comparable<RequestWrapper> {
		private Request request;
		private Client requestClient;
		private CallBack callback;
		
		private String requestUrl;
		private String requestParam;
		
		public void wrap(Request request, Client requestClient, CallBack callback) {
			this.request = request;
			this.requestClient = requestClient;
			this.callback = callback;
			this.requestUrl = request.getRequestUrl();
			this.requestParam = request.getParamsJsonString();
		}
		
		public Request getRequest() {
			return request;
		}
		public Client getRequestClient() {
			return requestClient;
		}
		public CallBack getCallback() {
			return callback;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(request, requestClient, callback);
		}
		
		@Override
		public boolean equals(Object another) {
			if(null == another) {
				return false;
			}
			if(!(another instanceof RequestWrapper)) {
				return false;
			}
			RequestWrapper anotherWrapper = (RequestWrapper) another;
			Request anotherRequest = anotherWrapper.getRequest();
			String anotherUrl = anotherRequest.getRequestUrl();
			String anotherParam = anotherRequest.getParamsJsonString();
 			return StringUtils.equalsIgnoreCase(requestUrl, anotherUrl) && StringUtils.equalsIgnoreCase(requestParam, anotherParam);
		}
		
		@Override
		public String toString() {
			return "request url: " + requestUrl + " param: " + requestParam;
		}

		@Override
		public int compareTo(RequestWrapper another) {
			if(this.equals(another)) {
				return 0;
			}
			return Integer.compare(this.hashCode(), another.hashCode());
		}
	}

}

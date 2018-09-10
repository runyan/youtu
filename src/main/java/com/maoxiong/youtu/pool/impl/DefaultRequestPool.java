package com.maoxiong.youtu.pool.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.pool.AbstractRequestPool;
import com.maoxiong.youtu.request.Request;

/**
 * 
 * @author yanrun
 *
 */
public class DefaultRequestPool extends AbstractRequestPool {
	
	private static final Logger logger = LogManager.getLogger(DefaultRequestPool.class);

	private final AtomicInteger threadSequance = new AtomicInteger();
	private static final int MAX_THREAD_NUM = 50;
	private static final Set<String> THREAD_SET = new HashSet<>(MAX_THREAD_NUM);
	
	private ExecutorService threadPool;
	private Set<RequestWrapper> requestSet;
	private ThreadLocal<ExecutorService> poolLocal = new ThreadLocal<>();
	private ThreadLocal<Boolean> executeLocal = new ThreadLocal<>();
	private ThreadLocal<Set<RequestWrapper>> requestLocal = new ThreadLocal<>();
	private static volatile boolean isClosed;
	private static volatile Map<String, ExecutorService> exsitPools;
	
	private final String currentThreadName = Thread.currentThread().getName();
	
	private DefaultRequestPool() {
		Object signObj = Context.get("sign");
		signObj = null == signObj ? "" : signObj;
		String sign = String.valueOf(signObj);
		if(StringUtils.isBlank(sign)) {
			throw new IllegalStateException("have not init properly");
		}
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
		if(isClosed) {
			throw new IllegalStateException("cannot add request to a closed pool");
		}
		THREAD_SET.add(currentThreadName);
		int activeThreadNum = THREAD_SET.size();
		int maxThreadNum = MAX_THREAD_NUM;
		if(activeThreadNum > maxThreadNum) {
			throw new RuntimeException("cannot add more requests, max active thread number allowed: " + maxThreadNum
					+ ", current thread number: " + activeThreadNum);
		}
		RequestWrapper wrapper = wrapRequest(requestClient.getRequest(), requestClient, callback);
		requestSet = getRequestSet();
		requestSet.add(wrapper);
		size = requestSet.size();
		logger.info("added request: " + wrapper  + " for " + currentThreadName + ", "
				+ "total " + size + (size == 1 ? " request" : " requests") + " for " + currentThreadName);
	}
	
	@Override
	public void addRequestsByMap(Map<Client, CallBack> requestMap) {
		List<RequestWrapper> wrapperList = new ArrayList<>(requestMap.size());
		requestMap.forEach((client, callback) -> {
			RequestWrapper wrapper = wrapRequest(client.getRequest(), client, callback);
			wrapperList.add(wrapper);
			wrapper = null;
		});
		requestSet.addAll(wrapperList);
	}
	
	@Override
	public void execute() {
		threadPool = Optional.ofNullable(poolLocal.get()).orElseGet(() -> {
			ExecutorService initPool = new ThreadPoolExecutor(size, 50, 500, TimeUnit.MILLISECONDS, 
					new ArrayBlockingQueue<Runnable>(100), (r) -> new Thread(r, "ThreadPool thread: "  + threadSequance.incrementAndGet()));
			poolLocal.set(initPool);
			return initPool;
		});
		requestSet = getRequestSet();
		boolean isExecuting = Optional.ofNullable(executeLocal.get()).orElseGet(() -> {
			Boolean initExecuting = false;
			executeLocal.set(initExecuting);
			return initExecuting;
		});
		if(isExecuting) {
			logger.warn("abort execute for " + currentThreadName + 
					", request pool is executing, should not call execute more than once");
			return ;
		}
		if(threadPool.isShutdown() || threadPool.isTerminated() || isClosed) {
			throw new IllegalStateException("pool is already shut down");
		}
		exsitPools.put(currentThreadName, threadPool);
		if(requestSet.isEmpty()) {
			logger.warn("nothing to execute");
			return ;
		}
		executeLocal.set(true);
		List<Callable<Boolean>> requestList = new ArrayList<>(requestSet.size());
		requestSet.forEach(wrapper -> {
			requestList.add(new ExecuteTask(wrapper));
		});
		try {
			threadPool.invokeAll(requestList, 5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.error("error while executing tasks");
			e.printStackTrace();
		}
		executeLocal.set(false);
		requestSet.clear();
		THREAD_SET.remove(currentThreadName);
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
	
	private void cleanUp() {
		exsitPools.forEach((threadName, pool) -> {
			if(null != pool) {
				if(!pool.isShutdown() && !pool.isTerminated()) {
					pool.shutdown();
				}
			}
		});
	}
	
	private Set<RequestWrapper> getRequestSet() {
		return Optional.ofNullable(requestLocal.get()).orElseGet(() -> {
			Set<RequestWrapper> initSet = Collections.synchronizedSet(new HashSet<>(8));
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
	
	private class RequestWrapper {
		private Request request;
		private Client requestClient;
		private CallBack callback;
		
		private String requestUrl;
		private String requestParam;
		
		public void wrap(Request request, Client requestClient, CallBack callback) {
			Objects.requireNonNull(requestClient, "null requestClient");
			Objects.requireNonNull(callback, "null callback");
			Objects.requireNonNull(request, "null request");
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
 			return anotherUrl.equals(requestUrl) && anotherParam.equals(requestParam);
		}
		
		@Override
		public String toString() {
			return "request url: " + requestUrl + " param: " + requestParam;
		}
	}
	
	private class ExecuteTask implements Callable<Boolean> {

		private RequestWrapper request;
		
		ExecuteTask(RequestWrapper wrapper) {
			this.request = wrapper;
		}
		
		@Override
		public Boolean call() throws Exception {
			try {
				request.getRequestClient().execute(request.getCallback());
			} catch(Exception e) {
				logger.error(request + " error");
				return false;
			}
			return true;
		}
		
	}

}

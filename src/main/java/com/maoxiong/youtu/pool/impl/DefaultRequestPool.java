package com.maoxiong.youtu.pool.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.context.Context;
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
	
	private final ExecutorService threadPool;
	private Set<RequestWrapper> requestSet;
	private static volatile boolean isClosed;
	private volatile AtomicBoolean isExecuting = new AtomicBoolean(false);
	private volatile AtomicInteger activeThreadNum = new AtomicInteger(0);
	private Queue<RequestPool> queue = Context.REQUEAT_POOL_QUEUE;
	
	private volatile int size;
	
	@SuppressWarnings("unchecked")
	public DefaultRequestPool() {
		Initializer.initCheck();
		threadPool = new ThreadPoolExecutor(10, MAX_THREAD_NUM, 1, TimeUnit.MINUTES, 
				new LinkedBlockingQueue<>(), (r) -> new Thread(r, "ThreadPool thread: "  + threadSequance.incrementAndGet()));
		requestSet = ConcurrentHashSet.SingletonHolder.INSTANCE.getSet();
		queue.offer(this);
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
			LogUtil.info("added request: {} by {} total {}", 
					wrapper, currentThreadName, size + (size == 1 ? " request" : " requests"));
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
		String currentThreadName = Thread.currentThread().getName();
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
				size = requestSet.size();
				LogUtil.info("added {} by {}, total {} ", 
						mapSize + (mapSize == 1 ? " request" : " requests") + ": " + 
						wrapperList.toString(), currentThreadName, size + (size == 1 ? " request" : " requests"));
			}
		}
	}
	
	private void checkBeforeAdd() {
		if(isClosed) {
			throw new IllegalStateException("cannot add request to a closed pool");
		}
		int curThreadNum = activeThreadNum.get();
		if(curThreadNum > MAX_THREAD_NUM) {
			throw new RuntimeException("cannot add more requests, max active thread number allowed: " + MAX_THREAD_NUM
					+ ", current thread number: " + curThreadNum);
		}
		activeThreadNum.addAndGet(1);
	}
	
	@Override
	public void execute() {
		if(threadPool.isShutdown() || threadPool.isTerminated() || isClosed) {
			throw new IllegalStateException("pool is already shut down");
		}
		if(requestSet.isEmpty()) {
			LogUtil.warn("nothing to execute");
			return ;
		}
		size = requestSet.size();
		String currentThreadName = Thread.currentThread().getName();
		if(isExecuting.get()) {
			LogUtil.warn("abort execute for {}, request pool is executing, should not call execute more than once", currentThreadName);
			return ;
		}
		List<CompletableFuture<Void>> futureList = new ArrayList<>(size);
		CompletableFuture<Void> future;
		for(RequestWrapper wrapper : requestSet) {
			future = CompletableFuture.runAsync(() -> {
				try {
					wrapper.getRequestClient().execute(wrapper.getCallback());
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error("error while executing: {}", wrapper.getRequest());
				}
			}, threadPool);
			futureList.add(future);
		}
		CompletableFuture<Void> completedFutures = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[size]));
		try {
			completedFutures.get(1, TimeUnit.MINUTES);
		} catch(TimeoutException e) {
			e.printStackTrace();
			LogUtil.error("execute timeout");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			LogUtil.error("execute exception");
		} finally {
			isExecuting.set(false);
			requestSet.clear();
			activeThreadNum.decrementAndGet();
		}
	}
	
	@Override
	public void cancel() {
		close();
	}
	
	@Override
	public void close() {
		isClosed = true;
		threadPool.shutdown();
		queue.poll();
	}
	
	@Override
	public int size() {
		return requestSet.size();
	}

	@Override
	public boolean isEmpty() {
		return requestSet.isEmpty();
	}
	
	@Override
	public boolean isClosed() {
		return isClosed;
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
	
	@SuppressWarnings("hiding")
	private static final class ConcurrentHashSet<RequestWrapper> implements Set<RequestWrapper> {

		private final static Object NULLObj = new Object();
		private final ConcurrentHashMap<RequestWrapper, Object> map = new ConcurrentHashMap<>(16);
		
		private ConcurrentHashSet() {
			
		}
		
		public enum SingletonHolder {
			/**
			 * instance
			 */
			INSTANCE; 
			
			@SuppressWarnings("rawtypes")
			private ConcurrentHashSet set;
			
			SingletonHolder() {
				set = new ConcurrentHashSet<>();
			}
			
			@SuppressWarnings("rawtypes")
			public ConcurrentHashSet getSet() {
				return set;
			}
		}
		
		@Override
		public int size() {
			return map.size();
		}

		@Override
		public boolean isEmpty() {
			return map.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			return map.containsKey(o);
		}

		@Override
		public Iterator<RequestWrapper> iterator() {
			return map.keySet().iterator();
		}

		@Override
		public Object[] toArray() {
			return map.keySet().toArray();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return map.keySet().toArray(a);
		}

		@Override
		public boolean add(RequestWrapper e) {
			return map.put(e, NULLObj) == null;
		}

		@Override
		public boolean remove(Object o) {
			return map.remove(o, NULLObj);
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return map.keySet().containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends RequestWrapper> c) {
			c.forEach(item -> {
				map.put(item, NULLObj);
			});
			return true;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return map.keySet().retainAll(c);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			c.forEach(item -> {
				if(map.containsKey(item)) {
					map.remove(item);
				}
			});
			return true;
		}

		@Override
		public void clear() {
			map.clear();
		}

	}

}

package com.maoxiong.youtu.pool.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.initializer.Initializer;
import com.maoxiong.youtu.internal.datastructure.ConcurrentHashSet;
import com.maoxiong.youtu.pool.RequestPool;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.CommonUtil;
import com.maoxiong.youtu.util.LogUtil;
import com.maoxiong.youtu.util.UniqueIDUtil;

/**
 * 
 * @author yanrun
 *
 */
public class DefaultRequestPool implements RequestPool {
	
	private final AtomicInteger threadSequance = new AtomicInteger();
	
	private final ExecutorService threadPool;
	private Set<RequestWrapper> requestSet;
	private static volatile boolean isClosed;
	private volatile AtomicBoolean isExecuting = new AtomicBoolean(false);
	private Queue<RequestPool> queue = Context.REQUEAT_POOL_QUEUE;
	
	private volatile int size;
	
	@SuppressWarnings("unchecked")
	public DefaultRequestPool() {
		Initializer.initCheck();
		threadPool = new ThreadPoolExecutor(10, 50, 1, TimeUnit.MINUTES, 
				new LinkedBlockingQueue<>(50), 
				(r) -> new Thread(r, "ThreadPool thread: "  + threadSequance.incrementAndGet()));
		requestSet = ConcurrentHashSet.SingletonHolder.INSTANCE.getSet();
		queue.offer(this);
	}
	
	@Override
	public void addRequest(Client requestClient, CallBack callback) {
		checkBeforeAdd();
		RequestWrapper wrapper = new RequestWrapper(requestClient, callback);
		boolean added = requestSet.add(wrapper);
		String currentThreadName = Thread.currentThread().getName();
		if(added) {
			size = requestSet.size();
			LogUtil.info("added request: {} by {} total {}", 
					requestClient.getClass().getSimpleName().replace("Client", "request").concat(String.valueOf(wrapper.getId())), 
					currentThreadName, size + CommonUtil.singularOrPlural(size, " request", " requests"));
		}
	}
	
	@Override
	public void addRequestsByMap(Map<Client, CallBack> requestMap) {
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
			checkBeforeAdd();
			boolean added = false;
			List<RequestWrapper> wrapperList = new ArrayList<>(mapSize);
			Set<Entry<Client, CallBack>> entrySet = requestMap.entrySet();
			Client client;
			CallBack callback;
			RequestWrapper wrapper;
			for(Entry<Client, CallBack> entry : entrySet) {
				client = entry.getKey();
				callback = entry.getValue();
				wrapper = new RequestWrapper(client, callback);
				wrapperList.add(wrapper);
				added = requestSet.add(wrapper);
			}
			if(added) {
				size = requestSet.size();
				String currentThreadName = Thread.currentThread().getName();
				LogUtil.info("added {} by {}, total {} ", 
						mapSize + " requests", currentThreadName, size + CommonUtil.singularOrPlural(size, " request", " requests"));
			}
		}
	}
	
	private void checkBeforeAdd() {
		if(isClosed) {
			throw new IllegalStateException("cannot add request to a closed pool");
		}
		if(isExecuting.get()) {
			throw new RuntimeException("cannot add request to an executing pool");
		}
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
		String currentThreadName = Thread.currentThread().getName();
		if(isExecuting.get()) {
			LogUtil.warn("abort execute for {}, request pool is executing, should not call execute more than once", currentThreadName);
			return ;
		}
		size = requestSet.size();
		CompletableFuture<Void> completableFuture;
		List<CompletableFuture<Void>> futureList = new LinkedList<>();
		Instant begin = Instant.now();
		for (RequestWrapper request : requestSet) {
			completableFuture = CompletableFuture.runAsync(new PooledTask(request.getRequestClient(), 
					request.getCallback()), threadPool);
			futureList.add(completableFuture);
		}
		completableFuture = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[requestSet.size()]));
		completableFuture.whenComplete((v, u) -> {
			if (Objects.isNull(u)) {
				long timeUsed = ChronoUnit.MILLIS.between(begin, Instant.now());
				LogUtil.info("total execute time {} s", timeUsed * 1.0 / 1000);
			} else {
				u.printStackTrace();
			}
		});
		isExecuting.set(false);
		requestSet.clear();
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
	
	private class RequestWrapper implements Comparable<RequestWrapper> {
		private long id;
		private Request request;
		private Client requestClient;
		private CallBack callback;
		
		private String requestUrl;
		private String requestParam;
		
		public RequestWrapper(Client requestClient, CallBack callback) {
			Objects.requireNonNull(requestClient, "null requestClient");
			Objects.requireNonNull(callback, "null callback");
			this.id = new UniqueIDUtil(0, 0).nextId();
			this.request = Optional.ofNullable(requestClient.getRequest())
					.orElseThrow(() -> new RuntimeException("have not set request for client:" + requestClient.getClass().getName()));
			this.requestClient = requestClient;
			this.callback = callback;
			this.requestUrl = request.getRequestUrl();
			this.requestParam = request.getParamsJsonString();
		}
		
		public long getId() {
			return id;
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

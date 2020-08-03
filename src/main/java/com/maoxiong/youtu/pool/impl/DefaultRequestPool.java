package com.maoxiong.youtu.pool.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.entity.result.BaseResult;
import com.maoxiong.youtu.initializer.Initializer;
import com.maoxiong.youtu.internal.datastructure.ConcurrentHashSet;
import com.maoxiong.youtu.pool.RequestPool;
import com.maoxiong.youtu.util.CommonUtil;
import com.maoxiong.youtu.util.LogUtil;

/**
 * 
 * @author yanrun
 *
 */
public class DefaultRequestPool implements RequestPool {
	
	private final AtomicInteger threadSequance = new AtomicInteger();
	private final ExecutorService threadPool;
	private final Set<RequestWrapper> requestSet;
	private final boolean shouldShutdownAfterExecution;
	
	private volatile boolean isClosed;
	private volatile AtomicBoolean isExecuting = new AtomicBoolean(false);
	
	public DefaultRequestPool() {
		this(true);
	}
	
	public DefaultRequestPool(boolean shouldShutdownAfterExecution) {
		Initializer.initCheck();
		this.threadPool = new ThreadPoolExecutor(10, 50, 1, TimeUnit.MINUTES, 
				new LinkedBlockingQueue<>(50), 
				(r) -> new Thread(r, "ThreadPool thread: "  + threadSequance.incrementAndGet()));
		this.requestSet = new ConcurrentHashSet<>();
		this.shouldShutdownAfterExecution = shouldShutdownAfterExecution;
		Context.REQUEAT_POOL_QUEUE.offer(this);
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
		if (shouldShutdownAfterExecution) {
			close();
		}
	}
	
	@Override
	public void cancel() {
		threadPool.shutdown();
	}
	
	@Override
	public void close() {
		if (!threadPool.isShutdown() || !threadPool.isTerminated()) {
			threadPool.shutdown();
		}
		isClosed = true;
		Context.REQUEAT_POOL_QUEUE.poll();
		isExecuting.set(false);
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

	@Override
	public void addRequest(Client requestClient, CallBack<? extends BaseResult> callback) {
		checkBeforeAdd();
		RequestWrapper wrapper = new RequestWrapper(requestClient, callback);
		boolean added = requestSet.add(wrapper);
		String currentThreadName = Thread.currentThread().getName();
		if(added) {
			int size = requestSet.size();
			LogUtil.info("added request: {} by {} total {}", 
					requestClient.getClass().getSimpleName().replace("Client", "request").concat(String.valueOf(wrapper.getId())), 
					currentThreadName, size + CommonUtil.singularOrPlural(size, " request", " requests"));
		}
	}
	
}

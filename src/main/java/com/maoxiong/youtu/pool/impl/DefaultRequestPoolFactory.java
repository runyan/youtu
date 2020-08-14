package com.maoxiong.youtu.pool.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.maoxiong.youtu.util.LogUtil;

/**
 * 
 * @author yanrun
 *
 */
public class DefaultRequestPoolFactory {
	
	private static final Map<Boolean, DefaultRequestPoolObjectFactory> CACHED_FACTORIES = new ConcurrentHashMap<>(4);
	private static final Map<DefaultRequestPoolObjectFactory, PoolForRequestPool> CACHED_POOLS = new ConcurrentHashMap<>(16);
	
	public static DefaultRequestPool getDefaultRequestPool() {
		return getDefaultRequestPool(true, null);
	}
	
	public static DefaultRequestPool getDefaultRequestPool(boolean shouldShutdownAfterExecution) {
		return getDefaultRequestPool(shouldShutdownAfterExecution, null);
	}
	
	public static DefaultRequestPool getDefaultRequestPool(boolean shouldShutdownAfterExecution, GenericObjectPoolConfig<DefaultRequestPool> config) {
		DefaultRequestPoolObjectFactory factory = CACHED_FACTORIES
				.computeIfAbsent(shouldShutdownAfterExecution, v -> new DefaultRequestPoolObjectFactory(shouldShutdownAfterExecution));
		PoolForRequestPool pool = CACHED_POOLS
				.computeIfAbsent(factory, v -> new PoolForRequestPool(factory, config));
		try {
			return pool.borrowObject();
		} catch (Exception e) {
			LogUtil.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
}

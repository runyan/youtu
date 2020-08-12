package com.maoxiong.youtu.pool.impl;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.maoxiong.youtu.util.LogUtil;

/**
 * 
 * @author yanrun
 *
 */
public class DefaultRequestPoolFactory {
	
	private static final Map<Boolean, DefaultRequestPoolObjectFactory> CACHED_FACTORIES = new ConcurrentHashMap<>();
	private static final Map<DefaultRequestPoolObjectFactory, PoolForRequestPool> CACHED_POOLS = new ConcurrentHashMap<>();
	
	public static DefaultRequestPool getDefaultRequestPool(boolean shouldShutdownAfterExecution) {
		return getDefaultRequestPool(shouldShutdownAfterExecution, null);
	}
	
	public static DefaultRequestPool getDefaultRequestPool(boolean shouldShutdownAfterExecution, GenericObjectPoolConfig<DefaultRequestPool> config) {
		DefaultRequestPoolObjectFactory factory = CACHED_FACTORIES.get(shouldShutdownAfterExecution);
		if (Objects.isNull(factory)) {
			factory = new DefaultRequestPoolObjectFactory(shouldShutdownAfterExecution);
			CACHED_FACTORIES.put(shouldShutdownAfterExecution, factory);
		}
		PoolForRequestPool pool = CACHED_POOLS.get(factory);
		if (Objects.isNull(pool)) {
			pool = new PoolForRequestPool(factory, config);
			CACHED_POOLS.put(factory, pool);
		}
		try {
			return pool.borrowObject();
		} catch (Exception e) {
			LogUtil.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
}

package com.maoxiong.youtu.pool.impl;

import java.util.Objects;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * 
 * @author yanrun
 *
 */
class DefaultRequestPoolObjectFactory extends BasePooledObjectFactory<DefaultRequestPool> {

	private boolean shouldShutdownAfterExecution = true;
	private PoolForRequestPool pool;
	
	public DefaultRequestPoolObjectFactory(boolean shouldShutdownAfterExecution) {
		this.shouldShutdownAfterExecution = shouldShutdownAfterExecution;
	}

	@Override
	public DefaultRequestPool create() throws Exception {
		DefaultRequestPool defaultRequestPool = new DefaultRequestPool(shouldShutdownAfterExecution);
		defaultRequestPool.setCreatePool(pool);
		return defaultRequestPool;
	}

	@Override
	public PooledObject<DefaultRequestPool> wrap(DefaultRequestPool pool) {
		return new DefaultPooledObject<>(pool);
	}
	
	public void setPool(PoolForRequestPool pool) {
		this.pool = pool;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (shouldShutdownAfterExecution ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (Objects.isNull(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DefaultRequestPoolObjectFactory other = (DefaultRequestPoolObjectFactory) obj;
		if (Objects.isNull(pool)) {
			if (Objects.nonNull(other.pool)) {
				return false;
			}
		} else if (!pool.equals(other.pool)) {
			return false;
		}
		if (shouldShutdownAfterExecution != other.shouldShutdownAfterExecution) {
			return false;
		}
		return true;
	}
}

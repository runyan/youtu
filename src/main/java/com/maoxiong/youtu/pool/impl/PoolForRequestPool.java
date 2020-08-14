package com.maoxiong.youtu.pool.impl;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 
 * @author yanrun
 *
 */
class PoolForRequestPool extends GenericObjectPool<DefaultRequestPool> {
	
	private PooledObjectFactory<DefaultRequestPool> factory;
	private GenericObjectPoolConfig<DefaultRequestPool> config;
	
	PoolForRequestPool(PooledObjectFactory<DefaultRequestPool> factory,
			GenericObjectPoolConfig<DefaultRequestPool> config) {
		super(factory, Optional.ofNullable(config).orElseGet(PoolForRequestPool::getDefaultConfig));
		this.factory = factory;
		this.config = config;
		if (factory instanceof DefaultRequestPoolObjectFactory) {
			((DefaultRequestPoolObjectFactory) factory).setPool(this);
		}
	}
	
	private static GenericObjectPoolConfig<DefaultRequestPool> getDefaultConfig() {
		GenericObjectPoolConfig<DefaultRequestPool> config = new GenericObjectPoolConfig<>();
		config.setMaxTotal(50);
		config.setMaxWaitMillis(1000);
		return config;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (Objects.isNull(config) ? 0 : config.hashCode());
		result = prime * result + (Objects.isNull(factory) ? 0 : factory.hashCode());
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
		PoolForRequestPool other = (PoolForRequestPool) obj;
		if (Objects.isNull(config)) {
			if (Objects.nonNull(other.config)) {
				return false;
			}
		} else if (!config.equals(other.config)) {
			return false;
		}
		if (Objects.isNull(factory)) {
			if (Objects.nonNull(other.factory)) {
				return false;
			}
		} else if (!factory.equals(other.factory)) {
			return false;
		}
		return true;
	}
	
}

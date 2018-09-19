package com.maoxiong.youtu.cache.impl;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.maoxiong.youtu.cache.Cache;

public class CaffeineCache<K, V> implements Cache<K, V> {
	
	private com.github.benmanes.caffeine.cache.Cache<K, V> cache;
	
	public CaffeineCache() {
		cache = Caffeine.newBuilder()
				.expireAfterWrite(10, TimeUnit.MINUTES)
			    .maximumSize(16)
			    .build();
	}

	@Override
	public V getIfPresent(K key) {
		return cache.getIfPresent(key);
	}

	@Override
	public V get(K key, Function<? super K, ? extends V> supplier) {
		return cache.get(key, supplier);
	}

	@Override
	public void set(K key, V value) {
		cache.put(key, value);
	}

	@Override
	public int getSize() {
		return (int) cache.estimatedSize();
	}

	@Override
	public void invalidate(K key) {
		cache.invalidate(key);
	}

	@Override
	public void invalidateAll() {
		cache.invalidateAll();
	}

	@Override
	public void cleanUp() {
		cache.cleanUp();
	}

}
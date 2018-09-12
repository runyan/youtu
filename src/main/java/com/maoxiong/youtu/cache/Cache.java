package com.maoxiong.youtu.cache;

import java.util.function.Function;

public interface Cache<K, V> {
	
	V getIfPresent(K key);
	V get(K key, Function<? super K, ? extends V> supplier);
	void set(K key, V value);
	int getSize();
	void invalidate(K key);
	void invalidateAll();
	void cleanUp();

}

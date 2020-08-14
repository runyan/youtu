package com.maoxiong.youtu.cache;

import java.util.function.Function;

/**
 * 
 * @author yanrun
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public interface Cache<K, V> {
	
	/**
	 * 根据缓存键从缓存中获取值
	 * 
	 * @param key 缓存键
	 * @return 缓存值
	 */
	V getIfPresent(K key);
	/**
	 * 根据缓存键从缓存中获取值
	 * 
	 * @param key 缓存键
	 * @param supplier 计算函数
	 * @return 缓存值
	 */
	V get(K key, Function<? super K, ? extends V> supplier);
	/**
	 * 设置缓存值
	 * 
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	void set(K key, V value);
	/**
	 * 获取缓存键值对个数
	 * 
	 * @return 缓存键值对个数
	 */
	int getSize();
	/**
	 * 根据缓存键失效缓存
	 * 
	 * @param key 缓存键
	 */
	void invalidate(K key);
	/**
	 * 失效全部缓存
	 */
	void invalidateAll();
	/**
	 * 清楚所有缓存
	 */
	void cleanUp();

}

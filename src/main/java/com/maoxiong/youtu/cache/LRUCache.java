package com.maoxiong.youtu.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LRUCache<K, V> {
	
    private final int MAX_CACHE_SIZE;
    private final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Map<K, V> map;

    public LRUCache(int cacheSize) {
        MAX_CACHE_SIZE = cacheSize;
        int capacity = (int) Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTOR) + 1;
        /*
         * 第三个参数设置为true，代表linkedlist按访问顺序排序，可作为LRU缓存
         * 第三个参数设置为false，代表按插入顺序排序，可作为FIFO缓存
         */
        map = Collections.synchronizedMap(new LinkedHashMap<K, V>(capacity, DEFAULT_LOAD_FACTOR, true) {
			private static final long serialVersionUID = 8385908577610959752L;

			@Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        });
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public void remove(K key) {
        map.remove(key);
    }

    public Set<Map.Entry<K, V>> getAll() {
        return map.entrySet();
    }
    
    public boolean containsKey(Object key) {
    	return map.containsKey(key);
    }
    
    public int size() {
    	return map.size();
    }
    
    @Override
    public int hashCode() {
    	return map.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> {
        	stringBuilder.append(String.format("%s: %s  ", key, value));
        });
        return stringBuilder.toString();
    }
    
}

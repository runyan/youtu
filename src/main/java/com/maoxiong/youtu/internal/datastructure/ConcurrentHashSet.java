package com.maoxiong.youtu.internal.datastructure;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;


public class ConcurrentHashSet<T> implements Set<T> {

	private final static Object PRESENT = new Object();
	private final ConcurrentHashMap<T, Object> MAP;
	
	public ConcurrentHashSet() {
		this(16);
	}
	
	public ConcurrentHashSet(int capacity) {
		MAP = new ConcurrentHashMap<>(capacity);
	}
	
	@Override
	public int size() {
		return MAP.size();
	}

	@Override
	public boolean isEmpty() {
		return MAP.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return MAP.containsKey(o);
	}

	@Override
	public Iterator<T> iterator() {
		return keySet().iterator();
	}

	@Override
	public Object[] toArray() {
		return keySet().toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return keySet().toArray(a);
	}

	@Override
	public boolean add(T e) {
		return Objects.isNull(MAP.put(e, PRESENT));
	}

	@Override
	public boolean remove(Object o) {
		return MAP.remove(o, PRESENT);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return keySet().containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean modified = false;
		Iterator<T> it = iterator();
		while(it.hasNext()) {
			modified = add(it.next());
		}
		return modified;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return keySet().retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Objects.requireNonNull(c);
		boolean modified = false;
		Iterator<?> itor = iterator();
		while(itor.hasNext()) {
			if(c.contains(itor.next())) {
				itor.remove();
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public void clear() {
		MAP.clear();
	}
	
	private KeySetView<T, Object> keySet() {
		return MAP.keySet();
	}
}

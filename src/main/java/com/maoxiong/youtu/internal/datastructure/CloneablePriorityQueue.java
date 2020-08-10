package com.maoxiong.youtu.internal.datastructure;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.SortedSet;

/**
 * 
 * @author yanrun
 *
 * @param <E> the type of elements held in this collection
 */
public class CloneablePriorityQueue<E> extends AbstractQueue<E> implements Serializable, Cloneable {

	private static final long serialVersionUID = -1699908960172188097L;

	private static final int DEFAULT_INITIAL_CAPACITY = 11;

	private PriorityQueue<E> priorityQueue;

	public CloneablePriorityQueue() {
		this(DEFAULT_INITIAL_CAPACITY, null);
	}

	public CloneablePriorityQueue(Comparator<? super E> comparator) {
		this(DEFAULT_INITIAL_CAPACITY, comparator);
	}

	public CloneablePriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
		priorityQueue = new PriorityQueue<>(initialCapacity, comparator);
	}
	
	public CloneablePriorityQueue(Collection<? extends E> c) {
		priorityQueue = new PriorityQueue<>(c);
	}
	
	public CloneablePriorityQueue(SortedSet<? extends E> c) {
		priorityQueue = new PriorityQueue<>(c);
	}

	@Override
	public boolean offer(E e) {
		return priorityQueue.offer(e);
	}

	@Override
	public E poll() {
		return priorityQueue.poll();
	}

	@Override
	public E peek() {
		return priorityQueue.peek();
	}

	@Override
	public Iterator<E> iterator() {
		return priorityQueue.iterator();
	}

	@Override
	public int size() {
		return priorityQueue.size();
	}

	@Override
	public Object clone() {
		return new CloneablePriorityQueue<>(priorityQueue);
	}
	
}

package com.maoxiong.youtu.pool;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;

public abstract class AbstractRequestPool {
	
	protected volatile int size = 0;
	
	protected abstract void addRequest(Client requestClient, CallBack callback);
	protected abstract void cancel();
	protected abstract void execute();
	protected abstract void close();
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}

}

package com.maoxiong.youtu.pool.impl;

import java.util.Objects;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;

/**
 * 
 * @author yanrun
 *
 */
class PooledTask implements Runnable {

	private Client client;
	private CallBack callback;
	
	public PooledTask(Client client, CallBack callback) {
		Objects.requireNonNull(client);
		Objects.requireNonNull(callback);
		this.client = client;
		this.callback = callback;
	}

	@Override
	public void run() {
		try {
			client.execute(callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

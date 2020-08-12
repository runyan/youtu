package com.maoxiong.youtu.pool.impl.internal;

import java.util.Objects;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.entity.result.BaseResult;

/**
 * 
 * @author yanrun
 *
 */
public class PooledTask implements Runnable {

	private Client client;
	private CallBack<? extends BaseResult> callback;

	public PooledTask(Client client, CallBack<? extends BaseResult> callback) {
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

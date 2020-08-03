package com.maoxiong.youtu.pool;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.entity.result.BaseResult;

/**
 * 
 * @author yanrun
 *
 */
public interface RequestPool {
	
	void addRequest(Client requestClient, CallBack<? extends BaseResult> callback);
	void cancel();
	void execute();
	void close();
	int size(); 
	boolean isClosed();
	boolean isEmpty();

}

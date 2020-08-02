package com.maoxiong.youtu.pool;

import java.util.Map;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;

/**
 * 
 * @author yanrun
 *
 */
public interface RequestPool {
	
	void addRequest(Client requestClient, CallBack callback);
	void cancel();
	void execute();
	void close();
	void addRequestsByMap(Map<Client, CallBack> requestMap);
	int size(); 
	boolean isClosed();
	boolean isEmpty();

}

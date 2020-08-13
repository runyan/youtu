package com.maoxiong.youtu.pool;

import java.io.Closeable;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.entity.result.BaseResult;

/**
 * 
 * @author yanrun
 *
 */
public interface RequestPool extends Closeable {
	
	/**
	 * 添加请求
	 * 
	 * @param requestClient 请求Client
	 * @param callback 回调函数
	 */
	void addRequest(Client requestClient, CallBack<? extends BaseResult> callback);
	/**
	 * 取消执行
	 */
	void cancel();
	/**
	 * 执行请求
	 */
	void execute();
	/**
	 * 关闭请求池
	 */
	void close();
	/**
	 * 获取请求池的大小
	 * 
	 * @return 请求池的大小
	 */
	int size(); 
	/**
	 * 请求池是否关闭
	 * 
	 * @return 请求池是否关闭
	 */
	boolean isClosed();
	/**
	 * 请求池是否为空
	 * 
	 * @return 请求池是否为空
	 */
	boolean isEmpty();

}

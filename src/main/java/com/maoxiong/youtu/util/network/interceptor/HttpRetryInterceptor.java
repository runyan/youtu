package com.maoxiong.youtu.util.network.interceptor;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Objects;

import com.maoxiong.youtu.util.LogUtil;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * @author yanrun
 *
 */
public class HttpRetryInterceptor implements Interceptor {
	
	private final int executionCount;
	private final long retryInterval;
	
	public HttpRetryInterceptor(Builder builder) {
		this.executionCount = builder.executionCount;
		this.retryInterval = builder.retryInterval;
	}

	@Override
	public Response intercept(Chain chain) {
		Request request = chain.request();  
		int retryNum = 0;  
		Response response = null;
		try {
        	response = chain.proceed(request); 
            synchronized (response) {
            	while (( Objects.isNull(response) || !response.isSuccessful()) && retryNum < executionCount) {  
            		LogUtil.warn("intercept Request is not successful - {}", (retryNum + 1));  
                    final long nextInterval = getRetryInterval();  
                    try {  
                    	LogUtil.warn("Wait for {}ms", nextInterval);  
                        Thread.sleep(nextInterval);  
                    } catch (final InterruptedException e) {  
                        Thread.currentThread().interrupt();  
                        throw new InterruptedIOException("thread interrputed while retrying");  
                    }  
                    retryNum++;  
                    // retry the request  
                    response = chain.proceed(request); 
                }  
			}
        } catch(IOException e) {
        	e.printStackTrace();
        	LogUtil.error("got an error while retrying");
        }
        return response;  
	}
	
	public long getRetryInterval() {  
        return this.retryInterval;  
    }  
	
	public static final class Builder {  
        private int executionCount;  
        private long retryInterval;  
        
        public Builder() {  
            executionCount = 3;  
            retryInterval = 1000;  
        }  
  
        public Builder executionCount(int executionCount){  
            this.executionCount = executionCount;  
            return this;  
        }  
  
        public Builder retryInterval(long retryInterval){  
            this.retryInterval = retryInterval;  
            return this;  
        }  
        
        public HttpRetryInterceptor build() {  
            return new HttpRetryInterceptor(this);  
        }  
    }  

}

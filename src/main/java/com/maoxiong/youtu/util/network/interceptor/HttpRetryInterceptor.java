package com.maoxiong.youtu.util.network.interceptor;

import java.io.IOException;
import java.io.InterruptedIOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * @author yanrun
 *
 */
public class HttpRetryInterceptor implements Interceptor {
	
	private static final Logger logger = LogManager.getLogger(HttpRetryInterceptor.class);
	
	private final int executionCount;
	private final long retryInterval;
	
	public HttpRetryInterceptor(Builder builder) {
		this.executionCount = builder.executionCount;
		this.retryInterval = builder.retryInterval;
	}

	@SuppressWarnings("resource")
	@Override
	public Response intercept(Chain chain) {
		Request request = chain.request();  
		int retryNum = 0;  
		Response response = null;
		try {
        	response = chain.proceed(request); 
            synchronized (response) {
            	while ((response == null || !response.isSuccessful()) && retryNum < executionCount) {  
                	logger.warn("intercept Request is not successful - " + (retryNum + 1));  
                    final long nextInterval = getRetryInterval();  
                    try {  
                    	logger.warn("Wait for " + nextInterval + "ms");  
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
        	logger.error("got an error while retrying");
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

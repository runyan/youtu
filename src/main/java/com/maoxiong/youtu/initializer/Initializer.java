package com.maoxiong.youtu.initializer;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.util.SignUtil;

public class Initializer {
	
	private static volatile boolean isInited = false;
	
	private String QQ;
	private String appId;
	private String secretId;
	private String secretKey;
	
	public Initializer(Builder builder) {
		this.QQ = builder.QQ;
		this.appId = builder.appId;
		this.secretId = builder.secretId;
		this.secretKey = builder.secretKey;
		String builderFilePath = builder.fileSavePath;
		Context.set("savePath", StringUtils.isBlank(builderFilePath) ? System.getProperty("user.dir") : builderFilePath);
	}
	
	public void init() {
		if(isInited) {
			throw new RuntimeException("should not init more than once");
		}
		if(StringUtils.isBlank(QQ)) {
			throw new IllegalArgumentException("need QQ");
		}
		if(StringUtils.isBlank(appId)) {
			throw new IllegalArgumentException("need appID");
		}
		if(StringUtils.isBlank(secretId)) {
			throw new IllegalArgumentException("need secretID");
		}
		if(StringUtils.isBlank(secretKey)) {
			throw new IllegalArgumentException("need secretKey");
		}
		Context.init(SignUtil.getSign(QQ, appId, secretId, secretKey), appId);
		isInited = true;
	}
	
	public static final class Builder {
		private String QQ;
		private String appId;
		private String secretId;
		private String secretKey;
		private String fileSavePath;
		
		public Builder() {
			super();
		}
		
		public Builder QQ(String qq) {
			this.QQ = qq;
			return this;
		}
		
		public Builder appId(String appId) {
			this.appId = appId;
			return this;
		}
		
		public Builder secretId(String secretId) {
			this.secretId = secretId;
			return this;
		}
		
		public Builder secretKey(String secretKey) {
			this.secretKey = secretKey;
			return this;
		}
		
		public Builder fileSavePath(String fileSavePath) {
			this.fileSavePath = fileSavePath;
			return this;
		}
		
		public Initializer bulid() {
			return new Initializer(this);
		}
	}
}

package com.maoxiong.youtu.initializer;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.util.SignUtil;
import com.maoxiong.youtu.util.configfile.ConfigFileUtil;
import com.maoxiong.youtu.util.configfile.PropertyUtil;

/**
 * 
 * @author yanrun
 *
 */
public class Initializer {

	private static volatile boolean initailized;

	private String QQ;
	private String appId;
	private String secretId;
	private String secretKey;

	public Initializer() {
		this("");
	}

	public Initializer(String propertiesFilePath) {
		this(constructBuilder(ConfigFileUtil.loadProperties(propertiesFilePath)));
	}

	public Initializer(Properties properties) {
		this(constructBuilder(properties));
	}

	public Initializer(Builder builder) {
		this.QQ = builder.QQ;
		this.appId = builder.appId;
		this.secretId = builder.secretId;
		this.secretKey = builder.secretKey;
		String builderFilePath = builder.fileSavePath;
		Context.set("savePath",
				StringUtils.isBlank(builderFilePath) ? System.getProperty("user.dir") : builderFilePath);
	}

	private static Initializer.Builder constructBuilder(Properties properties) {
		String qq = PropertyUtil.getPropertyValue(properties, "youtu.qq");
		String appId = PropertyUtil.getPropertyValue(properties, "youtu.appId");
		String secretId = PropertyUtil.getPropertyValue(properties, "youtu.secretId");
		String secretKey = PropertyUtil.getPropertyValue(properties, "youtu.secretKey");
		String fileSavePath = com.maoxiong.youtu.util.configfile.PropertyUtil.getPropertyValue(properties,
				"youtu.fileSavePath");
		Initializer.Builder builder = new Initializer.Builder().QQ(qq).appId(appId).secretId(secretId)
				.secretKey(secretKey).fileSavePath(fileSavePath);
		return builder;
	}

	public void init() {
		if (initailized) {
			throw new IllegalStateException("should not init more than once");
		}
		if (StringUtils.isBlank(QQ)) {
			throw new IllegalArgumentException("need QQ");
		}
		if (StringUtils.isBlank(appId)) {
			throw new IllegalArgumentException("need appID");
		}
		if (StringUtils.isBlank(secretId)) {
			throw new IllegalArgumentException("need secretID");
		}
		if (StringUtils.isBlank(secretKey)) {
			throw new IllegalArgumentException("need secretKey");
		}
		Context.init(SignUtil.getSign(QQ, appId, secretId, secretKey), appId);
		initailized = true;
	}

	public static void initCheck() {
		Object signObj = Context.get("sign");
		signObj = null == signObj ? "" : signObj;
		String sign = String.valueOf(signObj);
		if (StringUtils.isBlank(sign)) {
			throw new IllegalStateException("have not init properly");
		}
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

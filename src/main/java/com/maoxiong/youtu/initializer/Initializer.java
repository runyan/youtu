package com.maoxiong.youtu.initializer;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.annotation.ConfigAnnotationParser;
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

	private String qq;
	private String appId;
	private String secretId;
	private String secretKey;

	public Initializer() {
		this("");
	}

	public Initializer(String propertiesFilePath) {
		this(ConfigFileUtil.loadProperties(propertiesFilePath));
	}
	
	public Initializer(Class<?> configClass) {
		this(ConfigAnnotationParser.parseConfigProperties(configClass));
	}

	public Initializer(Properties properties) {
		this(PropertyUtil.getPropertyValue(properties, "youtu.qq"),
				PropertyUtil.getPropertyValue(properties, "youtu.appId"),
				PropertyUtil.getPropertyValue(properties, "youtu.secretId"),
				PropertyUtil.getPropertyValue(properties, "youtu.secretKey"),
				PropertyUtil.getPropertyValue(properties, "youtu.fileSavePath"));
	}

	public Initializer(Builder builder) {
		this(builder.qq, builder.appId, builder.secretId, builder.secretKey, builder.fileSavePath);
	}

	private Initializer(String qq, String appId, String secretId, String secretKey, String fileSavePath) {
		this.qq = qq;
		this.appId = appId;
		this.secretId = secretId;
		this.secretKey = secretKey;
		String builderFilePath = fileSavePath;
		Context.set("savePath",
				StringUtils.isBlank(builderFilePath) ? System.getProperty("user.dir") : builderFilePath);
	}

	public void init() {
		if (initailized) {
			throw new IllegalStateException("should not init more than once");
		}
		if (StringUtils.isBlank(qq)) {
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
		Context.init(SignUtil.getSign(qq, appId, secretId, secretKey), appId);
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
		private String qq;
		private String appId;
		private String secretId;
		private String secretKey;
		private String fileSavePath;

		public Builder() {
			super();
		}

		public Builder qq(String qq) {
			this.qq = qq;
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

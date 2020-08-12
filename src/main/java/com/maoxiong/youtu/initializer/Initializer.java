package com.maoxiong.youtu.initializer;

import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.annotation.parser.ConfigAnnotationParser;
import com.maoxiong.youtu.constants.ConfigConstans;
import com.maoxiong.youtu.constants.ContextConstants;
import com.maoxiong.youtu.context.Context;
import com.maoxiong.youtu.util.ConfigFileUtil;
import com.maoxiong.youtu.util.SignUtil;

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
		String qq = System.getProperty(ConfigConstans.QQ);
		if (StringUtils.isNotBlank(qq)) {
			String appId = System.getProperty(ConfigConstans.APP_ID);
			String secretId = System.getProperty(ConfigConstans.SECRET_ID);
			String secretKey = System.getProperty(ConfigConstans.SECRET_KEY);
			String fileSavePath = System.getProperty(ConfigConstans.FILE_SAVE_PATH);
			setup(qq, appId, secretId, secretKey, fileSavePath);
		} else {
			Properties props = ConfigFileUtil.loadProperties(StringUtils.EMPTY);
			setup(String.valueOf(props.getOrDefault(ConfigConstans.QQ, StringUtils.EMPTY)),
					String.valueOf(props.getOrDefault(ConfigConstans.APP_ID, StringUtils.EMPTY)),
					String.valueOf(props.getOrDefault(ConfigConstans.SECRET_ID, StringUtils.EMPTY)),
					String.valueOf(props.getOrDefault(ConfigConstans.SECRET_KEY, StringUtils.EMPTY)),
					String.valueOf(props.getOrDefault(ConfigConstans.FILE_SAVE_PATH, StringUtils.EMPTY)));
		}
	}

	public Initializer(String propertiesFilePath) {
		this(ConfigFileUtil.loadProperties(propertiesFilePath));
	}

	public Initializer(Class<?> configClass) {
		this(ConfigAnnotationParser.parseConfigProperties(configClass));
	}

	public Initializer(Properties properties) {
		this(String.valueOf(properties.getOrDefault(ConfigConstans.QQ, StringUtils.EMPTY)),
				String.valueOf(properties.getOrDefault(ConfigConstans.APP_ID, StringUtils.EMPTY)),
				String.valueOf(properties.getOrDefault(ConfigConstans.SECRET_ID, StringUtils.EMPTY)),
				String.valueOf(properties.getOrDefault(ConfigConstans.SECRET_KEY, StringUtils.EMPTY)),
				String.valueOf(properties.getOrDefault(ConfigConstans.FILE_SAVE_PATH, StringUtils.EMPTY)));
	}

	public Initializer(Builder builder) {
		this(builder.qq, builder.appId, builder.secretId, builder.secretKey, builder.fileSavePath);
	}

	private Initializer(String qq, String appId, String secretId, String secretKey, String fileSavePath) {
		setup(qq, appId, secretId, secretKey, fileSavePath);
	}
	
	private void setup(String qq, String appId, String secretId, String secretKey, String fileSavePath) {
		this.qq = qq;
		this.appId = appId;
		this.secretId = secretId;
		this.secretKey = secretKey;
		String builderFilePath = fileSavePath;
		Context.set(ContextConstants.SAVE_PATH,
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
		Object signObj = Context.get(ContextConstants.SIGN);
		signObj = Objects.isNull(signObj) ? StringUtils.EMPTY : signObj;
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

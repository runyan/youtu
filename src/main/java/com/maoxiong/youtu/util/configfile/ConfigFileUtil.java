package com.maoxiong.youtu.util.configfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.cache.impl.LruCache;
import com.maoxiong.youtu.util.ClassUtil;

/**
 * 
 * @author yanrun
 *
 */
public class ConfigFileUtil {

	static final LruCache<String, Properties> PROPERTY_CACHE = new LruCache<>(64);
	
	private static final List<Configable> CONFIGABLE_LIST;
	private static final Map<String, Configable> SUFFIX_MAP;
	
	static {
		List<Class<?>> implClasses = 
				ClassUtil.getAllClassByInterface(Configable.class);
		CONFIGABLE_LIST = initializeConfigables(implClasses);
		Collections.sort(CONFIGABLE_LIST, new Comparator<Configable>() {

			@Override
			public int compare(Configable o1, Configable o2) {
				return o1.getPriority() - o2.getPriority();
			}
		});
		SUFFIX_MAP = new HashMap<>(16);
		CONFIGABLE_LIST.forEach(configable -> {
			SUFFIX_MAP.put(configable.getSupportFileSuffix(), configable);
		});
	}
	
	private ConfigFileUtil() {
		throw new RuntimeException("no constructor for you");
	}
	
	public static Properties loadProperties(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return loadFromDefaultFilePath();
		}
		return loadPropertiesFromFile(filePath);
	}
	
	private static Properties loadFromDefaultFilePath() {
		Properties props;
		for (Configable configable : CONFIGABLE_LIST) {
			props = configable.loadProperties(configable.getDefaultFilePath());
			if (!Objects.isNull(props)) {
				return props;
			}
		}
		throw new RuntimeException("no config file path provided, nor default config files provided");
	}
	
	private static Properties loadPropertiesFromFile(String filePath) {
		int last = StringUtils.lastIndexOf(filePath, ".");
		String suffix = StringUtils.substring(filePath, last + 1);
		Configable configable = SUFFIX_MAP.get(suffix);
		if (Objects.isNull(configable)) {
			throw new IllegalArgumentException("unsupportted file type");
		}
		return configable.loadProperties(filePath);
	}
	
	private static List<Configable> initializeConfigables(List<Class<?>> implClasses) {
		if (Objects.isNull(implClasses) || implClasses.isEmpty()) {
			return null;
		}
		List<Configable> retList = new ArrayList<>(implClasses.size());
		implClasses.forEach(clz -> {
			try {
				Configable configable = (Configable)clz.newInstance();
				retList.add(configable);
			} catch (InstantiationException | IllegalAccessException ignore) {
			}
		});
		return retList;
	}
	
}

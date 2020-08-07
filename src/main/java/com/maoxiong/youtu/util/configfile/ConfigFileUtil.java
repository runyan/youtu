package com.maoxiong.youtu.util.configfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;
import java.util.stream.Collectors;

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

	private static Queue<Configable> configableQueue;
	private static Map<String, Configable> configableSuffixMap = new HashMap<>(16);
	private static volatile boolean loaded;
	private static final Object LOCK = new Object();

	private ConfigFileUtil() {
		throw new RuntimeException("no constructor for you");
	}

	public static Properties loadProperties(String filePath) {
		synchronized (LOCK) {
			if (!loaded) {
				initializeConfigables(ClassUtil.getAllClassByInterface(Configable.class));
				configableQueue.forEach(configable -> {
					configableSuffixMap.put(configable.getSupportFileSuffix(), configable);
				});
				loaded = true;
			}
		}
		return StringUtils.isEmpty(filePath) ? loadPropertiesFromDefaultFilePath() : 
			loadPropertiesFromCustomFilePath(filePath);
	}

	private static Properties loadPropertiesFromDefaultFilePath() {
		Properties props;
		for (Configable configable : configableQueue) {
			props = configable.loadProperties(configable.getDefaultFilePath());
			if (Objects.nonNull(props)) {
				return props;
			}
		}
		throw new RuntimeException("no config file path provided, nor default config files provided");
	}

	private static Properties loadPropertiesFromCustomFilePath(String filePath) {
		int last = StringUtils.lastIndexOf(filePath, ".");
		String suffix = StringUtils.substring(filePath, last + 1);
		Configable configable = Optional.ofNullable(configableSuffixMap.get(suffix))
				.orElseThrow(() -> new IllegalArgumentException("unsupportted file type"));
		return configable.loadProperties(filePath);
	}

	private static void initializeConfigables(List<Class<?>> implClasses) {
		if (Objects.isNull(implClasses) || implClasses.isEmpty()) {
			throw new RuntimeException("no implementation class found");
		}
		configableQueue = new PriorityQueue<>((c1, c2) -> c1.getPriority() - c2.getPriority());
		List<Configable> configableList = implClasses.stream().map(clz -> {
			try {
				if (Configable.class.isAssignableFrom(clz)) {
					Configable configable = (Configable) clz.newInstance();
					return configable;
				}
			} catch (InstantiationException | IllegalAccessException ignore) {
			}
			return null;
		}).filter(configable -> Objects.nonNull(configable)).collect(Collectors.toList());
		configableQueue.addAll(configableList);
	}
	
}

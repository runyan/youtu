package com.maoxiong.youtu.util;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.annotation.parser.ConfigableLoaderAnnotaionParser;
import com.maoxiong.youtu.internal.datastructure.CloneablePriorityQueue;
import com.maoxiong.youtu.util.configloader.ConfigFileLoader;

/**
 * 
 * @author yanrun
 *
 */
public class ConfigFileUtil {

	private static final Map<Class<?>, ConfigFileLoader> INSTANTIATED_CONFIG_LOADER_MAP 
		= new HashMap<>(16);
	private static final Map<String, Class<?>> CONFIG_LOADER_CLASS_MAP 
		= new HashMap<>(16);
	private static CloneablePriorityQueue<Class<?>> sortedLoaderQueue;
	private static volatile boolean loaded;
	private static final Object LOCK = new Object();

	private ConfigFileUtil() {
		throw new RuntimeException("no constructor for you");
	}
	
	public static Properties loadProperties(String filePath) {
		if (!loaded) {
			synchronized (LOCK) {
				if (!loaded) {
					init();
					loaded = true;
				}
			}
		}
		return StringUtils.isEmpty(filePath) ? loadPropertiesFromDefaultFilePath() : 
			loadPropertiesFromCustomFilePath(filePath);
	}
	
	private static Properties loadPropertiesFromDefaultFilePath() {
		CloneablePriorityQueue<Class<?>> theQueue = sortedLoaderQueue.clone();
		Properties props;
		ConfigFileLoader loader;
		while (!theQueue.isEmpty()) {
			Class<?> clz = theQueue.poll();
			loader = INSTANTIATED_CONFIG_LOADER_MAP.computeIfAbsent(clz, 
					v -> instantiateConfigLoader(clz));
			props = loader.loadProperties(ConfigableLoaderAnnotaionParser.getDefaultFilePath(clz));
			if (Objects.nonNull(props)) {
				return props;
			}
		}
		throw new RuntimeException("no config file path provided, nor default config file provided");
	}
	
	private static Properties loadPropertiesFromCustomFilePath(String filePath) {
		int last = StringUtils.lastIndexOf(filePath, ".");
		String suffix = StringUtils.substring(filePath, last + 1);
		if (Objects.isNull(CONFIG_LOADER_CLASS_MAP.get(suffix))) {
			throw new IllegalArgumentException("unsupportted file type");
		}
		Class<?> clz = CONFIG_LOADER_CLASS_MAP.get(suffix);
		ConfigFileLoader loader = INSTANTIATED_CONFIG_LOADER_MAP.computeIfAbsent(clz, 
				v -> instantiateConfigLoader(clz));
		return loader.loadProperties(filePath);
	}
	
	private static ConfigFileLoader instantiateConfigLoader(Class<?> clz) {
		ConfigFileLoader loader = INSTANTIATED_CONFIG_LOADER_MAP.computeIfAbsent(clz, v -> {
			try {
				return (ConfigFileLoader)clz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException("cannot instantiate: " + clz.getName() + 
						", is it a sub class of " + ConfigFileLoader.class.getName() + "?");
			}
		});
		return loader;
	}

	private static void init() {
		List<Class<?>> configLoaderClasses = 
				ClassUtil.getAllClassByInterface(ConfigFileLoader.class);
		if (Objects.isNull(configLoaderClasses) || configLoaderClasses.isEmpty()) {
			throw new RuntimeException("no configuarion file class provided");
		}
		sortedLoaderQueue = new CloneablePriorityQueue<>(configLoaderClasses.size(), (c1, c2) -> {
			return ConfigableLoaderAnnotaionParser.getConfigFilePriority(c1)
					.compareTo(ConfigableLoaderAnnotaionParser.getConfigFilePriority(c2));
		});
		configLoaderClasses = configLoaderClasses.stream()
				.filter(Objects::nonNull)
				.filter(clz -> !Modifier.isAbstract(clz.getModifiers()) &&
						ConfigFileLoader.class.isAssignableFrom(clz))
				.distinct()
				.collect(Collectors.toList());
		configLoaderClasses.forEach(clz -> {
			String[] suffixs = ConfigableLoaderAnnotaionParser.getConfigLoaderSuffixs(clz);
			if (Objects.nonNull(suffixs) && suffixs.length > 0) {
				for (String suffix : suffixs) {
					suffix = StringUtils.deleteWhitespace(suffix);
					if (StringUtils.isNotEmpty(suffix)) {
						CONFIG_LOADER_CLASS_MAP.put(suffix, clz);
					}
				}
				sortedLoaderQueue.offer(clz);
			}
		});
	}
	
}
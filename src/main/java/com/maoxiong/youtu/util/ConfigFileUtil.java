package com.maoxiong.youtu.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.annotation.parser.ConfigableLoaderAnnotaionParser;
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
	private static Queue<Class<?>> SORTED_CONFIG_LOADER_CLASS_QUEUE;
	private static volatile boolean loaded;
	private static final Object LOCK = new Object();

	private ConfigFileUtil() {
		throw new RuntimeException("no constructor for you");
	}
	
	public static void main(String[] args) {
		System.out.println(loadProperties(""));
	}

	public static Properties loadProperties(String filePath) {
		synchronized (LOCK) {
			if (!loaded) {
				init();
				loaded = true;
			}
		}
		return StringUtils.isEmpty(filePath) ? loadPropertiesFromDefaultFilePath() : 
			loadPropertiesFromCustomFilePath(filePath);
	}
	
	private static Properties loadPropertiesFromDefaultFilePath() {
		Properties props;
		ConfigFileLoader loader;
		Class<?> clz;
		while (!SORTED_CONFIG_LOADER_CLASS_QUEUE.isEmpty()) {
			clz = SORTED_CONFIG_LOADER_CLASS_QUEUE.poll();
			loader = INSTANTIATED_CONFIG_LOADER_MAP.get(clz);
			if (Objects.isNull(loader)) {
				loader = instantiateConfigLoader(clz);
			}
			props = loader.loadProperties(loader.getDefaultFilePath());
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
		ConfigFileLoader loader = INSTANTIATED_CONFIG_LOADER_MAP.get(clz);
		if (Objects.isNull(loader)) {
			loader = instantiateConfigLoader(clz);
		}
		return loader.loadProperties(filePath);
	}
	
	private static ConfigFileLoader instantiateConfigLoader(Class<?> clz) {
		try {
			ConfigFileLoader loader = (ConfigFileLoader)clz.newInstance();
			INSTANTIATED_CONFIG_LOADER_MAP.put(clz, loader);
			return loader;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("cannot instantiate: " + clz.getName() + 
					", is it a sub class of " + ConfigFileLoader.class.getName() + "?");
		}
	}

	private static void init() {
		List<Class<?>> configFileClasses = 
				ClassUtil.getAllClassByInterface(ConfigFileLoader.class);
		if (Objects.isNull(configFileClasses) || configFileClasses.isEmpty()) {
			throw new RuntimeException("no configuarion file class provided");
		}
		SORTED_CONFIG_LOADER_CLASS_QUEUE = new PriorityQueue<>(configFileClasses.size(), (c1, c2) -> {
			return ConfigableLoaderAnnotaionParser.getConfigFilePriority(c1)
					.compareTo(ConfigableLoaderAnnotaionParser.getConfigFilePriority(c2));
		});
		configFileClasses = configFileClasses.stream()
				.filter(clz -> ConfigFileLoader.class.isAssignableFrom(clz))
				.collect(Collectors.toList());
		configFileClasses.forEach(clz -> {
			String suffix = ConfigableLoaderAnnotaionParser.getConfigLoaderSuffix(clz);
			if (StringUtils.isNotEmpty(suffix)) {
				CONFIG_LOADER_CLASS_MAP.put(suffix, clz);
				SORTED_CONFIG_LOADER_CLASS_QUEUE.offer(clz);
			}
			
		});
	}
	
}

package com.maoxiong.youtu.util;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * 获取接口的所有实现类 理论上也可以用来获取类的所有子类
 * 查询路径有限制，只局限于接口所在模块下，比如pandora-gateway,而非整个pandora（会递归搜索该文件夹下所以的实现类）
 * 路径中不可含中文，否则会异常。若要支持中文路径，需对该模块代码中url.getPath() 返回值进行urldecode.
 * 
 * @author yanrun
 *
 */
public class ClassUtil {
	
	private static final String TYPE_FILE = "file";
	private static final String TYPE_JAR = "jar";
	
	private ClassUtil() {
		throw new RuntimeException("no constructor for you");
	}

	public static List<Class<?>> getAllClassByInterface(Class<?> clazz) {
		// 判断是否是一个接口
		if (clazz.isInterface()) {
			try {
				List<Class<?>> allClass = getAllClass(clazz.getPackage().getName());
				/**
				 * 循环判断路径下的所有类是否实现了指定的接口 并且排除接口类自己
				 */
				List<Class<?>> list = allClass.stream().filter(clz -> {
					return clazz.isAssignableFrom(clz) && !clazz.equals(clz);
				}).collect(Collectors.toList());
				LogUtil.debug("class list size: {}", list.size());
				return list;
			} catch (Exception e) {
				LogUtil.error(e.getMessage());
				throw new RuntimeException(e.getMessage());
			}
		}
		return Collections.unmodifiableList(Collections.emptyList());
	}

	/**
	 * 从一个指定路径下查找所有的类
	 *
	 * @param packagename
	 */
	private static List<Class<?>> getAllClass(String packagename) {
		LogUtil.debug("packageName to search: {}", packagename);
		List<String> classNameList = getClassName(packagename);
		List<Class<?>> list = classNameList.stream()
				.filter(className -> StringUtils.isNotEmpty(className))
				.filter(className -> !StringUtils.contains(className.toLowerCase(), "test"))
				.map(className -> {
					try {
						return Class.forName(className, false, Thread.currentThread().getContextClassLoader());
					} catch (ClassNotFoundException e) {
						LogUtil.warn("load class from {} failed: {}", className, e.getMessage());
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		LogUtil.debug("find list size: {}", list.size());
		return list;
	}

	/**
	 * 获取某包下所有类
	 * 
	 * @param packageName 包名
	 * @return 类的完整名称
	 */
	public static List<String> getClassName(String packageName) {
		List<String> fileNames = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String packagePath = StringUtils.replace(packageName, ".", "/");
		URL url = loader.getResource(packagePath);
		if (Objects.nonNull(url)) {
			String type = url.getProtocol();
			LogUtil.debug("file type: {}", type);
			if (TYPE_FILE.equals(type)) {
				String fileSearchPath = url.getPath();
				LogUtil.debug("fileSearchPath: {}", fileSearchPath);
				fileSearchPath = StringUtils.substring(fileSearchPath, 0, StringUtils.indexOf(fileSearchPath, "/classes"));
				LogUtil.debug("fileSearchPath: {}", fileSearchPath);
				fileNames = getClassNameByFile(fileSearchPath);
			} else if (TYPE_JAR.equals(type)) {
				try {
					JarURLConnection jarUrlConnection = (JarURLConnection) url.openConnection();
					JarFile jarFile = jarUrlConnection.getJarFile();
					fileNames = getClassNameByJar(jarFile, packagePath);
				} catch (java.io.IOException e) {
					throw new RuntimeException("open Package URL failed：" + e.getMessage());
				}

			} else {
				throw new RuntimeException("file system not support! cannot load MsgProcessor！");
			}
		}
		return fileNames;
	}

	/**
	 * 从项目文件获取某包下所有类
	 * 
	 * @param filePath 文件路径
	 * @return 类的完整名称
	 */
	private static List<String> getClassNameByFile(String filePath) {
		List<String> myClassName = new ArrayList<>();
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				myClassName.addAll(getClassNameByFile(childFile.getPath()));
			} else {
				String childFilePath = childFile.getPath();
				if (StringUtils.endsWith(childFilePath, ".class")) {
					childFilePath = StringUtils.substring(childFilePath, StringUtils.indexOf(childFilePath, "\\classes") + 9,
							StringUtils.lastIndexOf(childFilePath, "."));
					childFilePath = StringUtils.replace(childFilePath, "\\", ".");
					myClassName.add(childFilePath);
				}
			}
		}

		return myClassName;
	}

	/**
	 * 从jar获取某包下所有类
	 * 
	 * @return 类的完整名称
	 */
	private static List<String> getClassNameByJar(JarFile jarFile, String packagePath) {
		List<String> myClassName = new ArrayList<>();
		try {
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (StringUtils.endsWith(entryName, ".class")) {
					entryName = StringUtils.substring(StringUtils.replace(entryName, "/", "."), 0, StringUtils.lastIndexOf(entryName, "."));
					myClassName.add(entryName);
				}
			}
		} catch (Exception e) {
			LogUtil.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		return myClassName;
	}
}

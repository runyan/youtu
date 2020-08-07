package com.maoxiong.youtu.util;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
		List<Class<?>> list = new ArrayList<>();
		// 判断是否是一个接口
		if (clazz.isInterface()) {
			try {
				List<Class<?>> allClass = getAllClass(clazz.getPackage().getName());
				/**
				 * 循环判断路径下的所有类是否实现了指定的接口 并且排除接口类自己
				 */
				for (int i = 0; i < allClass.size(); i++) {
					/**
					 * 判断是不是同一个接口
					 */
					// isAssignableFrom:判定此 Class 对象所表示的类或接口与指定的 Class
					// 参数所表示的类或接口是否相同，或是否是其超类或超接口
					if (clazz.isAssignableFrom(allClass.get(i))) {
						if (!clazz.equals(allClass.get(i))) {
							// 自身并不加进去
							list.add(allClass.get(i));
						}
					}
				}
			} catch (Exception e) {
				LogUtil.error(e.getMessage());
				throw new RuntimeException(e.getMessage());
			}
		}
		LogUtil.info("class list size :" + list.size());
		return list;
	}

	/**
	 * 从一个指定路径下查找所有的类
	 *
	 * @param packagename
	 */
	private static List<Class<?>> getAllClass(String packagename) {
		LogUtil.info("packageName to search：" + packagename);
		List<String> classNameList = getClassName(packagename);
		List<Class<?>> list = new ArrayList<>();

		for (String className : classNameList) {
			try {
				list.add(Class.forName(className));
			} catch (ClassNotFoundException e) {
				LogUtil.error("load class from name failed:" + className + e.getMessage());
				throw new RuntimeException("load class from name failed:" + className + e.getMessage());
			}
		}
		LogUtil.info("find list size :" + list.size());
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
		String packagePath = packageName.replace(".", "/");
		URL url = loader.getResource(packagePath);
		if (url != null) {
			String type = url.getProtocol();
			LogUtil.debug("file type : " + type);
			if (type.equals(TYPE_FILE)) {
				String fileSearchPath = url.getPath();
				LogUtil.debug("fileSearchPath: " + fileSearchPath);
				fileSearchPath = fileSearchPath.substring(0, fileSearchPath.indexOf("/classes"));
				LogUtil.debug("fileSearchPath: " + fileSearchPath);
				fileNames = getClassNameByFile(fileSearchPath);
			} else if (type.equals(TYPE_JAR)) {
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
		List<String> myClassName = new ArrayList<String>();
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				myClassName.addAll(getClassNameByFile(childFile.getPath()));
			} else {
				String childFilePath = childFile.getPath();
				if (childFilePath.endsWith(".class")) {
					childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9,
							childFilePath.lastIndexOf("."));
					childFilePath = childFilePath.replace("\\", ".");
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
		List<String> myClassName = new ArrayList<String>();
		try {
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(".class")) {
					entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
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

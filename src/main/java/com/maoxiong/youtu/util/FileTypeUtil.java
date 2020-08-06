package com.maoxiong.youtu.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author yanrun
 *
 */
public class FileTypeUtil {

	/**
	 * 缓存文件头信息-文件头信息
	 */
	public static final Map<String, String> FILE_TYPES = new HashMap<>(64);

	static {
		// images
		FILE_TYPES.put("FFD8FF", "jpg");
		FILE_TYPES.put("89504E47", "png");
		FILE_TYPES.put("47494638", "gif");
		FILE_TYPES.put("49492A00", "tif");
		FILE_TYPES.put("424D", "bmp");
		// files
		// CAD
		FILE_TYPES.put("41433130", "dwg"); 
		FILE_TYPES.put("38425053", "psd");
		// 日记本
		FILE_TYPES.put("7B5C727466", "rtf"); 
		FILE_TYPES.put("3C3F786D6C", "xml");
		FILE_TYPES.put("68746D6C3E", "html");
		// 邮件
		FILE_TYPES.put("44656C69766572792D646174653A", "eml");
		FILE_TYPES.put("D0CF11E0", "doc");
		FILE_TYPES.put("5374616E64617264204A", "mdb");
		FILE_TYPES.put("252150532D41646F6265", "ps");
		FILE_TYPES.put("255044462D312E", "pdf");
		FILE_TYPES.put("504B03040A00000000008", "docx");
		// zip 压缩文件
		FILE_TYPES.put("504B0304", "zip");
		FILE_TYPES.put("52617221", "rar");
		FILE_TYPES.put("57415645", "wav");
		FILE_TYPES.put("41564920", "avi");
		FILE_TYPES.put("2E524D46", "rm");
		FILE_TYPES.put("000001BA", "mpg");
		FILE_TYPES.put("000001B3", "mpg");
		FILE_TYPES.put("6D6F6F76", "mov");
		FILE_TYPES.put("3026B2758E66CF11", "asf");
		FILE_TYPES.put("4D546864", "mid");
		FILE_TYPES.put("1F8B08", "gz");
	}

	public FileTypeUtil() {
		throw new RuntimeException("no constructor for you");
	}

	/**
	 * 根据文件路径获取文件类型
	 *
	 * @param filePath 文件路径
	 * @return 文件类型
	 */
	public static String getFileType(String filePath) {
		String value = getFileHeader(filePath);
		String result = "";
		Set<Entry<String, String>> entrySet = FILE_TYPES.entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (value.startsWith(entry.getKey())) {
				result = entry.getValue();
				break;
			}
		}
		return result;
	}

	/**
	 * 根据文件路径获取文件头信息
	 *
	 * @param filePath 文件路径
	 * @return 文件头信息
	 */
	public static String getFileHeader(String filePath) {
		String value = "";
		try {
			byte[] b = Files.readAllBytes(Paths.get(filePath));
			value = bytesToHexString(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 将要读取文件头信息的文件的byte数组转换成string类型表示
	 *
	 * @param src 要读取文件头信息的文件的byte数组
	 * @return 文件头十六进制信息
	 */
	private static String bytesToHexString(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		String hv;
		for (byte b : src) {
			// 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
			hv = Integer.toHexString(b & 0xFF).toUpperCase();
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder.toString();
	}
}

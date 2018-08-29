package com.maoxiong.youtu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import com.maoxiong.youtu.initializer.Initializer;

/**
 * 
 * @author yanrun
 *
 */
public class FileUtil {

	/**
	 * 读取文件内容，作为字符串返回
	 */
	public static String readFileAsString(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		if(!Files.exists(path)) {
			throw new FileNotFoundException(filePath);
		}
		long fileLength = Files.size(path);
		long maxFileLength = 1024 * 1024 * 1024;
		if (fileLength > maxFileLength) {
			throw new IOException("File is too large");
		}
		StringBuilder sb = new StringBuilder((int) fileLength);
		BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
		String str = null;
		while((str = reader.readLine()) != null) {
            sb.append(str);
        }
		return sb.toString();
	}

	/**
	 * 根据文件路径读取byte[] 数组
	 */
	public static byte[] readFileByBytes(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		if(!Files.exists(path)) {
			throw new FileNotFoundException(filePath);
		}
		return Files.readAllBytes(path);
	}
	
	/**
	 * 将二进制数组转换成文件
	 * @param src 源二进制数组
	 * @param suffix 文件后缀
	 * @return 文件路径
	 */
	public static String genFileFromBytes(byte[] src, String suffix) {
		Path path;
		String filePath = "";
		String savePath = Initializer.fileSavePath;
		String fileSeparator = File.separator;
		Objects.requireNonNull(savePath, "path cannot be null");
		try {
			path = Paths.get(savePath.concat(fileSeparator).concat(String.valueOf(System.currentTimeMillis())).concat(suffix));
			if(!Files.exists(path)) {
				path = Files.createFile(path);
			}
			path = Files.write(path, src);
			filePath = path.toAbsolutePath().toString();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("create file error");
		}
		return filePath;
	}
	
}

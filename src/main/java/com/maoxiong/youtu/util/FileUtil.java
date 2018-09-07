package com.maoxiong.youtu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.maoxiong.youtu.context.Context;

/**
 * 
 * @author yanrun
 *
 */
public class FileUtil {
	
	private static final Logger logger = LogManager.getLogger(FileUtil.class);
	
	private FileUtil() {
		throw new RuntimeException("no constructor for you");
	}

	/**
	 * 根据文件路径读取byte[] 数组
	 */
	public static byte[] readFileByBytes(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			throw new FileNotFoundException(filePath);
		}
		if(Files.isDirectory(path) || !Files.isReadable(path)) {
			throw new IllegalArgumentException("file " + filePath + " is either a directory or is not readdable");
		}
		long fileSize = Files.size(path);
		long fileSizeInMB = fileSize / 1024 / 1024;
		if(fileSizeInMB >= 1) {
			logger.warn(filePath + "'s size exceeds 1MB which may result in slow response");
		}
		try (FileChannel fc = new RandomAccessFile(filePath, "r").getChannel()) {
			long channelSize = fc.size();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, channelSize).load();
			byte[] result = new byte[(int) channelSize];
			int remainingBytes = byteBuffer.remaining();
			if (remainingBytes > 0) {
				byteBuffer.get(result, 0, remainingBytes);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("cannot convert file: " + filePath + " to bytes");
		}
	}

	/**
	 * 将二进制数组转换成文件
	 * 
	 * @param src
	 *            源二进制数组
	 * @param suffix
	 *            文件后缀
	 * @return 文件路径
	 */
	public static String genFileFromBytes(byte[] src, String suffix) {
		Path path;
		String filePath = "";
		String savePath = String.valueOf(Context.get("savePath"));
		String fileSeparator = File.separator;
		Objects.requireNonNull(savePath, "path cannot be null");
		try {
			path = Paths.get(
					savePath.concat(fileSeparator).concat(String.valueOf(System.currentTimeMillis())).concat(suffix));
			if (!Files.exists(path)) {
				path = Files.createFile(path);
			}
			path = Files.write(path, src);
			filePath = path.toAbsolutePath().toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("create file error");
		}
		return filePath;
	}

}

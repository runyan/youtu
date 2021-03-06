package com.maoxiong.youtu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.cache.Cache;
import com.maoxiong.youtu.cache.impl.CaffeineCache;
import com.maoxiong.youtu.constants.ContextConstants;
import com.maoxiong.youtu.context.Context;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 
 * @author yanrun
 *
 */
public class FileUtil {

	private static final Cache<String, byte[]> BYTE_CACHE = new CaffeineCache<>();

	private static final int MB = 1024 * 1024;

	private FileUtil() {
		throw new RuntimeException("no constructor for you");
	}

	/**
	 * 根据文件路径读取byte[] 数组
	 */
	public static byte[] readFileByBytes(String filePath) {
		return BYTE_CACHE.get(filePath, k -> readFile(filePath));
	}

	private static byte[] readFile(String filePath) {
		try {
			Path path = Paths.get(filePath);
			if (Files.notExists(path)) {
				throw new FileNotFoundException(filePath);
			}
			if (Files.isDirectory(path) || !Files.isReadable(path)) {
				throw new IllegalArgumentException("file " + filePath + " is either a directory nor is readdable");
			}
			long fileSize = Files.size(path);
			long fileSizeInMb = fileSize / MB;
			String tempFilePath = "";
			if (fileSizeInMb >= 1) {
				LogUtil.warn("{}'s size exceeds 1MB, compress file", filePath);
				String fileType = FileTypeUtil.getFileType(filePath);
				String fileName = System.getProperty("user.dir").concat(File.separator).concat("temp")
						.concat(String.valueOf(System.currentTimeMillis()));
				tempFilePath = StringUtils.isBlank(fileType) ? fileName : fileName.concat(".").concat(fileType);
				Files.deleteIfExists(Paths.get(tempFilePath));
				Thumbnails.of(filePath).scale(fileSizeInMb >= 2 ? 0.25 : 0.5).useOriginalFormat().toFile(tempFilePath);
				filePath = tempFilePath;
				Context.set(ContextConstants.TEMP_FILE_SAVE_PATH, tempFilePath);
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
				throw new RuntimeException("cannot convert file: " + filePath + " to byte array");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将二进制数组转换成文件
	 * 
	 * @param src    源二进制数组
	 * @param suffix 文件后缀
	 * @return 文件路径
	 */
	public static String genFileFromBytes(byte[] src, String suffix) {
		Path path;
		String filePath = StringUtils.EMPTY;
		String savePath = String.valueOf(Context.get(ContextConstants.SAVE_PATH));
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
			throw new RuntimeException("create file error");
		}
		return filePath;
	}

}

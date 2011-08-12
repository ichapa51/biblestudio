package org.biblestudio.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class FileUtils {

	public static final String FILE_SEP = System.getProperty("file.separator");
	
	private FileUtils() {
		
	}
	
	public static String getFileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		if (i == -1) {
			return "";
		}
		return fileName.substring(i + 1);
	}
	
	public static String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}
	
	public static String getPathWithoutExt(String filePath)
	{
		int i = filePath.lastIndexOf('.');
		if(i == -1) {
			return filePath;
		}
		return filePath.substring(0, i);
	}
	
	public static String getPathWithoutExt(File file)
	{
		return getPathWithoutExt(file.getAbsolutePath());
	}
	
	public static String getFileNameFromPath(String path)
	{
		String ext = getFileExtension(path);
		if(ext.equals(StringUtils.EMPTY)) {
			return StringUtils.EMPTY;
		}
		int index = Math.max(path.lastIndexOf("/"), path.lastIndexOf("\\"));
		if(index >= 0) {
			return path.substring(index + 1, path.length());
		}
		return path;
	}
	
	public static String getFileNameWithoutExt(String path)
	{
		String fileName = getFileNameFromPath(path);
		int i = fileName.lastIndexOf('.');
		if (i != -1) {
			return fileName.substring(0, i);
		}
		return fileName;
	}
	
	public static String getFileNameWithoutExt(File file)
	{
		return getFileNameWithoutExt(file.getName());
	}
	
	public static String removeLastSeparator(String path)
	{
		if(path.endsWith("\\")) {
			return path.substring(0, path.length()-1);
		} else if(path.endsWith("/")) {
			return path.substring(0, path.length()-1);
		}
		return path;
	}
	
	public static InputStream createInputStream(byte[] data) {
		return new ByteArrayInputStream(data);
	}
	
	public static byte[] createByteArray(InputStream input, boolean closeInput) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		int buffer = 2048;
		int count = 0;
		byte data[] = new byte[buffer];
		while ((count = input.read(data, 0, buffer)) != -1) {
			output.write(data, 0, count);
		}
		if (closeInput) {
			input.close();
		}
		return output.toByteArray();
	}
	
	public static ByteArrayInputStream createByteArrayInputStream(InputStream input, boolean closeInput) throws IOException {
		byte data[] = createByteArray(input, closeInput);
		return new ByteArrayInputStream(data);
	}
}

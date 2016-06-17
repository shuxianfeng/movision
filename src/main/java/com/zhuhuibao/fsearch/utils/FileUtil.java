package com.zhuhuibao.fsearch.utils;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtil {
	public static final String LINE = System.getProperty("line.separator");
	public static final String UTF8 = "UTF-8";

	//
	// public static class Exception extends Exception {
	//
	// public Exception(String message) {
	// super(message);
	// }
	//
	// public Exception(String message, Throwable cause) {
	// super(message, cause);
	// }
	// }

	public static void close(Closeable in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {

			}
		}
	}

	public static byte[] readAsByteArray(File file) throws IOException {
		return readAsByteArray(new FileInputStream(file));
	}

	/***
	 * 
	 * read bytes from input stream, then close the stream
	 * 
	 * ***/
	public static byte[] readAsByteArray(InputStream inStream)
			throws IOException {
		try {
			int size = 1024;
			byte ba[] = new byte[size];
			int readSoFar = 0;
			do {
				int nRead = inStream.read(ba, readSoFar, size - readSoFar);
				if (nRead == -1) {
					break;
				}
				readSoFar += nRead;
				if (readSoFar == size) {
					int newSize = size * 2;
					byte newBa[] = new byte[newSize];
					System.arraycopy(ba, 0, newBa, 0, size);
					ba = newBa;
					size = newSize;
				}
			} while (true);
			byte newBa[] = new byte[readSoFar];
			System.arraycopy(ba, 0, newBa, 0, readSoFar);
			return newBa;
		} finally {
			close(inStream);
		}
	}

	public static boolean isValidFile(String[] allowedExts, String fileName) {
		String ext = getFileExt(fileName);
		if (ext == null)
			return false;
		ext = ext.trim().toLowerCase();
		for (int i = 0; i < allowedExts.length; i++) {
			if (ext.equals(allowedExts[i])) {
				return true;
			}
		}
		return false;
	}

	public static String getFileExt(String fileName) {
		if (fileName == null)
			return null;
		fileName = fileName.trim();
		int index = fileName.lastIndexOf('.');
		if (index > 0 && index < fileName.length() - 1) {
			return fileName.substring(index + 1).toLowerCase();
		}
		return null;
	}

	private static void writeString(File file, String content,
			String outputCharset, boolean append) throws IOException {
		if (outputCharset == null) {
			outputCharset = UTF8;
		}
		if (content == null) {
			return;
		}
		BufferedWriter output = null;
		try {
			ensureFile(file);
			output = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, append), outputCharset));
			output.write(content);
		} finally {
			close(output);
		}
	}

	private static void writeBlob(File file, String outputCharset,
			InputStream in, String inputCharset, boolean append)
			throws IOException {
		if (outputCharset == null) {
			outputCharset = UTF8;
		}
		if (inputCharset == null) {
			inputCharset = UTF8;
		}
		Writer output = null;
		BufferedReader input = null;
		try {
			ensureFile(file);
			input = new BufferedReader(new InputStreamReader(in, inputCharset));
			output = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, append), outputCharset));
			int len = 0;
			char[] buf = new char[2048];
			while ((len = input.read(buf)) > 0) {
				output.write(buf, 0, len);
			}
		} finally {
			close(input);
			close(output);
		}
	}

	public static void append(File file, String content) throws IOException {
		writeString(file, content, null, true);
	}

	public static void append(File file, String content, String outputCharset)
			throws IOException {
		writeString(file, content, outputCharset, true);
	}

	public static void append(File file, String outputCharset, InputStream in,
			String inputCharset) throws IOException {
		writeBlob(file, outputCharset, in, inputCharset, true);
	}

	public static void write(File file, String content) throws IOException {
		writeString(file, content, null, false);
	}

	public static void write(File file, String content, String outputCharset)
			throws IOException {
		writeString(file, content, outputCharset, false);
	}

	public static void write(File file, String outputCharset, InputStream in,
			String inputCharset) throws IOException {
		writeBlob(file, outputCharset, in, inputCharset, false);
	}

	public static String read(String path) throws IOException {
		return read(new File(path), null);
	}

	public static String read(File f) throws IOException {
		return read(f, null);
	}

	public static String read(InputStream in) throws IOException {
		return read(in, null);
	}

	public static String read(String path, String charset) throws IOException {
		return read(new File(path), charset);
	}

	public static String read(File f, String charset) throws IOException {
		return read(new FileInputStream(f), charset);
	}

	public static String read(InputStream in, String charset)
			throws IOException {
		if (charset == null) {
			charset = UTF8;
		}
		StringBuilder buf = new StringBuilder();
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(in, charset));
			int len = 0;
			char[] cbuf = new char[2048];
			while ((len = input.read(cbuf)) > 0) {
				buf.append(cbuf, 0, len);
			}
			return buf.toString();
		} finally {
			close(input);
		}
	}

	public static interface ReadLineHandler {
		boolean handle(String s, int line) throws Exception;
	}

	public static void read(InputStream in, String charset,
			ReadLineHandler handler) throws Exception {
		String s = null;
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(in,
					charset == null ? UTF8 : charset));
			int i = 0;
			while ((s = input.readLine()) != null) {
				if (!handler.handle(s, ++i)) {
					break;
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			close(input);
		}
	}

	public static void copy(InputStream input, OutputStream output)
			throws IOException {
		copy(input, output, 8024);
	}

	public static void copy(InputStream input, OutputStream output,
			int buffersize) throws IOException {
		byte[] buf = new byte[buffersize];
		int n;
		while ((n = input.read(buf)) != -1) {
			if (n > 0) {
				output.write(buf, 0, n);
			}
		}
	}

	public static void copy(File src, File dest) throws IOException {
		if (!src.exists()) {
			throw new IOException("copyFiles: Can not find source: "
					+ src.getAbsolutePath() + ".");
		} else if (!src.canRead()) {
			throw new IOException("copyFiles: No right to source: "
					+ src.getAbsolutePath() + ".");
		}
		if (src.isDirectory()) {
			if (!dest.exists()) {
				if (!dest.mkdirs()) {
					throw new IOException(
							"copyFiles: Could not create direcotry: "
									+ dest.getAbsolutePath() + ".");
				}
			}
			String[] list = src.list();
			for (int i = 0; i < list.length; i++) {
				File src1 = new File(src, list[i]);
				File dest1 = new File(dest, list[i]);
				copy(src1, dest1);
			}
		} else {
			ensureFile(dest);

			BufferedInputStream fin = null;
			BufferedOutputStream fout = null;
			try {
				fin = new BufferedInputStream(new FileInputStream(src));
				fout = new BufferedOutputStream(new FileOutputStream(dest));
				copy(fin, fout);
			} finally {
				close(fin);
				close(fout);
			}
		}
	}

	public static void save(InputStream in, String filePath) throws IOException {
		BufferedOutputStream out = null;
		try {
			File file = new File(filePath);
			ensureFile(file);
			out = new BufferedOutputStream(new FileOutputStream(file));
			copy(in, out);
		} finally {
			close(in);
			close(out);
		}
	}

	public static File getFile(String[] paths) throws IOException {
		if (paths == null || paths.length == 0) {
			throw new IOException("Bad paths");
		}
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			if (path == null || path.isEmpty()) {
				continue;
			}
			if (File.separatorChar == '/') {
				path = StringUtil.replaceAll(path, "\\", File.separator);
			} else {
				path = StringUtil.replaceAll(path, "/", File.separator);
			}
			// remove head slash
			if (buf.length() > 0) {
				char firstChar = path.charAt(0);
				if (firstChar == File.separatorChar) {
					path = path.substring(1);
					if (path.isEmpty()) {
						continue;
					}
				}
			}
			// remove tail slash
			{
				int len = path.length();
				char lastChar = path.charAt(len - 1);
				if (lastChar == File.separatorChar) {
					path = path.substring(0, len - 1);
					if (path.isEmpty()) {
						continue;
					}
				}
			}
			// append slash
			if (buf.length() > 0) {
				buf.append(File.separatorChar);
			}
			buf.append(path);
		}
		return new File(buf.toString());
	}

	public static boolean makeDirByChild(File file) throws IOException {
		File parent = file.getParentFile();
		if (parent != null) {
			return parent.mkdirs();
		}
		return false;
	}

	public static boolean ensureFile(File file) throws IOException {
		if (file.exists()) {
			return false;
		}
		makeDirByChild(file);
		return file.createNewFile();
	}

	public static boolean delete(File file) throws IOException {
		if (!file.exists()) {
			return false;
		}
		if (file.isFile()) {
			return file.delete();
		}
		for (File subFile : file.listFiles()) {
			delete(subFile);
		}
		return file.delete();
	}

	public static interface FileListHandler {

		boolean willOpenStream(String fileName, boolean isDirectory)
				throws Exception;

		void streamOpened(String fileName, InputStream in) throws Exception;
	}

	public static void listClassPathFiles(String jarPath, String dirPath,
			FileListHandler handler) throws Exception {
		JarFile jar = new JarFile(jarPath);
		try {
			Enumeration<JarEntry> entries = jar.entries();
			String relativePath = dirPath;
			// conf/locales
			if (!relativePath.isEmpty()) {
				if (relativePath.charAt(0) == '/') {
					relativePath = dirPath.substring(1);
				}
				if (!relativePath.isEmpty()) {
					if (relativePath.charAt(relativePath.length() - 1) != '/') {
						relativePath += '/';
					}
				}
			}
			while (entries.hasMoreElements()) {
				JarEntry entry = (JarEntry) entries.nextElement();
				String entryName = entry.getName();
				// example: conf/
				// conf/
				// conf/a -> a (ok)
				// conf/dir/ -> dir/ (ok)
				// conf/dir/b -> dir/b
				// conf/dir/c/ -> dir/c/
				if (!relativePath.isEmpty()) {
					if (!entryName.startsWith(relativePath)
							|| entryName.length() == relativePath.length()) {
						continue;
					}
					entryName = entryName.substring(relativePath.length());
				}
				// else conf/ img/ log4j.properties
				int slashIndex = entryName.indexOf('/');
				if (slashIndex < 0
						|| (slashIndex == entryName.length() - 1 && slashIndex == entryName
								.lastIndexOf('/'))) {
					if (slashIndex > 0) {
						entryName = entryName.substring(0, slashIndex);
					}
					if (handler.willOpenStream(entryName, entry.isDirectory())
							&& !entry.isDirectory()) {
						handler.streamOpened(entryName,
								jar.getInputStream(entry));
					}
				}
			}
		} finally {
			close(jar);
		}
	}

	public static void listClassPathFiles(Class<?> clazz, String dirPath,
			FileListHandler handler) throws Exception {
		URL dirURL = clazz.getResource(dirPath);
		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			File file = new File(dirURL.getFile());
			if (!file.isDirectory()) {
				throw new IOException("Not a directory");
			}
			for (File subFile : file.listFiles()) {
				if (handler.willOpenStream(subFile.getName(),
						subFile.isDirectory())) {
					handler.streamOpened(subFile.getName(),
							new FileInputStream(subFile));
				}
			}
		} else if (dirURL.getProtocol().equals("jar")) {
			// file:/xxx/xxx.jar!/conf/locales
			String jarPath = dirURL.getPath().substring(5,
					dirURL.getPath().indexOf("!"));
			listClassPathFiles(jarPath, dirPath, handler);
		} else {
			throw new IOException("Bad dirPath: " + dirPath);
		}
	}
}

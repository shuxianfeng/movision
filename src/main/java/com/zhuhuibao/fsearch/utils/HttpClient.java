package com.zhuhuibao.fsearch.utils;

import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClient {

	public static class Multipart {
		private InputStream inputStream;
		private String name;
		private String contentType;

		public Multipart() {
			super();
		}

		public Multipart(File file) {
			try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
			name = file.getName();
		}

		public InputStream getInputStream() {
			return inputStream;
		}

		public void setInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

	}

	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String BOUNDARY_PREFIX = "--";
	private static final String BOUNDARY = "----WebKitFormBoundaryKcBmeUNzbBJstmOI";
	private static final String MULTIPART_NEWLINE = "\r\n";
	private static final byte[] MULTIPART_NEWLINE_BYTES = MULTIPART_NEWLINE
			.getBytes();
	private static final byte[] MULTIPART_LASTLINE_BYTES = (BOUNDARY_PREFIX
			+ BOUNDARY + BOUNDARY_PREFIX + MULTIPART_NEWLINE).getBytes();

	private int connectTimeout = 30000;
	private int readTimeout = 30000;
	private int responseCode;
	private String responseBody;
	private String responseCharset;

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getResponseCharset() {
		return responseCharset;
	}

	public void setResponseCharset(String responseCharset) {
		this.responseCharset = responseCharset;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public boolean doGet(String targetURL, Map<String, Object> params,
			Map<String, Object> headers) throws Exception {
		return doRequest(false, targetURL, params, headers);
	}

	public boolean doPost(String targetURL, Map<String, Object> params,
			Map<String, Object> headers) throws Exception {
		return doRequest(true, targetURL, params, headers);
	}

	private boolean isMultipart(Object value) {
		if (value == null) {
			return false;
		}
		if (value instanceof File || value instanceof Multipart) {
			return true;
		}
		if (value.getClass().isArray()) {
			int length = Array.getLength(value);
			if (length == 0) {
				return false;
			}
			Object item = Array.get(value, 0);
			if (item instanceof File || item instanceof Multipart) {
				return true;
			}
		}
		return false;
	}

	private boolean containsMultipart(Map<String, Object> params) {
		for (Entry<String, Object> entry : params.entrySet()) {
			Object value = entry.getValue();
			if (isMultipart(value)) {
				return true;
			}
		}
		return false;
	}

	private void writeMultipart(OutputStream out, String key, Object o)
			throws Exception {
		Multipart part = null;
		if (o instanceof Multipart) {
			part = (Multipart) o;
		} else if (o instanceof File) {
			part = new Multipart((File) o);
		} else {
			throw new RuntimeException("Bad multipart");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(BOUNDARY_PREFIX).append(BOUNDARY).append(MULTIPART_NEWLINE);
		sb.append(
				"Content-Disposition: form-data; name=\"" + key
						+ "\"; filename=\"" + part.getName()).append("\"");
		sb.append(MULTIPART_NEWLINE);
		String contentType = part.getContentType();
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		sb.append("Content-Type: ").append(contentType);
		sb.append(MULTIPART_NEWLINE);
		sb.append(MULTIPART_NEWLINE);

		String s = sb.toString();
		byte[] data = s.getBytes();
		out.write(data);

		InputStream in = part.getInputStream();
		try {
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
		} catch (Exception t) {
			throw t;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Throwable tt) {
				}
			}
		}
		out.write(MULTIPART_NEWLINE_BYTES);
	}

	private void writeFormField(OutputStream out, String key, Object value)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(BOUNDARY_PREFIX).append(BOUNDARY).append(MULTIPART_NEWLINE);
		sb.append("Content-Disposition: form-data; name=\"" + key + "\"");
		sb.append(MULTIPART_NEWLINE);
		sb.append(MULTIPART_NEWLINE);
		sb.append(value);
		sb.append(MULTIPART_NEWLINE);

		byte[] data = sb.toString().getBytes();
		out.write(data);
	}

	private void appendFormData(StringBuilder sb, String key, Object value)
			throws UnsupportedEncodingException {
		sb.append(key).append("=");
		sb.append(value == null ? "" : URLEncoder.encode(value.toString(),
				"UTF-8"));
	}

	protected void beforeSend(HttpURLConnection connection) throws Exception {

	}

	private boolean doRequest(boolean post, String targetURL,
			Map<String, Object> params, Map<String, Object> headers)
			throws Exception {
		URL url;
		HttpURLConnection connection = null;

		try {
			boolean multipart = false;
			byte[] paramAsBytes = null;

			if (params != null && (!params.isEmpty())) {
				multipart = containsMultipart(params);
				if (!multipart) {
					StringBuilder paramAsStr = new StringBuilder();
					boolean first = true;
					for (Entry<String, Object> entry : params.entrySet()) {
						if (first) {
							first = false;
						} else {
							paramAsStr.append("&");
						}
						Object value = entry.getValue();
						if (value != null && value.getClass().isArray()) {
							int length = Array.getLength(value);
							for (int i = 0; i < length; i++) {
								if (i > 0) {
									paramAsStr.append("&");
								}
								appendFormData(paramAsStr, entry.getKey(),
										Array.get(value, i));
							}
						} else {
							appendFormData(paramAsStr, entry.getKey(), value);
						}
					}
					if (post) {
						paramAsBytes = paramAsStr.toString().getBytes();
					} else {
						StringBuilder buf = new StringBuilder(targetURL);
						if (targetURL.indexOf('?') > 0) {
							buf.append("&");
						} else {
							buf.append("?");
						}
						buf.append(paramAsStr);
						targetURL = buf.toString();
					}
				}
			}

			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod(post ? POST : GET);
			connection
					.setRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
			if (multipart) {
				connection.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + BOUNDARY);
			} else {
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				connection.setRequestProperty("Content-Length",
						String.valueOf(paramAsBytes == null ? 0
								: paramAsBytes.length));
			}
			if (headers != null) {
				for (Entry<String, Object> entry : headers.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry
							.getValue() == null ? "" : entry.getValue()
							.toString());
				}
			}
			beforeSend(connection);

			if (multipart) {
				OutputStream out = connection.getOutputStream();
				for (Entry<String, Object> entry : params.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					if (value == null) {
						value = "";
					}
					if (isMultipart(value)) {
						if (value.getClass().isArray()) {
							int length = Array.getLength(value);
							for (int i = 0; i < length; i++) {
								Object item = Array.get(value, i);
								writeMultipart(out, key, item);
							}
						} else {
							writeMultipart(out, key, value);
						}
					} else {
						if (value.getClass().isArray()) {
							int length = Array.getLength(value);
							for (int i = 0; i < length; i++) {
								Object item = Array.get(value, i);
								writeFormField(out, key, item);
							}
						} else {
							writeFormField(out, key, value);
						}
					}
				}
				// last line
				out.write(MULTIPART_LASTLINE_BYTES);
			} else if (paramAsBytes != null) {
				OutputStream os = connection.getOutputStream();
				os.write(paramAsBytes);
				os.flush();
				os.close();
			}

			// Get Response
			responseCode = connection.getResponseCode();
			InputStream is = null;
			boolean success = responseCode == HttpURLConnection.HTTP_OK;
			if (success) {
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, responseCharset == null ? "UTF-8" : responseCharset));
			String line;
			StringBuffer buffer = new StringBuffer();
			boolean first = true;
			while ((line = reader.readLine()) != null) {
				if (first) {
					first = false;
				} else {
					buffer.append(FileUtil.LINE);
				}
				buffer.append(line);
			}
			responseBody = buffer.toString();
			reader.close();
			return success;
		} catch (Exception e) {
			throw e;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public Map<String, Object> buildRequestParams(Object... t) {
		if (t == null || t.length <= 0) {
			return null;
		}
		if (t.length % 2 != 0) {
			throw new RuntimeException("illegal args count");
		}
		Map<String, Object> params = new HashMap<String, Object>(t.length);
		for (int i = 0; i < t.length; i += 2) {
			if (t[i] == null || !t[i].getClass().equals(String.class)) {
				throw new RuntimeException("illegal arg: " + t[i] + "at " + i);
			}
			String key = t[i].toString();
			Object value = t[i + 1];
			params.put(key, value);
		}
		return params;
	}

}
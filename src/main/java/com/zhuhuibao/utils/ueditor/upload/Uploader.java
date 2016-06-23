package com.zhuhuibao.utils.ueditor.upload;

import com.zhuhuibao.utils.ueditor.define.State;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class Uploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;
	private MultipartFile upfile;

	public Uploader(HttpServletRequest request, Map<String, Object> conf,MultipartFile upfile) {
		this.request = request;
		this.conf = conf;
		this.upfile = upfile;

	}

	public final State doExec() {
		String filedName = (String) this.conf.get("fieldName");
		State state;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName),
					this.conf);
		} else {
			state = BinaryUploader.saveToObject(this.conf,upfile);
		}

		return state;
	}
}

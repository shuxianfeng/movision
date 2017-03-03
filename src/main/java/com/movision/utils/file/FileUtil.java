package com.movision.utils.file;

import com.movision.common.Response;
import com.movision.common.constant.ApiConstants;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.BusinessException;
import com.movision.utils.MsgPropertiesUtils;
import com.movision.utils.PropertiesUtils;
import com.movision.utils.UUIDGenerator;
import com.movision.utils.oss.AliOSSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Component
public class FileUtil {
	private static Logger log = LoggerFactory.getLogger(FileUtil.class);

	@Autowired
	AliOSSClient aliOSSClient;

	@Autowired
	ApiConstants apiConstants;

	/**
	 * 下载文件
	 *
	 * @param response
	 * @param fileName
	 * @param type
	 * @param chann
	 * @return
	 * @throws IOException
	 */
	public Response downloadObject(HttpServletResponse response, String fileName, String type, String chann)
			throws IOException {
		Response result = new Response();
		String uploadMode = PropertiesUtils.getValue("upload.mode");
		if (uploadMode.equals("zhb")) {
			String downloadDir;

			switch (type) {
			case "img":
				if (chann != null) {
					downloadDir = PropertiesUtils.getValue("uploadDir") + "/" + chann + "/img";
				} else {
					downloadDir = PropertiesUtils.getValue("uploadDir");
				}

				break;
			case "doc":
				if (chann != null) {
					downloadDir = PropertiesUtils.getValue("uploadDir") + "/" + chann + "/doc";

				} else {
					downloadDir = PropertiesUtils.getValue("uploadDir");
				}
				break;
			default:
				log.error("下载类型不支持");
				throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "下载类型不支持");
			}
			String fileurl = downloadDir + "/" + fileName;
			result = downloadFile(response, fileurl);

		} else if (uploadMode.equals("alioss")) {
			byte[] bytes;
			Map<String, Object> map = aliOSSClient.downloadStream(fileName, type, chann);
			String status = String.valueOf(map.get("status"));
			if (status.equals("success")) {

				bytes = (byte[]) map.get("data");
				result = downloadFile(response, bytes);
			} else {
				genErrorMessage(result);
				return result;
			}

		}

		return result;
	}

	/**
	 * 文件下载 (流模式)
	 *
	 * @param response
	 * @param bytes
	 * @return
	 */
	private static Response downloadFile(HttpServletResponse response, byte[] bytes) {
		Response result = new Response();

		try {
			ServletOutputStream stream = response.getOutputStream();
			stream.write(bytes);
			stream.flush();
			stream.close();
		} catch (Exception e) {
			log.error("download file error!", e);
			genErrorMessage(result);
			return result;
		}
		return result;
	}

	private static void genErrorMessage(Response result) {
		result.setCode(MsgCodeConstant.response_status_400);
		result.setMsgCode(MsgCodeConstant.file_download_error);
		result.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.file_download_error))));
	}

	/**
	 * 下载文件
	 *
	 * @param response
	 * @param fileurl
	 *            文件完整路径
	 * @throws IOException
	 */
	public static Response downloadFile(HttpServletResponse response, String fileurl) throws IOException {
		Response result = new Response();
		try {
			File file = new File(fileurl);
			if (file.exists()) { // 如果文件存在
				FileInputStream inputStream = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				int length = inputStream.read(data);
				inputStream.close();
				ServletOutputStream stream = response.getOutputStream();
				stream.write(data);
				stream.flush();
				stream.close();
			} else {
				// result.setCode(MsgCodeConstant.response_status_400);
				// result.setMsgCode(MsgCodeConstant.file_not_exist);
				// result.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.file_not_exist))));
				log.error("文件不存在");
				throw new BusinessException(MsgCodeConstant.file_not_exist, MsgPropertiesUtils.getValue(String
						.valueOf(MsgCodeConstant.file_not_exist)));
			}
		} catch (Exception e) {
			log.error("download file error!", e);
			genErrorMessage(result);
			return result;
		}
		return result;
	}

	/**
	 * 重新命名文件
	 *
	 * @param file
	 * @return
	 */
	public static File renameFile(File file) {

		int index = file.getName().lastIndexOf("."); // 获取文件名中【.】的下标
		String body = UUIDGenerator.genShortUuid();// file.getName().substring(0,
													// index);//文件名
		String postfix; // 表示文件名的后缀，即【.ccc】
		String timer; // 代表当前系统时间的数字

		// 如果该文件的名字中，没有【.】的话，那么lastIndexOf(".")将返回-1
		if (index != -1) {
			timer = new Date().getTime() + ""; // 在文件的名字前面，添加的表示当前系统时间的数字
			postfix = file.getName().substring(index); // 获取到文件名当中的【.ccc】
		} else {
			timer = new Date().getTime() + "";
			postfix = ""; // 如果lastIndexOf(".")返回-1，说明该文件名中没有【.】即没有后缀
		}
		String newName = body + timer + postfix; // 构造新的文件名
		return new File(file.getParent(), newName); // 返回重命名后的文件
	}

	public static String renameFile(String fileName) {

		int index = fileName.lastIndexOf(".");
		String body = UUIDGenerator.genShortUuid();// fileName.substring(0,
													// index);
		String postfix;
		String timer;

		if (index != -1) {
			timer = new Date().getTime() + "";
			postfix = fileName.substring(index);
		} else {
			timer = new Date().getTime() + "";
			postfix = "";
		}
		return body + timer + postfix;
	}

	/**
	 * 判断文件是否存在 防止上传时篡改文件名称
	 *
	 * @param fileName
	 *            文件名称
	 * @param type
	 *            文件类型： img,doc
	 * @param chann
	 *            频道关键字
	 * @return
	 */
	public boolean isExistFile(String fileName, String type, String chann) {
		String uploadMode = PropertiesUtils.getValue("upload.mode");
		switch (uploadMode) {
		case "alioss":
			Map<String, Object> map = aliOSSClient.downloadStream(fileName, type, chann);
			String status = (String) map.get("status");
			return status.equals("success");
		case "zhb":
			String fileUrl = apiConstants.getUploadDir() + "/" + chann + "/" + type + "/" + fileName;
			log.info("测试环境查看询价单路径，fileUrl="+fileUrl);
			File file = new File(fileUrl);
			return file.exists();
		default:
			return false;
		}
	}

	/**
	 * 获取文件名后缀
	 *
	 * @param fileName
	 * @return
	 */
	public static String getSuffix(String fileName) {
		int index = fileName.lastIndexOf(".");
		String suffix = fileName.substring(index + 1, fileName.length());
		return suffix.toLowerCase();
	}

	/**
	 * 获取允许的文件后缀名组
	 *
	 * @param type
	 * @return
	 */
	public static String[] getAllowSuffixs(String type) {
		if ("img".equals(type)) {
			String allowImgSuffix = PropertiesUtils.getValue("allowed.img.suffix");
			return allowImgSuffix.split(",");
		} else if ("doc".equals(type)) {
			String allowFileSuffix = PropertiesUtils.getValue("allowed.file.suffix");
			return allowFileSuffix.split(",");
		}else if ("zip".equals(type)) {
			String allowFileSuffix = PropertiesUtils.getValue("allowed.file.suffix");
			return allowFileSuffix.split(",");
		} else if ("video".equals(type)) {
			String allowFileSuffix = PropertiesUtils.getValue("allowed.video.suffix");
			return allowFileSuffix.split(",");
		}
		return null;
	}

	/**
	 * 判断上传文件 是否允许的后缀名
	 * 
	 * @param fileName
	 * @param type
	 * @return
	 */
	public static boolean isAllowed(String fileName, String type) {
		String suffix = getSuffix(fileName);
		String[] allows = getAllowSuffixs(type);

		return allows != null && Arrays.asList(allows).contains(suffix);

	}

	public static String getFileNameByUrl(String url){
		String fileName = url.substring(url.lastIndexOf("/")+1);
		log.info("fileName="+fileName);
		return fileName;
	}

	public static void main(String[] args) {
		// FileUtil fileUtil = new FileUtil();
		// boolean bool = fileUtil.isExistFile("111","doc","tech");
		// System.out.println(bool);
		// String name = file.getName();
		// String a = FileUtil.renameFile(name);
		// System.out.println(a);
//		System.out.println(getSuffix("xxxx.xlsx"));
		System.out.println("1111");
//		System.out.println(getFileNameByUrl("//image.zhuangyuhao.com/upload/price/img/DBgRcpns1480490323747.png"));
	}
}

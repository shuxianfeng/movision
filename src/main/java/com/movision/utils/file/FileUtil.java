package com.movision.utils.file;

import com.movision.common.Response;
import com.movision.common.constant.ApiConstants;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.BusinessException;
import com.movision.utils.propertiesLoader.MsgPropertiesLoader;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import com.movision.utils.UUIDGenerator;
import com.movision.utils.oss.AliOSSClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Component
public class FileUtil {
	private static Logger log = LoggerFactory.getLogger(FileUtil.class);

	@Autowired
	AliOSSClient aliOSSClient;

	@Autowired
	ApiConstants apiConstants;

	/**
	 * 下载文件(图片及doc文件下载工具类)
	 *
	 * @param fileName
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static Response downloadObject(String fileUrl, String downloadDir, String fileName, String type)
			throws IOException {
		//fileUrl下载地址 downloadDir服务器存放路径 fileName文件名 type下载文件类型
//		String fileurl;

		switch (type) {
			case "img":

//			fileurl = downloadDir + fileName;

				break;
			case "doc":

//			fileurl = downloadDir + fileName;

				break;
			default:
				log.error("下载类型不支持");
				throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "下载类型不支持");
		}
		Response result = downloadFile(fileUrl, downloadDir, fileName);

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
        result.setMessage((MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.file_download_error))));
    }

	/**
	 * 下载文件
	 *
	 * @param fileUrl
	 *            文件完整路径
	 * @throws IOException
	 */
	public static Response downloadFile(String fileUrl, String downloadDir, String fileName) throws IOException {
		Response result = new Response();
		try {
			//校验存储的文件夹是否存在，不存在时自动创建目录
			File file = new File(downloadDir);
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir();
			}
			// 构造URL
			URL url = new URL(fileUrl);
			// 打开连接
			URLConnection con = url.openConnection();
			// 输入流
			InputStream is = con.getInputStream();
			// 1M的数据缓冲
			byte[] bs = new byte[1024 * 1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			OutputStream os = new FileOutputStream(downloadDir + fileName);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();

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
        String uploadMode = PropertiesLoader.getValue("upload.mode");
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
	 * 获取图片名称
	 *
	 * @param url 类似:http://139.196.189.100/upload/brand/img/613.jpg
	 * @return 613.jpg
	 */
	public static String getPicName(String url) {

		return url.substring(url.lastIndexOf("/") + 1);
	}

	/**
	 * 获取指定文件夹下的文件的名称的集合
	 *
	 * @param filepath
	 * @param suffix   若不传，则不需要筛选指定后缀文件
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String> readfileName(String filepath, String suffix)
			throws FileNotFoundException, IOException {
		List<String> list = new ArrayList<String>();
		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {
				if (StringUtils.isNotEmpty(suffix)) {
					if (file.getName().endsWith("." + suffix)) {
						list.add(file.getName());
					}
				} else {
					list.add(file.getName());
				}
				// System.out.println("文件");
				// System.out.println("path=" + file.getPath());
				// System.out.println("absolutepath=" + file.getAbsolutePath());
				// System.out.println("name=" + file.getName());

			} else if (file.isDirectory()) {
				System.out.println(filepath + "，是文件夹");
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {

					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {

						if (StringUtils.isNotEmpty(suffix)) {
							if (readfile.getName().endsWith("." + suffix)) {
								list.add(readfile.getName());
							}
						} else {
							list.add(readfile.getName());
						}

					} else if (readfile.isDirectory()) {

						readfile(filepath + "\\" + filelist[i], suffix);
					}
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("readfile()   Exception:" + e.getMessage());
		}
		return list;
	}

	/**
	 * 获取指定文件夹下的文件
	 *
	 * @param filepath 文件夹路径
	 * @param suffix   匹配的文件的后缀，如jpg
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String> readfile(String filepath, String suffix)
			throws FileNotFoundException, IOException {
		List<String> list = new ArrayList<String>();
		try {
			File file = new File(filepath);
			if (!file.isDirectory() && file.getName().endsWith("." + suffix)) {

				list.add(file.getAbsolutePath());
			} else if (file.isDirectory()) {
				System.out.println("文件夹");
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()
							&& readfile.getName().endsWith("." + suffix)) {

						list.add(readfile.getAbsolutePath());

					} else if (readfile.isDirectory()) {
						readfile(filepath + "\\" + filelist[i], suffix);
					}
				}

			}

		} catch (FileNotFoundException e) {
			System.out.println("readfile()   Exception:" + e.getMessage());
		}
		return list;
	}

	/**
	 * 获取允许的文件后缀名组
	 *
	 * @param type
	 * @return
	 */
	public static String[] getAllowSuffixs(String type) {
		if ("img".equals(type)) {
            String allowImgSuffix = PropertiesLoader.getValue("allowed.img.suffix");
            return allowImgSuffix.split(",");
		} else if ("doc".equals(type)) {
            String allowFileSuffix = PropertiesLoader.getValue("allowed.file.suffix");
            return allowFileSuffix.split(",");
		}else if ("zip".equals(type)) {
            String allowFileSuffix = PropertiesLoader.getValue("allowed.file.suffix");
            return allowFileSuffix.split(",");
		} else if ("video".equals(type)) {
            String allowFileSuffix = PropertiesLoader.getValue("allowed.video.suffix");
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

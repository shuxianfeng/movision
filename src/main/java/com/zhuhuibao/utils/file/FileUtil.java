package com.zhuhuibao.utils.file;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.oss.AliOSSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;


@Service
public class FileUtil {
    private static Logger log = LoggerFactory.getLogger(FileUtil.class);


    @Autowired
    AliOSSClient aliOSSClient;

    /**
     * 下载文件
     * @param response
     * @param fileName
     * @param type
     * @param chann
     * @return
     * @throws IOException
     */
    public Response downloadObject(HttpServletResponse response, String fileName, String type, String chann) throws IOException {
        Response result = new Response();
        String uploadMode = PropertiesUtils.getValue("upload.mode");
        if (uploadMode.equals("zhb")) {
            String downloadDir;

            switch (type) {
                case "img":
                    if (chann != null) {
                        downloadDir = PropertiesUtils.getValue("uploadDir") + "/" + chann + "/img";
                    } else {
                        downloadDir = PropertiesUtils.getValue("uploadDoc");
                    }

                    break;
                case "doc":
                    if (chann != null) {
                        downloadDir = PropertiesUtils.getValue("uploadDir") + "/" + chann + "/doc";

                    } else {
                        downloadDir = PropertiesUtils.getValue("uploadDoc");
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
            Map<String,Object> map  = aliOSSClient.downloadStream(fileName, type, chann);
            String status =String.valueOf(map.get("status"));
            if(status.equals("success")){

                bytes = (byte[]) map.get("data");
                result = downloadFile(response,bytes);
            }else{
                result.setCode(MsgCodeConstant.response_status_400);
                result.setMsgCode(MsgCodeConstant.file_download_error);
                result.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.file_download_error))));
                return result;
            }

        }


        return result;
    }

    /**
     * 文件下载 (流模式)
     * @param response
     * @param bytes
     * @return
     */
    private static Response downloadFile(HttpServletResponse response, byte[] bytes) {
        Response result = new Response();

        try{
            ServletOutputStream stream = response.getOutputStream();
            stream.write(bytes);
            stream.flush();
            stream.close();
        }catch (Exception e){
            log.error("download file error!");
            result.setCode(MsgCodeConstant.response_status_400);
            result.setMsgCode(MsgCodeConstant.file_download_error);
            result.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.file_download_error))));
            return result;
        }
        return result;
    }


    /**
     * 下载文件
     *
     * @param response
     * @param fileurl  文件完整路径
     * @throws IOException
     */
    private static Response downloadFile(HttpServletResponse response, String fileurl) throws IOException {
        Response result = new Response();
        try {
            File file = new File(fileurl);
            if (file.exists()) {   //如果文件存在
                FileInputStream inputStream = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
//                int length = inputStream.read(data);
                inputStream.close();
                ServletOutputStream stream = response.getOutputStream();
                stream.write(data);
                stream.flush();
                stream.close();
            } else {
                result.setCode(MsgCodeConstant.response_status_400);
                result.setMsgCode(MsgCodeConstant.file_not_exist);
                result.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.file_not_exist))));
            }
        } catch (Exception e) {
            log.error("download file error!");
            result.setCode(MsgCodeConstant.response_status_400);
            result.setMsgCode(MsgCodeConstant.file_download_error);
            result.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.file_download_error))));
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

        int index = file.getName().lastIndexOf("."); //获取文件名中【.】的下标
        String body = file.getName().substring(0, index);//文件名
        String postfix; //表示文件名的后缀，即【.ccc】
        String timer; //代表当前系统时间的数字

        //如果该文件的名字中，没有【.】的话，那么lastIndexOf(".")将返回-1
        if (index != -1) {
            timer = new Date().getTime() + ""; //在文件的名字前面，添加的表示当前系统时间的数字
            postfix = file.getName().substring(index); //获取到文件名当中的【.ccc】
        } else {
            timer = new Date().getTime() + "";
            postfix = ""; //如果lastIndexOf(".")返回-1，说明该文件名中没有【.】即没有后缀
        }
        String newName = body + timer + postfix; //构造新的文件名
        return new File(file.getParent(), newName); //返回重命名后的文件
    }

    public static String renameFile(String fileName) {

        int index = fileName.lastIndexOf(".");
        String body = fileName.substring(0, index);
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
}

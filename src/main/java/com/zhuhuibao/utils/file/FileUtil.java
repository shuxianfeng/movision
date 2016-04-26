package com.zhuhuibao.utils.file;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/4/26 0026.
 */
public class FileUtil {

    /**
     * 下载文件
     * @param response
     * @param fileurl  文件完整路径
     * @throws IOException
     */
    public static void downloadFile(HttpServletResponse response, String fileurl) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Content-disposition", "attachment;filename=" + fileurl);
        response.setContentType("application/octet-stream");
        //fileurl = ApiConstants.getUploadDoc() + Constant.upload_price_document_url + "/" + fileurl;
        File file = new File(fileurl);
        if (file.exists()) {   //如果文件存在
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            int length = inputStream.read(data);
            inputStream.close();
            ServletOutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();
        }
    }
}

package com.zhuhuibao.utils.file;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/4/26 0026.
 */
public class FileUtil {
    private static Logger log = LoggerFactory.getLogger(FileUtil.class);
    /**
     * 下载文件
     * @param response
     * @param fileurl  文件完整路径
     * @throws IOException
     */
    public static Response downloadFile(HttpServletResponse response, String fileurl) throws IOException {
        Response jsonResult = new Response();
        try {
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
            else
            {
                jsonResult.setCode(MsgCodeConstant.response_status_400);
                jsonResult.setMsgCode(MsgCodeConstant.file_not_exist);
                jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.file_not_exist))));
            }
        }catch(Exception e)
        {
            log.error("download file error!");
            jsonResult.setCode(MsgCodeConstant.response_status_400);
            jsonResult.setMsgCode(MsgCodeConstant.file_download_error);
            jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.file_download_error))));
            return jsonResult;
        }
        return jsonResult;
    }
}

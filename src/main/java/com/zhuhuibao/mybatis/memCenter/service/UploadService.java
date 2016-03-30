package com.zhuhuibao.mybatis.memCenter.service;

/**
 * Created by cxx on 2016/3/7 0007.
 */

import com.oreilly.servlet.MultipartRequest;
import com.zhuhuibao.common.ApiConstants;
import com.zhuhuibao.utils.RandomFileNamePolicy;
import com.zhuhuibao.utils.ResourcePropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 上传处理
 * @author cxx
 *
 */
@Service
@Transactional
public class UploadService {
    private static final Logger log = LoggerFactory.getLogger(UploadService.class);

    @Autowired
    ApiConstants ApiConstants;

    /**
     * 上传图片，返回url
     */

    public String upload(HttpServletRequest req,String type)throws IOException
    {
        //指定所上传的文件，上传成功后，在服务器的保存位置
        String saveDirectory ="";
        if("img".equals(type)) {
            saveDirectory = ApiConstants.getUploadDir()+"/img";
        }else{
            saveDirectory = ApiConstants.getUploadDir()+"/doc";
        }
        String ip_address = ResourcePropertiesUtils.getValue("host.ip");

        //指定所上传的文件最大上传文件大小
        int maxPostSize = ApiConstants.getUploadMaxPostSize();

        //指定所上传的文件命名规则
        RandomFileNamePolicy rfnp = new RandomFileNamePolicy();

        //完成文件上传
        MultipartRequest multi = new MultipartRequest(req, saveDirectory, maxPostSize, "UTF-8", rfnp);

        Enumeration fileNames = multi.getFileNames();
        String url = "";
        while(fileNames.hasMoreElements()){
            String fileName = (String)fileNames.nextElement();
            if(null != multi.getFile(fileName)){
                String lastFileName = multi.getFilesystemName(fileName);
                if("img".equals(type)){
                    url =  ip_address + "/upload/img/" + lastFileName;
                }else{
                    url =  ip_address + "/upload/doc/" + lastFileName;
                }

            }
        }

        return url;
    }
}

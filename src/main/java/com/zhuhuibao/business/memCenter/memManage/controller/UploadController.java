package com.zhuhuibao.business.memCenter.memManage.controller;

import com.oreilly.servlet.MultipartRequest;
import com.zhuhuibao.common.ApiConstants;
import com.zhuhuibao.utils.RandomFileNamePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by cxx on 2016/3/2 0002.
 */
@RestController
public class UploadController {

    @Autowired
    ApiConstants ApiConstants;

    @RequestMapping(value = "/rest/upload", method = RequestMethod.POST)
    public void upload(HttpServletRequest req, HttpServletResponse response) throws IOException {
        //指定所上传的文件，上传成功后，在硬盘上的保存位置
        String saveDirectory = ApiConstants.getUploadDir();

        //指定所上传的文件最大上传文件大小
        int maxPostSize = ApiConstants.getUploadMaxPostSize();

        //指定所上传的文件命名规则
        RandomFileNamePolicy rfnp = new RandomFileNamePolicy();

        //完成文件上传
        MultipartRequest multi = new MultipartRequest(req, saveDirectory, maxPostSize, "UTF-8", rfnp);

        Enumeration fileNames = multi.getFileNames();
        while(fileNames.hasMoreElements()){
            String fileName = (String)fileNames.nextElement();
            if(null != multi.getFile(fileName)){
                String lastFileName = multi.getFilesystemName(fileName);
                String url = saveDirectory + "/" + lastFileName;
                response.getWriter().write(url);
            }
        }
    }
}

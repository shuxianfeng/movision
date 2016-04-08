package com.zhuhuibao.mybatis.memCenter.service;

/**
 * Created by cxx on 2016/3/7 0007.
 */

import com.oreilly.servlet.MultipartRequest;
import com.zhuhuibao.common.ApiConstants;
import com.zhuhuibao.common.Constant;
import com.zhuhuibao.utils.RandomFileNamePolicy;
import com.zhuhuibao.utils.ResourcePropertiesUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.StringTokenizer;

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
            log.info("suffix = " +getFileSuffix(req)); 
        }else{
            saveDirectory = ApiConstants.getUploadDir()+Constant.upload_price_document_url;
            log.info("suffix = " +getFileSuffix(req)); 
            File file = new File(saveDirectory);
            if(!file.exists())
            {
            	file.mkdirs();
            }
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
                    url =  lastFileName;
                }

            }
        }

        return url;
    }

    /**
     * 获得文件的后缀
     * @param req
     * @throws IOException
     */
	private String getFileSuffix(HttpServletRequest req) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) req.getInputStream()));  
        String line = null;  
        while ((line = br.readLine()) != null) {  
            if( line.toLowerCase().indexOf("filename") > -1)
            {
            	break;
            }
        }  
        log.info(line);
        StringTokenizer st = new StringTokenizer(line);
        String filename = "";
	     while (st.hasMoreTokens()) {
	    	 filename = st.nextToken();
	         if(filename.toLowerCase().indexOf("filename") > -1)
	         {
	        	 break;
	         }
	     }
	     log.info(filename);
	     String[] arr = filename.split("=");
	     return arr[1].substring(arr[1].lastIndexOf(".")+1);
	}
    
    public static void main(String[] args) {
		String str = "Content-Disposition: form-data; name=\"\"; filename=\"1.png\"";
		StringTokenizer st = new StringTokenizer(str);
		String filename = "";
	     while (st.hasMoreTokens()) {
	    	 filename = st.nextToken();
	    	System.out.println(filename);
	         if(filename.toLowerCase().indexOf("filename") > -1)
	         {
	        	 break;
	         }
	     }
	     System.out.println(filename);
	}
}

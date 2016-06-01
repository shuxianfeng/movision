package com.zhuhuibao.mybatis.memCenter.service;

/**
 * Created by cxx on 2016/3/7 0007.
 */

import com.oreilly.servlet.MultipartRequest;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.utils.RandomFileNamePolicy;
import com.zhuhuibao.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
        int maxPostSize = 0 ;
        if("img".equals(type)) {
            saveDirectory = ApiConstants.getUploadDir()+"/img";
            //指定所上传的文件最大上传文件大小
            maxPostSize = ApiConstants.getUploadPicMaxPostSize();
        }else if("doc".equals(type)){
            saveDirectory = ApiConstants.getUploadDoc()+"/price";
            //指定所上传的文件最大上传文件大小
            maxPostSize = ApiConstants.getUploadDocMaxPostSize();
        }else if("tech".equals(type)){
            //技术资料
            saveDirectory = ApiConstants.getUploadDoc()+"/tech";
            //技术资料最大1G
            maxPostSize = ApiConstants.getUploadDocMaxPostSize();
        }
        else if("expert".equals(type)){
            //技术资料
            saveDirectory = ApiConstants.getUploadDir()+"/expert";
            //技术资料最大1G
            maxPostSize = ApiConstants.getUploadPicMaxPostSize();
        }else{
            saveDirectory = ApiConstants.getUploadDoc()+"/job";
            maxPostSize = ApiConstants.getUploadDocMaxPostSize();
        }
        //目录不存在则创建
        File dir = new File(saveDirectory);
        if(!dir.exists() && !dir.isDirectory()) {
            dir.mkdir();
            log.info("mk dir susscess dirName = "+saveDirectory);
        }
        //String a = getFileSuffix(req);

        String ip_address = PropertiesUtils.getValue("host.ip");

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
   /* private String getFileSuffix(HttpServletRequest req) throws IOException {
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
        String suffix = arr[1];
        suffix = suffix.replace("\"", "");
        return suffix.substring(suffix.lastIndexOf("."));
    }*/
}

package com.zhuhuibao.business.common;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.oss.AliOSSClient;
import com.zhuhuibao.utils.oss.ZhbOssClient;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Map;

/**
 * 上传
 */
@RestController
/*@RequestMapping("/rest/common")*/
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);



    @Autowired
    ZhbOssClient zhbOssClient;

    @ApiOperation(value = "上传图片，返回url", notes = "上传图片，返回url", response = Response.class)
    @RequestMapping(value = {"/rest/uploadImg","/rest/common/upload_img"}, method = RequestMethod.POST)
    public Response uploadImg(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        Response result = new Response();

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            String url = zhbOssClient.uploadObject(file,"img",null);
            result.setData(url);
            return result;
        }else {
            log.error("上传图片失败");
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

    }
}

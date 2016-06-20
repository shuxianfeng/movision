package com.zhuhuibao.business.common;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cxx on 2016/6/17 0017.
 */
@RestController
/*@RequestMapping("/rest/common")*/
public class CommonController {

    @Autowired
    UploadService uploadService;

    @ApiOperation(value = "上传图片，返回url", notes = "上传图片，返回url", response = Response.class)
    @RequestMapping(value = {"/rest/uploadImg","/rest/common/upload_img"}, method = RequestMethod.POST)
    public Response uploadImg(HttpServletRequest req) throws Exception {
        Response result = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            //完成文件上传
            String url = uploadService.upload(req,"img");
            result.setData(url);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }
}

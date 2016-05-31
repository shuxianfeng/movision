package com.zhuhuibao.business.tech.site;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.mybatis.techtrain.entity.TechData;
import com.zhuhuibao.mybatis.techtrain.service.TechnologyService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@RequestMapping("/rest/tech/site/data")
@Api(value = "techData", description = "技术资料接口")
public class TechDataController {
    private static final Logger log = LoggerFactory.getLogger(TechDataController.class);

    @Autowired
    UploadService uploadService;

    @Autowired
    TechnologyService techService;


    @ApiOperation(value="上传技术资料(行业解决方案，技术文档，培训资料)",notes="上传技术资料(行业解决方案，技术文档，培训资料)",response = Response.class)
    @RequestMapping(value = "upload_tech_data", method = RequestMethod.POST)
    public Response uploadAskList(HttpServletRequest req) throws IOException {
        Response result = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session){
            String url = uploadService.upload(req,"tech");
            Map map = new HashMap();
            map.put(Constants.name,url);
            result.setData(map);
            result.setCode(200);
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @RequestMapping(value="add_Tech_data", method = RequestMethod.POST)
    @ApiOperation(value="新增技术资料(行业解决方案，技术文档，培训资料)",notes = "新增技术资料(行业解决方案，技术文档，培训资料)",response = Response.class)
    public Response insertTechData(@ApiParam(value = "技术资料:行业解决方案，技术文档，培训资料")  @ModelAttribute(value="techData")TechData techData)
    {
        int result = techService.insertTechData(techData);
        Response response = new Response();
        return response;
    }
}

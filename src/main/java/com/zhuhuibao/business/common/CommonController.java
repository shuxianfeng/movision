package com.zhuhuibao.business.common;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.common.entity.SysJoinus;
import com.zhuhuibao.mybatis.common.entity.SysResearch;
import com.zhuhuibao.mybatis.common.service.SysJoinusService;
import com.zhuhuibao.mybatis.common.service.SysResearchService;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.oms.service.CategoryService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
import com.zhuhuibao.utils.oss.ZhbOssClient;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 上传
 */
@RestController
/*@RequestMapping("/rest/common")*/
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    MemberService memberService;

    @Autowired
    ZhbOssClient zhbOssClient;

    @Autowired
    SysResearchService sysResearchService;

    @Autowired
    SysJoinusService joinusService;

    @Autowired
    CategoryService categoryService;

    @ApiOperation(value = "上传图片，返回url", notes = "上传图片，返回url", response = Response.class)
    @RequestMapping(value = {"rest/uploadImg","/rest/common/upload_img"}, method = RequestMethod.POST)
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

    @ApiOperation(value = "上传图片，返回url(免登陆)", notes = "上传图片，返回url(免登陆)", response = Response.class)
    @RequestMapping(value = "/rest/common/upload_img_without_login", method = RequestMethod.POST)
    public Response upload_img_without_login(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        Response result = new Response();
            String url = zhbOssClient.uploadObject(file,"img",null);
            result.setData(url);
            return result;
    }

    /**
     *留言
     * @return
     */
    @ApiOperation(value="留言",notes="留言",response = Response.class)
    @RequestMapping(value = "/rest/common/add_message", method = RequestMethod.POST)
    public Response message(@ModelAttribute Message message) {
        Response response = new Response();
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            message.setCreateid(String.valueOf(createid));
            memberService.saveMessage(message);
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value="提升计划",notes="提升计划",response = Response.class)
    @RequestMapping(value = "/rest/common/add_research", method = RequestMethod.POST)
    public Response addResearch(@ApiParam @ModelAttribute  SysResearch sysResearch){

        sysResearchService.insert(sysResearch);

        return new Response();
    }


    @ApiOperation(value="加盟",notes="加盟",response = Response.class)
    @RequestMapping(value = "/rest/common/joinus", method = RequestMethod.POST)
    public Response joinus(@ApiParam @ModelAttribute SysJoinus joinus){
        //校验验证码
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
        String sessionCode = (String) sess.getAttribute("joinus");
        String imgCode = joinus.getImgCode();
        if(!sessionCode.equals(imgCode)){
             throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"图形验证码不正确");
        }

        joinusService.insert(joinus);

        return new Response();
    }

    @ApiOperation(value="图形验证码",notes="图形验证码")
    @RequestMapping(value = "/rest/common/imgCode", method = RequestMethod.GET)
    public void getImageCode(HttpServletRequest req, HttpServletResponse response){

        VerifyCodeUtils.getImageCode(req, response,100,40,4,"joinus");

    }

    @RequestMapping(value = "/rest/common/sel_firstCategory", method = RequestMethod.GET)
    @ApiOperation(value = "系统一级分类", notes = "系统一级分类", response = Response.class)
    public Response getProductFirstCategory() throws IOException {
        Response jsonResult = new Response();
        List<ResultBean> systemList = categoryService.findSystemList();
        jsonResult.setData(systemList);
        return jsonResult;
    }

    @RequestMapping(value = "/rest/common/sel_secondCategory", method = RequestMethod.GET)
    @ApiOperation(value = "系统二级分类", notes = "系统二级分类", response = Response.class)
    public Response getProductSecondCategory(@RequestParam String parentId) throws IOException {
        Response response = new Response();
        List<ResultBean> subSystemList = categoryService.findSubSystemList(parentId);
        response.setData(subSystemList);
        return response;
    }
}

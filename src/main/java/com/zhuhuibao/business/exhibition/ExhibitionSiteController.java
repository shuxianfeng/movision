package com.zhuhuibao.business.exhibition;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.mybatis.memCenter.entity.MeetingOrder;
import com.zhuhuibao.mybatis.memCenter.service.ExhibitionService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 会展接口类
 * Created by cxx on 2016/5/11 0011.
 */
@RestController
@RequestMapping("/rest/exhibition")
public class ExhibitionSiteController {
    private static final Logger log = LoggerFactory.getLogger(ExhibitionSiteController.class);

    @Autowired
    private ExhibitionService exhibitionService;

    /**
     * 发布会展定制
     */
    @ApiOperation(value="发布会展定制",notes="发布会展定制",response = JsonResult.class)
    @RequestMapping(value = "publishMeetingOrder", method = RequestMethod.POST)
    public JsonResult publishMeetingOrder(MeetingOrder meetingOrder) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                meetingOrder.setCreateid(principal.getId().toString());
                exhibitionService.publishMeetingOrder(meetingOrder);
            }else{
                jsonResult.setCode(401);
                jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                jsonResult.setMsgCode(MsgCodeConstant.un_login);
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            jsonResult.setMsgCode(MsgCodeConstant.un_login);
        }
        return jsonResult;
    }

}

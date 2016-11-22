package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.ExpertConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mobile.web.mc.MobileExpertController;
import com.zhuhuibao.mybatis.expert.entity.Achievement;
import com.zhuhuibao.mybatis.expert.entity.Dynamic;
import com.zhuhuibao.mybatis.expert.entity.Expert;
import com.zhuhuibao.mybatis.expert.entity.ExpertSupport;
import com.zhuhuibao.service.MobileExpertPageService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专家频道
 *
 * @author changxinwei
 * @date 2016年11月21日
 */

@RestController
@RequestMapping("/rest/m/expert/site/")
public class MobileExpertPageController {

    private static final Logger log = LoggerFactory.getLogger(MobileExpertController.class);

    @Autowired
    private MobileExpertPageService mobileExpertPageService;


    @RequestMapping(value = "sel_expert_home_page", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-专家首页-专家频道首页详情", notes = "触屏端-专家首页-专家频道首页详情", response = Response.class)
    public Response selExpertHomePage(@ApiParam(value = "当前登陆人id") @RequestParam(required = false) String createId,
                                      @ApiParam(value = "条数") @RequestParam(required = false) int count) {
        Map<String, List> map = new HashMap<>();
        try {
            // banner位广告图片区域
            List bannerList = new ArrayList();
            map.put("bannerList", bannerList);

            // 筑慧专家库区域
            List<Expert> expertsList = mobileExpertPageService.findNewSettledExpertList(count);
            map.put("expertsList", expertsList);

            //专家培训区域
            List trainList = mobileExpertPageService.findExpertTrainList(count);
            map.put("trainList", trainList);

            //最新技术成果
            List<Map<String, String>> achievementList = mobileExpertPageService.findNewAchievementList(count);
            map.put("achievementList", achievementList);

            //协会动态区域
            List<Map<String, String>> dynamicList = mobileExpertPageService.findNewDynamicList(count);
            map.put("dynamicList", dynamicList);

        } catch (Exception e) {
            log.error("sel_expert_home_page error! ", e);
        }
        return new Response(map);
    }

    @RequestMapping(value = "sel_expert_list", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-专家首页-专家库更多列表", notes = "触屏端-专家首页-专家库更多列表", response = Response.class)
    public Response selExpertList(@ApiParam(value = "省") @RequestParam(required = false) String province,
                                  @ApiParam(value = "专家类型") @RequestParam(required = false) String expertType,
                                  @RequestParam(required = false, defaultValue = "1") String pageNo,
                                  @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        try {
            Paging<Map<String, Object>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            Map<String, Object> map = new HashMap<>();
            //查询传参
            map.put("province", province);
            map.put("expertType", expertType);
            map.put("type", ExpertConstant.EXPERT_TYPE_ONE);
            List<Map<String, Object>> expertList = mobileExpertPageService.findAllExpert(pager, map);
            pager.result(expertList);
            response.setData(pager);
        } catch (Exception e) {
            log.error("sel_expert_list error! ", e);
        }
        return response;
    }


    @RequestMapping(value = "sel_expert_details_list", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-专家首页-专家和技术成果详情", notes = "触屏端-专家首页-专家和技术成果详情", response = Response.class)
    public Response selExpertDetailsList(@ApiParam(value = "商品ID") @RequestParam String goodsID,
                                         @ApiParam(value = "商品类型同筑慧币") @RequestParam String type) {
        Response response = new Response();
        try {
            response.setData(mobileExpertPageService.viewGoodsRecord(goodsID, type));
        } catch (Exception e) {
            log.error("sel_expert_details_list error! ", e);
        }
        return response;
    }


    @RequestMapping(value = "sel_expert_train_list", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-专家首页-专家培训列表", notes = "触屏端-专家首页-专家培训列表", response = Response.class)
    public Response selExpertTrainList(@ApiParam(value = "省") @RequestParam(required = false) String province,
                                       @RequestParam(required = false, defaultValue = "1") String pageNo,
                                       @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        try {
            Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            Map<String, Object> condition = new HashMap<>();
            condition.put("province", province);
            condition.put("courseType", ExpertConstant.COURSE_TYPE_EXPERT);
            //销售中
            condition.put("status", TechConstant.PublishCourseStatus.SALING.toString());
            List<Map<String, String>> techList = mobileExpertPageService.findAllPublishCoursePager(pager, condition);
            pager.result(techList);
            response.setData(pager);
        } catch (Exception e) {
            log.error("sel_expert_train_list error! ", e);
        }
        return response;
    }

    @RequestMapping(value = "sel_publish_course_detail", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-专家首页-预览课程详情", notes = "触屏端-专家首页-预览课程详情", response = Response.class)
    public Response selPublishCourseDetail(@ApiParam(value = "培训课程ID") @RequestParam Long courseId) {
        Response response = new Response();
        try {
            Map<String, Object> condition = new HashMap<>();
            condition.put("courseid", courseId);
            Map<String, String> course = mobileExpertPageService.previewTrainCourseDetail(condition);
            response.setData(course);
        } catch (Exception e) {
            log.error("sel_publish_course_detail error! ", e);
        }
        return response;
    }


    @ApiOperation(value = "触屏端-专家首页-技术成果列表", notes = "触屏端-专家首页-技术成果列表", response = Response.class)
    @RequestMapping(value = "sel_achievement_list", method = RequestMethod.GET)
    public Response selAchievementList(@ApiParam(value = "系统分类") @RequestParam(required = false) String systemType,
                                       @ApiParam(value = "应用领域") @RequestParam(required = false) String useArea,
                                       @RequestParam(required = false, defaultValue = "1") String pageNo,
                                       @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
        try {
            Paging<Achievement> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            Map<String, Object> map = new HashMap<>();
            //查询传参
            map.put("systemType", systemType);
            map.put("useArea", useArea);
            map.put("type", ExpertConstant.EXPERT_TYPE_ONE);
            List<Achievement> achievementList = mobileExpertPageService.findAllAchievementList(pager, map);
            pager.result(achievementList);
            response.setData(pager);
        } catch (Exception e) {
            log.error("sel_achievement_list error! ", e);
        }
        return response;
    }


    @ApiOperation(value = "触屏端-专家首页-申请专家", notes = "触屏端-专家首页-申请专家", response = Response.class)
    @RequestMapping(value = "add_apply_expert", method = RequestMethod.POST)
    public Response addApplyExpert(@ModelAttribute Expert expert) {
        Response response = new Response();
        try {
            Long createId = ShiroUtil.getCreateID();
            if (createId != null) {
                expert.setCreateId(String.valueOf(createId));
                mobileExpertPageService.queryExpertByCreateid(createId.toString(), expert);
            } else {
                throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        } catch (Exception e) {
            log.error("add_apply_expert error! ", e);
        }
        return response;
    }


    @ApiOperation(value = "触屏端-专家首页-申请专家支持", notes = "触屏端-专家首页-申请专家支持", response = Response.class)
    @RequestMapping(value = "add_expert_support", method = RequestMethod.POST)
    public Response addExpertSupport(@ApiParam(value = "联系人名称") @RequestParam String linkName,
                                       @ApiParam(value = "手机") @RequestParam String mobile,
                                       @ApiParam(value = "验证码") @RequestParam String code,
                                       @ApiParam(value = "申请原因") @RequestParam(required = false) String reason) {
        Response response = new Response();
        try {
            ExpertSupport expertSupport = new ExpertSupport();
            expertSupport.setLinkName(linkName);
            expertSupport.setMobile(mobile);
            expertSupport.setReason(reason);
            mobileExpertPageService.checkMobileCode(code, mobile, ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT, expertSupport);
        } catch (Exception e) {
            log.error("add_expert_support error! ", e);
        }
        return response;
    }


    @ApiOperation(value = "触屏端-专家首页-申请专家支持-手机验证码", notes = "触屏端-专家首页-申请专家支持-手机验证码", response = Response.class)
    @RequestMapping(value = "get_expert_support", method = RequestMethod.GET)
    public Response getExpertSupport(@ApiParam(value = "手机号码") @RequestParam String mobile,
                                     @ApiParam(value = "图形验证码") @RequestParam String imgCode) {
        Response response = new Response();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session sess = currentUser.getSession(true);
            String sessImgCode = (String) sess.getAttribute(ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT);
            mobileExpertPageService.getTrainMobileCode(mobile, ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT, imgCode, sessImgCode);
        } catch (Exception e) {
            log.error("get_expert_support error! ", e);
        }
        return response;
    }


    @ApiOperation(value = "触屏端-专家首页-申请专家支持-图形验证码", notes = "触屏端-专家首页-申请专家支持-图形验证码", response = Response.class)
    @RequestMapping(value = "sel_support_img_code", method = RequestMethod.GET)
    public void selSupportImgCode(HttpServletResponse response) {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session sess = currentUser.getSession(false);
            String verifyCode = VerifyCodeUtils.outputHttpVerifyImage(100, 40, response, Constants.CHECK_IMG_CODE_SIZE);
            sess.setAttribute(ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT, verifyCode);
        } catch (Exception e) {
            log.error("sel_support_img_code error! ", e);
        }
    }


    @ApiOperation(value = "触屏端-专家首页-协会动态列表", notes = "触屏端-专家首页-协会动态列表", response = Response.class)
    @RequestMapping(value = "sel_dynamic_list", method = RequestMethod.GET)
    public Response dynamicList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        try {
            Paging<Dynamic> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            Map<String, Object> map = new HashMap<>();
            //查询传参
            map.put("type", ExpertConstant.EXPERT_TYPE_ONE);
            List<Dynamic> dynamicList = mobileExpertPageService.findAllDynamicList(pager, map);
            pager.result(dynamicList);
            response.setData(pager);
        } catch (Exception e) {
            log.error("sel_dynamic_list error! ", e);
        }
        return response;
    }


    /**
     * 协会动态详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "触屏端-专家首页-协会动态详情", notes = "触屏端-专家首页-协会动态详情", response = Response.class)
    @RequestMapping(value = "sel_dynamic_details", method = RequestMethod.GET)
    public Response selDynamicDetails(@ApiParam(value = "协会动态Id") @RequestParam String id) {
        Response response = new Response();
        try {
            Dynamic dynamic = mobileExpertPageService.queryDynamicById(id);
            response.setData(dynamic);
        } catch (Exception e) {
            log.error("sel_dynamic_details error! ", e);
        }
        return response;
    }



/*    @ApiOperation(value="给专家留言",notes="给专家留言",response = Response.class)
    @RequestMapping(value = "base/add_message", method = RequestMethod.POST)
    public Response message(@ModelAttribute Message message) throws Exception {
        Response response = new Response();
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            message.setCreateid(String.valueOf(createid));
            boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.GZJLY.toString());
            if(bool) {
                memberService.saveMessage(message);
            }else{//支付失败稍后重试，联系客服
                throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }*/


}
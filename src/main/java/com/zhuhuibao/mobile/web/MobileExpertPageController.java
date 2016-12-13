package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.*;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mobile.web.mc.MobileExpertController;
import com.zhuhuibao.mybatis.expert.entity.Achievement;
import com.zhuhuibao.mybatis.expert.entity.Dynamic;
import com.zhuhuibao.mybatis.expert.entity.Expert;
import com.zhuhuibao.mybatis.expert.entity.ExpertSupport;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.Messages;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
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
@RequestMapping("/rest/m/expert/site")
@Api(value = "mobileExpert", description = "专家频道")
public class MobileExpertPageController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(MobileExpertController.class);

    @Autowired
    private MobileExpertPageService mobileExpertPageService;

    @Autowired
    ZhbService zhbService;

    @ApiOperation(value = "触屏端-专家首页-专家技术成果-系統分類常量", notes = "触屏端-专家首页-专家技术成果-系統分類常量", response = Response.class)
    @RequestMapping(value = "sel_systemList", method = RequestMethod.GET)
    public Response SystemList() {
        Response response = new Response();
        List<Map<String, String>> list = mobileExpertPageService.findByType(ExpertConstant.EXPERT_SYSTEM_TYPE);
        response.setData(list);
        return response;
    }

    @ApiOperation(value = "触屏端-专家首页-专家技术成果-应用领域常量", notes = "触屏端-专家首页-专家技术成果-应用领域常量", response = Response.class)
    @RequestMapping(value = "sel_useAreaList", method = RequestMethod.GET)
    public Response useAreaList() {
        Response response = new Response();
        List<Map<String, String>> list = mobileExpertPageService.findByType(ExpertConstant.EXPERT_USEAREA_TYPE);
        response.setData(list);
        return response;
    }

    @RequestMapping(value = "sel_expert_home_page", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-专家首页-专家频道首页详情", notes = "触屏端-专家首页-专家频道首页详情", response = Response.class)
    public Response selExpertHomePage() {
        Map<String, List> map = new HashMap<>();
        int count = 4;
        try {
            // banner位广告图片区域
            map.put("bannerList", mobileExpertPageService.findBannerList());

            // 筑慧专家库区域
            List<Expert> expertsList = mobileExpertPageService.findNewSettledExpertList(count);
            map.put("expertsList", expertsList);

            // 专家培训区域
            Map<String, Object> condition = new HashMap<>();
            condition.put("status", TechConstant.PublishCourseStatus.SALING.toString());
            condition.put("courseType", ExpertConstant.COURSE_TYPE_EXPERT);
            condition.put("count", count);
            List<Map<String, String>> trainList = mobileExpertPageService.findExpertTrainList(condition);
            map.put("trainList", trainList);

            // 最新技术成果
            List<Map<String, String>> achievementList = mobileExpertPageService.findNewAchievementList(count);
            map.put("achievementList", achievementList);

            // 协会动态区域
            List<Map<String, String>> dynamicList = mobileExpertPageService.findNewDynamicList(3);
            map.put("dynamicList", dynamicList);

        } catch (Exception e) {
            log.error("sel_expert_home_page error! ", e);
        }
        return new Response(map);
    }

    @RequestMapping(value = "sel_expert_list", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-专家首页-专家库更多列表", notes = "触屏端-专家首页-专家库更多列表", response = Response.class)
    public Response selExpertList(@ApiParam(value = "省") @RequestParam(required = false) String province, @ApiParam(value = "专家类型") @RequestParam(required = false) String expertType,
            @RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        try {
            Paging<Map<String, Object>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            Map<String, Object> map = new HashMap<>();
            // 查询传参
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

    @RequestMapping(value = "sel_resume_details", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-专家首页-专家详情", notes = "触屏端-专家首页-专家详情", response = Response.class)
    public Response selResumeDetails(@ApiParam(value = "专家的id") @RequestParam(required = true) String id) {
        Map<String, Object> resultMap = new HashMap<>();
        Response response = new Response();
        try {
            getPrivilegeGoodsDetails(resultMap, id, ZhbConstant.ZhbGoodsType.CKZJXX);
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("type", "CKZJXX");
            boolean count = mobileExpertPageService.findDetails(map);
            resultMap.put("count", count);
            response.setData(resultMap);
        } catch (Exception e) {
            log.error("sel_resume_details error! ", e);
            response.setCode(400);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "sel_expert_details_list", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-专家首页-专家的技术成果详情", notes = "触屏端-专家首页-专家的技术成果详情", response = Response.class)
    public Response selExpertDetailsList(@ApiParam(value = "商品ID") @RequestParam(required = true) String id) {
        Response response = new Response();
        Map<String, Object> resultMap = new HashMap<>();
        try {
            getPrivilegeGoodsDetails(resultMap, id, ZhbConstant.ZhbGoodsType.CKZJJSCG);
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("type", "CKZJJSCG");
            boolean count = mobileExpertPageService.findDetails(map);
            resultMap.put("count", count);
            response.setData(resultMap);
        } catch (Exception e) {
            log.error("sel_expert_details_list error! ", e);
            response.setCode(400);
            response.setMessage(e.getMessage());

        }
        return response;
    }

    @RequestMapping(value = "sel_expert_train_list", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-专家首页-专家培训列表", notes = "触屏端-专家首页-专家培训列表", response = Response.class)
    public Response selExpertTrainList(@ApiParam(value = "省") @RequestParam(required = false) String province, @RequestParam(required = false, defaultValue = "1") String pageNo,
            @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        try {
            Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            Map<String, Object> condition = new HashMap<>();
            condition.put("province", province);
            condition.put("courseType", ExpertConstant.COURSE_TYPE_EXPERT);
            List<Map<String, String>> techList = mobileExpertPageService.findAllPublishCoursePager(pager, condition);
            pager.result(techList);
            response.setData(pager);
        } catch (Exception e) {
            log.error("sel_expert_train_list error! ", e);
            response.setCode(400);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "sel_publish_course_detail", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-专家首页-预览课程详情", notes = "触屏端-专家首页-预览课程详情", response = Response.class)
    public Response selPublishCourseDetail(@ApiParam(value = "培训课程ID") @RequestParam(required = false) Long courseId) {
        Response response = new Response();
        try {
            Map<String, Object> condition = new HashMap<>();
            condition.put("courseid", courseId);
            condition.put("courseType", ExpertConstant.COURSE_TYPE_EXPERT);
            Map<String, String> course = mobileExpertPageService.previewTrainCourseDetail(condition);
            response.setData(course);
        } catch (Exception e) {
            log.error("sel_publish_course_detail error! ", e);
            response.setCode(400);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "触屏端-专家首页-技术成果列表", notes = "触屏端-专家首页-技术成果列表", response = Response.class)
    @RequestMapping(value = "sel_achievement_list", method = RequestMethod.GET)
    public Response selAchievementList(@ApiParam(value = "系统分类") @RequestParam(required = false) String systemType, @ApiParam(value = "应用领域") @RequestParam(required = false) String useArea,
            @RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
        try {
            Paging<Achievement> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            Map<String, Object> map = new HashMap<>();
            // 查询传参
            map.put("systemType", systemType);
            map.put("useArea", useArea);
            map.put("type", ExpertConstant.EXPERT_TYPE_ONE);
            List<Achievement> achievementList = mobileExpertPageService.findAllAchievementList(pager, map);
            pager.result(achievementList);
            response.setData(pager);
        } catch (Exception e) {
            log.error("sel_achievement_list error! ", e);
            response.setCode(400);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "触屏端-专家首页-申请专家入驻", notes = "触屏端-专家首页-申请专家入驻", response = Response.class)
    @RequestMapping(value = "add_apply_expert", method = RequestMethod.POST)
    public Response addApplyExpert(@ModelAttribute Expert expert) {
        Response response = new Response();
        try {
            mobileExpertPageService.queryExpertByCreateid(expert);
        } catch (Exception e) {
            log.error("add_apply_expert error! ", e);
            response.setCode(400);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "触屏端-专家首页-申请专家支持", notes = "触屏端-专家首页-申请专家支持", response = Response.class)
    @RequestMapping(value = "add_expert_support", method = RequestMethod.POST)
    public Response addExpertSupport(@ApiParam(value = "联系人名称") @RequestParam String linkName, @ApiParam(value = "手机") @RequestParam(required = true) String mobile,
            @ApiParam(value = "验证码") @RequestParam(required = true) String code, @ApiParam(value = "申请原因") @RequestParam(required = false) String reason) {
        Response response = new Response();
        try {
            ExpertSupport expertSupport = new ExpertSupport();
            expertSupport.setLinkName(linkName);
            expertSupport.setMobile(mobile);
            expertSupport.setReason(reason);
            mobileExpertPageService.checkMobileCode(code, mobile, ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT, expertSupport);
        } catch (Exception e) {
            log.error("add_expert_support error! ", e);
            response.setCode(400);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "触屏端-专家首页-申请专家支持-手机验证码获取", notes = "触屏端-专家首页-申请专家支持-手机验证码获取", response = Response.class)
    @RequestMapping(value = "get_expert_support", method = RequestMethod.GET)
    public Response getExpertSupport(@ApiParam(value = "手机号码") @RequestParam(required = true) String mobile, @ApiParam(value = "图形验证码") @RequestParam(required = true) String imgCode) {
        Response response = new Response();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session sess = currentUser.getSession(true);
            String sessImgCode = (String) sess.getAttribute(ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT);
            response = mobileExpertPageService.getTrainMobileCode(mobile, ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT, imgCode, sessImgCode);
        } catch (Exception e) {
            log.error("get_expert_support error! ", e);
            response.setCode(400);
            response.setMessage(e.getMessage());

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
    public Response dynamicList(@RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        try {
            Paging<Dynamic> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            Map<String, Object> map = new HashMap<>();
            // 查询传参
            map.put("type", ExpertConstant.EXPERT_TYPE_ONE);
            List<Dynamic> dynamicList = mobileExpertPageService.findAllDynamicList(pager, map);
            pager.result(dynamicList);
            response.setData(pager);
        } catch (Exception e) {
            log.error("sel_dynamic_list error! ", e);
        }
        return response;
    }

    @ApiOperation(value = "触屏端-专家首页-协会动态详情", notes = "触屏端-专家首页-协会动态详情", response = Response.class)
    @RequestMapping(value = "sel_dynamic_details", method = RequestMethod.GET)
    public Response selDynamicDetails(@ApiParam(value = "协会动态Id") @RequestParam(required = true) String id) {
        Response response = new Response();
        try {
            Dynamic dynamic = mobileExpertPageService.queryDynamicById(id);
            response.setData(dynamic);
        } catch (Exception e) {
            log.error("sel_dynamic_details error! ", e);
        }
        return response;
    }

    @ApiOperation(value = "触屏端-专家首页-提交留言", notes = "触屏端-专家首页-提交留言", response = Response.class)
    @RequestMapping(value = "add_message", method = RequestMethod.POST)
    public Response addMessage(@ModelAttribute Messages message) throws Exception {
        Response response = new Response();
        try {
            Long createid = ShiroUtil.getCreateID();
            if (createid != null) {
                message.setCreateid(String.valueOf(createid));
                boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.GZJLY.toString());
                if (bool) {
                    mobileExpertPageService.addMessage(message);
                    zhbService.payForGoods(Long.parseLong(message.getId()), ZhbPaymentConstant.goodsType.GZJLY.toString());
                } else {// 支付失败稍后重试，联系客服
                    throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
                }
            } else {
                throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        } catch (Exception e) {
            log.error("add_message error! ", e);
            response.setCode(400);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "触屏端-专家首页-专家留言页面", notes = "触屏端-专家首页-给专家留言页面", response = Response.class)
    @RequestMapping(value = "add_message_page", method = RequestMethod.GET)
    public Response selMessagePage(@ApiParam(value = "专家的id") @RequestParam(required = true) String id) throws Exception {
        Response response = new Response();
        Map<String, Object> resultMap = new HashMap<>();
        try {
            getPrivilegeGoodsDetails(resultMap, id, ZhbConstant.ZhbGoodsType.GZJLY);
            Expert expert = mobileExpertPageService.findExpertById(id);
            resultMap.put("expert", expert);
            if (null != ShiroUtil.getCreateID()) {
                Long createId = ShiroUtil.getCreateID();
                Member member = mobileExpertPageService.findDetailsById(createId);
                resultMap.put("member", member);
            }
            response.setData(resultMap);
        } catch (Exception e) {
            log.error("add_message_page error! ", e);
            response.setCode(400);
            response.setMessage(e.getMessage());
        }

        return response;
    }
}
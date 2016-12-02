package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ExpertConstant;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.mobile.web.mc.MobileExpertController;
import com.zhuhuibao.mybatis.tech.entity.TechCooperation;
import com.zhuhuibao.mybatis.tech.entity.TechExpertCourse;
import com.zhuhuibao.mybatis.tech.service.DictionaryTechDataService;
import com.zhuhuibao.mybatis.tech.service.TechCooperationService;
import com.zhuhuibao.service.MobileTechService;
import com.zhuhuibao.service.payment.PaymentService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * User: zhuangyuhao
 * Date: 2016/11/23
 * Time: 14:09
 */
@RestController
@RequestMapping("/rest/m/tech/site/")
public class MobileTechSiteController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(MobileExpertController.class);

    @Autowired
    DictionaryTechDataService dicTDService;

    @Autowired
    MobileTechService mTechSV;

    @Autowired
    PaymentService paymentService;

    @Autowired
    TechCooperationService techCooSV;

    @RequestMapping(value = "sel_tech_cooperation", method = RequestMethod.GET)
    @ApiOperation(value = "查看技术成果，技术需求信息", notes = "查看技术成果，技术需求信息", response = Response.class)
    public Response queryByChannelInfo(@ApiParam(value = "1:技术成果，2：技术需求") @RequestParam(required = false) Integer type) {

        return new Response(mTechSV.getTechCoop(type, 6));
    }

    @RequestMapping(value = "sel_all_news", method = RequestMethod.GET)
    @ApiOperation(value = "查询新技术列表", notes = "查询新技术列表", response = Response.class)
    public Response findAllTechNewsList(@ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                        @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();
        response.setData(mTechSV.getNewTechList(pageNo, pageSize));
        return response;
    }

    @RequestMapping(value = "sel_new_tech_detail", method = RequestMethod.GET)
    @ApiOperation(value = "查询新技术详情", notes = "查询新技术详情", response = Response.class)
    public Response queryTechNewsDetail(@ApiParam(value = "新闻ID") @RequestParam String newsId) {
        Response response = new Response();
        response.setData(mTechSV.getNewTechDetail(newsId));
        return response;
    }

    @ApiOperation(value = "技术资料列表", notes = "技术资料列表", response = Response.class)
    @RequestMapping(value = "list_tech_data", method = RequestMethod.GET)
    public Response listTechDataPage(@ApiParam(value = "类别ID:1:解决方案 2:技术资料 3:培训资料") @RequestParam(required = false) String fcateId,
                                     @ApiParam(value = "子类别ID") @RequestParam(required = false) String scateId,
                                     @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                     @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) {

        return new Response(mTechSV.getTechDataList(fcateId, scateId, pageNo, pageSize));
    }

    @RequestMapping(value = "sel_tech_data_detail", method = RequestMethod.GET)
    @ApiOperation(value = "查询技术资料详情", notes = "查询技术资料详情", response = Response.class)
    public Response previewTechDataDetail(@ApiParam(value = "技术资料ID") @RequestParam() String techDataId) {
        Response response = new Response();
        response.setData(mTechSV.getTechDataDetail(techDataId));
        return response;
    }

    @ApiOperation(value = "查询技术合作详情", notes = "查询技术合作详情", response = Response.class)
    @RequestMapping(value = "sel_tech_coop_detail", method = RequestMethod.GET)
    public Response selTechCoopDetail(@ApiParam(value = "商品ID") @RequestParam Long GoodsID) throws Exception {
        Map map = new HashMap();
        TechCooperation techCoo = techCooSV.selectTechCooperationById(String.valueOf(GoodsID));
        Integer techType = techCoo.getType();   //1:技术成果，2：技术需求
        if (techType == 1) {
            getPrivilegeGoodsDetails(map, String.valueOf(GoodsID), ZhbConstant.ZhbGoodsType.CKJSCG);
        } else {
            Map<String, Object> m = new HashMap<>();
            m.put("id", GoodsID);
            m.put("type", 2);
            map = techCooSV.previewTechCooperationDetail(m);
            techCooSV.updateTechCooperationViews(String.valueOf(GoodsID));
        }
        return new Response(map);
    }

    @RequestMapping(value = "sel_tech_course_list", method = RequestMethod.GET)
    @ApiOperation(value = "查询最新发布的课程(默认10条)", notes = "查询最新发布的课程(默认10条)", response = Response.class)
    public Response findLatestPublishCourse(@ApiParam(value = "地区（省）") @RequestParam(required = false) String province,
                                            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                            @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        response.setData(mTechSV.getTechCoursePageList(pageNo, pageSize, province));
        return response;
    }

    @RequestMapping(value = "sel_tech_course_detail", method = RequestMethod.GET)
    @ApiOperation(value = "查看培训课程详情", notes = "查看培训课程详情", response = Response.class)
    public Response previewPublishCourseDetail(@ApiParam(value = "培训课程ID") @RequestParam Long courseId) {
        Response response = new Response();
        response.setData(mTechSV.getTechCourseDetail(courseId));
        return response;
    }

    @RequestMapping(value = "add_course", method = RequestMethod.POST)
    @ApiOperation(value = "申请技术培训课程", notes = "申请技术培训课程", response = Response.class)
    public Response insertTechTrainCourse(@ApiParam(value = "申请技术培训课程") @ModelAttribute(value = "techCourse") TechExpertCourse techCourse) {
        Response response = new Response();
        try {
            mTechSV.addCourse(techCourse);
        } catch (Exception e) {
            log.error("add_expert_support error! ", e);
            response.setData(400);
            response.setMessage(e.getMessage());
        }
        return response;
}

    @RequestMapping(value = "getTechIndexInfo", method = RequestMethod.GET)
    @ApiOperation(value = "获取手机端技术&培训-首页-信息", notes = "获取手机端技术&培训-首页-信息", response = Response.class)
    public Response insertTechTrainCourse() {
        Response response = new Response();
        response.setData(mTechSV.getTechIndexInfo());
        return response;
    }

    @RequestMapping(value = "sel_second_category", method = RequestMethod.GET)
    @ApiOperation(value = "查询解决方案、技术资料，培训资料行业类别", notes = "查询解决方案、技术资料，培训资料行业类别", response = Response.class)
    public Response selectSecondCategoryByFirstId() {
        Response response = new Response();
        List<Map<String, Object>> categoryList = dicTDService.selectCategoryInfo(0);
        response.setData(categoryList);
        return response;
    }

    @ApiOperation(value = "触屏端-专家首页-申请专家支持-手机验证码获取", notes = "触屏端-专家首页-申请专家支持-手机验证码获取", response = Response.class)
    @RequestMapping(value = "get_expert_support", method = RequestMethod.GET)
    public Response getExpertSupport(@ApiParam(value = "手机号码") @RequestParam(required = true) String mobile,
                                     @ApiParam(value = "图形验证码") @RequestParam(required = true) String imgCode) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        String sessImgCode = (String) sess.getAttribute(ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT);
        Response response = dicTDService.getTrainMobileCode(mobile, ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT, imgCode, sessImgCode);
        return response;
    }


}

package com.zhuhuibao.business.expert.site;

import com.taobao.api.ApiException;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.alipay.service.direct.AlipayDirectService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.aop.LoginAccess;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.*;
import com.zhuhuibao.common.util.ConvertUtil;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.constants.service.ConstantService;
import com.zhuhuibao.mybatis.expert.entity.*;
import com.zhuhuibao.mybatis.expert.service.ExpertNewService;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.payment.service.PaymentGoodsService;
import com.zhuhuibao.mybatis.tech.entity.TechExpertCourse;
import com.zhuhuibao.mybatis.tech.service.PublishTCourseService;
import com.zhuhuibao.mybatis.tech.service.TechExpertCourseService;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.service.payment.PaymentService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
import com.zhuhuibao.utils.oss.ZhbOssClient;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/5/18 0018.
 */
@RestController
@RequestMapping("/rest/expert/site")
public class ExpertSiteController {
    private static final Logger log = LoggerFactory
            .getLogger(ExpertSiteController.class);

    @Autowired
    ExpertNewService expertNewSV;
    
    @Autowired
    private ExpertService expertService;

    @Autowired
    private ConstantService constantService;

    @Autowired
    TechExpertCourseService techCourseService;

    @Autowired
    PublishTCourseService ptCourseService;

    @Autowired
    AlipayDirectService alipayDirectService;

    @Autowired
    MemberService memberService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentGoodsService goodsService;

    @Autowired
    ZhbOssClient zhbOssClient;

    @Autowired
    ZhbService zhbService;

    private static final String PARTNER = AlipayPropertiesLoader.getPropertyValue("partner");

    @ApiOperation(value="发布技术成果",notes="发布技术成果",response = Response.class)
    @RequestMapping(value = "ach/add_achievement", method = RequestMethod.POST)
    public Response publishAchievement(@ModelAttribute Achievement achievement) throws Exception {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if(createId!=null){
            achievement.setCreateId(String.valueOf(createId));
            expertService.publishAchievement(achievement);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }

  /*  @ApiOperation(value="技术成果详情",notes="技术成果详情",response = Response.class)
    @RequestMapping(value = "ach/sel_achievement", method = RequestMethod.GET)
    //    @ZhbAutoPayforAnnotation(goodsType=ZhbGoodsType.CKJSCG)
    public Response queryAchievementById(@ApiParam(value = "技术成果ID")@RequestParam String id) throws Exception {
        Map<String,String> map = expertService.queryAchievementById(id);
        Response response = paymentService.viewGoodsRecord(Long.parseLong(id),map,"expertcoop");
        return response;
    }*/

    @ApiOperation(value="技术成果列表(前台分页)",notes="技术成果列表(前台分页)",response = Response.class)
    @RequestMapping(value = "ach/sel_achievementList", method = RequestMethod.GET)
    public Response achievementList(@ApiParam(value = "系统分类")@RequestParam(required = false) String systemType,
                                    @ApiParam(value = "应用领域")@RequestParam(required = false)String useArea,
                                    @RequestParam(required = false)String pageNo,
                                    @RequestParam(required = false)String pageSize) throws Exception {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Achievement> pager = new Paging<Achievement>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("systemType",systemType);
        map.put("useArea",useArea);
        map.put("type", ExpertConstant.EXPERT_TYPE_ONE);
        List<Achievement> achievementList = expertService.findAllAchievementList(pager,map);
        List list = new ArrayList();
        for(Achievement achievement:achievementList){
            Map m = new HashMap();
            m.put("id",achievement.getId());
            m.put("title",achievement.getTitle());
            m.put("updateTime",achievement.getUpdateTime());
            list.add(m);
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="技术成果列表(前台)控制条数",notes="技术成果列表(前台)控制条数",response = Response.class)
    @RequestMapping(value = "ach/sel_achievementList_count", method = RequestMethod.GET)
    public Response achievementListByCount(@ApiParam(value = "条数")@RequestParam int count) throws Exception {
        Response response = new Response();
        List<Map<String,String>> achievementList = expertService.findAchievementListByCount(count);
        response.setData(achievementList);
        return response;
    }

    @ApiOperation(value="协会动态详情",notes="协会动态详情",response = Response.class)
    @RequestMapping(value = "dynamic/sel_dynamic", method = RequestMethod.GET)
    public Response queryDynamicById(@ApiParam(value = "协会动态Id")@RequestParam String id) throws Exception {
        Response response = new Response();
        Dynamic dynamic = expertService.queryDynamicById(id);
        if(dynamic!=null){
            dynamic.setViews(String.valueOf(Integer.parseInt(dynamic.getViews())+1));
            expertService.updateDynamicViews(dynamic);
            response.setData(dynamic);
        }else {
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR,"页面不存在");
        }
        return response;
    }

    @ApiOperation(value="协会动态列表(前台分页)",notes="协会动态列表(前台分页)",response = Response.class)
    @RequestMapping(value = "dynamic/sel_dynamicList", method = RequestMethod.GET)
    public Response dynamicList(@RequestParam(required = false)String pageNo,
                                @RequestParam(required = false)String pageSize) throws Exception {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Dynamic> pager = new Paging<Dynamic>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("type",ExpertConstant.EXPERT_TYPE_ONE);
        List<Dynamic> dynamicList = expertService.findAllDynamicList(pager,map);
        List list = new ArrayList();
        for(Dynamic dynamic:dynamicList){
            Map m = new HashMap();
            m.put("id",dynamic.getId());
            m.put("title",dynamic.getTitle());
            m.put("updateTime",dynamic.getUpdateTime());
            list.add(m);
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="协会动态列表(前台)控制条数",notes="协会动态列表(前台)控制条数",response = Response.class)
    @RequestMapping(value = "dynamic/sel_dynamicList_count", method = RequestMethod.GET)
    public Response dynamicListByCount(@ApiParam(value = "条数")@RequestParam int count) throws Exception {
        Response response = new Response();
        List<Map<String,String>> dynamicList = expertService.findDynamicListByCount(count);
        response.setData(dynamicList);
        return response;
    }

    @ApiOperation(value="申请专家",notes="申请专家",response = Response.class)
    @RequestMapping(value = "base/add_expert", method = RequestMethod.POST)
    public Response applyExpert(@ModelAttribute Expert expert) throws Exception {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if(createId!=null){
            expert.setCreateId(String.valueOf(createId));
            Expert expert1 = expertService.queryExpertByCreateid(createId.toString());
            if(expert1==null){
                expertService.applyExpert(expert);
            }else {
                throw new BusinessException(MsgCodeConstant.EXPERT_ISEXIST,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.EXPERT_ISEXIST)));
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value="专家上传照片",notes="专家上传照片",response = Response.class)
    @RequestMapping(value = "base/upload_photo", method = RequestMethod.POST)
    public Response uploadPhoto(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        Response response = new Response();
        String url = zhbOssClient.uploadObject(file,"img","expert");
        response.setData(url);
        return response;
    }

   /* @ApiOperation(value="专家详情(前台)",notes="专家详情(前台)",response = Response.class)
    @RequestMapping(value = "base/sel_expert", method = RequestMethod.GET)
    public Response expertInfo(@ApiParam(value = "专家id")@RequestParam String id) {
        Response response = new Response();
        Expert expert = expertService.queryExpertById(id);
        //返回到页面
        Map map = new HashMap();
        map.put("id",expert.getId());
        map.put("name",expert.getName());
        map.put("company",expert.getCompany());
        map.put("position",expert.getPosition());
        map.put("title",expert.getTitle());
        map.put("photo",expert.getPhotoUrl());
        map.put("province",expert.getProvinceName());
        map.put("city",expert.getCityName());
        map.put("area",expert.getAreaName());
        map.put("hot",expert.getViews());
        map.put("introduce",expert.getIntroduce());
        //技术成果
        Map<String,Object> achievementMap = new HashMap<>();
        //查询传参
        achievementMap.put("createId",expert.getCreateId());
        achievementMap.put("status",ExpertConstant.EXPERT_ACHIEVEMENT_STATUS_ONE);
        List<Achievement> achievementList = expertService.findAchievementList(achievementMap);
        List list = new ArrayList();
        for(Achievement achievement:achievementList){
            Map m = new HashMap();
            m.put("id",achievement.getId());
            m.put("title",achievement.getTitle());
            m.put("updateTime",achievement.getUpdateTime());
            list.add(m);
        }
        map.put("achievementList",list);

        Long createid = ShiroUtil.getCreateID();
        Long comanyId = ShiroUtil.getCompanyID();
        if(createid!=null){
            //查询该专家是否已被查看过
            Map<String,Object> con = new HashMap<String,Object>();
            con.put("goodsId",id);
            con.put("companyId",comanyId);
            con.put("type","expert");
            int count = goodsService.checkIsViewGoods(con);
            if(count > 0){
                map.put("address",expert.getAddress());
                map.put("telephone",expert.getTelephone());
                map.put("mobile",expert.getMobile());
                map.put("isLook",true);
            }else {
                map.put("address","");
                map.put("telephone","");
                map.put("mobile","");
                map.put("isLook",false);
            }
        }else {
            map.put("address","");
            map.put("telephone","");
            map.put("mobile","");
            map.put("isLook",false);
        }

        response.setData(map);
        //点击率加1
        expert.setViews(String.valueOf(Integer.parseInt(expert.getViews())+1));
        expertService.updateExpertViews(expert);
        return response;
    }*/

  /*  @ApiOperation(value="专家联系方式详情(前台)",notes="专家联系方式详情(前台)",response = Response.class)
    @RequestMapping(value = "base/sel_expert_contact", method = RequestMethod.GET)
    public Response expertContactInfo(@ApiParam(value = "专家id")@RequestParam String id)  {
        Response response = new Response();
        Long createid = ShiroUtil.getCreateID();
        Long companyId = ShiroUtil.getCompanyID();
        if(createid!=null){
            //记录查看专家
            goodsService.insertViewGoods(Long.parseLong(id),createid,companyId,"expert");
            //查询专家联系方式
            Expert expert = expertService.queryExpertById(id);
            //返回到页面
            Map map = new HashMap();
            map.put("address",expert.getAddress());
            map.put("telephone",expert.getTelephone());
            map.put("mobile",expert.getMobile());
            response.setData(map);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }*/

    @ApiOperation(value="专家列表(前台分页)",notes="专家列表(前台分页)",response = Response.class)
    @RequestMapping(value = "base/sel_expertList", method = RequestMethod.GET)
    public Response expertList(@ApiParam(value = "省")@RequestParam(required = false) String province,
                               @ApiParam(value = "专家类型")@RequestParam(required = false) String expertType,
                               @RequestParam(required = false,defaultValue = "1")String pageNo,
                               @RequestParam(required = false,defaultValue = "10")String pageSize)  {
        Response response = new Response();

        Paging<Map<String,Object>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("province",province);
        map.put("expertType",expertType);
        map.put("type",ExpertConstant.EXPERT_TYPE_ONE);
        List<Map<String,Object>> expertList = expertService.findAllExpert(pager,map);
        for (Map<String,Object> expert : expertList) {
            String provinceCode = String.valueOf(expert.get("province"));
            if(!StringUtils.isEmpty(provinceCode)){
                ConvertUtil.execute(expert, "province", "dictionaryService", "findProvinceByCode", new Object[]{provinceCode});
            }else{
                expert.put("provinceName","");
            }
            String cityCode =  String.valueOf(expert.get("city"));
            if(!StringUtils.isEmpty(cityCode)){
                ConvertUtil.execute(expert, "city", "dictionaryService", "findCityByCode", new Object[]{cityCode});
            }else{
                expert.put("cityName","");
            }
            String areaCode =  String.valueOf(expert.get("area"));
            if(!StringUtils.isEmpty(areaCode)){
                ConvertUtil.execute(expert, "area", "dictionaryService", "findAreaByCode", new Object[]{areaCode});
            }else{
                expert.put("areaName","");
            }


        }
        pager.result(expertList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="热门专家(前台)",notes="热门专家(前台)",response = Response.class)
    @RequestMapping(value = "base/sel_hot_expert", method = RequestMethod.GET)
    public Response queryHotExpert(@ApiParam(value = "条数")@RequestParam(required = false) int count) {
        Response response = new Response();
        List<Expert> expertList = expertService.queryHotExpert(count);
        List list = new ArrayList();
        for (Expert expert : expertList) {
            Map expertMap = new HashMap();
            expertMap.put("id", expert.getId());
            expertMap.put("name", expert.getName());
            expertMap.put("company", expert.getCompany());
            expertMap.put("position", expert.getPosition());
            expertMap.put("photo", expert.getPhotoUrl());
            list.add(expertMap);
        }
        response.setData(list);
        return response;
    }

    @ApiOperation(value="最新专家(前台)",notes="最新专家(前台)",response = Response.class)
    @RequestMapping(value = "base/sel_latest_expert", method = RequestMethod.GET)
    public Response queryLatestExpert(@ApiParam(value = "条数")@RequestParam(required = false) int count) {
        Response response = new Response();
        List<Expert> expertList = expertService.queryLatestExpert(count);
        List list = new ArrayList();
        for (Expert expert : expertList) {
            Map expertMap = new HashMap();
            expertMap.put("createid", expert.getCreateId());
            expertMap.put("id", expert.getId());
            expertMap.put("name", expert.getName());
            expertMap.put("company", expert.getCompany());
            expertMap.put("position", expert.getPosition());
            expertMap.put("photo", expert.getPhotoUrl());
            expertMap.put("hot", expert.getViews());
            expertMap.put("introduce", expert.getIntroduce());
            list.add(expertMap);
        }
        response.setData(list);
        return response;
    }

    /**
     * 向专家提问时的图形验证码
     * @param response
     */
    @ApiOperation(value="向专家提问时的图形验证码",notes="向专家提问时的图形验证码")
    @RequestMapping(value = "base/imgCode", method = RequestMethod.GET)
    public void getCode(HttpServletResponse response) {
        log.debug("获得验证码");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        log.debug("verifyCode == " + verifyCode);
        sess.setAttribute("expert", verifyCode);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            int w = 100;// 定义图片的width
            int h = 40;// 定义图片的height
            VerifyCodeUtils.outputImage1(w, h, out, verifyCode);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value="向专家咨询(前台)",notes="向专家咨询(前台)",response = Response.class)
    @RequestMapping(value = "base/add_askExpert", method = RequestMethod.POST)
    public Response askExpert(@ApiParam(value = "咨询内容")@RequestParam String content,
                              @ApiParam(value = "验证码")@RequestParam String code)  {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
        if(null != sess) {
            String verifyCode = (String) sess.getAttribute("expert");
            if(!code.equalsIgnoreCase(verifyCode)){
                throw new BusinessException(MsgCodeConstant.validate_error,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.validate_error)));
            }else {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)sess.getAttribute("member");
                if(null != principal){
                    Question question = new Question();
                    question.setCreateid(principal.getId().toString());

                    question.setContent(content);
                    expertService.askExpert(question);
                }else {
                    throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                }
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value="專家互動(前台)",notes="專家互動(前台)",response = Response.class)
    @RequestMapping(value = "base/sel_expertInteraction", method = RequestMethod.GET)
    public Response expertInteraction(@RequestParam int count)  {
        Response response = new Response();
        List<Map<String,String>> list = expertService.expertInteraction(count);
        response.setData(list);
        return response;
    }

    @ApiOperation(value="系統分類常量",notes="系統分類常量",response = Response.class)
    @RequestMapping(value = "base/sel_systemList", method = RequestMethod.GET)
    public Response SystemList()  {
        Response response = new Response();
        List<Map<String,String>> list = constantService.findByType(ExpertConstant.EXPERT_SYSTEM_TYPE);
        response.setData(list);
        return response;
    }

    @ApiOperation(value="應用領域常量",notes="應用領域常量",response = Response.class)
    @RequestMapping(value = "base/sel_useAreaList", method = RequestMethod.GET)
    public Response useAreaList()  {
        Response response = new Response();
        List<Map<String,String>> list = constantService.findByType(ExpertConstant.EXPERT_USEAREA_TYPE);
        response.setData(list);
        return response;
    }

    @ApiOperation(value="申請專家支持",notes="申請專家支持",response = Response.class)
    @RequestMapping(value = "base/add_expertSupport", method = RequestMethod.POST)
    public Response applyExpertSupport(@ApiParam(value = "联系人名称")@RequestParam String linkName,
                                       @ApiParam(value = "手机")@RequestParam String mobile,
                                       @ApiParam(value = "验证码")@RequestParam String code,
                                       @ApiParam(value = "申请原因")@RequestParam(required = false) String reason)  {
        Response response = new Response();
        ExpertSupport expertSupport = new ExpertSupport();
        expertSupport.setLinkName(linkName);
        expertSupport.setMobile(mobile);
        expertSupport.setReason(reason);
        expertService.checkMobileCode(code,mobile,ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT);
        Long createId = ShiroUtil.getCreateID();
        if(createId!=null){
            expertSupport.setCreateid(String.valueOf(createId));
            expertService.applyExpertSupport(expertSupport);
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }

    @ApiOperation(value="申請專家支持获取验证码",notes="申請專家支持获取验证码",response = Response.class)
    @RequestMapping(value = "base/get_mobileCode", method = RequestMethod.GET)
    public Response get_mobileCode(@ApiParam(value = "手机号码") @RequestParam String mobile,
                                   @ApiParam(value ="图形验证码") @RequestParam String imgCode)  throws IOException, ApiException {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        String sessImgCode = (String) sess.getAttribute(ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT);
        if(imgCode.equalsIgnoreCase(sessImgCode)) {
            String verifyCode = expertService.getTrainMobileCode(mobile,ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT);
        }else{
            throw new BusinessException(MsgCodeConstant.validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.validate_error)));
        }
        return response;
    }

    @ApiOperation(value="申請專家支持图形验证码",notes="申請專家支持图形验证码",response = Response.class)
    @RequestMapping(value = "train/sel_supportImgCode", method = RequestMethod.GET)
    public void getApplySupportCode(HttpServletResponse response) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
        String verifyCode = VerifyCodeUtils.outputHttpVerifyImage(100,40,response, Constants.CHECK_IMG_CODE_SIZE);
        sess.setAttribute(ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT, verifyCode);
    }

    @ApiOperation(value="最新专家培训",notes="最新专家培训",response = Response.class)
    @RequestMapping(value = "train/sel_latest_train", method = RequestMethod.GET)
    public Response queryLatestExpertTrain(@ApiParam(value = "条数")@RequestParam int count)  {
        Response response = new Response();
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("status", TechConstant.PublishCourseStatus.SALING.toString());
        condition.put("courseType",ExpertConstant.COURSE_TYPE_EXPERT);
        condition.put("count",count);
        List<Map<String,String>> courseList = ptCourseService.findLatestPublishCourse(condition);
        response.setData(courseList);
        return response;
    }

    @ApiOperation(value="专家培训列表",notes="专家培训列表",response = Response.class)
    @RequestMapping(value = "train/sel_trainList", method = RequestMethod.GET)
    public Response queryExpertTrainList(@ApiParam(value = "省")@RequestParam(required = false) String province,
                                         @RequestParam(required = false)String pageNo,
                                         @RequestParam(required = false)String pageSize)  {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("province",province);
        condition.put("courseType", ExpertConstant.COURSE_TYPE_EXPERT);
        //销售中
        condition.put("status", TechConstant.PublishCourseStatus.SALING.toString());
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String, String>> techList = ptCourseService.findAllPublishCoursePager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="开课申请保存",notes="开课申请保存",response = Response.class)
    @RequestMapping(value = "train/add_class", method = RequestMethod.POST)
    public Response startClassSave(@ApiParam(value = "开课申请保存")  @ModelAttribute(value="techCourse")TechExpertCourse techCourse)  {
        Response response = new Response();
        //时间格式校验
        checkParams(techCourse);

        expertService.checkMobileCode(techCourse.getCode(),techCourse.getMobile(),ExpertConstant.MOBILE_CODE_SESSION_TYPE_CLASS);
        Long createId = ShiroUtil.getCreateID();
        if(createId != null) {
            techCourse.setProposerId(createId);
            techCourseService.insertTechExpertCourse(techCourse);
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    /**
     * 参数校验
     * @param techCourse
     */
    private void checkParams(TechExpertCourse techCourse) {
        //时间格式校验
        String startTime = techCourse.getStartTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            sf.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR,"开始时间格式不正确");
        }
        String endTime = techCourse.getEndTime();
        try {
            sf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR,"结束时间格式不正确");
        }
    }

    @ApiOperation(value="开课申请获取验证码",notes="开课申请获取验证码",response = Response.class)
    @RequestMapping(value = "train/get_classMobileCode", method = RequestMethod.GET)
    public Response get_classMobileCode(@ApiParam(value = "手机号码") @RequestParam String mobile,
                                        @ApiParam(value ="图形验证码") @RequestParam String imgCode) throws IOException, ApiException{
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        String sessImgCode = (String) sess.getAttribute(ExpertConstant.MOBILE_CODE_SESSION_TYPE_CLASS);
        if(imgCode.equalsIgnoreCase(sessImgCode)) {
            expertService.getTrainMobileCode(mobile,ExpertConstant.MOBILE_CODE_SESSION_TYPE_CLASS);
        }else{
            throw new BusinessException(MsgCodeConstant.validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.validate_error)));
        }
        return response;
    }


    @ApiOperation(value="申请开课图形验证码",notes="申请开课的图形验证码",response = Response.class)
    @RequestMapping(value = "train/sel_applyImgCode", method = RequestMethod.GET)
    public void getApplyImgCode(HttpServletResponse response) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
        String verifyCode = VerifyCodeUtils.outputHttpVerifyImage(100,40,response, Constants.CHECK_IMG_CODE_SIZE);
        sess.setAttribute(ExpertConstant.MOBILE_CODE_SESSION_TYPE_CLASS, verifyCode);
    }

    @ApiOperation(value="专家培训详情",notes="专家培训详情",response = Response.class)
    @RequestMapping(value = "train/sel_train_info", method = RequestMethod.GET)
    public Response queryExpertTrainInfoById(@RequestParam String id)  {
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("courseid",id);
        condition.put("courseType",ExpertConstant.COURSE_TYPE_EXPERT);
        Map<String,String> course = ptCourseService.previewTrainCourseDetail(condition);
        Response response = new Response();
        response.setData(course);
        return response;
    }


    @ApiOperation(value="专家培训课程下单获取验证码",notes="专家培训课程下单获取验证码",response = Response.class)
    @RequestMapping(value = "train/get_mobileCode", method = RequestMethod.GET)
    public Response get_TrainMobileCode(@ApiParam(value = "手机号码") @RequestParam String mobile,
                                        @ApiParam(value ="图形验证码") @RequestParam String imgCode) throws IOException, ApiException{
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        String sessImgCode = (String) sess.getAttribute(ExpertConstant.MOBILE_CODE_SESSION_TYPE_TRAIN);
        if(imgCode.equalsIgnoreCase(sessImgCode)) {
            expertService.getTrainMobileCode(mobile,ExpertConstant.MOBILE_CODE_SESSION_TYPE_TRAIN);
        }else{
            throw new BusinessException(MsgCodeConstant.validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.validate_error)));
        }
        return response;
    }

    @ApiOperation(value="专家培训课程下单验证验证码是否正确",notes="专家培训课程下单验证验证码是否正确",response = Response.class)
    @RequestMapping(value = "train/check_mobileCode", method = RequestMethod.POST)
    public Response check_mobileCode(@RequestParam String code,@RequestParam String mobile)  {
        Response response = new Response();
        expertService.checkMobileCode(code,mobile,ExpertConstant.MOBILE_CODE_SESSION_TYPE_TRAIN);
        return response;
    }

    @ApiOperation(value="专家培训课程下单图形验证码",notes="专家培训课程下单图形验证码",response = Response.class)
    @RequestMapping(value = "train/sel_orderImgCode", method = RequestMethod.GET)
    public void getOrderImgCode(HttpServletResponse response) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
        String verifyCode = VerifyCodeUtils.outputHttpVerifyImage(100,40,response, Constants.CHECK_IMG_CODE_SIZE);
        sess.setAttribute(ExpertConstant.MOBILE_CODE_SESSION_TYPE_TRAIN, verifyCode);
    }

    @ApiOperation(value="给专家留言",notes="给专家留言",response = Response.class)
    @RequestMapping(value = "base/add_message", method = RequestMethod.POST)
    public Response message(@ModelAttribute Message message) throws Exception {
        Response response = new Response();
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            message.setCreateid(String.valueOf(createid));
            boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.GZJLY.toString());
            if(bool) {
                memberService.saveMessage(message);
                zhbService.payForGoods(Long.parseLong(message.getId()),ZhbPaymentConstant.goodsType.GZJLY.toString());
            }else{//支付失败稍后重试，联系客服
                throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查询問題列表", notes = "查询問題列表", response = Response.class)
    @RequestMapping(value = "base/sel_questionList", method = RequestMethod.GET)
    public Response queryExpertQuestion(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                        @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String, String>> questionList = expertService.findAllQuestionList(pager);
        pager.result(questionList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "查询問題详情", notes = "查询問題详情", response = Response.class)
    @RequestMapping(value = "base/sel_myQuestion", method = RequestMethod.GET)
    public Response queryMyQuestionById(@ApiParam(value = "問題id") @RequestParam String id) {
        Response response = new Response();
        Map map = expertService.queryMyQuestionById(id);
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            Map<String,String> map1 = expertService.queryQuestionById(id);
            if(createid.equals(map1.get("createid"))){
                map.put("isCan",1);
            }else {
                map.put("isCan",0);
            }
        }else {
            map.put("isCan",0);
        }
        response.setData(map);
        return response;
    }

    @ApiOperation(value = "关闭问题", notes = "关闭问题", response = Response.class)
    @RequestMapping(value = "base/upd_closeQuestion", method = RequestMethod.POST)
    @LoginAccess
    public Response closeQuestion(@ApiParam(value = "問題id") @RequestParam String id) {
        Response response = new Response();
        Question question = new Question();
        Long createid = ShiroUtil.getCreateID();
        Map<String, String> map1 = expertService.queryQuestionById(id);
        if (createid.equals(map1.get("createid"))) {
            question.setId(id);
            //狀態設為已關閉
            question.setStatus(ExpertConstant.EXPERT_QUESTION_STATUS_TWO);
            expertService.updateQuestionInfo(question);
        }
        return response;
    }

    @ApiOperation(value = "采纳答案", notes = "采纳答案", response = Response.class)
    @RequestMapping(value = "base/upd_acceptAnswer", method = RequestMethod.POST)
    @LoginAccess
    public Response acceptAnswer(@ApiParam(value = "問題id") @RequestParam String questionId,
                                 @ApiParam(value = "答案id") @RequestParam String answerId) {
        Response response = new Response();
        Question question = new Question();
        Long createid = ShiroUtil.getCreateID();
        Map<String, String> map1 = expertService.queryQuestionById(questionId);
        if (createid.equals(map1.get("createid"))) {
            question.setId(questionId);
            //設置採納答案id
            question.setAnswerId(answerId);
            //狀態設為已關閉
            question.setStatus(ExpertConstant.EXPERT_QUESTION_STATUS_FOUR);
            expertService.updateQuestionInfo(question);
        }
        return response;
    }
    
    @ApiOperation(value = "查询专家是否能回答该问题", notes = "查询专家是否能回答该问题", response = Response.class)
    @RequestMapping(value = "base/query_exp_can_or_not_answer", method = RequestMethod.GET)
    @LoginAccess
    public Response queryExpPrivilege(@ApiParam(value = "问题id") @RequestParam String questionId) {
    	
    	return expertNewSV.queryExpPrivilege(questionId);
    }
    
    
    @ApiOperation(value = "专家回答问题", notes = "专家回答问题", response = Response.class)
    @RequestMapping(value = "base/exp_add_answer", method = RequestMethod.POST)
    @LoginAccess
    public Response acceptAnswer(@ModelAttribute Answer answer) {
    	
    	return expertNewSV.addExpAnswer(answer);
    }
    
}

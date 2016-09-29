package com.zhuhuibao.business.project.site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.fsearch.pojo.spec.ProjectSearchSpec;
import com.zhuhuibao.fsearch.service.impl.ProjectFSService;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.advertising.service.SysAdvertisingService;
import com.zhuhuibao.mybatis.category.service.CategoryService;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.service.payment.PaymentService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/rest/project")
@Api(value = "Project", description = "项目信息")
public class ProjectController {
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    ProjectService projectService;

    @Autowired
    private MemberService memberService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    ProjectFSService projectfsService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private SysAdvertisingService advService;

    @Autowired
    BrandService brandService;
    /**
     * 根据条件查询项目分页信息
     *
     * @param name 项目信息
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = {"searchProjectPage", "site/base/sel_projectPage"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据条件查询项目分页信息", notes = "根据条件查询分页", response = Response.class)
    public Response searchProjectPage(@ApiParam(value = "项目名称") @RequestParam(required = false) String name,
                                      @ApiParam(value = "城市Code") @RequestParam(required = false) String city,
                                      @ApiParam(value = "省代码") @RequestParam(required = false) String province,
                                      @ApiParam(value = "项目类别") @RequestParam(required = false) String category,
                                      @ApiParam(value = "开工日期查询开始日期") @RequestParam(required = false) String startDateA,
                                      @ApiParam(value = "开工日期查询结束日期") @RequestParam(required = false) String startDateB,
                                      @ApiParam(value = "竣工日期查询开始日期") @RequestParam(required = false) String endDateA,
                                      @ApiParam(value = "竣工日期查询结束日期") @RequestParam(required = false) String endDateB,
                                      @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        //封装查询参数
        Map<String, Object> map = new HashMap<>();
        if (name != null && !"".equals(name)) {
            map.put("name", name.replace("_", "\\_"));
        }
        map.put("city", city);
        map.put("province", province);
        map.put("category", category);
        map.put("startDateA", startDateA);
        map.put("startDateB", startDateB);
        map.put("endDateA", endDateA);
        map.put("endDateB", endDateB);
        log.info("查询工程信息：queryProjectInfo", map);
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        //调用查询接口
        List<Map<String, String>> projectList = projectService.findAllProject(map, pager);
        pager.result(projectList);
        return new Response(pager);
    }


    @RequestMapping(value = {"searchProjectPage1", "site/base/sel_project_page"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据条件查询项目信息(分页)", notes = "根据条件查询项目信息(分页)", response = Response.class)
    public Response searchProjectPage(@ApiParam("搜索条件") @ModelAttribute ProjectSearchSpec spec) {
        if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
            spec.setLimit(12);
        }
        Response response = new Response();
        response.setCode(200);
        Map<String, Object> ret;
        try {
            ret = projectfsService.searchProjectPage(spec);
            response.setMsgCode(1);
            response.setMessage("OK!");
            response.setData(ret);
        } catch (Exception e) {
            response.setMsgCode(0);
            response.setMessage("search error!");
        }
        return response;
    }

    @RequestMapping(value = {"previewUnLoginProject", "site/base/sel_unLoginProject"}, method = RequestMethod.GET)
    @ApiOperation(value = "预览未登陆的项目信息", notes = "根据Id查看未登陆的项目信息", response = Response.class)
    public Response previewUnLoginProject(@ApiParam(value = "项目信息ID") @RequestParam Long porjectID) throws Exception {
        Map<String, Object> map = projectService.previewUnLoginProject(porjectID);
        return new Response(map);
    }


    @RequestMapping(value = {"greatCompany", "site/base/sel_greatCompany"}, method = RequestMethod.GET)
    @ApiOperation(value = "优秀工程商", notes = "优秀工程商", response = Response.class)
    public Response sel_great_manufacturer(@ApiParam(value = "频道类型 4：项目") @RequestParam String chanType,
                                           @ApiParam(value = "频道下子页面:index") @RequestParam String page,
                                           @ApiParam(value = "广告所在区域:F1:优秀工程商") @RequestParam String advArea) {
        /*Response response = new Response();
        Map<String, Object> map = new HashMap<>();
        map.put("chanType", chanType);
        map.put("page", page);
        map.put("advArea", advArea);
        List<Map<String, String>> list = memberService.queryGreatCompany(map);
        response.setData(list);
        return response;*/
        
    	Map<String, Object> result = new HashMap<>();

        List<Map<String, String>> comList = new ArrayList<>();
        List<Map<String, String>> brandList = new ArrayList<>();
        List<Map<String, String>> otherList = new ArrayList<>();
        
        //校验advArea
        if (!advArea.contains("F") || advArea.length() < 2) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "传值不正确");
        }
        String pid = advArea.substring(1, advArea.length());
        if (!StringUtils.isEmpty(pid)) {
            List<Map<String, String>> scategory = categoryService.findSubSystemByPid(pid);
            result.put("scategory", scategory);
        } else {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "传值不正确");
        }
        
        List<SysAdvertising> advertisings = advService.findListByCondition(chanType, page, advArea);
        if(null != advertisings && advertisings.size() > 0){
        	for (SysAdvertising advertising : advertisings) {
                Map<String, String> comMap = new HashMap<>();
                Map<String, String> brandMap = new HashMap<>();
                Map<String, String> otherMap = new HashMap<>();

                switch (advertising.getAdvType()) {
                    case "company":
                        comMap.put("imgUrl", advertising.getImgUrl());
                        comMap.put("linkUrl", advertising.getLinkUrl());
                        comMap.put("title", advertising.getTitle());
                        String id = advertising.getConnectedId();  //关联ID(关联用户的ID,关联产品的产品ID,关联品牌的品牌ID)

                        comMap.put("comId", id);
                        Member member = memberService.findMemById(id);
                        if (member != null) {
                            String comName = member.getEnterpriseName();
                            comMap.put("comName", comName == null ? "" : comName);
                        } else {
                            comMap.put("comName", "");
                        }

                        comList.add(comMap);
                        break;
                    case "brand":
                        brandMap.put("imgUrl", advertising.getImgUrl());
                        brandMap.put("linkUrl", advertising.getLinkUrl());
                        brandMap.put("title", advertising.getTitle());
                        brandMap.put("id", advertising.getConnectedId());
                        //根据品牌查询子系统ID    scateid
                        List<String> scateIds = brandService.findScateIdByBrandId(advertising.getConnectedId());
                        if(scateIds.size() > 0){
                            brandMap.put("scateid", StringUtils.isEmpty(scateIds.get(0)) ? "" : scateIds.get(0));
                        }else{
                            brandMap.put("scateid","");
                        }

                        brandList.add(brandMap);
                        break;
                    case "other":
                        otherMap.put("imgUrl", advertising.getImgUrl());
                        otherMap.put("linkUrl", advertising.getLinkUrl());
                        otherMap.put("title", advertising.getTitle());
                        otherMap.put("id", advertising.getConnectedId());
                        otherList.add(otherMap);
                        break;
                }
            }
        }else{
        	throw new BusinessException(MsgCodeConstant.NOT_EXIST_ADV, "暂未设置广告信息");
        }

        result.put("company", comList);
        result.put("brand", brandList);
        result.put("other", otherList);

        return new Response(result);
        
    }

    @RequestMapping(value = {"queryLatestProject", "site/base/sel_latestProject"}, method = RequestMethod.GET)
    @ApiOperation(value = "查询最新项目信息或者搜索时的推荐，默认10条", notes = "查询最新项目信息，默认10条", response = Response.class)
    public Response queryLatestProject(@ApiParam(value = "查询条数") @RequestParam(required = false, defaultValue = "10") String count) throws IOException {
        Response response = new Response();
        List<Map<String, Object>> projectList = projectService.queryLatestNProject(count);
        response.setData(projectList);
        return response;
    }
}

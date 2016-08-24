package com.zhuhuibao.business.contractor;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.expo.service.ExpoService;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.SuccessCaseService;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.mybatis.tech.service.TechCooperationService;
import com.zhuhuibao.mybatis.witkey.service.CooperationService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/rest/contractor/site")
@Api(value = "Contractor",description = "工程商频道")
public class ContractorController {
    private static final Logger log = LoggerFactory.getLogger(ContractorController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private SuccessCaseService successCaseService;
    
    @Autowired
    ChannelNewsService newsService;
    @Autowired
    private ExpoService expoService;

    @Autowired
    private CooperationService cooperationService;

    @Autowired
    private TechCooperationService techCooperationService;

    @Autowired
    private JobPositionService jobPositionService;

    /**
     *最新工程商(个数后台控制)
     * @return
     */
    @ApiOperation(value = "最新工程商",notes = "最新工程商")
    @RequestMapping(value = "sel_new_engineer", method = RequestMethod.GET)
    public Response newEngineer()  {
        String type = "4";
        Response response = new Response();
        List list = memberService.findNewEngineerOrSupplier(type);
        response.setData(list);
        return response;
    }

    /**
     *工程商简版介绍
     * @return
     */
    @ApiOperation(value = "工程商简版介绍", notes = "工程商简版介绍")
    @RequestMapping(value = "sel_simple_introduce", method = RequestMethod.GET)
    public Response introduce(@ApiParam("id") @RequestParam String id) {
        Response response = new Response();
        Map map = memberService.introduce(id,"2"); //资质类型：1：供应商资质；2：工程商资质；3：个人资质
        response.setData(map);
        return response;
    }

    /**
     *工程商简版介绍
     * @return
     */
    @ApiOperation(value = "Vip工程商简版介绍", notes = "Vip工程商简版介绍")
    @RequestMapping(value = "sel_vip_simple_introduce", method = RequestMethod.GET)
    public Response vipIntroduce(@ApiParam("id") @RequestParam String id) {
        Response response = new Response();
        Map map = memberService.vipIntroduce(id,"2"); //资质类型：1：供应商资质；2：工程商资质；3：个人资质
        if(map==null){
        	response.setCode(400);
        	response.setMessage("非vip用户不能查询vip工程商信息");
        }else{
        	response.setData(map);
        }
        return response;
    }
    @ApiOperation(value = "公司成功案例（分页）", notes = "公司成功案例（分页）")
    @RequestMapping(value = "sel_company_success_caseList", method = RequestMethod.GET)
    public Response sel_company_success_caseList(@ApiParam(value = "公司id")@RequestParam String id,
                                                 @RequestParam(required = false,defaultValue = "1") String pageNo,
                                                 @RequestParam(required = false,defaultValue = "10") String pageSize)  {
        Response response = new Response();

        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        //查询公司优秀案例
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", "1");
        queryMap.put("createid",id);
        List<Map<String,String>> caseList = successCaseService.findAllSuccessCaseList(pager,queryMap);
        pager.result(caseList);

        response.setData(pager);
        return response;
    }

    /**
     *名企展示
     * @return
     */
    @Deprecated
    @ApiOperation(value="优秀工程商",notes="优秀工程商",response = Response.class)
    @RequestMapping(value = {"sel_great_company"}, method = RequestMethod.GET)
    public Response greatCompany() {
        Response response = new Response();
        List list = memberService.greatCompany("2");    //2:工程商
        response.setData(list);
        return response;
    }

    /**
     *最新认证工程商(个数后台控制)
     * @return
     */
    @ApiOperation(value="最新认证工程商",notes="最新认证工程商",response = Response.class)
    @RequestMapping(value = {"sel_new_identify_engineer"}, method = RequestMethod.GET)
    public Response newIdentifyEngineer()  {
        Response response = new Response();
        List list = memberService.findnewIdentifyEngineer("2");   //2:工程商
        response.setData(list);
        return response;
    }

    @ApiOperation(value = "工程商信息广告位",notes = "工程商信息广告位")
    @RequestMapping(value = "sel_adv_engineer", method = RequestMethod.GET)
    public Response sel_adv_engineer(@ApiParam(value="频道类型 2:工程商")@RequestParam String chanType,
                                     @ApiParam(value="频道下子页面.index:首页;") @RequestParam String page,
                                     @ApiParam(value="广告所在区域:F2:工程商信息") @RequestParam String advArea)  {
        Response response = new Response();
        Map<String, Object> map = new HashMap<>();
        map.put("chanType",chanType);
        map.put("page",page);
        map.put("advArea",advArea);
        List<Map<String,String>> companyList = memberService.queryEngineerList(map);

        response.setData(companyList);
        return response;
    }
    /**
     * 查询资讯内容列表
     *
     * @param channelNews
     * @param pageNo
     * @param pageSize
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @ApiOperation(value = "查询行业咨询", notes = "查询行业咨询")
    @RequestMapping(value = "sel_channel_news", method = RequestMethod.GET)
    public Response queryChannelNewsList(
    		@ApiParam(value = "类型：1 行业资讯，2 人物专访 3：行业风云人物 4：工程商风采 5：工程资料")@RequestParam String type,
            @RequestParam(required = false,defaultValue = "1") String pageNo,
            @RequestParam(required = false,defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map> pager = new Paging<Map>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("channelid", "1");
        map.put("sort", type);
        map.put("status", "1");
        List<Map> channelList = newsService.findAllChanNewsList(pager, map);
        pager.result(channelList);
        response.setData(pager);
        return response;
    }
    
    /**
     * 
     * @param id 栏目信息的ID
     * @throws IOException
     */ 
    @ApiOperation(value = "查询行业咨询详情", notes = "查询行业咨询详情")
    @RequestMapping(value = "sel_news_details", method = RequestMethod.GET)
    public Response queryDetailsById(@ApiParam(value="咨询id")@RequestParam Long id) throws IOException {
    	Response response = new Response(); 
    	List<Map> chanMap= newsService.queryDetailsById(id);
        response.setData(chanMap);
        return response;
    }
    
    @ApiOperation(value = "vip工程商信息活动",notes = "vip工程商信息活动")
    @RequestMapping(value = "sel_contractor_activity", method = RequestMethod.GET)
    public Response sel_contractor_activity(@ApiParam(value = "公司id")@RequestParam String id,
                                            @RequestParam(required = false,defaultValue = "1") String pageNo,
                                            @RequestParam(required = false,defaultValue = "10") String pageSize)  {
        Response response = new Response();
        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        map.put("createid",id);
        List<Map<String,String>> resultList = expoService.findAllExpoListByCompanyId(pager,map);
        pager.result(resultList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "vip工程商信息威客",notes = "vip工程商信息威客")
    @RequestMapping(value = "sel_contractor_witkey", method = RequestMethod.GET)
    public Response sel_contractor_witkey(@ApiParam(value = "公司id")@RequestParam String id,
                                            @RequestParam(required = false,defaultValue = "1") String pageNo,
                                            @RequestParam(required = false,defaultValue = "10") String pageSize)  {
        Response response = new Response();
        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        map.put("createid",id);
        List<Map<String,String>> resultList = cooperationService.findAllWitkeyByCompanyId(pager,map);
        pager.result(resultList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "vip工程商信息技术",notes = "vip工程商信息技术")
    @RequestMapping(value = "sel_contractor_tech", method = RequestMethod.GET)
    public Response sel_contractor_tech(@ApiParam(value = "公司id")@RequestParam String id,
                                          @RequestParam(required = false,defaultValue = "1") String pageNo,
                                          @RequestParam(required = false,defaultValue = "10") String pageSize)  {
        Response response = new Response();
        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        map.put("createid",id);
        List<Map<String,String>> resultList = techCooperationService.findAllTechByCompanyId(pager,map);
        pager.result(resultList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "vip工程商信息人才",notes = "vip工程商信息人才")
    @RequestMapping(value = "sel_contractor_job", method = RequestMethod.GET)
    public Response sel_contractor_job(@ApiParam(value = "公司id")@RequestParam String id,
                                        @RequestParam(required = false,defaultValue = "1") String pageNo,
                                        @RequestParam(required = false,defaultValue = "10") String pageSize)  {
        Response response = new Response();
        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        map.put("createid",id);
        List<Map<String,String>> resultList = jobPositionService.findAllJobByCompanyId(pager,map);
        pager.result(resultList);
        response.setData(pager);
        return response;
    }
}

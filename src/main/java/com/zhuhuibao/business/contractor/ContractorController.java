package com.zhuhuibao.business.contractor;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.SuccessCaseService;
import com.zhuhuibao.utils.pagination.model.Paging;
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

}

package com.zhuhuibao.business.contractor;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.SuccessCaseService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/4/11 0011.
 */
@RestController
//@RequestMapping("/rest/contractor/site/base")
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
     * @throws IOException
     */
    @ApiOperation(value = "")
    @RequestMapping(value = {"/rest/engineerSupplier/newEngineer","/rest/contractor/site/sel_new_engineer"}, method = RequestMethod.GET)
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
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/engineerSupplier/introduce","/rest/contractor/site/sel_simple_introduce"}, method = RequestMethod.GET)
    public Response introduce(String id, String type) {
        Response response = new Response();
        Map map = memberService.introduce(id,type);
        response.setData(map);
        return response;
    }

    @ApiOperation(value = "公司成功案例（分页）", notes = "公司成功案例（分页）")
    @RequestMapping(value = "/rest/contractor/site/sel_company_success_caseList", method = RequestMethod.GET)
    public Response sel_company_success_caseList(@ApiParam(value = "公司id")@RequestParam String id,
                                                 @RequestParam(required = false) String pageNo,
                                                 @RequestParam(required = false) String pageSize)  {
        Response response = new Response();

        //设定默认分页pageSize
        if (com.zhuhuibao.utils.pagination.util.StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (com.zhuhuibao.utils.pagination.util.StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

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
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/engineerSupplier/greatCompany","/rest/contractor/site/sel_great_company"}, method = RequestMethod.GET)
    public Response greatCompany(String type) {
        Response response = new Response();
        List list = memberService.greatCompany(type);
        response.setData(list);
        return response;
    }

    /**
     *最新认证工程商(个数后台控制)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/engineerSupplier/newIdentifyEngineer","/rest/contractor/site/sel_new_identify_engineer"}, method = RequestMethod.GET)
    public Response newIdentifyEngineer(String type)  {
        Response response = new Response();
        List list = memberService.findnewIdentifyEngineer(type);
        response.setData(list);
        return response;
    }

    /**
     *留言
     * @return
     * @throws IOException
     */
    @ApiOperation(value="留言",notes="留言",response = Response.class)
    @RequestMapping(value = {"/rest/engineerSupplier/message","/rest/contractor/site/add_message"}, method = RequestMethod.POST)
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

}

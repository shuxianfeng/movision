package com.zhuhuibao.business.supplier;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/4/11 0011.
 */
@RestController
public class SupplierController {
    private static final Logger log = LoggerFactory.getLogger(SupplierController.class);

    @Autowired
    private MemberService memberService;

    /**
     *最新厂商(个数后台控制)
     * @return
     * @throws IOException
     */
    @Deprecated
    @RequestMapping(value = {"/rest/engineerSupplier/newManufacturer","/rest/supplier/site/sel_new_manufacturer"}, method = RequestMethod.GET)
    public Response newManufacturer()  {
        String type = "1";
        Response response = new Response();
        List list = memberService.findNewEngineerOrSupplier(type);
        response.setData(list);
        return response;
    }

    /**
     *最新代理商(个数后台控制)
     * @return
     * @throws IOException
     */
    @Deprecated
    @RequestMapping(value = {"/rest/engineerSupplier/newAgent","/rest/supplier/site/sel_new_agent"}, method = RequestMethod.GET)
    public Response newAgent()  {
        String type = "2";
        Response response = new Response();
        List list = memberService.findNewEngineerOrSupplier(type);
        response.setData(list);
        return response;
    }

    /**
     *最新渠道商(个数后台控制)
     * @return
     * @throws IOException
     */
    @Deprecated
    @RequestMapping(value = {"/rest/engineerSupplier/newChannel","/rest/supplier/site/sel_new_channel"}, method = RequestMethod.GET)
    public Response newChannel()  {
        String type = "3";
        Response response = new Response();
        List list = memberService.findNewEngineerOrSupplier(type);
        response.setData(list);
        return response;
    }

    /**
     *工程商，供应商简版介绍
     * @return
     * @throws IOException
     */
    @Deprecated
    @RequestMapping(value = "/rest/supplier/site/sel_simple_introduce", method = RequestMethod.GET)
    public Response introduce(String id, String type) {
        Response response = new Response();
        Map map = memberService.introduce(id,type);
        response.setData(map);
        return response;
    }

    /**
     *名企展示
     * @return
     * @throws IOException
     */
    @Deprecated
    @RequestMapping(value = "/rest/supplier/site/sel_great_company", method = RequestMethod.GET)
    public Response greatCompany(String type) {
        Response response = new Response();
        List list = memberService.greatCompany(type);
        response.setData(list);
        return response;
    }

    /**
     *最新认证工程商,供应商(个数后台控制)
     * @return
     * @throws IOException
     */
    @Deprecated
    @RequestMapping(value = "/rest/supplier/site/sel_new_identify_engineer", method = RequestMethod.GET)
    public Response newIdentifyEngineer(String type)  {
        Response response = new Response();
        List list = memberService.findnewIdentifyEngineer(type);
        response.setData(list);
        return response;
    }

}

package com.zhuhuibao.business.mall;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 商城商户主页
 * Created by cxx on 2016/6/22 0022.
 */
@RestController
@RequestMapping("/rest/supplier/site")
public class CompanyInfoController {
    private static final Logger log = LoggerFactory.getLogger(CompanyInfoController.class);

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "商户主页相关信息", notes = "商户主页相关信息")
    @RequestMapping(value = "sel_index_companyInfo", method = RequestMethod.GET)
    public Response companyInfo()  {
        Response response = new Response();

        return response;
    }
}

package com.zhuhuibao.business.mall;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.SuccessCaseService;
import com.zhuhuibao.mybatis.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ProductService productService;

    @Autowired
    private SuccessCaseService successCaseService;

    @ApiOperation(value = "商户主页相关信息", notes = "商户主页相关信息")
    @RequestMapping(value = "sel_index_companyInfo", method = RequestMethod.GET)
    public Response companyInfo(@ApiParam(value = "商户id")@RequestParam String id)  {
        Response response = new Response();

        //查询公司信息
        Member member = memberService.findMemById(id);

        //查询公司产品类别
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", Constants.product_status_publish);
        queryMap.put("createid",id);
        List<Map<String,String>> productTypeList = productService.queryProductTypeListByCompanyId(queryMap);

        //页面展示
        Map map = new HashMap();
        map.put("companyName",member.getEnterpriseName());
        map.put("webSite",member.getEnterpriseWebSite());
        map.put("address",member.getProvinceName()+member.getCityName()+member.getAreaName()+member.getAddress());
        map.put("telephone",member.getEnterpriseTelephone());
        map.put("fax",member.getEnterpriseFox());
        map.put("introduce",member.getEnterpriseDesc());
        map.put("productTypeList",productTypeList);

        response.setData(map);
        return response;
    }

    @ApiOperation(value = "热销商品", notes = "热销商品")
    @RequestMapping(value = "sel_company_hot_product", method = RequestMethod.GET)
    public Response sel_company_hot_product(@ApiParam(value = "商户id")@RequestParam String id,
                                            @ApiParam(value = "条数")@RequestParam String count)  {
        Response response = new Response();

        //查询公司热销产品
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", Constants.product_status_publish);
        queryMap.put("createid",id);
        queryMap.put("count",count);
        List<Map<String,String>> productList = productService.queryHotProductListByCompanyId(queryMap);

        response.setData(productList);
        return response;
    }

    @ApiOperation(value = "最新供应商品", notes = "最新供应商品")
    @RequestMapping(value = "sel_company_latest_product", method = RequestMethod.GET)
    public Response sel_company_latest_product(@ApiParam(value = "商户id")@RequestParam String id,
                                            @ApiParam(value = "条数")@RequestParam String count)  {
        Response response = new Response();

        //查询公司最新供应商品
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", Constants.product_status_publish);
        queryMap.put("createid",id);
        queryMap.put("count",count);
        List<Map<String,String>> productList = productService.queryLatestProductListByCompanyId(queryMap);

        response.setData(productList);
        return response;
    }

    @ApiOperation(value = "优秀案例", notes = "优秀案例")
    @RequestMapping(value = "sel_company_great_case", method = RequestMethod.GET)
    public Response sel_company_great_case(@ApiParam(value = "商户id")@RequestParam String id,
                                               @ApiParam(value = "条数")@RequestParam String count)  {
        Response response = new Response();

        //查询公司最新供应商品
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", "1");
        queryMap.put("is_deleted", "0");
        queryMap.put("createid",id);
        queryMap.put("count",count);
        List<Map<String,String>> caseList = successCaseService.queryGreatCaseListByCompanyId(queryMap);

        response.setData(caseList);
        return response;
    }
}

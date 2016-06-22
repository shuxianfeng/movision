package com.zhuhuibao.business.mall;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.SuccessCase;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.SuccessCaseService;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.utils.pagination.model.Paging;
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
        if(member.getProvince()!=null){
            map.put("address",member.getProvinceName()+member.getCityName()+member.getAreaName()+member.getAddress());
        }else {
            map.put("address","");
        }

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
                                            @ApiParam(value = "条数")@RequestParam int count)  {
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
                                            @ApiParam(value = "条数")@RequestParam int count)  {
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
                                               @ApiParam(value = "条数")@RequestParam int count)  {
        Response response = new Response();

        //查询公司优秀案例
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", "1");
        queryMap.put("is_deleted", "0");
        queryMap.put("createid",id);
        queryMap.put("count",count);
        List<Map<String,String>> caseList = successCaseService.queryGreatCaseListByCompanyId(queryMap);

        response.setData(caseList);
        return response;
    }

    @ApiOperation(value = "公司介绍", notes = "公司介绍")
    @RequestMapping(value = "sel_company_introduce", method = RequestMethod.GET)
    public Response sel_company_introduce(@ApiParam(value = "商户id")@RequestParam String id)  {
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
        if("1".equals(member.getCurrency())){
            map.put("registerCapital",member.getRegisterCapital()+"万元");
        }else{
            map.put("registerCapital",member.getRegisterCapital()+"万美元");
        }
        if(member.getEnterpriseProvince()!=null){
            map.put("address",member.getEnterpriseProvinceName()+member.getEnterpriseCityName()+member.getEnterpriseAreaName());
        }else {
            map.put("address","");
        }

        map.put("companyType",member.getEnterpriseTypeName());
        map.put("createTime",member.getEnterpriseCreaterTime());
        map.put("introduce",member.getEnterpriseDesc());
        map.put("saleRange",member.getSaleProductDesc());
        map.put("productTypeList",productTypeList);

        response.setData(map);
        return response;
    }

    @ApiOperation(value = "公司联系方式", notes = "公司联系方式")
    @RequestMapping(value = "sel_company_contact", method = RequestMethod.GET)
    public Response sel_company_contact(@ApiParam(value = "商户id")@RequestParam String id)  {
        Response response = new Response();

        //查询公司信息
        Member member = memberService.findMemById(id);

        //页面展示
        Map map = new HashMap();
        map.put("companyName",member.getEnterpriseName());
        map.put("webSite",member.getEnterpriseWebSite());
        if(member.getProvince()!=null){
            map.put("address",member.getProvinceName()+member.getCityName()+member.getAreaName()+member.getAddress());
        }else {
            map.put("address","");
        }
        map.put("telephone",member.getEnterpriseTelephone());
        map.put("fax",member.getEnterpriseFox());

        response.setData(map);
        return response;
    }

    @ApiOperation(value = "公司荣誉资质", notes = "公司荣誉资质")
    @RequestMapping(value = "sel_company_certificate", method = RequestMethod.GET)
    public Response sel_company_certificate(@ApiParam(value = "商户id")@RequestParam String id)  {
        Response response = new Response();

        CertificateRecord certificateRecord = new CertificateRecord();
        certificateRecord.setMem_id(id);
        //供应商资质
        certificateRecord.setType("1");
        certificateRecord.setIs_deleted(0);
        //审核通过
        certificateRecord.setStatus("1");
        List<CertificateRecord> certificateRecordList = memberService.certificateSearch(certificateRecord);

        response.setData(certificateRecordList);
        return response;
    }

    @ApiOperation(value = "公司成功案例（分页）", notes = "公司成功案例（分页）")
    @RequestMapping(value = "sel_company_success_caseList", method = RequestMethod.GET)
    public Response sel_company_success_caseList(@ApiParam(value = "商户id")@RequestParam String id,
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

    @ApiOperation(value = "成功案例详情", notes = "成功案例详情")
    @RequestMapping(value = "sel_company_success_case", method = RequestMethod.GET)
    public Response sel_company_success_case(@ApiParam(value = "案例id")@RequestParam String id)  {
        Response response = new Response();
        SuccessCase successCase = successCaseService.querySuccessCaseById(id);
        response.setData(successCase);
        return response;
    }

    @ApiOperation(value = "公司产品（分页）", notes = "公司产品（分页）")
    @RequestMapping(value = "sel_company_product_list", method = RequestMethod.GET)
    public Response sel_company_product_list(@ApiParam(value = "产品类别id")@RequestParam(required = false) String fcateid,
                                             @ApiParam(value = "商户id")@RequestParam String id,
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

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", "1");
        queryMap.put("createid",id);
        queryMap.put("fcateid",fcateid);
        List<Map<String,String>> productList = productService.findAllProductListByProductType(pager,queryMap);
        pager.result(productList);

        response.setData(pager);
        return response;
    }
}

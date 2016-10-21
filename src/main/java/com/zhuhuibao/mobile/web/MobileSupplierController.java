package com.zhuhuibao.mobile.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.AdvertisingConstant;
import com.zhuhuibao.common.pojo.AskPriceResultBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.fsearch.pojo.spec.SupplierSearchSpec;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.service.*;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 供应商相关业务控制层
 *
 * @author liyang
 * @date 2016年10月13日
 */
@RestController
@RequestMapping("/rest/m/supplier/site/")
@Api(value = "MobileSupplier", description = "触屏端供应链频道")
public class MobileSupplierController {

    private static final Logger log = LoggerFactory.getLogger(MobileSupplierController.class);

    @Autowired
    private MobileSysAdvertisingService advertisingService;

    @Autowired
    private MobileEnquiryService enquiryService;

    @Autowired
    private MobileBrandService mobileBrandService;

    @Autowired
    private MobileProductService mobileProductService;

    @Autowired
    private MobileMemberService memberService;

    @Autowired
    private MobileCategoryService categoryService;

    @Autowired
    private MobileSuccessCaseService successCaseService;

    /**
     * 触屏端供应链广告图片位置
     */
    private final String mSupplierArea = "M_Supplier";

    /**
     * 触屏端供应链首页
     * 
     * @return response 响应
     */
    @ApiOperation(value = "触屏端供应链首页", notes = "触屏端供应链首页")
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public Response index(@ApiParam(value = "当前登陆人id") @RequestParam(required = false) String createId) {
        Response response = new Response();
        // banner位广告图片
        List<SysAdvertising> bannerAdvList = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingChanType.mobile.value, mSupplierArea, "M_Supplier_Banner");
        // 热门品牌广告图片
        List<SysAdvertising> hotBrandList = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingChanType.mobile.value, mSupplierArea, "M_Supplier_Hot_Brand");
        // 热门厂商广告图片
        List<SysAdvertising> hotSupplierList = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingChanType.mobile.value, mSupplierArea, "M_Supplier_Hot_Supplier");
        // 取最新6条公开询价数据
        List askPriceList = enquiryService.queryNewestAskPrice(6, createId);
        Map<String, List> dataList = new HashMap<>();
        dataList.put("banner", bannerAdvList);
        dataList.put("hotBrand", hotBrandList);
        dataList.put("hotSupplier", hotSupplierList);
        dataList.put("askPriceList", askPriceList);
        response.setData(dataList);
        return response;
    }

    /**
     * 触屏端供应链-品牌馆
     *
     * @return response 响应
     */
    @ApiOperation(value = "触屏端供应链-对应类别品牌展示列表页面", notes = "触屏端供应链-对应类别品牌展示列表页面")
    @RequestMapping(value = "sel_hot_brand_list", method = RequestMethod.GET)
    public Response sel_hot_brand_list() {
        Response response = new Response();
        try {
            response.setData(mobileBrandService.selHotBrandList());
        } catch (Exception e) {
            log.error("sel_hot_brand_list error! ", e);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 触屏端供应链-对应类别品牌展示列表页面
     * 
     * @param pageNo
     *            页码
     * @param pageSize
     *            每页显示条数
     * @return response 响应
     */
    @ApiOperation(value = "触屏端供应链-对应类别品牌展示列表页面", notes = "触屏端供应链-对应类别品牌展示列表页面")
    @RequestMapping(value = "sel_hot_brand_list_by_type", method = RequestMethod.GET)
    public Response sel_hot_brand_list_by_type(@ApiParam(value = "一级分类id") @RequestParam(required = true) String parentId, @ApiParam(value = "一级分类id") @RequestParam(required = true) String subTypeId,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") int pageNo, @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Response response = new Response();
        Paging<Map> pager = new Paging<>(pageNo, pageSize);
        try {
            response.setData(mobileBrandService.selHotBrandListByType(parentId, subTypeId, pager));
        } catch (Exception e) {
            log.error("sel_hot_brand_list_by_type error! ", e);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 触屏端供应链-品牌信息详情页面
     * 
     * @param id
     *            品牌id
     * @return response 响应
     */
    @ApiOperation(value = "触屏端供应链-品牌信息详情页面", notes = "触屏端供应链-品牌信息详情页面")
    @RequestMapping(value = "sel_hot_brand_info", method = RequestMethod.GET)
    public Response sel_brand_info(@ApiParam(value = "品牌主键id") @RequestParam(required = true) String id, @ApiParam(value = "品牌所属类别id") @RequestParam(required = true) String scateId) {
        Response response = new Response();
        try {
            // 品牌信息
            Map brandInfo = mobileBrandService.selBrandInfo(id, scateId);
            // 该品牌类别下产品信息
            List productList = mobileProductService.findProductByBrandAndSubSystem(id, scateId);
            // 该品牌下产品类别信息
            List typeList = mobileProductService.findSubSystemByBrand(id);
            Map modelMap = new HashMap();
            modelMap.put("brandInfo", brandInfo);
            modelMap.put("typeList", typeList);
            modelMap.put("productList", productList);
            response.setData(modelMap);
        } catch (Exception e) {
            log.error("sel_brand_info error! ", e);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 触屏端搜索供应商信息
     *
     * @param spec
     *            查询条件
     * @return
     */
    @ApiOperation(value = "触屏端搜索供应商", notes = "触屏端搜索供应商", response = Response.class)
    @RequestMapping(value = { "sel_supplier_list" }, method = RequestMethod.GET)
    public Response sel_supplier_list(SupplierSearchSpec spec) {
        Response response = new Response();
        try {
            response.setData(memberService.searchSuppliers(spec));
        } catch (Exception e) {
            response.setMsgCode(0);
            response.setMessage("sel_supplier_list  error!" + e);
        }
        return response;
    }

    /**
     * 触屏端--询价馆
     * 
     * @param askPriceSearch
     *            查询条件
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "触屏端--询价馆", notes = "触屏端--询价馆", response = Response.class)
    @RequestMapping(value = { "sel_enquiry_list" }, method = RequestMethod.GET)
    public Response sel_enquiry_list(AskPriceSearchBean askPriceSearch, @RequestParam(required = false, defaultValue = "1") String pageNo,
            @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<AskPriceResultBean> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        try {
            response.setData(enquiryService.selEnquiryList(askPriceSearch, pager));
        } catch (Exception e) {
            response.setMessage("sel_enquiry_list  error!" + e);
        }
        return response;
    }

    /**
     * 触屏端--查看具体询价详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "触屏端--查看具体询价详情", notes = "触屏端--查看具体询价详情", response = Response.class)
    @RequestMapping(value = { "sel_enquiry_info" }, method = RequestMethod.GET)
    public Response sel_enquiry_info(@ApiParam(value = "询价主键id") @RequestParam(required = false) String id) {
        Response response = new Response();
        try {
            response.setData(enquiryService.queryAskPriceByID(id));
        } catch (Exception e) {
            response.setMessage("sel_enquiry_info  error!" + e);
        }
        return response;
    }

    /**
     * 触屏端--系统分类
     *
     * @return
     */
    @ApiOperation(value = "触屏端--系统分类", notes = "触屏端--系统分类", response = Response.class)
    @RequestMapping(value = { "sel_category" }, method = RequestMethod.GET)
    public Response sel_category() {
        Response response = new Response();
        try {
            response.setData(categoryService.selCategory());
        } catch (Exception e) {
            response.setMessage("sel_enquiry_info  error!" + e);
        }
        return response;
    }

    /**
     * 供应商成功案例
     * 
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "公司成功案例（分页）", notes = "公司成功案例（分页）")
    @RequestMapping(value = "sel_company_success_caseList", method = RequestMethod.GET)
    public Response sel_company_success_caseList(@ApiParam(value = "公司id") @RequestParam String id, @RequestParam(required = false, defaultValue = "1") String pageNo,
            @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String, String>> caseList = successCaseService.findAllSuccessCaseList(pager, id);
        pager.result(caseList);
        response.setData(pager);
        return response;
    }
}

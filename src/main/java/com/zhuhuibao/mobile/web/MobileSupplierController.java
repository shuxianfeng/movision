package com.zhuhuibao.mobile.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.service.*;
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
import com.zhuhuibao.fsearch.pojo.spec.ContractorSearchSpec;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
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
    private AdvertisingService advertisingService;

    @Autowired
    private AskPriceService askPriceService;

    @Autowired
    private MobileBrandService mobileBrandService;

    @Autowired
    private MobileProductService mobileProductService;

    @Autowired
    private MobileAgentService mobileAgentService;

    @Autowired
    private MobileMemberService memberService;

    @Autowired
    private MobileChannelNewsService channelNewsService;

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
        List askPriceList = askPriceService.queryNewestAskPrice(6, createId);
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


}

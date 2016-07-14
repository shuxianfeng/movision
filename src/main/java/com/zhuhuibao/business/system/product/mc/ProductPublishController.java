package com.zhuhuibao.business.system.product.mc;

import java.io.IOException;
import java.util.*;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.oms.service.ComplainSuggestService;
import com.zhuhuibao.mybatis.product.service.ProductService;

/**
 * 产品控制层
 *
 * @author penglong
 */
@RestController
@Api(value = "productSite", description = "产品(前台页面)")
public class ProductPublishController {

    private static final Logger log = LoggerFactory.getLogger(ProductPublishController.class);

    @Autowired
    private ProductService productService;

    @RequestMapping(value = {"/rest/productDetail/queryProductInfoById", "/rest/system/site/product/sel_productDetail"}, method = RequestMethod.GET)
    @ApiOperation(value = "预览产品详情", notes = "预览产品详情", response = Response.class)
    public Response queryProductInfoById(Long id) throws IOException {
        Response response = productService.queryProductInfoById(id);
        productService.updateHit(id);
        return response;
    }

    @RequestMapping(value = {"/rest/productDetail/queryPrdDescParamService", "/rest/system/site/product/sel_prdDescParam"}, method = RequestMethod.GET)
    @ApiOperation(value = "产品详情页面预览产品参数", notes = "产品详情页面预览产品参数", response = Response.class)
    public Response queryPrdDescParamService(Long id) throws IOException {
        Map<String, Object> map = productService.queryPrdDescParamService(id);
        return new Response(map);
    }

    /**
     * 品牌详情页面查询二级系统
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/productDetail/querySCateListByBrandId", "/rest/system/site/product/sel_sCateListByBrandId"}, method = RequestMethod.GET)
    @ApiOperation(value = "品牌详情页面查询二级系统", notes = "品牌详情页面查询二级系统", response = Response.class)
    public Response querySCateListByBrandId(Product product) throws IOException {
        Response response = new Response();
        response = productService.querySCateListByBrandId(product);

        return response;
    }

    /**
     * 品牌详情页面根据二级系统查询产品
     *
     * @param product
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    @RequestMapping(value = {"/rest/productDetail/queryProductInfoBySCategory", "/rest/system/site/product/sel_productInfoBySCategory"}, method = RequestMethod.GET)
    @ApiOperation(value = "品牌详情页面根据二级系统查询产品", notes = "品牌详情页面根据二级系统查询产品", response = Response.class)
    public Response queryProductInfoBySCategory(ProductWithBLOBs product) throws IOException {
        Response response = new Response();
        Map<String, Object> productMap = new HashMap<String, Object>();
        productMap.put("scateid", product.getScateid());
        productMap.put("brandid", product.getBrandid());
        productMap.put("status", Constants.product_status_publish);
        productMap.put("count", Constants.brand_page_product_count);
        productMap.put("order", "publishTime");
        response = productService.queryProductInfoBySCategory(productMap);

        return response;
    }

    /**
     * 产品推荐信息，根据点击率排序
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    @RequestMapping(value = {"/rest/productDetail/queryRecommendProduct", "/rest/system/site/product/sel_recommendProduct"}, method = RequestMethod.GET)
    @ApiOperation(value = "产品推荐信息，根据点击率排序", notes = "产品推荐信息，根据点击率排序", response = Response.class)
    public Response queryRecommendProduct() throws IOException {
        Response response = new Response();
        Map<String, Object> productMap = new HashMap<String, Object>();
        productMap.put("status", Constants.product_status_publish);
        productMap.put("count", Constants.recommend_product_count);
        productMap.put("order", "hit");
        response = productService.queryRecommendHotProduct(productMap);

        return response;
    }

    /**
     * 查询热点产品
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    @RequestMapping(value = {"/rest/productDetail/queryHotProduct", "/rest/system/site/product/sel_hotProduct"}, method = RequestMethod.GET)
    @ApiOperation(value = "查询热点产品", notes = "查询热点产品", response = Response.class)
    public Response queryHotProduct() throws IOException {
        Response response = new Response();
        Map<String, Object> productMap = new HashMap<String, Object>();
        productMap.put("status", Constants.product_status_publish);
        productMap.put("count", Constants.hot_product_count);
        productMap.put("order", "hit");
        response = productService.queryRecommendHotProduct(productMap);

        return response;
    }
}

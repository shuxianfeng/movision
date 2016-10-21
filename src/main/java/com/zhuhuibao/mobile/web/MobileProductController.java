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
import com.zhuhuibao.fsearch.pojo.spec.ProductSearchSpec;
import com.zhuhuibao.fsearch.service.exception.ServiceException;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.service.MobileAgentService;
import com.zhuhuibao.service.MobileProductService;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 产品相关业务控制层
 *
 * @author liyang
 * @date 2016年10月14日
 */
@RestController
@RequestMapping("/rest/m/product/site/")
@Api(value = "mobileProduct", description = "产品")
public class MobileProductController {

    private static final Logger log = LoggerFactory.getLogger(MobileProductController.class);

    @Autowired
    private MobileProductService mobileProductService;

    @Autowired
    private MobileAgentService mobileAgentService;

    /**
     * 触屏端供应链-品牌下更多产品
     *
     * @param id
     *            品牌id
     * @param scateId
     *            品牌所属类别id
     * @param pageNo
     *            页码
     * @param pageSize
     *            每页条数
     * @return
     */
    @ApiOperation(value = "触屏端供应链-品牌下更多产品", notes = "触屏端供应链-品牌下更多产品")
    @RequestMapping(value = "sel_hot_brand_product_list", method = RequestMethod.GET)
    public Response sel_hot_brand_product_list(@ApiParam(value = "品牌主键id") @RequestParam(required = true) String id, @ApiParam(value = "品牌所属类别id") @RequestParam(required = true) String scateId,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") int pageNo, @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Response response = new Response();
        Paging<Map> pager = new Paging<>(pageNo, pageSize);
        response.setData(mobileProductService.findProductByBrandAndSubSystemPages(id, scateId, pager));
        return response;
    }

    /**
     * 触屏端-产品详情页面
     *
     * @param id
     *            商品主键id
     * @return
     */
    @ApiOperation(value = "触屏端-产品详情页面", notes = "触屏端-产品详情页面")
    @RequestMapping(value = "sel_product_info", method = RequestMethod.GET)
    public Response sel_product_info(@ApiParam(value = "商品主键id") @RequestParam(required = false) Long id) {
        Response response = new Response();
        Map modelMap = new HashMap();
        // 产品基本信息
        ProductWithBLOBs product = mobileProductService.queryProductById(id);
        // 产品参数信息
        Map prdDescParam = mobileProductService.queryPrdDescParam(id);
        // 产品代理商
        Map agentMap = mobileAgentService.getAgentByProId(String.valueOf(id));
        modelMap.put("productInfo", product);
        modelMap.put("prdDescParam", prdDescParam);
        modelMap.put("agentMap", agentMap);
        response.setData(modelMap);
        return response;
    }

    /**
     * 触屏端供应链-检索产品
     *
     * @param spec
     *            检索条件
     * @return
     */
    @ApiOperation(value = "触屏端供应链-搜索产品", notes = "触屏端供应链-品牌下更多产品")
    @RequestMapping(value = "sel_product_list", method = RequestMethod.GET)
    public Response sel_product_list(ProductSearchSpec spec) {
        Response response = new Response();
        try {
            response.setData(mobileProductService.selProductList(spec));
        } catch (ServiceException e) {
            response.setCode(500);
            response.setMessage("sel_product_list  error!" + e);
        }
        return response;
    }

    /**
     * 检索供应商下面的商品
     * 
     * @param fcateid
     *            产品类别id
     * @param id
     *            商户id
     * @param pageNo
     *            页码
     * @param pageSize
     *            页数
     * @return
     */
    @ApiOperation(value = "检索供应商下面的商品", notes = "检索供应商下面的商品）")
    @RequestMapping(value = "sel_company_product_list", method = RequestMethod.GET)
    public Response sel_company_product_list(@ApiParam(value = "产品类别id") @RequestParam(required = false) String fcateid, @ApiParam(value = "商户id") @RequestParam String id,
            @RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String, String>> productList = mobileProductService.findAllProductListByProductType(fcateid, id, pager);
        pager.result(productList);

        return new Response(pager);
    }
}

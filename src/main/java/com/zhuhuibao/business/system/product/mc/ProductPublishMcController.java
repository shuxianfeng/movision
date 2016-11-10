package com.zhuhuibao.business.system.product.mc;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.aop.LoginAccess;
import com.zhuhuibao.aop.UserAccess;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.CheckBrand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.oms.entity.ComplainSuggest;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
import com.zhuhuibao.mybatis.oms.service.ComplainSuggestService;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 产品控制层
 *
 * @author penglong
 */
@RestController
@Api(value = "productPublishMc", description = "产品(个人中心)")
public class ProductPublishMcController {

    private static final Logger log = LoggerFactory.getLogger(ProductPublishMcController.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private ComplainSuggestService suggestService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    BrandService brandService;

    @LoginAccess
    @UserAccess(value = "ALL", viplevel = "30,60,130,160")
    @RequestMapping(value = { "/rest/addProduct", "/rest/system/mc/product/add_product" }, method = RequestMethod.POST)
    @ApiOperation(value = "新增产品", notes = "新增产品", response = Response.class)
    public Response addProduct(String json) throws IOException {
        Gson gson = new Gson();
        ProductWithBLOBs product = gson.fromJson(json, ProductWithBLOBs.class);
        Response response = new Response();
        productService.insertProduct(product);
        return response;
    }

    @Deprecated
    @RequestMapping(value = { "/rest/addComplainSuggest", "/rest/system/mc/product/add_complainSuggest" }, method = RequestMethod.POST)
    @ApiOperation(value = "找不到产品对应的类别新增投诉和建议", notes = "找不到产品对应的类别新增投诉和建议", response = Response.class)
    public Response addComplainSuggest(ComplainSuggest suggest) throws IOException {
        Response response = new Response();
        suggestService.insert(suggest);

        return response;
    }

    @UserAccess(value = "ALL", viplevel = "30,60,130,160")
    @LoginAccess
    @RequestMapping(value = { "/rest/updateProduct", "/rest/system/mc/product/upd_product" }, method = RequestMethod.POST)
    @ApiOperation(value = "更新产品", notes = "更新产品", response = Response.class)
    public Response updateProduct(ProductWithBLOBs product) throws IOException {
        Response response;
        Long createid = ShiroUtil.getCreateID();
        Product b = productService.findById(product.getId());
        if (b != null) {
            if (String.valueOf(createid).equals(String.valueOf(b.getCreateid()))) {

                product.setStatus(Constants.product_status_nocheck);

                response = productService.updateProduct(product);
            } else {
                throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
            }
        } else {
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
        }

        return response;
    }

    @LoginAccess
    @UserAccess(value = "ALL", viplevel = "30,60,130,160")
    @RequestMapping(value = { "/rest/updateProductStatus", "/rest/system/mc/product/upd_productStatus" }, method = RequestMethod.POST)
    @ApiOperation(value = "更新产品状态", notes = "更新产品状态", response = Response.class)
    public Response updateProductStatus(ProductWithBLOBs product) throws IOException {

        return productService.updateProductStatus(product);
    }

    @LoginAccess
    @UserAccess(value = "ALL", viplevel = "30,60,130,160")
    @RequestMapping(value = { "/rest/batchUnpublish", "/rest/system/mc/product/upd_batchPublish" }, method = RequestMethod.POST)
    @ApiOperation(value = "批量更新产品状态", notes = "批量更新产品状态", response = Response.class)
    public Response batchUnpublish(@RequestParam String[] ids) throws IOException {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, ids);

        return productService.batchUnpublish(list);
    }

    @LoginAccess
    @UserAccess(value = "ALL", viplevel = "30,60,130,160")
    @RequestMapping(value = { "/rest/queryProductById", "/rest/system/mc/product/sel_productById" }, method = RequestMethod.GET)
    @ApiOperation(value = "获得产品信息根据ID", notes = "获得产品信息根据ID", response = Response.class)
    public Response queryProductById(Long id) throws IOException {
        Long createid = ShiroUtil.getCreateID();
        Product b = productService.findById(id);
        if (b != null) {
            if (String.valueOf(createid).equals(String.valueOf(b.getCreateid()))) {
                return productService.selectByPrimaryKey(id);
            } else {
                throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
            }
        } else {
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
        }

    }

    @LoginAccess
    @UserAccess(value = "ALL", viplevel = "30,60,130,160")
    @RequestMapping(value = { "/rest/findAllProduct", "/rest/system/mc/product/sel_allProduct" }, method = RequestMethod.GET)
    @ApiOperation(value = "我的产品", notes = "我的产品", response = Response.class)
    public Response findAllProduct(ProductWithBLOBs product, @RequestParam(defaultValue = "1") String pageNo, @RequestParam(defaultValue = "10") String pageSize) throws IOException {

        Long createid = ShiroUtil.getCreateID();
        if (product.getCreateid() != null) {

            if (!Objects.equals(createid, product.getCreateid())) {
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询无权限");
            } else {
                Paging<Product> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
                List<Product> productList = productService.findAllByPager(pager, product);
                pager.result(productList);

                return new Response(pager);
            }

        } else {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "参数错误");
        }

    }

    @RequestMapping(value = { "/rest/getProductFirstCategory", "/rest/system/mc/product/sel_productFirstCategory" }, method = RequestMethod.GET)
    @ApiOperation(value = "系统一级分类", notes = "系统一级分类", response = Response.class)
    public Response getProductFirstCategory() {
        List<ResultBean> systemList = categoryMapper.findSystemList();
        return new Response(systemList);
    }

    /**
     * 查询大系统下所有子系统类目
     *
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = { "/rest/getProductSecondCategory", "/rest/system/mc/product/sel_productSecondCategory" }, method = RequestMethod.GET)
    @ApiOperation(value = "系统二级分类", notes = "系统二级分类", response = Response.class)
    public Response getProductSecondCategory(HttpServletRequest req) throws IOException {
        String parentId = req.getParameter("parentID");
        Response response = new Response();
        List<ResultBean> subSystemList = categoryMapper.findSubSystemList(parentId);
        response.setData(subSystemList);

        return response;
    }

    /**
     * 查询品牌
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = { "/rest/getBrandList", "/rest/system/mc/product/sel_brandList" }, method = RequestMethod.GET)
    @ApiOperation(value = "查询品牌", notes = "查询品牌", response = Response.class)
    public Response getBrandList(Brand brand) throws IOException {
        brand.setStatus("1"); // 已审核通过
        List<Brand> brandList = brandMapper.searchBrandByStatus(brand);
        Response result = new Response();
        result.setCode(200);
        result.setData(brandList);

        return result;
    }

    @RequestMapping(value = { "/rest/system/mc/product/sel_brand_kw" }, method = RequestMethod.GET)
    @ApiOperation(value = "查询品牌", notes = "查询品牌", response = Response.class)
    public Response getAllPassBrand(@ApiParam("品牌中英文名称关键字") @RequestParam String keyword, @ApiParam("显示数量(默认10)") @RequestParam(defaultValue = "10") String count) throws UnsupportedEncodingException {
        log.debug(">>>>keyword keyword>>>:{} && count>>>{}", keyword, count);
        // keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
        List<Map<String, String>> list = brandService.findByKeyword(keyword, count);

        return new Response(list);
    }
}
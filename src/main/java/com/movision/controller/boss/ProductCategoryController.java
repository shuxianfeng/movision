package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.ProductCategoryFacade;
import com.movision.mybatis.brand.entity.Brand;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscount;
import com.movision.mybatis.productcategory.entity.ProductCategory;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/3/1 16:54
 */
@RestController
@RequestMapping(value = "/boss/goods/category")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryFacade productCategoryFacade;


    /**
     * 商品管理*--商品分类列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "商品分类列表（分页）", notes = "商品分类列表（分页）", response = Response.class)
    @RequestMapping(value = "query_category_list", method = RequestMethod.POST)
    public Response queryCategoryList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @RequestParam(required = false, defaultValue = "10") String pageSize
    ) {
        Response response = new Response();
        Paging<ProductCategory> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ProductCategory> list = productCategoryFacade.findAllCategory(pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 商品管理*--商品活动列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "商品活动列表（分页）", notes = "商品活动列表（分页）", response = Response.class)
    @RequestMapping(value = "query_active_list", method = RequestMethod.POST)
    public Response findAllGoodsDiscount(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                         @RequestParam(required = false, defaultValue = "10") String pageSize
    ) {
        Response response = new Response();
        Paging<GoodsDiscount> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsDiscount> list = productCategoryFacade.findAllGoodsDiscount(pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }
    /**
     * 商品管理*--商品品牌列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "商品品牌列表（分页）", notes = "商品品牌列表（分页）", response = Response.class)
    @RequestMapping(value = "query_brand_list", method = RequestMethod.POST)
    public Response findAllBrand(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                 @RequestParam(required = false, defaultValue = "10") String pageSize
    ) {
        Response response = new Response();
        Paging<Brand> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Brand> list = productCategoryFacade.findAllBrand(pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }
    /**
     * 商品管理*--商品分类列表搜索
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "商品分类列表搜索（分页）", notes = "商品分类列表搜索（分页）", response = Response.class)
    @RequestMapping(value = "query_category_condition", method = RequestMethod.POST)
    public Response queryCategoryCondition(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                           @RequestParam(required = false, defaultValue = "10") String pageSize,
                                           @ApiParam(value = "商品名称") @RequestParam(required = false) String typename
    ) {
        Response response = new Response();
        Paging<ProductCategory> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ProductCategory> list = productCategoryFacade.findAllCategoryCondition(typename, pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 商品管理*--商品活动列表搜索
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "商品活动列表搜索（分页）", notes = "商品活动列表搜索（分页）", response = Response.class)
    @RequestMapping(value = "query_active_condition", method = RequestMethod.POST)
    public Response findAllCategoryCondition(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                             @RequestParam(required = false, defaultValue = "10") String pageSize,
                                             @ApiParam(value = "活动名称") @RequestParam(required = false) String name,
                                             @ApiParam(value = "是否启用") @RequestParam(required = false) String isdel
    ) {
        Response response = new Response();
        Paging<GoodsDiscount> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsDiscount> list = productCategoryFacade.findAllCategoryCondition(name, isdel, pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }
    /**
     * 商品管理*--商品品牌列表搜索
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "商品品牌列表搜索（分页）", notes = "商品品牌列表搜索（分页）", response = Response.class)
    @RequestMapping(value = "query_brand_condition", method = RequestMethod.POST)
    public Response queryCategoryCondition(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                           @RequestParam(required = false, defaultValue = "10") String pageSize,
                                           @ApiParam(value = "品牌名称") @RequestParam(required = false) String brandname,
                                           @ApiParam(value = "是否启用") @RequestParam(required = false) String isdel
    ) {
        Response response = new Response();
        Paging<Brand> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Brand> list = productCategoryFacade.findAllBrandCondition(brandname, isdel, pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }
    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除分类", notes = "删除分类", response = Response.class)
    @RequestMapping(value = "delete_category", method = RequestMethod.POST)
    public Response deleteCategory(@ApiParam(value = "分类id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        int result = productCategoryFacade.deleteCategory(id);
        if (response.getCode() == 200) {
            response.setMessage("删除成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询", notes = "根据id查询", response = Response.class)
    @RequestMapping(value = "query_categorybyid", method = RequestMethod.POST)
    public Response queryCategory(@ApiParam(value = "分类id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        ProductCategory productCategory = productCategoryFacade.queryCategory(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(productCategory);
        return response;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询", notes = "根据id查询", response = Response.class)
    @RequestMapping(value = "query_brandbyid", method = RequestMethod.POST)
    public Response queryBrand(@ApiParam(value = "品牌id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        Brand brand = productCategoryFacade.queryBrand(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(brand);
        return response;
    }
    /**
     * 增加类别
     *
     * @param
     * @param typename
     * @param imgurl
     * @return
     */
    @ApiOperation(value = "增加类别", notes = "增加类别", response = Response.class)
    @RequestMapping(value = "add_category", method = RequestMethod.POST)
    public Response addCategory(@ApiParam(value = "分类名称") @RequestParam String typename,
                                @ApiParam(value = "图片") @RequestParam String imgurl) {
        Response response = new Response();
        Map<String, Integer> map = productCategoryFacade.addCategory(typename, imgurl);
        if (response.getCode() == 200) {
            response.setMessage("增加成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 增加品牌
     *
     * @param brandname
     * @param
     * @param isdel
     * @return
     */
    @ApiOperation(value = "增加品牌", notes = "增加品牌", response = Response.class)
    @RequestMapping(value = "add_brand", method = RequestMethod.POST)
    public Response addBrand(
            @ApiParam(value = "品牌名称") @RequestParam String brandname,
            @ApiParam(value = "是否启用") @RequestParam(required = false) String isdel) {
        Response response = new Response();
        Map<String, Integer> map = productCategoryFacade.addBrand(brandname, isdel);
        if (response.getCode() == 200) {
            response.setMessage("增加成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 修改类别
     *
     * @param
     * @param typename
     * @param imgurl
     * @return
     */
    @ApiOperation(value = "修改类别", notes = "修改类别", response = Response.class)
    @RequestMapping(value = "update_category", method = RequestMethod.POST)
    public Response updateCategory(
            @ApiParam(value = "分类名称") @RequestParam(required = false) String typename,
            @ApiParam(value = "图片") @RequestParam(required = false) String imgurl,
            @ApiParam(value = "分类id") @RequestParam(required = false) String id) {
        Response response = new Response();
        Map<String, Integer> map = productCategoryFacade.updateCategory(id, typename, imgurl);
        if (response.getCode() == 200) {
            response.setMessage("修改成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 修改品牌
     *
     * @param
     * @param brandname
     * @param isdel
     * @return
     */
    @ApiOperation(value = "修改品牌", notes = "修改品牌", response = Response.class)
    @RequestMapping(value = "update_brand", method = RequestMethod.POST)
    public Response updateBrand(
            @ApiParam(value = "品牌名称") @RequestParam(required = false) String brandname,
            @ApiParam(value = "是否启用") @RequestParam(required = false) String isdel,
            @ApiParam(value = "品牌id") @RequestParam(required = false) String id) {
        Response response = new Response();
        Map<String, Integer> map = productCategoryFacade.updateBrand(brandname, isdel, id);
        if (response.getCode() == 200) {
            response.setMessage("修改成功");
        }
        response.setData(map);
        return response;
    }
    /**
     * 停用
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "停用", notes = "停用", response = Response.class)
    @RequestMapping(value = "update_down", method = RequestMethod.POST)
    public Response updateDown(@ApiParam(value = "品牌id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        int result = productCategoryFacade.updateDown(id);
        if (response.getCode() == 200) {
            response.setMessage("停用成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 启用
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "启用", notes = "启用", response = Response.class)
    @RequestMapping(value = "update_up", method = RequestMethod.POST)
    public Response updateUp(@ApiParam(value = "品牌id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        int result = productCategoryFacade.updateUp(id);
        if (response.getCode() == 200) {
            response.setMessage("启用成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 活动停用
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "活动停用", notes = "活动停用", response = Response.class)
    @RequestMapping(value = "update_downD", method = RequestMethod.POST)
    public Response updateDownD(@ApiParam(value = "活动id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        int result = productCategoryFacade.updateDownD(id);
        if (response.getCode() == 200) {
            response.setMessage("停用成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 活动启用
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "活动启用", notes = "活动启用", response = Response.class)
    @RequestMapping(value = "update_upD", method = RequestMethod.POST)
    public Response updateUpD(@ApiParam(value = "活动id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        int result = productCategoryFacade.updateUpD(id);
        if (response.getCode() == 200) {
            response.setMessage("启用成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 编辑活动
     *
     * @param name
     * @param id
     * @param discount
     * @param content
     * @param startdate
     * @param enddate
     * @param isenrent
     * @param rentday
     * @param orderid
     * @param isdel
     * @return
     */
    @ApiOperation(value = "编辑活动", notes = "编辑活动", response = Response.class)
    @RequestMapping(value = "update_active", method = RequestMethod.POST)
    public Response updateGoodsDis(@ApiParam(value = "活动名称") @RequestParam String name,
                                   @ApiParam(value = "活动id") @RequestParam(required = false) String id,
                                   @ApiParam(value = "活动折扣") @RequestParam String discount,
                                   @ApiParam(value = "活动内容") @RequestParam(required = false) String content,
                                   @ApiParam(value = "活动开始时间") @RequestParam String startdate,
                                   @ApiParam(value = "活动结束时间") @RequestParam String enddate,
                                   @ApiParam(value = "是否为整租活动：0 是 1 否',") @RequestParam(required = false) String isenrent,
                                   @ApiParam(value = "整租天数") @RequestParam(required = false) String rentday,
                                   @ApiParam(value = "排序id") @RequestParam(required = false) String orderid,
                                   @ApiParam(value = "是否启用") @RequestParam(required = false) String isdel
    ) {
        Response response = new Response();
        Map<String, Integer> map = productCategoryFacade.updateGoodsDis(name, id, discount, content, startdate, enddate, isenrent, rentday, orderid, isdel);
        if (response.getCode() == 200) {
            response.setMessage("修改成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询", notes = "根据id查询", response = Response.class)
    @RequestMapping(value = "query_activebyid", method = RequestMethod.POST)
    public Response queryGoodsDiscount(@ApiParam(value = "活动id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        GoodsDiscount goodsDiscount = productCategoryFacade.queryGoodsDiscount(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsDiscount);
        return response;
    }

    /**
     * 增加活动
     *
     * @param name
     * @param
     * @param discount
     * @param content
     * @param startdate
     * @param enddate
     * @param isenrent
     * @param rentday
     * @param orderid
     * @param isdel
     * @return
     */
    @ApiOperation(value = "增加活动", notes = "增加活动", response = Response.class)
    @RequestMapping(value = "add_active", method = RequestMethod.POST)
    public Response updateGoodsDis(@ApiParam(value = "活动名称") @RequestParam String name,
                                   @ApiParam(value = "活动折扣") @RequestParam String discount,
                                   @ApiParam(value = "活动内容") @RequestParam(required = false) String content,
                                   @ApiParam(value = "活动开始时间") @RequestParam String startdate,
                                   @ApiParam(value = "活动结束时间") @RequestParam String enddate,
                                   @ApiParam(value = "是否为整租活动：0 是 1 否',") @RequestParam(required = false) String isenrent,
                                   @ApiParam(value = "整租天数") @RequestParam(required = false) String rentday,
                                   @ApiParam(value = "排序id") @RequestParam(required = false) String orderid,
                                   @ApiParam(value = "是否启用") @RequestParam(required = false) String isdel
    ) {
        Response response = new Response();
        Map<String, Integer> map = productCategoryFacade.addGoodsDiscount(name, discount, content, startdate, enddate, isenrent, rentday, orderid, isdel);
        if (response.getCode() == 200) {
            response.setMessage("增加成功");
        }
        response.setData(map);
        return response;
    }
}

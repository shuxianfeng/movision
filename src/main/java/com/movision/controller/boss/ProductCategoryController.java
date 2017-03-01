package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.ProductCategoryFacade;
import com.movision.mybatis.productcategory.entity.ProductCategory;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


}

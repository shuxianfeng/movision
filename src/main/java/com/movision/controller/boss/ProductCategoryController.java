package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.ProductCategoryFacade;
import com.movision.mybatis.productcategory.entity.ProductCategory;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
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
    public Response queryGoodsList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @RequestParam(required = false, defaultValue = "10") String pageSize
    ) {
        Response response = new Response();
        Paging<ProductCategory> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ProductCategory> list = productCategoryFacade.findAllCategory(pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }
}

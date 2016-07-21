package com.zhuhuibao.business.system.brand.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by cxx on 2016/3/7 0007.
 */
@RestController
public class BrandController {
    private static final Logger log = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    private BrandService brandService;

    @Autowired
    ApiConstants ApiConstants;


    @ApiOperation(value = "新建品牌",notes = "新建品牌", response = Response.class)
    @RequestMapping(value = {"/rest/addBrand","/rest/system/mc/brand/add_brand"}, method = RequestMethod.POST)
    public Response upload(@ModelAttribute Brand brand) {
        Response result = new Response();
        brandService.addBrand(brand);
        return result;
    }

    @ApiOperation(value = "更新品牌",notes = "更新品牌", response = Response.class)
    @RequestMapping(value = {"/rest/updateBrand","/rest/system/mc/brand/upd_brand"}, method = RequestMethod.POST)
    public Response updateBrand(@ModelAttribute Brand brand, @RequestParam(required = false) String type)  {
        Response result = new Response();
        //如果是未通过的品牌进行更新，则状态变为待审核
        if(type==null || "".equals(type)){
            if(brand != null && brand.getId() != null) {
                Brand brand1 = brandService.brandDetails(String.valueOf(brand.getId()));
                if (brand1.getStatus() == 0) {
                    brand.setStatus(2);
                }
            }
        }
        brandService.updateBrand(brand);
        return result;
    }

    @ApiOperation(value = "删除品牌",notes = "删除品牌", response = Response.class)
    @RequestMapping(value = {"/rest/deleteBrand","/rest/system/mc/brand/del_brand"}, method = RequestMethod.POST)
    public Response deleteBrand(@RequestParam String id)  {
        Response result = new Response();
        brandService.deleteBrand(id);
        return result;
    }

    @ApiOperation(value = "批量删除品牌",notes = "批量删除品牌", response = Response.class)
    @RequestMapping(value = {"/rest/batchDeleteBrand","/rest/system/mc/brand/del_batch_brand"}, method = RequestMethod.POST)
    public Response batchDeleteBrand(@ApiParam(value = "ids,逗号隔开") @RequestParam String ids)  {
        Response result = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            brandService.deleteBrand(id);
        }
        return result;
    }

    @ApiOperation(value = "查询品牌（自己发布的品牌）",notes = "查询品牌（自己发布的品牌）", response = Response.class)
    @RequestMapping(value = {"/rest/searchBrand","/rest/system/mc/brand/sel_my_brand"}, method = RequestMethod.GET)
    public Response searchBrand(@ModelAttribute Brand brand)  {
        List<Brand> brandList = brandService.searchBrand(brand);
        Response result = new Response();
        result.setData(brandList);
        return result;
    }


    @ApiOperation(value = "查询品牌",notes = "查询品牌", response = Response.class)
    @RequestMapping(value = {"/rest/searchBrandSelect","/rest/system/mc/brand/sel_brand_by_createId"}, method = RequestMethod.GET)
    public Response searchBrandSelect(@ModelAttribute Brand brand)  {
        List<Brand> brandList = brandService.searchBrandByStatus(brand);
        List list = new ArrayList();
        for (Brand aBrandList : brandList) {
            Map map = new HashMap();
            Brand brand1 = aBrandList;
            map.put("code", brand1.getId());
            map.put("name", brand1.getCNName());
            list.add(map);
        }
        Response result = new Response();
        result.setData(list);
        return result;
    }

    @ApiOperation(value = "品牌数量（自己发布的品牌）",notes = "品牌数量（自己发布的品牌）", response = Response.class)
    @RequestMapping(value = {"/rest/searchBrandSize","/rest/system/mc/brand/sel_brand_size"}, method = RequestMethod.GET)
    public Response searchBrandSize(@ModelAttribute Brand brand) {
        int size = brandService.searchBrandSize(brand);
        Response result = new Response();
        result.setData(size);
        return result;
    }



    @ApiOperation(value = "查看品牌详情",notes = "查看品牌详情", response = Response.class)
    @RequestMapping(value = {"/rest/brandDetails","/rest/system/mc/brand/sel_brand"}, method = RequestMethod.GET)
    public Response brandDetails(@RequestParam String id) {
        Brand brand = brandService.brandDetails(id);
        Response result = new Response();
        result.setData(brand);
        return result;
    }

    @ApiOperation(value = "查询二级系统下所有品牌",notes = "查询二级系统下所有品牌", response = Response.class)
    @RequestMapping(value = {"/rest/brand/findBrandByScateid","/rest/system/mc/brand/sel_brand_by_scateid"}, method = RequestMethod.GET)
    public Response findBrandByScateid(@ModelAttribute Product product)  {
        List<ResultBean> brandList = brandService.findBrandByScateid(product);
        Response result = new Response();
        result.setData(brandList);
        return result;
    }
}
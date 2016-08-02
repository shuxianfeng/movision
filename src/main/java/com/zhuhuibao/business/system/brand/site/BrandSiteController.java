package com.zhuhuibao.business.system.brand.site;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.pojo.*;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.oms.entity.Category;
import com.zhuhuibao.mybatis.oms.service.CategoryService;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品牌业务管理
 * Created by cxx on 2016/3/16 0016.
 */
@RestController
public class BrandSiteController {
    private static final Logger log = LoggerFactory.getLogger(BrandSiteController.class);

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询大系统，子系统，品牌（首页）
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/category/all","/rest/system/site/category/sel_all_category"}, method = RequestMethod.GET)
    public Response findAll()  {
        Response result = new Response();
        List<ResultBean> sysList = categoryService.findSystemList();
        List<SysBean> allList = categoryService.searchAll();
        List<BrandBean> brandList = brandService.searchAll();
        List list = new ArrayList();
        for (ResultBean aSysList : sysList) {
            List list1 = new ArrayList();
            Map map1 = new HashMap();
            ResultBean a = aSysList;
            map1.put("id", a.getCode());
            map1.put("name", a.getName());
            map1.put("icon", a.getSmallIcon());
            for (SysBean anAllList : allList) {
                Map map2 = new HashMap();
                SysBean b = anAllList;
                if (a.getCode().equals(b.getId())) {
                    map2.put("id", b.getCode());
                    map2.put("name", b.getSubSystemName());
                    List list3 = new ArrayList();
                    for (BrandBean aBrandList : brandList) {
                        Map map3 = new HashMap();
                        BrandBean c = aBrandList;
                        if (b.getCode().equals(c.getScateid())) {
                            map3.put("id", c.getId());
                            map3.put("name", c.getBrandCNName());
                            list3.add(map3);
                        }
                    }
                    map2.put("brand", list3);
                    list1.add(map2);
                }
            }
            map1.put("subSystem", list1);
            list.add(map1);
        }
        result.setCode(200);
        result.setData(list);

        return result;
    }

    /**
     * 查询二级系统下推荐品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/brand/findSuggestBrand","/rest/system/site/brand/sel_suggest_brand_by_subCategory"}, method = RequestMethod.GET)
    public Response findSuggestBrand(String id)  {
        Response result = new Response();
        List<ResultBean> SubSystemList = categoryService.findSubSystemListLimit(id);
        List<SuggestBrand> brandList = brandService.SuggestBrand();
        List list = new ArrayList();
        for (ResultBean aSubSystemList : SubSystemList) {
            List list1 = new ArrayList();
            Map map1 = new HashMap();
            ResultBean a = aSubSystemList;
            map1.put("id", a.getCode());
            map1.put("name", a.getName());
            for (SuggestBrand aBrandList : brandList) {
                Map map2 = new HashMap();
                SuggestBrand b = aBrandList;
                if (a.getCode().equals(b.getScateid())) {
                    if (list1.size() < 6) {
                        map2.put("id", b.getId());
                        map2.put("name", b.getBrandCNName());
                        map2.put("logoUrl", b.getLogoUrl());
                        list1.add(map2);
                    }
                }
            }
            map1.put("brand", list1);
            list.add(map1);
        }
        result.setCode(200);
        result.setData(list);

        return result;
    }
    /**
     * 查询大系统下的子系统
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/category/findSubSystem","/rest/system/site/category/sel_sub_category"}, method = RequestMethod.GET)
    public Response findSubSystem(String id)  {
        List<ResultBean> subSystemList = categoryService.findSubSystemList(id);
        Map map = new HashMap();
        Category category = categoryService.findSystem(id);
        map.put("id",id);
        map.put("name",category.getName());
        map.put("url",category.getBigIcon());
        map.put("subSystemList",subSystemList);
        Response result = new Response();
        result.setCode(200);
        result.setData(map);

        return result;
    }

    /**
     * 查询二级系统下所有品牌
     * @return
     * @throws IOException
     */
    @ApiOperation(value="查询二级系统下所有品牌",notes="查询二级系统下所有品牌",response = Response.class)
    @RequestMapping(value = {"/rest/brand/findAllBrand","/rest/system/site/brand/sel_all_brand"}, method = RequestMethod.GET)
    public Response findAllBrand(@RequestParam String scateid) {
        List<Map<String,Object>> brandList = brandService.findAllBrand(scateid);
        Response result = new Response();
        result.setCode(200);
        result.setData(brandList);
        return result;
    }

    /**
     * 查询推荐品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/searchSuggestBrand","/rest/system/site/brand/sel_suggest_brand"}, method = RequestMethod.GET)
    public Response searchSuggestBrand()  {
        List<Map<String,Object>> brandList = brandService.searchSuggestBrand();
        Response result = new Response();
        result.setCode(200);
        result.setData(brandList);

        return result;
    }

    /**
     * 查询品牌详情
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/brand/details","/rest/system/site/brand/sel_brand"}, method = RequestMethod.GET)
    public Response details(String id, String scateid)  {
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        Map map3 = new HashMap();
        //品牌详情
        BrandDetailBean brand = brandService.details(id);
        Brand brand1 = brandService.brandDetails(id);
        if(brand1.getViews()==null){
            brand1.setViews(1);
        }else{
            brand1.setViews(brand1.getViews()+1);
        }
        brandService.updateBrand(brand1);
        map2.put("company",brand.getCompany());
        map2.put("webSite",brand.getWebSite());
        map2.put("phone",brand.getPhone());
        map2.put("address",brand.getAddress());
        map2.put("introduce",brand.getBrandDesc());
        map2.put("imgUrl",brand.getImgUrl());
        map2.put("logo",brand.getLogo());

        ResultBean  result = categoryService.querySystem(scateid);
        map3.put("brandid",id);
        map3.put("brandName",brand.getCnName());
        map2.put("brandName",brand.getCnName());
        map3.put("scateid",scateid);
        map3.put("scateName",result.getName());
        ResultBean  result1 = categoryService.querySystem(result.getSmallIcon());
        map3.put("fcateid",result1.getCode());
        map3.put("fcateName",result1.getName());
        map1.put("brandDesc",map2);
        map1.put("navigation",map3);
        Response response = new Response();
        response.setCode(200);
        response.setData(map1);

        return response;
    }

    /**
     * 品牌对应的子系统
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/brand/findSubSystemByBrand","/rest/system/site/category/sel_category_by_brand"}, method = RequestMethod.GET)
    public Response findSubSystemByBrand(String id)  {
        //品牌对应的子系统
        List<ResultBean> list = productService.findSubSystem(id);
        Response result = new Response();
        result.setData(list);
        return result;
    }
}

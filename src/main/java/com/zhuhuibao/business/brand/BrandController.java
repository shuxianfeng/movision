package com.zhuhuibao.business.brand;

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
public class BrandController {
    private static final Logger log = LoggerFactory.getLogger(BrandController.class);

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
    @RequestMapping(value = "/rest/category/all", method = RequestMethod.GET)
    public JsonResult findAll() throws Exception {
        JsonResult result = new JsonResult();
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
    @RequestMapping(value = "/rest/brand/findSuggestBrand", method = RequestMethod.GET)
    public JsonResult findSuggestBrand(String id) throws Exception {
        JsonResult result = new JsonResult();
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
    @RequestMapping(value = "/rest/category/findSubSystem", method = RequestMethod.GET)
    public JsonResult findSubSystem(String id) throws Exception {
        List<ResultBean> subSystemList = categoryService.findSubSystemList(id);
        Map map = new HashMap();
        Category category = categoryService.findSystem(id);
        map.put("id",id);
        map.put("name",category.getName());
        map.put("url",category.getBigIcon());
        map.put("subSystemList",subSystemList);
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(map);

        return result;
    }

    /**
     * 查询二级系统下所有品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/brand/findAllBrand", method = RequestMethod.GET)
    public JsonResult findAllBrand(Product product) throws Exception {
        List<ResultBean> brandList = brandService.findAllBrand(product);
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(brandList);

        return result;
    }

    /**
     * 查询推荐品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/searchSuggestBrand", method = RequestMethod.GET)
    public JsonResult searchSuggestBrand() throws Exception {
        List<ResultBean> brandList = brandService.searchSuggestBrand();
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(brandList);

        return result;
    }

    /**
     * 查询品牌详情
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/brand/details", method = RequestMethod.GET)
    public JsonResult details(String id, String scateid) throws Exception {
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        Map map3 = new HashMap();
        //品牌详情
        BrandDetailBean brand = brandService.details(id);
        Brand brand1 = brandService.brandDetails(Integer.parseInt(id));
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
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(200);
        jsonResult.setData(map1);

        return jsonResult;
    }

    /**
     * 品牌对应的子系统
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/brand/findSubSystemByBrand", method = RequestMethod.GET)
    public JsonResult findSubSystemByBrand(String id) throws Exception {
        //品牌对应的子系统
        List<ResultBean> list = productService.findSubSystem(id);
        JsonResult result = new JsonResult();
        result.setData(list);
        return result;
    }
}

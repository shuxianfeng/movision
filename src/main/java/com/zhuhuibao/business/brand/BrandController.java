package com.zhuhuibao.business.brand;

import com.zhuhuibao.common.*;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.oms.entity.Category;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/3/16 0016.
 */
@RestController
public class BrandController {
    private static final Logger log = LoggerFactory
            .getLogger(BrandController.class);

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询大系统，子系统，品牌（首页）
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/category/all", method = RequestMethod.GET)
    public void findAll(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult result = new JsonResult();
        List<ResultBean> sysList = categoryMapper.findSystemList();
        List<SysBean> allList = categoryMapper.searchAll();
        List<BrandBean> brandList = brandMapper.searchAll();
        List list = new ArrayList();
        for(int i=0;i<sysList.size();i++){
            List list1 = new ArrayList();
            Map map1 = new HashMap();
            ResultBean a = sysList.get(i);
            map1.put("id",a.getCode());
            map1.put("name",a.getName());
            map1.put("icon",a.getSmallIcon());
            for(int y=0;y<allList.size();y++){
                Map map2 = new HashMap();
                SysBean b = allList.get(y);
                if(a.getCode().equals(b.getId())){
                    map2.put("id",b.getCode());
                    map2.put("name",b.getSubSystemName());
                    List list3 = new ArrayList();
                    for(int x=0;x<brandList.size();x++){
                        Map map3 = new HashMap();
                        BrandBean c = brandList.get(x);
                        if(b.getCode().equals(c.getScateid())){
                            map3.put("id",c.getId());
                            map3.put("name",c.getBrandCNName());
                            list3.add(map3);
                        }
                    }
                    map2.put("brand",list3);
                    list1.add(map2);
                }
            }
            map1.put("subSystem",list1);
            list.add(map1);
        }
        result.setCode(200);
        result.setData(list);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 查询二级系统下推荐品牌
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/brand/findSuggestBrand", method = RequestMethod.GET)
    public void findSuggestBrand(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult result = new JsonResult();
        List<ResultBean> SubSystemList = categoryMapper.findSubSystemList(id);
        List<SuggestBrand> brandList = brandMapper.SuggestBrand();
        List list = new ArrayList();
        for(int i=0;i<SubSystemList.size();i++){
            List list1 = new ArrayList();
            Map map1 = new HashMap();
            ResultBean a = SubSystemList.get(i);
            map1.put("id",a.getCode());
            map1.put("name",a.getName());
            for(int x=0;x<brandList.size();x++){
                Map map2 = new HashMap();
                SuggestBrand b = brandList.get(x);
                if(a.getCode().equals(b.getScateid())){
                    if(list1.size()<6){
                        map2.put("id",b.getId());
                        map2.put("name",b.getBrandCNName());
                        map2.put("logoUrl",b.getLogoUrl());
                        list1.add(map2);
                    }
                }
            }
            map1.put("brand",list1);
            list.add(map1);
        }
        result.setCode(200);
        result.setData(list);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }
    /**
     * 查询大系统下的子系统
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/category/findSubSystem", method = RequestMethod.GET)
    public void findSubSystem(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        List<ResultBean> subSystemList = categoryMapper.findSubSystemList(id);
        Map map = new HashMap();
        Category category = categoryMapper.findSystem(id);
        map.put("id",id);
        map.put("name",category.getName());
        map.put("url",category.getBigIcon());
        map.put("subSystemList",subSystemList);
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 查询二级系统下所有品牌
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/brand/findAllBrand", method = RequestMethod.GET)
    public void findAllBrand(HttpServletRequest req, HttpServletResponse response, Product product) throws IOException {
        List<ResultBean> brandList = brandMapper.findAllBrand(product);
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(brandList);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 查询推荐品牌
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/searchSuggestBrand", method = RequestMethod.GET)
    public void searchSuggestBrand(HttpServletRequest req, HttpServletResponse response) throws IOException {
        List<ResultBean> brandList = brandMapper.searchSuggestBrand();
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(brandList);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }


}

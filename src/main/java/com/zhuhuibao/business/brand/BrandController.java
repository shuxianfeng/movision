package com.zhuhuibao.business.brand;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.oms.entity.Category;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
     * 查询二级系统下所有品牌，分页
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/findAllBrand", method = RequestMethod.GET)
    public void findAllBrand(HttpServletRequest req, HttpServletResponse response, Product product, String pageNo, String pageSize) throws IOException {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<ResultBean> pager = new Paging<ResultBean>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<ResultBean> brandList = brandMapper.findAllBrand(pager.getRowBounds(),product);
        pager.result(brandList);
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(pager);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 查询二级系统下num个推荐品牌
     * @param req
     * @return
     * @throws IOException
     *//*
    @RequestMapping(value = "/rest/searchSuggestBrandByNumber", method = RequestMethod.GET)
    public void searchAllBrandByNumber(HttpServletRequest req, HttpServletResponse response, Product product) throws IOException {
        List<ResultBean> brandList = brandMapper.searchAllBrandByNumber(product);
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(brandList);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }*/

    /**
     * 查询num个推荐品牌
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

    /**
     * 查询大系统下的子系统
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/findSubSystem", method = RequestMethod.GET)
    public void findSubSystem(HttpServletRequest req, HttpServletResponse response,String parentId) throws IOException {
        List<ResultBean> subSystemList = categoryMapper.findSubSystemList(parentId);
        Map map = new HashMap();
        Category category = categoryMapper.findSystem(parentId);
        map.put("id",parentId);
        map.put("name",category.getName());
        map.put("url",category.getBigIcon());
        map.put("subSystemList",subSystemList);
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }
}

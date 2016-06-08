package com.zhuhuibao.business.oms.system;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.pojo.ResultBean;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 运营中心类目管理
 * Created by cxx on 2016/3/4 0004.
 */
@RestController
public class SystemController {
    private static final Logger log = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    /**
     * 查询大系统类目
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/systemSearch","/rest/system/oms/category/sel_bigSystem"}, method = RequestMethod.GET)
    public Response systemSearch() throws Exception {
        Response response = new Response();
        List<ResultBean> systemList = categoryService.findSystemList();
        response.setCode(200);
        response.setData(systemList);
        return response;
    }

    /**
     * 查询大系统下所有子系统类目
     * @param req
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/subSystemSearch","/rest/system/oms/category/sel_subSystem"}, method = RequestMethod.GET)
    public Response subSystemSearch(HttpServletRequest req) throws Exception {
        Response response = new Response();
        String parentId = req.getParameter("parentId");
        List<ResultBean> subSystemList = categoryService.findSubSystemList(parentId);
        response.setCode(200);
        response.setData(subSystemList);
        return response;
    }


    /**
     * 查询大系统，子系统
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/system/findSystem","/rest/system/oms/category/sel_allSystem"}, method = RequestMethod.GET)
    public Response findSystem() throws IOException {
        return categoryService.findAllSystem();
    }

    /**
     * 添加类目
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/system/addSystem","/rest/system/oms/category/add_system"}, method = RequestMethod.POST)
    public Response addSystem(Category category) throws Exception {
        Response result = new Response();
        categoryService.addSystem(category);
        return result;
    }

    /**
     * 编辑类目
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/system/updateSystem","/rest/system/oms/category/upd_system"}, method = RequestMethod.POST)
    public Response updateSystem(Category category) throws Exception {
        Response result = new Response();
        categoryService.updateSystem(category);
        return result;
    }

    /**
     * 删除类目
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/system/deleteSystem","/rest/system/oms/category/del_system"}, method = RequestMethod.POST)
    public Response deleteSystem(Category category) throws Exception {
        Response result = new Response();
        Product product = productService.findProductBySystemId(category.getId().toString());
        if(product!=null){
            result.setCode(400);
            result.setMessage("该系统下有产品，无法删除");
        }else{
            categoryService.deleteSystem(category);
            result.setCode(200);
        }

        return result;
    }

}

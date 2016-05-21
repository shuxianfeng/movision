package com.zhuhuibao.business.oms.system;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
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
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = "/rest/systemSearch", method = RequestMethod.GET)
    public JsonResult systemSearch() throws Exception {
        JsonResult jsonResult = new JsonResult();
        List<ResultBean> systemList = categoryService.findSystemList();
        jsonResult.setCode(200);
        jsonResult.setData(systemList);
        return jsonResult;
    }

    /**
     * 查询大系统下所有子系统类目
     * @param req
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/rest/subSystemSearch", method = RequestMethod.GET)
    public JsonResult subSystemSearch(HttpServletRequest req) throws Exception {
        JsonResult jsonResult = new JsonResult();
        String parentId = req.getParameter("parentId");
        List<ResultBean> subSystemList = categoryService.findSubSystemList(parentId);
        jsonResult.setCode(200);
        jsonResult.setData(subSystemList);
        return jsonResult;
    }


    /**
     * 查询大系统，子系统
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/rest/system/findSystem", method = RequestMethod.GET)
    public JsonResult findSystem() throws IOException {
        return categoryService.findAllSystem();
    }

    /**
     * 添加类目
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/rest/system/addSystem", method = RequestMethod.POST)
    public JsonResult addSystem(Category category) throws Exception {
        JsonResult result = new JsonResult();
        categoryService.addSystem(category);
        return result;
    }

    /**
     * 编辑类目
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/rest/system/updateSystem", method = RequestMethod.POST)
    public JsonResult updateSystem(Category category) throws Exception {
        JsonResult result = new JsonResult();
        categoryService.updateSystem(category);
        return result;
    }

    /**
     * 删除类目
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/rest/system/deleteSystem", method = RequestMethod.POST)
    public JsonResult deleteSystem(Category category) throws Exception {
        JsonResult result = new JsonResult();
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

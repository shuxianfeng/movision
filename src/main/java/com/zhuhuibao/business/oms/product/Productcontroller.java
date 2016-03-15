package com.zhuhuibao.business.oms.product;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.oms.entity.Category;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
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
import java.util.List;

/**
 * Created by cxx on 2016/3/4 0004.
 */
@RestController
public class ProductController {
    private static final Logger log = LoggerFactory
            .getLogger(ProductController.class);

    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 查询大系统类目
     * @param req
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/rest/systemSearch", method = RequestMethod.GET)
    public void systemSearch(HttpServletRequest req, HttpServletResponse response) throws IOException {
        List<ResultBean> systemList = categoryMapper.findSystemList();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(systemList));
    }

    /**
     * 查询大系统下所有子系统类目
     * @param req
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/rest/subSystemSearch", method = RequestMethod.GET)
    public void subSystemSearch(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String parentId = req.getParameter("parentId");
        List<ResultBean> subSystemList = categoryMapper.findSubSystemList(parentId);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(subSystemList));
    }

    /**
     * 添加类目
     * @param req
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/rest/addSystem", method = RequestMethod.POST)
    public void addSystem(HttpServletRequest req, HttpServletResponse response,Category category) throws IOException {
        JsonResult result = new JsonResult();
        int isAdd = categoryMapper.addSystem(category);
        if(isAdd==0){
            result.setCode(400);
            result.setMessage("添加失败");
        }else{
            result.setCode(200);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 编辑类目
     * @param req
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/rest/updateSystem", method = RequestMethod.POST)
    public void updateSystem(HttpServletRequest req, HttpServletResponse response,Category category) throws IOException {
        JsonResult result = new JsonResult();
        int isUpdate = categoryMapper.updateSystem(category);
        if(isUpdate==0){
            result.setCode(400);
            result.setMessage("更新失败");
        }else{
            result.setCode(200);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 删除类目
     * @param req
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/rest/deleteSystem", method = RequestMethod.POST)
    public void deleteSystem(HttpServletRequest req, HttpServletResponse response,Category category) throws IOException {
        JsonResult result = new JsonResult();
        int isDelete = categoryMapper.deleteSystem(category);
        if(isDelete==0){
            result.setCode(400);
            result.setMessage("删除失败");
        }else{
            result.setCode(200);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }
}

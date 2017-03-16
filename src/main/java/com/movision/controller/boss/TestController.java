package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.user.MenuFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/16 11:16
 */
@RestController
public class TestController {

    @Autowired
    private MenuFacade menuFacade;

    @ApiOperation(value = "查询所有菜单", notes = "查询所有菜单", response = Response.class)
    @RequestMapping(value = {"/boss/test/query_all_menu", "/boss/test/lallaalalal"}, method = RequestMethod.GET)
    public Response queryAllMenu() {
        Response response = new Response();
        List<Map<String, Object>> list = menuFacade.getAllMenu();
        response.setData(list);
        return response;
    }
}

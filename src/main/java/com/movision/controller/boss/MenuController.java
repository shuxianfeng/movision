package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.user.MenuFacade;
import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author zhuangyuhao
 * @Date 2017/1/19 17:15
 */
@RestController
@RequestMapping("/boss/menu")
public class MenuController {

    @Autowired
    private MenuFacade menuFacade;

    @ApiOperation(value = "新增菜单", notes = "新增菜单", response = Response.class)
    @RequestMapping(value = "add_menu", method = RequestMethod.POST)
    public Response addMenu(@ApiParam @ModelAttribute Menu menu) {
        Response response = new Response();
        boolean isAdd = menuFacade.addMenu(menu);
        if (isAdd) {
            response.setCode(200);
        } else {
            response.setCode(400);
        }
        return response;
    }

    @ApiOperation(value = "修改菜单", notes = "修改菜单", response = Response.class)
    @RequestMapping(value = "update_menu", method = RequestMethod.POST)
    public Response updateMenu(@ApiParam @ModelAttribute Menu menu) {
        Response response = new Response();
        boolean isAdd = menuFacade.updateMenu(menu);
        if (isAdd) {
            response.setCode(200);
        } else {
            response.setCode(400);
        }
        return response;
    }

    @ApiOperation(value = "查询菜单详情", notes = "查询菜单详情", response = Response.class)
    @RequestMapping(value = "query_menu", method = RequestMethod.GET)
    public Response queryMenu(@ApiParam(value = "菜单id") @RequestParam int id) {
        Response response = new Response();
        Menu menu = menuFacade.queryMenu(id);
        response.setData(menu);
        return response;
    }

    @ApiOperation(value = "查询菜单列表（分页）", notes = "查询菜单列表（分页）", response = Response.class)
    @RequestMapping(value = "query_menu_list", method = RequestMethod.GET)
    public Response queryMenuList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                  @RequestParam(required = false, defaultValue = "10") String pageSize,
                                  @ApiParam(value = "菜单名称") @RequestParam(required = false) String menuname) {
        Response response = new Response();
        Paging<Menu> pager = new Paging<Menu>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Menu> list = menuFacade.queryMenuList(pager, menuname);
        pager.result(list);
        response.setData(pager);
        return response;
    }


}

package com.movision.controller.boss;

import com.movision.facade.user.MenuFacade;
import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.mybatis.bossMenu.entity.MenuDetail;
import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/14 16:42
 */
public class MenuControllerTest extends SpringTestCase {

    @Autowired
    private MenuFacade menuFacade;

/*
    @Test
    public void queryMenu() throws Exception {
        MenuDetail menu = menuFacade.queryMenuDetail(2);
        System.out.println(menu.toString());
    }
*/

    /*
        @Test
        public void updateMenu() {
            Menu menu = new Menu();
            menu.setId(2);
            menu.setRemark("菜单管理啦啦啦啦");
            menu.setUrl("boss/menu");
            boolean isAdd = menuFacade.updateMenu(menu);
            System.out.println(isAdd);
        }
    */
    @Test
    public void addMenu() {
        Menu menu = new Menu();
        menu.setMenuname("订单管理子菜单-测试用");
        menu.setPid(173);
        menuFacade.addMenu(menu);
    }





}
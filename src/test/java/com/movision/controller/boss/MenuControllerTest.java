package com.movision.controller.boss;

import com.movision.facade.user.MenuFacade;
import com.movision.mybatis.bossMenu.entity.Menu;
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

    @Test
    public void queryMenu() throws Exception {
        Menu menu = menuFacade.queryMenu(1);
        System.out.println(menu.toString());
    }

}
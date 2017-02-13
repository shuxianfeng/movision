package com.movision.facade.user;

import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.mybatis.bossMenu.service.MenuService;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/19 17:05
 */
@Service
public class MenuFacade {

    private static Logger log = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    private MenuService menuService;

    public Boolean addMenu(Menu menu) {
        return menuService.addAdminMenu(menu);
    }

    public Boolean updateMenu(Menu menu) {
        return menuService.updateMenu(menu);
    }

    public Menu queryMenu(int id) {
        return menuService.queryMenu(id);
    }

    public List<Menu> queryMenuList(Paging<Menu> pager, String menuname) {

        Map<String, Object> map = new HashedMap();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(menuname)) {
            map.put("menuname", menuname);
        }
        return menuService.queryMenuList(pager, map);
    }


    public List<Map<String, Object>> getAllMenu() {
        List<Menu> allParentMenuList = menuService.getAllParentMenu();
        List<Menu> allChildrenMenuList = menuService.getAllChildrenMenu();

        List<Map<String, Object>> result = new ArrayList<>();
        Map map = new HashedMap();

        for (Menu pmenu : allParentMenuList) {
            int parid = pmenu.getId();

            List<Menu> sameParentMenus = new ArrayList<>();
            for (Menu cmenu : allChildrenMenuList) {
                int pid = cmenu.getPid();
                if (pid == parid) {
                    //若是属于该父菜单的字菜单，则放入同一个list
                    sameParentMenus.add(cmenu);
                }
            }
            map.put("parent_menu", pmenu);
            map.put("child_menu", sameParentMenus);
            result.add(map);
        }
        return result;
    }
}

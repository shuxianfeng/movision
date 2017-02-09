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

    public List<Menu> queryMenuList(String pageNo, String pageSize, String menuname) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Menu> pager = new Paging<Menu>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashedMap();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(menuname)) {
            map.put("menuname", menuname);
        }
        return menuService.queryMenuList(pager, map);
    }

    public List<Map<String, Object>> getAllMenu() {
        List<Menu> menuList = menuService.getAllMenu();
        for (Menu menu : menuList) {
            int pid = menu.getPid();
            if (pid == 0) {
                //是一个父菜单
                int id = menu.getId();

            }
        }
        return null;
    }
}

package com.movision.mybatis.bossMenu.service;

import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.mybatis.bossMenu.mapper.MenuMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/19 16:54
 */
@Service
public class MenuService {

    private static Logger log = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    private MenuMapper adminMenuMapper;

    public Boolean addAdminMenu(Menu menu) {
        try {
            log.info("新增菜单，menu=" + menu.toString());
            int n = adminMenuMapper.insertSelective(menu);
            return n == 1;
        } catch (Exception e) {
            log.error("新增菜单异常，menu=" + menu.toString(), e);
            throw e;
        }
    }

    public Boolean updateMenu(Menu menu) {
        try {
            log.info("修改菜单，menu=" + menu.toString());
            int n = adminMenuMapper.updateByPrimaryKeySelective(menu);
            return n == 1;
        } catch (Exception e) {
            log.error("修改菜单异常，menu=" + menu.toString(), e);
            throw e;
        }
    }

    public Menu queryMenu(int id) {
        try {
            log.info("查询菜单详情，id=" + id);
            return adminMenuMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("查询菜单详情，id=" + id, e);
            throw e;
        }
    }

    public List<Menu> queryMenuList(Paging<Menu> pager, Map<String, Object> map) {
        try {
            log.info("查询菜单列表");
            return adminMenuMapper.selectMenuList(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询菜单列表异常", e);
            throw e;
        }
    }


}

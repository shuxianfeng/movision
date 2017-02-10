package com.movision.mybatis.bossMenu.service;

import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.mybatis.bossMenu.mapper.MenuMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/19 16:54
 */
@Service
@Transactional
public class MenuService {

    private static Logger log = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    private MenuMapper menuMapper;

    public Boolean addAdminMenu(Menu menu) {
        try {
            log.info("新增菜单，menu=" + menu.toString());
            int n = menuMapper.insertSelective(menu);
            return n == 1;
        } catch (Exception e) {
            log.error("新增菜单异常，menu=" + menu.toString(), e);
            throw e;
        }
    }

    public Boolean updateMenu(Menu menu) {
        try {
            log.info("修改菜单，menu=" + menu.toString());
            int n = menuMapper.updateByPrimaryKeySelective(menu);
            return n == 1;
        } catch (Exception e) {
            log.error("修改菜单异常，menu=" + menu.toString(), e);
            throw e;
        }
    }

    //    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Menu queryMenu(int id) {
        try {
            log.info("查询菜单详情，id=" + id);
            return menuMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("查询菜单详情，id=" + id, e);
            throw e;
        }
    }


    public List<Menu> queryMenuList(Paging<Menu> pager, Map<String, Object> map) {
        try {
            log.info("查询菜单列表,传参：" + map.toString());
            return menuMapper.findAllMenuList(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询菜单列表异常", e);
            throw e;
        }
    }

    public List<Menu> queryMenuListByRoleid(Integer id) {
        try {
            log.info("根据角色id查询菜单，id=" + id);
            return menuMapper.selectByRoleid(id);
        } catch (Exception e) {
            log.error("根据角色id查询菜单失败，id=" + id, e);
            throw e;
        }
    }


    public List<Menu> getAllMenu() {
        try {
            log.info("查询所有菜单");
            return menuMapper.selectAllMenu();
        } catch (Exception e) {
            log.error("查询所有菜单失败", e);
            throw e;
        }
    }

    public List<Menu> getAllParentMenu() {
        try {
            log.info("查询所有父级菜单");
            return menuMapper.selectAllParentMenu();
        } catch (Exception e) {
            log.error("查询所有父级菜单失败", e);
            throw e;
        }
    }

    public List<Menu> selectAllChildrenMenu() {
        try {
            log.info("查询所有子级菜单");
            return menuMapper.selectAllChildrenMenu();
        } catch (Exception e) {
            log.error("查询所有子级菜单失败", e);
            throw e;
        }
    }


}

package com.movision.mybatis.bossMenu.mapper;

import com.movision.mybatis.bossMenu.entity.AuthMenu;
import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.mybatis.bossMenu.entity.MenuVo;
import com.movision.mybatis.role.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface MenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    List<Menu> findAllMenuList(RowBounds rowBounds, Map<String, Object> map);

    void delMenu(int[] ids);

    List<Menu> selectByRoleid(@Param("roleid") Integer roleid);


    List<Menu> selectAllMenu();

    List<AuthMenu> selectAllParentMenu();

    List<AuthMenu> selectAllChildrenMenu();

    List<AuthMenu> selectAuthroizeParentMenu(@Param("roleid") Integer roleid);

    List<AuthMenu> selectAuthroizeChildrenMenu(@Param("roleid") Integer roleid);

    int isExistSameName(Menu menu);

    List<MenuVo> querySidebarFather(@Param("roleid") Integer roleid);

    List<MenuVo> querySidebarSon(@Param("roleid") Integer roleid);
}
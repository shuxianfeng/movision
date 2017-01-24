package com.movision.mybatis.adminMenu.mapper;

import com.movision.mybatis.adminMenu.entity.Menu;
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

    List<Menu> selectMenuList(RowBounds rowBounds, Map<String, Object> map);

    // TODO: 2017/1/22 删除菜单
}
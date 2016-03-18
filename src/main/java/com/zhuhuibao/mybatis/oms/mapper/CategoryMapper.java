package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.common.SystemBean;
import com.zhuhuibao.mybatis.oms.entity.Category;

import java.util.List;

public interface CategoryMapper {

    List<ResultBean> findSystemList();

    List<ResultBean> findSubSystemList(String parentId);

    int addSystem(Category category);

    int updateSystem(Category category);

    int deleteSystem(Category category);

    Category findSystem(String parentId);

    List<SystemBean> searchAll();
}
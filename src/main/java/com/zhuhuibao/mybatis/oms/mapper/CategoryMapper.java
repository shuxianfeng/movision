package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.oms.entity.Category;

import java.util.List;

public interface CategoryMapper {

    List<ResultBean> findSystemList();

    List<ResultBean> findSubSystemList(String parentId);

    List<ResultBean> findSubSystemListByNumber(Category category);

    int addSystem(Category category);

    int updateSystem(Category category);

    int deleteSystem(Category category);
}
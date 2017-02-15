package com.movision.mybatis.category.mapper;

import com.movision.mybatis.category.entity.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    List<Category> queryGoodsCategory();

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
}
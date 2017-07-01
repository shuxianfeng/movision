package com.movision.mybatis.circleCategory.mapper;

import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.circleCategory.entity.CircleCategoryVo;

import java.util.List;
import java.util.Map;

public interface CircleCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CircleCategory record);

    int insertSelective(CircleCategory record);

    CircleCategory selectByPrimaryKey(Integer id);

    List<CircleCategory> queryCircleCategoryList();

    List<CircleCategoryVo> queryCircleByCategory();

    int updateByPrimaryKeySelective(CircleCategory record);

    int updateByPrimaryKey(CircleCategory record);

    Category queryCircleCategory(Map map);

    int updateCircleCategory(CircleCategory map);

    List<CircleCategory> queryCircleTytpeListByUserid(Integer userid);

    List<CircleCategory> queryCircleTypeList(Integer userid);
}
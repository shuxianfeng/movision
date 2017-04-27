package com.movision.mybatis.category.mapper;

import com.movision.mybatis.category.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> queryGoodsCategory();

    List<Category> queryCircleTypeList(Integer userid);

    List<Category> queryCircleTytpeListByUserid(Integer userid);

    int addCircleType(Map map);

    Category queryCircleCategory(String category);

    int updateCircleCategory(Map map);
}
package com.movision.mybatis.category.service;

import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.mapper.CategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/2/22 19:10
 */
@Service
public class CategoryService {
    Logger logger = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 查询圈子分类
     *
     * @return
     */
    public List<Category> queryCircleTypeList() {
        try {
            logger.info("查询圈子分类列表");
            return categoryMapper.queryCircleTypeList();
        } catch (Exception e) {
            logger.error("查询圈子分类列表异常");
            throw e;
        }
    }
}

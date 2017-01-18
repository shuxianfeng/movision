package com.movision.mybatis.circleCategory.service;

import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.circleCategory.mapper.CircleCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/18 19:43
 */
@Service
public class CircleCategoryService {

    @Autowired
    private CircleCategoryMapper circleCategoryMapper;

    public List<CircleCategory> queryCircleCategoryList() {
        return circleCategoryMapper.queryCircleCategoryList();
    }
}

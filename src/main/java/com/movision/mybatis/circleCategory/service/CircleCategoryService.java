package com.movision.mybatis.circleCategory.service;

import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.circleCategory.entity.CircleCategoryVo;
import com.movision.mybatis.circleCategory.mapper.CircleCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/18 19:43
 */
@Service
public class CircleCategoryService {
    private static Logger log = LoggerFactory.getLogger(CircleCategoryService.class);

    @Autowired
    private CircleCategoryMapper circleCategoryMapper;

    public List<CircleCategory> queryCircleCategoryList() {
        try {
            log.info("查询圈子类型列表");
            return circleCategoryMapper.queryCircleCategoryList();
        } catch (Exception e) {
            log.error("查询圈子类型列表失败");
            throw e;
        }
    }

    public List<CircleCategoryVo> queryCircleByCategory() {
        try {
            log.info("查询所有圈子的分类");
            return circleCategoryMapper.queryCircleByCategory();
        } catch (Exception e) {
            log.error("查询所有圈子的分类失败");
            throw e;
        }
    }
}

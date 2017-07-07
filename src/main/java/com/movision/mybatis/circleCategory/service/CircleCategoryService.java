package com.movision.mybatis.circleCategory.service;

import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.circleCategory.entity.CircleCategoryVo;
import com.movision.mybatis.circleCategory.mapper.CircleCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/18 19:43
 */
@Service
@Transactional
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


    /**
     * 添加圈子分类
     *
     * @param map
     * @return
     */
    public int addCircleType(CircleCategory map) {
        try {
            log.info("添加圈子分类");
            return circleCategoryMapper.insertSelective(map);
        } catch (Exception e) {
            log.error("添加圈子分类异常", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 回显圈子类型详情接口
     *
     * @param map
     * @return
     */
    public Category queryCircleCategory(Map map) {
        try {
            log.info("回显圈子类型详情接口");
            return circleCategoryMapper.queryCircleCategory(map);
        } catch (Exception e) {
            log.error("回显圈子类型详情接口异常", e);
            throw e;
        }
    }


    /**
     * 编辑圈子类型
     *
     * @param map
     * @return
     */
    public int updateCircleCategory(CircleCategory map) {
        try {
            log.info("编辑圈子类型");
            return circleCategoryMapper.updateCircleCategory(map);
        } catch (Exception e) {
            log.error("编辑圈子类型异常", e);
            throw e;
        }
    }

    /**
     * 根据用户id查询所属圈子分类列表
     *
     * @param userid
     * @return
     */
    public List<CircleCategory> queryCircleTytpeListByUserid(Integer userid) {
        try {
            log.info("根据用户id查询所属圈子分类列表");
            return circleCategoryMapper.queryCircleTytpeListByUserid(userid);
        } catch (Exception e) {
            log.error("根据用户id查询所属圈子分类列表异常", e);
            throw e;
        }
    }
}

package com.movision.mybatis.category.service;

import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.mapper.CategoryMapper;
import com.movision.utils.L;
import net.sf.ehcache.constructs.nonstop.ThrowTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public List<Category> queryCircleTypeList(Integer userid) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("查询圈子分类列表 userid=" + userid);
            }
            return categoryMapper.queryCircleTypeList(userid);
        } catch (Exception e) {
            logger.error("查询圈子分类列表异常 userid=" + userid, e);
            throw e;
        }
    }


    /**
     * 根据用户id查询所属圈子分类列表
     *
     * @param userid
     * @return
     */
    public List<Category> queryCircleTytpeListByUserid(Integer userid) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("根据用户id查询所属圈子分类列表 userid=" + userid);
            }
            return categoryMapper.queryCircleTytpeListByUserid(userid);
        } catch (Exception e) {
            logger.error("根据用户id查询所属圈子分类列表异常 userid=" + userid, e);
            throw e;
        }
    }


    /**
     * 添加圈子分类
     *
     * @param map
     * @return
     */
    public int addCircleType(Map map) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("添加圈子分类");
            }
            return categoryMapper.addCircleType(map);
        } catch (Exception e) {
            logger.error("添加圈子分类异常", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 回显圈子类型详情接口
     *
     * @param category
     * @return
     */
    public Category queryCircleCategory(String category) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("回显圈子类型详情接口 category=" + category);
            }
            return categoryMapper.queryCircleCategory(category);
        } catch (Exception e) {
            logger.error("回显圈子类型详情接口异常 category=" + category, e);
            throw e;
        }
    }

    /**
     * 编辑圈子类型
     *
     * @param map
     * @return
     */
    public int updateCircleCategory(Map map) {
        try {
            logger.info("编辑圈子类型");
            return categoryMapper.updateCircleCategory(map);
        } catch (Exception e) {
            logger.error("编辑圈子类型异常", e);
            throw e;
        }
    }
}

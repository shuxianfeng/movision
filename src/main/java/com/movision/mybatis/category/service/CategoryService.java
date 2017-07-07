package com.movision.mybatis.category.service;

import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.mapper.CategoryMapper;
import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.circleCategory.mapper.CircleCategoryMapper;
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
    @Autowired
    private CircleCategoryMapper circleCategoryMapper;

    /**
     * 查询圈子分类
     *
     * @return
     */
    public List<CircleCategory> queryCircleTypeList(Integer userid) {
        try {
            logger.info("查询圈子分类列表");
            return circleCategoryMapper.queryCircleTypeList(userid);
        } catch (Exception e) {
            logger.error("查询圈子分类列表异常 userid=" + userid, e);
            throw e;
        }
    }

}

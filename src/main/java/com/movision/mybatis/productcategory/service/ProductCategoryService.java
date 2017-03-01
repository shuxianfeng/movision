package com.movision.mybatis.productcategory.service;

import com.movision.mybatis.goods.mapper.GoodsMapper;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.productcategory.entity.ProductCategory;
import com.movision.mybatis.productcategory.mapper.ProductCategoryMapper;
import com.movision.utils.pagination.model.Paging;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/3/1 16:44
 */
@Service
@Transactional
public class ProductCategoryService {

    private static Logger log = LoggerFactory.getLogger(ProductCategoryService.class);

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    /**
     * 查询分类列表
     *
     * @param pager
     * @return
     */
    public List<ProductCategory> findAllCategory(Paging<ProductCategory> pager) {
        try {
            log.info("查询分类列表");
            return productCategoryMapper.findAllProductCategory(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询分类列表失败", e);
            throw e;
        }
    }

    /**
     * 分类搜索
     *
     * @param map
     * @param pager
     * @return
     */
    public List<ProductCategory> findAllCategoryCondition(Map map, Paging<ProductCategory> pager) {
        try {
            log.info("分类搜索");
            return productCategoryMapper.findAllCategoryCondition(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("分类搜索失败", e);
            throw e;
        }
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    public int deleteCategory(Integer id) {
        try {
            log.info("删除分类");
            return productCategoryMapper.deleteCategory(id);
        } catch (Exception e) {
            log.error("删除分类失败");
            throw e;
        }
    }

}

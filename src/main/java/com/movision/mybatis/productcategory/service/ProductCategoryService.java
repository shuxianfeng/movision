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

}

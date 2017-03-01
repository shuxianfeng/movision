package com.movision.facade.boss;

import com.movision.mybatis.productcategory.entity.ProductCategory;
import com.movision.mybatis.productcategory.service.ProductCategoryService;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/3/1 16:48
 */
@Service
public class ProductCategoryFacade {
    @Autowired
    private ProductCategoryService productCategoryService;
    private static Logger log = LoggerFactory.getLogger(ProductCategoryFacade.class);
    @Value("#{configProperties['img.domain']}")
    private String imgdomain;


    /**
     * 查询类别列表
     *
     * @param pager
     * @return
     */
    public List<ProductCategory> findAllCategory(Paging<ProductCategory> pager) {
        return productCategoryService.findAllCategory(pager);
    }

}

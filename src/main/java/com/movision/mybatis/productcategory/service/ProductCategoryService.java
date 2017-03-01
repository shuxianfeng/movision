package com.movision.mybatis.productcategory.service;

import com.movision.mybatis.brand.entity.Brand;
import com.movision.mybatis.goods.mapper.GoodsMapper;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscount;
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
     * 查询活动列表
     *
     * @param pager
     * @return
     */
    public List<GoodsDiscount> findAllGoodsDiscount(Paging<GoodsDiscount> pager) {
        try {
            log.info("查询分类列表");
            return productCategoryMapper.findAllGoodsDiscount(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询分类列表失败", e);
            throw e;
        }
    }
    /**
     * 查询品牌列表
     *
     * @param pager
     * @return
     */
    public List<Brand> findAllBrand(Paging<Brand> pager) {
        try {
            log.info("查询品牌列表");
            return productCategoryMapper.findAllBrand(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询品牌列表失败", e);
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
     * 活动搜索
     *
     * @param map
     * @param pager
     * @return
     */
    public List<GoodsDiscount> findAllGoodsDiscountCondition(Map map, Paging<GoodsDiscount> pager) {
        try {
            log.info("活动搜索");
            return productCategoryMapper.findAllGoodsDiscountCondition(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("活动搜索失败", e);
            throw e;
        }
    }

    /**
     * 品牌分类搜索
     *
     * @param map
     * @param pager
     * @return
     */
    public List<Brand> findAllBrandCondition(Map map, Paging<Brand> pager) {
        try {
            log.info("品牌分类搜索");
            return productCategoryMapper.findAllBrandCondition(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("品牌分类搜索失败", e);
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
            log.error("删除分类失败", e);
            throw e;
        }
    }

    /**
     * 添加商品分类
     *
     * @param productCategory
     * @return
     */
    public int addCategory(ProductCategory productCategory) {
        try {
            log.info("添加商品分类");
            return productCategoryMapper.addCategory(productCategory);
        } catch (Exception e) {
            log.error("添加商品分类失败", e);
            throw e;
        }
    }

    /**
     * 添加品牌
     *
     * @param brand
     * @return
     */
    public int addBrand(Brand brand) {
        try {
            log.info("添加添加品牌");
            return productCategoryMapper.addBrand(brand);
        } catch (Exception e) {
            log.error("添加添加品牌失败", e);
            throw e;
        }
    }

    /**
     * 添加活动
     *
     * @param goodsDiscount
     * @return
     */
    public int addGoodsDiscount(GoodsDiscount goodsDiscount) {
        try {
            log.info("添加活动");
            return productCategoryMapper.addGoodsDiscount(goodsDiscount);
        } catch (Exception e) {
            log.error("添加活动失败", e);
            throw e;
        }
    }
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public ProductCategory queryCategory(Integer id) {
        try {
            log.info("根据id查询");
            return productCategoryMapper.queryCategory(id);
        } catch (Exception e) {
            log.error("根据id查询失败", e);
            throw e;
        }
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public GoodsDiscount queryGoodsDiscount(Integer id) {
        try {
            log.info("根据id查询");
            return productCategoryMapper.queryGoodsDiscount(id);
        } catch (Exception e) {
            log.error("根据id查询失败", e);
            throw e;
        }
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public Brand queryBrand(Integer id) {
        try {
            log.info("根据id查询");
            return productCategoryMapper.queryBrand(id);
        } catch (Exception e) {
            log.error("根据id查询失败", e);
            throw e;
        }
    }

    /**
     * 编辑分类
     *
     * @param productCategory
     * @return
     */
    public int updateCategory(ProductCategory productCategory) {
        try {
            log.info("编辑分类");
            return productCategoryMapper.updateCategory(productCategory);
        } catch (Exception e) {
            log.error("编辑分类失败", e);
            throw e;
        }
    }

    /**
     * 编辑活动
     *
     * @param goodsDiscount
     * @return
     */
    public int updateDiscount(GoodsDiscount goodsDiscount) {
        try {
            log.info("编辑活动");
            return productCategoryMapper.updateDiscount(goodsDiscount);
        } catch (Exception e) {
            log.error("编辑活动失败", e);
            throw e;
        }
    }

    /**
     * 编辑品牌
     *
     * @param brand
     * @return
     */
    public int updateBrand(Brand brand) {
        try {
            log.info("编辑品牌");
            return productCategoryMapper.updateBrand(brand);
        } catch (Exception e) {
            log.error("编辑品牌失败", e);
            throw e;
        }
    }
    /**
     * 停用
     *
     * @param id
     * @return
     */
    public int updateDown(Integer id) {
        try {
            log.info("停用");
            return productCategoryMapper.updateStop(id);
        } catch (Exception e) {
            log.error("停用失败", e);
            throw e;
        }
    }

    /**
     * 启用
     *
     * @param id
     * @return
     */
    public int updateUp(Integer id) {
        try {
            log.info("启用");
            return productCategoryMapper.updateUp(id);
        } catch (Exception e) {
            log.error("启用失败", e);
            throw e;
        }
    }

    /**
     * 活动停用
     *
     * @param id
     * @return
     */
    public int updateDownD(Integer id) {
        try {
            log.info("活动停用");
            return productCategoryMapper.updateDownD(id);
        } catch (Exception e) {
            log.error("活动停用失败", e);
            throw e;
        }
    }

    /**
     * 活动启用
     *
     * @param id
     * @return
     */
    public int updateUpD(Integer id) {
        try {
            log.info("活动启用");
            return productCategoryMapper.updateUpD(id);
        } catch (Exception e) {
            log.error("活动启用失败", e);
            throw e;
        }
    }
}

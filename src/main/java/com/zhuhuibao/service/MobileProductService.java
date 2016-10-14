package com.zhuhuibao.service;

import java.util.*;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.mybatis.product.entity.ProductParam;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.product.mapper.ProductMapper;
import com.zhuhuibao.mybatis.product.service.ProductParamService;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 产品相关接口实现类
 *
 * @author liyang
 * @date 2016年10月13日
 */
@Service
@Transactional
public class MobileProductService {

    private static final Logger log = LoggerFactory.getLogger(MobileProductService.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductParamService paramService;

    /**
     * 根据品牌id查询该品牌下共有多少产品分类
     * 
     * @param brandId
     *            品牌id
     * @return
     */
    public List findSubSystemByBrand(String brandId) {
        return productService.findSubSystem(brandId);
    }

    /**
     * 根据品牌id和分类获取下面的所有的产品
     * 
     * @param brandId
     *            品牌id
     * @param scateId
     *            二级系统分类id
     * @return
     */
    public List<ProductWithBLOBs> findProductByBrandAndSubSystem(String brandId, String scateId) {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("scateid", scateId);
        productMap.put("brandid", brandId);
        productMap.put("status", Constants.product_status_publish);
        // TODO 展示产品个数待定
        productMap.put("count", 4);
        productMap.put("order", "publishTime");
        return productMapper.queryProductInfoBySCategory(productMap);
    }

    /**
     * 根据品牌id和分类获取下面的所有的产品分页信息
     *
     * @param brandId
     *            品牌id
     * @param scateId
     *            二级系统分类id
     * @return
     */
    public List<ProductWithBLOBs> findProductByBrandAndSubSystemPages(String brandId, String scateId, Paging<Map> pager) {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("scateid", scateId);
        productMap.put("brandid", brandId);
        productMap.put("status", Constants.product_status_publish);
        productMap.put("order", "publishTime");
        return productMapper.queryProductInfoBySCategoryPages(pager.getRowBounds(), productMap);
    }

    /**
     * 获取产品详情信息
     * 
     * @param id
     *            产品主键id
     * @return
     */
    public ProductWithBLOBs queryProductById(Long id) {
        ProductWithBLOBs product = productMapper.selectByPrimaryKey(id);
        if (product != null && product.getParamIDs() != null && product.getParamIDs().length() > 0) {
            List<ProductParam> params = new ArrayList<>();
            String iDs = product.getParamIDs();
            String paramValues = product.getParamValues();
            Map<String, String> paramMap = new TreeMap<>();
            if (iDs.indexOf(",") > 0) {
                String[] arr_param = iDs.split(",");
                String[] arr_paramValues = paramValues.split(",");
                for (int i = 0; i < arr_param.length; i++) {
                    ProductParam pp = paramService.queryParamById(Long.parseLong(arr_param[i]));
                    if (pp != null) {
                        pp.setPvalue(arr_paramValues[i]);
                        params.add(pp);
                    }
                }
            } else {
                ProductParam pp = paramService.queryParamById(Long.parseLong(iDs));
                if (pp != null) {
                    paramMap.put("pvalue", paramValues);
                    pp.setPvalue(paramValues);
                    params.add(pp);
                }
            }
            product.setParams(params);
        }
        return product;
    }

    /**
     * 查询产品参数
     * 
     * @param id
     *            产品主键id
     * @return
     */
    public Map<String, Object> queryPrdDescParam(Long id) {
        return productService.queryPrdDescParamService(id);
    }
}

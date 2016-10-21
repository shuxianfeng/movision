package com.zhuhuibao.service;

import java.util.*;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.fsearch.pojo.spec.ProductSearchSpec;
import com.zhuhuibao.fsearch.service.exception.ServiceException;
import com.zhuhuibao.fsearch.service.impl.ProductsService;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.entity.ProductParam;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.product.mapper.ProductMapper;
import com.zhuhuibao.mybatis.product.service.ProductParamService;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.utils.MapUtil;
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
    private ProductsService productsService;

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

    /**
     * 根据条件查询产品信息
     *
     * @param memberId
     * @param fcateid
     * @param scateid
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<Product> getProductList(Long memberId, Integer fcateid, Integer scateid, Integer status, String pageNo, String pageSize) {
        Paging<Product> productPaging = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        ProductWithBLOBs product = new ProductWithBLOBs();
        product.setCreateid(memberId);
        product.setScateid(scateid);
        product.setFcateid(fcateid);
        product.setStatus(status);
        List<Product> productList = productService.findAllByPager(productPaging, product);
        productPaging.result(productList);

        return productPaging;
    }

    /**
     * 根据ID更新产品状态
     *
     * @param memberId
     * @param productId
     * @param status
     */
    public void updateProductStatus(Long memberId, Long productId, Integer status) {
        ProductWithBLOBs product = new ProductWithBLOBs();

        product.setCreateid(memberId);
        product.setId(productId);
        product.setStatus(status);

        productMapper.updateProductStatus(product);
    }

    /**
     * 查询登录会员的产品详情
     *
     * @param productId
     * @return
     */
    public ProductWithBLOBs getMemberProductById(Long productId, Long memberId) {
        Map<String, Object> param = MapUtil.convert2HashMap("id", productId, "memberId", memberId);
        ProductWithBLOBs product = productMapper.selectByIdMemberId(param);
        productService.deailProductParam(product);

        return product;
    }

    /**
     * 检索产品信息
     *
     * @param spec
     * @return
     */
    public Map<String, Object> selProductList(ProductSearchSpec spec) throws ServiceException {
        if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
            spec.setLimit(12);
        }
        return productsService.search(spec);

    }

    /**
     * 根据产品名称获取供应商下面的所有产品信息
     *
     * @param fcateid
     * @param id
     * @param pager
     * @return
     */
    public List<Map<String, String>> findCompanyProductListByName(String fcateid, String id, String name, Paging<Map<String, String>> pager) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("status", "1");
        queryMap.put("createid", id);
        queryMap.put("fcateid", fcateid);
        queryMap.put("name", name);
        return productMapper.findCompanyProductListByName(pager.getRowBounds(), queryMap);
    }
}

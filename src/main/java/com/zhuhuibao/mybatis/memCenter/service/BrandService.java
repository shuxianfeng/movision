package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.BrandBean;
import com.zhuhuibao.common.pojo.BrandDetailBean;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.pojo.SuggestBrand;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品牌业务处理
 * Created by cxx on 2016/3/23 0023.
 */
@Service
@Transactional
public class BrandService {
    private static final Logger log = LoggerFactory.getLogger(BrandService.class);

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    ProductService productService;

    /**
     * 根据会员id，状态status(可以为空)查询品牌
     */
    public List<Brand> searchBrandByStatus(Brand brand) {
        try {
            return brandMapper.searchBrandByStatus(brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public List<Brand> searchBrand(Brand brand) {
        try {
            return brandMapper.searchBrand(brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public List<Brand> searchBrandByPager(Paging<Brand> pager, Brand brand) {
        try {
            return brandMapper.findAllByPager(pager.getRowBounds(), brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 查询拥有产品的品牌
     */
    public List<BrandBean> searchAll() {
        try {
            return brandMapper.searchAll();
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 查询拥有产品的推荐品牌
     */
    public List<SuggestBrand> SuggestBrand() {
        try {
            return brandMapper.SuggestBrand();
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 查询二级系统下所有品牌,返回id,品牌logo
     */
    public List<ResultBean> findAllBrand(Product product) {
        try {
            return brandMapper.findAllBrand(product);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 查询二级系统下所有品牌，返回id，name
     */
    public List<ResultBean> findBrandByScateid(Product product) {
        try {
            return brandMapper.findBrandByScateid(product);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 查询推荐品牌
     */
    public List<ResultBean> searchSuggestBrand() {
        try {
            return brandMapper.searchSuggestBrand();
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 查询品牌详情
     */
    public BrandDetailBean details(String id) {
        try {
            return brandMapper.details(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 新建品牌
     */
    public int addBrand(Brand brand) {
        try {
            return brandMapper.addBrand(brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 更新品牌
     */
    public int updateBrand(Brand brand) {
        try {
            return brandMapper.updateBrand(brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 删除品牌
     */
    public int deleteBrand(String id) {
        try {
            return brandMapper.deleteBrand(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 品牌数量
     */
    public int searchBrandSize(Brand brand) {
        try {
            return brandMapper.searchBrandSize(brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 品牌数量
     */
    public int findBrandSize(Brand brand) {
        try {
            return brandMapper.findBrandSize(brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 查询品牌详情
     */
    public Brand brandDetails(String id) {
        try {
            return brandMapper.brandDetails(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 会员中心首页获得产品相关信息 品牌数量，在线产品数量，关联的代理商
     *
     * @param createId
     * @return
     */
    public Map<String, Object> queryBrandProductAgentCount(Long createId) {
        log.info("query brand product agent count");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Map<String, Object>> mapList = brandMapper.queryBrandProductAgentCount(createId);
            Map<String, Object> map1 = mapList.get(0);
            resultMap.put("brandCount", map1.get("count"));
            Map<String, Object> map2 = mapList.get(1);
            resultMap.put("productCount", map2.get("count"));
            Map<String, Object> map3 = mapList.get(2);
            resultMap.put("agentCount", map3.get("count"));
        } catch (Exception e) {
            log.error("query brand product agent count error!", e);
            throw new BusinessException(MsgCodeConstant.mcode_common_failure, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure)));
        }
        return resultMap;
    }


    public List<Map<String, String>> queryRecommendBrand(Map<String, Object> map) {
        List<Map<String, String>> list;
        try {
            list = brandMapper.queryRecommendBrand(map);
            for (Map<String, String> item : list) {
                List<String> scateIds = productService.findScateIdByBrandId(item.get("id"));
                if (scateIds.size() > 0) {
                    item.put("scateid", StringUtils.isEmpty(scateIds.get(0)) ? "" : scateIds.get(0));
                } else {
                    item.put("scateid", "");
                }

            }
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
        return list;
    }
}

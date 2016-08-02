package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.BrandBean;
import com.zhuhuibao.common.pojo.BrandDetailBean;
import com.zhuhuibao.common.pojo.SuggestBrand;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.SysBrand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.SysBrandMapper;
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

    @Autowired
    SysBrandMapper sysBrandMapper;

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

    public List<Brand> searchBrandByPager(Paging<Brand> pager, Brand brand) {
        try {
            return brandMapper.findAllByPager(pager.getRowBounds(), brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public List<BrandBean> searchAll() {
        try {
            return brandMapper.searchAll();
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public List<SuggestBrand> SuggestBrand() {
        try {
            return brandMapper.SuggestBrand();
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public List<Map<String,Object>> findAllBrand(String scateid) {
        try {
            return brandMapper.findAllBrand(scateid);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public List<Map<String,Object>> findBrandByScateid(String scateid) {
        try {
            return brandMapper.findBrandByScateid(scateid);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public List<Map<String,Object>> searchSuggestBrand() {
        try {
            return brandMapper.searchSuggestBrand();
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public BrandDetailBean details(String id) {
        try {
            return brandMapper.details(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public int addBrand(Brand brand) {
        try {
            brandMapper.addBrand(brand);
            return brand.getId();
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public int updateBrand(Brand brand) {
        try {
            return brandMapper.updateBrand(brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public int deleteBrand(String id) {
        try {
            return brandMapper.deleteBrand(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public int searchBrandSize(Brand brand) {
        try {
            return brandMapper.searchBrandSize(brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public int findBrandSize(Brand brand) {
        try {
            return brandMapper.findBrandSize(brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

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

    public int addSysBrand(SysBrand sysBrand) {
        try {
            return sysBrandMapper.insertSelective(sysBrand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public int deleteBrandSysByBrandID(Integer id) {
        try {
            return sysBrandMapper.deleteBrandSysByBrandID(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }
}

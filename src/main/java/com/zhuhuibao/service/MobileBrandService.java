package com.zhuhuibao.service;

import com.zhuhuibao.common.pojo.*;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.CheckBrand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.memCenter.service.CheckBrandService;
import com.zhuhuibao.mybatis.oms.service.CategoryService;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品牌管理相关接口实现类
 *
 * @author liyang
 * @date 2016年10月13日
 */
@Service
@Transactional
public class MobileBrandService {

    private static final Logger log = LoggerFactory.getLogger(MobileBrandService.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CheckBrandService checkBrandService;

    /**
     * 查询对应类别品牌
     *
     * @return
     */
    public List selHotBrandListByType(String parentId, String subTypeId, Paging<Map> pager) {
        Map queryMap = new HashMap();
        queryMap.put("parentId", parentId);
        queryMap.put("subTypeId", subTypeId);
        List<SuggestBrand> list = brandMapper.findAllHotBrandListByType(pager.getRowBounds(), queryMap);
        if (null == subTypeId || (null != subTypeId & subTypeId.equals(""))) {
            for (SuggestBrand suggestBrand : list) {
                suggestBrand.setScateid(this.findScateIdByBrandId(suggestBrand.getId()).get(0));
            }
        }
        return list;
    }

    /**
     * 品牌馆展示所有类别下面的品牌
     *
     * @return
     */
    public Object selHotBrandList() {
        List<ResultBean> sysList = categoryService.findSystemList();
        List<SysBean> allList = categoryService.searchAll();
        List<BrandBean> brandList = brandService.searchAll();
        List list = new ArrayList();
        for (ResultBean r : sysList) {
            List list1 = new ArrayList();
            Map map1 = new HashMap();
            map1.put("id", r.getCode());
            map1.put("name", r.getName());
            map1.put("icon", r.getSmallIcon());
            for (SysBean sb : allList) {
                Map map2 = new HashMap();
                if (r.getCode().equals(sb.getId())) {
                    map2.put("id", sb.getCode());
                    map2.put("name", sb.getSubSystemName());
                    List list3 = new ArrayList();
                    for (BrandBean brand : brandList) {
                        Map map3 = new HashMap();
                        if (sb.getCode().equals(brand.getScateid())) {
                            map3.put("id", brand.getId());
                            map3.put("name", brand.getBrandCNName());
                            list3.add(map3);
                        }
                    }
                    map2.put("brand", list3);
                    list1.add(map2);
                }
            }
            map1.put("subSystem", list1);
            list.add(map1);
        }
        return list;
    }

    /**
     * 品牌详情页
     *
     * @param id
     *            品牌主键id
     * @param scateId
     *            品牌所属类别id
     * @return
     */
    public Map selBrandInfo(String id, String scateId) {
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        Map map3 = new HashMap();
        // 品牌详情
        BrandDetailBean brand = brandService.details(id);
        // 计算点击量
        Brand brand1 = brandService.brandDetails(id);
        if (brand1.getViews() == null) {
            brand1.setViews(1);
        } else {
            brand1.setViews(brand1.getViews() + 1);
        }
        brandService.updateBrand(brand1);
        // 品牌描述
        map2.put("company", brand.getCompany());
        map2.put("webSite", brand.getWebSite());
        map2.put("phone", brand.getPhone());
        map2.put("address", brand.getAddress());
        map2.put("introduce", brand.getBrandDesc());
        map2.put("imgUrl", brand.getImgUrl());
        map2.put("logo", brand.getLogo());

        ResultBean result = categoryService.querySystem(scateId);
        // 导航信息
        map3.put("brandid", id);
        map3.put("brandName", brand.getCnName());
        map2.put("brandName", brand.getCnName());
        map3.put("scateid", scateId);
        String scateName = null == result ? "" : result.getName();
        map3.put("scateName", scateName);
        // 暂时没明白这边代码的意思
        ResultBean result1 = result == null ? null : categoryService.querySystem(result.getSmallIcon());
        if (null != result) {
            map3.put("fcateid", result1.getCode());
            map3.put("fcateName", result1.getName());
        } else {
            map3.put("fcateid", "");
            map3.put("fcateName", "");
        }
        map1.put("brandDesc", map2);
        map1.put("navigation", map3);
        return map1;
    }

    /**
     * 查询我的品牌(审核信息）列表
     * 
     * @param memberId
     * @param status
     * @param publishTimeOrder
     * @return
     */
    public List<Map<String, Object>> getMyBrandChkList(Long memberId, String status, String publishTimeOrder) {
        Map<String, Object> params = MapUtil.convert2HashMap("createid", memberId, "status", status, "publishTimeOrder", publishTimeOrder);

        return checkBrandService.searchMyBrand(params);
    }

    /**
     * 根据ID查询品牌（审核信息）详情
     * 
     * @param brandId
     * @return
     */
    public CheckBrand getBrandChkById(String brandId) {
        return checkBrandService.queryBrandById(brandId);
    }

    /**
     * 根据ID查询品牌分类信息
     * 
     * @param brandId
     * @return
     */
    public List<Map<String, Object>> getBrandSysChkById(String brandId) {
        return brandService.queryBrandSysById(brandId);
    }

    /**
     * 根据品牌id获取品牌所属2级分类信息
     * 
     * @param brandId
     * @return
     */
    public List<String> findScateIdByBrandId(String brandId) {
        return brandService.findScateIdByBrandId(brandId);
    }
}

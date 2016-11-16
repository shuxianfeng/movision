package com.zhuhuibao.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.expert.entity.Achievement;
import com.zhuhuibao.mybatis.expert.entity.Dynamic;
import com.zhuhuibao.mybatis.expert.mapper.AchievementMapper;
import com.zhuhuibao.mybatis.expert.mapper.DynamicMapper;
import com.zhuhuibao.mybatis.expert.mapper.ExpertMapper;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.service.payment.PaymentService;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 触屏端筑慧专家service
 *
 * @author changxinwei
 * @date 2016年11月16日
 */
@Service
@Transactional
public class MobileExpertService {

    private static final Logger log = LoggerFactory.getLogger(ExpertService.class);

    @Autowired
    private ExpertMapper expertMapper;

    @Autowired
    private ExpertService expertService;

    @Autowired
    private AchievementMapper achievementMapper;

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    PaymentService paymentService;

    /**
     * 专家信息
     *
     * @param pager
     * @param map
     * @return 专家信息
     */
    public List<Map<String, String>> findAllMyLookedMobileExpertList(Paging<Map<String, String>> pager, Map<String, Object> map) {
        return expertMapper.findAllMyLookedMobileExpertList(pager.getRowBounds(), map);
    }

    /***
     * 删除专家信息
     *
     * @param id 专家信息
     * @return 删除记录
     */
    public int deleteLookedExpert(String id) {
        return expertService.deleteLookedExpert(id);
    }


    /**
     * 专家技术成果
     *
     * @param pager
     * @param map
     * @return
     */

    public List<Map<String, String>> findAllMyLookedAchievementList(Paging<Map<String, String>> pager, Map<String, Object> map) {
        return achievementMapper.findAllMyLookedMobileAchievementList(pager.getRowBounds(), map);
    }


    /**
     * 删除查看专家成果
     *
     * @param id 专家成果Id
     * @return 删除条数
     */
    public int deleteLookedAchievement(String id) {
        return expertService.deleteLookedAchievement(id);
    }


    /***
     * 协会列表
     *
     * @param map 查询参数
     * @return 协会详情
     */
    public List<Dynamic> findAllDynamicList(Paging<Dynamic> pager, Map<String, Object> map) {
        return dynamicMapper.findAllDynamicList(pager.getRowBounds(), map);

    }


    /**
     * 删除协会动态
     *
     * @param dynamic 协会
     * @return 协会条数
     */

    public int updateDynamic(Dynamic dynamic) {
        return expertService.updateDynamic(dynamic);
    }


    /**
     * 协会动态信息
     *
     * @param id
     * @return
     */
    public Dynamic queryDynamicById(String id) {
        return expertService.queryDynamicById(id);
    }


    /**
     * 我的技术成果
     *
     * @param map 查询条件
     * @return 技术成果
     */
    public List<Map<String, String>> findAllAchievementList(Paging<Map<String, String>> pager, Map<String, Object> map) {
        return achievementMapper.findAllMyLookedAchievementList(pager.getRowBounds(), map);
    }


    /***
     * 删除技术成果
     *
     * @param achievement 删除Id
     */
    public int updateAchievement(Achievement achievement) {
        return expertService.updateAchievement(achievement);
    }

    /**
     * 技术成果详情
     *
     * @param id 技术成果id
     * @return 成果详情
     */
    public Map<String, String> queryAchievementById(String id) {
        return expertService.queryAchievementById(id);
    }


    /**
     * 专家信息详情,技术成果信息
     *
     * @param goodsID 商品的Id
     * @param type    筑慧币
     * @return
     */
    public Response viewGoodsRecord(String goodsID, String type) throws Exception {
        return paymentService.viewGoodsRecord(Long.parseLong(goodsID), type);
    }


}
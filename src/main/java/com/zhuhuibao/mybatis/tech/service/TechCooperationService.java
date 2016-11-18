package com.zhuhuibao.mybatis.tech.service;

import com.zhuhuibao.common.constant.MessageTextConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.mybatis.tech.entity.TechCooperation;
import com.zhuhuibao.mybatis.tech.mapper.TechCooperationMapper;
import com.zhuhuibao.mybatis.tech.mapper.TechDataMapper;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 技术频道业务处理类
 *
 * @author pl
 * @create 2016/5/27 0027
 **/
@Service
@Transactional
public class TechCooperationService {

    private final static Logger log = LoggerFactory.getLogger("TechService");

    @Autowired
    TechCooperationMapper techMapper;

    @Autowired
    TechDataMapper techDataMapper;

    @Autowired
    TechDataService tdService;

    @Autowired
    ZhbService zhbService;

    @Autowired
    SiteMailService siteMailService;

    /**
     * 插入技术成果或者技术需求
     *
     * @param tech
     * @return
     */
    public int insertTechCooperation(TechCooperation tech) throws Exception {
        int result = 0;
        log.info("insert tech cooperation info " + StringUtils.beanToString(tech));
        try {//2-技术需求 发布需要筑慧币
            String cooperation = tech.getCooperation();
            if (StringUtils.isEmpty(cooperation)) {
                tech.setCooperation("面议");
            }

            String type = String.valueOf(tech.getType());
            if (type.equals("2")) {
                boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.FBJSXQ.toString());
                if (bool) {
                    result = techMapper.insertSelective(tech);
                    zhbService.payForGoods(tech.getId(), ZhbPaymentConstant.goodsType.FBJSXQ.toString());
                } else {//支付失败稍后重试，联系客服
                    throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
                }
            } else {//1-技术成果
                result = techMapper.insertSelective(tech);
            }
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
        return result;
    }

    /**
     * 技术培训频道技术合作搜索
     *
     * @param condition 搜索条件
     * @return
     */
    public List<Map<String, String>> findAllTechCooperationPager(Paging<Map<String, String>> pager, Map<String, Object> condition) {
        log.info("find all tech cooperation for pager " + StringUtils.mapToString(condition));
        List<Map<String, String>> techList;
        try {
            techList = techMapper.findAllTechCooperationPager(pager.getRowBounds(), condition);
        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw e;
        }
        return techList;
    }

    /**
     * 运营管理平台技术合作搜索
     *
     * @param condition 搜索条件
     * @return
     */
    public List<Map<String, String>> findAllOMSTechCooperationPager(Paging<Map<String, String>> pager, Map<String, Object> condition) {
        log.info("find all OMS tech cooperation for pager " + StringUtils.mapToString(condition));
        List<Map<String, String>> techList;
        try {
            techList = techMapper.findAllOMSTechCooperationPager(pager.getRowBounds(), condition);
        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw e;
        }
        return techList;
    }

    /**
     * 更新技术合作:成果和需求
     *
     * @param tech
     * @return
     */
    public int updateTechCooperation(TechCooperation tech) throws Exception {
        int result;
        log.info("update oms tech cooperation " + StringUtils.beanToString(tech));
        try {
            String cooperation = tech.getCooperation();
            if(StringUtils.isEmpty(cooperation)){
               tech.setCooperation("面议");
            }
            tech.setUpdateTime(DateUtils.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
            result = techMapper.updateByPrimaryKeySelective(tech);
            if ("3".equals(String.valueOf(tech.getStatus()))) {
            	
            	String source = tech.getType() == 1 ? MessageTextConstant.ACHIEVEMENT : MessageTextConstant.JSXQ;
                siteMailService.addRefuseReasonMail(ShiroUtil.getOmsCreateID(), tech.getCreateID(),
                		tech.getReason(), source, tech.getTitle(), String.valueOf(tech.getId()));
            }
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.mcode_common_failure, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure)));
        }
        return result;
    }

    /**
     * 注销技术合作:成果和需求
     *
     * @param condition
     * @return
     */
    public int deleteTechCooperation(Map<String, Object> condition) {
        int result;
        log.info("delete oms tech cooperation " + StringUtils.mapToString(condition));
        try {
            result = techMapper.deleteTechCooperation(condition);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
        return result;
    }

    /**
     * 更新技术合作的点击率
     *
     * @param techCoopId 技术合作ID
     * @return
     */
    public int updateTechCooperationViews(String techCoopId) {
        int result;
        log.info("update tech cooperation views " + techCoopId);
        try {
            result = techMapper.updateTechCooperationViews(Long.valueOf(techCoopId));
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
        return result;
    }

    /**
     * 根据ID查看技术合作详情
     *
     * @param id 技术合作ID
     * @return
     */
    public TechCooperation selectTechCooperationById(String id) {
        TechCooperation techCoop;
        log.info("select tech cooperation by id " + id);
        try {
            techCoop = techMapper.selectByPrimaryKey(Long.valueOf(id));
        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw e;
        }
        return techCoop;
    }

    /**
     * 预览技术合作详情
     *
     * @param condition 技术合作ID
     * @return
     */
    public Map<String, Object> previewTechCooperationDetail(Map<String, Object> condition) {
        Map<String, Object> techCoop;
        log.info("preview tech cooperation by id " + StringUtils.mapToString(condition));
        try {
            techCoop = techMapper.previewTechCooperationDetail(condition);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
        return techCoop;
    }

    /**
     * 未登录预览技术合作详情
     *
     * @param id 技术合作ID
     * @return
     */
    public Map<String, Object> previewUnloginTechCoopDetail(String id) {
        Map<String, Object> techCoop;
        log.info("preview unlogin tech cooperation " + id);
        try {
            techCoop = techMapper.previewUnloginTechCoopDetail(Long.valueOf(id));
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
        return techCoop;
    }

    /**
     * 会员中心编辑技术合作
     *
     * @param id 技术合作ID
     * @return
     */
    public Map<String, String> selectMcCoopDetail(String id) {
        Map<String, String> techCoop;
        log.info("preview mc tech cooperation by id " + id);
        try {
            techCoop = techMapper.selectMcCoopDetail(Long.valueOf(id));
        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw e;
        }
        return techCoop;
    }

    /**
     * 查询技术合作的点击排行
     *
     * @param map
     * @return
     */
    public List<Map<String, String>> findCoopViewsOrder(Map<String, Object> map) {
        log.info("find views order " + StringUtils.mapToString(map));
        List<Map<String, String>> dataList;
        try {
            dataList = techMapper.findCoopViewsOrder(map);
        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw e;
        }
        return dataList;
    }

    /**
     * 查询技术频道首页技术合作：技术成果，技术需求
     *
     * @param map
     * @return
     */
    public List<Map<String, String>> findIndexTechCooperation(Map<String, Object> map) {
        log.info("find home page tech cooperation " + StringUtils.mapToString(map));
        List<Map<String, String>> coopList;
        try {
            coopList = techMapper.findIndexTechCooperation(map);
        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw e;
        }
        return coopList;
    }

    public List<Map<String,String>> findAllMyLookedAchievementList(Paging<Map<String, String>> pager, Map<String, Object> map) {
        try {
            return techMapper.findAllMyLookedAchievementList(pager.getRowBounds(),map);
        } catch (Exception e) {
            log.error("TechCooperationService::findAllMyLookedAchievementList",e);
            throw e;
        }
    }

    public int deleteLookedAchievement(String id) {
        try {
            return techMapper.deleteLookedAchievement(id);
        } catch (Exception e) {
            log.error("TechCooperationService::deleteLookedAchievement::id=="+id,e);
            throw e;
        }
    }

    public List<Map<String,String>> findAllTechByCompanyId(Paging<Map<String, String>> pager, Map<String, Object> map) {
        try {
            return techMapper.findAllTechByCompanyId(pager.getRowBounds(),map);
        } catch (Exception e) {
            log.error("TechCooperationService::findAllTechByCompanyId",e);
            throw e;
        }
    }
}

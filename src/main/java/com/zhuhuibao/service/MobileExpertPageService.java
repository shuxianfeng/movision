package com.zhuhuibao.service;

import com.taobao.api.ApiException;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.util.ConvertUtil;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.expert.entity.Achievement;
import com.zhuhuibao.mybatis.expert.entity.Dynamic;
import com.zhuhuibao.mybatis.expert.entity.Expert;
import com.zhuhuibao.mybatis.expert.entity.ExpertSupport;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.tech.service.PublishTCourseService;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.service.payment.PaymentService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专家频道相关业务处理
 *
 * @author changxinwei
 * @date 2016年11月22日
 */


@Service
@Transactional
public class MobileExpertPageService {


    @Autowired
    ExpertService expertService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    PublishTCourseService ptCourseService;

    @Autowired
    MemberService memberService;

    @Autowired
    ZhbService zhbService;


    /**
     * 最新入驻的专家
     *
     * @return 专家信息
     */
    public List<Expert> findNewSettledExpertList(int count) {
        List<Expert> expertList = expertService.queryLatestExpert(count);
        List list = new ArrayList();
        for (Expert expert : expertList) {
            Map expertMap = new HashMap();
            expertMap.put("createid", expert.getCreateId());
            expertMap.put("id", expert.getId());
            expertMap.put("name", expert.getName());
            expertMap.put("company", expert.getCompany());
            expertMap.put("position", expert.getPosition());
            expertMap.put("photo", expert.getPhotoUrl());
            expertMap.put("hot", expert.getViews());
            expertMap.put("introduce", expert.getIntroduce());
            list.add(expertMap);
        }
        return list;
    }


    /**
     * 首页专家培训
     *
     * @return 专家培训信息
     */
    public List<Map<String, Object>> findExpertTrainList(int count) {
        return  null;
    }


    /**
     * 最新技术成果
     *
     * @return 最新技术成果信息
     */
    public List<Map<String, String>> findNewAchievementList(int count) {
        return expertService.findAchievementListByCount(count);
    }


    /**
     * 最新技术成果
     *
     * @return 最新技术成果信息
     */
    public List<Map<String, String>> findNewDynamicList(int count) {
        return expertService.findDynamicListByCount(count);
    }


    /**
     * 专家列表
     *
     * @param pager 分页
     * @param map   查询参数
     * @return
     */
    public List<Map<String, Object>> findAllExpert(Paging<Map<String, Object>> pager, Map<String, Object> map) {
        List<Map<String, Object>> expertList = expertService.findAllExpert(pager, map);
        for (Map<String, Object> expert : expertList) {
            String provinceCode = String.valueOf(expert.get("province"));
            if (!StringUtils.isEmpty(provinceCode)) {
                ConvertUtil.execute(expert, "province", "dictionaryService", "findProvinceByCode", new Object[]{provinceCode});
            } else {
                expert.put("provinceName", "");
            }
            String cityCode = String.valueOf(expert.get("city"));
            if (!StringUtils.isEmpty(cityCode)) {
                ConvertUtil.execute(expert, "city", "dictionaryService", "findCityByCode", new Object[]{cityCode});
            } else {
                expert.put("cityName", "");
            }
            String areaCode = String.valueOf(expert.get("area"));
            if (!StringUtils.isEmpty(areaCode)) {
                ConvertUtil.execute(expert, "area", "dictionaryService", "findAreaByCode", new Object[]{areaCode});
            } else {
                expert.put("areaName", "");
            }
        }
        return expertList;
    }


    /**
     * 培训列表
     *
     * @param pager
     * @param condition
     * @return
     */
    public List<Map<String, String>> findAllPublishCoursePager(Paging<Map<String, String>> pager, Map<String, Object> condition) {
        return ptCourseService.findAllPublishCoursePager(pager, condition);
    }


    /**
     * 培训详情页
     *
     * @param condition
     * @return
     */
    public Map<String, String> previewTrainCourseDetail(Map<String, Object> condition) {
        return ptCourseService.previewTrainCourseDetail(condition);
    }


    /**
     * 技术成果
     *
     * @param pager 分页
     * @param map   技术成果
     * @return
     */
    public List<Achievement> findAllAchievementList(Paging<Achievement> pager, Map<String, Object> map) {
        List<Achievement> achievementList = expertService.findAllAchievementList(pager, map);
        List list = new ArrayList<>();
        for (Achievement achievement : achievementList) {
            Map m = new HashMap();
            m.put("id", achievement.getId());
            m.put("title", achievement.getTitle());
            m.put("updateTime", achievement.getUpdateTime());
            list.add(m);
        }
        return list;
    }


    /**
     * 根据createid查询专家是否存在
     *
     * @param s
     * @return
     */
    public Expert queryExpertByCreateid(String s, Expert expert) {
        Expert expert1 = expertService.queryExpertByCreateid(s);
        if (expert1 == null) {
            expertService.applyExpert(expert);
        } else {
            throw new BusinessException(MsgCodeConstant.EXPERT_ISEXIST, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.EXPERT_ISEXIST)));
        }
        return expertService.queryExpertByCreateid(s);
    }


    /**
     * 申请专家支持
     *
     * @param mobile
     * @param mobileCodeSessionTypeSupport
     * @return
     */
    public String getTrainMobileCode(String mobile, String mobileCodeSessionTypeSupport, String imgCode, String sessImgCode) throws IOException, ApiException {

        if (imgCode.equalsIgnoreCase(sessImgCode)) {
            String verifyCode = expertService.getTrainMobileCode(mobile,mobileCodeSessionTypeSupport);
        } else {
            throw new BusinessException(MsgCodeConstant.validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.validate_error)));
        }
        return null;
    }


    /**
     * 申请专家支持
     *
     * @param code
     * @param mobile
     * @param type
     */
    public void checkMobileCode(String code, String mobile, String type, ExpertSupport expertSupport) throws Exception {
        Long createId = ShiroUtil.getCreateID();
        expertService.checkMobileCode(code, mobile, type);
        if (createId != null) {
            expertSupport.setCreateid(String.valueOf(createId));
            expertService.applyExpertSupport(expertSupport);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
    }

    /**
     *
     * 协会动态列表
     *
     * @param pager
     * @param map
     * @return
     */

    public List<Dynamic> findAllDynamicList(Paging<Dynamic> pager, Map<String, Object> map) {

        List<Dynamic> dynamicList=expertService.findAllDynamicList(pager,map);
        List list = new ArrayList();
        for (Dynamic dynamic : dynamicList) {
            Map m = new HashMap();
            m.put("id", dynamic.getId());
            m.put("title", dynamic.getTitle());
            m.put("updateTime", dynamic.getUpdateTime());
            list.add(m);
        }
        return dynamicList;
    }


    /**
     * 协会动态详情
     *
     * @param id
     * @return
     */
    public Dynamic queryDynamicById(String id) {
        Dynamic dynamic = expertService.queryDynamicById(id);
        if(dynamic!=null){
            dynamic.setViews(String.valueOf(Integer.parseInt(dynamic.getViews())+1));
            expertService.updateDynamicViews(dynamic);
        }else {
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR,"页面不存在");
        }
        return dynamic;
    }


    /**
     *
     * 给专家留言
     *
     * @param message
     */
    public void addMessage(Message message) {
          boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.GZJLY.toString());
            if(bool) {
                memberService.saveMessage(message);
            }else{//支付失败稍后重试，联系客服
                throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
            }
    }
}

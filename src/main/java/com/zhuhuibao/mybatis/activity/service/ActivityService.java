package com.zhuhuibao.mybatis.activity.service;

import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.activity.entity.Activity;
import com.zhuhuibao.mybatis.activity.entity.ActivityApply;
import com.zhuhuibao.mybatis.activity.mapper.ActivityApplyMapper;
import com.zhuhuibao.mybatis.activity.mapper.ActivityMapper;
import com.zhuhuibao.service.order.ZHOrderService;
import com.zhuhuibao.utils.IdGenerator;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
@Service
@Transactional
public class ActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private static final String PARTNER = AlipayPropertiesLoader.getPropertyValue("partner");

    @Autowired
    private ActivityApplyMapper activityApplyMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    ZHOrderService zhOrderService;

    public int addActivity(ActivityApply activityApply){
        try{
            return activityApplyMapper.insertSelective(activityApply);
        }catch (Exception e){
            log.error("ActivityService::addActivity", e);
            // e.printStackTrace();
            throw e;
        }
    }

    public Activity findByActivityId(String id) {
        Activity activity = null;
        try{
            activity = activityMapper.selectByPrimaryKey(Long.valueOf(id));
        } catch (Exception e){
            log.error("t_sys_activity 查询异常>>>",e);
        }
        return activity;
    }

    public Map<String, String> findByOrderNo(String orderNo) {
        Map<String, String> map = null;
        try{
            map = activityMapper.findByOrderNo(orderNo);
        } catch (Exception e){
            log.error("查询异常>>>",e);
        }
        return map;
    }


    /**
     * 报名活动
     * @param activityApply
     * @param mobileCode
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String applyActivity(ActivityApply activityApply, String mobileCode){
        String orderNo =  IdGenerator.createOrderNo();
        Map<String,String> msgParam = new HashMap<>();
        msgParam.put("orderNo",orderNo);
        msgParam.put("goodsId",activityApply.getActivityId());
        msgParam.put("partner",PARTNER);

        try{
            Subject currentUser = SecurityUtils.getSubject();
            Session sess = currentUser.getSession(true);
            String sessMobileCode = (String) sess.getAttribute("activity"+activityApply.getApplyMobile());
            if(mobileCode.equals(sessMobileCode)){

                activityApply.setOrderNo(orderNo);
                addActivity(activityApply);
            }else{
                throw new BusinessException(MsgCodeConstant.member_mcode_mobile_validate_error,
                        MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
            }

            zhOrderService.createActivityOrder(msgParam);
        }catch (Exception e){
            log.error("报名活动异常>>>" ,e);
            throw e;
        }

        return orderNo;
    }
}

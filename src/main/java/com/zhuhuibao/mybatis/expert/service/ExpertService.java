package com.zhuhuibao.mybatis.expert.service;

import com.google.gson.Gson;
import com.taobao.api.ApiException;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.expert.entity.*;
import com.zhuhuibao.mybatis.expert.mapper.*;
import com.zhuhuibao.mybatis.memberReg.entity.Validateinfo;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.utils.*;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.sms.SDKSendSms;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * 专家业务处理类
 * Created by cxx on 2016/5/17 0017.
 */
@Service
@Transactional
public class ExpertService {
    private static final Logger log = LoggerFactory.getLogger(ExpertService.class);

    @Autowired
    private AchievementMapper achievementMapper;

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private ExpertMapper expertMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private ExpertSupportMapper expertSupportMapper;

    @Autowired
    private MemberRegService memberRegService;

    /**
     * 发布技术成果
     * @param achievement
     * @return
     */
    public int publishAchievement(Achievement achievement)
    {
        try{
            return achievementMapper.publishAchievement(achievement);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 技术成果详情
     * @param id
     * @return
     */
    public Map<String,String> queryAchievementById(String id){
        try{
            return achievementMapper.queryAchievementById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 未登录技术成果详情
     * @param id
     * @return
     */
    public Map<String,String> queryUnloginAchievementById(String id){
        try{
            return achievementMapper.queryUnLoginAchievementById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 技术成果列表(分页)
     */
    public List<Achievement> findAllAchievementList(Paging<Achievement> pager,Map<String,Object> map){
        try{
            return achievementMapper.findAllAchievementList(pager.getRowBounds(),map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 技术成果列表
     */
    public List<Achievement> findAchievementList(Map<String,Object> map){
        try{
            return achievementMapper.findAllAchievementList(map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 技术成果列表(控制条数)
     */
    public List<Map<String,String>> findAchievementListByCount(int count){
        try{
            return achievementMapper.findAchievementListByCount(count);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 更新技术成果
     */
    public int updateAchievement(Achievement achievement){
        try{
            return achievementMapper.updateAchievement(achievement);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 发布协会动态
     * @param dynamic
     * @return
     */
    public int publishDynamic(Dynamic dynamic){
        try{
            return dynamicMapper.publishDynamic(dynamic);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 协会动态详情
     * @param id
     * @return
     */
    public Dynamic queryDynamicById(String id){
        try{
            return dynamicMapper.queryDynamicById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 更新协会动态
     */
    public int updateDynamic(Dynamic dynamic){
        try{
            return dynamicMapper.updateDynamic(dynamic);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 协会动态列表
     */
    public List<Dynamic> findAllDynamicList(Paging<Dynamic> pager,Map<String,Object> map){
        try{
            return dynamicMapper.findAllDynamicList(pager.getRowBounds(),map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 协会动态列表(控制条数)
     */
    public List<Map<String,String>> findDynamicListByCount(int count){
        try{
            return dynamicMapper.findDynamicListByCount(count);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 申请专家
     * @param expert
     * @return
     */
    public int applyExpert(Expert expert){
        try{
            return expertMapper.expertMapper(expert);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 专家详情
     * @param id
     * @return
     */
    public Expert queryExpertById(String id){
        try{
            return expertMapper.queryExpertById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据createid查询专家是否存在
     * @param createid
     * @return
     */
    public Expert queryExpertByCreateid(String createid){
        try{
            return expertMapper.queryExpertByCreateid(createid);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 更新专家信息
     * @param expert
     * @return
     */
    public int updateExpert(Expert expert){
        try{
            return expertMapper.updateExpert(expert);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 更新专家点击率
     * @param expert
     * @return
     */
    public int updateExpertViews(Expert expert){
        try{
            return expertMapper.updateExpertViews(expert);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 专家列表
     * @param pager,map
     * @return
     */
    public List<Expert> findAllExpertList(Paging<Expert> pager,Map<String,Object> map){
        try{
            return expertMapper.findAllExpertList(pager.getRowBounds(),map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 热门专家
     * @param count
     * @return
     */
    public List<Expert> queryHotExpert(int count){
        try{
            return expertMapper.queryHotExpert(count);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 最新专家
     * @param count
     * @return
     */
    public List<Expert> queryLatestExpert(int count){
        try{
            return expertMapper.queryLatestExpert(count);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 向专家咨询
     * @param question
     * @return
     */
    public int askExpert(Question question){
        try{
            return questionMapper.askExpert(question);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 查询等我回答的問題列表
     * @param pager
     * @return
     */
    public List<Map<String,String>> queryExpertQuestion(Paging<Map<String,String>> pager,Map<String, Object> map){
        try{
            return questionMapper.findAllExpertQuestion(pager.getRowBounds(),map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 立刻回答
     * @param answer
     * @return
     */
    public int answerQuestion(Answer answer){
        try{
            return answerMapper.answerQuestion(answer);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询我(專家)已經回答的問題列表
     * @param pager
     * @return
     */
    public List<Map<String,String>> queryMyAnswerQuestion(Paging<Map<String,String>> pager,Map<String, Object> map){
        try{
            return questionMapper.findAllMyAnswerQuestion(pager.getRowBounds(),map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询我提問的問題列表
     * @param pager
     * @return
     */
    public List<Map<String,String>> queryMyQuestion(Paging<Map<String,String>> pager,Map<String, Object> map){
        try{
            return questionMapper.findAllMyQuestion(pager.getRowBounds(),map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询我提問的一條問題及其回答內容
     * @param id
     * @return
     */
    public Map queryMyQuestionById(String id){
        try{
            //查詢問題信息和提問者的信息
            Map<String,String> question = questionMapper.queryMyQuestionById(id);
            //查詢該問題的回答信息以及回答者的信息
            List<Map<String,String>> answerList = answerMapper.queryAnswerByQuestionId(String.valueOf(question.get("id")));
            Map map = new HashMap();
            map.put("question",question);
            map.put("answerList",answerList);
            map.put("answerSize",answerList.size());
            return map;
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 專家互動(前台)
     * @param count
     * @return
     */
    public List<Map<String,String>> expertInteraction(int count){
        try{
            return questionMapper.expertInteraction(count);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 更新問題信息
     * @param question
     * @return
     */
    public int updateQuestionInfo(Question question){
        try{
            return questionMapper.updateQuestionInfo(question);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 申請專家支持
     * @param expertSupport
     * @return
     */
    public int applyExpertSupport(ExpertSupport expertSupport){
        try{
            return expertSupportMapper.applyExpertSupport(expertSupport);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 专家回答列表
     * @param pager
     * @return
     */
    public List<Map<String,String>> findAllExpertAnswerListOms(Paging<Map<String,String>> pager){
        try{
            return answerMapper.findAllExpertAnswerListOms(pager.getRowBounds());
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 更新答案信息
     * @param answer
     * @return
     */
    public int updateAnswerInfo(Answer answer){
        try{
            return answerMapper.updateAnswerInfo(answer);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询問題列表
     * @param pager
     * @return
     */
    public List<Map<String,String>> findAllQuestionListOms(Paging<Map<String,String>> pager){
        try{
            return questionMapper.findAllQuestionListOms(pager.getRowBounds());
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询問題列表
     * @param pager
     * @return
     */
    public List<Map<String,String>> findAllExpertSupportListOms(Paging<Map<String,String>> pager,Map<String,Object> map){
        try{
            return expertSupportMapper.findAllExpertSupportListOms(pager.getRowBounds(),map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 专家支持申请处理
     * @param expertSupport
     * @return
     */
    public int updateExpertSupport(ExpertSupport expertSupport){
        try{
            return expertSupportMapper.updateExpertSupport(expertSupport);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查看一条专家支持申请信息
     * @param id
     * @return
     */
    public Map<String,String> queryExpertSupportInfoById(String id){
        try{
            return expertSupportMapper.queryExpertSupportInfoById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 验证验证码是否正确
     * @param type
     * @return
     */
    public void checkMobileCode(String code,String mobile,String type) {
        if (code != null) {
            Validateinfo info = new Validateinfo();
            info.setAccount(mobile);
            info.setValid(0);
            info.setCheckCode(code);
            info = memberRegService.findMemberValidateInfo(info);
            Date currentTime = new Date();
            Date sendSMStime = DateUtils.date2Sub(DateUtils.str2Date(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"),12,10);

            //判断验证码是否过期
            if(currentTime.before(sendSMStime))
            {
                Subject currentUser = SecurityUtils.getSubject();
                Session session = currentUser.getSession(false);
                if (null != session) {
                    String verifyCode = (String) session.getAttribute(type + mobile);
                    //判断验证码是否正确
                    if (!code.equals(verifyCode)) {
                        throw new BusinessException(MsgCodeConstant.member_mcode_mobile_validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
                    }
                }else {
                    throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                }
            }else {
                throw new BusinessException(MsgCodeConstant.member_mcode_sms_timeout, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_sms_timeout)));
            }
        }else {
            throw new BusinessException(MsgCodeConstant.member_mcode_mobile_validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
        }
    }

    /**
     * 发送验证码
     * @return
     */
    public String getTrainMobileCode(String mobile,String type)throws IOException, ApiException {
        log.debug("获得手机验证码  mobile=="+mobile);
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(Constants.CHECK_MOBILE_CODE_SIZE,VerifyCodeUtils.VERIFY_CODES_DIGIT);
        log.debug("verifyCode == " + verifyCode);
        //发送验证码到手机
        Map<String,String> map = new LinkedHashMap<>();
        map.put("code",verifyCode);
        map.put("time",Constants.sms_time);
        Gson gson = new Gson();
        String params = gson.toJson(map);
        SDKSendSms.sendSMS(mobile, params, PropertiesUtils.getValue("zhuhuibao_check_mobile_template_code"));

        Validateinfo info = new Validateinfo();
        info.setCreateTime(DateUtils.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
        info.setCheckCode(verifyCode);
        info.setAccount(mobile);
        memberRegService.inserValidateInfo(info);
        sess.setAttribute(type+mobile, verifyCode);
        return verifyCode;
    }

}

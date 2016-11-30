package com.zhuhuibao.service.payment;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.payment.service.PaymentGoodsService;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.mybatis.tech.service.TechCooperationService;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.witkey.entity.Cooperation;
import com.zhuhuibao.mybatis.witkey.service.CooperationService;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.zhuhuibao.common.constant.ZhbPaymentConstant.goodsType.*;

/**
 * 平台服务使用筑慧币支付
 *
 * @author pl
 * @version 2016/6/16 0016
 */
@Service
@Transactional
public class PaymentService {

    private final static Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    PaymentGoodsService goodsService;

    @Autowired
    ResumeService resume;

    @Autowired
    ProjectService projectService;

    @Autowired
    ZhbService zhbService;
    // 威客
    @Autowired
    private CooperationService cooperationService;

    // 技术成果
    @Autowired
    TechCooperationService techService;

    @Autowired
    private ExpertService expertService;

    @Autowired
    private VipInfoService vipInfoService;

    /**
     * 查看收费商品信息
     *
     * @param goodsID
     *            商品ID
     * @return
     * @throws Exception
     */
    public Response viewGoodsRecord(Long goodsID, String type) throws Exception {
        Response response = new Response();
        Map<String, Object> dataMap = getChargeGoodsRecord(goodsID, type);
        response.setData(dataMap);
        return response;
    }


    /**
     * 获取收费商品信息
     * 
     * @param goodsID
     * @param type
     * @return
     * @throws Exception
     *
     */
    public Map<String, Object> getChargeGoodsRecord(Long goodsID, String type) throws Exception {
        Long createId = ShiroUtil.getCreateID();
        Long companyId = ShiroUtil.getCompanyID();
        Map<String, Object> dataMap = new HashMap<>();
        if (createId != null) {
            Map<String, Object> con = new HashMap<>();
            con.put("goodsId", goodsID);
            con.put("companyId", companyId);
            con.put("type", type);
            // 判断信息是否已经购买成功
            int viewNumber = goodsService.checkIsViewGoods(con);
            if (viewNumber == 0) {
                // 未购买，获取加密信息
                dataMap.putAll(getEncryptedGoodsRecord(goodsID, type));
                dataMap.put("payment", ZhbPaymentConstant.PAY_ZHB_NON_PURCHASE);

            } else {
                // 已购买，获取非加密信息
                dataMap.putAll(getPurchasedGoodsRecord(goodsID, type, companyId, viewNumber));
                dataMap.put("payment", ZhbPaymentConstant.PAY_ZHB_PURCHASE);
            }
        } else {
            // 未登录，查看加密信息
            dataMap.putAll(getEncryptedGoodsRecord(goodsID, type));
        }
        return dataMap;
    }

    /**
     * 获取已付费商品信息
     * 
     * @param goodsID
     * @param type
     * @return
     * @throws Exception
     */
    public Map<String, Object> getPurchasedGoodsRecord(Long goodsID, String type, Long companyId, int viewNumber) throws Exception {
        Map<String, Object> dataMap = new HashMap<>();
        // 查看下载简历
        if (CXXZJL.toString().equals(type)) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(goodsID));
            Resume resume2 = resume.previewResume(map);
            dataMap.put("info", resume2);
            Resume resumeBean = new Resume();
            resumeBean.setViews("1");
            resumeBean.setId(String.valueOf(goodsID));
            // 更新点击率
            resume.updateResume(resumeBean);
            Map<String, Object> map1 = new HashMap<>();
            map1.put("resumeID", goodsID);
            map1.put("companyID", companyId);
            map1.put("createId", resume2.getCreateid());
            // 谁查看过我的简历 同一个人可以查看相同简历多次。
            resume.addLookRecord(map1);
        } else if (CKXMXX.toString().equals(type))// 查看项目信息
        {
            Map<String, Object> map = projectService.queryProjectDetail(goodsID);
            // 记录我查看的项目信息

            dataMap.put("info", map);
        } else if (CKWKRW.toString().equals(type))// 查看威客任务
        {
            Map<String, Object> cooperation = cooperationService.queryCooperationInfoById(String.valueOf(goodsID));
            dataMap.put("info", cooperation);
            Cooperation result = new Cooperation();
            result.setId(cooperation.get("id").toString());
            result.setViews(String.valueOf(Integer.parseInt(cooperation.get("views").toString()) + 1));
            cooperationService.updateCooperationViews(result);
        } else if (CKJSCG.toString().equals(type))// 查看技术成果
        {
            Map<String, Object> techMap = new HashMap<>();
            techMap.put("id", goodsID);
            Map<String, Object> techCoop = techService.previewTechCooperationDetail(techMap);
            techService.updateTechCooperationViews(String.valueOf(goodsID));
            dataMap.put("info", techCoop);
        } else if (CKZJJSCG.toString().equals(type))// 查看专家技术成果
        {
            Map<String, String> map = expertService.queryAchievementById(String.valueOf(goodsID));
            dataMap.put("info", map);
        } else if (CKZJXX.toString().equals(type))// 查看专家信息
        {
            Map map = expertService.getExpertDetail(String.valueOf(goodsID), viewNumber);
            dataMap.put("info", map);
        }

        return dataMap;
    }

    /**
     * 获取加密商品信息
     * 
     * @param goodsID
     * @param type
     * @return
     * @throws Exception
     */
    private Map<String, Object> getEncryptedGoodsRecord(Long goodsID, String type) throws Exception {
        Map<String, Object> dataMap = new HashMap<>();
        if (CKXMXX.toString().equals(type))// 查看项目信息
        {
            Map<String, Object> map = projectService.previewUnLoginProject(goodsID);
            dataMap.put("info", map);
        } else if (CKJSCG.toString().equals(type))// 查看技术成果
        {
            Map<String, Object> techCoop = techService.previewUnloginTechCoopDetail(String.valueOf(goodsID));
            dataMap.put("info", techCoop);
        } else if (CKZJJSCG.toString().equals(type))// 查看专家技术成果
        {
            Map<String, String> map = expertService.queryUnloginAchievementById(String.valueOf(goodsID));
            techService.updateTechCooperationViews(String.valueOf(goodsID));
            dataMap.put("info", map);
        } else if (CKZJXX.toString().equals(type))// 查看专家信息
        {
            Map map = expertService.getExpertDetail(String.valueOf(goodsID), 0);
            dataMap.put("info", map);
        } else if (CKWKRW.toString().equals(type))// 查看威客任务
        {
            Map<String, Object> cooperation = cooperationService.queryUnloginCooperationInfoById(String.valueOf(goodsID));
            dataMap.put("info", cooperation);
        } else if (CXXZJL.toString().equals(type)) { // 查看/下载简历
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(goodsID));
            Resume resumeInfo = resume.previewResume(map);
            resumeInfo.setRealName(resumeInfo.getRealName().substring(0, 1).concat("********"));
            resumeInfo.setMobile("********");
            resumeInfo.setEmail("********");
            dataMap.put("info", resumeInfo);
        }

        return dataMap;
    }

    /**
     * 简历信息查看
     * 
     * @param type
     * @return
     */
    public Response viewResumeRecord(long goodsID, String type) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        Long companyId = ShiroUtil.getCompanyID();
        ShiroRealm.ShiroUser member = ShiroUtil.getMember();
        int vipLevel = member.getVipLevel();
        Map<String, Object> dataMap = new HashMap<>();

        if (createId != null) {

            Map<String, Object> con = new HashMap<>();
            // 商品ID
            con.put("goodsId", goodsID);
            con.put("companyId", companyId);
            con.put("type", type);
            // 项目是否已经被同企业账号查看过
            int viewNumber = goodsService.checkIsViewGoods(con);
            if (viewNumber == 0) {

                // 购买次数不足
                long privilegeNum = vipInfoService.getExtraPrivilegeNum(ShiroUtil.getCompanyID(), type);

                // 购买余额
                DictionaryZhbgoods goodsConfig = zhbService.getZhbGoodsByPinyin(type);

                ZhbAccount account = zhbService.getZhbAccount(ShiroUtil.getCompanyID());

                boolean result = false;
                if (account != null) {
                    result = account.getAmount().compareTo(goodsConfig.getPrice()) >= 0;
                }

                if (privilegeNum <= 0 && !result) {

                    dataMap.put("payment", ZhbPaymentConstant.RESUME_BUY_TIMES_N0);

                } else if ((!result && vipLevel != 100) && privilegeNum <= 0) {

                    dataMap.put("payment", ZhbPaymentConstant.RESUME_BALANCE_NO);

                } else if (vipLevel == 100 && privilegeNum <= 0) {
                    dataMap.put("payment", ZhbPaymentConstant.RESUME_VIP_BUY_TIMES_N0);
                } else {

                    Resume resume2 = resume.previewResumeNew(String.valueOf(goodsID));

                    con.put("createId", createId);
                    Map isDownOrColl = resume.isDownOrColl(con);
                    if (isDownOrColl != null) {
                        String isDown = isDownOrColl.get("isDown").toString();
                        String isCollect = isDownOrColl.get("isCollect").toString();
                        resume2.setIsDownload(isDown);
                        resume2.setIsCollect(isCollect);
                    } else {
                        resume2.setIsDownload("1");
                        resume2.setIsCollect("1");
                    }

                    dataMap.put("info", resume2);
                    Resume resumeBean = new Resume();
                    resumeBean.setViews("1");
                    resumeBean.setType("1");
                    resumeBean.setId(String.valueOf(goodsID));
                    // 更新点击率
                    resume.updateResume(resumeBean);
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("resumeID", goodsID);
                    map1.put("companyID", companyId);
                    map1.put("createId", resume2.getCreateid());
                    // 谁查看过我的简历 同一个人可以查看相同简历多次。
                    resume.addLookRecord(map1);

                    dataMap.put("payment", ZhbPaymentConstant.RESUME_VIEW);

                }
                response.setData(dataMap);

            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("id", String.valueOf(goodsID));
                Resume resume2 = resume.previewResume(map);
                con.put("createId", createId);
                Map isDownOrColl = resume.isDownOrColl(con);
                if (isDownOrColl != null) {
                    String isDown = isDownOrColl.get("isDown").toString();
                    String isCollect = isDownOrColl.get("isCollect").toString();
                    if ("1".equals(isDown)) {
                        resume2.setIsDownload("2");
                        resume2.setIsCollect(isCollect);
                    } else {
                        resume2.setIsDownload(isDown);
                        resume2.setIsCollect(isCollect);
                    }
                }

                dataMap.put("info", resume2);
                Resume resumeBean = new Resume();
                resumeBean.setViews("1");
                resumeBean.setType("1");
                resumeBean.setId(String.valueOf(goodsID));
                // 更新点击率
                resume.updateResume(resumeBean);
                Map<String, Object> map1 = new HashMap<>();
                map1.put("resumeID", goodsID);
                map1.put("companyID", companyId);
                map1.put("createId", resume2.getCreateid());
                // 谁查看过我的简历 同一个人可以查看相同简历多次。
                resume.addLookRecord(map1);

                dataMap.put("payment", ZhbPaymentConstant.RESUME_VIEW);
                response.setData(dataMap);
            }

        } else {

            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));

        }

        return response;
    }
}

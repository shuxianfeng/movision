package com.movision.facade.vip;

import com.movision.common.util.ShiroUtil;
import com.movision.mybatis.applyVipDetail.entity.ApplyVipDetail;
import com.movision.mybatis.applyVipDetail.service.ApplyVipDetailService;
import com.movision.mybatis.auditVipDetail.entity.AuditVipDetail;
import com.movision.mybatis.auditVipDetail.service.AuditVipDetailService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.shiro.realm.ShiroRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/28 17:17
 */
@Service
public class VipFacade {

    @Autowired
    private ApplyVipDetailService applyVipDetailService;

    @Autowired
    private AuditVipDetailService auditVipDetailService;

    @Autowired
    private UserService userService;

    public void addVipApplyRecord() {

        int userid = ShiroUtil.getAppUserID();
        User user = userService.selectByPrimaryKey(userid);
        int oldlevel = user.getLevel();
        int applyLevel = oldlevel++;

        ApplyVipDetail applyVipDetail = new ApplyVipDetail();
        applyVipDetail.setApplyLevel(applyLevel);
        applyVipDetail.setUserid(userid);

        //查看用户申请vip状态
        ApplyVipDetail latestApplyVipDetail = applyVipDetailService.selectLatestVipApplyRecord(userid);
        if (null == latestApplyVipDetail) {
            //1 无申请记录, 添加申请记录
            applyVipDetailService.addVipApplyRecord(applyVipDetail);
        } else {
            //2 存在申请记录
            //在审核表中查询申请记录
            AuditVipDetail auditVipDetail = auditVipDetailService.selectByApplyId(latestApplyVipDetail.getId());
            if (null != auditVipDetail) {
                //如果存在审核记录，则根据该记录状态，来判断申请状态
                Integer status = auditVipDetail.getStatus();
                if (status == 1) {
                    //3 审核不通过，可以再次添加申请记录
                    applyVipDetailService.addVipApplyRecord(applyVipDetail);
                }
            }

        }
    }
}

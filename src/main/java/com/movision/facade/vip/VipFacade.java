package com.movision.facade.vip;

import com.movision.common.util.ShiroUtil;
import com.movision.mybatis.applyVipDetail.entity.ApplyVipDetail;
import com.movision.mybatis.applyVipDetail.service.ApplyVipDetailService;
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

    public int addVipApplyRecord() {
        ShiroRealm.ShiroUser shiroUser = ShiroUtil.getAppUser();
        int oldlevel = null == shiroUser.getLevel() ? 0 : shiroUser.getLevel();
        int applyLevel = oldlevel++;

        ApplyVipDetail applyVipDetail = new ApplyVipDetail();
        applyVipDetail.setApplyLevel(applyLevel);
        applyVipDetail.setUserid(shiroUser.getId());

        return applyVipDetailService.addVipApplyRecord(applyVipDetail);
    }
}

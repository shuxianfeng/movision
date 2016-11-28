package com.zhuhuibao.mobile.web;

import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.service.MobileProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 控制层基类
 *
 * @author liyang
 * @date 2016年11月24日
 */
public class BaseController {

    @Autowired
    private MobileProjectService mobileProjectService;

    @Autowired
    private ZhbService zhbService;

    @Autowired
    private VipInfoService vipInfoService;

    /**
     * 获取权限商品详情
     * 
     * @param resultMap
     * @param goodsId
     * @param zhbGoodsType
     * @throws Exception
     */
    public void getPrivilegeGoodsDetails(Map<String, Object> resultMap, String goodsId, ZhbConstant.ZhbGoodsType zhbGoodsType) throws Exception {
        // 判断是否登录
        if (null != ShiroUtil.getCreateID()) {
            // 剩余特权数量
            long privilegeNum = vipInfoService.getExtraPrivilegeNum(ShiroUtil.getCompanyID(), zhbGoodsType.toString());
            resultMap.put("privilegeNum", String.valueOf(privilegeNum));
            // 筑慧币余额
            ZhbAccount account = zhbService.getZhbAccount(ShiroUtil.getCompanyID());
            resultMap.put("zhbAmount", null != account ? account.getAmount().toString() : "0");
        } else {
            resultMap.put("privilegeNum", "0");
            resultMap.put("zhbAmount", "0");
        }
        // 筑慧币单价
        DictionaryZhbgoods goodsConfig = zhbService.getZhbGoodsByPinyin(zhbGoodsType.toString());
        resultMap.put("zhbPrice", null != goodsConfig ? String.valueOf(goodsConfig.getPriceDoubleValue()) : "999");
        //此处排除公开询价这种场景
        if(StringUtils.isNotEmpty(goodsId)){
            // 获取商品详情
            Map<String, Object> goodsDetail = mobileProjectService.getProjectDetail(Long.parseLong(goodsId), zhbGoodsType);
            resultMap.putAll(goodsDetail);
        }
    }

}

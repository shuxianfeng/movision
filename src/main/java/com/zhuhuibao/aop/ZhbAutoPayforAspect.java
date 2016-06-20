package com.zhuhuibao.aop;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.annotation.ZhbAutoPayforAnnotation;
import com.zhuhuibao.common.annotation.ZhbAutoPayforAnnotation.ZhbGoodsType;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbConstant.ZhbCode;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.entity.ZhbRecord;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.shiro.realm.ShiroRealm.ShiroUser;

/**
 * VIP限制切面 在方法执行前判断是否可以执行，执行后自动减少对应的权限数量
 * 
 * @author tongxinglong
 *
 */
@Component
@Aspect
public class ZhbAutoPayforAspect {
	private static final Logger log = LoggerFactory.getLogger(ZhbAutoPayforAspect.class);

	@Autowired
	private VipInfoService vipInfoService;

	@Autowired
	private ZhbService zhbService;

	@AfterReturning(returning = "response", pointcut = "@annotation(zhbAutoPayforAnnotation)")
	public void after(JoinPoint joinPoint, Response response, ZhbAutoPayforAnnotation zhbAutoPayforAnnotation) {
		// 当要求进行扣减筑慧币时进行相关操作
		if (ZhbCode.DEDUCT.value == response.getZhbCode()) {
			ZhbGoodsType goodsType = zhbAutoPayforAnnotation.goodsType();
			DictionaryZhbgoods goods = zhbService.getZhbGoodsByPinyin(goodsType.toString());
			if (null != goods && null != response.getGoodsId() && response.getGoodsId() > 0) {
				int result = 0;
				// 先判断是否可以自定义特权信息
				if (vipInfoService.getExtraPrivilegeNum(ShiroUtil.getCompanyID(), goods.getPinyin()) > 0) {
					result = vipInfoService.useExtraPrivilege(ShiroUtil.getCompanyID(), goods.getPinyin());
				}

				if (0 == result) {
					// 筑慧币支付
					ZhbRecord zhbRecord = new ZhbRecord();
					zhbRecord.setOrderNo("0");
					zhbRecord.setBuyerId(ShiroUtil.getCompanyID());
					zhbRecord.setOperaterId(ShiroUtil.getCreateID());
					zhbRecord.setAmount(goods.getPrice());
					zhbRecord.setGoodsType(goods.getType());
					zhbRecord.setGoodsId(response.getGoodsId());

					result = zhbService.payForGoods(response.getGoodsId(), goods.getType(), goods.getPrice());
				}

				if (1 != result) {
					// 将data数据置为null
					response.setData(null);
					log.warn("ZhbAutoPayforAspect::after::自动支付失败,goodsType=" + goodsType + ",goodsId" + response.getGoodsId() + ",operaterID="
							+ ShiroUtil.getCreateID());
					throw new BusinessException(MsgCodeConstant.ZHB_AUTOPAYFOR_FAILED, "筑慧币余额不足");
				}

			} else {
				// 将data数据置为null
				response.setData(null);
				log.warn("ZhbAutoPayforAspect::after::自动支付失败,goodsType=" + goodsType + ",goodsId" + response.getGoodsId() + ",operaterID="
						+ ShiroUtil.getCreateID());
				throw new BusinessException(MsgCodeConstant.ZHB_AUTOPAYFOR_FAILED, "筑慧币余额不足");
			}
		}
	}
}

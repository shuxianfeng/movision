package com.zhuhuibao.business.oms.member;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.business.mall.MallIndexController;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.MemberShop;
import com.zhuhuibao.mybatis.memCenter.service.MemShopService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 商户店铺 - 运营系统
 *
 * @author jianglz
 * @since 16/6/22.
 */
@RestController
@RequestMapping("/rest/mall/oms")
@Api(value = "Mall-Shop-OMS", description = "筑慧商城商户店铺-运营系统")
public class OmsShopController {
    private static final Logger log = LoggerFactory.getLogger(MallIndexController.class);


    @Autowired
    MemShopService memShopService;

    @Autowired
    MemberService memberService;

    @ApiOperation(value = "查询商户店铺信息", notes = "查询商户店铺信息")
    @RequestMapping(value = "sel_shop1", method = RequestMethod.GET)
    public Response searchOne(@ApiParam("商铺ID") @RequestParam String shopId) {

        MemberShop shop = memShopService.findByID(shopId);

        if (shop == null) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "商铺不存在");
        }
        return new Response(shop);
    }

    @ApiOperation(value = "搜索商户店铺", notes = "搜索商户店铺")
    @RequestMapping(value = "sel_shop", method = RequestMethod.GET)
    public Response searchShop(@ApiParam("会员账号") @RequestParam(required = false) String account,
                               @ApiParam("企业名称") @RequestParam(required = false) String companyName,
                               @ApiParam("店铺名称") @RequestParam(required = false) String shopName,
                               @ApiParam("审核状态") @RequestParam(required = false) String status,
                               @RequestParam(required = false,defaultValue = "1") String pageNo,
                               @RequestParam(required = false,defaultValue = "10") String pageSize) {
        log.debug("搜索商铺...");

        Map<String, String> paramMap = new HashMap<>();

        if (!StringUtils.isEmpty(account)) {
            paramMap.put("account", account);
        }
        if (!StringUtils.isEmpty(companyName)) {
            paramMap.put("companyName", companyName);
        }
        if (!StringUtils.isEmpty(shopName)) {
            paramMap.put("shopName", shopName);
        }
        if (!StringUtils.isEmpty(status)) {
            paramMap.put("status", status);
        }
        Paging<MemberShop> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        List<MemberShop> list = memShopService.findAllByCondition(pager, paramMap);
        pager.result(list);

        return new Response(pager);
    }


    @ApiOperation(value = "更新商户店铺状态", notes = "更新商户店铺状态")
    @RequestMapping(value = "upd_shop_st", method = RequestMethod.POST)
    public Response uploadStatus(@ApiParam("商铺ID") @RequestParam String shopId,
                                 @ApiParam("状态 1：待审核 2：已审核  3：已拒绝 4：已注销") @RequestParam  String status,
                                 @ApiParam("拒绝理由") @RequestParam(required = false)  String reason) {
        MemberShop shop = check(shopId);

      
        shop.setStatus(status);

        if ("3".equals(status)) {
            if (StringUtils.isEmpty(reason)) {
                throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "拒绝理由不能为空");
            }
            shop.setReason(reason);
        }

        memShopService.update(shop);

        return new Response();
    }


    /**
     * 验证商铺是否为登陆用户所在企业商铺
     *
     * @param shopId
     * @return
     */
    private MemberShop check(@ApiParam("商铺ID") @RequestParam String shopId) {
//        Long companyID =ShiroUtil.getCompanyID();
//        if(companyID == null){
//            throw new BusinessException(MsgCodeConstant.un_login, "请登录");
//        }
        //验证店铺ID是否为该用户所在企业的店铺
        MemberShop shop = memShopService.findByID(shopId);
                //.findByCompanyID(companyID);
        if (shop == null) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "用户商铺不存在");
        } else {
            if (!String.valueOf(shop.getId()).equals(shopId)) {
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "用户商铺不存在");
            }
        }
        return shop;
    }
}

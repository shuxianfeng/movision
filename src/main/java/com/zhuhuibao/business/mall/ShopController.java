package com.zhuhuibao.business.mall;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MemberConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.MemShopCheck;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.MemberShop;
import com.zhuhuibao.mybatis.memCenter.service.MemShopCheckService;
import com.zhuhuibao.mybatis.memCenter.service.MemShopService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 商户店铺 - 会员中心
 * @author jianglz
 * @since 16/6/22.
 */
@RestController
@RequestMapping("/rest/member/mc/shop")
@Api(value = "Mall-Shop", description = "筑慧商城会员中心商户店铺")
public class ShopController {
    private static final Logger log = LoggerFactory.getLogger(MallIndexController.class);


    @Autowired
    MemShopService memShopService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemShopCheckService shopCheckService;


    @ApiOperation(value = "查询商户店铺信息", notes = "查询商户店铺信息")
    @RequestMapping(value = "sel_shop", method = RequestMethod.GET)
    public Response searchOne() {
        MemberShop shop;
        Long companyId = ShiroUtil.getCompanyID();
        //判断审核表是否存在
        MemShopCheck check = shopCheckService.findByCompanyID(companyId);
        if(check == null){
            shop =  memShopService.findByCompanyID(companyId);
            if (shop == null) {
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "商铺不存在");
            }
            return new Response(shop);
        }else{
            return new Response(check);
        }
    }

    @ApiOperation(value = "编辑商户店铺", notes = "编辑商户店铺")
    @RequestMapping(value = "upd_shop", method = RequestMethod.POST)
    public Response updateStop(@ApiParam("商铺ID") @RequestParam String shopId,
                               @ApiParam("商铺名称") @RequestParam String shopName,
                            @ApiParam("banner图片URL") @RequestParam String bannerUrl){

        log.debug("更新商铺...");
        Long memberId = ShiroUtil.getCreateID();
        if(memberId==null){
            throw new AuthException(MsgCodeConstant.un_login,
                    MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        if(StringUtils.isEmpty(shopName)){
           throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR,"商铺名称不能为空");
        }
        if(StringUtils.isEmpty(bannerUrl)){
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR,"商铺banner图不能为空");
        }

        Long companyId = ShiroUtil.getCompanyID();

        check(shopId,companyId);

        Member member =  memberService.findMemById(String.valueOf(memberId));

        String account = member.getMobile() == null ? member.getEmail() : member.getMobile();
        String companyName = member.getEnterpriseName() == null ? "" : member.getEnterpriseName();


        MemShopCheck shopCheck = shopCheckService.findByCompanyID(companyId);
        if(shopCheck == null){
            shopCheck = new MemShopCheck();
            shopCheck.setOpreatorId(memberId.intValue());
            shopCheck.setCompanyId(companyId.intValue());
            shopCheck.setCompanyAccount(account);
            shopCheck.setCompanyName(companyName);
            shopCheck.setUpdateTime(DateUtils.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
            shopCheck.setStatus(MemberConstant.ShopStatus.DSH.toString());
            shopCheck.setShopName(shopName);
            shopCheck.setBannerUrl(bannerUrl);

            shopCheckService.insert(shopCheck);
        } else{
            shopCheck.setStatus(MemberConstant.ShopStatus.DSH.toString());
            shopCheck.setShopName(shopName);
            shopCheck.setBannerUrl(bannerUrl);
            shopCheckService.update(shopCheck);
        }

        return new Response();
    }

    /**
     * 验证商铺是否为登陆用户所在企业商铺
     * @param shopId  商铺ID
     */
    private void check(String shopId,Long companyId) {
        if(companyId==null){
            throw new AuthException(MsgCodeConstant.un_login,
                    MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        //验证店铺ID是否为该用户所在企业的店铺
        MemberShop shop =  memShopService.findByCompanyID(companyId);
        if(shop == null){
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"用户商铺不存在");
        }else{
            if(!String.valueOf(shop.getId()).equals(shopId)){
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"用户商铺不存在");
            }
        }
    }
}

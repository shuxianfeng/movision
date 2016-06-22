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
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.MemberShop;
import com.zhuhuibao.mybatis.memCenter.service.MemShopService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
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
@RequestMapping("/rest/mall/mc")
@Api(value = "Mall-Shop", description = "筑慧商城商户店铺")
public class ShopController {
    private static final Logger log = LoggerFactory.getLogger(MallIndexController.class);


    @Autowired
    MemShopService memShopService;

    @Autowired
    MemberService memberService;


    @ApiOperation(value = "新增商户店铺", notes = "新增商户店铺")
    @RequestMapping(value = "add_shop", method = RequestMethod.POST)
    public Response addShop(@ApiParam("商铺名称") @RequestParam String shopName,
                            @ApiParam("banner图片URL") @RequestParam String bannerUrl){


        if(StringUtils.isEmpty(shopName)){
           throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR,"商铺名称不能为空");
        }
        if(StringUtils.isEmpty(bannerUrl)){
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR,"商铺banner图不能为空");
        }

        Long memberId = ShiroUtil.getCreateID();
        if(memberId==null){
            throw new AuthException(MsgCodeConstant.un_login,
                    MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        Long companyId = ShiroUtil.getCompanyID();
        Member member =  memberService.findMemById(String.valueOf(memberId));

        String account = member.getMobile() == null ? member.getEmail() : member.getMobile();
        String companyName = member.getEnterpriseName() == null ? "" : member.getEnterpriseName();

        MemberShop memberShop = new MemberShop();
        memberShop.setOpreatorId(memberId.intValue());
        memberShop.setCompanyId(companyId.intValue());
        memberShop.setCompanyAccount(account);
        memberShop.setCompanyName(companyName);
        memberShop.setUpdateTime(new Date());
        memberShop.setStatus(MemberConstant.ShopStatus.DSH.toString());
        memberShop.setShopName(shopName);
        memberShop.setBannerUrl(bannerUrl);

        memShopService.insert(memberShop);

        return new Response();
    }


    @ApiOperation(value = "更新商户店铺", notes = "更新商户店铺")
    @RequestMapping(value = "upd_shop", method = RequestMethod.POST)
    public Response updateShop(@ApiParam("商铺ID") @RequestParam String shopId,
                               @ApiParam("Banner图片URL")@RequestParam String bannderUrl){
        log.debug("更新商铺...");

        MemberShop shop = check(shopId);

        shop.setUpdateTime(new Date());
        shop.setBannerUrl(bannderUrl);

        memShopService.upload(shop);

        return new Response();
    }


    /**
     * 验证商铺是否为登陆用户所在企业商铺
     * @param shopId
     * @return
     */
    private MemberShop check(@ApiParam("商铺ID") @RequestParam String shopId) {
        Long companyId = ShiroUtil.getCompanyID();
        //验证店铺ID是否为该用户所在企业的店铺
        MemberShop shop =  memShopService.findByCompanyID(companyId);
        if(shop == null){
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"用户商铺不存在");
        }else{
            if(!String.valueOf(shop.getId()).equals(shopId)){
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"用户商铺不存在");
            }
        }
        return shop;
    }
}

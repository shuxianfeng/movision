package com.zhuhuibao.mobile.web.mc;

import java.io.IOException;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.PageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.aop.LoginAccess;
import com.zhuhuibao.aop.UserAccess;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.service.MobileProductService;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * @author tongxinglong
 * @date 2016/10/19 0019.
 */
@RestController
@RequestMapping("/rest/m/product/mc/")
public class MobileProductMcController {
    @Autowired
    private MobileProductService mobileProductService;

    @LoginAccess
    @UserAccess(value = "ALL", viplevel = "30,60,130,160")
    @RequestMapping(value = { "/sel_product_list" }, method = RequestMethod.GET)
    @ApiOperation(value = "我的产品", notes = "我的产品", response = Response.class)
    public Response selProductList(@ApiParam(value = "一级分类") @RequestParam(required = false) Integer fcateid, @ApiParam(value = "二级分类") @RequestParam(required = false) Integer scateid,
            @ApiParam(value = "状态") @RequestParam(required = false) Integer status, @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
            @ApiParam(value = "每页显示的条数", defaultValue = "10") @RequestParam(required = false) String pageSize) throws IOException {

        Paging<Product> productPaging = mobileProductService.getProductList(ShiroUtil.getCreateID(), fcateid, scateid, status, pageNo, pageSize);
        return new Response(productPaging);
    }

    @LoginAccess
    @UserAccess(value = "ALL", viplevel = "30,60,130,160")
    @RequestMapping(value = { "/upd_product_status" }, method = RequestMethod.POST)
    @ApiOperation(value = "修改产品状态", notes = "修改产品状态", response = Response.class)
    public Response updProductStatus(@ApiParam(value = "产品Id") @RequestParam(required = false) Long productId, @ApiParam(value = "状态") @RequestParam(required = false) Integer status)
            throws IOException {
        mobileProductService.updateProductStatus(ShiroUtil.getCreateID(), productId, status);
        return new Response();
    }

    @LoginAccess
    @UserAccess(value = "ALL", viplevel = "30,60,130,160")
    @RequestMapping(value = { "/sel_product_detail" }, method = RequestMethod.GET)
    @ApiOperation(value = "获得产品信息根据ID", notes = "获得产品信息根据ID", response = Response.class)
    public Response selProductDetail(@ApiParam(value = "产品Id") @RequestParam Long productId) throws IOException {

        return new Response(mobileProductService.getMemberProductById(productId, ShiroUtil.getCreateID()));
    }
}

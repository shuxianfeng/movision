package com.zhuhuibao.business.oms.product;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * Created by gmli on 2016/08/10
 */
@RestController
@RequestMapping("/rest/product/oms")
@Api(value="Product")
public class ProductController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	@Autowired
    private ProductService productService;

    @ApiOperation(value="产品查询列表(运营分页)",notes="产品查询列表(运营分页)",response = Response.class)
    @RequestMapping(value = "sel_productList", method = RequestMethod.GET)
    public Response achievementListOms(ProductWithBLOBs product,
                                       @RequestParam(required = false)String pageNo,
                                       @RequestParam(required = false)String pageSize)  {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Product> pager = new Paging<Product>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        
        List<Product> adList = productService.findAllProduct(pager, product);
        pager.result(adList);
        response.setData(pager);
        return response;
    }
    
    @ApiOperation(value="查询产品详情",notes="查询产品详情",response = Response.class)
    @RequestMapping(value = "sel_productDetails", method = RequestMethod.GET)
    public Response queryProductById(Long id) throws IOException {

        return productService.selectByPrimaryKey(id);
    }
    
    @ApiOperation(value="更新产品信息",notes="产品审核",response = Response.class)
    @RequestMapping(value = "upd_product", method = RequestMethod.POST)
    public Response updateProduct(ProductWithBLOBs product) throws IOException {
        Response response = new Response();
        Long createid = ShiroUtil.getOmsCreateID();
        if(createid!=null){
           response = productService.updateProduct(product); 
            
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

}

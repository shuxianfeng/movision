package com.zhuhuibao.business.system.product.site;

import java.io.IOException;
import java.util.Map;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.fsearch.pojo.spec.ProductSearchSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zhuhuibao.fsearch.service.impl.ProductsService;

import org.springframework.web.bind.annotation.RestController;

/**
 * 产品控制
 * @author caijl@20160317
 *
 */
@RestController
@Api(value = "products",description = "产品搜索")
public class ProductsController {
	private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
	
	@Autowired
	ProductsService productsService;
	
	@RequestMapping(value={"/rest/searchProducts","/rest/system/site/product/sel_products"}, method = RequestMethod.GET)
	@ApiOperation(value = "产品搜索",notes = "产品搜索",response = Response.class)
	public Response searchProducts(ProductSearchSpec spec) throws IOException
	{
		if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
			spec.setLimit(12);
		}
		Response response = new Response();
		response.setCode(200);
		Map<String, Object> ret = null;
		try{
			ret = productsService.search(spec);
			response.setMsgCode(1);
			response.setMessage("OK!");
			response.setData(ret);
		}
		catch (Exception e) {
			response.setMsgCode(0);
			response.setMessage("search error!");
		}

		return response;
	}
}

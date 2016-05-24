package com.zhuhuibao.business.product;

import java.io.IOException;
import java.util.Map;

import com.zhuhuibao.fsearch.pojo.ProductSearchSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zhuhuibao.common.pojo.JsonResult;
import com.zhuhuibao.fsearch.service.impl.ProductsService;
import org.springframework.web.bind.annotation.RestController;

/**
 * 产品控制
 * @author caijl@20160317
 *
 */
@RestController
public class ProductsController {
	private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
	
	@Autowired
	ProductsService productsService;
	
	@RequestMapping(value="/rest/searchProducts", method = RequestMethod.GET)
	public JsonResult searchProducts(ProductSearchSpec spec) throws IOException
	{
		if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
			spec.setLimit(12);
		}
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		Map<String, Object> ret = null;
		try{
			ret = productsService.search(spec);
		}
		catch (Exception e) {
			jsonResult.setMsgCode(0);
			jsonResult.setMessage("search error!");
		}
		
		jsonResult.setMsgCode(1);
		jsonResult.setMessage("OK!");
		jsonResult.setData(ret);

		return jsonResult;
	}
}

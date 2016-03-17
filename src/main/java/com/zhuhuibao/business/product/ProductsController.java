package com.zhuhuibao.business.product;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.service.impl.ProductsService;
import com.zhuhuibao.pojo.ProductSearchSpec;

/**
 * 产品控制
 * @author caijl@20160317
 *
 */
@Controller
public class ProductsController {
	private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
	
	@Autowired
	ProductsService productsService;
	
	@RequestMapping(value="/rest/searchProducts", method = RequestMethod.GET)
	@ResponseBody
	public void searchProducts(HttpServletRequest req,HttpServletResponse response,ProductSearchSpec spec) throws JsonGenerationException, JsonMappingException, IOException
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

		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
}

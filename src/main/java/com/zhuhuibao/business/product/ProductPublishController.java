package com.zhuhuibao.business.product;

import java.io.IOException;
import java.util.*;

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
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
import com.zhuhuibao.mybatis.product.entity.ComplainSuggest;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.product.service.ComplainSuggestService;
import com.zhuhuibao.mybatis.product.service.ProductParamService;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 产品控制层
 * @author penglong
 *
 */
@Controller
public class ProductPublishController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductPublishController.class);
	
	@Autowired
	private ProductService productService;
	@Autowired
	private ComplainSuggestService suggestService;
	@Autowired
	private ProductParamService paramService;
	@Autowired
    private CategoryMapper categoryMapper;
	@Autowired
	private BrandMapper brandMapper;
	
	/**
	 * 新增产品
	 * @param req
	 * @param response
	 * @param product
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@RequestMapping(value="/rest/addProduct", method = RequestMethod.POST)
	public void addProduct(HttpServletRequest req,HttpServletResponse response,String json) throws JsonGenerationException, JsonMappingException, IOException
	{
		Gson gson = new Gson();
		ProductWithBLOBs product = gson.fromJson(json, ProductWithBLOBs.class);
		JsonResult jsonResult = new JsonResult();
		productService.insertProduct(product);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/addComplainSuggest", method = RequestMethod.POST)
	public void addComplainSuggest(HttpServletRequest req,HttpServletResponse response,ComplainSuggest suggest) throws JsonGenerationException, JsonMappingException, IOException
	{
		JsonResult jsonResult = new JsonResult();
		suggestService.insert(suggest);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	
	@RequestMapping(value="/rest/updateProduct", method = RequestMethod.POST)
	public void updateProduct(HttpServletRequest req,HttpServletResponse response,ProductWithBLOBs product) throws JsonGenerationException, JsonMappingException, IOException
	{
		JsonResult jsonResult = new JsonResult();
		jsonResult = productService.updateProduct(product);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/queryProductById", method = RequestMethod.GET)
	public void queryProductById(HttpServletRequest req,HttpServletResponse response,Long id) throws JsonGenerationException, JsonMappingException, IOException
	{
		JsonResult jsonResult = new JsonResult();
		jsonResult = productService.selectByPrimaryKey(id);
		productService.updateHit(id);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/queryParam", method = RequestMethod.GET)
	public void queryParam(HttpServletRequest req,HttpServletResponse response,String paramIDs) throws JsonGenerationException, JsonMappingException, IOException
	{
		JsonResult jsonResult = new JsonResult();
		jsonResult = paramService.queryParam(paramIDs);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/queryProductByParam", method = RequestMethod.GET)
	public void queryProductByParam(HttpServletRequest req,HttpServletResponse response,ProductWithBLOBs product) throws JsonGenerationException, JsonMappingException, IOException
	{
		JsonResult jsonResult = new JsonResult();
		jsonResult = productService.queryProductByParam(product);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	/**
	 * 查询产品分页
	 * @param req
	 * @param response
	 * @param product
	 * @param pageNo
	 * @param pageSize
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@RequestMapping(value="/rest/findAllProduct")
	@ResponseBody
	public void findAllProduct(HttpServletRequest req,HttpServletResponse response,ProductWithBLOBs product,String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException
	{
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Product> pager = new Paging<Product>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<Product>  productList = productService.findAllByPager(pager,product);
        pager.result(productList);
        jsonResult.setData(pager);
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value = "/rest/getProductFirstCategory", method = RequestMethod.GET)
	public void getProductFirstCategory(HttpServletRequest req, HttpServletResponse response) throws IOException {
		JsonResult jsonResult = new JsonResult();
		List<ResultBean> systemList = categoryMapper.findSystemList();
		response.setContentType("application/json;charset=utf-8");
		jsonResult.setData(systemList);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}

	    /**
	     * 查询大系统下所有子系统类目
	     * @param req
	     * @return
	     * @throws IOException
	     */

	    @RequestMapping(value = "/rest/getProductSecondCategory", method = RequestMethod.GET)
	    public void getProductSecondCategory(HttpServletRequest req, HttpServletResponse response) throws IOException {
	        String parentId = req.getParameter("parentID");
	        JsonResult jsonResult = new JsonResult();
	        List<ResultBean> subSystemList = categoryMapper.findSubSystemList(parentId);
	        jsonResult.setData(subSystemList);
	        response.setContentType("application/json;charset=utf-8");
	        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	    }
	    
	    /**
	     * 查询品牌
	     * @param req
	     * @return
	     * @throws IOException
	     */
	    @RequestMapping(value = "/rest/getBrandList", method = RequestMethod.GET)
	    public void getBrandList(HttpServletRequest req, HttpServletResponse response, Brand brand) throws IOException {
	    	brand.setStatus(1);
	        List<Brand> brandList = brandMapper.searchBrandByStatus(brand);
	        JsonResult result = new JsonResult();
	        result.setCode(200);
	        result.setData(brandList);
	        response.setContentType("application/json;charset=utf-8");
	        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	    }
	    
	    /**
	     * 品牌详情页面查询二级系统
	     * @param req
	     * @return
	     * @throws IOException
	     */
	    @RequestMapping(value = "/rest/productDetail/querySCateListByBrandId", method = RequestMethod.GET)
	    public void querySCateListByBrandId(HttpServletRequest req, HttpServletResponse response, Product product) throws IOException {
	    	JsonResult jsonResult = new JsonResult();
	        jsonResult = productService.querySCateListByBrandId(product);
	        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	    }
	    
	    /**
		 * 品牌详细页面根据二级系统查询产品
		 * @param req
		 * @param response
		 * @param product
		 * @param pageNo
		 * @param pageSize
		 * @throws IOException 
		 * @throws JsonMappingException 
		 * @throws JsonGenerationException 
		 */
		@RequestMapping(value="/rest/productDetail/queryProductInfoBySCategory")
		@ResponseBody
		public void queryProductInfoBySCategory(HttpServletRequest req,HttpServletResponse response,ProductWithBLOBs product) throws JsonGenerationException, JsonMappingException, IOException
		{
			JsonResult jsonResult = new JsonResult();
			Map<String,Object> productMap = new HashMap<String,Object>();
			productMap.put("scateid",product.getScateid());
			productMap.put("brandid",product.getBrandid());
			productMap.put("status",Constant.product_status_publish);
			productMap.put("count",Constant.brand_page_product_count);
			productMap.put("order", "publishTime");
	        jsonResult = productService.queryProductInfoBySCategory(productMap);
	        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
		}
		
		/**
		 * 产品推荐信息，根据点击率排序
		 * @param req
		 * @param response
		 * @param product
		 * @param pageNo
		 * @param pageSize
		 * @throws IOException 
		 * @throws JsonMappingException 
		 * @throws JsonGenerationException 
		 */
		@RequestMapping(value="/rest/productDetail/queryRecommendProduct")
		@ResponseBody
		public void queryRecommendProduct(HttpServletRequest req,HttpServletResponse response,ProductWithBLOBs product,String count) throws JsonGenerationException, JsonMappingException, IOException
		{
			JsonResult jsonResult = new JsonResult();
			Map<String,Object> productMap = new HashMap<String,Object>();
			productMap.put("status",Constant.product_status_publish);
			productMap.put("count",Constant.recommend_product_count);
			productMap.put("order", "hit");
	        jsonResult = productService.queryProductInfoBySCategory(productMap);
	        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
		}
		
		/**
		 * 查询热点产品
		 * @param req
		 * @param response
		 * @param product
		 * @param pageNo
		 * @param pageSize
		 * @throws IOException 
		 * @throws JsonMappingException 
		 * @throws JsonGenerationException 
		 */
		@RequestMapping(value="/rest/productDetail/queryHotProduct")
		@ResponseBody
		public void queryHotProduct(HttpServletRequest req,HttpServletResponse response,ProductWithBLOBs product,String count) throws JsonGenerationException, JsonMappingException, IOException
		{
			JsonResult jsonResult = new JsonResult();
			Map<String,Object> productMap = new HashMap<String,Object>();
			productMap.put("status",Constant.product_status_publish);
			productMap.put("count",Constant.hot_product_count);
			productMap.put("order", "hit");
	        jsonResult = productService.queryProductInfoBySCategory(productMap);
	        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
		}
		
		/**
		 * 产品列表页面
		 * @param req
		 * @param response
		 * @param product
		 * @param pageNo
		 * @param pageSize
		 * @throws IOException 
		 * @throws JsonMappingException 
		 * @throws JsonGenerationException 
		 */
		@RequestMapping(value="/rest/productDetail/findAllProductList")
		@ResponseBody
		public void findAllProductList(HttpServletRequest req,HttpServletResponse response,ProductWithBLOBs product,String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException
		{
			JsonResult jsonResult = new JsonResult();
			if (StringUtils.isEmpty(pageNo)) {
	            pageNo = "1";
	        }
	        if (StringUtils.isEmpty(pageSize)) {
	            pageSize = "10";
	        }
	        Paging<Product> pager = new Paging<Product>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
	        List<Product>  users = productService.findAllByPager(pager,product);
	        pager.result(users);
	        jsonResult.setData(pager);
	        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
		}
}

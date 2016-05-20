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
import org.springframework.web.bind.annotation.*;

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
import com.zhuhuibao.mybatis.product.service.ProductService;
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
    private CategoryMapper categoryMapper;
	@Autowired
	private BrandMapper brandMapper;
	
	/**
	 * 新增产品
	 * @throws IOException
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@RequestMapping(value="/rest/addProduct", method = RequestMethod.POST)
	public JsonResult addProduct(String json) throws IOException
	{
		Gson gson = new Gson();
		ProductWithBLOBs product = gson.fromJson(json, ProductWithBLOBs.class);
		JsonResult jsonResult = new JsonResult();
		productService.insertProduct(product);
		return jsonResult;
	}
	
	@RequestMapping(value="/rest/addComplainSuggest", method = RequestMethod.POST)
	public JsonResult addComplainSuggest(ComplainSuggest suggest) throws IOException
	{
		JsonResult jsonResult = new JsonResult();
		suggestService.insert(suggest);

		return jsonResult;
	}
	
	
	@RequestMapping(value="/rest/updateProduct", method = RequestMethod.POST)
	public JsonResult updateProduct(ProductWithBLOBs product) throws IOException
	{
		JsonResult jsonResult = new JsonResult();
		jsonResult = productService.updateProduct(product);

		return jsonResult;
	}
	
	@RequestMapping(value="/rest/updateProductStatus", method = RequestMethod.POST)
	public JsonResult updateProductStatus(ProductWithBLOBs product) throws IOException
	{
		JsonResult jsonResult = new JsonResult();
		jsonResult = productService.updateProductStatus(product);

		return jsonResult;
	}
	
	@RequestMapping(value="/rest/batchUnpublish", method = RequestMethod.POST)
	public JsonResult batchUnpublish(@RequestParam String[] ids) throws IOException
	{
		JsonResult jsonResult = new JsonResult();
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, ids);
		jsonResult = productService.batchUnpublish(list);

		return jsonResult;
	}
	
	@RequestMapping(value="/rest/queryProductById", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult queryProductById(Long id) throws IOException
	{
		JsonResult jsonResult = new JsonResult();
		jsonResult = productService.selectByPrimaryKey(id);

		return jsonResult;
	}
	
	@RequestMapping(value="/rest/productDetail/queryProductInfoById", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult queryProductInfoById(Long id) throws IOException
	{
		JsonResult jsonResult = productService.queryProductInfoById(id);
		productService.updateHit(id);
		return jsonResult;
	}
	
	@RequestMapping(value="/rest/productDetail/queryPrdDescParamService", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult queryPrdDescParamService(Long id) throws IOException
	{

		return productService.queryPrdDescParamService(id);
	}
	
	/**
	 * 查询产品分页
	 * @param product
	 * @param pageNo
	 * @param pageSize
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@RequestMapping(value="/rest/findAllProduct", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult findAllProduct(ProductWithBLOBs product, String pageNo, String pageSize) throws IOException
	{
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Product> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Product>  productList = productService.findAllByPager(pager,product);
        pager.result(productList);
        jsonResult.setData(pager);

		return jsonResult;
	}
	
	@RequestMapping(value = "/rest/getProductFirstCategory", method = RequestMethod.GET)
	public JsonResult getProductFirstCategory(HttpServletResponse response) throws IOException {
		JsonResult jsonResult = new JsonResult();
		List<ResultBean> systemList = categoryMapper.findSystemList();
		response.setContentType("application/json;charset=utf-8");
		jsonResult.setData(systemList);

		return jsonResult;
	}

	    /**
	     * 查询大系统下所有子系统类目
	     * @param req
	     * @return
	     * @throws IOException
	     */

	    @RequestMapping(value = "/rest/getProductSecondCategory", method = RequestMethod.GET)
	    public JsonResult getProductSecondCategory(HttpServletRequest req) throws IOException {
	        String parentId = req.getParameter("parentID");
	        JsonResult jsonResult = new JsonResult();
	        List<ResultBean> subSystemList = categoryMapper.findSubSystemList(parentId);
	        jsonResult.setData(subSystemList);

			return jsonResult;
	    }
	    
	    /**
	     * 查询品牌
	     * @return
	     * @throws IOException
	     */
	    @RequestMapping(value = "/rest/getBrandList", method = RequestMethod.GET)
	    public JsonResult getBrandList(Brand brand) throws IOException {
	    	brand.setStatus(1);
	        List<Brand> brandList = brandMapper.searchBrandByStatus(brand);
	        JsonResult result = new JsonResult();
	        result.setCode(200);
	        result.setData(brandList);

			return result;
	    }
	    
	    /**
	     * 品牌详情页面查询二级系统
	     * @return
	     * @throws IOException
	     */
	    @RequestMapping(value = "/rest/productDetail/querySCateListByBrandId", method = RequestMethod.GET)
	    public JsonResult querySCateListByBrandId(Product product) throws IOException {
	    	JsonResult jsonResult = new JsonResult();
	        jsonResult = productService.querySCateListByBrandId(product);

			return jsonResult;
	    }
	    
	    /**
		 * 品牌详情页面根据二级系统查询产品
		 * @param product
		 * @throws IOException
		 * @throws JsonMappingException 
		 * @throws JsonGenerationException 
		 */
		@RequestMapping(value="/rest/productDetail/queryProductInfoBySCategory",method = RequestMethod.GET)
		@ResponseBody
		public JsonResult queryProductInfoBySCategory(ProductWithBLOBs product) throws IOException
		{
			JsonResult jsonResult = new JsonResult();
			Map<String,Object> productMap = new HashMap<String,Object>();
			productMap.put("scateid",product.getScateid());
			productMap.put("brandid",product.getBrandid());
			productMap.put("status",Constant.product_status_publish);
			productMap.put("count",Constant.brand_page_product_count);
			productMap.put("order", "publishTime");
	        jsonResult = productService.queryProductInfoBySCategory(productMap);

			return jsonResult;
		}
		
		/**
		 * 产品推荐信息，根据点击率排序
		 * @throws IOException
		 * @throws JsonMappingException 
		 * @throws JsonGenerationException 
		 */
		@RequestMapping(value="/rest/productDetail/queryRecommendProduct",method = RequestMethod.GET)
		@ResponseBody
		public JsonResult queryRecommendProduct() throws IOException
		{
			JsonResult jsonResult = new JsonResult();
			Map<String,Object> productMap = new HashMap<String,Object>();
			productMap.put("status",Constant.product_status_publish);
			productMap.put("count",Constant.recommend_product_count);
			productMap.put("order", "hit");
	        jsonResult = productService.queryRecommendHotProduct(productMap);

			return jsonResult;
		}
		
		/**
		 * 查询热点产品
		 * @throws IOException
		 * @throws JsonMappingException 
		 * @throws JsonGenerationException 
		 */
		@RequestMapping(value="/rest/productDetail/queryHotProduct",method = RequestMethod.GET)
		@ResponseBody
		public JsonResult queryHotProduct() throws IOException
		{
			JsonResult jsonResult = new JsonResult();
			Map<String,Object> productMap = new HashMap<String,Object>();
			productMap.put("status",Constant.product_status_publish);
			productMap.put("count",Constant.hot_product_count);
			productMap.put("order", "hit");
	        jsonResult = productService.queryRecommendHotProduct(productMap);

			return jsonResult;
		}
}

package com.zhuhuibao.business.product;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhuhuibao.common.Response;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
import com.zhuhuibao.mybatis.oms.entity.ComplainSuggest;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.oms.service.ComplainSuggestService;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 产品控制层
 * @author penglong
 *
 */
@RestController
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
	public Response addProduct(String json) throws IOException
	{
		Gson gson = new Gson();
		ProductWithBLOBs product = gson.fromJson(json, ProductWithBLOBs.class);
		Response response = new Response();
		productService.insertProduct(product);
		return response;
	}
	
	@RequestMapping(value="/rest/addComplainSuggest", method = RequestMethod.POST)
	public Response addComplainSuggest(ComplainSuggest suggest) throws IOException
	{
		Response response = new Response();
		suggestService.insert(suggest);

		return response;
	}
	
	
	@RequestMapping(value="/rest/updateProduct", method = RequestMethod.POST)
	public Response updateProduct(ProductWithBLOBs product) throws IOException
	{
		Response response = new Response();
		response = productService.updateProduct(product);

		return response;
	}
	
	@RequestMapping(value="/rest/updateProductStatus", method = RequestMethod.POST)
	public Response updateProductStatus(ProductWithBLOBs product) throws IOException
	{
		Response response = new Response();
		response = productService.updateProductStatus(product);

		return response;
	}
	
	@RequestMapping(value="/rest/batchUnpublish", method = RequestMethod.POST)
	public Response batchUnpublish(@RequestParam String[] ids) throws IOException
	{
		Response response = new Response();
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, ids);
		response = productService.batchUnpublish(list);

		return response;
	}
	
	@RequestMapping(value="/rest/queryProductById", method = RequestMethod.GET)
	@ResponseBody
	public Response queryProductById(Long id) throws IOException
	{
		Response response = new Response();
		response = productService.selectByPrimaryKey(id);

		return response;
	}
	
	@RequestMapping(value="/rest/productDetail/queryProductInfoById", method = RequestMethod.GET)
	@ResponseBody
	public Response queryProductInfoById(Long id) throws IOException
	{
		Response response = productService.queryProductInfoById(id);
		productService.updateHit(id);
		return response;
	}
	
	@RequestMapping(value="/rest/productDetail/queryPrdDescParamService", method = RequestMethod.GET)
	@ResponseBody
	public Response queryPrdDescParamService(Long id) throws IOException
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
	public Response findAllProduct(ProductWithBLOBs product, String pageNo, String pageSize) throws IOException
	{
		Response response = new Response();
		if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Product> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Product>  productList = productService.findAllByPager(pager,product);
        pager.result(productList);
        response.setData(pager);

		return response;
	}
	
	@RequestMapping(value = "/rest/getProductFirstCategory", method = RequestMethod.GET)
	public Response getProductFirstCategory(HttpServletResponse response) throws IOException {
		Response jsonResult = new Response();
		List<ResultBean> systemList = categoryMapper.findSystemList();
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
	    public Response getProductSecondCategory(HttpServletRequest req) throws IOException {
	        String parentId = req.getParameter("parentID");
	        Response response = new Response();
	        List<ResultBean> subSystemList = categoryMapper.findSubSystemList(parentId);
	        response.setData(subSystemList);

			return response;
	    }
	    
	    /**
	     * 查询品牌
	     * @return
	     * @throws IOException
	     */
	    @RequestMapping(value = "/rest/getBrandList", method = RequestMethod.GET)
	    public Response getBrandList(Brand brand) throws IOException {
	    	brand.setStatus(1);
	        List<Brand> brandList = brandMapper.searchBrandByStatus(brand);
	        Response result = new Response();
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
	    public Response querySCateListByBrandId(Product product) throws IOException {
	    	Response response = new Response();
	        response = productService.querySCateListByBrandId(product);

			return response;
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
		public Response queryProductInfoBySCategory(ProductWithBLOBs product) throws IOException
		{
			Response response = new Response();
			Map<String,Object> productMap = new HashMap<String,Object>();
			productMap.put("scateid",product.getScateid());
			productMap.put("brandid",product.getBrandid());
			productMap.put("status", Constants.product_status_publish);
			productMap.put("count", Constants.brand_page_product_count);
			productMap.put("order", "publishTime");
	        response = productService.queryProductInfoBySCategory(productMap);

			return response;
		}
		
		/**
		 * 产品推荐信息，根据点击率排序
		 * @throws IOException
		 * @throws JsonMappingException 
		 * @throws JsonGenerationException 
		 */
		@RequestMapping(value="/rest/productDetail/queryRecommendProduct",method = RequestMethod.GET)
		@ResponseBody
		public Response queryRecommendProduct() throws IOException
		{
			Response response = new Response();
			Map<String,Object> productMap = new HashMap<String,Object>();
			productMap.put("status", Constants.product_status_publish);
			productMap.put("count", Constants.recommend_product_count);
			productMap.put("order", "hit");
	        response = productService.queryRecommendHotProduct(productMap);

			return response;
		}
		
		/**
		 * 查询热点产品
		 * @throws IOException
		 * @throws JsonMappingException 
		 * @throws JsonGenerationException 
		 */
		@RequestMapping(value="/rest/productDetail/queryHotProduct",method = RequestMethod.GET)
		@ResponseBody
		public Response queryHotProduct() throws IOException
		{
			Response response = new Response();
			Map<String,Object> productMap = new HashMap<String,Object>();
			productMap.put("status", Constants.product_status_publish);
			productMap.put("count", Constants.hot_product_count);
			productMap.put("order", "hit");
	        response = productService.queryRecommendHotProduct(productMap);

			return response;
		}
}

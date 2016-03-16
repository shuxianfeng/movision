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
import org.springframework.web.bind.annotation.RestController;

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
	public void addProduct(HttpServletRequest req,HttpServletResponse response,ProductWithBLOBs product) throws JsonGenerationException, JsonMappingException, IOException
	{
		//productService.constructProduct(product);
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
        List<Product>  users = productService.findAllByPager(pager,product);
        pager.result(users);
        jsonResult.setData(pager);
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	  @RequestMapping(value = "/rest/getProductFirstCategory", method = RequestMethod.GET)
	    public void getProductFirstCategory(HttpServletRequest req, HttpServletResponse response) throws IOException {
	        List<ResultBean> systemList = categoryMapper.findSystemList();
	        response.setContentType("application/json;charset=utf-8");
	        response.getWriter().write(JsonUtils.getJsonStringFromObj(systemList));
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
	        List<ResultBean> subSystemList = categoryMapper.findSubSystemList(parentId);
	        response.setContentType("application/json;charset=utf-8");
	        response.getWriter().write(JsonUtils.getJsonStringFromObj(subSystemList));
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
}

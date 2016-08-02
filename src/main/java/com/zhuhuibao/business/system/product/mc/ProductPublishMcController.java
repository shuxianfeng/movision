package com.zhuhuibao.business.system.product.mc;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.oms.entity.ComplainSuggest;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
import com.zhuhuibao.mybatis.oms.service.ComplainSuggestService;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 产品控制层
 * @author penglong
 *
 */
@RestController
@Api(value = "productPublishMc",description = "产品(个人中心)")
public class ProductPublishMcController {

	private static final Logger log = LoggerFactory.getLogger(ProductPublishMcController.class);

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
	 *
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@RequestMapping(value = {"/rest/addProduct", "/rest/system/mc/product/add_product"}, method = RequestMethod.POST)
	@ApiOperation(value = "新增产品", notes = "新增产品", response = Response.class)
	public Response addProduct(String json) throws IOException {
		Gson gson = new Gson();
		ProductWithBLOBs product = gson.fromJson(json, ProductWithBLOBs.class);
		Response response = new Response();
		productService.insertProduct(product);
		return response;
	}

	@RequestMapping(value = {"/rest/addComplainSuggest", "/rest/system/mc/product/add_complainSuggest"}, method = RequestMethod.POST)
	@ApiOperation(value = "找不到产品对应的类别新增投诉和建议", notes = "找不到产品对应的类别新增投诉和建议", response = Response.class)
	public Response addComplainSuggest(ComplainSuggest suggest) throws IOException {
		Response response = new Response();
		suggestService.insert(suggest);

		return response;
	}


	@RequestMapping(value = {"/rest/updateProduct", "/rest/system/mc/product/upd_product"}, method = RequestMethod.POST)
	@ApiOperation(value = "更新产品", notes = "更新产品", response = Response.class)
	public Response updateProduct(ProductWithBLOBs product) throws IOException {
		Response response = new Response();
		response = productService.updateProduct(product);

		return response;
	}

	@RequestMapping(value = {"/rest/updateProductStatus", "/rest/system/mc/product/upd_productStatus"}, method = RequestMethod.POST)
	@ApiOperation(value = "更新产品状态", notes = "更新产品状态", response = Response.class)
	public Response updateProductStatus(ProductWithBLOBs product) throws IOException {
		Response response = new Response();
		response = productService.updateProductStatus(product);
		return response;
	}

	@RequestMapping(value = {"/rest/batchUnpublish", "/rest/system/mc/product/upd_batchPublish"}, method = RequestMethod.POST)
	@ApiOperation(value = "批量更新产品状态", notes = "批量更新产品状态", response = Response.class)
	public Response batchUnpublish(@RequestParam String[] ids) throws IOException {
		Response response = new Response();
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, ids);
		response = productService.batchUnpublish(list);

		return response;
	}

	@RequestMapping(value={"/rest/queryProductById","/rest/system/mc/product/sel_productById"}, method = RequestMethod.GET)
	@ApiOperation(value = "获得产品信息根据ID", notes = "获得产品信息根据ID", response = Response.class)
	public Response queryProductById(Long id) throws IOException
	{
		Response response = new Response();
		response = productService.selectByPrimaryKey(id);

		return response;
	}

	/**
	 * 查询产品分页
	 *
	 * @param product
	 * @param pageNo
	 * @param pageSize
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@RequestMapping(value = {"/rest/findAllProduct", "/rest/system/mc/product/sel_allProduct"}, method = RequestMethod.GET)
	@ApiOperation(value = "我的产品", notes = "我的产品", response = Response.class)
	public Response findAllProduct(ProductWithBLOBs product, String pageNo, String pageSize) throws IOException {
		Response response = new Response();
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmpty(pageSize)) {
			pageSize = "10";
		}
		Paging<Product> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
		List<Product> productList = productService.findAllByPager(pager, product);
		pager.result(productList);
		response.setData(pager);

		return response;
	}

	@RequestMapping(value = {"/rest/getProductFirstCategory", "/rest/system/mc/product/sel_productFirstCategory"}, method = RequestMethod.GET)
	@ApiOperation(value = "系统一级分类", notes = "系统一级分类", response = Response.class)
	public Response getProductFirstCategory(HttpServletResponse response) throws IOException {
		Response jsonResult = new Response();
		List<ResultBean> systemList = categoryMapper.findSystemList();
		jsonResult.setData(systemList);
		return jsonResult;
	}

	/**
	 * 查询大系统下所有子系统类目
	 *
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = {"/rest/getProductSecondCategory", "/rest/system/mc/product/sel_productSecondCategory"}, method = RequestMethod.GET)
	@ApiOperation(value = "系统二级分类", notes = "系统二级分类", response = Response.class)
	public Response getProductSecondCategory(HttpServletRequest req) throws IOException {
		String parentId = req.getParameter("parentID");
		Response response = new Response();
		List<ResultBean> subSystemList = categoryMapper.findSubSystemList(parentId);
		response.setData(subSystemList);

		return response;
	}

	/**
	 * 查询品牌
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/getBrandList", "/rest/system/mc/product/sel_brandList"}, method = RequestMethod.GET)
	@ApiOperation(value = "查询品牌", notes = "查询品牌", response = Response.class)
	public Response getBrandList(Brand brand) throws IOException {
		brand.setStatus("1");
		List<Brand> brandList = brandMapper.searchBrandByStatus(brand);
		Response result = new Response();
		result.setCode(200);
		result.setData(brandList);

		return result;
	}
}
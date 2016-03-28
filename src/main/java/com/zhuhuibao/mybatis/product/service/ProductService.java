package com.zhuhuibao.mybatis.product.service;

import java.io.IOException;
import java.util.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.taobao.api.internal.util.StringUtils;
import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.MsgCodeConstant;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.oms.entity.Category;
import com.zhuhuibao.mybatis.oms.entity.CategoryAssemble;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
import com.zhuhuibao.mybatis.product.entity.ParamPrice;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.entity.ProductMap;
import com.zhuhuibao.mybatis.product.entity.ProductParam;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.product.mapper.ProductMapper;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 产品业务处理类
 * @author penglong
 *
 */
@Service
@Transactional
public class ProductService {
	private static final Logger log = LoggerFactory.getLogger(ProductService.class);
	
	@Autowired
	ProductMapper productMapper;
	
	@Autowired
	ProductParamService paramService;
	
	/**
	 * 插入产品
	 * @param product
	 */
	public int insertProduct(ProductWithBLOBs product)
	{
		log.info("insert product");
		int productId = 0;
		try
		{
			Map<String,Long> paramMap = paramService.insertParam(product);
			List<ParamPrice> paramPrice = product.getParamPrice();
			if(paramPrice!= null && !paramPrice.isEmpty())
			{
				String productName = product.getName();
				for(int i=0;i<paramPrice.size();i++)
				{
					StringBuilder ids_sb = new StringBuilder();
					StringBuilder value_sb = new StringBuilder();
					ParamPrice pp= paramPrice.get(i);
					ids_sb.append(paramMap.get(pp.getFname()));
					if(pp.getSname()!= null && pp.getSname().length() > 0 && paramMap.get(pp.getSname()) != null)
					{
						ids_sb.append(",");
						ids_sb.append(paramMap.get(pp.getSname()));
					}
					value_sb.append(pp.getFvalue());
					if(pp.getSvalue() != null && pp.getSvalue().length() > 0)
					{
						value_sb.append(",");
						value_sb.append(pp.getSvalue());
					}
					product.setName(productName+" "+pp.getFvalue()+" "+pp.getSvalue());
					product.setParamIDs(ids_sb.toString());
					product.setParamValues(value_sb.toString());
					product.setPrice(pp.getPrice());
					product.setRepository(pp.getRepository());
					product.setImgUrl(pp.getImgUrl());
					//用上传的参数图片
					productId = productMapper.insertSelective(product);
				}
			}
			else
			{
				productId = productMapper.insertSelective(product);
			}
		}
		catch(Exception e)
		{
			log.error("insert product error!",e);
		}
		return productId;
	}
	
	 /**
     * 分页查询所有用户
     * @param pager    分页属性
     * @return   user
     */
    public List<Product> findAllByPager(Paging<Product> pager,Product product) {
        log.debug("分页查询所有产品");
        return productMapper.findAllByPager(pager.getRowBounds(),product);
    }
    
    public JsonResult updateProduct(ProductWithBLOBs product)
    {
    	JsonResult jsonResult = new JsonResult();
    	log.info("update product");
    	try
    	{
    		if(product.getNumber() == null || product.getNumber().trim().equals(""))
    		{
    			product.setNumber("1");
    		}
    		productMapper.updateByPrimaryKeySelective(product);
    	}
    	catch(Exception e)
    	{
    		log.error("update product error",e);
    		jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
    	}
    	return jsonResult;
    }
    
    public JsonResult batchUnpublish(List<String> list)
    {
    	JsonResult jsonResult = new JsonResult();
    	log.info("update product");
    	try
    	{
    		productMapper.batchUnpublish(list);
    	}
    	catch(Exception e)
    	{
    		log.error("update product error",e);
    		jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
    	}
    	return jsonResult;
    }
    
    /**
     * 点击数加+1
     * @param id
     * @return
     */
    public JsonResult updateHit(Long id)
    {
    	JsonResult jsonResult = new JsonResult();
    	log.info("update product");
    	try
    	{
    		productMapper.updateHit(id);
    	}
    	catch(Exception e)
    	{
    		log.error("update product error",e);
    		jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
    	}
    	return jsonResult;
    }
    
    public JsonResult selectByPrimaryKey(Long id)
    {
    	JsonResult jsonResult = new JsonResult();
    	ProductWithBLOBs product = null;
    	try
    	{
    		product = productMapper.selectByPrimaryKey(id);
    		if(product.getParamIDs() != null && product.getParamIDs().length() > 0)
    		{
    			List<ProductParam> params = new ArrayList<ProductParam>();
    			String param = product.getParamIDs();
    			String paramValues = product.getParamValues();
    			Map<String,String> paramMap = null;
    			if(param.indexOf(",") > 0)
    			{
    				String[] arr_param = param.split(",");
    				String[] arr_paramValues = paramValues.split(",");
    				for(int i=0;i<arr_param.length;i++)
    				{
    					paramMap = new TreeMap<String,String>();
    					ProductParam pp = paramService.queryParamById(Long.parseLong(arr_param[i]));
    					if(pp != null)
    					{
    						pp.setPvalue(arr_paramValues[i]);
    						params.add(pp);
    					}
    				}
    			}
    			else
    			{
    				paramMap = new TreeMap<String,String>();
					ProductParam pp = paramService.queryParamById(Long.parseLong(param));
					if(pp != null)
					{
						paramMap.put("pvalue", paramValues);
						pp.setPvalue(paramValues);
						params.add(pp);
					}
    			}
    			product.setParams(params);
    		}
    		
    		jsonResult.setData(product);
    	}
    	catch(Exception e)
    	{
    		log.error("update product error",e);
    		jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
    	}
    	return jsonResult;
    }
    
    /**
     * 加入对象到集合列表中
     * @param a  集合对象
     * @param k  图片路径
     * @param v  skuid
     */
    private void addToImg(Map<String,List<Long>> a, String k, long v) {
    	List<Long> arr = (List<Long>)a.get(k);
    	if (arr == null) {
    		arr = new ArrayList<Long>();
    		arr.add(v);
    		a.put(k, arr);
    	}
    	else
    	{
    		arr.add(v);
    		a.put(k,arr);
    	}
    }
    
    /**
     * 产品详情页面中查询产品信息
     * @param id
     * @return
     */
    public JsonResult queryProductInfoById(Long id)
    {
    	JsonResult jsonResult = new JsonResult();
    	//保存参数值信息
    	Map<String,Object> paramValuesMap  = new TreeMap<String,Object>();
    	Map<String,Object> productMap = new TreeMap<String,Object>();
    	ProductWithBLOBs product = null;
    	try
    	{
    		product = productMapper.selectByPrimaryKey(id);
    		//组成参数列表
    		if(product.getParamIDs() != null && product.getParamIDs().length() > 0)
    		{
    			setProductParamInfo(paramValuesMap, productMap, product);
    			List<Product> productList = productMapper.queryProductByParamIDs(product.getParamIDs());
    			
    			if(!productList.isEmpty())
    			{
    				setProductImgs(productMap, productList);
    				setProductList(id, paramValuesMap, productMap, productList);
    			}
    		}
    		else
    		{
    			setSingleProductInfo(productMap, product);
    		}
    		jsonResult.setData(productMap);
    	}
    	catch(Exception e)
    	{
    		log.error("update product error",e);
    		jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
    	}
    	return jsonResult;
    }

    /**
     * 设置单个产品的信息，没有产品参数的情况
     * @param productMap
     * @param product
     */
	private void setSingleProductInfo(Map<String, Object> productMap,
			ProductWithBLOBs product) {
		Map<String,Object> navigationMap = new TreeMap<String,Object>();
		navigationMap.put(Constant.product_field_fcateid, product.getFcateid());
		navigationMap.put(Constant.product_field_fcateName,product.getFcateName());
		navigationMap.put(Constant.product_field_scateid, product.getScateid());
		navigationMap.put(Constant.product_field_scateName,product.getScateName());
		navigationMap.put(Constant.product_field_brandid, product.getBrandid());
		navigationMap.put(Constant.product_field_brandName, product.getCNName());
		productMap.put(Constant.product_field_navigation, navigationMap);
		//图片
		List<Map<String,Object>> imgList = new ArrayList<Map<String,Object>>();
		Map<String,List<Long>> imgsMap = new HashMap<String,List<Long>>();
		for (String s : product.getImgUrl().split(";")) {
			addToImg(imgsMap, s, product.getId());
			//a.put(s, p);
		}
		if(!imgsMap.isEmpty())
		{
			for(Map.Entry<String,List<Long>> entry:imgsMap.entrySet())
			{
				Map<String,Object> imgMap = new TreeMap<String,Object>();
				imgMap.put(Constant.product_field_skuid, entry.getValue());
				imgMap.put(Constant.product_field_imgUrl,entry.getKey());
				imgList.add(imgMap);
			}
		}
		productMap.put(Constant.product_field_imgs, imgList);
		List<Map<String,Object>> prdList = new ArrayList<Map<String,Object>>();
		Map<String,Object> prdMap = new TreeMap<String,Object>();
		Map<String,Object> prdInfoMap = new TreeMap<String,Object>();
		prdMap.put(Constant.product_field_skuid, product.getId());
		prdMap.put(Constant.product_field_name, product.getName());
		prdMap.put(Constant.product_field_price, product.getPrice());
		prdMap.put(Constant.product_field_number, product.getNumber());
		prdMap.put(Constant.product_field_defalut, new Boolean(true));
		prdInfoMap.put(Constant.product_field_k, "");
		prdInfoMap.put(Constant.product_field_v, prdMap);
		prdList.add(prdInfoMap);
		productMap.put(Constant.product_field_products, prdList);
		List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
		productMap.put(Constant.product_field_params, paramList);
	}

    /**
     * 设置由不同参数组成的产品集合
     * @param id
     * @param paramValuesMap
     * @param productMap
     * @param productList
     */
	private void setProductList(Long id, Map<String, Object> paramValuesMap,
			Map<String, Object> productMap, List<Product> productList) {
		//产品列表
		List<Map<String,Object>> prdList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<productList.size();i++)
		{
			Map<String,Object> prdInfoMap = new TreeMap<String,Object>();
			Map<String,Object> prdMap = new TreeMap<String,Object>();
			StringBuilder sbkey = new StringBuilder();
			Product prd = productList.get(i);
			prdMap.put(Constant.product_field_skuid, prd.getId());
			prdMap.put(Constant.product_field_name, prd.getName());
			prdMap.put(Constant.product_field_price, prd.getPrice());
			prdMap.put(Constant.product_field_number, prd.getNumber());
			if(id.equals(prd.getId()))
			{
				setNavigation(productMap, prdMap, prd);
			}
			String prdParamsIDS = prd.getParamIDs();
			String prdParamValues = prd.getParamValues();
			if(prdParamsIDS.indexOf(",")>0)
			{
				String[] arr_prdParamsIDS  = prdParamsIDS.split(",");
				String[] arr_prdParamValues = prdParamValues.split(",");
				for(int n=0;n<arr_prdParamsIDS.length;n++)
				{
					Map<String,Object> prdParamValuesMap = (Map<String, Object>) paramValuesMap.get(arr_prdParamsIDS[n]);
					sbkey.append(arr_prdParamsIDS[n]);
					sbkey.append(",");
					sbkey.append(prdParamValuesMap.get(arr_prdParamValues[n]));
					sbkey.append("|");
				}
			}
			else
			{
				Map<String,Object> prdParamValuesMap = (Map<String, Object>) paramValuesMap.get(prdParamValues);
				sbkey.append(prdParamsIDS);
				sbkey.append(",");
				sbkey.append(prdParamValuesMap.get(prdParamValues));
				sbkey.append("|");
			}
			String key = sbkey.delete(sbkey.lastIndexOf("|"), sbkey.length()).toString();
			prdInfoMap.put(Constant.product_field_k, key);
			prdInfoMap.put(Constant.product_field_v, prdMap);
			prdList.add(prdInfoMap);
		}
		productMap.put(Constant.product_field_products, prdList);
	}

	/**
	 * 设置导航信息(面包屑)
	 * @param productMap
	 * @param prdMap
	 * @param prd
	 */
	private void setNavigation(Map<String, Object> productMap,
			Map<String, Object> prdMap, Product prd) {
		prdMap.put(Constant.product_field_defalut, new Boolean(true));
		Map<String,Object> navigationMap = new TreeMap<String,Object>();
		navigationMap.put(Constant.product_field_fcateid, prd.getFcateid());
		navigationMap.put(Constant.product_field_fcateName,prd.getFcateName());
		navigationMap.put(Constant.product_field_scateid, prd.getScateid());
		navigationMap.put(Constant.product_field_scateName,prd.getScateName());
		navigationMap.put(Constant.product_field_brandid, prd.getBrandid());
		navigationMap.put(Constant.product_field_brandName, prd.getCNName());
		productMap.put(Constant.product_field_navigation, navigationMap);
	}

    /**
     *设置产品的图片集合信息
     * @param productMap
     * @param productList
     */
	private void setProductImgs(Map<String, Object> productMap,
			List<Product> productList) {
		Map<String,List<Long>> a = new HashMap<String,List<Long>>();
		for (Product p : productList) {
			
			for (String s : p.getImgUrl().split(";")) {
				addToImg(a, s, p.getId());
				//a.put(s, p);
			}
		}
		List<Map<String,Object>> imgList = new ArrayList<Map<String,Object>>();
		if(!a.isEmpty())
		{
			for(Map.Entry<String,List<Long>> entry:a.entrySet())
			{
				Map<String,Object> imgsMap = new TreeMap<String,Object>();
				imgsMap.put(Constant.product_field_skuid, entry.getValue());
				imgsMap.put(Constant.product_field_imgUrl,entry.getKey());
				imgList.add(imgsMap);
			}
		}
		productMap.put(Constant.product_field_imgs, imgList);
	}

    /**
     * 设置产品参数信息
     * @param paramValuesMap
     * @param productMap
     * @param product
     */
	private void setProductParamInfo(Map<String, Object> paramValuesMap,
			Map<String, Object> productMap, ProductWithBLOBs product) {
		List<ProductParam> params = getParamsInfo(product);
		List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
		if(!params.isEmpty())
		{
			for(ProductParam p : params)
			{
				Map<String,Object> paramMap  = new TreeMap<String,Object>();
				Map<String,Object> paramValueMap  = new TreeMap<String,Object>();
				paramMap.put(Constant.product_field_key, p.getId());
				paramMap.put(Constant.product_field_value,p.getPname());
				String pValue = p.getPvalue();
				String[] arr_pValue = null;
				if(pValue.indexOf(",") > 0)
				{
					//参数值数组
					arr_pValue = pValue.split(",");
					for(int i = 0;i<arr_pValue.length;i++)
					{
						paramValueMap.put(arr_pValue[i],String.valueOf(i));
					}
				}
				else
				{
					arr_pValue = new String[1];
					arr_pValue[0] = pValue;
					paramValueMap.put(pValue, "0");
				}
				paramMap.put(Constant.product_field_values,arr_pValue);
				paramValuesMap.put(String.valueOf(p.getId()), paramValueMap);
				paramList.add(paramMap);
			}
		}
		productMap.put(Constant.product_field_params, paramList);
	}

	/**
	 * 获得产品参数信息
	 * @param product
	 * @return
	 */
	private List<ProductParam> getParamsInfo(ProductWithBLOBs product) {
		List<ProductParam> params = new ArrayList<ProductParam>();
		String param = product.getParamIDs();
		
		//多个参数的情况
		if(param.indexOf(",") > 0)
		{
			String[] arr_param = param.split(",");
			List<Integer> paramList = new ArrayList<Integer>();
			for(int i=0;i<arr_param.length;i++)
			{
				paramList.add(Integer.parseInt(arr_param[i]));
			}
			params = paramService.selectParamByIds(paramList);
		}//一个产品没有参数情况
		else
		{
			ProductParam pp = paramService.queryParamById(Long.parseLong(param));
			params.add(pp);
		}
		return params;
	}
    
    /**
     * 获得此品牌所属的二级系统
     * @param product
     * @return
     */
    public JsonResult querySCateListByBrandId(Product product)
    {
    	JsonResult jsonResult = new JsonResult();
    	List<ResultBean> scategoryList = null;
    	try
    	{
    		scategoryList = productMapper.getSCateListByBrandId(product);
    		jsonResult.setData(scategoryList);
    	}
    	catch(Exception e)
    	{
    		log.error("update product error",e);
    		jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
    	}
    	return jsonResult;
    }
    
    /**
     * 根据二级分类和品牌查询所有产品
     * @param product
     * @return
     */
    public JsonResult queryProductInfoBySCategory(Map<String,Object> product)
    {
    	JsonResult jsonResult = new JsonResult();
    	List<ProductWithBLOBs> productList = null;
    	try
    	{
    		productList = productMapper.queryProductInfoBySCategory(product);
    		jsonResult.setData(productList);
    	}
    	catch(Exception e)
    	{
    		log.error("update product error",e);
    		jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
    	}
    	return jsonResult;
    }
    
    /**
     * 根据二级分类和品牌查询所有产品
     * @param product
     * @return
     */
    public JsonResult queryRecommendHotProduct(Map<String,Object> product)
    {
    	JsonResult jsonResult = new JsonResult();
    	List<ProductMap> productList = null;
    	try
    	{
    		productList = productMapper.queryRecommendHotProduct(product);
    		jsonResult.setData(productList);
    	}
    	catch(Exception e)
    	{
    		log.error("update product error",e);
    		jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
    	}
    	return jsonResult;
    }
    
    /*public JsonResult assembleSysCategory() throws JsonGenerationException, JsonMappingException, IOException
    {
    	JsonResult result = new JsonResult();
    	List<CategoryAssemble> categoryAssembleList = productMapper.findCategoryAssemble();
    	//List<Map<String,Object>> categroyList = new ArrayList<Map<String,Object>>();
    	Map<String,Object> categoryMap = new TreeMap<String,Object>();
    	List<Map<String,Object>> fcateList = new ArrayList<Map<String,Object>>();
    	if(!categoryAssembleList.isEmpty())
    	{
    		List<Map<String,Object>> scateList = new ArrayList<Map<String,Object>>();
    		Map<String,Object> fcateMap = new TreeMap<String,Object>();
    		for(CategoryAssemble cate : categoryAssembleList)
    		{
    			if(fcateMap.get("fcateid")!= null && fcateMap.get("fcateid").equals(cate.getFcateid()))
    			{
    				Map<String,Object> scateMap = new TreeMap<String,Object>();
    				scateMap = new TreeMap<String,Object>();
					scateMap.put("scateid", cate.getScateid());
        			scateMap.put("sname",cate.getSname());
        			scateList.add(scateMap);
    			}
    			else
    			{
    				if(fcateMap.size() > 0)
    				{
    					fcateMap.put("list",scateList);
    					fcateList.add(fcateMap);
    				}
    				fcateMap = new TreeMap<String,Object>();
    				scateList = new ArrayList<Map<String,Object>>();
    				fcateMap.put("fcateid", cate.getFcateid());
        			fcateMap.put("fname", cate.getFname());
        			fcateMap.put("smallIcon", cate.getSmallIcon());
        			
        			Map<String,Object> scateMap = new TreeMap<String,Object>();
    				scateMap.put("scateid", cate.getScateid());
        			scateMap.put("sname",cate.getSname());
        			scateList.add(scateMap);
    			}
    		}
    		if(fcateMap.size() > 0)
			{
    			fcateMap.put("list",scateList);
				fcateList.add(fcateMap);
			}
    	}
    	List<CategoryAssemble> sencondCategoryList = productMapper.findSecondCategoryBrand();
    	if(!sencondCategoryList.isEmpty())
    	{
    		for(Map<String,Object> fmap : fcateList)
    		{
    			List<Map<String,Object>> scateList = (List<Map<String, Object>>) fmap.get("list");
    			for(Map<String,Object> smap : scateList)
    			{
    				List<Map<String,Object>> brandMapList = new ArrayList<Map<String,Object>>();
    				for(CategoryAssemble sbrand: sencondCategoryList)
    				{
    					if(smap.get("scateid").equals(sbrand.getScateid()))
    					{
    						Map<String,Object> brandMap = new TreeMap<String,Object>();
    						brandMap.put("brandid", sbrand.getBrandid());
    						brandMap.put("brandCNName", sbrand.getBrandCNName());
    						brandMap.put("brandENName", sbrand.getBrandENName());
    						brandMapList.add(brandMap);
    					}
    				}
    				smap.put("brand", brandMapList);
    			}
    		}
    	}
    	log.info(JsonUtils.getJsonStringFromObj(fcateList));
		result.setData(categoryMap);
    	return result;
    }*/
    
    public JsonResult queryPrdDescParamService(Long id)
    {
    	JsonResult jsonResult = new JsonResult();
    	ProductWithBLOBs product = null;
    	try
    	{
    		product = productMapper.selectByPrimaryKey(id);
    		Map<String,Object> map = new TreeMap<String,Object>();
    		map.put(Constant.product_field_id,product.getId());
    		map.put(Constant.product_field_detailDesc, product.getDetailDesc());
    		map.put(Constant.product_field_paras, product.getParas());
    		map.put(Constant.product_field_service, product.getService());
    		jsonResult.setData(map);
    	}
    	catch(Exception e)
    	{
    		log.error("query product detail param servie error!",e);
    		jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
    	}
    	return jsonResult;
    }
	
	/**
	 * 查询品牌对应的子系统
	 */
	public List<ResultBean> findSubSystem(String id)
	{
		log.debug("查询品牌对应的子系统");
		List<ResultBean> list = productMapper.findSubSystem(id);
		return list;
	}
}

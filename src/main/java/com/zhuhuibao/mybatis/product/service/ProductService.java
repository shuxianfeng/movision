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
    			List<ProductParam> params = new ArrayList<ProductParam>();
    			String param = product.getParamIDs();
    			
    			//相同参数产生多个产品
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
    			List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
    			if(!params.isEmpty())
    			{
    				for(ProductParam p : params)
    				{
    					Map<String,Object> paramMap  = new TreeMap<String,Object>();
    					Map<String,Object> paramValueMap  = new TreeMap<String,Object>();
    					paramMap.put("key", p.getId());
    					paramMap.put("value",p.getPname());
    					String pValue = p.getPvalue();
    					String[] arr_pValue = null;
    					if(pValue.indexOf(",") > 0)
    					{
    						//参数值数组
    						arr_pValue = pValue.split(",");
    						paramValueMap.put(arr_pValue[0],"0");
    						paramValueMap.put(arr_pValue[1], "1");
    					}
    					else
    					{
    						arr_pValue = new String[1];
    						arr_pValue[0] = pValue;
    						paramValueMap.put(pValue, "0");
    					}
    					paramMap.put("values",arr_pValue);
    					paramValuesMap.put(String.valueOf(p.getId()), paramValueMap);
    					paramList.add(paramMap);
    				}
    			}
    			productMap.put("params", paramList);
    			List<Product> productList = productMapper.queryProductByParamIDs(product.getParamIDs());
    			if(!productList.isEmpty())
    			{
    				//产品图片
    				List<Map<String,Object>> imgList = new ArrayList<Map<String,Object>>();
    				for(Product prd : productList)
    				{
    					boolean flag = true;
    					for(Map<String,Object> img : imgList)
    					{
    						if(img.get("imgUrl").equals(prd.getImgUrl()))
    						{
    							Long[] arr_skuid = (Long[]) img.get("skuid");
    							Long[] s_arr_skuid = new Long[arr_skuid.length+1];
    							System.arraycopy(arr_skuid, 0, s_arr_skuid, 0, arr_skuid.length);
    							s_arr_skuid[s_arr_skuid.length-1] = prd.getId();
    							img.put("skuid", s_arr_skuid);
    							flag = false;
    						}
    					}
    					if(flag)
    					{
	    					Map<String,Object> imgsMap = new TreeMap<String,Object>();
	    					Long[]  skuid = {prd.getId()};
	    					imgsMap.put("skuid", skuid);
	    					imgsMap.put("imgUrl",prd.getImgUrl());
	    					imgList.add(imgsMap);
    					}
    				}
    				productMap.put("imgs", imgList);
    				//产品列表
    				List<Map<String,Object>> prdList = new ArrayList<Map<String,Object>>();
    				for(int i=0;i<productList.size();i++)
    				{
    					Map<String,Object> prdInfoMap = new TreeMap<String,Object>();
    					Map<String,Object> prdMap = new TreeMap<String,Object>();
    					StringBuilder sbkey = new StringBuilder();
    					Product prd = productList.get(i);
    					prdMap.put("skuid", prd.getId());
    					prdMap.put("name", prd.getName());
    					prdMap.put("price", prd.getPrice());
    					prdMap.put("number", prd.getNumber());
    					if(id == prd.getId())
    					{
    						prdMap.put("defalut", new Boolean(true));
    						Map<String,Object> navigationMap = new TreeMap<String,Object>();
    						navigationMap.put("fcateid", prd.getFcateid());
    						navigationMap.put("fcateName",prd.getFcateName());
    						navigationMap.put("scateid", prd.getScateid());
    						navigationMap.put("scateName",prd.getScateName());
    						navigationMap.put("brandid", prd.getBrandid());
    						navigationMap.put("brandName", prd.getCNName());
    						productMap.put("navigation", navigationMap);
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
    					prdInfoMap.put("k", key);
    					prdInfoMap.put("v", prdMap);
    					prdList.add(prdInfoMap);
					}
    				productMap.put("products", prdList);
    			}
    		}
    		else
    		{
    			Map<String,Object> navigationMap = new TreeMap<String,Object>();
				navigationMap.put("fcateid", product.getFcateid());
				navigationMap.put("fcateName",product.getFcateName());
				navigationMap.put("scateid", product.getScateid());
				navigationMap.put("scateName",product.getScateName());
				navigationMap.put("brandid", product.getBrandid());
				navigationMap.put("brandName", product.getCNName());
				productMap.put("navigation", navigationMap);
				List<Map<String,Object>> imgList = new ArrayList<Map<String,Object>>();
				Map<String,Object> imgsMap = new TreeMap<String,Object>();
				Long[]  skuid = {product.getId()};
				imgsMap.put("skuid", skuid);
				imgsMap.put("imgUrl",product.getImgUrl());
				imgList.add(imgsMap);
				productMap.put("imgs", imgList);
				List<Map<String,Object>> prdList = new ArrayList<Map<String,Object>>();
				Map<String,Object> prdMap = new TreeMap<String,Object>();
				Map<String,Object> prdInfoMap = new TreeMap<String,Object>();
				prdMap.put("skuid", product.getId());
				prdMap.put("name", product.getName());
				prdMap.put("price", product.getPrice());
				prdMap.put("number", product.getNumber());
				prdInfoMap.put("k", "");
				prdInfoMap.put("v", prdMap);
				prdList.add(prdInfoMap);
				productMap.put("products", prdList);
				List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
				productMap.put("params", paramList);
				
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
    
    public JsonResult assembleSysCategory() throws JsonGenerationException, JsonMappingException, IOException
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
    }
    
    public JsonResult queryPrdDescParamService(Long id)
    {
    	JsonResult jsonResult = new JsonResult();
    	ProductWithBLOBs product = null;
    	try
    	{
    		product = productMapper.selectByPrimaryKey(id);
    		Map<String,Object> map = new TreeMap<String,Object>();
    		map.put("id",product.getId());
    		map.put("detailDesc", product.getDetailDesc());
    		map.put("paras", product.getParas());
    		map.put("service", product.getService());
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
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException 
	{
		Gson gson = new Gson();
		String[] a = { "3", "3", "4", "23423", "23232" };
		String str ="72";
		String[] arr = str.split(",");
//		System.out.println(gson.toJson(a));
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

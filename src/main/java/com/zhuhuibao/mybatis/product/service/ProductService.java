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
    private CategoryMapper categoryMapper;
	
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
					product.setStatus(0);
					product.setName(productName+" "+pp.getFvalue()+" "+pp.getSvalue());
					product.setParamIDs(ids_sb.toString());
					product.setParamValues(value_sb.toString());
					product.setPrice(pp.getPrice());
					product.setRepository(pp.getRepository());
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
    
    public JsonResult selectByPrimaryKey(Long id)
    {
    	JsonResult jsonResult = new JsonResult();
    	ProductWithBLOBs product = null;
    	try
    	{
    		product = productMapper.selectByPrimaryKey(id);
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
    
    public JsonResult queryProductByParam(ProductWithBLOBs product)
    {
    	JsonResult jsonResult = new JsonResult();
    	ProductWithBLOBs _product = null;
    	try
    	{
    		_product = productMapper.queryProductByParam(product);
    		jsonResult.setData(_product);
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
     * //二级分类
    			Map<String,Object> scateMap = new HashMap<String,Object>();
    			scateMap.put("scateid", cate.getScateid());
    			scateMap.put("sname",cate.getSname());
    			List<Map<String,Object>> sCateList = new ArrayList<Map<String,Object>>();
    			//品牌
    			if(cate.getBrandid() != null)
    			{
	    			Map<String,Object> brandMap = new HashMap<String,Object>();
	    			brandMap.put("brandid", cate.getBrandid());
	    			brandMap.put("brandCNName", cate.getBrandCNName());
	    			brandMap.put("brandENName", cate.getBrandENName());
    			}
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public JsonResult assembleSysCategory() throws JsonGenerationException, JsonMappingException, IOException
    {
    	JsonResult result = new JsonResult();
    	List<CategoryAssemble> categoryAssembleList = categoryMapper.findCategoryAssemble();
    	//List<Map<String,Object>> categroyList = new ArrayList<Map<String,Object>>();
    	Map<String,Object> categoryMap = new TreeMap<String,Object>();
    	if(!categoryAssembleList.isEmpty())
    	{
    		Map<String,Object> fcateMap = new TreeMap<String,Object>();
    		Map<String,Object> allScateMap = new TreeMap<String,Object>();
    		
    		Map<String,Object> scateMap = new TreeMap<String,Object>();
    		Map<String,Object> brandMap = new TreeMap<String,Object>();
    		for(CategoryAssemble cate : categoryAssembleList)
    		{
    			if(fcateMap.get("fcateid")!= null && fcateMap.get("fcateid").equals(cate.getFcateid()))
    			{
	    			fcateMap.put("fcateid", cate.getFcateid());
	    			fcateMap.put("fname", cate.getFname());
	    			fcateMap.put("smallIcon", cate.getSmallIcon());
    				if(scateMap.get("scateid").equals(cate.getScateid()))
    				{
    					if(cate.getBrandid() != null)
    	    			{
    						brandMap = new TreeMap<String,Object>();
    		    			brandMap.put("brandid", cate.getBrandid());
    		    			brandMap.put("brandCNName", cate.getBrandCNName());
    		    			brandMap.put("brandENName", cate.getBrandENName());
    		    			scateMap.put("brand", brandMap);
    	    			}
    				}
    				else
    				{
    					scateMap.put("scateid", cate.getScateid());
            			scateMap.put("sname",cate.getSname());
    					if(cate.getBrandid() != null)
    	    			{
    						brandMap = new TreeMap<String,Object>();
    		    			brandMap.put("brandid", cate.getBrandid());
    		    			brandMap.put("brandCNName", cate.getBrandCNName());
    		    			brandMap.put("brandENName", cate.getBrandENName());
    		    			scateMap.put("brand", brandMap);
    	    			}
    					
    					
    				}
    			}
    			else
    			{
    				if(fcateMap.size() > 0)
    				{
    					categoryMap.put(cate.getFcateid(), fcateMap);
    				}
    				if(scateMap.size() > 0)
    				{
    					fcateMap.put("second",scateMap );
    				}
    				fcateMap = new TreeMap<String,Object>();
    				fcateMap.put("fcateid", cate.getFcateid());
        			fcateMap.put("fname", cate.getFname());
        			fcateMap.put("smallIcon", cate.getSmallIcon());
        			
        			scateMap = new TreeMap<String,Object>();
    				scateMap.put("scateid", cate.getScateid());
        			scateMap.put("sname",cate.getSname());
    				
					if(cate.getBrandid() != null)
	    			{
						brandMap = new TreeMap<String,Object>();
		    			brandMap.put("brandid", cate.getBrandid());
		    			brandMap.put("brandCNName", cate.getBrandCNName());
		    			brandMap.put("brandENName", cate.getBrandENName());
		    			scateMap.put("brand", brandMap);
	    			}
    			}
    		}
    		/*if(fcateMap.size() > 0)
			{
				categoryMap.put(cate.getFcateid(), fcateMap);
			}
			if(scateMap.size() > 0)
			{
				fcateMap.put("second",scateMap );
			}*/
    		log.info(JsonUtils.getJsonStringFromObj(categoryMap));
    		result.setData(categoryMap);
    	}
    	return result;
    }
    
    public JsonResult aaaaa() throws JsonGenerationException, JsonMappingException, IOException
    {
    	JsonResult result = new JsonResult();
    	List<CategoryAssemble> categoryAssembleList = categoryMapper.findCategoryAssemble();
    	//List<Map<String,Object>> categroyList = new ArrayList<Map<String,Object>>();
    	Map<String,Object> categoryMap = new TreeMap<String,Object>();
    	if(!categoryAssembleList.isEmpty())
    	{
    		List<Map<String,Object>> fcateList = new ArrayList<Map<String,Object>>();
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
    		log.info(JsonUtils.getJsonStringFromObj(fcateList));
    		result.setData(categoryMap);
    	}
    	return result;
    }
    
    /**
     * 系统频道获得推荐品牌
     * @return
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public JsonResult systemRecommentdBrand(Integer fcateid) throws JsonGenerationException, JsonMappingException, IOException
    {
    	JsonResult result = new JsonResult();
		List<Category> secondCategoryList = categoryMapper.findSecondSystemList(fcateid);
		List<Map<String,Object>> sCateList = new ArrayList<Map<String,Object>>();
		for(Category sCate : secondCategoryList)
		{
			Map<String,Object> sCateMap = new HashMap<String,Object>();
			List<Map<String,Object>> brandMapList = new ArrayList<Map<String,Object>>();
			List<Brand> brandList = productMapper.queryBrandBySecondCategory(sCate.getId());
			if(!brandList.isEmpty())
			{
				for(Brand brand : brandList)
				{
					Map<String,Object> brandMap = new HashMap<String,Object>();
					brandMap.put("id",brand.getId());
					brandMap.put("brandCNName", brand.getBrandCNName());
					brandMap.put("brandENName", brand.getBrandENName());
					brandMapList.add(brandMap);
				}
			}
			sCateMap.put("id", sCate.getId());
			sCateMap.put("name", sCate.getName());
			sCateMap.put("brand",brandMapList);
			sCateList.add(sCateMap);
		}
		log.info(JsonUtils.getJsonStringFromObj(sCateList));
		result.setData(sCateList);
    	return result;
    
    }
	
	public void constructProduct(ProductWithBLOBs product)
	{
		List<ParamPrice> paramPriceList = new ArrayList<ParamPrice>();
		
		ParamPrice pp = new ParamPrice();
		pp.setFname("材质");
		pp.setFvalue("无氧铜线");
		pp.setSname("是否屏蔽");
		pp.setSvalue("屏蔽");
		pp.setPrice(3.0);
		pp.setRepository(300.0);
		pp.setImgUrl("网线.png;234.png;345.png");
		ParamPrice pp2 = new ParamPrice();
		pp2.setFname("材质");
		pp2.setFvalue("无氧铜线");
		pp2.setSname("是否屏蔽");
		pp2.setSvalue("非屏蔽");
		pp2.setPrice(3.0);
		pp2.setRepository(300.0);
		pp2.setImgUrl("光缆.png;234.png;345.png");
		paramPriceList.add(pp);
		paramPriceList.add(pp2);
		
		
		ParamPrice pp3 = new ParamPrice();
		pp3.setFname("材质");
		pp3.setFvalue("紫铜线");
		pp3.setSname("是否屏蔽");
		pp3.setSvalue("屏蔽");
		pp3.setPrice(3.0);
		pp3.setRepository(30.00);
		pp3.setImgUrl("");
		ParamPrice pp4 = new ParamPrice();
		pp4.setFname("材质");
		pp4.setFvalue("紫铜线");
		pp4.setSname("是否屏蔽");
		pp4.setSvalue("非屏蔽");
		pp4.setPrice(3.0);
		pp4.setRepository(300.00);
		pp4.setImgUrl("电线.png;234.png;345.png");
		paramPriceList.add(pp3);
		paramPriceList.add(pp4);
		
		
		product.setParamPrice(paramPriceList);
		
		List<ProductParam> params = new ArrayList<ProductParam>();
		ProductParam param = new ProductParam();
		param.setPname("材质");
		param.setPvalue("无氧铜线,紫铜线");
		ProductParam param1 = new ProductParam();
		param1.setPname("是否屏蔽");
		param1.setPvalue("屏蔽,非屏蔽");
		params.add(param);
		params.add(param1);
		product.setParams(params);
	}
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		 String json = "{\"createid\":1,\"name\":\"1\",\"unit\":\"1\",\"number\":\"1\",\"price\":\"1\",\"repository\":\"1\",\"imgUrl\":\"\",\"detailDesc\":\"<p>11</p>\",\"paras\":\"<p>22</p>\",\"service\":\"<p>33</p>\",\"params\":[{\"pname\":\"A\",\"pvalue\":\"A1，A2\"}],\"paramPrice\":[{\"fname\":\"A\",\"fvalue\":\"A1\",\"sname\":\"\",\"svalue\":\"\",\"price\":\"1\",\"repository\":\"11\",\"imgUrl\":\"\"},{\"fname\":\"A\",\"fvalue\":\"A2\",\"sname\":\"\",\"svalue\":\"\",\"price\":\"2\",\"repository\":\"22\",\"imgUrl\":\"\"}]}";
		 
		 Gson gson = new Gson();
			ProductWithBLOBs product = gson.fromJson(json, ProductWithBLOBs.class);
			System.out.println(product.getParamPrice().get(0).getFname());
	}
}

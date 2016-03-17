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

import com.taobao.api.internal.util.StringUtils;
import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.MsgCodeConstant;
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
					ids_sb.append(",");
					ids_sb.append(paramMap.get(pp.getSname()));
					value_sb.append(pp.getFvalue());
					value_sb.append(",");
					value_sb.append(pp.getSvalue());
					product.setName(productName+" "+pp.getFvalue()+" "+pp.getSvalue());
					product.setParamIDs(ids_sb.toString());
					product.setParamValues(value_sb.toString());
					product.setPrice(pp.getPrice());
					product.setRepository(pp.getRepository());
					//用上传的参数图片
					String imgUrl =  pp.getImgUrl();
					if(imgUrl != null && imgUrl.length() > 0)
					{
						String[] arr_str = imgUrl.split(";");
						StringBuilder sb = new StringBuilder();
						for(String img : arr_str)
						{
							sb.append(Constant.upload_img_prifix);
							sb.append(img);
							sb.append(";");
						}
						product.setImgUrl(sb.delete(sb.lastIndexOf(";"),sb.length()).toString());
					}
					else
					{
						product.setImgUrl(null);
					}
					productId = productMapper.insertSelective(product);
				}
			}
			else
			{
				String imgUrl =  product.getImgUrl();
				if(imgUrl != null && imgUrl.length() > 0)
				{
					String[] arr_str = imgUrl.split(";");
					StringBuilder sb = new StringBuilder();
					for(String img : arr_str)
					{
						sb.append(Constant.upload_img_prifix);
						sb.append(img);
						sb.append(";");
					}
					product.setImgUrl(sb.delete(sb.lastIndexOf(";"),sb.length()).toString());
				}
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
    	jsonResult.setData(_product);
    	return jsonResult;
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
		ProductWithBLOBs product = new ProductWithBLOBs();
		List<ParamPrice> paramPriceList = new ArrayList<ParamPrice>();
		
		ParamPrice pp = new ParamPrice();
		pp.setFname("材质");
		pp.setFvalue("无氧铜线");
		pp.setSname("是否屏蔽");
		pp.setSvalue("屏蔽");
		pp.setPrice(3.0);
		pp.setRepository(300.0);
		pp.setImgUrl("123.png");
		ParamPrice pp2 = new ParamPrice();
		pp2.setFname("材质");
		pp2.setFvalue("无氧铜线");
		pp2.setSname("是否屏蔽");
		pp2.setSvalue("非屏蔽");
		pp2.setPrice(3.0);
		pp2.setRepository(300.0);
		pp2.setImgUrl("123.png");
		paramPriceList.add(pp);
		paramPriceList.add(pp2);
		
		
		ParamPrice pp3 = new ParamPrice();
		pp3.setFname("材质");
		pp3.setFvalue("无氧铜线");
		pp3.setSname("是否屏蔽");
		pp3.setSvalue("屏蔽");
		pp3.setPrice(3.0);
		pp3.setRepository(30.00);
		pp3.setImgUrl("123.png");
		ParamPrice pp4 = new ParamPrice();
		pp4.setFname("材质");
		pp4.setFvalue("无氧铜线");
		pp4.setSname("是否屏蔽");
		pp4.setSvalue("非屏蔽");
		pp4.setPrice(3.0);
		pp4.setRepository(300.00);
		pp4.setImgUrl("123.png");
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
		
		
		/*ProductService service = new ProductService();
		Map<String,Integer> paramMap = service.insertParam(product);*/
		
		
		System.out.println(JsonUtils.getJsonStringFromObj(product));
	}
}

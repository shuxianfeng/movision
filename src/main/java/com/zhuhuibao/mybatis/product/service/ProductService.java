package com.zhuhuibao.mybatis.product.service;

import java.util.*;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.service.MemShopService;
import com.zhuhuibao.mybatis.product.entity.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.product.mapper.ProductMapper;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 产品业务处理类
 *
 * @author penglong
 */
@Service
@Transactional
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductParamService paramService;

    @Autowired
    MemShopService memShopService;

    /**
     * 插入产品
     *
     * @param product
     */
    public int insertProduct(ProductWithBLOBs product) {
        int productId = 0; 
        try {
            Map<String, Long> paramMap = paramService.insertParam(product);
            List<ParamPrice> paramPrice = product.getParamPrice();
            //product.setStatus(Constants.product_status_nocheck);
            if (paramPrice != null && !paramPrice.isEmpty()) {
                String productName = product.getName();
                for (ParamPrice aParamPrice : paramPrice) {
                    StringBuilder ids_sb = new StringBuilder();
                    StringBuilder value_sb = new StringBuilder();
                    ParamPrice pp = aParamPrice;
                    ids_sb.append(paramMap.get(pp.getFname()));
                    if (pp.getSname() != null && pp.getSname().length() > 0 && paramMap.get(pp.getSname()) != null) {
                        ids_sb.append(",");
                        ids_sb.append(paramMap.get(pp.getSname()));
                    }
                    value_sb.append(pp.getFvalue());
                    if (pp.getSvalue() != null && pp.getSvalue().length() > 0) {
                        value_sb.append(",");
                        value_sb.append(pp.getSvalue());
                    }
                    product.setName(productName + " " + pp.getFvalue() + " " + pp.getSvalue());
                    product.setParamIDs(ids_sb.toString());
                    product.setParamValues(value_sb.toString());
                    product.setPrice(pp.getPrice());
                    product.setRepository(pp.getRepository());
                    product.setImgUrl(pp.getImgUrl());
                    //用上传的参数图片
                    productId = productMapper.insertSelective(product);
                }
            } else {
                productId = productMapper.insertSelective(product);
            }
        } catch (Exception e) {
            log.error("insert product error! {}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "系统异常");
        }
        return productId;
    }

    /**
     * 分页查询所有用户
     *
     * @param pager 分页属性
     * @return user
     */
    public List<Product> findAllByPager(Paging<Product> pager, Product product) {
        log.debug("分页查询所有产品");
        return productMapper.findAllByPager(pager.getRowBounds(), product);
    }

    public Response updateProduct(ProductWithBLOBs product) {
        Response response = new Response();
        try {
            if (product != null) {
                if (product.getNumber() == null || product.getNumber().trim().equals("")) {
                    product.setNumber("1");
                }
                if (product.getPrice() == null || product.getPrice().trim().equals("")) {
                    product.setPrice(Constants.product_price);
                }

               // product.setStatus(Constants.product_status_nocheck);

                productMapper.updateByPrimaryKeySelective(product);
            }
        } catch (Exception e) {
            log.error("select  error {}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新失败");
        }
        return response;
    }

    /**
     * 产品上下架
     *
     * @param product
     * @return
     */
    public Response updateProductStatus(ProductWithBLOBs product) {
        Response response = new Response();
        try {
            productMapper.updateProductStatus(product);
        } catch (Exception e) {
            log.error("update error {}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新失败");
        }
        return response;
    }

    public Response batchUnpublish(List<String> list) {
        Response response = new Response();
        try {
            productMapper.batchUnpublish(list);
        } catch (Exception e) {
            log.error("update  error", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新失败");
        }
        return response;
    }

    /**
     * 点击数加+1
     *
     * @param id
     * @return
     */
    public Response updateHit(Long id) {
        Response response = new Response();
        try {
            productMapper.updateHit(id);
        } catch (Exception e) {
            log.error("update  error", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新失败");
        }
        return response;
    }

    public Response selectByPrimaryKey(Long id) {
        Response response = new Response();
        ProductWithBLOBs product;
        try {
            product = productMapper.selectByPrimaryKey(id);
            if (product != null && product.getParamIDs() != null && product.getParamIDs().length() > 0) {
                List<ProductParam> params = new ArrayList<>();
                String param = product.getParamIDs();
                String paramValues = product.getParamValues();
                Map<String, String> paramMap;
                if (param.indexOf(",") > 0) {
                    String[] arr_param = param.split(",");
                    String[] arr_paramValues = paramValues.split(",");
                    for (int i = 0; i < arr_param.length; i++) {
                        paramMap = new TreeMap<>();
                        ProductParam pp = paramService.queryParamById(Long.parseLong(arr_param[i]));
                        if (pp != null) {
                            pp.setPvalue(arr_paramValues[i]);
                            params.add(pp);
                        }
                    }
                } else {
                    paramMap = new TreeMap<>();
                    ProductParam pp = paramService.queryParamById(Long.parseLong(param));
                    if (pp != null) {
                        paramMap.put("pvalue", paramValues);
                        pp.setPvalue(paramValues);
                        params.add(pp);
                    }
                }
                product.setParams(params);
            }

            response.setData(product);
        } catch (Exception e) {
            log.error("select error {}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return response;
    }

    /**
     * 加入对象到集合列表中
     *
     * @param a 集合对象
     * @param k 图片路径
     * @param v skuid
     */
    private void addToImg(Map<String, List<Long>> a, String k, long v) {
        List<Long> arr = a.get(k);
        if (arr == null) {
            arr = new ArrayList<>();
            arr.add(v);
            a.put(k, arr);
        } else {
            arr.add(v);
            a.put(k, arr);
        }
    }

    /**
     * 产品详情页面中查询产品信息
     *
     * @param id
     * @return
     */
    public Map<String, Object> queryProductInfoById(Long id) {
        //保存参数值信息
        Map<String, Object> paramValuesMap = new TreeMap<>();
        Map<String, Object> productMap = new TreeMap<>();
        ProductWithMember product;
        try {
            product = productMapper.selectProductMemberByid(id);
            if (product != null) {
                //组成参数列表
                if (product.getParamIDs() != null && product.getParamIDs().length() > 0) {
                    setMemberInfo(productMap, product);
                    setProductParamInfo(paramValuesMap, productMap, product);
                    List<Product> productList = productMapper.queryProductByParamIDs(product.getParamIDs());

                    if (!productList.isEmpty()) {
                        setProductImgs(productMap, productList);
                        setProductList(id, paramValuesMap, productMap, productList);
                    }
                } else {
                    setSingleProductInfo(productMap, product);
                }
            } else {
                log.error("产品不存在,id="+id);
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "产品不存在");
            }

        } catch (Exception e) {
            log.error("update product error {}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
        return productMap;
    }

    /**
     * 设置单个产品的信息，没有产品参数的情况
     *
     * @param productMap
     * @param product
     */
    private void setSingleProductInfo(Map<String, Object> productMap,
                                      ProductWithMember product) {
        //面包屑
        Map<String, Object> navigationMap = new TreeMap<String, Object>();
        navigationMap.put(Constants.product_field_fcateid, product.getFcateid());
        navigationMap.put(Constants.product_field_fcateName, product.getFcateName());
        navigationMap.put(Constants.product_field_scateid, product.getScateid());
        navigationMap.put(Constants.product_field_scateName, product.getScateName());
        navigationMap.put(Constants.product_field_brandid, product.getBrandid());
        navigationMap.put(Constants.product_field_brandName, product.getCNName());
        productMap.put(Constants.product_field_navigation, navigationMap);
        setMemberInfo(productMap, product);


        //图片
        List<Map<String, Object>> imgList = new ArrayList<Map<String, Object>>();
        Map<String, List<Long>> imgsMap = new HashMap<String, List<Long>>();
        for (String s : product.getImgUrl().split(";")) {
            addToImg(imgsMap, s, product.getId());
            //a.put(s, p);
        }
        if (!imgsMap.isEmpty()) {
            for (Map.Entry<String, List<Long>> entry : imgsMap.entrySet()) {
                Map<String, Object> imgMap = new TreeMap<String, Object>();
                imgMap.put(Constants.product_field_skuid, entry.getValue());
                imgMap.put(Constants.product_field_imgUrl, entry.getKey());
                imgList.add(imgMap);
            }
        }
        productMap.put(Constants.product_field_imgs, imgList);
        List<Map<String, Object>> prdList = new ArrayList<Map<String, Object>>();
        Map<String, Object> prdMap = new TreeMap<String, Object>();
        Map<String, Object> prdInfoMap = new TreeMap<String, Object>();
        prdMap.put(Constants.product_field_skuid, product.getId());
        prdMap.put(Constants.product_field_name, product.getName());
        prdMap.put(Constants.product_field_price, product.getPrice());
        prdMap.put(Constants.product_field_number, product.getNumber());
        prdMap.put(Constants.product_field_unit, product.getUnit());
        prdMap.put(Constants.product_field_defalut, new Boolean(true));
        prdInfoMap.put(Constants.product_field_k, "");
        prdInfoMap.put(Constants.product_field_v, prdMap);
        prdList.add(prdInfoMap);
        productMap.put(Constants.product_field_products, prdList);
        List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
        productMap.put(Constants.product_field_params, paramList);
    }

    /**
     * 查找会员相关信息
     *
     * @param productMap
     * @param product
     */
    private void setMemberInfo(Map<String, Object> productMap, ProductWithMember product) {
        //会员信息
        Map<String, Object> memberMap = new TreeMap<>();
        memberMap.put("identify", product.getIdentify());
        memberMap.put("memberName", product.getMemberName());
        memberMap.put("enterpriseLogo", product.getEnterpriseLogo());
        memberMap.put("enterpriseWebSite", product.getEnterpriseWebSite());
        memberMap.put("address", product.getAddress());
        memberMap.put("shopID", product.getMemberId());
        //查询企业商铺
        /* MemberShop shop =  memShopService.findByCompanyID(Long.valueOf(product.getMemberId()));
        if(shop != null){
            memberMap.put("shopID",shop.getId());
        } else{
            memberMap.put("shopID","");
        }*/

        productMap.put("member", memberMap);
    }

    /**
     * 设置由不同参数组成的产品集合
     *
     * @param id
     * @param paramValuesMap
     * @param productMap
     * @param productList
     */
    private void setProductList(Long id, Map<String, Object> paramValuesMap,
                                Map<String, Object> productMap, List<Product> productList) {
        //产品列表
        List<Map<String, Object>> prdList = new ArrayList<Map<String, Object>>();
        for (Product aProductList : productList) {
            Map<String, Object> prdInfoMap = new TreeMap<>();
            Map<String, Object> prdMap = new TreeMap<>();
            StringBuilder sbkey = new StringBuilder();
            Product prd = aProductList;
            prdMap.put(Constants.product_field_skuid, prd.getId());
            prdMap.put(Constants.product_field_name, prd.getName());
            prdMap.put(Constants.product_field_price, prd.getPrice());
            prdMap.put(Constants.product_field_number, prd.getNumber());
            prdMap.put(Constants.product_field_unit, prd.getUnit());
            if (id.equals(prd.getId())) {
                setNavigation(productMap, prdMap, prd);
            }
            String prdParamsIDS = prd.getParamIDs();
            String prdParamValues = prd.getParamValues();
            if (prdParamsIDS.indexOf(",") > 0) {
                String[] arr_prdParamsIDS = prdParamsIDS.split(",");
                String[] arr_prdParamValues = prdParamValues.split(",");
                for (int n = 0; n < arr_prdParamsIDS.length; n++) {
                    Map<String, Object> prdParamValuesMap = (Map<String, Object>) paramValuesMap.get(arr_prdParamsIDS[n]);
                    sbkey.append(arr_prdParamsIDS[n]);
                    sbkey.append(",");
                    sbkey.append(prdParamValuesMap.get(arr_prdParamValues[n]));
                    sbkey.append("|");
                }
            } else {
                Map<String, Object> prdParamValuesMap = (Map<String, Object>) paramValuesMap.get(prdParamValues);
                sbkey.append(prdParamsIDS);
                sbkey.append(",");
                sbkey.append(prdParamValuesMap.get(prdParamValues));
                sbkey.append("|");
            }
            String key = sbkey.delete(sbkey.lastIndexOf("|"), sbkey.length()).toString();
            prdInfoMap.put(Constants.product_field_k, key);
            prdInfoMap.put(Constants.product_field_v, prdMap);
            prdList.add(prdInfoMap);
        }
        productMap.put(Constants.product_field_products, prdList);
    }

    /**
     * 设置导航信息(面包屑)
     *
     * @param productMap
     * @param prdMap
     * @param prd
     */
    private void setNavigation(Map<String, Object> productMap,
                               Map<String, Object> prdMap, Product prd) {
        prdMap.put(Constants.product_field_defalut, true);
        Map<String, Object> navigationMap = new TreeMap<String, Object>();
        navigationMap.put(Constants.product_field_fcateid, prd.getFcateid());
        navigationMap.put(Constants.product_field_fcateName, prd.getFcateName());
        navigationMap.put(Constants.product_field_scateid, prd.getScateid());
        navigationMap.put(Constants.product_field_scateName, prd.getScateName());
        navigationMap.put(Constants.product_field_brandid, prd.getBrandid());
        navigationMap.put(Constants.product_field_brandName, prd.getCNName());
        productMap.put(Constants.product_field_navigation, navigationMap);
    }

    /**
     * 设置产品的图片集合信息
     *
     * @param productMap
     * @param productList
     */
    private void setProductImgs(Map<String, Object> productMap,
                                List<Product> productList) {
        Map<String, List<Long>> a = new HashMap<String, List<Long>>();
        for (Product p : productList) {

            for (String s : p.getImgUrl().split(";")) {
                addToImg(a, s, p.getId());
                //a.put(s, p);
            }
        }
        List<Map<String, Object>> imgList = new ArrayList<Map<String, Object>>();
        if (!a.isEmpty()) {
            for (Map.Entry<String, List<Long>> entry : a.entrySet()) {
                Map<String, Object> imgsMap = new TreeMap<String, Object>();
                imgsMap.put(Constants.product_field_skuid, entry.getValue());
                imgsMap.put(Constants.product_field_imgUrl, entry.getKey());
                imgList.add(imgsMap);
            }
        }
        productMap.put(Constants.product_field_imgs, imgList);
    }

    /**
     * 设置产品参数信息
     *
     * @param paramValuesMap
     * @param productMap
     * @param product
     */
    private void setProductParamInfo(Map<String, Object> paramValuesMap,
                                     Map<String, Object> productMap, ProductWithBLOBs product) {
        List<ProductParam> params = getParamsInfo(product);
        List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
        if (!params.isEmpty()) {
            for (ProductParam p : params) {
                Map<String, Object> paramMap = new TreeMap<String, Object>();
                Map<String, Object> paramValueMap = new TreeMap<String, Object>();
                paramMap.put(Constants.product_field_key, p.getId());
                paramMap.put(Constants.product_field_value, p.getPname());
                String pValue = p.getPvalue();
                String[] arr_pValue;
                if (pValue.indexOf(",") > 0) {
                    //参数值数组
                    arr_pValue = pValue.split(",");
                    for (int i = 0; i < arr_pValue.length; i++) {
                        paramValueMap.put(arr_pValue[i], String.valueOf(i));
                    }
                } else {
                    arr_pValue = new String[1];
                    arr_pValue[0] = pValue;
                    paramValueMap.put(pValue, "0");
                }
                paramMap.put(Constants.product_field_values, arr_pValue);
                paramValuesMap.put(String.valueOf(p.getId()), paramValueMap);
                paramList.add(paramMap);
            }
        }
        productMap.put(Constants.product_field_params, paramList);
    }

    /**
     * 获得产品参数信息
     *
     * @param product
     * @return
     */
    private List<ProductParam> getParamsInfo(ProductWithBLOBs product) {
        List<ProductParam> params = new ArrayList<>();
        String param = product.getParamIDs();

        //多个参数的情况
        if (param.indexOf(",") > 0) {
            String[] arr_param = param.split(",");
            List<Integer> paramList = new ArrayList<>();
            for (String anArr_param : arr_param) {
                paramList.add(Integer.parseInt(anArr_param));
            }
            params = paramService.selectParamByIds(paramList);
        }//一个产品没有参数情况
        else {
            ProductParam pp = paramService.queryParamById(Long.parseLong(param));
            params.add(pp);
        }
        return params;
    }

    /**
     * 获得此品牌所属的二级系统
     *
     * @param product
     * @return
     */
    public Response querySCateListByBrandId(Product product) {
        Response response = new Response();
        List<ResultBean> scategoryList;
        try {
            scategoryList = productMapper.getSCateListByBrandId(product);
            response.setData(scategoryList);
        } catch (Exception e) {
            log.error("update product error >>>{}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新失败");
        }
        return response;
    }

    /**
     * 根据二级分类和品牌查询所有产品
     *
     * @param product
     * @return
     */
    public Response queryProductInfoBySCategory(Map<String, Object> product) {
        Response response = new Response();
        List<ProductWithBLOBs> productList;
        try {
            productList = productMapper.queryProductInfoBySCategory(product);
            response.setData(productList);
        } catch (Exception e) {
            log.error("update product error>>> {}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新失败");
        }
        return response;
    }

    /**
     * 根据二级分类和品牌查询所有产品
     *
     * @param product
     * @return
     */
    public List<ProductMap> queryRecommendHotProduct(Map<String, Object> product) {
        List<ProductMap> productList;
        try {
            productList = productMapper.queryRecommendHotProduct(product);
        } catch (Exception e) {
            log.error("update product error >>> {}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
        return productList;
    }


    /**
     * 产品详情页面预览产品参数
     *
     * @param id
     * @return
     */
    public Map<String, Object> queryPrdDescParamService(Long id) {
        Response response = new Response();
        ProductWithBLOBs product;
        Map<String, Object> map = new TreeMap<>();

        try {
            product = productMapper.selectByPrimaryKey(id);
            if (product != null) {

                map.put(Constants.product_field_id, product.getId());
                map.put(Constants.product_field_detailDesc, product.getDetailDesc());
                map.put(Constants.product_field_paras, product.getParas());
                map.put(Constants.product_field_service, product.getService());
                response.setData(map);
            } else {
                log.error("产品不存在");
                throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "产品不存在");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询失败:{}", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
        return map;
    }

    /**
     * 查询品牌对应的子系统
     */
    public List<ResultBean> findSubSystem(String id) {
        try {
            return productMapper.findSubSystem(id);
        } catch (Exception e) {
            log.error("查询失败:{}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
    }


    public List<Product> findProductBySystemId(String id) {
        return productMapper.findProductBySystemId(id);

    }


    public List<Map<String, String>> queryProductTypeListByCompanyId(Map<String, Object> queryMap) {
        try {
            return productMapper.queryProductTypeListByCompanyId(queryMap);
        } catch (Exception e) {
            log.error("查询失败:{}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
    }

    public List<Map<String, String>> queryHotProductListByCompanyId(Map<String, Object> queryMap) {
        try {
            return productMapper.queryHotProductListByCompanyId(queryMap);
        } catch (Exception e) {
            log.error("查询失败:{}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
    }

    public List<Map<String, String>> queryLatestProductListByCompanyId(Map<String, Object> queryMap) {
        try {
            return productMapper.queryLatestProductListByCompanyId(queryMap);
        } catch (Exception e) {
            log.error("查询失败:{}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
    }

    public List<Map<String, String>> findAllProductListByProductType(Paging<Map<String, String>> pager, Map<String, Object> queryMap) {
        try {
            return productMapper.findAllProductListByProductType(pager.getRowBounds(), queryMap);
        } catch (Exception e) {
            log.error("查询失败:{}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
    }

    public List<Map<String, String>> queryHotProduct(Map<String, Object> queryMap) {
        try {
            return productMapper.queryHotProduct(queryMap);
        } catch (Exception e) {
            log.error("查询失败:{}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
    }

    /**
     * @param brandId
     * @return
     */
    public List<String> findScateIdByBrandId(String brandId) {
        List<String> list;
        try {
            list = productMapper.findScateByBrandId(brandId);
        } catch (Exception e) {
            log.error("查询失败:{}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
        return list;
    }


    public Product findById(Long id) {
        try {
            return productMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("查询失败:{}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
    }
    
	/**
	 * 查询所有产品
	 * @param pager
	 * @param product
	 * @return
	 */
	public List<Product> findAllProduct(Paging<Product> pager,
			ProductWithBLOBs product) {
		 try {
		       log.debug("分页查询所有产品");
	        return productMapper.findAllProduct(pager.getRowBounds(), product);
		 } catch (Exception e) {
	            log.error("查询失败:{}", e);
	            e.printStackTrace();
	            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
	        }
	}
}

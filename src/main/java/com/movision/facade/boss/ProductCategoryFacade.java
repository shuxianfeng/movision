package com.movision.facade.boss;

import com.movision.mybatis.brand.entity.Brand;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscount;
import com.movision.mybatis.productcategory.entity.ProductCategory;
import com.movision.mybatis.productcategory.service.ProductCategoryService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author zhanglei
 * @Date 2017/3/1 16:48
 */
@Service
public class ProductCategoryFacade {
    @Autowired
    private ProductCategoryService productCategoryService;
    private static Logger log = LoggerFactory.getLogger(ProductCategoryFacade.class);
    @Value("#{configProperties['img.domain']}")
    private String imgdomain;


    /**
     * 查询类别列表
     *
     * @param pager
     * @return
     */
    public List<ProductCategory> findAllCategory(Paging<ProductCategory> pager) {
        return productCategoryService.findAllCategory(pager);
    }

    /**
     * 查询活动列表
     *
     * @param pager
     * @return
     */
    public List<GoodsDiscount> findAllGoodsDiscount(Paging<GoodsDiscount> pager) {
        return productCategoryService.findAllGoodsDiscount(pager);
    }
    /**
     * 查询品牌列表
     *
     * @param pager
     * @return
     */
    public List<Brand> findAllBrand(Paging<Brand> pager) {
        return productCategoryService.findAllBrand(pager);
    }
    /**
     * 条件搜索商品分类
     *
     * @param typename
     * @param pager
     * @return
     */
    public List<ProductCategory> findAllCategoryCondition(String typename, Paging<ProductCategory> pager) {
        Map<String, Object> map = new HashMap<>();
        map.put("typename", typename);

        return productCategoryService.findAllCategoryCondition(map, pager);
    }

    /**
     * 条件搜索商品活动
     *
     * @param name
     * @param pager
     * @return
     */
    public List<GoodsDiscount> findAllCategoryCondition(String name, String isdel, Paging<GoodsDiscount> pager) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("isdel", isdel);
        return productCategoryService.findAllGoodsDiscountCondition(map, pager);
    }
    /**
     * 条件搜索商品品牌
     *
     * @param brandname
     * @param pager
     * @return
     */
    public List<Brand> findAllBrandCondition(String brandname, String isdel, Paging<Brand> pager) {
        Map<String, Object> map = new HashMap<>();
        map.put("typename", brandname);
        map.put("isdel", isdel);
        return productCategoryService.findAllBrandCondition(map, pager);
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    public int deleteCategory(Integer id) {
        return productCategoryService.deleteCategory(id);
    }


    /**
     * 增加商品类别
     *
     * @param typename
     * @param imgurl
     * @return
     */
    public Map<String, Integer> addCategory(String typename, String imgurl) {
        Map<String, Integer> map = new HashMap<>();
        ProductCategory productCategory = new ProductCategory();
        int maxpro = productCategoryService.queryAllProtype();
        int have = productCategoryService.queryCategoryHave(typename);
        int result = 0;
        if (have >= 1) {
            result = 0;
        } else {
            productCategory.setProtype(maxpro + 1);
            productCategory.setTypename(typename);
            productCategory.setImgurl(imgurl);
            productCategory.setIntime(new Date());
            result = productCategoryService.addCategory(productCategory);
        }
        map.put("result", result);

        return map;
    }


    /**
     * 增加品牌
     * @param brandname
     * @param isdel
     * @return
     */
    public Map<String, Integer> addBrand(String brandname, String isdel) {
        Map<String, Integer> map = new HashMap<>();
        Brand brand = new Brand();
        int maxpro = productCategoryService.queryAllBrand();
        int have = productCategoryService.queryBrandHave(brandname);
        int result = 0;
        if (have >= 1) {
            result = 0;
        } else {
            brand.setBrandid(maxpro + 1);
            brand.setBrandname(brandname);
            brand.setIsdel(Integer.parseInt(isdel));
            brand.setIntime(new Date());
            result = productCategoryService.addBrand(brand);
        }
        map.put("result", result);
        return map;
    }

    /**
     * 增加商品活动
     * @param name
     * @param discount
     * @param content
     * @param startdate
     * @param enddate
     * @param isenrent
     * @param rentday
     * @param orderid
     * @param isdel
     * @return
     */
    public Map<String, Integer> addGoodsDiscount(String name, String discount, String content, String startdate, String enddate, String isenrent, String rentday, String orderid, String isdel) {
        Map<String, Integer> map = new HashMap<>();
        GoodsDiscount goodsDiscount = new GoodsDiscount();
        goodsDiscount.setName(name);
        goodsDiscount.setIsdel(Integer.parseInt(isdel));
        goodsDiscount.setDiscount(Integer.parseInt(discount));
        goodsDiscount.setContent(content);
        goodsDiscount.setIsenrent(Integer.parseInt(isenrent));
        goodsDiscount.setRentday(Integer.parseInt(rentday));
        goodsDiscount.setOrderid(Integer.parseInt(orderid));
        Date isessencetime = null;//开始时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (startdate != null) {
            try {
                isessencetime = format.parse(startdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        goodsDiscount.setStartdate(isessencetime);
        Date max = null;//最大时间
        if (enddate != null) {
            try {
                max = format.parse(enddate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        goodsDiscount.setEnddate(max);
        int result = productCategoryService.addGoodsDiscount(goodsDiscount);
        map.put("result", result);
        return map;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public ProductCategory queryCategory(Integer id) {
        return productCategoryService.queryCategory(id);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public GoodsDiscount queryGoodsDiscount(Integer id) {
        return productCategoryService.queryGoodsDiscount(id);
    }
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public Brand queryBrand(Integer id) {
        return productCategoryService.queryBrand(id);
    }

    /**
     * 修改类别
     *
     * @param
     * @param id
     * @param typename
     * @param imgurl
     * @return
     */
    public Map<String, Integer> updateCategory(String id, String typename, String imgurl) {
        Map<String, Integer> map = new HashMap<>();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(Integer.parseInt(id));
        productCategory.setTypename(typename);

        productCategory.setImgurl(imgurl);
            int result = productCategoryService.updateCategory(productCategory);
            map.put("result", result);

        return map;
    }

    /**
     * 停用
     *
     * @param id
     * @return
     */
    public int updateDown(Integer id) {
        return productCategoryService.updateDown(id);
    }

    /**
     * 启用
     *
     * @param id
     * @return
     */
    public int updateUp(Integer id) {
        return productCategoryService.updateUp(id);
    }

    /**
     * 活动停用
     *
     * @param id
     * @return
     */
    public int updateDownD(Integer id) {
        return productCategoryService.updateDownD(id);
    }

    /**
     * 活动启用
     *
     * @param id
     * @return
     */
    public int updateUpD(Integer id) {
        return productCategoryService.updateUpD(id);
    }

    /**
     * 修改品牌
     *
     * @param brandname
     * @param isdel
     * @return
     */
    public Map<String, Integer> updateBrand(String brandname, String isdel, String id) {
        Map<String, Integer> map = new HashMap<>();
        Brand brand = new Brand();
        brand.setBrandname(brandname);
        brand.setIsdel(Integer.parseInt(isdel));
        brand.setId(Integer.parseInt(id));
        int result = productCategoryService.updateBrand(brand);
        map.put("result", result);
        return map;
    }

    /**
     * 编辑活动
     *
     * @param name
     * @param discount
     * @param content
     * @param startdate
     * @param enddate
     * @param isenrent
     * @param rentday
     * @param orderid
     * @param isdel
     * @return
     */
    public Map<String, Integer> updateGoodsDis(String name, String id, String discount, String content, String startdate, String enddate, String isenrent, String rentday, String orderid, String isdel) {

        Map<String, Integer> map = new HashMap<>();
        GoodsDiscount goodsDiscount = new GoodsDiscount();
        goodsDiscount.setName(name);
        goodsDiscount.setIsdel(Integer.parseInt(isdel));
        goodsDiscount.setDiscount(Integer.parseInt(discount));
        goodsDiscount.setContent(content);
        goodsDiscount.setIsenrent(Integer.parseInt(isenrent));
        goodsDiscount.setRentday(Integer.parseInt(rentday));
        goodsDiscount.setOrderid(Integer.parseInt(orderid));
        goodsDiscount.setId(Integer.parseInt(id));
        Date isessencetime = null;//开始时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (startdate != null) {
            try {
                isessencetime = format.parse(startdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        goodsDiscount.setStartdate(isessencetime);
        Date max = null;//最大时间
        if (enddate != null) {
            try {
                max = format.parse(enddate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        goodsDiscount.setEnddate(max);
        int result = productCategoryService.updateDiscount(goodsDiscount);
        map.put("result", result);
        return map;
    }
}

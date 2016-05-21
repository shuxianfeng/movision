package com.zhuhuibao.business.memCenter.BrandManage;

import com.zhuhuibao.common.ApiConstants;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by cxx on 2016/3/7 0007.
 */
@RestController
public class BrandManageController {
    private static final Logger log = LoggerFactory.getLogger(BrandManageController.class);

    @Autowired
    private BrandService brandService;

    @Autowired
    ApiConstants ApiConstants;

    @Autowired
    UploadService uploadService;
    /**
     * 新建品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/addBrand", method = RequestMethod.POST)
    public JsonResult upload(Brand brand) throws IOException {
        JsonResult result = new JsonResult();
        brand.setPublishTime(new Date());
        brandService.addBrand(brand);
        return result;
    }

    /**
     * 更新品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/updateBrand", method = RequestMethod.POST)
    public JsonResult updateBrand(Brand brand, String type) throws IOException {
        JsonResult result = new JsonResult();
        //如果是未通过的品牌进行更新，则状态变为待审核
        if(type==null || "".equals(type)){
            if(brand != null && brand.getId() != null) {
                Brand brand1 = brandService.brandDetails(brand.getId());
                if (brand1.getStatus() == 0) {
                    brand.setStatus(2);
                }
                brand.setLastModifyTime(new Date());
            }
        }
        brandService.updateBrand(brand);
        return result;
    }

    /**
     * 删除品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/deleteBrand", method = RequestMethod.POST)
    public JsonResult deleteBrand(String id) throws IOException {
        JsonResult result = new JsonResult();
        brandService.deleteBrand(id);
        return result;
    }

    /**
     * 批量删除品牌
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/batchDeleteBrand", method = RequestMethod.POST)
    public JsonResult batchDeleteBrand(HttpServletRequest req) throws IOException {
        String ids[] = req.getParameterValues("id");
        JsonResult result = new JsonResult();
        for (String id : ids) {
            brandService.deleteBrand(id);
        }
        return result;
    }

    /**
     * 查询品牌（自己发布的品牌）
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/searchBrand", method = RequestMethod.GET)
    public JsonResult searchBrand(Brand brand) throws IOException {
        List<Brand> brandList = brandService.searchBrand(brand);
        JsonResult result = new JsonResult();
        result.setData(brandList);
        return result;
    }

    /**
     * 查询品牌(系统所有的品牌，分页，运营系统用)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/searchBrandByPager", method = RequestMethod.GET)
    public JsonResult searchBrandByPager(Brand brand, String pageNo, String pageSize) throws IOException {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Brand> pager = new Paging<Brand>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<Brand> brandList = brandService.searchBrandByPager(pager,brand);
        pager.result(brandList);
        JsonResult result = new JsonResult();
        result.setData(pager);
        return result;
    }

    /**
     * 查询品牌code,name
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/searchBrandSelect", method = RequestMethod.GET)
    public JsonResult searchBrandSelect(Brand brand) throws IOException {
        List<Brand> brandList = brandService.searchBrandByStatus(brand);
        List list = new ArrayList();
        for (Brand aBrandList : brandList) {
            Map map = new HashMap();
            Brand brand1 = aBrandList;
            map.put("code", brand1.getId());
            map.put("name", brand1.getCNName());
            list.add(map);
        }
        JsonResult result = new JsonResult();
        result.setData(list);
        return result;
    }

    /**
     * 品牌数量（自己发布的品牌）
     */
    @RequestMapping(value = "/rest/searchBrandSize", method = RequestMethod.GET)
    public JsonResult searchBrandSize(Brand brand) throws IOException {
        int size = brandService.searchBrandSize(brand);
        JsonResult result = new JsonResult();
        result.setData(size);
        return result;
    }

    /**
     * 品牌数量(运营系统，系统所有品牌)
     */
    @RequestMapping(value = "/rest/findBrandSize", method = RequestMethod.GET)
    public JsonResult findBrandSize(Brand brand) throws IOException {
        int size = brandService.findBrandSize(brand);
        JsonResult result = new JsonResult();
        result.setData(size);
        return result;
    }

    /**
     * 查看品牌详情
     */
    @RequestMapping(value = "/rest/brandDetails", method = RequestMethod.GET)
    public JsonResult brandDetails(int id) throws IOException {
        Brand brand = brandService.brandDetails(id);
        if(brand.getViews()==null){
            brand.setViews(1);
        }else{
            brand.setViews(brand.getViews()+1);
        }
        brandService.updateBrand(brand);
        JsonResult result = new JsonResult();
        result.setData(brand);
        return result;
    }

    /**
     * 上传图片，返回url
     */
    @RequestMapping(value = "/rest/uploadImg", method = RequestMethod.POST)
    public JsonResult uploadImg(HttpServletRequest req) throws IOException {
        //完成文件上传
        JsonResult result = new JsonResult();
        String url = uploadService.upload(req,"img");
        result.setData(url);
        return result;
    }

    /**
     * 查询二级系统下所有品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/brand/findBrandByScateid", method = RequestMethod.GET)
    public JsonResult findBrandByScateid(Product product) throws IOException {
        List<ResultBean> brandList = brandService.findBrandByScateid(product);
        JsonResult result = new JsonResult();
        result.setData(brandList);
        return result;
    }
}
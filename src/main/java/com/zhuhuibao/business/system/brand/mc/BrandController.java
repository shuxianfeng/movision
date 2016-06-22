package com.zhuhuibao.business.system.brand.mc;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
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
import java.io.IOException;
import java.util.*;

/**
 * Created by cxx on 2016/3/7 0007.
 */
@RestController
public class BrandController {
    private static final Logger log = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    private BrandService brandService;

    @Autowired
    ApiConstants ApiConstants;


    /**
     * 新建品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/addBrand","/rest/system/mc/brand/add_brand"}, method = RequestMethod.POST)
    public Response upload(Brand brand) {
        Response result = new Response();
        brandService.addBrand(brand);
        return result;
    }

    /**
     * 更新品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/updateBrand","/rest/system/mc/brand/upd_brand"}, method = RequestMethod.POST)
    public Response updateBrand(Brand brand, String type)  {
        Response result = new Response();
        //如果是未通过的品牌进行更新，则状态变为待审核
        if(type==null || "".equals(type)){
            if(brand != null && brand.getId() != null) {
                Brand brand1 = brandService.brandDetails(brand.getId());
                if (brand1.getStatus() == 0) {
                    brand.setStatus(2);
                }
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
    @RequestMapping(value = {"/rest/deleteBrand","/rest/system/mc/brand/del_brand"}, method = RequestMethod.POST)
    public Response deleteBrand(String id)  {
        Response result = new Response();
        brandService.deleteBrand(id);
        return result;
    }

    /**
     * 批量删除品牌
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/batchDeleteBrand","/rest/system/mc/brand/del_batch_brand"}, method = RequestMethod.POST)
    public Response batchDeleteBrand(HttpServletRequest req)  {
        String ids[] = req.getParameterValues("id");
        Response result = new Response();
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
    @RequestMapping(value = {"/rest/searchBrand","/rest/system/mc/brand/sel_my_brand"}, method = RequestMethod.GET)
    public Response searchBrand(Brand brand)  {
        List<Brand> brandList = brandService.searchBrand(brand);
        Response result = new Response();
        result.setData(brandList);
        return result;
    }

    /**
     * 查询品牌(系统所有的品牌，分页，运营系统用)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/searchBrandByPager","/rest/system/oms/brand/sel_all_brand"}, method = RequestMethod.GET)
    public Response searchBrandByPager(Brand brand, String pageNo, String pageSize)  {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Brand> pager = new Paging<Brand>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<Brand> brandList = brandService.searchBrandByPager(pager,brand);
        pager.result(brandList);
        Response result = new Response();
        result.setData(pager);
        return result;
    }

    /**
     * 查询品牌code,name
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/searchBrandSelect","/rest/system/mc/brand/sel_brand_by_createId"}, method = RequestMethod.GET)
    public Response searchBrandSelect(Brand brand)  {
        List<Brand> brandList = brandService.searchBrandByStatus(brand);
        List list = new ArrayList();
        for (Brand aBrandList : brandList) {
            Map map = new HashMap();
            Brand brand1 = aBrandList;
            map.put("code", brand1.getId());
            map.put("name", brand1.getCNName());
            list.add(map);
        }
        Response result = new Response();
        result.setData(list);
        return result;
    }

    /**
     * 品牌数量（自己发布的品牌）
     */
    @RequestMapping(value = {"/rest/searchBrandSize","/rest/system/mc/brand/sel_brand_size"}, method = RequestMethod.GET)
    public Response searchBrandSize(Brand brand) {
        int size = brandService.searchBrandSize(brand);
        Response result = new Response();
        result.setData(size);
        return result;
    }

    /**
     * 品牌数量(运营系统，系统所有品牌)
     */
    @RequestMapping(value = {"/rest/findBrandSize","/rest/system/oms/brand/sel_brand_size"}, method = RequestMethod.GET)
    public Response findBrandSize(Brand brand){
        int size = brandService.findBrandSize(brand);
        Response result = new Response();
        result.setData(size);
        return result;
    }

    /**
     * 查看品牌详情
     */
    @RequestMapping(value = {"/rest/brandDetails","/rest/system/mc/brand/sel_brand_info"}, method = RequestMethod.GET)
    public Response brandDetails(int id) {
        Brand brand = brandService.brandDetails(id);
        Response result = new Response();
        result.setData(brand);
        return result;
    }

    /**
     * 查询二级系统下所有品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/brand/findBrandByScateid","/rest/system/mc/brand/sel_brand_by_scateid"}, method = RequestMethod.GET)
    public Response findBrandByScateid(Product product)  {
        List<ResultBean> brandList = brandService.findBrandByScateid(product);
        Response result = new Response();
        result.setData(brandList);
        return result;
    }
}
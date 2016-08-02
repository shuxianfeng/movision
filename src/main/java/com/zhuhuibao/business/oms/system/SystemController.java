package com.zhuhuibao.business.oms.system;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.CheckBrand;
import com.zhuhuibao.mybatis.memCenter.entity.CheckSysBrand;
import com.zhuhuibao.mybatis.memCenter.entity.SysBrand;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.memCenter.service.CheckBrandService;
import com.zhuhuibao.mybatis.oms.entity.Category;
import com.zhuhuibao.mybatis.oms.service.CategoryService;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运营中心类目管理
 * Created by cxx on 2016/3/4 0004.
 */
@RestController
public class SystemController {
    private static final Logger log = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CheckBrandService checkBrandService;

    @Autowired
    private BrandService brandService;
    /**
     * 查询大系统类目
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/systemSearch","/rest/system/oms/category/sel_bigSystem"}, method = RequestMethod.GET)
    public Response systemSearch() throws Exception {
        Response response = new Response();
        List<ResultBean> systemList = categoryService.findSystemList();
        response.setCode(200);
        response.setData(systemList);
        return response;
    }

    /**
     * 查询大系统下所有子系统类目
     * @param req
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/subSystemSearch","/rest/system/oms/category/sel_subSystem"}, method = RequestMethod.GET)
    public Response subSystemSearch(HttpServletRequest req) throws Exception {
        Response response = new Response();
        String parentId = req.getParameter("parentId");
        List<ResultBean> subSystemList = categoryService.findSubSystemList(parentId);
        response.setCode(200);
        response.setData(subSystemList);
        return response;
    }


    /**
     * 查询大系统，子系统
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/system/findSystem","/rest/system/oms/category/sel_allSystem"}, method = RequestMethod.GET)
    public Response findSystem() throws IOException {
        return categoryService.findAllSystem();
    }

    /**
     * 添加类目
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/system/addSystem","/rest/system/oms/category/add_system"}, method = RequestMethod.POST)
    public Response addSystem(Category category) throws Exception {
        Response result = new Response();
        categoryService.addSystem(category);
        return result;
    }

    /**
     * 编辑类目
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/system/updateSystem","/rest/system/oms/category/upd_system"}, method = RequestMethod.POST)
    public Response updateSystem(Category category) throws Exception {
        Response result = new Response();
        categoryService.updateSystem(category);
        return result;
    }

    /**
     * 删除类目
     * @return
     * @throws IOException
     */

    @RequestMapping(value = {"/rest/system/deleteSystem","/rest/system/oms/category/del_system"}, method = RequestMethod.POST)
    public Response deleteSystem(Category category) throws Exception {
        Response result = new Response();
        List<Product> list = productService.findProductBySystemId(category.getId().toString());
        if(list.size()!=0){
            result.setCode(400);
            result.setMessage("该系统下有产品，无法删除");
        }else{
            categoryService.deleteSystem(category);
            result.setCode(200);
        }

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
        Paging<CheckBrand> pager = new Paging<CheckBrand>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<CheckBrand> brandList = checkBrandService.searchBrandByPager(pager,brand);
        pager.result(brandList);
        Response result = new Response();
        result.setData(pager);
        return result;
    }


    @ApiOperation(value = "查看品牌详情",notes = "查看品牌详情", response = Response.class)
    @RequestMapping(value = "/rest/system/oms/brand/sel_brand", method = RequestMethod.GET)
    public Response brandDetails(@RequestParam String id) {
        Response result = new Response();
        Map<String,Object> map = new HashMap<>();
        CheckBrand brand = checkBrandService.queryBrandById(id);
        List<Map<String,Object>> sysList = brandService.queryBrandSysById(id);
        map.put("brandInfo",brand);
        map.put("sysList",sysList);
        result.setData(map);
        return result;
    }

    @ApiOperation(value = "审核品牌",notes = "审核品牌", response = Response.class)
    @RequestMapping(value = "/rest/system/oms/brand/upd_brand", method = RequestMethod.POST)
    public Response updateBrand(@ModelAttribute CheckBrand brand, @RequestParam String json)  {
        Response result = new Response();
        //审核品牌基本信息
        checkBrandService.updateBrand(brand);
        //删除原有的对应关系
        brandService.deleteBrandSysByBrandID(brand.getId());
        //插入新的对应关系
        Gson gson=new Gson();
        List<CheckSysBrand> rs= new ArrayList<CheckSysBrand>();
        Type type = new TypeToken<ArrayList<CheckSysBrand>>() {}.getType();
        rs = gson.fromJson(json, type);
        for(CheckSysBrand sysBrand:rs){
            sysBrand.setBrandid(String.valueOf(brand.getId()));
            brandService.addSysBrand(sysBrand);
        }
        //审核通过，将字表的数据同步到主表，包括基本信息表和品牌所属分类表
        if("1".equals(brand.getStatus())){
            CheckBrand checkBrand = checkBrandService.queryBrandById(String.valueOf(brand.getId()));
            Brand newBrand = new Brand();
            newBrand.setId(checkBrand.getId());
            newBrand.setCNName(checkBrand.getCnname());
            newBrand.setENName(checkBrand.getEnname());
            newBrand.setLogourl(checkBrand.getLogourl());
            newBrand.setCreateid(checkBrand.getCreateid());
            newBrand.setDescription(checkBrand.getDescription());
            newBrand.setImgurl(checkBrand.getImgurl());
            newBrand.setCertificate(checkBrand.getCertificate());
            newBrand.setOwner(checkBrand.getOwner());
            newBrand.setWebSite(checkBrand.getWebsite());
            newBrand.setPublishTime(checkBrand.getPublishtime());
            newBrand.setLastModifyTime(checkBrand.getLastmodifytime());
            newBrand.setStatus("1");
            //判断是否是第一次审核通过
            Brand isExist = brandService.brandDetails(String.valueOf(brand.getId()));
            if(isExist!=null){
                brandService.addBrand(newBrand);
            }else {
                brandService.updateBrand(newBrand);
            }
        }

        return result;
    }
}

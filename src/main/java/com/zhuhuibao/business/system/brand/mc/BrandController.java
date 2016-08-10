package com.zhuhuibao.business.system.brand.mc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.CheckBrand;
import com.zhuhuibao.mybatis.memCenter.entity.CheckSysBrand;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.memCenter.service.CheckBrandService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by cxx on 2016/3/7 0007.
 */
@RequestMapping("/rest/system/mc/brand/")
@RestController
public class BrandController {
    private static final Logger log = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    private BrandService brandService;

    @Autowired
    ApiConstants ApiConstants;

    @Autowired
    private CheckBrandService checkBrandService;

    @ApiOperation(value = "新建品牌",notes = "新建品牌", response = Response.class)
    @RequestMapping(value = "add_brand", method = RequestMethod.POST)
    public Response upload(@ModelAttribute CheckBrand brand, @RequestParam String json) {
        Response result = new Response();
        Gson gson=new Gson();
        List<CheckSysBrand> rs= new ArrayList<CheckSysBrand>();
        Type type = new TypeToken<ArrayList<CheckSysBrand>>() {}.getType();
        rs = gson.fromJson(json, type);
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            brand.setCreateid(createid);
            int brandId = checkBrandService.addBrand(brand);
            for(CheckSysBrand sysBrand:rs){
                sysBrand.setBrandid(String.valueOf(brandId));
                checkBrandService.addSysBrand(sysBrand);
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "查看品牌详情",notes = "查看品牌详情", response = Response.class)
    @RequestMapping(value = "sel_brand", method = RequestMethod.GET)
    public Response brandDetails(@RequestParam String id) {
        Response result = new Response();
        Map<String,Object> map = new HashMap<>();
        CheckBrand brand = checkBrandService.queryBrandById(id);
        List<Map<String,Object>> sysList = brandService.queryBrandSysById(id);
        map.put("brandInfo",brand);
        map.put("sysList",sysList);
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            if(String.valueOf(createid).equals(String.valueOf(brand.getCreateid()))){
                result.setData(map);
            }else {
                throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "更新品牌",notes = "更新品牌", response = Response.class)
    @RequestMapping(value = "upd_brand", method = RequestMethod.POST)
    public Response updateBrand(@ModelAttribute CheckBrand brand,@RequestParam String json)  {
        Response result = new Response();
        //状态变成待审核
        brand.setStatus(2);
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            CheckBrand b = checkBrandService.queryBrandById(String.valueOf(brand.getId()));
            if(b!=null){
                if(String.valueOf(createid).equals(String.valueOf(b.getCreateid()))){
                    //更新品牌基本信息
                    checkBrandService.updateBrand(brand);
                    //删除原有的对应关系
                    checkBrandService.deleteBrandSysByBrandID(brand.getId());
                    //插入新的对应关系
                    Gson gson=new Gson();
                    List<CheckSysBrand> rs= new ArrayList<CheckSysBrand>();
                    Type type = new TypeToken<ArrayList<CheckSysBrand>>() {}.getType();
                    rs = gson.fromJson(json, type);
                    for(CheckSysBrand sysBrand:rs){
                        sysBrand.setBrandid(String.valueOf(brand.getId()));
                        checkBrandService.addSysBrand(sysBrand);
                    }
                }else {
                    throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
                }
            }else {
                throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "删除品牌",notes = "删除品牌", response = Response.class)
    @RequestMapping(value = "del_brand", method = RequestMethod.POST)
    public Response deleteBrand(@RequestParam String id)  {
        Response result = new Response();
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            CheckBrand b = checkBrandService.queryBrandById(String.valueOf(id));
            if(b!=null){
                if(String.valueOf(createid).equals(String.valueOf(b.getCreateid()))){
                    brandService.deleteBrand(id);
                    checkBrandService.deleteBrand(id);
                }else {
                    throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
                }
            }else {
                throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "批量删除品牌",notes = "批量删除品牌", response = Response.class)
    @RequestMapping(value = "del_batch_brand", method = RequestMethod.POST)
    public Response batchDeleteBrand(@ApiParam(value = "ids,逗号隔开") @RequestParam String ids)  {
        Response result = new Response();
        String[] idList = ids.split(",");
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            for (String id : idList) {
                CheckBrand b = checkBrandService.queryBrandById(String.valueOf(id));
                if(b!=null){
                    if(String.valueOf(createid).equals(String.valueOf(b.getCreateid()))){
                        brandService.deleteBrand(id);
                        checkBrandService.deleteBrand(id);
                    }else {
                        throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
                    }
                }else {
                    throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
                }
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "查询品牌（自己发布的品牌）",notes = "查询品牌（自己发布的品牌）", response = Response.class)
    @RequestMapping(value = "sel_my_brand", method = RequestMethod.GET)
    public Response searchBrand(@RequestParam(required = false) String status)  {
        Response result = new Response();
        Long createid = ShiroUtil.getCreateID();
        Map<String,Object> map = new HashMap<>();
        if(createid!=null){
            map.put("createid",createid);
            map.put("status",status);
            List<Map<String,Object>> brandList = checkBrandService.searchMyBrand(map);
            result.setData(brandList);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }


    @ApiOperation(value = "查询品牌",notes = "查询品牌(用户自己发布审核通过的品牌)", response = Response.class)
    @RequestMapping(value = "sel_brand_by_createId", method = RequestMethod.GET)
    public Response searchBrandSelect(@ModelAttribute Brand brand)  {
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

    @ApiOperation(value = "品牌数量（自己发布的品牌）",notes = "品牌数量（自己发布的品牌）", response = Response.class)
    @RequestMapping(value = "sel_brand_size", method = RequestMethod.GET)
    public Response searchBrandSize(@ModelAttribute Brand brand) {
        int size = brandService.searchBrandSize(brand);
        Response result = new Response();
        result.setData(size);
        return result;
    }

    @ApiOperation(value = "查询二级系统下所有品牌",notes = "查询二级系统下所有品牌", response = Response.class)
    @RequestMapping(value = "sel_brand_by_scateid", method = RequestMethod.GET)
    public Response findBrandByScateid(@RequestParam String scateid)  {
        List<Map<String,Object>> brandList = brandService.findBrandByScateid(scateid);
        Response result = new Response();
        result.setData(brandList);
        return result;
    }
}
package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/3/4 17:21
 */
@RestController
@RequestMapping("/boss/advertisement")
public class AdvertisementController {

    @Autowired
    HomepageManageService homepageManageService;

    /**
     * 查询广告列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询广告列表", notes = "用于查询广告列表接口", response = Response.class)
    @RequestMapping(value = "query_advertisement_list", method = RequestMethod.POST)
    public Response queryAdvertisementList(@ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                           @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<HomepageManage> pager = new Paging(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<HomepageManage> list = homepageManageService.queryAdvertisementList(pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 查询广告详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询广告详情", notes = "查询广告详情", response = Response.class)
    @RequestMapping(value = "query_avertisement_particulars", method = RequestMethod.POST)
    public Response queryAvertisementById(@ApiParam(value = "广告id") @RequestParam String id) {
        Response response = new Response();
        HomepageManage particulars = homepageManageService.queryAvertisementById(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(particulars);
        return response;
    }
}

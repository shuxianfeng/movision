package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.HomepageManageFacade;
import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.mybatis.manageType.entity.ManageType;
import com.movision.utils.file.FileUtil;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/3/4 17:21
 */
@RestController
@RequestMapping("/boss/advertisement")
public class AdvertisementController {

    @Autowired
    HomepageManageFacade homepageManageFacade;

    @Autowired
    MovisionOssClient movisionOssClient;

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
        List<HomepageManage> list = homepageManageFacade.queryAdvertisementList(pager);
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
        HomepageManage particulars = homepageManageFacade.queryAvertisementById(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(particulars);
        return response;
    }

    /**
     * 查询广告类型
     *
     * @return
     */
    @ApiOperation(value = "查询广告类型", notes = "用于查询广告类型接口", response = Response.class)
    @RequestMapping(value = "query_advertisement_type", method = RequestMethod.POST)
    public Response queryAdvertisementTypeList() {
        Response response = new Response();
        List<ManageType> type = homepageManageFacade.queryAdvertisementTypeList();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(type);
        return response;
    }

    /**
     * 添加广告
     *
     * @param topictype
     * @param orderid
     * @param content
     * @param subcontent
     * @param url
     * @param transurl
     * @return
     */
    @ApiOperation(value = "添加广告", notes = "用于添加广告接口", response = Response.class)
    @RequestMapping(value = "add_advertisement", method = RequestMethod.POST)
    public Response addAdvertisement(@ApiParam(value = "类型") @RequestParam String topictype,
                                     @ApiParam(value = "排序") @RequestParam(required = false) String orderid,
                                     @ApiParam(value = "主标题") @RequestParam String content,
                                     @ApiParam(value = "副标题") @RequestParam String subcontent,
                                     @ApiParam(value = "图片URL") @RequestParam String url,
                                     @ApiParam(value = "跳转链接URL") @RequestParam String transurl) {
        Response response = new Response();
        int map = homepageManageFacade.addAdvertisement(topictype, orderid, content, subcontent, url, transurl);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 添加广告类型
     *
     * @param type
     * @param name
     * @param wide
     * @param high
     * @param quantity
     * @return
     */
    @ApiOperation(value = "添加广告类型", notes = "用于添加广告类型接口", response = Response.class)
    @RequestMapping(value = "add_advertisement_type", method = RequestMethod.POST)
    public Response addAdvertisementType(@ApiParam(value = "广告类型") @RequestParam String type,
                                         @ApiParam(value = "广告名") @RequestParam String name,
                                         @ApiParam(value = "广告宽度") @RequestParam String wide,
                                         @ApiParam(value = "广告高度") @RequestParam String high,
                                         @ApiParam(value = "广告数量") @RequestParam String quantity) {
        Response response = new Response();
        int i = homepageManageFacade.addAdvertisementType(type, name, wide, high, quantity);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(i);
        return response;
    }

    /**
     * 根据id查询广告类型详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询广告类型", notes = "用于根据广告id查询广告类型详情", response = Response.class)
    @RequestMapping(value = "query_type_id", method = RequestMethod.POST)
    public Response queryAdvertisementTypeById(@ApiParam(value = "类型id") @RequestParam String id) {
        ManageType manageType = homepageManageFacade.queryAdvertisementTypeById(id);
        Response response = new Response();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(manageType);
        return response;
    }

    /**
     * 根据条件查询广告类型名称
     *
     * @param name
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "模糊查询广告类型名称", notes = "用于条件查询广告类型名称接口", response = Response.class)
    @RequestMapping(value = "query_type_like_name", method = RequestMethod.POST)
    public Response queryAdvertisementTypeLikeName(@ApiParam(value = "位置名称") @RequestParam String name,
                                                   @ApiParam(value = "当前第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                   @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<ManageType> pager = new Paging<ManageType>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ManageType> list = homepageManageFacade.queryAdvertisementTypeLikeName(name, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 编辑广告
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "编辑广告", notes = "用于编辑广告接口", response = Response.class)
    @RequestMapping(value = "update_advertisement", method = RequestMethod.POST)
    public Response updateAdvertisement(@ApiParam(value = "广告id") @RequestParam String id,
                                        @ApiParam(value = "类型") @RequestParam String topictype,
                                        @ApiParam(value = "排序") @RequestParam(required = false) String orderid,
                                        @ApiParam(value = "主标题") @RequestParam String content,
                                        @ApiParam(value = "副标题") @RequestParam String subcontent,
                                        @ApiParam(value = "图片URL") @RequestParam String url,
                                        @ApiParam(value = "跳转链接URL") @RequestParam String transurl) {
        Response response = new Response();
        int i = homepageManageFacade.updateAdvertisement(id, topictype, orderid, content, subcontent, url, transurl);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(i);
        return response;
    }

    /**
     * 上传广告相关图片
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传广告相关图片", notes = "上传广告相关图片", response = Response.class)
    @RequestMapping(value = {"/upload_advertisement_img"}, method = RequestMethod.POST)
    public Response updatePostImg(@RequestParam(value = "file", required = false) MultipartFile file) {

        String url = movisionOssClient.uploadObject(file, "img", "advertisement");
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("name", FileUtil.getFileNameByUrl(url));
        return new Response(map);
    }
}

package com.zhuhuibao.business.tech.site;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.aop.LoginAccess;
import com.zhuhuibao.aop.UserAccess;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.tech.entity.DictionaryTechData;
import com.zhuhuibao.mybatis.tech.entity.TechData;
import com.zhuhuibao.mybatis.tech.service.DictionaryTechDataService;
import com.zhuhuibao.mybatis.tech.service.TechDataService;
import com.zhuhuibao.mybatis.tech.service.TechDownloadDataService;
import com.zhuhuibao.utils.CommonUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.file.FileUtil;
import com.zhuhuibao.utils.oss.ZhbOssClient;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RestController
@RequestMapping("/rest/tech/site/data")
@Api(value = "techData", description = "技术资料接口")
public class TechDataController {
    private static final Logger log = LoggerFactory.getLogger(TechDataController.class);

    @Autowired
    TechDataService techDataService;

    @Autowired
    DictionaryTechDataService dicTDService;

    @Autowired
    ApiConstants ApiConstants;

    @Autowired
    TechDownloadDataService dlService;

    @Autowired
    ZhbOssClient zhbOssClient;

    @Autowired
    FileUtil fileUtil;

    @LoginAccess
    @ApiOperation(value = "上传技术资料(行业解决方案，技术文档，培训资料)", notes = "上传技术资料(行业解决方案，技术文档，培训资料)", response = Response.class)
    @RequestMapping(value = "upload_tech_data", method = RequestMethod.POST)
    public Response uploadTechData(@RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        try {
            String url = zhbOssClient.uploadObject(file, "doc", "tech");
            map.put(Constants.name, url);
            if (url.lastIndexOf(".") != -1) {
                map.put(TechConstant.FILE_FORMAT, url.substring(url.lastIndexOf(".")));
            } else {
                map.put(TechConstant.FILE_FORMAT, "");
            }
            map.put(TechConstant.FILE_SIZE, file.getSize());

        } catch (Exception e) {
            log.error("上传技术资料失败>>>", e);
            throw e;
        }

        return new Response(map);
    }

    @RequestMapping(value = "add_Tech_data", method = RequestMethod.POST)
    @ApiOperation(value = "新增技术资料(行业解决方案，技术文档，培训资料)", notes = "新增技术资料(行业解决方案，技术文档，培训资料)", response = Response.class)
    public Response insertTechData(@ApiParam(value = "技术资料:行业解决方案，技术文档，培训资料") @ModelAttribute(value = "techData") TechData techData) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (null != createId) {
            techData.setCreateid(createId);
            int result = techDataService.insertTechData(techData);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @RequestMapping(value = "sel_tech_data", method = RequestMethod.GET)
    @ApiOperation(value = "运营管理平台搜索技术资料", notes = "运营管理平台搜索技术资料", response = Response.class)
    public Response findAllTechDataPager(@ApiParam(value = "1解决方案，2技术资料，3培训资料") @RequestParam(required = false) String fCategory,
                                         @ApiParam(value = "资料标题") @RequestParam(required = false) String title,
                                         @ApiParam(value = "二级分类code") @RequestParam(required = false) String sCategory,
                                         @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                         @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("fCategory", fCategory);
        condition.put("sCategory", sCategory);
        if (null != title && !"".equals(title)) {
            condition.put("title", title.replace("_", "\\_"));
        }
        //审核通过
        condition.put("status", TechConstant.TechDataStatus.AUDITPASS.toString());
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<TechData> pager = new Paging<TechData>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<TechData> techList = techDataService.findAllTechDataPager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value = "sel_second_category", method = RequestMethod.GET)
    @ApiOperation(value = "查询解决方案、技术资料，培训资料行业类别", notes = "查询解决方案、技术资料，培训资料行业类别", response = Response.class)
    public Response selectSecondCategoryByFirstId() {
        Response response = new Response();
        List<Map<String, Object>> categoryList = dicTDService.selectCategoryInfo(0);
        response.setData(categoryList);
        return response;
    }

    //vip减 项目条数减
    @ApiOperation(value = "下载技术资料", notes = "下载技术资料", response = Response.class)
    @RequestMapping(value = "downloadFile", method = RequestMethod.GET)
    public Response downloadBill(HttpServletResponse response,
                                 @ApiParam(value = "技术资料ID") @RequestParam String techDataId) throws Exception {
        Response jsonResult = new Response();
        log.debug("download tech data");
        try {
            Long createId = ShiroUtil.getCreateID();
            if (createId != null) {
                String attachName = techDataService.selectTechDataAttachName(Long.parseLong(techDataId));
                response.setDateHeader("Expires", 0);
                response.setHeader("Cache-Control",
                        "no-store, no-cache, must-revalidate");
                response.addHeader("Cache-Control", "post-check=0, pre-check=0");
                response.setHeader("Content-disposition", "attachment;filename=" + attachName);
                response.setContentType("application/octet-stream");
//                attachName = ApiConstants.getUploadDoc() + TechConstant.UPLOAD_TECH_DOC_URL + "/" + attachName;
                jsonResult = fileUtil.downloadObject(response, attachName, "doc", "tech");
                //插入我的下载资料
                dlService.insertDownloadData(techDataId, createId);
                //更新下载率
                Map<String, Object> map = new HashMap<>();
                map.put("download", "download");
                map.put("id", techDataId);
                techDataService.updateTechDataViewsOrDL(map);
            } else {
                jsonResult.setCode(401);
                jsonResult.setMsgCode(MsgCodeConstant.un_login);
                jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                return jsonResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("download tech data error! ", e);
        }
        return jsonResult;
    }

    @RequestMapping(value = "sel_tech_data_detail", method = RequestMethod.GET)
    @ApiOperation(value = "预览解决方案、技术资料，培训资料详情页面", notes = "预览解决方案、技术资料，培训资料详情页面", response = Response.class)
    public Response previewTechDataDetail(@ApiParam(value = "技术资料ID") @RequestParam() String techDataId) {
        Response response = new Response();
        List<Map<String, String>> techDataList = techDataService.previewTechDataDetail(Long.parseLong(techDataId));

        Map<String, Object> map = new HashMap<>();
        map.put("views", "views");
        map.put("id", techDataId);
        techDataService.updateTechDataViewsOrDL(map);

        response.setData(techDataList);
        return response;
    }

    @RequestMapping(value = "sel_views_order", method = RequestMethod.GET)
    @ApiOperation(value = "查询解决方案、技术资料，培训资料的点击排行", notes = "查询解决方案、技术资料，培训资料的点击排行", response = Response.class)
    public Response findDataViewsOrder(@ApiParam(value = "分类：1：行业解决方案，2：技术资料，3：培训资料") @RequestParam Integer categoryId) {
        Response response = new Response();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", TechConstant.TechDataStatus.AUDITPASS.toString());
        map.put("categoryId", categoryId);
        map.put("count", TechConstant.DATA_VIEWS_COUNT_TEN);
        List<Map<String, String>> techDataList = techDataService.findDataViewsOrder(map);
        response.setData(techDataList);
        return response;
    }

    @RequestMapping(value = "sel_download_order", method = RequestMethod.GET)
    @ApiOperation(value = "查询解决方案、技术资料，培训资料的下载排行", notes = "查询解决方案、技术资料，培训资料的下载排行", response = Response.class)
    public Response findDownloadOrder(@ApiParam(value = "分类：1：行业解决方案，2：技术资料，3：培训资料") @RequestParam Integer categoryId) {
        Response response = new Response();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", TechConstant.TechDataStatus.AUDITPASS.toString());
        map.put("categoryId", categoryId);
        map.put("count", TechConstant.DATA_DOWNLOAD_COUNT_TEN);
        List<Map<String, String>> techDataList = techDataService.findDownloadOrder(map);
        response.setData(techDataList);
        return response;
    }


    @ApiOperation(value = "解决方案，技术资料，培训资料 列表(分页)", notes = "解决方案，技术资料，培训资料 列表(分页)", response = Response.class)
    @RequestMapping(value = "list_tech_data", method = RequestMethod.GET)
    public Response listTechDataPage(@ApiParam(value = "类别ID:1:解决方案 2:技术资料 3:培训资料") @RequestParam String fcateId,
                                     @ApiParam(value = "子类别ID") @RequestParam(required = false) String scateId,
                                     @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                     @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) {

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("fCategory", fcateId);
        if (StringUtils.isEmpty(scateId)) {
            conditionMap.put("sCategory", scateId);
        }

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String, String>> techList = techDataService.findAllTechDataOnlyPager(pager, conditionMap);
        pager.result(techList);

        return new Response(pager);
    }

    @ApiOperation(value = "根据父类查询子类别信息", notes = "根据父类查询子类别信息", response = Response.class)
    @RequestMapping(value = "sel_scate_byfid", method = RequestMethod.GET)
    public Response getScategoryByFid(@ApiParam(value = "类别ID:1:解决方案 2:技术资料 3:培训资料") @RequestParam String fcateId) {

        List<DictionaryTechData> slist = dicTDService.getSecondCategory(Integer.valueOf(fcateId));
        return new Response(slist);
    }
}

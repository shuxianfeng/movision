package com.movision.controller.boss.parse;

import com.movision.common.Response;
import com.movision.facade.boss.PostFacade;
import com.movision.facade.boss.XmlParseFacade;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @Author zhurui
 * @Date 2017/10/24 11:42
 */
@RestController
@RequestMapping("/boss/xmlparse")
public class XmlParseController {

    @Autowired
    private XmlParseFacade xmlParseFacade;

    /**
     * xml文件解析
     *
     * @param file
     * @param nickname
     * @param phone
     * @return
     */
    @ApiOperation(value = "解析xml文件", notes = "用于解析xml文件", response = Response.class)
    @RequestMapping(value = "/analysis_xml", method = RequestMethod.POST)
    public Response xmlParse(HttpServletRequest request,
                             @ApiParam(value = "文件") @RequestParam MultipartFile file,
                             @ApiParam(value = "用户昵称") @RequestParam String nickname,
                             @ApiParam(value = "手机号") @RequestParam String phone,
                             @ApiParam(value = "圈子id") @RequestParam String circleid) {
        Response response = new Response();
        Map map = xmlParseFacade.analysisXml(request, file, nickname, phone, circleid);
        if (map.get("code").equals(200)) {
            response.setMessage("操作成功");
            response.setData(map);
        } else if (map.get("code").equals(300)) {
            response.setMessage("新增用户失败");
            response.setData(map);
        } else {
            response.setData(map);
        }
        return response;
    }


    /**
     * 帖子导出
     *
     * @return
     */
    @ApiOperation(value = "xml解析的帖子导出Excel", notes = "用于帖子导出", response = Response.class)
    @RequestMapping(value = "/export_excel", method = RequestMethod.POST)
    public Response excelExport(@ApiParam(value = "圈子id") @RequestParam String circleid) {
        Response response = new Response();
        Map map = xmlParseFacade.exportExcel(circleid);
        response.setMessage("操作成功");
        response.setData(map);
        return response;
    }

    /**
     * 查询帖子列表
     *
     * @return
     */
    @ApiOperation(value = "查询xml解析出的帖子列表", notes = "用于查询xml解析出的帖子列表", response = Response.class)
    @RequestMapping(value = "/query_xml_analysis_post_list", method = RequestMethod.POST)
    public Response queryXmlAnalysisAndPost(@ApiParam(value = "圈子id") @RequestParam(required = false) String circleid,
                                            @ApiParam(value = "当前页") @RequestParam(defaultValue = "1") String pageNo,
                                            @ApiParam(value = "每页几条") @RequestParam(defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostList> pag = new Paging<PostList>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostList> postVos = xmlParseFacade.queryXmlAnalysisAndPost(pag, circleid);
        response.setMessage("查询成功");
        pag.result(postVos);
        response.setData(pag);
        return response;
    }


    /**
     * 导入Excel
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "导入Excel文件", notes = "用于解析EXCEL文件，修改帖子")
    @RequestMapping(value = "/input_excel_post", method = RequestMethod.POST)
    public Response inputExcelToPost(HttpServletRequest request,
                                     @ApiParam(value = "Excel文件") @RequestParam MultipartFile file,
                                     @ApiParam(value = "导入Excel文件处理方式，xml：xml解析后，运营修改的Excel文件，Excel：Excel直接导入") @RequestParam String type) {
        Response response = new Response();
        Map map = xmlParseFacade.inputExcelToPost(request, file, type);
        if (map.get("code").equals("200")) {
            response.setMessage("操作成功");
            response.setData(map);
        } else {
            response.setMessage("操作失败");
            response.setData(map);
        }
        return response;
    }


}

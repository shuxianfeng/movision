package com.movision.controller.boss.parse;

import com.movision.common.Response;
import com.movision.facade.boss.PostFacade;
import com.movision.facade.boss.XmlParseFacade;
import com.movision.mybatis.post.entity.Post;
import com.movision.utils.oss.MovisionOssClient;
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
    public Response xmlParse(@ApiParam(value = "文件") @RequestParam MultipartFile file,
                             @ApiParam(value = "用户昵称") @RequestParam String nickname,
                             @ApiParam(value = "手机号") @RequestParam String phone) {
        Response response = new Response();
        Map map = xmlParseFacade.analysisXml(file, nickname, phone);
        response.setMessage("操作成功");
        response.setData(map);
        return response;
    }


}

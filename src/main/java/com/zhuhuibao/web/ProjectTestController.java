package com.zhuhuibao.web;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.project.entity.ProjectTest;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.mybatis.project.service.ProjectTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cxx on 2016/7/22 0022.
 */
@RestController
@RequestMapping("/rest/project/test")
public class ProjectTestController {

    @Autowired
    ProjectTestService projectTestService;

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "change",method = RequestMethod.POST)
    @ApiOperation(value = "项目信息描述内容提取转换",notes = "项目信息描述内容提取转换",response = Response.class)
    public Response change(@RequestParam(required = false)int begin,@RequestParam(required = false)int end) {
        Response response = new Response();
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null && createid == 11){
            Map<String,Object> map = new HashMap<>();
            map.put("begin",begin);
            map.put("end",end);
            List<Map<String,String>> list = projectService.queryDescription(map);
            for(Map result:list){
                String old = result.get("description").toString();
                String areaSize = "";
                String desc = "";
                String background = "";
                String remark = "";
                if(old.contains("建筑面积")){
                    areaSize = old.substring(old.indexOf("建筑面积")).substring(4,20).replace(",","");
                    Pattern p = Pattern.compile("(\\d+)");
                    Matcher m = p.matcher(areaSize);
                    if(m.find()){
                        areaSize = m.group(1);
                    }
                    if(areaSize.contains("万")){
                        areaSize = m.group(1)+"万";
                    }
                }

                if(old.contains("部分建材及配置")){
                    desc = old.substring(0,old.indexOf("部分建材及配置"));
                }

                if(old.contains("备注：")){
                    if(old.contains("项目背景：")){
                        if(old.indexOf("备注：")<old.lastIndexOf("项目背景：")){
                            remark = old.substring(old.indexOf("备注："),old.lastIndexOf("项目背景：")).substring(3);
                        }
                    }
                }

                if(old.contains("项目背景")){
                    if(old.contains("审批手续进展情况")){
                        if(old.indexOf("项目背景：")<old.lastIndexOf("审批手续进展情况")){
                            background = old.substring(old.indexOf("项目背景："),old.lastIndexOf("审批手续进展情况")).substring(5);
                        }
                    }
                }
                ProjectTest projectTest = projectTestService.query(Integer.parseInt(result.get("id").toString()));
                if(projectTest==null){
                    ProjectTest test = new ProjectTest();
                    test.setAreaSize(areaSize);
                    test.setDescription(desc);
                    test.setRemark(remark);
                    test.setBackground(background);
                    test.setId(Integer.parseInt(result.get("id").toString()));
                    projectTestService.insert(test);
                }
            }
        }else {
            throw new AuthException(400, "你想干嘛？你木有权限哟，哈哈，妈的智障！");
        }
        return response;
    }
}
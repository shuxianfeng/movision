package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.project.entity.ProjectClues;
import com.zhuhuibao.service.ProjectCluesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目线索controller
 * 
 * @author tongxinglong
 * @date 2016/10/31 0031.
 */
@RestController
@RequestMapping("/rest/m/projectClues/site/")
public class MobileProjectCluesController {

    @Autowired
    private ProjectCluesService cluesService;

    @RequestMapping(value = "/add_projectClues", method = RequestMethod.POST)
    @ApiOperation(value = "添加项目线索信息", notes = "添加项目线索信息", response = Response.class)
    public Response addProjectClues(@ModelAttribute ProjectClues projectClues) {
        cluesService.addProjectClues(projectClues);
        return new Response();
    }
}

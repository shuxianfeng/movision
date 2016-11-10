package com.zhuhuibao.business.project.oms;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.project.entity.ProjectClues;
import com.zhuhuibao.mybatis.project.form.ProjectCluesForm;
import com.zhuhuibao.service.ProjectCluesService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 项目线索相关业务控制层
 *
 * @author liyang
 * @date 2016年10月28日
 */
@RestController
@RequestMapping("/rest/projectClues/oms/")
@Api(value = "projectClues", description = "项目线索")
public class ProjectCluesController {
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtils.DEFAULT_DATE_FORMAT, true));
    }

    private static final Logger log = LoggerFactory.getLogger(ProjectCluesController.class);

    @Autowired
    private ProjectCluesService cluesService;

    @RequestMapping(value = "/sel_projectClues_list", method = RequestMethod.GET)
    @ApiOperation(value = "分页查询项目线索相关信息", notes = "分页查询项目线索相关信息", response = Response.class)
    public Response selProjectCluesList(@ApiParam(value = "提交时间开始时间") @RequestParam(required = false) String addTimeStart, @ApiParam(value = "提交时间结束时间") @RequestParam(required = false) String addTimeEnd,
            @ApiParam(value = "状态") @RequestParam(required = false) String status, @ApiParam(value = "页码") @RequestParam(required = false) int pageNo,
            @ApiParam(value = "每页显示的数目") @RequestParam(required = false) int pageSize) {
        Response response = new Response();
        Paging<ProjectCluesForm> pager = new Paging<>(pageNo, pageSize);
        try {
            List<ProjectCluesForm> list = cluesService.selProjectCluesList(addTimeStart, addTimeEnd, status, pager);
            pager.result(list);
            response.setData(pager);
        } catch (Exception e) {
            log.error("sel_projectClues_list error! ", e);
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping(value = "/add_projectClues", method = RequestMethod.POST)
    @ApiOperation(value = "添加项目线索信息", notes = "添加项目线索信息", response = Response.class)
    public Response addProjectClues(@ModelAttribute ProjectClues projectClues) {
        cluesService.addProjectClues(projectClues);
        return new Response();
    }

    @RequestMapping(value = "/upd_projectClues", method = RequestMethod.POST)
    @ApiOperation(value = "更新项目线索信息", notes = "更新项目线索信息", response = Response.class)
    public Response updProjectClues(@ModelAttribute ProjectClues projectClues) {
        cluesService.updateProjectClues(projectClues);
        return new Response();
    }

    @RequestMapping(value = "/del_projectClues", method = RequestMethod.POST)
    @ApiOperation(value = "删除项目线索信息", notes = "删除项目线索信息", response = Response.class)
    public Response delProjectClues(@ApiParam(value = "主键id") @RequestParam(required = false) int id) {
        cluesService.deleteProjectClues(id);
        return new Response();
    }

    @RequestMapping(value = "/sel_projectClues", method = RequestMethod.GET)
    @ApiOperation(value = "查询项目线索信息", notes = "查询项目线索信息", response = Response.class)
    public Response selProjectClues(@ApiParam(value = "主键id") @RequestParam(required = false) int id) {
        Response response = new Response();
        response.setData(cluesService.selectProjectClues(id));
        return response;
    }

}

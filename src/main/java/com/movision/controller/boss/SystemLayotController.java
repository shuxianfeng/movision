package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.SystemLayoutFacade;
import com.movision.mybatis.systemLayout.entity.SystemLayout;
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
 * @Date 2017/10/11 11:41
 */
@RestController
@RequestMapping("/boss/system/layout")
public class SystemLayotController {

    @Autowired
    private SystemLayoutFacade systemLayoutFacade;


    /**
     * 新增系统配置
     *
     * @param module
     * @param type
     * @param value
     * @param describe
     * @return
     */
    @ApiOperation(value = "新增系统配置", notes = "新增系统配置", response = Response.class)
    @RequestMapping(value = "insertSystemLayout", method = RequestMethod.POST)
    public Response insertSystemLayout(@ApiParam(value = "所属模块") @RequestParam String module,
                                       @ApiParam(value = "类型名称") @RequestParam String type,
                                       @ApiParam(value = "值") @RequestParam String value,
                                       @ApiParam(value = "描述") @RequestParam String describe) {
        Response response = new Response();
        systemLayoutFacade.insertSystemLayout(module, type, value, describe);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    /**
     * 条件查询系统配置列表
     *
     * @param type
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询全部配置", notes = "根据条件查询全部配置", response = Response.class)
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    public Response querySystemLayotAll(@ApiParam(value = "类型") @RequestParam(required = false) String type,
                                        @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                        @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<SystemLayout> pag = new Paging(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<SystemLayout> list = systemLayoutFacade.querySystemLayotAll(type, pag);
        pag.result(list);
        response.setMessage("查询成功");
        response.setData(pag);
        return response;
    }

    /**
     * 根据id查询系统配置
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询系统配置", notes = "根据id查询系统配置", response = Response.class)
    @RequestMapping(value = "/queryDetails", method = RequestMethod.POST)
    public Response querySystemLayoutById(@ApiParam(value = "id") @RequestParam String id) {
        Response response = new Response();
        SystemLayout layout = systemLayoutFacade.querySystemLayoutById(id);
        response.setMessage("查询成功");
        response.setData(layout);
        return response;
    }

    /**
     * 根据id修改系统配置
     *
     * @param id
     * @param value
     * @param module
     * @param type
     * @param describe
     * @return
     */
    @ApiOperation(value = "编辑系统配置", notes = "根据id修改系统配置", response = Response.class)
    @RequestMapping(value = "/updateSystemLayoutById", method = RequestMethod.POST)
    public Response updateSystemLayoutById(@ApiParam(value = "id") @RequestParam String id,
                                           @ApiParam(value = "值") @RequestParam(required = false) String value,
                                           @ApiParam(value = "模块") @RequestParam(required = false) String module,
                                           @ApiParam(value = "类型名称") @RequestParam(required = false) String type,
                                           @ApiParam(value = "描述") @RequestParam(required = false) String describe) {
        Response response = new Response();
        systemLayoutFacade.updateSystemLayoutById(id, module, type, value, describe);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    /**
     * 删除系统配置
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id删除系统配置", notes = "根据id删除系统配置", response = Response.class)
    @RequestMapping(value = "deleteSystemLayoutById", method = RequestMethod.POST)
    public Response deleteSystemLayoutById(@ApiParam(value = "id") @RequestParam String id) {
        Response response = new Response();
        systemLayoutFacade.deleteSystemLayoutById(id);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }
}

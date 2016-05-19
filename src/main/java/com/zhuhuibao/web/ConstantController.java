package com.zhuhuibao.web;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.util.ConvertUtil;
import com.zhuhuibao.mybatis.constants.entity.Constant;
import com.zhuhuibao.mybatis.constants.service.ConstantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常量相关接口
 *
 * @author jianglz
 * @version 2016-05-19
 */
@RestController
@RequestMapping("/rest/const")
@Api(value = "constant", description = "常量")
public class ConstantController {
    private static final Logger log = LoggerFactory.getLogger(ConstantController.class);

    @Autowired
    ConstantService service;


    @RequestMapping(value = "/findByType", method = RequestMethod.GET)
    @ApiOperation(value = "根据类型查询", notes = "根据类型查询", response = JsonResult.class)
    public JsonResult findByType(@ApiParam(value = "类型") @RequestParam String type) {
        JsonResult result = new JsonResult();
        try {
            List<Constant> list = service.findByType(type);
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return result;
    }


    @RequestMapping(value = "/findJobByID", method = RequestMethod.GET)
    @ApiOperation(value = "测试", notes = "测试", response = JsonResult.class)
    public JsonResult findJobByID(@ApiParam(value = "id") @RequestParam String id) {
        JsonResult result = new JsonResult();
        try {

            Map<String, Object> map = service.findJobByID(id);
            map = ConvertUtil.execute(map, "positionType", "jobPositionService", "findById", new Object[]{String.valueOf(map.get("positionType"))});
            map = ConvertUtil.execute(map, "salary", "constantService", "findByTypeCode", new Object[]{"1", String.valueOf(map.get("salary"))});

            result.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return result;
    }

}

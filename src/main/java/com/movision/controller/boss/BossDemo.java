package com.movision.controller.boss;

import com.movision.common.Response;
import com.wordnik.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("rest/common/oms")
public class BossDemo {

    private static final Logger log = LoggerFactory.getLogger(BossDemo.class);

    @ApiOperation(value = "地区代理信息列表(运营分页)", notes = "地区代理信息列表(运营分页)", response = Response.class)
    @RequestMapping(value = "sel_joinus_page", method = RequestMethod.GET)
    public Response listJoinus(
            @RequestParam(required = false, defaultValue = "1") String pageNo,
            @RequestParam(required = false, defaultValue = "10") String pageSize) {
        log.debug("find all joinus pager....");

        return new Response();
    }


}

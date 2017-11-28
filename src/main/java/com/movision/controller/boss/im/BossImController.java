package com.movision.controller.boss.im;

import com.movision.common.Response;
import com.movision.facade.im.ImFacade;
import com.movision.mybatis.imUserAccusation.entity.ImUserAccPage;
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
 * @Author zhuangyuhao
 * @Date 2017/11/28 10:05
 */
@RestController
@RequestMapping("boss/im")
public class BossImController {

    @Autowired
    private ImFacade imFacade;

    @RequestMapping(value = "get_im_user_accusation_page", method = RequestMethod.GET)
    @ApiOperation(value = "IM用户举报列表（分页）", notes = "IM用户举报列表（分页）", response = Response.class)
    public Response getImAccusationPage(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                        @RequestParam(required = false, defaultValue = "10") String pageSize,
                                        @ApiParam(value = "举报状态，String类型。0：未处理，1：已处理") @RequestParam(required = false) String type) {
        Response response = new Response();
        Paging<ImUserAccPage> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ImUserAccPage> list = imFacade.queryImAccusationForPage(pager, type);
        pager.result(list);
        response.setData(pager);
        return response;
    }
}

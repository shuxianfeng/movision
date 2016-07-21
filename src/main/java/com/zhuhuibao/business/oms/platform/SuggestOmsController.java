package com.zhuhuibao.business.oms.platform;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.common.service.SuggestService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/7/21 0021.
 */
@RestController
@RequestMapping("/rest/sys/oms/suggest")
public class SuggestOmsController {

    @Autowired
    SuggestService suggestService;

    @RequestMapping(value="sel_suggestList", method = RequestMethod.GET)
    @ApiOperation(value = "意见反馈列表",notes = "意见反馈列表",response = Response.class)
    public Response sel_suggestList(@RequestParam(required = false)String title,
                                    @RequestParam(required = false)String status,
                                    @RequestParam(required = false)String type,
                                    @RequestParam(required = false)String pageNo,
                                    @RequestParam(required = false)String pageSize) throws IOException
    {
        Response response = new Response();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        map.put("title",title);
        map.put("status",status);
        map.put("type",type);
        List<Map<String,String>> list = suggestService.findAllSuggest(pager,map);
        pager.result(list);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value="sel_suggest", method = RequestMethod.GET)
    @ApiOperation(value = "意见反馈详情",notes = "意见反馈详情",response = Response.class)
    public Response sel_suggest(@RequestParam String id) throws IOException
    {
        Response response = new Response();
        Map<String,String> map = suggestService.querySuggestById(id);
        response.setData(map);
        return response;
    }

    @RequestMapping(value="upd_suggest", method = RequestMethod.POST)
    @ApiOperation(value = "意见反馈处理",notes = "意见反馈处理",response = Response.class)
    public Response upd_suggest(@RequestParam String id,@RequestParam String status) throws IOException
    {
        Response response = new Response();
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("status",status);
        suggestService.updateSuggest(map);
        return response;
    }
}

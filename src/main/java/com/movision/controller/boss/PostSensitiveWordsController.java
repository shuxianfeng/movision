package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.PostSensitiveWordsFacade;
import com.movision.mybatis.postSensitiveWords.entity.PostSensitiveWords;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/4/26 10:12
 */
@RestController
@RequestMapping("/boss/postsensitivewords")
public class PostSensitiveWordsController {

    @Autowired
    private PostSensitiveWordsFacade postSensitiveWordsFacade;

    /**
     * 查询脱敏列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询脱敏列表（分页）", notes = "查询脱敏列表（分页）", response = Response.class)
    @RequestMapping(value = "/query_postsenti_list", method = RequestMethod.POST)
    public Response findAllPostSensitiveWords(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                              @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostSensitiveWords> pager = new Paging<PostSensitiveWords>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostSensitiveWords> list = postSensitiveWordsFacade.findAllPostSensitiveWords(pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "增加脱敏", notes = "增加脱敏", response = Response.class)
    @RequestMapping(value = "/add_postsenti", method = RequestMethod.POST)
    public Response insert(@ApiParam(value = "脱敏词") @RequestParam(required = false) String name) {
        Response response = new Response();
        Map<String, Object> map = postSensitiveWordsFacade.insert(name);
        if (response.getCode() == 200) {
            response.setData(map);
        }
        return response;
    }

    @ApiOperation(value = "修改脱敏", notes = "修改脱敏", response = Response.class)
    @RequestMapping(value = "/update_postsenti", method = RequestMethod.POST)
    public Response updateByPrimaryKeySelective(@ApiParam(value = "脱敏id") @RequestParam String id,
                                                @ApiParam(value = "脱敏词") @RequestParam(required = false) String name,
                                                @ApiParam(value = "脱敏时间") @RequestParam(required = false) String intime) {
        Response response = new Response();
        Map<String, Object> map = postSensitiveWordsFacade.updateByPrimaryKeySelective(id, name, intime);
        if (response.getCode() == 200) {
            response.setData(map);
        }
        return response;
    }


    @ApiOperation(value = "删除脱敏", notes = "删除脱敏", response = Response.class)
    @RequestMapping(value = "/delete_postsenti", method = RequestMethod.POST)
    public Response deleteByPrimaryKey(@ApiParam(value = "脱敏id") @RequestParam Integer id) {
        Response response = new Response();
        int result = postSensitiveWordsFacade.deleteByPrimaryKey(id);
        if (response.getCode() == 200) {
            response.setData(result);
        }
        return response;
    }


    @ApiOperation(value = "查询脱敏列表（分页）", notes = "查询脱敏列表（分页）", response = Response.class)
    @RequestMapping(value = "/query_postsenti_condition", method = RequestMethod.POST)
    public Response findAllPostCodition(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                        @RequestParam(required = false, defaultValue = "10") String pageSize,
                                        @ApiParam(value = "脱敏词") @RequestParam(required = false) String name) {
        Response response = new Response();
        Paging<PostSensitiveWords> pager = new Paging<PostSensitiveWords>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        List<PostSensitiveWords> list = postSensitiveWordsFacade.findAllPostCodition(name, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }
}

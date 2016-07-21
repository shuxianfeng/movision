package com.zhuhuibao.business.system.suggest;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.common.entity.Suggest;
import com.zhuhuibao.mybatis.common.service.SuggestService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by cxx on 2016/7/21 0021.
 */
@RestController
@RequestMapping("/rest/sys/site/suggest")
public class SuggestController {

    @Autowired
    private SuggestService suggestService;

    @RequestMapping(value="add_suggest", method = RequestMethod.POST)
    @ApiOperation(value = "提交意见反馈",notes = "提交意见反馈",response = Response.class)
    public Response add_suggest(@ModelAttribute Suggest suggest) throws IOException
    {
        Response response = new Response();
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            suggestService.addSuggest(suggest);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }
}

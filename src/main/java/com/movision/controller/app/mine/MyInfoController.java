package com.movision.controller.app.mine;

import com.movision.common.Response;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.Goods.GoodsFacade;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.role.entity.Role;
import com.movision.mybatis.user.entity.RegisterUser;
import com.movision.shiro.realm.ShiroRealm;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/24 20:27
 */
@RestController
@RequestMapping("app/mine/")
public class MyInfoController {

    @Autowired
    private GoodsFacade goodsFacade;

    @ApiOperation(value = "查询我的达人页信息", notes = "查询我的达人页信息", response = Response.class)
    @RequestMapping(value = {"/get_my_info"}, method = RequestMethod.POST)
    public Response getMyInfo(@RequestParam(required = false, defaultValue = "1") String goodPageNo,
                              @RequestParam(required = false, defaultValue = "10") String goodPageSize,
                              @RequestParam(required = false, defaultValue = "1") String postPageNo,
                              @RequestParam(required = false, defaultValue = "10") String postPageSize) throws Exception {
        Response response = new Response();
        //获取当前用户信息
        ShiroRealm.ShiroUser user = ShiroUtil.getAppUser();
        //获取当前用户id
        int userid = ShiroUtil.getAppUserID();
        //todo 获取最喜欢的商品
        Paging<Goods> goodsPaging = new Paging<>(Integer.valueOf(goodPageNo), Integer.valueOf(goodPageSize));
        List<Goods> goodsList = goodsFacade.findAllMyCollectGoods(goodsPaging, userid);
        goodsPaging.result(goodsList);

        //todo 获取收藏的精选：帖子+活动

        Map map = new HashedMap();
        map.put("user", user);
        map.put("collect_goods", goodsPaging);

        response.setData(map);
        return response;
    }

}

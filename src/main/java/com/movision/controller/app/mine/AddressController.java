package com.movision.controller.app.mine;

import com.movision.common.Response;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.address.AddressFacade;
import com.movision.mybatis.address.entity.Address;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 19:58
 */
@RestController
@RequestMapping("app/mine/")
public class AddressController {

    @Autowired
    private AddressFacade addressFacade;

    @ApiOperation(value = "获取我的地址列表", notes = "获取我的地址列表", response = Response.class)
    @RequestMapping(value = {"/get_my_address_list"}, method = RequestMethod.GET)
    public Response getMyAddressList() {
        Response response = new Response();
        List<Map<String, Object>> list = addressFacade.queryMyAddressList(ShiroUtil.getAppUserID());
        response.setData(list);
        return response;
    }

    @ApiOperation(value = "添加我的收获地址", notes = "添加我的收获地址", response = Response.class)
    @RequestMapping(value = {"/add_my_address"}, method = RequestMethod.POST)
    public Response addMyAddress(@ApiParam @ModelAttribute Address address) {
        Response response = new Response();
        addressFacade.addMyAddress(address);
        return response;
    }

}

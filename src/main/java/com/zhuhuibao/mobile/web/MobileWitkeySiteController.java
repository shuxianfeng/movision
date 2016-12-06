package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.constants.service.ConstantService;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.payment.service.PaymentGoodsService;
import com.zhuhuibao.mybatis.witkey.entity.Cooperation;
import com.zhuhuibao.service.MobileWitkeyService;
import com.zhuhuibao.service.payment.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Mobile witkey controller.
 *
 * @author zhuangyuhao
 * @date 2016 -11-21 17:06:25
 */
@RestController
@RequestMapping("/rest/m/witkey/site/")
public class MobileWitkeySiteController extends BaseController {

    @Autowired
    ConstantService service;
    @Autowired
    private MobileWitkeyService mWitkeySV;
    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentGoodsService goodsService;

    @ApiOperation(value = "查询任务列表（前台分页）", notes = "查询任务列表（前台分页）", response = Response.class)
    @RequestMapping(value = "sel_witkeyList", method = RequestMethod.GET)
    public Response findAllCooperationByPager(@ApiParam @RequestParam(required = false, defaultValue = "1") String pageNo,
            @ApiParam @RequestParam(required = false, defaultValue = "10") String pageSize, @ApiParam(value = "合作类型") @RequestParam(required = false) String type,
            @ApiParam(value = "项目类别") @RequestParam(required = false) String category, @ApiParam(value = "系统分类") @RequestParam(required = false) String systemType,
            @ApiParam(value = "省") @RequestParam(required = false) String province, @ApiParam(value = "关键字") @RequestParam(required = false) String smart,
            @ApiParam(value = "发布类型，1：接任务，2：接服务，3：资质合作") @RequestParam String parentId, @ApiParam(value = "发布人，0：个人，1：公司") @RequestParam(required = false) String publisher) {

        Response Response = new Response();
        Response.setData(mWitkeySV.getPager(pageNo, pageSize, type, category, systemType, province, smart, parentId, publisher));
        return Response;
    }

    @ApiOperation(value = "留言", notes = "留言", response = Response.class)
    @RequestMapping(value = "add_message", method = RequestMethod.POST)
    public Response message(@ModelAttribute Message message) {
        Response response = new Response();
        mWitkeySV.addMessage(message);
        return response;
    }

    @ApiOperation(value = "威客信息詳情", notes = "威客信息詳情", response = Cooperation.class)
    @RequestMapping(value = "getCoopDetail", method = RequestMethod.GET)
    public Response cooperationInfo(@ApiParam(value = "威客id") @RequestParam Long id, @ApiParam(value = "威客大类，1：任务；2：服务；3：资质合作") @RequestParam String type) throws Exception {
        Map map = mWitkeySV.getWitkeyDetail(id, type);
        getPrivilegeGoodsDetails(map, null, ZhbConstant.ZhbGoodsType.CKWKRW);
        if (null != ShiroUtil.getCreateID() && type.equals("1")) {
            Map<String, Object> con = new HashMap<>();
            con.put("goodsId", id);
            con.put("companyId", ShiroUtil.getCreateID());
            con.put("type", ZhbConstant.ZhbGoodsType.CKWKRW);
            // 判断信息是否已经购买成功
            int viewNumber = goodsService.checkIsViewGoods(con);
            if (viewNumber == 0) {
                map.put("payment", ZhbPaymentConstant.PAY_ZHB_NON_PURCHASE);

            } else {
                map.put("payment", ZhbPaymentConstant.PAY_ZHB_PURCHASE);
            }
        }
        return new Response(map);
    }

    @ApiOperation(value = "威客首页", notes = "威客首页", response = Cooperation.class)
    @RequestMapping(value = "getWitkeyIndexInfo", method = RequestMethod.GET)
    public Response getWitkeyIndexInfo() {
        Response response = new Response();
        // 获取任务。服务。资质合作数据
        response.setData(mWitkeySV.getWitkeyAllInfo());
        return response;
    }

    @RequestMapping(value = "findProjectAndSystem", method = RequestMethod.GET)
    @ApiOperation(value = "获取系统类型和项目类别", notes = "获取系统类型和项目类别", response = Response.class)
    public Response findByType() {

        return new Response(mWitkeySV.getTypeList());
    }

}

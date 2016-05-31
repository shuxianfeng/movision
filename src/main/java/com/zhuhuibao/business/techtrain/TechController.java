package com.zhuhuibao.business.techtrain;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.alipay.service.direct.AlipayDirectService;
import com.zhuhuibao.alipay.service.refund.AlipayRefundService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.pojo.OrderReqBean;
import com.zhuhuibao.common.pojo.RefundItem;
import com.zhuhuibao.common.pojo.RefundReqBean;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.mybatis.order.entity.Order;
import com.zhuhuibao.mybatis.order.service.OrderService;
import com.zhuhuibao.mybatis.techtrain.entity.TechData;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.utils.CommonUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.ValidateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import com.zhuhuibao.mybatis.techtrain.entity.TechCooperation;
import com.zhuhuibao.mybatis.techtrain.service.TechnologyService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 技术培训
 */
@RestController
@RequestMapping("/rest/tech")
@Api(value = "techApi", description = "技术培训接口")
public class TechController {

    private static final Logger log = LoggerFactory.getLogger(TechController.class);

    private static final String ALIPAY_GOODS_TYPE = "0";//虚拟类商品

    private static final String PARTNER = AlipayPropertiesLoader.getPropertyValue("partner");

    @Autowired
    AlipayDirectService alipayDirectService;

    @Autowired
    AlipayRefundService alipayRefundService;

    @Autowired
    OrderService orderService;

    @Autowired
    TechnologyService techService;

    @Autowired
    private UploadService uploadService;

    @ApiOperation(value = "培训课程下单支付", notes = "培训课程下单支付")
    @RequestMapping(value = "course/pay", method = RequestMethod.POST)
    public void doPay(HttpServletRequest request, HttpServletResponse response,
                      @ApiParam @ModelAttribute(value = "order") OrderReqBean order) throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(order);

        if ("true".equals(order.getNeedInvoice())) {
            String invoiceTitle = order.getInvoiceTitle();
            if (invoiceTitle == null) {
                log.error("已选需要发票,发票抬头不能为空");
                throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "已选需要发票,发票抬头不能为空");
            }
            String invoiceType = order.getInvoiceType();
            if (invoiceType == null) {
                log.error("已选需要发票,发票类型不能为空");
                throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "已选需要发票,发票类型不能为空");
            }
        }

        log.info("技术培训下单页面,请求参数:{}", json);
        Map paramMap = gson.fromJson(json, Map.class);
        //特定参数
        paramMap.put("exterInvokeIp", ValidateUtils.getIpAddr(request));//客户端IP地址
        paramMap.put("goods_type", ALIPAY_GOODS_TYPE);//商品类型
        paramMap.put("partner", PARTNER);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号

        log.debug("调用立即支付接口......");

        //需要判断购买数量是否>=产品剩余数量
        alipayDirectService.doPay(response, paramMap);
    }


    //detail_data 退款详细数据 必填(支付宝交易号^退款金额^备注)多笔请用#隔开

    /**
     * 批量退款接口
     *
     * @param response
     * @param data
     */
    @ApiOperation(value = "培训课程批量退款", notes = "培训课程批量退款")
    @RequestMapping(value = "course/refund", method = RequestMethod.POST)
    public void doRefund(HttpServletResponse response, @ApiParam @ModelAttribute(value = "data") RefundReqBean data) throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(data);
        log.info("技术培训批量退款页面,请求参数:{}", json);
        Map paramMap = gson.fromJson(json, Map.class);
        paramMap.put("partner", PARTNER);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号

        //拼装请求参数
        //orderNos  operatorId refundDate totalFee batchNum   detailData
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);

        List<RefundItem> items = data.getItems();
        if (items.size() > 0) {
            paramMap.put("batchNum", items.size());
            OMSRealm.ShiroOmsUser user = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
            if (user == null) {
                log.error("用户未登陆");
                throw new AuthException(MsgCodeConstant.un_login,
                        MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
            paramMap.put("operatorId", user.getId());
            List<String> orderNoList = new ArrayList<>();
            BigDecimal totalFee = new BigDecimal(0);
            List<String> detailList = new ArrayList<>();
            for (RefundItem item : items) {
                String reason = item.getReason();
                if (reason.contains("^") || reason.contains("|") || reason.contains("$") ||
                        reason.contains("#")) {
                    log.error("退款理由中不能包含 '^' ,'|', '$' ,'#' 等特殊字符");
                    throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR,
                            "退款理由中不能包含 '^' ,'|', '$' ,'#' 等特殊字符");
                }
                Order order = orderService.findByOrderNo(item.getOrderNo());
                detailList.add(String.valueOf(order.getBuyerId()) + "^" + item.getFee() + "^" + reason);

                orderNoList.add(item.getOrderNo());
                totalFee.add(new BigDecimal(item.getFee()));

            }
            String orderNos = CommonUtils.splice(orderNoList, ",");
            paramMap.put("orderNos", orderNos);
            paramMap.put("totalFee", totalFee.toString());
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            paramMap.put("refundDate", sf.format(new Date()));

            //detailData 多条   原付款支付宝交易号^退款总金额^退款理由 #  原付款支付宝交易号^退款总金额^退款理由
            String detailData = CommonUtils.splice(detailList, "#");
            paramMap.put("detailData", detailData);


            log.debug("调用批量退款接口......");
            alipayRefundService.doRefund(response, paramMap);
        }

    }

    @RequestMapping(value="site/coop/add_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value="新增技术合作(技术成果，技术需求)",notes = "新增技术合作(技术成果，技术需求)",response = Response.class)
    public Response insertTechCooperation(@ApiParam(value = "技术合作：技术成果，技术需求")  @ModelAttribute(value="techCoop")TechCooperation techCoop)
    {
        log.info("insert tech cooperation");
        int result = techService.insertTechCooperation(techCoop);
        Response response = new Response();
        return response;
    }


    @RequestMapping(value="site/coop/sel_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value="查看技术合作详情",notes = "查看技术合作详情",response = Response.class)
    public Response previewTechCooperation(@ApiParam(value = "技术合作成果、需求ID")  @RequestParam String techCoopId)
    {

        TechCooperation techCoop = techService.selectTechCooperationById(techCoopId);
        techService.updateTechCooperationViews(techCoopId);
        Response response = new Response();
        response.setData(techCoop);
        return response;
    }

    @RequestMapping(value="site/coop/sel_tech_cooperation", method = RequestMethod.GET)
    @ApiOperation(value="频道页搜索技术合作(技术成果，技术需求)",notes = "频道页搜索技术合作(技术成果，技术需求)",response = Response.class)
    public Response findAllTechCooperationPager(@ApiParam(value = "系统分类") @RequestParam(required = false) String systemCategory,
                                                @ApiParam(value = "应用领域") @RequestParam(required = false) String applicationArea,
                                                @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                                @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        condition.put("systemCategory", systemCategory);
        condition.put("applicationArea", applicationArea);
        condition.put("status", TechConstant.TechCooperationnStatus.AUDITPASS.toString());
        List<Map<String, String>> techList = techService.findAllTechCooperationPager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }


    @RequestMapping(value="oms/coop/sel_tech_cooperation", method = RequestMethod.GET)
    @ApiOperation(value="运营管理平台搜索技术合作(技术成果，技术需求)",notes = "运营管理平台技术合作(技术成果，技术需求)",response = Response.class)
    public Response findAllTechCooperationPager(@ApiParam(value = "系统分类") @RequestParam(required = false) String systemCategory,
                                                @ApiParam(value = "应用领域") @RequestParam(required = false) String applicationArea,
                                                @ApiParam(value = "标题") @RequestParam(required = false) String title,
                                                @ApiParam(value = "类型：1成果，2需求") @RequestParam(required = false) String type,
                                                @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                                @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                                @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("systemCategory", systemCategory);
        condition.put("applicationArea", applicationArea);
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        if (title != null && !"".equals(title)) {
            condition.put("title", title.replace("_", "\\_"));
        }
        condition.put("type", type);
        condition.put("status", status);
        List<Map<String, String>> techList = techService.findAllOMSTechCooperationPager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }


    @RequestMapping(value="oms/coop/upd_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value="修改技术合作(技术成果，技术需求)",notes = "修改技术合作(技术成果，技术需求)",response = Response.class)
    public Response updateTechCooperation( @ApiParam(value = "技术合作：技术成果，技术需求")  @ModelAttribute(value="techCoop")TechCooperation techCoop)
    {
        Response response = new Response();
        int result = techService.updateTechCooperation(techCoop);
        return response;
    }

    @RequestMapping(value="oms/coop/del_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value="删除技术合作(技术成果，技术需求)",notes = "删除技术合作(技术成果，技术需求)",response = Response.class)
    public Response deleteTechCooperation( @ApiParam(value = "技术合作ID")  @RequestParam() String techId)
    {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("id", techId);
        condition.put("status", TechConstant.TechCooperationnStatus.DELETE.toString());
        int result = techService.deleteTechCooperation(condition);
        return response;
    }

    @ApiOperation(value="上传技术资料(行业解决方案，技术文档，培训资料)",notes="上传技术资料(行业解决方案，技术文档，培训资料)",response = Response.class)
    @RequestMapping(value = "site/data/upload_tech_data", method = RequestMethod.POST)
    public Response uploadAskList(HttpServletRequest req) throws IOException {
        Response result = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session){
            String url = uploadService.upload(req,"tech");
            Map map = new HashMap();
            map.put(Constants.name,url);
            result.setData(map);
            result.setCode(200);
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @RequestMapping(value="site/data/add_Tech_data", method = RequestMethod.POST)
    @ApiOperation(value="新增技术资料(行业解决方案，技术文档，培训资料)",notes = "新增技术资料(行业解决方案，技术文档，培训资料)",response = Response.class)
    public Response insertTechData(@ApiParam(value = "技术资料:行业解决方案，技术文档，培训资料")  @ModelAttribute(value="techData")TechData techData)
    {
        int result = techService.insertTechData(techData);
        Response response = new Response();
        return response;
    }

    @RequestMapping(value="oms/data/del_tech_data", method = RequestMethod.POST)
    @ApiOperation(value="删除技术资料(行业解决方案，技术文档，培训资料)",notes = "删除技术资料(行业解决方案，技术文档，培训资料)",response = Response.class)
    public Response deleteTechData( @ApiParam(value = "技术资料ID")  @RequestParam() String techDataId)
    {
        Response response = new Response();
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("id",techDataId);
        condition.put("status", TechConstant.TechCooperationnStatus.DELETE.toString());
        int result = techService.deleteTechData(condition);
        return response;
    }

    @RequestMapping(value="oms/data/upd_tech_data", method = RequestMethod.POST)
    @ApiOperation(value="修改技术资料(行业解决方案，技术文档，培训资料)",notes = "修改技术资料(行业解决方案，技术文档，培训资料)",response = Response.class)
    public Response updateTechData( @ApiParam(value = "技术合作：技术成果，技术需求")  @ModelAttribute(value="techData") TechData techData)
    {
        Response response = new Response();
        int result = techService.updateTechData(techData);
        return response;
    }


}

package com.zhuhuibao.business.techtrain;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.alipay.service.direct.AlipayDirectService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.pojo.OrderReqBean;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.techtrain.entity.TechCooperation;
import com.zhuhuibao.mybatis.techtrain.service.TechnologyService;
import com.zhuhuibao.utils.ValidateUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    TechnologyService techService;

    @ApiOperation(value = "立即支付", notes = "立即支付")
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public void doPay(HttpServletRequest request, HttpServletResponse response,
                      @ApiParam  @ModelAttribute(value="order") OrderReqBean order) throws Exception {

        Gson gson = new Gson();
        String json   = gson.toJson(order);

        if("true".equals(order.getNeedInvoice())){
            String invoiceTitle = order.getInvoiceTitle();
            if(invoiceTitle == null){
                log.error("已选需要发票,发票抬头不能为空");
                throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR,"已选需要发票,发票抬头不能为空");
            }
            String invoiceType = order.getInvoiceType();
            if(invoiceType == null){
                log.error("已选需要发票,发票类型不能为空");
                throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR,"已选需要发票,发票类型不能为空");
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

    @RequestMapping(value="coop/add_TechCooperation", method = RequestMethod.POST)
    @ApiOperation(value="新增技术合作(技术成果，技术需求)",notes = "新增技术合作(技术成果，技术需求)",response = Response.class)
    public Response insertTechCooperation(@ApiParam(value = "技术合作：技术成果，技术需求")  @ModelAttribute(value="techCoop")TechCooperation techCoop)
    {
        log.info("insert tech cooperation");
        int result = techService.insert(techCoop);
        Response response = new Response();
        return response;
    }

    @RequestMapping(value="coop/sel_tech_cooperation", method = RequestMethod.GET)
    @ApiOperation(value="频道页搜索技术合作(技术成果，技术需求)",notes = "频道页搜索技术合作(技术成果，技术需求)",response = Response.class)
    public Response findAllTechCooperationPager(@ApiParam(value = "系统分类") @RequestParam(required = false) String systemCategory,
                                                @ApiParam(value = "应用领域") @RequestParam(required = false) String applicationArea,
                                                @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                                @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize)
    {
        Response response = new Response();
        Map<String,Object> condition = new HashMap<String,Object>();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        condition.put("systemCategory",systemCategory);
        condition.put("applicationArea",applicationArea);
        condition.put("status", TechConstant.TechCooperationnStatus.AUDITPASS.toString());
        List<Map<String,String>> techList = techService.findAllTechCooperationPager(pager,condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value="coop/sel_oms_tech_cooperation", method = RequestMethod.GET)
    @ApiOperation(value="运营管理平台搜索技术合作(技术成果，技术需求)",notes = "运营管理平台技术合作(技术成果，技术需求)",response = Response.class)
    public Response findAllOMSTechCooperationPager(@ApiParam(value = "系统分类") @RequestParam(required = false) String systemCategory,
                                                   @ApiParam(value = "应用领域") @RequestParam(required = false) String applicationArea,
                                                   @ApiParam(value = "标题") @RequestParam(required = false) String title,
                                                   @ApiParam(value = "类型：1成果，2需求") @RequestParam(required = false) String type,
                                                   @ApiParam(value = "状态") @RequestParam(required = false)   String status,
                                                   @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                                   @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize)
    {
        Response response = new Response();
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("systemCategory",systemCategory);
        condition.put("applicationArea",applicationArea);
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        if(title != null && !"".equals(title))
        {
            condition.put("title",title.replace("_","\\_"));
        }
        condition.put("type",type);
        condition.put("status",status);
        List<Map<String,String>> techList = techService.findAllTechCooperationPager(pager,condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value="coop/upd_oms_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value="修改技术合作(技术成果，技术需求)",notes = "修改技术合作(技术成果，技术需求)",response = Response.class)
    public Response updateOMSTechCooperation( @ApiParam(value = "技术合作：技术成果，技术需求")  @ModelAttribute(value="techCoop")TechCooperation techCoop)
    {
        Response response = new Response();
        int result = techService.updateTechCooperation(techCoop);
        return response;
    }

    @RequestMapping(value="coop/del_oms_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value="删除技术合作(技术成果，技术需求)",notes = "删除技术合作(技术成果，技术需求)",response = Response.class)
    public Response deleteOMSTechCooperation( @ApiParam(value = "技术合作ID")  @RequestParam() String techId)
    {
        Response response = new Response();
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("id",techId);
        condition.put("status", TechConstant.TechCooperationnStatus.DELETE.toString());
        int result = techService.deleteTechCooperation(condition);
        return response;
    }
}

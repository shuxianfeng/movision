package com.zhuhuibao.business.job.site;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.EmployeeSize;
import com.zhuhuibao.mybatis.memCenter.entity.EnterpriseType;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.JobRelResumeService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.mybatis.payment.service.PaymentGoodsService;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by Administrator on 2016/4/21 0021.
 */
@RestController
@RequestMapping("/rest/job/site/base/")
public class JobCommonController {
    private final static Logger log = LoggerFactory.getLogger(JobCommonController.class);

    @Autowired
    JobPositionService job;

    @Autowired
    ResumeService resume;

    @Autowired
    SiteMailService smService;

    @Autowired
    JobRelResumeService jrrService;

    @Autowired
    ChannelNewsService newsService;

    @Autowired
    PaymentGoodsService goodsService;

    @Autowired
    private MemberService memberService;

    @RequestMapping(value="sel_unRead_msg_count",method = RequestMethod.GET)
    @ApiOperation(value="未读消息数目",notes = "人才网未读消息数目",response = Response.class)
    public Response queryUnreadMsgCount()
    {
        Response response = new Response();
        Long receiveID = ShiroUtil.getCreateID();
        if(receiveID != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("recID", receiveID);
            map.put("type", JobConstant.SITEMAIL_TYPE_JOB_ELEVEN);
            map.put("status", Constants.MAILSITE_STATUS_UNREAD);
            response.setData(smService.queryUnreadMsgCount(map));
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "企业性质", notes = "企业性质", response = Response.class)
    @RequestMapping(value = "sel_enterpriseTypeList", method = RequestMethod.GET)
    public Response enterpriseTypeList()  {
        Response result = new Response();
        List<EnterpriseType> enterpriseType = memberService.findEnterpriseTypeList();
        result.setData(enterpriseType);
        return result;
    }

    @ApiOperation(value = "人员规模", notes = "人员规模", response = Response.class)
    @RequestMapping(value = "sel_employeeSizeList", method = RequestMethod.GET)
    public Response employeeSizeList()  {
        Response result = new Response();
        List<EmployeeSize> employeeSizeList = memberService.findEmployeeSizeList();
        result.setData(employeeSizeList);
        return result;
    }
}

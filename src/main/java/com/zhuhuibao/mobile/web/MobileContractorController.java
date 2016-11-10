package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.AdvertisingConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.fsearch.pojo.spec.ContractorSearchSpec;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.service.MobileChannelNewsService;
import com.zhuhuibao.service.MobileMemberService;
import com.zhuhuibao.service.MobileSysAdvertisingService;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工程商相关业务控制层
 *
 * @author liyang
 * @date 2016年10月12日
 */
@RestController
@RequestMapping("/rest/m/contractor/site/")
@Api(value = "MobileContractor", description = "触屏端工程商频道")
public class MobileContractorController {

    private static final Logger log = LoggerFactory.getLogger(MobileContractorController.class);

    @Autowired
    private MobileSysAdvertisingService advertisingService;

    @Autowired
    private MobileMemberService memberService;

    @Autowired
    private MobileChannelNewsService channelNewsService;

    /**
     * 触屏端工程商首页
     * 
     * @return response 响应
     */
    @ApiOperation(value = "触屏端工程商首页", notes = "触屏端工程商首页")
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public Response index() {
        Response response = new Response();
        List<SysAdvertising> bannerAdvList = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Contractor_Banner.value);
        // 加入我们广告图片
        List<SysAdvertising> joinUsAdvList = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Contractor_Join_Us.value);
        // 名企展示广告图片
        List<SysAdvertising> greatCompanyAdvList = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Contractor_Enterprise_Display.value);
        // 风云人物展示广告图片
        List<SysAdvertising> greatPersonAdvList = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Contractor_Influential_Man.value);

        Map<String, List> advList = new HashMap<>();
        advList.put("banner", bannerAdvList);
        advList.put("joinUs", joinUsAdvList);
        advList.put("greatCompany", greatCompanyAdvList);
        advList.put("greatPerson", greatPersonAdvList);
        response.setData(advList);
        return response;
    }

    /**
     * 触屏端-名企（厂商，代理商，渠道商)展示更多页面
     * 
     * @param pageNo
     *            页码
     * @param pageSize
     *            每页显示条数
     * @return response 响应
     */
    @ApiOperation(value = "触屏端-名企(厂商，代理商，渠道商)展示更多页面", notes = "触屏端-名企(厂商，代理商，渠道商)展示更多页面")
    @RequestMapping(value = "sel_great_company_list", method = RequestMethod.GET)
    public Response selGreatCompanyList(@ApiParam(value = "类别") @RequestParam(required = true) String identify, @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") int pageNo,
            @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Response response = new Response();
        Paging<Member> pager = new Paging<>(pageNo, pageSize);
        try {
            response.setData(memberService.getGreatCompany(pager, identify));
        } catch (Exception e) {
            log.error("sel_great_company_list error! ", e);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 触屏端-名企(厂商，代理商，渠道商)信息详情
     * 
     * @param id
     *            工程商id
     * @return response 响应
     */
    @ApiOperation(value = "触屏端-名企(厂商，代理商，渠道商)信息详情", notes = "触屏端-名企(厂商，代理商，渠道商)信息详情")
    @RequestMapping(value = "sel_great_company_info", method = RequestMethod.GET)
    public Response selGreatCompanyInfo(@ApiParam(value = "名企主键id") @RequestParam(required = true) String id) {
        Response response = new Response();
        try {
            response.setData(memberService.getGreatCompanyInfo(id));
        } catch (Exception e) {
            log.error("sel_great_company_info error! ", e);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 触屏端工程商-风云人物展示更多
     * 
     * @param type
     *            信息类型
     * @param isHot
     *            是否热门
     * @param pageNo
     *            页码
     * @param pageSize
     *            每页条数
     * @return
     */
    @ApiOperation(value = "触屏端工程商-风云人物展示更多", notes = "触屏端工程商-风云人物展示更多")
    @RequestMapping(value = "sel_great_person_list", method = RequestMethod.GET)
    public Response selGreatPersonList(@ApiParam(value = "类型：1 行业资讯，2 人物专访 3：行业风云人物 4：工程商风采 5：工程资料") @RequestParam String type,
            @ApiParam(value = "是否是热门资料") @RequestParam(required = false, defaultValue = "false") boolean isHot, @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") int pageNo,
            @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Response response = new Response();
        Paging<Map> pager = new Paging<>(pageNo, pageSize);
        response.setData(channelNewsService.findAllChanNewsList(pager, isHot, type));
        return response;
    }

    /**
     * 触屏端工程商-搜索风云人物
     *
     * @param type
     *            信息类型
     * @param isHot
     *            是否热门
     * @param pageNo
     *            页码
     * @param pageSize
     *            每页条数
     * @param title
     *            信息标题
     * @return
     */
    @ApiOperation(value = "触屏端工程商-搜索风云人物", notes = "触屏端工程商-搜索风云人物")
    @RequestMapping(value = "sel_great_person_list_by_title", method = RequestMethod.GET)
    public Response selGreatPersonListByTitle(@ApiParam(value = "信息标题") @RequestParam(required = false) String title,
            @ApiParam(value = "类型：1 行业资讯，2 人物专访 3：行业风云人物 4：工程商风采 5：工程资料") @RequestParam String type, @ApiParam(value = "是否是热门资料") @RequestParam(required = false, defaultValue = "false") boolean isHot,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") int pageNo, @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Response response = new Response();
        Paging<Map> pager = new Paging<>(pageNo, pageSize);
        response.setData(channelNewsService.findChanNewsListByTitle(pager, isHot, type, title));
        return response;
    }

    /**
     * 触屏端工程商-风云人物详情
     * 
     * @param id
     *            栏目信息的ID
     */
    @ApiOperation(value = "触屏端工程商-风云人物详情", notes = "触屏端工程商-风云人物详情")
    @RequestMapping(value = "sel_great_person_info", method = RequestMethod.GET)
    public Response selGreatPersonInfo(@ApiParam(value = "咨询id") @RequestParam Long id) {
        Response response = new Response();
        response.setData(channelNewsService.queryDetailsById(id));
        return response;
    }

    /**
     * 触屏端搜索工程商信息
     * 
     * @param spec
     *            查询条件
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "触屏端搜索工程商", notes = "触屏端搜索工程商", response = Response.class)
    @RequestMapping(value = { "sel_contractor_list" }, method = RequestMethod.GET)
    public Response selContractorList(ContractorSearchSpec spec) {
        Response response = new Response();
        try {
            response.setData(memberService.searchContractors(spec));
        } catch (Exception e) {
            response.setMsgCode(0);
            log.error("sel_contractor_list error! ", e);
            response.setMessage("sel_contractor_list  error!" + e);
        }
        return response;
    }

    /**
     * 触屏端留言
     * 
     * @return
     */
    @ApiOperation(value = "触屏端留言", notes = "触屏端留言", response = Response.class)
    @RequestMapping(value = "/add_message", method = RequestMethod.POST)
    public Response message(@ModelAttribute Message message) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID() == null ? 0l : ShiroUtil.getCreateID();
        message.setCreateid(String.valueOf(createId));
        String title = "";
        if (ShiroUtil.getCreateID() == null) {
            title = "来自匿名用户的留言";
        } else {
            if (StringUtils.isNotBlank(ShiroUtil.getMember().getNickname())) {
                title = "来自" + ShiroUtil.getMember().getNickname() + "的留言";
            } else {
                if (StringUtils.isNotBlank(ShiroUtil.getMember().getCompanyName())) {
                    title = "来自" + ShiroUtil.getMember().getCompanyName() + "的留言";
                } else {
                    title = "来自匿名用户的留言";
                }
            }
        }
        message.setTitle(title);
        memberService.saveMessage(message);
        return response;
    }
}

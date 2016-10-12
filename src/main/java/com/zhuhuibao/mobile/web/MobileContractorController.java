package com.zhuhuibao.mobile.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.AdvertisingConstant;
import com.zhuhuibao.fsearch.pojo.spec.ContractorSearchSpec;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.service.AdvertisingService;
import com.zhuhuibao.service.MobileChannelNewsService;
import com.zhuhuibao.service.MobileMemberService;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 工程商相关业务控制层
 *
 * @author liyang
 * @date 2016年10月12日
 */
@RestController
@RequestMapping("/rest/m/contractor/sit/")
@Api(value = "Contractor", description = "触屏端工程商频道")
public class MobileContractorController {

    private static final Logger log = LoggerFactory.getLogger(MobileContractorController.class);

    @Autowired
    private AdvertisingService advertisingService;

    @Autowired
    private MobileMemberService memberService;

    @Autowired
    private MobileChannelNewsService channelNewsService;

    /**
     * 触屏端工程商广告图片位置
     */
    private final String mAdvArea = "M_Engineering";

    /**
     * 触屏端工程商首页
     * 
     * @return response 响应
     */
    @ApiOperation(value = "触屏端工程商首页", notes = "触屏端工程商首页")
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public Response index() {
        Response response = new Response();
        // banner位广告图片 todo chanType 待定 广告位置
        List<SysAdvertising> bannerAdvList = advertisingService.queryAdveristing(AdvertisingConstant.AdvertisingChanType.mobile.value, mAdvArea, "M_Engineering_banner");
        // 加入我们广告图片
        List<SysAdvertising> joinUsAdvList = advertisingService.queryAdveristing(AdvertisingConstant.AdvertisingChanType.mobile.value, mAdvArea, "M_Engineering_joinus");
        // 名企展示广告图片
        List<SysAdvertising> greatCompanyAdvList = advertisingService.queryAdveristing(AdvertisingConstant.AdvertisingChanType.mobile.value, mAdvArea, "M_Engineering_Enterprisedisplay");
        // 风云人物展示广告图片
        List<SysAdvertising> greatPersonAdvList = advertisingService.queryAdveristing(AdvertisingConstant.AdvertisingChanType.mobile.value, mAdvArea, "M_Engineering_Influentialman");

        Map<String, List> advList = new HashMap<>();
        advList.put("banner", bannerAdvList);
        advList.put("joinUs", joinUsAdvList);
        advList.put("greatCompany", greatCompanyAdvList);
        advList.put("greatPerson", greatPersonAdvList);
        response.setData(advList);
        return response;
    }

    /**
     * 触屏端工程商-名企展示更多页面
     * 
     * @param pageNo
     *            页码
     * @param pageSize
     *            每页显示条数
     * @return response 响应
     */
    @ApiOperation(value = "触屏端工程商-名企展示更多", notes = "触屏端工程商-名企展示更多")
    @RequestMapping(value = "sel_great_company_list", method = RequestMethod.GET)
    public Response sel_great_company_list(@ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") int pageNo,
            @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Response response = new Response();
        Paging<Member> pager = new Paging<>(pageNo, pageSize);
        try {
            response.setData(memberService.getGreatCompany(pager));
        } catch (Exception e) {
            log.error("sel_great_company_list error! ", e);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 触屏端工程商-名企信息详情
     * 
     * @param id
     *            工程商id
     * @return response 响应
     */
    @ApiOperation(value = "触屏端工程商-名企信息详情", notes = "触屏端工程商-名企信息详情")
    @RequestMapping(value = "sel_great_company_info", method = RequestMethod.GET)
    public Response sel_great_company_info(@ApiParam(value = "名企主键id") @RequestParam(required = true) String id) {
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
    public Response sel_great_person_list(@ApiParam(value = "类型：1 行业资讯，2 人物专访 3：行业风云人物 4：工程商风采 5：工程资料") @RequestParam String type,
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
    public Response sel_great_person_list_by_title(@ApiParam(value = "信息标题") @RequestParam(required = false) String title,
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
    public Response sel_great_person_info(@ApiParam(value = "咨询id") @RequestParam Long id) {
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
    public Response sel_contractor_list(ContractorSearchSpec spec) {
        Response response = new Response();
        try {
            response.setData(memberService.searchContractors(spec));
        } catch (Exception e) {
            response.setMsgCode(0);
            response.setMessage("sel_contractor_list  error!" + e);
        }
        return response;
    }
}

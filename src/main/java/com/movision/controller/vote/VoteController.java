package com.movision.controller.vote;

import com.movision.common.Response;
import com.movision.facade.voteH5.VoteFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.activeH5.entity.ActiveH5;
import com.movision.mybatis.activeH5.entity.ActiveH5Vo;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.take.entity.Take;
import com.movision.mybatis.take.entity.TakeVo;
import com.movision.utils.file.FileUtil;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/8/22 14:43
 */
@RestController
@RequestMapping("/vote/wechat/")
public class VoteController {

    @Autowired
    private VoteFacade voteFacade;

    /**
     * 添加活动
     *
     * @param name
     * @param photo
     * @param begintime
     * @param endtime
     * @param activitydescription
     * @return
     */
    @ApiOperation(value = "添加活动(后台)", notes = "添加活动", response = Response.class)
    @RequestMapping(value = "insertSelective", method = RequestMethod.POST)
    public Response insertSelective(HttpServletRequest request,
                                    @ApiParam(value = "活动名称") @RequestParam String name,
                                    @ApiParam(value = "活动图片") @RequestParam String photo,
                                    @ApiParam(value = "活动开始时间") @RequestParam String begintime,
                                    @ApiParam(value = "活动结束时间") @RequestParam String endtime,
                                    @ApiParam(value = "活动说明") @RequestParam String activitydescription) {
        Response response = new Response();
        int result = voteFacade.insertSelective(request, name, photo, begintime, endtime, activitydescription);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 根据id查询活动详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "queryActivityById", method = RequestMethod.POST)
    @ApiOperation(value = "查询活动详情（后台）", notes = "查询活动详情", response = Response.class)
    public Response queryActivityById(@ApiParam(value = "活动详情") @RequestParam int id) {
        Response response = new Response();
        ActiveH5 activeH5 = voteFacade.queryActivityById(id);
        response.setMessage("查询成功");
        response.setData(activeH5);
        return response;
    }


    /**
     * 删除活动
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除活动（后台）", notes = "删除活动", response = Response.class)
    @RequestMapping(value = "deleteActive", method = RequestMethod.POST)
    public Response deleteActive(@ApiParam(value = "活动id") @RequestParam int id) {
        Response response = new Response();
        int result = voteFacade.deleteActive(id);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 编辑活动
     *
     * @param id
     * @param name
     * @param photo
     * @param activitydescription
     * @param begintime
     * @param endtime
     * @return
     */
    @RequestMapping(value = "updateActivity", method = RequestMethod.POST)
    @ApiOperation(value = "编辑活动（后台）", notes = "编辑活动接口", response = Response.class)
    public Response updateActivity(HttpServletRequest request,
                                   @ApiParam(value = "活动id") @RequestParam int id,
                                   @ApiParam(value = "活动名称") @RequestParam String name,
                                   @ApiParam(value = "活动封面") @RequestParam String photo,
                                   @ApiParam(value = "活动说明") @RequestParam String activitydescription,
                                   @ApiParam(value = "开始时间") @RequestParam String begintime,
                                   @ApiParam(value = "结束时间") @RequestParam String endtime) {
        Response response = new Response();
        voteFacade.updateActivity(request, id, name, photo, activitydescription, begintime, endtime);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }


    /**
     * 查询全部活动
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询全部活动（后台）", notes = "查询全部活动", response = Response.class)
    @RequestMapping(value = "findAllActive", method = RequestMethod.POST)
    public Response findAllActive(@ApiParam(value = "活动名称") @RequestParam(required = false) String name,
                                  @ApiParam(value = "活动开始时间") @RequestParam(required = false) String bigintime,
                                  @ApiParam(value = "活动结束时间") @RequestParam(required = false) String endtime,
                                  @ApiParam(value = "第几页") @RequestParam(required = false) String pageNo,
                                  @ApiParam(value = "每页多少条") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        List resault = new ArrayList();
        if (StringUtil.isNotEmpty(pageNo) && StringUtil.isNotEmpty(pageSize)) {
            Paging<ActiveH5Vo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
            resault = voteFacade.findAllActive(name, bigintime, endtime, pager);
            pager.result(resault);
            response.setData(pager);
        } else {
            resault = voteFacade.queryAllActive(name);
            response.setData(resault);
        }
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        return response;
    }


    /**
     * 添加投稿
     *
     * @param activeid
     * @param name
     * @return
     */
    @ApiOperation(value = "添加投稿", notes = "添加投稿", response = Response.class)
    @RequestMapping(value = "insertSelectiveTP", method = RequestMethod.POST)
    public Response insertSelectiveTP(HttpServletRequest request,
                                      @ApiParam(value = "活动id") @RequestParam(required = false) String activeid,
                                      @ApiParam(value = "作品名称") @RequestParam(required = false) String name,
                                      @ApiParam(value = "投稿人电话") @RequestParam(required = false) String phone,
                                      @ApiParam(value = "投稿内容") @RequestParam(required = false) String photo,
                                      @ApiParam(value = "投稿描述") @RequestParam(required = false) String describe,
                                      @ApiParam(value = "投稿人") @RequestParam(required = false) String nickname,
                                      @ApiParam(value = "banner图") @RequestParam(required = false) String banner) {
        Response response = new Response();
        int result = voteFacade.insertSelectiveTP(request, activeid, name, phone, photo, describe, nickname, banner);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 编辑投稿信息
     * @param id
     * @param activeid
     * @param name
     * @param phone
     * @param photo
     * @param describe
     * @param nickname
     * @param banner
     * @param audit
     * @param mark
     * @return
     */
    @ApiOperation(value = "编辑投稿信息", notes = "编辑投稿", response = Response.class)
    @RequestMapping(value = "updateTakeById", method = RequestMethod.POST)
    public Response updateTakeById(HttpServletRequest request,
                                   @ApiParam(value = "作品id") @RequestParam String id,
                                   @ApiParam(value = "活动id") @RequestParam(required = false) String activeid,
                                   @ApiParam(value = "作品名称") @RequestParam(required = false) String name,
                                   @ApiParam(value = "投稿人电话") @RequestParam(required = false) String phone,
                                   @ApiParam(value = "投稿内容") @RequestParam(required = false) String photo,
                                   @ApiParam(value = "投稿描述") @RequestParam(required = false) String describe,
                                   @ApiParam(value = "投稿人") @RequestParam(required = false) String nickname,
                                   @ApiParam(value = "banner图") @RequestParam(required = false) String banner,
                                   @ApiParam(value = "审核") @RequestParam(required = false) String audit,
                                   @ApiParam(value = "号码") @RequestParam(required = false) String mark) {
        Response response = new Response();
        voteFacade.updateTakeById(request, id, activeid, name, phone, photo, describe, nickname, banner, audit, mark);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }


    /**
     * 投稿审核
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "投稿审核（后台）", notes = "用于投稿审核接口", response = Response.class)
    @RequestMapping(value = "updateTakeByAudit", method = RequestMethod.POST)
    public Response updateTakeByAudit(@ApiParam(value = "投稿id") @RequestParam String id,
                                      @ApiParam(value = "审核状态") @RequestParam String type) {
        Response response = new Response();
        voteFacade.updateTakeByAudit(id, type);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    /**
     * 查询投稿详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "queryTakeById", method = RequestMethod.POST)
    @ApiOperation(value = "根据id查询投稿详情", notes = "查询投稿详情", response = Response.class)
    public Response queryTakeById(@ApiParam(value = "投稿id") @RequestParam int id) {
        Response response = new Response();
        TakeVo take = voteFacade.queryTakeById(id);
        response.setMessage("查询成功");
        response.setData(take);
        return response;
    }


    /**
     * 删除投稿
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除投稿（后台）", notes = "删除投稿", response = Response.class)
    @RequestMapping(value = "deleteTakePeople", method = RequestMethod.POST)
    public Response deleteTakePeople(@ApiParam(value = "参赛人员id") @RequestParam int id) {
        Response response = new Response();
        int result = voteFacade.deleteTakePeople(id);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 条件查询投稿列表
     *
     * @return
     */
    @ApiOperation(value = "条件查询投稿列表", notes = "条件查询投稿列表", response = Response.class)
    @RequestMapping(value = "findAllTake", method = RequestMethod.POST)
    public Response findAllTake(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                                @ApiParam(value = "编号") @RequestParam(required = false) String mark,
                                @ApiParam(value = "姓名") @RequestParam(required = false) String nickname) {
        Response response = new Response();
        Paging<TakeVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List result = voteFacade.findAllTakeCondition(pager, mark, nickname);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        pager.result(result);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "投票排行", notes = "投票排行", response = Response.class)
    @RequestMapping(value = "voteDesc", method = RequestMethod.POST)
    public Response voteDesc(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                             @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                             @ApiParam(value = "活动id") @RequestParam int activeid) {
        Response response = new Response();
        Paging<TakeVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List result = voteFacade.voteDesc(pager, activeid);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        pager.result(result);
        response.setData(pager);
        return response;
    }

    /**
     * 条件查询投稿列表
     *
     * @return
     */
    @ApiOperation(value = "条件查询投稿列表(后台)", notes = "条件查询投稿列表", response = Response.class)
    @RequestMapping(value = "findTakeList", method = RequestMethod.POST)
    public Response findTakeList(@ApiParam(value = "投稿name") @RequestParam(required = false) String name,
                                 @ApiParam(value = "活动id") @RequestParam(required = false) String activityid,
                                 @ApiParam(value = "审核状态 0未审核 1审核通过") @RequestParam(required = false) String audit,
                                 @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                 @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<TakeVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List result = voteFacade.findAllTake(pager, name, audit, activityid);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        pager.result(result);
        response.setData(pager);
        return response;
            }

    @ApiOperation(value = "所有参赛", notes = "所有参赛", response = Response.class)
    @RequestMapping(value = "findAll", method = RequestMethod.POST)
    public Response findAll() {
        Response response = new Response();
        List<TakeVo> result = voteFacade.findAll();
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 修改访问量
     *
     * @param activeid
     * @return
     */
    @ApiOperation(value = "修改访问量", notes = "修改访问量", response = Response.class)
    @RequestMapping(value = "updatePageView", method = RequestMethod.POST)
    public Response updatePageView(@ApiParam(value = "活动id") @RequestParam int activeid) {
        Response response = new Response();
        int result = voteFacade.updatePageView(activeid);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 首页数据
     *
     * @param activeid
     * @return
     */
    @ApiOperation(value = "首页数据", notes = "首页数据", response = Response.class)
    @RequestMapping(value = "querySum", method = RequestMethod.POST)
    public Response querySum(@ApiParam(value = "活动id") @RequestParam int activeid) {
        Response response = new Response();
        ActiveH5Vo result = voteFacade.querySum(activeid);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 查询活动说明
     *
     * @param activeid
     * @return
     */
    @ApiOperation(value = "查询活动说明", notes = "查询活动说明", response = Response.class)
    @RequestMapping(value = "queryH5Describe", method = RequestMethod.POST)
    public Response queryH5Describe(@ApiParam(value = "活动id") @RequestParam int activeid) {
        Response response = new Response();
        ActiveH5 result = voteFacade.queryH5Describe(activeid);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 是否在时间内
     *
     * @param activeid
     * @return
     */
    @ApiOperation(value = "是否在时间内", notes = "是否在时间内", response = Response.class)
    @RequestMapping(value = "isTake", method = RequestMethod.POST)
    public Response isTake(@ApiParam(value = "活动id") @RequestParam int activeid) {
        Response response = new Response();
        int result = voteFacade.isTake(activeid);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 添加投票记录
     *
     * @param activeid
     * @param name
     * @return
     */
    @ApiOperation(value = "添加投票记录", notes = "添加投票记录", response = Response.class)
    @RequestMapping(value = "insertSelectiveV", method = RequestMethod.POST)
    public Response insertSelectiveV(@ApiParam(value = "活动id") @RequestParam(required = false) String activeid,
                                     @ApiParam(value = "姓名") @RequestParam(required = false) String name,
                                     @ApiParam(value = "参赛id") @RequestParam(required = false) String takeid,
                                     @ApiParam(value = "投票号码") @RequestParam(required = false) String takenumber) {
        Response response = new Response();
        int result = voteFacade.insertSelectiveV(activeid, name, takeid, takenumber);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 上传投票系统相关图片
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传投票系统相关图片", notes = "上传投票相关图片", response = Response.class)
    @RequestMapping(value = {"/upload_vote_img"}, method = RequestMethod.POST)
    public Response updatePostImg(@RequestParam(value = "file", required = false) MultipartFile[] file) {
        Response response = new Response();
        List list = voteFacade.updatePostImgTest(file);
        response.setMessage("操作成功");
        response.setData(list);
        return response;
    }
}

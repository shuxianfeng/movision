package com.movision.facade.boss;

import com.movision.mybatis.submission.entity.Submission;
import com.movision.mybatis.submission.entity.SubmissionVo;
import com.movision.mybatis.submission.service.SubmissionService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserAll;
import com.movision.mybatis.user.entity.UserVo;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/24 14:49
 */
@Service
public class UserManageFacade {
    @Autowired
    private UserService userService;

    @Autowired
    private SubmissionService submissionService;

    /**
     * 查看vip申请列表
     *
     * @param pager
     * @return
     */
    public List<UserVo> queryApplyVipList(Paging<UserVo> pager) {
        List<UserVo> users = userService.findAllqueryUsers(pager);//查询所有申请加v用户
        return users;
    }

    /**
     * 查询所有VIP用户
     *
     * @param pager
     * @return
     */
    public List<UserVo> queryVipList(Paging<UserVo> pager) {
        List<UserVo> users = userService.findAllqueryUserVIPByList(pager);//查询所有VIP用户
        return users;
    }

    /**
     * 查询投稿列表
     *
     * @param pager
     * @return
     */
    public List<SubmissionVo> queryContributeList(Paging<SubmissionVo> pager) {
        return submissionService.queryContributeList(pager);
    }

    /**
     * 查询投稿说明
     * @param id
     * @return
     */
    public Submission queryContributeBounce(String id) {
        return submissionService.queryContributeBounce(id);
    }

    /**
     * 对VIP列表排序
     *
     * @param time
     * @param grade
     * @param pager
     * @return
     */
    public List<UserVo> queryAddVSortUser(String time, String grade, Paging<UserVo> pager) {
        Map map = new HashedMap();
        map.put("time", time);
        map.put("grade", grade);
        return userService.queryAddVSortUser(map, pager);
    }

    /**
     * 条件查询VIP申请用户列表
     *
     * @param username
     * @param phone
     * @param begintime
     * @param endtime
     * @param pager
     * @return
     */
    public List<UserVo> queryUniteConditionByApply(String username, String phone, String begintime, String endtime, Paging<UserVo> pager) {
        Map map = new HashedMap();
        String beg = null;
        String end = null;
        if (begintime != null && endtime != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long l = new Long(begintime);
            Long o = new Long(endtime);
            beg = format.format(l);
            end = format.format(o);
        }
        map.put("username", username);
        map.put("phone", phone);
        map.put("begintime", beg);
        map.put("endtime", end);
        return userService.queryUniteConditionByApply(map,pager);
    }

    /**
     * 条件查询投稿列表
     *
     * @param nickname
     * @param email
     * @param type
     * @param vip
     * @param begintime
     * @param endtime
     * @param pager
     * @return
     */
    public List<SubmissionVo> queryUniteConditionByContribute(String nickname, String email, String type, String vip, String begintime, String endtime, Paging<SubmissionVo> pager) {
        String beg = null;
        String end = null;
        //对时间做转换 毫秒转 日期类型
        if (begintime != null && endtime != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long l = new Long(begintime);
            Long o = new Long(endtime);
            beg = format.format(l);
            end = format.format(o);
        }
        Map map = new HashedMap();
        map.put("nickname", nickname);
        map.put("email", email);
        map.put("type", type);
        map.put("vip", vip);
        map.put("begintime", beg);
        map.put("endtime", end);
        return submissionService.queryUniteConditionByContribute(map, pager);
    }

    /**
     * 查询所有用户列表
     *
     * @param pager
     * @return
     */
    public List<UserAll> queryAllUserList(Paging<UserAll> pager, String pointsSort, String postsumSort, String isessenceSort, String fansSort) {
        Map map = new HashedMap();
        map.put("pointsSort", pointsSort);//积分排序
        map.put("postsumSort", postsumSort);//帖子排序
        map.put("isessenceSort", isessenceSort);//精贴排序
        map.put("fansSort", fansSort);//关注排序
        return userService.queryAllUserList(pager, map);
    }
}

package com.movision.facade.boss;

import com.movision.mybatis.province.entity.ProvinceVo;
import com.movision.mybatis.record.entity.RecordVo;
import com.movision.mybatis.record.service.RecordService;
import com.movision.mybatis.submission.entity.Submission;
import com.movision.mybatis.submission.entity.SubmissionVo;
import com.movision.mybatis.submission.service.SubmissionService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserAll;
import com.movision.mybatis.user.entity.UserParticulars;
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

    @Autowired
    private RecordService recordService;

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
     * 逻辑删除投稿
     *
     * @param id
     * @return
     */
    public int deleteContributeById(String id) {
        return submissionService.deleteContributeById(id);
    }

    /**
     * 查询所有用户列表
     *
     * @param pager
     * @return
     */
    public List<UserAll> queryAllUserList(Paging<UserAll> pager, String nickname, String phone, String authentication, String vip, String seal,
                                          String begintime, String endtime, String pointsSort, String postsumSort, String isessenceSort,
                                          String fansSort, String conditionon, String conditiontwo, String price, String login) {
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
        map.put("nickname", nickname);//用户名
        map.put("phone", phone);//手机号
        map.put("authentication", authentication);//实名认证
        map.put("level", vip);//是否是VIP
        map.put("status", seal);//是否封号
        map.put("begintime", beg);//注册开始时间
        map.put("endtime", end);//注册结束时间
        map.put("pointsSort", pointsSort);//积分排序
        map.put("postsumSort", postsumSort);//帖子排序
        map.put("isessenceSort", isessenceSort);//精贴排序
        map.put("fansSort", fansSort);//关注排序
        map.put("conditionon", conditionon);//条件1
        map.put("conditiontwo", conditiontwo);//条件2
        map.put("price", price);//比较值
        map.put("login", login);//登录状态
        List<UserAll> list = userService.queryAllUserList(pager, map);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getQq() != null) {
                list.get(i).setLogin("1");//QQ
            }
            if (list.get(i).getOpenid() != null) {
                list.get(i).setLogin("2");//微信
            }
            if (list.get(i).getSina() != null) {
                list.get(i).setLogin("3");//微博
            }
            if (list.get(i).getQq() != null && list.get(i).getOpenid() != null) {
                list.get(i).setLogin("4");//QQ,微信
            }
            if (list.get(i).getQq() != null && list.get(i).getSina() != null) {
                list.get(i).setLogin("5");//QQ，微博
            }
            if (list.get(i).getOpenid() != null && list.get(i).getSina() != null) {
                list.get(i).setLogin("6");//微信，微博
            }
            if (list.get(i).getQq() != null && list.get(i).getOpenid() != null && list.get(i).getSina() != null) {
                list.get(i).setLogin("7");//QQ,微信,微博
            }
        }
        return list;
    }

    /**
     * 对用户账户进行封号
     *
     * @param userid
     * @param type
     * @return
     */
    public int deleteUserByid(String userid, String type) {
        Map map = new HashedMap();
        map.put("userid", userid);
        map.put("type", type);
        return userService.deleteUserByid(map);
    }

    /**
     * 对用户进行加V去V操作
     *
     * @param userid
     * @param type
     * @return
     */
    public int deleteUserLevl(String userid, String type) {
        Map map = new HashedMap();
        map.put("userid", userid);
        map.put("type", type);
        return userService.deleteUserLevl(map);
    }

    /**
     * 查询用户积分流水列表
     *
     * @param pager
     * @return
     */
    public List<RecordVo> queryIntegralList(String userid, Paging<RecordVo> pager) {
        return recordService.queryIntegralList(userid, pager);
    }

    /**
     * 查询用户详情
     *
     * @param userid
     * @return
     */
    public UserParticulars queryUserParticulars(String userid) {
        UserParticulars userParticulars = userService.queryUserParticulars(userid);
        List<ProvinceVo> provinceVos = userService.queryProvinces(userid);
        userParticulars.setProvinces(provinceVos);
        return userParticulars;
    }
}

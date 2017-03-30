package com.movision.facade.boss;

import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
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

    @Autowired
    private CommentService commentService;


    //用于返回用户登录状态
    public String returnLoginType(String qq, String opendid, String sina) {
        if (qq != null) {
            return "1";//QQ
        }
        if (opendid != null) {
            return "2";//微信
        }
        if (sina != null) {
            return "3";//微博
        }
        if (qq != null && opendid != null) {
            return "4";//QQ,微信
        }
        if (qq != null && sina != null) {
            return "5";//QQ，微博
        }
        if (opendid != null && sina != null) {
            return "6";//微信，微博
        }
        if (qq != null && opendid != null && sina != null) {
            return "7";//QQ,微信,微博
        }
        return "0";
    }

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
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getAuthstatus() == null) {
                users.get(i).setAuthstatus(3);
            }
        }
        return users;
    }

    /**
     * 根据条件查看VIP列表
     *
     * @param name
     * @param phone
     * @param authstatus
     * @param begintime
     * @param endtime
     * @param type
     * @param pager
     * @return
     */
    public List<UserVo> queryByConditionvipList(String name, String phone, String authstatus, String begintime, String endtime, String type, Paging<UserVo> pager) {
        Map map = new HashedMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtil.isNotEmpty(name)) {
            map.put("name", name);
        }
        if (StringUtil.isNotEmpty(phone)) {
            map.put("phone", phone);
        }
        if (StringUtil.isNotEmpty(authstatus)) {
            if (Integer.parseInt(authstatus) == 3) {//为3时就是查未审核的用户，数据库中为null
                map.put("authstatus", null);
            } else {
                map.put("authstatus", authstatus);
            }
        }
        Date beg = null;
        Date end = null;
        if (StringUtil.isNotEmpty(begintime) && StringUtil.isNotEmpty(endtime)) {
            try {
                beg = format.parse(begintime);
                end = format.parse(endtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            map.put("begintime", beg);
            map.put("endtime", end);
        }
        if (StringUtil.isNotEmpty(type)) {
            map.put("type", type);
        }
        List<UserVo> users = userService.queryByConditionvipList(map, pager);
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getAuthstatus() == null) {
                users.get(i).setAuthstatus(3);
            }
        }
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
     * 对投稿做审核操作
     *
     * @param id
     * @return
     */
    public int update_contribute_audit(String id, String status) {
        Map map = new HashedMap();
        map.put("id", id);
        map.put("status", status);
        return submissionService.update_contribute_audit(map);
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
        Date beg = null;
        Date end = null;
        if (StringUtil.isNotEmpty(begintime) && StringUtil.isNotEmpty(endtime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                beg = format.parse(begintime);
                end = format.parse(endtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (StringUtil.isNotEmpty(nickname)) {
            map.put("nickname", nickname);//用户名
        }
        if (StringUtil.isNotEmpty(phone)) {
            map.put("phone", phone);//手机号
        }
        if (StringUtil.isNotEmpty(authentication)) {
            map.put("authentication", authentication);//实名认证
        }
        if (StringUtil.isNotEmpty(vip)) {
            map.put("level", vip);//是否是VIP
        }
        if (StringUtil.isNotEmpty(seal)) {
            map.put("status", seal);//是否封号
        }
        map.put("begintime", beg);//注册开始时间
        map.put("endtime", end);//注册结束时间
        if (StringUtil.isNotEmpty(pointsSort)) {
            map.put("pointsSort", pointsSort);//积分排序
        }
        if (StringUtil.isNotEmpty(postsumSort)) {
            map.put("postsumSort", postsumSort);//帖子排序
        }
        if (StringUtil.isNotEmpty(isessenceSort)) {
            map.put("isessenceSort", isessenceSort);//精贴排序
        }
        if (StringUtil.isNotEmpty(fansSort)) {
            map.put("fansSort", fansSort);//关注排序
        }
        if (StringUtil.isNotEmpty(conditionon)) {
            map.put("conditionon", conditionon);//条件1
        }
        if (StringUtil.isNotEmpty(conditiontwo)) {
            map.put("conditiontwo", conditiontwo);//条件2
        }
        if (StringUtil.isNotEmpty(price)) {
            map.put("price", price);//比较值
        }
        if (StringUtil.isNotEmpty(login)) {
            map.put("login", login);//登录状态
        }
        List<UserAll> list = userService.queryAllUserList(pager, map);
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).getAuthstatus() == null) {
                list.get(j).setAuthstatus(3);//如果当前用户没有实名认证 返回3作为提示
            }
        }
        for (int i = 0; i < list.size(); i++) {
            String resault = returnLoginType(list.get(i).getQq(), list.get(i).getOpenid(), list.get(i).getSina());
            list.get(i).setLogin(resault);
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

    /**
     * 根据id查询用户列表
     *
     * @param userids
     * @return
     */
    public List<UserAll> queryUserByIds(String userids) {
        List<UserAll> list = new ArrayList<>();
        System.out.println(userids);
        String[] str = userids.split(",");
        for (String itm : str) {
            UserAll users = userService.queryUserById(Integer.parseInt(itm));
            String resault = returnLoginType(users.getQq(), users.getOpenid(), users.getSina());
            users.setLogin(resault);
            list.add(users);
        }
        return list;
    }


    /**
     * 根据圈子关注查询用户列表
     *
     * @param circleid
     * @param pager
     * @return
     */
    public List<UserAll> queryAttentionUserList(String circleid, String type, Paging<UserAll> pager) {
        Map map = new HashedMap();
        map.put("circleid", Integer.parseInt(circleid));
        map.put("type", type);
        List<UserAll> list = userService.queryAttentionUserList(map, pager);
        for (int i = 0; i < list.size(); i++) {
            String resault = returnLoginType(list.get(i).getQq(), list.get(i).getOpenid(), list.get(i).getSina());
            list.get(i).setLogin(resault);
        }
        return list;
    }


    /**
     * 根据用户id查询用户帖子被评论的评论列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<CommentVo> queryCommentListByUserid(String userid, Paging<CommentVo> pager) {
        return commentService.queryCommentListByUserid(userid, pager);
    }

    /**
     * 查询用户评论帖子的评论列表
     *
     * @param user
     * @param pager
     * @return
     */
    public List<CommentVo> queryTheUserComments(String user, Paging<CommentVo> pager) {
        return commentService.queryTheUserComments(user, pager);
    }
}

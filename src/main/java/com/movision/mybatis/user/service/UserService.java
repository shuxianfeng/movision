package com.movision.mybatis.user.service;

import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.post.entity.ActiveVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.mapper.PostMapper;
import com.movision.mybatis.submission.entity.Submission;
import com.movision.mybatis.submission.entity.SubmissionVo;
import com.movision.mybatis.user.entity.*;
import com.movision.mybatis.user.mapper.UserMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/17 19:43
 */
@Service
@Transactional
public class UserService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    public LoginUser queryLoginUserByPhone(String phone) {
        try {
            log.info("根据手机号查询LoginUser用户, phone=" + phone);
            User user = new User();
            user.setPhone(phone);
            return userMapper.selectLoginUserByPhone(user);
        } catch (Exception e) {
            log.error("根据手机号查询LoginUser用户失败, phone=" + phone, e);
            throw e;
        }
    }

    public User queryUserByPhone(String phone) {
        try {
            log.info("根据手机号查询User用户, phone=" + phone);
            User user = new User();
            user.setPhone(phone);
            return userMapper.selectByPhone(user);
        } catch (Exception e) {
            log.error("根据手机号查询User用户失败,phone=" + phone, e);
            throw e;
        }
    }

    public boolean insertUser(User user) {
        try {
            log.info("插入用户表，user=" + user.toString());
            int n = userMapper.insert(user);
            return n == 1;
        } catch (Exception e) {
            log.error("插入用户表异常，user=" + user.toString(), e);
            throw e;
        }
    }

    public boolean updateUserPointsAdd(Map mapadd) {
        try {
            log.info("根据手机号给贴主增加积分");
            int n = userMapper.updateUserPointsAdd(mapadd);
            return n == 1;
        } catch (NumberFormatException e) {
            log.error("给贴主增加积分失败");
            throw e;
        }
    }

    public boolean updateUserPointsMinus(Map map) {
        try {
            log.info("根据用户id操作用户积分减操作");
            int n = userMapper.updateUserPointsMinus(map);
            return n == 1;
        } catch (NumberFormatException e) {
            log.error("减少用户积分操作失败");
            throw e;
        }
    }

    public int queryUserByPoints(String userid) {
        try {
            log.info("查询用户积分是否充足");
            return userMapper.queryUserByPoints(Integer.parseInt(userid));
        } catch (Exception e) {
            log.error("用户积分查询异常");
            throw e;
        }
    }

    public String isExistAccount(String phone) {
        try {
            int n = userMapper.isExistAccount(phone);
            return n >= 1 ? "isExist" : "";
        } catch (Exception e) {
            log.error("is exist account ", e);
        }
        return null;
    }

    public int registerAccount(RegisterUser registerUser) {
        return userMapper.registerAccount(registerUser);
    }

    public UserVo queryUserInfo(int userid) {
        try {
            log.info("查询用户个人主页--个人信息");
            return userMapper.queryUserInfo(userid);
        } catch (Exception e) {
            log.error("查询用户个人主页--个人信息失败");
            throw e;
        }
    }

    public List<PostVo> personPost(Paging<PostVo> pager, int userid) {
        try {
            log.info("查询个人主页中用户发布的历史帖子和用户分享的历史帖子");
            return postMapper.findAllPersonPost(pager.getRowBounds(), userid);
        } catch (Exception e) {
            log.error("查询个人主页中用户发布的历史帖子和用户分享的历史帖子失败");
            throw e;
        }
    }

    public List<ActiveVo> personActive(Paging<ActiveVo> pager, int userid) {
        try {
            log.info("查询个人主页中用户曾经参与过的所有活动列表");
            return postMapper.findAllPersonActive(pager.getRowBounds(), userid);
        } catch (Exception e) {
            log.error("查询个人主页中用户曾经参与过的所有活动列表失败");
            throw e;
        }
    }

    public User queryCircleMasterByPhone(String phone) {
        try {
            log.info("查询圈子所属圈主");
            return userMapper.queryCircleMasterByPhone(phone);
        } catch (Exception e) {
            log.error("查询圈子所属圈主失败");
            throw e;
        }
    }

    public List<User> queryCircleManagerList(int circleid) {
        try {
            log.info("查询圈子的所有管理员列表");
            return userMapper.queryCircleManagerList(circleid);
        } catch (Exception e) {
            log.error("查询圈子的所有管理员列表失败");
            throw e;
        }
    }

    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }

    /**
     * 查询用户微信id
     *
     * @param userid
     * @return
     */
    public String queryUserByOpenid(Integer userid) {
        try {
            log.info("查询用户微信id");
            return userMapper.queryUserByOpenid(userid);
        } catch (Exception e) {
            log.error("用户微信id查询异常");
            throw e;
        }
    }

    /**
     * 根据圈子id查询用户昵称
     * @param userid
     * @return
     */
    public String queryUserByNickname(Integer userid) {
        try {
            log.info("查询用户昵称");
            return userMapper.queryUserByNickname(userid);
        } catch (Exception e) {
            log.error("查询用户昵称异常");
            throw e;
        }
    }

    /**
     * 根据帖子id查询用户昵称
     *
     * @param postid
     * @return
     */
    public String queryUserByNicknameBy(Integer postid) {
        try {
            log.info("查询用户昵称");
            return userMapper.queryUserByNicknameBy(postid);
        } catch (Exception e) {
            log.error("查询用户昵称异常");
            throw e;
        }
    }

    /**
     * 后台管理 /-查询报名信息
     * @param id
     * @return
     */
    public User queryUserB(int id){
        try{
            log.info("查询成功");
            return  userMapper.findAllUser(id);
        }catch (Exception e){
            log.error("查询失败");
            throw  e;
        }
    }

    public User queryUser(String phone) {
        try {
            log.info("查询用户信息");
            return userMapper.queryUser(phone);
        } catch (Exception e) {
            log.error("查询用户信息异常");
            throw e;
        }
    }

    /**
     * 模糊查询发帖人
     *
     * @param name
     * @param pager
     * @return
     */
    public List<UserLike> likeQueryPostByNickname(String name, Paging<UserLike> pager) {
        try {
            log.info("模糊查询发帖人");
            return userMapper.findAlllikeQueryPostByNickname(name, pager.getRowBounds());
        } catch (Exception e) {
            log.error("模糊查询发帖人异常");
            throw e;
        }
    }

    public int queryUserPoint(int userid) {
        try {
            log.info("根据用户id查询用户积分");
            return userMapper.queryUserPoint(userid);
        } catch (Exception e) {
            log.error("根据用户id查询用户积分失败");
            throw e;
        }
    }

    /**
     * 根据用户id查询手机号
     *
     * @param userid
     * @return
     */
    public String queryUserbyPhoneByUserid(Integer userid) {
        try {
            log.info("根据用户id查询用户手机号");
            return userMapper.queryUserbyPhoneByUserid(userid);
        } catch (Exception e) {
            log.error("根据用户id查询用户手机号异常");
            throw e;
        }
    }

    /**
     * 查询圈子管理员列表
     *
     * @param circleid
     * @return
     */
    public List<User> queryUserByAdministratorList(Integer circleid) {
        try {
            log.info("查询圈子管理员列表");
            return userMapper.queryUserByAdministratorList(circleid);
        } catch (Exception e) {
            log.error("查询圈子管理员列表异常");
            throw e;
        }
    }

    /**
     * 查询圈子创建人名称
     *
     * @param userid
     * @return
     */
    public String queryUserByNicknameByAdmin(String userid) {
        try {
            log.info("查询圈子创建人名称");
            return userMapper.queryUserByNicknameByAdmin(userid);
        } catch (Exception e) {
            log.error("查询圈子创建人异常");
            throw e;
        }
    }

    public int updateRegisterUser(RegisterUser registerUser) {
        try {
            log.info("修改app用户token");
            return userMapper.updateRegisterUser(registerUser);
        } catch (Exception e) {
            log.error("修改app用户token异常", e);
            throw e;
        }
    }


    /**
     * 查询所有申请加v用户
     *
     * @param pager
     * @return
     */
    public List<UserVo> findAllqueryUsers(Paging<UserVo> pager) {
        try {
            log.info("查询所有申请加V用户");
            return userMapper.findAllqueryUsers(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询所有申请加V用户异常");
            throw e;
        }
    }


    /**
     * 查询所有VIP用户
     *
     * @param pager
     * @return
     */
    public List<UserVo> findAllqueryUserVIPByList(Paging<UserVo> pager) {
        try {
            log.info("查询所有VIP用户");
            return userMapper.findAllqueryUserVIPByList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询所有VIP用户异常");
            throw e;
        }
    }

    public int updateByPrimaryKeySelective(User user) {
        try {
            log.info("修改个人资料");
            return userMapper.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            log.error("修改个人资料失败", e);
            throw e;
        }
    }

    /**
     * 查询圈主列表
     *
     * @param pager
     * @return
     */
    public List<User> queryCircleManList(Paging<User> pager) {
        try {
            log.info("查询圈主列表");
            return userMapper.findAllQueryCircleManList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询圈主列表异常");
            throw e;
        }
    }

    /**
     * 对VIP列表排序
     *
     * @param map
     * @param pager
     * @return
     */
    public List<UserVo> queryAddVSortUser(Map map, Paging<UserVo> pager) {
        try {
            log.info("对VIP列表排序");
            return userMapper.findAllqueryAddVSortUser(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("对VIP列表排序异常");
            throw e;
        }
    }

    /**
     * 条件查询VIP申请用户列表
     *
     * @param map
     * @param pager
     * @return
     */
    public List<UserVo> queryUniteConditionByApply(Map map, Paging<UserVo> pager) {
        try {
            log.info("条件查询VIP申请用户列表");
            return userMapper.findAllQueryUniteConditionByApply(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件查询VIP申请用户列表异常");
            throw e;
        }
    }

    /**
     * 查询所有用户列表
     *
     * @param pager
     * @return
     */
    public List<UserAll> queryAllUserList(Paging<UserAll> pager, Map map) {
        try {
            log.info("查询所有用户列表");
            return userMapper.findAllqueryAllUserList(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询所有用户列表异常");
            throw e;
        }
    }
}

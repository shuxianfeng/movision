package com.movision.mybatis.bossUser.service;

import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.bossUser.entity.BossUserVo;
import com.movision.mybatis.bossUser.mapper.BossUserMapper;
import com.movision.utils.L;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/18 18:32
 */
@Service
@Transactional
public class BossUserService {

    private static Logger log = LoggerFactory.getLogger(BossUserService.class);

    @Autowired
    private BossUserMapper bossUserMapper;

    public BossUser queryAdminUserByPhone(String phone) {
        BossUser bossUser = new BossUser();
        bossUser.setPhone(phone);
        try {
            log.info("根据手机号查询boss用户,phone=" + phone);
            return bossUserMapper.selectByPhone(bossUser);
        } catch (Exception e) {
            log.error("根据手机号查询boss用户失败,phone=" + phone, e);
            throw e;
        }
    }

    public Boolean isExistPhone(String phone) {
        try {
            log.info("判断boss系统是否存在该手机号， phone = " + phone);
            int n = bossUserMapper.isExistPhone(phone);
            return n != 0;
        } catch (Exception e) {
            log.error("判断boss系统是否存在该手机号查询失败, phone = " + phone, e);
            throw e;
        }
    }

    public int addUser(BossUser bossUser) {
        try {
            log.info("新增boss用户， bossUser = " + bossUser.toString());
            bossUserMapper.insertSelective(bossUser);
            return bossUser.getId();
        } catch (Exception e) {
            log.error("新增boss用户失败, bossUser = " + bossUser.toString(), e);
            throw e;
        }
    }

    public Boolean updateUser(BossUser bossUser) {
        try {
            log.info("修改boss用户， bossUser = " + bossUser.toString());
            int n = bossUserMapper.updateByPrimaryKeySelective(bossUser);
            return n == 1;
        } catch (Exception e) {
            log.error("修改boss用户失败, bossUser = " + bossUser.toString(), e);
            throw e;
        }
    }

    public BossUser getBossUserByUsername(String username) {
        try {
            log.info("username=" + username);
            return bossUserMapper.selectByUsername(username);

        } catch (Exception e) {
            log.error("获取boss用户信息失败, username" + username);
            throw e;
        }
    }

    public Boolean updateUserLoginTime(BossUser bossUser) {
        try {
            log.info("更新boss用户登录时间, bossUser = " + bossUser.toString());
            int n = bossUserMapper.updateBossUserLoginTime(bossUser);
            return n == 1;
        } catch (Exception e) {
            log.error("更新boss用户登录时间失败");
            throw e;
        }
    }

    public void batchDelBossUser(int[] ids) {
        try {
            log.info("删除用户 " + ids);
            bossUserMapper.delBossUser(ids);
        } catch (Exception e) {
            log.error("删除用户失败");
            throw e;
        }
    }

    public List<Map<String, Object>> queryBossUserList(Paging<Map<String, Object>> pager, Map<String, Object> map) {
        try {
            log.info("查询boss用户列表");
            return bossUserMapper.findAllBossUser(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询boss用户列表异常", e);
            throw e;
        }
    }

    public Map<String, Object> queryBossUserDetial(Integer userid) {
        try {
            log.info("查询boss用户详情");
            return bossUserMapper.selectBossUserDetail(userid);
        } catch (Exception e) {
            log.error("查询boss用户详情失败", e);
            throw e;
        }
    }

    public BossUser selectByPrimaryKey(Integer id) {
        try {
            log.info("查询boss用户");
            return bossUserMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("查询boss用户失败", e);
            throw e;
        }
    }

    public List<BossUser> selectBossUserListByRoleId(Integer id) {
        try {
            log.info("根据角色id查询boss用户,roleid=" + id);
            return bossUserMapper.selectByRoleid(id);
        } catch (Exception e) {
            log.error("根据角色id查询boss用户失败,roleid=" + id, e);
            throw e;
        }
    }

    public int isExistSameUsername(BossUser bossUser) {
        try {
            log.info("校验是否存在相同的boss用户");
            return bossUserMapper.isExistSameName(bossUser);
        } catch (Exception e) {
            log.error("校验是否存在相同的boss用户失败", e);
            throw e;
        }
    }


    /**
     * 查询当前登录用户是否是特邀嘉宾
     *
     * @param userid
     * @return
     */
    public Integer queryUserByIscontribute(Integer userid) {
        try {
            log.info("查询当前登录用户是否是特邀嘉宾");
            return bossUserMapper.queryUserByIscontribute(userid);
        } catch (Exception e) {
            log.error("查询当前登录用户是否是特邀嘉宾异常");
            throw e;
        }
    }

    /**
     * 查询当前登录用户是否是圈主
     *
     * @param userid
     * @return
     */
    public Integer queryUserByiscircle(Integer userid) {
        try {
            log.info("查询当前登录用户是否是圈主");
            return bossUserMapper.queryUserByiscircle(userid);
        } catch (Exception e) {
            log.error("查询当前登录用户是否是圈主异常");
            throw e;
        }
    }

    /**
     * 查询当前登录用户是否是圈子管理员
     *
     * @param userid
     * @return
     */
    public Integer queryUserBycirclemanagements(Integer userid) {
        try {
            log.info("查询当前登录用户是否是圈子管理员");
            return bossUserMapper.queryUserBycirclemanagements(userid);
        } catch (Exception e) {
            log.error("查询当前登录用户是否是圈子管理员异常");
            throw e;
        }
    }

    /**
     * 根据用户id查询前台对应用户id
     *
     * @param userid
     * @return
     */
    public Integer queryUserById(Integer userid) {
        try {
            log.info("根据用户id查询前台对应用户id");
            return bossUserMapper.queryUserById(userid);
        } catch (Exception e) {
            log.error("根据用户id查询前台对应用户id异常");
            throw e;
        }
    }

    /**
     * 根据id查询该用户是否是特邀嘉宾
     *
     * @param userid
     * @return
     */
    public Integer queryUserIdBySpeciallyGuest(Integer userid) {
        try {
            log.info("根据id查询该用户是否是特邀嘉宾");
            return bossUserMapper.queryUserIdBySpeciallyGuest(userid);
        } catch (Exception e) {
            log.error("根据id查询该用户是否是特邀嘉宾异常");
            throw e;
        }
    }

    /**
     * 查询用户是否是管理员
     *
     * @param userid
     * @return
     */
    public BossUser queryUserByAdministrator(Integer userid) {
        try {
            log.info("查询用户是否是管理员");
            return bossUserMapper.queryUserByAdministrator(userid);
        } catch (Exception e) {
            log.error("查询用户是否是管理员异常");
            throw e;
        }
    }

    /**
     * 查询要添加的帖子是否属于本圈子
     * @param circleid
     * @return
     */
    public Integer queryCircleIdToCircle(Integer circleid) {
        try {
            log.info("查询要添加的帖子是否属于本圈子");
            return bossUserMapper.queryCircleIdToCircle(circleid);
        } catch (Exception e) {
            log.error("查询要添加的帖子是否属于本圈子异常");
            throw e;
        }
    }

    /**
     * 查询是否属于管理员管辖
     *
     * @param map
     * @return
     */
    public Integer queryCircleManageIdToCircle(Map map) {
        try {
            log.info("查询是否属于管理员管辖");
            return bossUserMapper.queryCircleManageIdToCircle(map);
        } catch (Exception e) {
            log.error("查询是否属于管理员管辖异常");
            throw e;
        }
    }

    /**
     * 查询用户操作帖子范畴
     *
     * @param ma
     * @return
     */
    public Integer queryPostByUserid(Map ma) {
        try {
            log.info("查询用户操作帖子范畴");
            return bossUserMapper.queryPostByUserid(ma);
        } catch (Exception e) {
            log.error("查询用户操作帖子范畴异常");
            throw e;
        }
    }

    /**
     * 查询用户可操作的帖子评论
     *
     * @param ma
     * @return
     */
    public Integer queryCommentByUserid(Map ma) {
        try {
            log.info("查询用户可操作的帖子评论");
            return bossUserMapper.queryCommentByUserid(ma);
        } catch (Exception e) {
            log.error("查询用户可操作的帖子评论异常");
            throw e;
        }
    }

    /**
     * 根据id查询出是否属于该圈主的帖子评论
     *
     * @param map
     * @return
     */
    public Integer queryCircleManageCommentByUserid(Map map) {
        try {
            log.info("根据id查询出是否属于该圈主的帖子评论");
            return bossUserMapper.queryCircleManageCommentByUserid(map);
        } catch (Exception e) {
            log.error("根据id查询出是否属于该圈主的帖子评论异常");
            throw e;
        }
    }

    /**
     * 查询评论是否为特约嘉宾评论
     *
     * @param id
     * @return
     */
    public Integer querySpeciallyCommentByUserid(Integer id) {
        try {
            log.info("查询评论是否为特约嘉宾的评论");
            return bossUserMapper.querySpeciallyCommentByUserid(id);
        } catch (Exception e) {
            log.error("查询评论是否是特约嘉宾的评论异常");
            throw e;
        }
    }
}

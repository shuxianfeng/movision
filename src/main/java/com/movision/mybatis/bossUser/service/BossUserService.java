package com.movision.mybatis.bossUser.service;

import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.bossUser.mapper.BossUserMapper;
import com.movision.mybatis.role.entity.Role;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/18 18:32
 */
@Service
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

    public Boolean addUser(BossUser bossUser) {
        try {
            log.info("新增boss用户， bossUser = " + bossUser.toString());
            int n = bossUserMapper.insertSelective(bossUser);
            return n == 1;
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
            log.info("username" + username);
            return bossUserMapper.selectByUsername(username);

        } catch (Exception e) {
            log.error("获取boss用户信息失败, username" + username);
            throw e;
        }
    }

    public Boolean updateUserLoginInfo(BossUser bossUser) {
        try {
            log.info("更新boss用户登录信息, bossUser = " + bossUser.toString());
            int n = bossUserMapper.updateBossUserLoginInfo(bossUser);
            return n == 1;
        } catch (Exception e) {
            log.error("更新boss用户登录信息失败");
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
            return bossUserMapper.selectBossUserList(pager.getRowBounds(), map);
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


}

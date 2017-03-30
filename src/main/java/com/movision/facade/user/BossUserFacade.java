package com.movision.facade.user;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.BusinessException;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.bossUser.entity.BossUserVo;
import com.movision.mybatis.bossUser.service.BossUserService;
import com.movision.utils.propertiesLoader.MsgPropertiesLoader;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/17 15:59
 */
@Service
public class BossUserFacade {

    @Autowired
    private BossUserService bossUserService;

    public List<Map<String, Object>> queryBossUserList(Paging<Map<String, Object>> pager, String username, String phone) {

        Map<String, Object> map = new HashedMap();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(username)) {
            map.put("username", username);
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(phone)) {
            map.put("phone", phone);
        }
        return bossUserService.queryBossUserList(pager, map);
    }


    public Boolean updateUser(BossUser bossUser) {
        return bossUserService.updateUser(bossUser);
    }

    public BossUser getByUsername(String username) {
        return bossUserService.getBossUserByUsername(username);
    }

    public Boolean updateLoginTime(BossUser bossUser) {
        return bossUserService.updateUserLoginTime(bossUser);
    }

    public void delUser(int[] ids) {
        bossUserService.batchDelBossUser(ids);
    }

    public Map<String, Object> getBossUserDetail(Integer userid) {
        return bossUserService.queryBossUserDetial(userid);
    }

    public BossUser selectByPrimaryKey(Integer id) {
        return bossUserService.selectByPrimaryKey(id);
    }


    /**
     * 选择性的更新用户信息
     *
     * @param bossUserVo
     */
    public void updateBySelectiveInfo(BossUserVo bossUserVo) {
        //判断用户是否存在
        Integer userid = bossUserVo.getId();
        BossUser bossUser = validateRequestBossuserIdIsExist(userid);

        BossUser newBossUser = new BossUser();
        newBossUser.setId(userid);
        newBossUser.setAfterlogintime(bossUser.getBeforelogintime());//更新上次登录时间
        newBossUser.setUsername(bossUserVo.getUsername());
        newBossUser.setName(bossUserVo.getName());
        newBossUser.setIssuper(bossUserVo.getIssuper());
        newBossUser.setIscircle(bossUserVo.getIscircle());
        newBossUser.setCommon(bossUserVo.getCommon());
        newBossUser.setCirclemanagement(bossUserVo.getCirclemanagement());
        newBossUser.setContributing(bossUserVo.getContributing());

        this.validateBossNameIsExist(newBossUser);
        //修改密码
        String password = bossUserVo.getNewPassword();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(password)) {
            String newPwd = new Md5Hash(password, null, 2).toString();
            newBossUser.setPassword(newPwd);
        }
        //更新用户信息
        this.updateUser(newBossUser);
    }

    /**
     * 修改用户信息时，校验该请求传入的bossuser的id是否存在
     * @param userid
     * @return
     */
    private BossUser validateRequestBossuserIdIsExist(Integer userid) {
        BossUser bossUser = this.selectByPrimaryKey(userid);
        if (null == bossUser) {
            throw new BusinessException(MsgCodeConstant.boss_user_not_exist, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.boss_user_not_exist)));
        }
        return bossUser;
    }

    /**
     * 增加用户
     *
     * @param bossUserVo
     */
    public int addBySelectiveInfo(BossUserVo bossUserVo) {

        String phone = bossUserVo.getPhone();
        this.validateBossuserPhoneIsExist(phone);

        //封装新增信息
        BossUser newBossUser = new BossUser();
        newBossUser.setName(bossUserVo.getName());
        newBossUser.setPhone(phone);
        newBossUser.setUsername(bossUserVo.getUsername());
        newBossUser.setIssuper(bossUserVo.getIssuper());
        newBossUser.setIscircle(bossUserVo.getIscircle());
        newBossUser.setCommon(bossUserVo.getCommon());
        newBossUser.setCirclemanagement(bossUserVo.getCirclemanagement());
        newBossUser.setContributing(bossUserVo.getContributing());

        this.validateBossNameIsExist(newBossUser);
        //新增密码
        String password = bossUserVo.getNewPassword();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(password)) {
            String newPwd = new Md5Hash(password, null, 2).toString();
            newBossUser.setPassword(newPwd);
        }
        return bossUserService.addUser(newBossUser);

    }

    /**
     * 校验新增管理员的手机号是否已经存在
     *
     * @param phone
     */
    private void validateBossuserPhoneIsExist(String phone) {
        Boolean isExistPhone = bossUserService.isExistPhone(phone);
        if (isExistPhone) {
            throw new BusinessException(MsgCodeConstant.phone_is_exist, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.phone_is_exist)));
        }
    }

    /**
     * 校验bossuser 名称是否已经存在
     *
     * @param bossUser
     */
    private void validateBossNameIsExist(BossUser bossUser) {
        //检验菜单名是否已经存在
        int isExist = bossUserService.isExistSameUsername(bossUser);
        if (isExist >= 1) {
            throw new BusinessException(MsgCodeConstant.boss_username_is_exist, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.boss_username_is_exist)));
        }
    }


    public BossUser getUserByPhone(String phone) {
        return bossUserService.queryAdminUserByPhone(phone);
    }


}

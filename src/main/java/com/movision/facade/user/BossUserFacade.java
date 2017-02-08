package com.movision.facade.user;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.AuthException;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.bossUser.service.BossUserService;
import com.movision.mybatis.role.entity.Role;
import com.movision.mybatis.user.entity.LoginUser;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;
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

    public List<BossUser> queryBossUserList(String pageNo, String pageSize, String username, String phone) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<BossUser> pager = new Paging<BossUser>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashedMap();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(username)) {
            map.put("username", username);
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(phone)) {
            map.put("phone", phone);
        }
        return bossUserService.queryBossUserList(pager, map);
    }

    public Boolean addUser(BossUser bossUser) {
        return bossUserService.addUser(bossUser);
    }

    public Boolean updateUser(BossUser bossUser) {
        return bossUserService.updateUser(bossUser);
    }

    public BossUser getByUsername(String username) {
        return bossUserService.getBossUserByUsername(username);
    }

    public Boolean updateLoginInfo(BossUser bossUser) {
        return bossUserService.updateUserLoginInfo(bossUser);
    }

    public void delUser(int[] ids) {
        bossUserService.batchDelBossUser(ids);
    }

}

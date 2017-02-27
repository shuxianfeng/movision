package com.movision.facade.boss;

import com.movision.mybatis.user.entity.UserVo;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/2/24 14:49
 */
@Service
public class UserManageFacade {
    @Autowired
    UserService userService;

    /**
     * 查看vip申请列表
     *
     * @param pager
     * @return
     */
    public List<UserVo> queryApplyVipList(Paging<UserVo> pager) {
        List<UserVo> resault = new ArrayList<>();
        List<Integer> users = userService.findAllqueryUsers(pager);//查询所有申请加v用户
        for (int i = 0; i < users.size(); i++) {//查看vip申请列表
            UserVo usermassage = userService.queryApplyVipList(users.get(i));
            resault.add(usermassage);
        }
        return resault;
    }

    /**
     * 查询所有VIP用户
     *
     * @param pager
     * @return
     */
    public List<UserVo> queryVipList(Paging<UserVo> pager) {
        List<UserVo> users = userService.findAllqueryVipList(pager);//查询所有VIP用户
        return users;
    }
}

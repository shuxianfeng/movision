package com.zhuhuibao.mybatis.oms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.mybatis.oms.entity.User;
import com.zhuhuibao.mybatis.oms.mapper.UserMapper;
import com.zhuhuibao.utils.MsgPropertiesUtils;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
@Service
@Transactional
public class UserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserMapper userMapper;

    public User selectUserByAccount(String userName)
    {
        User account = new User();
        try
        {
            account = userMapper.selectUserByAccount(userName);
        }
        catch(Exception e)
        {
            log.error("query user by account error!",e);
        }
        return account;
    }

    public Response selectByPrimaryKey(Integer id)
    {
        Response response = new Response();
        try
        {
            User account = userMapper.selectByPrimaryKey(id);
            response.setData(account);
        }
        catch(Exception e)
        {
            log.error("query user by account error!",e);
            response.setCode(MsgCodeConstant.response_status_400);
            response.setMsgCode(MsgCodeConstant.mcode_common_failure);
            response.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return response;
    }
    /**
     * 更新用户信息
     * @param record
     */
    public void updateRecordByPrimaryKey(User record)
    {
        
        try
        {
             userMapper.updateRecordByPrimaryKey(record);
            
        }
        catch(Exception e)
        {
            log.error("query user by account error!",e);
             
        }
        
    }
}

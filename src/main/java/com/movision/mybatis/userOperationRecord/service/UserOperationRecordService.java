package com.movision.mybatis.userOperationRecord.service;

import com.movision.mybatis.userOperationRecord.entity.UserOperationRecord;
import com.movision.mybatis.userOperationRecord.mapper.UserOperationRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author shuxf
 * @Date 2017/4/21 17:16
 */
@Service
public class UserOperationRecordService {

    private Logger log = LoggerFactory.getLogger(UserOperationRecordService.class);

    @Autowired
    private UserOperationRecordMapper userOperationRecordMapper;

    public UserOperationRecord queryUserOperationRecordByUser(int userid) {
        try {
            log.info("根据用户id查询用户是否首次点赞过是否首次评论过是否首次收藏过");
            return userOperationRecordMapper.queryUserOperationRecordByUser(userid);
        } catch (Exception e) {
            log.error("根据用户id查询用户是否首次点赞过是否首次评论过是否首次收藏过失败", e);
            throw e;
        }
    }

    public void insertUserOperationRecord(UserOperationRecord userOperationRecord) {
        try {
            log.info("插入用户操作记录");
            userOperationRecordMapper.insertSelective(userOperationRecord);
        } catch (Exception e) {
            log.error("插入用户操作记录失败");
            throw e;
        }
    }

    public void updateUserOperationRecord(UserOperationRecord userOperationRecord) {
        try {
            log.info("更新用户操作记录");
            userOperationRecordMapper.updateUserOperationRecord(userOperationRecord);
        } catch (Exception e) {
            log.error("更新用户操作记录失败");
            throw e;
        }
    }
}

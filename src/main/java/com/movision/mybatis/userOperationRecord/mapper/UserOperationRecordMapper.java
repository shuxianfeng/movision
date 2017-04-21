package com.movision.mybatis.userOperationRecord.mapper;

import com.movision.mybatis.userOperationRecord.entity.UserOperationRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOperationRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOperationRecord record);

    int insertSelective(UserOperationRecord record);

    UserOperationRecord selectByPrimaryKey(Integer id);

    UserOperationRecord queryUserOperationRecordByUser(int userid);

    void updateUserOperationRecord(UserOperationRecord userOperationRecord);

    int updateByPrimaryKeySelective(UserOperationRecord record);

    int updateByPrimaryKey(UserOperationRecord record);
}
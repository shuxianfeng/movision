package com.movision.mybatis.postAndUserRecord.service;

import com.movision.mybatis.postAndUserRecord.entity.PostAndUserRecord;
import com.movision.mybatis.postAndUserRecord.mapper.PostAndUserRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @Author zhanglei
 * @Date 2017/4/27 19:37
 */
@Repository
public class PostAndUserRecordService implements PostAndUserRecordMapper {
    @Autowired
    private MongoTemplate mongoTemplate;


    public void insert(PostAndUserRecord postAndUserRecord){

        mongoTemplate.insert(postAndUserRecord);
    }
}

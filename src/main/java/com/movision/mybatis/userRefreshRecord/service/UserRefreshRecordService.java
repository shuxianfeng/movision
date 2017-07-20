package com.movision.mybatis.userRefreshRecord.service;

import com.mongodb.*;
import com.movision.mybatis.userRefreshRecord.entity.UesrreflushCount;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecord;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecordVo;
import com.movision.mybatis.userRefreshRecord.mapper.UserRefreshRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/6/14 16:03
 */
@Service
public class UserRefreshRecordService implements UserRefreshRecordMapper {

    @Autowired
    private MongoTemplate mongoTemplate;


    public void insert(UserRefreshRecord userRefreshRecord) {

        mongoTemplate.insert(userRefreshRecord);
    }

    /**
     * 查询
     *根据postid查询帖子的流浪量
     * @return
     */
    public Integer postcount(int postid) {
        int obj = 0;
        try {
            MongoClient mClient = new MongoClient("localhost:27017");
            DB db = mClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            BasicDBObject queryObject = new BasicDBObject("postid", postid);
            obj = collection.find(queryObject).count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;

    }
}

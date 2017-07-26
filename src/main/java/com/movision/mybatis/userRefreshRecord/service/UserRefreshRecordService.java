package com.movision.mybatis.userRefreshRecord.service;

import com.mongodb.*;
import com.movision.mybatis.opularSearchTerms.entity.OpularSearchTermsVo;
import com.movision.mybatis.userRefreshRecord.entity.UesrreflushCount;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecord;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecordVo;
import com.movision.mybatis.userRefreshRecord.mapper.UserRefreshRecordMapper;
import com.movision.utils.propertiesLoader.MongoDbPropertiesLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
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
     *根据postid查询帖子的浏览量
     * @return
     */
    public Integer postcount(int postid) {
        int obj = 0;
        try {
            MongoClient mClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            DB db = mClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            BasicDBObject queryObject = new BasicDBObject("postid", postid);
            obj = collection.find(queryObject).count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;

    }

    public List<UesrreflushCount> group() {

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("postid").count().as("count"),
                Aggregation.sort(Sort.Direction.DESC, "count")
        );
        AggregationResults<UesrreflushCount> list = mongoTemplate.aggregate(aggregation, "userRefreshRecord", UesrreflushCount.class);
        List<UesrreflushCount> list1 = list.getMappedResults();
        return list1;

    }


    public List mongoList(String begintime, String endtime) {
        List<DBObject> list = null;
        BasicDBList condList = new BasicDBList();//存放查询条件的集合
        BasicDBObject searchQuery = new BasicDBObject();
        try {
            MongoClient mClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            DB db = mClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            searchQuery.put("intime", BasicDBObjectBuilder.start("$gte", begintime + " 00:00:00").add("$lte", endtime + " 23:59:59").get());
            condList.add(searchQuery);
            DBCursor dbCursor = collection.find(condList);
            list = dbCursor.toArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

}

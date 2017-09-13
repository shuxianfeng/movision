package com.movision.mybatis.userRefreshRecord.service;

import com.mongodb.*;
import com.movision.mybatis.userRefreshRecord.entity.UserReflushCount;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecord;
import com.movision.mybatis.userRefreshRecord.mapper.UserRefreshRecordMapper;
import com.movision.utils.propertiesLoader.MongoDbPropertiesLoader;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @Author zhanglei
 * @Date 2017/6/14 16:03
 */
@Service
public class UserRefreshRecordService implements UserRefreshRecordMapper {

    private static Logger log = LoggerFactory.getLogger(UserRefreshRecordService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(UserRefreshRecord userRefreshRecord) {

        mongoTemplate.insert(userRefreshRecord);
    }

    /**
     * 根据postid查询帖子的浏览量
     *
     * @return
     */
    public Integer postcount(int postid) {
        MongoClient mongoClient = null;
        int obj = 0;
        DB db = null;
        DBCursor cursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            BasicDBObject queryObject = new BasicDBObject("postid", postid);
            cursor = collection.find(queryObject);
            obj = cursor.count();
            cursor.close();
        } catch (Exception e) {
            log.error("根据postid查询帖子的浏览量失败", e);
        } finally {
            if (null != db) {
                db.requestDone();
                cursor.close();
                mongoClient.close();
            }
        }
        return obj;

    }

    /**
     * 查询mongo中的用户浏览帖子记录（已经按照浏览数从大到小排列）
     *
     * @return
     */
    public List<UserReflushCount> groupByPostid() {

        Aggregation aggregation = Aggregation.newAggregation(
                group("postid").count().as("count"),
                sort(Sort.Direction.DESC, "count")
        );
        AggregationResults<UserReflushCount> list = mongoTemplate.aggregate(aggregation, "userRefreshRecord", UserReflushCount.class);
        List<UserReflushCount> list1 = list.getMappedResults();
        return list1;

    }


    /*public List<DBObject> getMongoListByTimeRange(String begintime, String endtime) {
        MongoClient mongoClient = null;
        DB db = null;
        List<DBObject> list = null;
        BasicDBList condList = new BasicDBList();   //存放查询条件的集合
        BasicDBObject param = new BasicDBObject();
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            //数据表
            DBCollection table = db.getCollection("userRefreshRecord");

            if (StringUtils.isNotBlank(begintime) && StringUtils.isNotBlank(endtime)) {

                condList.add(new BasicDBObject("intime",
                        new BasicDBObject("$gte", begintime + " 00:00:00").append("$lte", endtime + " 23:59:59")));
            }

            if (condList != null && condList.size() > 0) {
                param.put("$and", condList);//多条件查询使用and
            }
            dbCursor = table.find(param);
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("根据开始和结束时间查询用户浏览记录失败", e);
        } finally {
            if (null != db) {
                db.requestDone();
                dbCursor.close();
                mongoClient.close();
            }
        }
        return list;

    }*/

    /**
     * 统计在一个月内 一个帖子的浏览次数
     *
     * @param begintime
     * @param endtime
     * @return
     */
    public List<UserReflushCount> getPostViewRecord(String begintime, String endtime) {

        log.debug("从mongoDB中查询帖子浏览记录");
        TypedAggregation<UserRefreshRecord> agg = Aggregation.newAggregation(
                UserRefreshRecord.class,
                project("postid", "intime")
                , match(Criteria.where("intime").gte(begintime + " 00:00:00").lte(endtime + " 23:59:59"))
                , group("postid").count().as("count")
                , sort(Sort.Direction.DESC, "count")
        );
        log.debug("执行语句=" + agg.toString());
        AggregationResults<UserReflushCount> result = mongoTemplate.aggregate(agg, UserReflushCount.class);
        log.debug("查询结果=" + result.getMappedResults());
        return result.getMappedResults();
    }


}

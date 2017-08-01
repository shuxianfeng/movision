package com.movision.mybatis.userRefreshRecord.service;

import com.mongodb.*;
import com.movision.mybatis.userRefreshRecord.entity.UesrreflushCount;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecord;
import com.movision.mybatis.userRefreshRecord.mapper.UserRefreshRecordMapper;
import com.movision.utils.propertiesLoader.MongoDbPropertiesLoader;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
     *根据postid查询帖子的浏览量
     * @return
     */
    public Integer postcount(int postid) {
        int obj = 0;
        DB db = null;
        try {
            MongoClient mClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            BasicDBObject queryObject = new BasicDBObject("postid", postid);
            obj = collection.find(queryObject).count();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != db) {
                db.requestDone();
                db = null;
            }
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


    public List getMongoListByTimeRange(String begintime, String endtime) {
        DB db = null;
        List<DBObject> list = null;
        BasicDBList condList = new BasicDBList();//存放查询条件的集合
        BasicDBObject param = new BasicDBObject();
        try {
            MongoClient mClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            if (StringUtils.isNotBlank(begintime) && StringUtils.isNotBlank(endtime)) {
                condList.add(new BasicDBObject("intime", new BasicDBObject("$gte", begintime + " 00:00:00").append("$lte", endtime + " 23:59:59")));
            }
            if (condList != null && condList.size() > 0) {
                param.put("$and", condList);//多条件查询使用and
            }
            DBCursor dbCursor = collection.find(param);
            list = dbCursor.toArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != db) {
                db.requestDone();
                db = null;
            }
        }
        return list;

    }

}

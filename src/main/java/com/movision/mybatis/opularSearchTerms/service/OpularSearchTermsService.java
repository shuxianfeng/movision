package com.movision.mybatis.opularSearchTerms.service;

import com.mongodb.*;
 import com.movision.mybatis.opularSearchTerms.entity.OpularSearchTerms;
import com.movision.mybatis.opularSearchTerms.entity.OpularSearchTermsVo;
import com.movision.mybatis.opularSearchTerms.mapper.OpularSearchTermsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
 import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
 import java.util.List;


/**
 * @Author zhanglei
 * @Date 2017/4/18 19:13
 */
@Repository
 public class OpularSearchTermsService implements OpularSearchTermsMapper {

    @Autowired
      private MongoTemplate mongoTemplate;

    @Override
     public void insert(OpularSearchTerms opularSearchTerms) {
        // TODO Auto-generated method stub
        mongoTemplate.insert(opularSearchTerms);
    }


    public List group() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("keywords").count().as("count"),
                Aggregation.sort(Sort.Direction.DESC, "count"),
                Aggregation.limit(20)

        );
        AggregationResults<OpularSearchTermsVo> list = mongoTemplate.aggregate(aggregation, "opularSearchTerms", OpularSearchTermsVo.class);
        System.out.print(aggregation);
        List<OpularSearchTermsVo> list1 = list.getMappedResults();
        return list1;

    }

    public List histroyWords(int userid) {
        List<DBObject> list = null;
        try {
            MongoClient mClient = new MongoClient("39.108.84.156:27017");
            DB db = mClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("opularSearchTerms");
            BasicDBObject queryObject = new BasicDBObject("userid", userid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("keywords", 1);
            DBCursor obj = collection.find(queryObject, keys).limit(12).sort(new BasicDBObject("intime", -1));
            list = obj.toArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
 }


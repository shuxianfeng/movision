package com.movision.mybatis.postAndUserRecord.service;

import com.mongodb.*;
import com.movision.mybatis.opularSearchTerms.entity.OpularSearchTermsVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postAndUserRecord.entity.PostAndUserRecord;
import com.movision.mybatis.postAndUserRecord.mapper.PostAndUserRecordMapper;
import com.movision.utils.propertiesLoader.MongoDbPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @Author zhanglei
 * @Date 2017/4/27 19:37
 */
@Repository
public class PostAndUserRecordService implements PostAndUserRecordMapper {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static Logger log = LoggerFactory.getLogger(PostAndUserRecordService.class);

    public void insert(PostAndUserRecord postAndUserRecord){

        mongoTemplate.insert(postAndUserRecord);
    }



      public List UserLookingHistory(int userid,int page,int pageSize ){
         List<DBObject> list = null;
          DB db = null;
          try {
            MongoClient mClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
              db = mClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("postAndUserRecord");

            BasicDBObject queryObject = new BasicDBObject("userid", userid);
            //指定需要显示列
           /** BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 1);
            keys.put("userid", 1);
            keys.put("postid", 1);
            keys.put("crileid", 1);
            keys.put("intime", 1);*/
          //  List sum=collection.distinct("postid",queryObject).skip((page - 1) * 10).sort(new BasicDBObject("intime", -1)).limit(pageSize);
            DBCursor obj = collection.find(queryObject).skip((page - 1) * 10).sort(new BasicDBObject("intime", -1)).limit(pageSize);
             list = obj.toArray();
            for (int i =0;i<list.size();i++){
                for (int j =list.size()-1;j>i;j--){
                    if(list.get(i).get("postid").equals(list.get(j).get("postid"))){
                        list.remove(j);
                    }
                }
             }
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

    /** public Map UserLookingHistory(int userid,int page,int pageSize ) {
        Map map=null;
        try {
            MongoClient mClient = new MongoClient("localhost:27017");
            DB db = mClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("postAndUserRecord");
            List<DBObject> ops = new ArrayList<DBObject>();
            DBObject query = new BasicDBObject();
            query.put("userid", userid);
            DBObject match = new BasicDBObject("$match", query);
            BasicDBObject keys = new BasicDBObject();
             keys.put("userid", 1);
            keys.put("postid", 1);
            keys.put("crileid", 1);
            keys.put("intime", 1);

            DBObject project = new BasicDBObject("$project", keys);
              /* Group操作
             DBObject groupFields = new BasicDBObject("_id", new BasicDBObject("postid","$postid"));
             DBObject group = new BasicDBObject("$group", groupFields);*/

            // 利用$group进行分组
          /**  DBObject group = new BasicDBObject("postid", "$postid");
            group.put("userid", "$userid");
            group.put("crileid", "$crileid");
            group.put("intime", "$intime");

            DBObject skip=new BasicDBObject("$skip",(page - 1) * 10);

            DBObject limit = new BasicDBObject("$limit", pageSize);

            DBObject sort = new BasicDBObject("$sort", new BasicDBObject(
                    "intime", -1));

            AggregationOutput output = collection.aggregate(match,project,group,skip,limit,sort);
               map=output.getCommandResult().toMap();
        }catch (Exception e){
            e.printStackTrace();
        }

        return map;

    }*/



}

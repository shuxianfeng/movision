package com.movision.mybatis.userDontLike.service;

import com.mongodb.*;
import com.movision.mybatis.opularSearchTerms.service.OpularSearchTermsService;
import com.movision.mybatis.userDontLike.entity.UserDontLike;
import com.movision.mybatis.userDontLike.mapper.UserDontLikeMapper;
import com.movision.utils.propertiesLoader.MongoDbPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/8/2 9:32
 */
@Service
public class UserDontLikeService implements UserDontLikeMapper {

    private static Logger log = LoggerFactory.getLogger(UserDontLikeService.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(UserDontLike userDontLike) {

        mongoTemplate.insert(userDontLike);
    }

    public List Isx(int userid) {
        MongoClient mClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor cursor = null;
        try {
            mClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userDontLike");
            BasicDBObject queryObject = new BasicDBObject("userid", userid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("type", 1);
            keys.put("postid", 1);
            cursor = collection.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = cursor.toArray();
            cursor.close();
        } catch (Exception e) {
            log.error("查询用户不喜欢的", e);
        } finally {
            if (null != db) {
                db.requestDone();
                cursor.close();
                mClient.close();
            }
        }
        return list;
    }


}

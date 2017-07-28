package com.movision.mybatis.labelSearchTerms.service;

import com.mongodb.*;
import com.movision.mybatis.labelSearchTerms.entity.LabelSearchTerms;
import com.movision.mybatis.labelSearchTerms.mapper.LabelSearchTermsMapper;
import com.movision.utils.propertiesLoader.MongoDbPropertiesLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/7/28 15:49
 */
@Service
public class LabelSearchTermsService implements LabelSearchTermsMapper {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(LabelSearchTerms labelSearchTerms) {

        mongoTemplate.insert(labelSearchTerms);
    }


    /**
     * 标签搜索历史记录
     *
     * @param userid
     * @return
     */
    public List histroyWordsLabel(int userid) {
        List<DBObject> list = null;
        try {
            MongoClient mClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            DB db = mClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("labelSearchTerms");
            BasicDBObject queryObject = new BasicDBObject("userid", userid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("name", 1);
            DBCursor obj = collection.find(queryObject, keys).limit(12).sort(new BasicDBObject("intime", -1));
            list = obj.toArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

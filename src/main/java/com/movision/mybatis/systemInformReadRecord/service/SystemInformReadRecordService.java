package com.movision.mybatis.systemInformReadRecord.service;

import com.mongodb.*;
import com.movision.mybatis.systemInformReadRecord.entity.SystemInformReadRecord;
import com.movision.mybatis.systemInformReadRecord.mapper.SystemInformReadRecordMapper;
import com.movision.utils.propertiesLoader.MongoDbPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/5 15:18
 */
@Service
public class SystemInformReadRecordService implements SystemInformReadRecordMapper {

    private static Logger log = LoggerFactory.getLogger(SystemInformReadRecordService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(SystemInformReadRecord systemInformReadRecord) {

        mongoTemplate.insert(systemInformReadRecord);
    }

    /**
     * 查询个人已读系统消息记录
     *
     * @param userid
     * @return
     */
    public List selectPersonSystemInfoRecord(int userid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor cursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("systemInformReadRecord");
            BasicDBObject queryObject = new BasicDBObject("userid", userid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("userid", 1);
            keys.put("inform_identity", 1);

            //按照intime倒序排列
            cursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = cursor.toArray();
            for (int i = 0; i < list.size(); i++) {
                for (int j = list.size() - 1; j > i; j--) {
                    if (list.get(i).get("inform_identity").equals(list.get(j).get("inform_identity"))) {
                        list.remove(j);
                    }
                }
            }
            list.subList(0, 10);
            cursor.close();
        } catch (Exception e) {
            log.error("查询个人已读系统消息记录", e);
        } finally {
            if (null != db) {
                db.requestDone();
                cursor.close();
                mongoClient.close();
            }
        }
        return list;
    }


}

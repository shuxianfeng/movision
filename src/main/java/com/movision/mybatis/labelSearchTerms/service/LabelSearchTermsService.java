package com.movision.mybatis.labelSearchTerms.service;

import com.mongodb.*;
import com.movision.facade.paging.PageFacade;
import com.movision.mybatis.labelSearchTerms.entity.LabelSearchTerms;
import com.movision.mybatis.labelSearchTerms.mapper.LabelSearchTermsMapper;
import com.movision.mybatis.userRefreshRecord.service.UserRefreshRecordService;
import com.movision.utils.propertiesLoader.MongoDbPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger log = LoggerFactory.getLogger(LabelSearchTermsService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PageFacade pageFacade;

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
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor cursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("labelSearchTerms");
            if (null == table) {
                return list;
            }
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("isdel", 0);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("name", 1);
            keys.put("type", 1);
            keys.put("labelid", 1);

            cursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = cursor.toArray();
            for (int i = 0; i < list.size(); i++) {
                for (int j = list.size() - 1; j > i; j--) {
                    if (list.get(i).get("labelid").equals(list.get(j).get("labelid"))) {
                        list.remove(j);
                    }
                }
            }
//            list.subList(0, 12);
            list = pageFacade.getPageList(list, 1, 12);
            cursor.close();
        } catch (Exception e) {
            log.error("查询标签搜索历史记录", e);
        } finally {
            if (null != db) {
                db.requestDone();
                cursor.close();
                mongoClient.close();
            }
        }
        return list;
    }


    public Integer updateColData(int userid) {
        MongoClient mongoClient = null;
        DB db = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("labelSearchTerms");
            BasicDBObject doc = new BasicDBObject();
            BasicDBObject res = new BasicDBObject();
            res.put("isdel", 1);
            System.out.println("将数据集中的所有文档的isdel修改成1！");
            doc.put("$set", res);
            table.update(new BasicDBObject("userid", userid), doc, false, true);

        } catch (Exception e) {
            log.error("把标签搜索记录的isdel改成1，失败", e);
        } finally {
            if (null != db) {
                db.requestDone();
                mongoClient.close();
            }
        }
        return 1;
    }
}

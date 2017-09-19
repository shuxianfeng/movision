package com.movision.mybatis.opularSearchTerms.service;

import com.mongodb.*;
import com.movision.facade.index.FacadePost;
import com.movision.facade.paging.PageFacade;
import com.movision.mybatis.opularSearchTerms.entity.OpularSearchTerms;
import com.movision.mybatis.opularSearchTerms.entity.OpularSearchTermsVo;
import com.movision.mybatis.opularSearchTerms.mapper.OpularSearchTermsMapper;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecordVo;
import com.movision.mybatis.userRefreshRecord.service.UserRefreshRecordService;
import com.movision.utils.propertiesLoader.MongoDbPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
 import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.comparator.ComparableComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * @Author zhanglei
 * @Date 2017/4/18 19:13
 */
@Repository
 public class OpularSearchTermsService implements OpularSearchTermsMapper {

    private static Logger log = LoggerFactory.getLogger(OpularSearchTermsService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PageFacade pageFacade;

    @Override
     public void insert(OpularSearchTerms opularSearchTerms) {

        mongoTemplate.insert(opularSearchTerms);
    }


    public List group() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("keywords").count().as("count"),
                Aggregation.sort(Sort.Direction.DESC, "count"),
                 Aggregation.limit(20)

        );
        AggregationResults<OpularSearchTermsVo> list = mongoTemplate.aggregate(aggregation, "opularSearchTerms", OpularSearchTermsVo.class);
        List<OpularSearchTermsVo> list1 = list.getMappedResults();
        return list1;

    }

    /**
     * 查询
     * @return
     */
    public List  userFlush(int userid) {
              Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("userid").is(userid)),
                    Aggregation.group("crileid").count().as("count"),
                    Aggregation.sort(Sort.Direction.DESC, "count"),
                    Aggregation.limit(1)
            );
            AggregationResults<UserRefreshRecordVo> list = mongoTemplate.aggregate(aggregation, "userRefreshRecord", UserRefreshRecordVo.class);
            List<UserRefreshRecordVo> list1 = list.getMappedResults();
            return list1;

    }

    public List histroyWords(int userid) {
        MongoClient mClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor cursor = null;
        try {
            mClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("opularSearchTerms");
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("isdel", 0);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("keywords", 1);
            cursor = collection.find(queryObject, keys).sort(new BasicDBObject("intime", 1)).limit(12);
            list = cursor.toArray();
            for (int i = 0; i < list.size(); i++) {
                for (int j = list.size() - 1; j > i; j--) {
                    if (list.get(i).get("keywords").equals(list.get(j).get("keywords"))) {
                        list.remove(j);
                    }
                }
            }
//            list.subList(0, 12);
            list = pageFacade.getPageList(list, 1, 12);
            cursor.close();
        } catch (Exception e) {
            log.error("获取帖子热门搜索词失败", e);
        } finally {
            if (null != db) {
                db.requestDone();
                cursor.close();
                mClient.close();
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
            DBCollection table = db.getCollection("opularSearchTerms");
            BasicDBObject doc = new BasicDBObject();
            BasicDBObject res = new BasicDBObject();
            res.put("isdel", 1);
            System.out.println("将数据集中的所有文档的isdel修改成1！");
            doc.put("$set", res);
            table.update(new BasicDBObject("userid", userid), doc, false, true);
        } catch (Exception e) {
            log.error("清除首页搜索历史失败", e);
        } finally {
            if (null != db) {
                db.requestDone();
                mongoClient.close();
            }
        }
        return 1;
    }
 }


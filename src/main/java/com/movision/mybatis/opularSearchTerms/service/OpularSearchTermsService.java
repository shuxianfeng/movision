package com.movision.mybatis.opularSearchTerms.service;

 import com.mongodb.BasicDBList;
 import com.mongodb.BasicDBObject;
 import com.mongodb.DBObject;
 import com.movision.mybatis.opularSearchTerms.entity.OpularSearchTerms;
import com.movision.mybatis.opularSearchTerms.mapper.OpularSearchTermsMapper;
import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

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





 }

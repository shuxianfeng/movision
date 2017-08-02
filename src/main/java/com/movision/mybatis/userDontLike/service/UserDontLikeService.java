package com.movision.mybatis.userDontLike.service;

import com.movision.mybatis.userDontLike.entity.UserDontLike;
import com.movision.mybatis.userDontLike.mapper.UserDontLikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author zhanglei
 * @Date 2017/8/2 9:32
 */
@Service
public class UserDontLikeService implements UserDontLikeMapper {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(UserDontLike userDontLike) {

        mongoTemplate.insert(userDontLike);
    }
}

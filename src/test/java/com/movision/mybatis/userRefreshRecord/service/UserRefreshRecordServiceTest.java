package com.movision.mybatis.userRefreshRecord.service;

import com.mongodb.DBObject;
import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/13 18:44
 */
public class UserRefreshRecordServiceTest extends SpringTestCase {
    @Autowired
    private UserRefreshRecordService userRefreshRecordService;

    @Test
    public void getPostViewRecordByUseridAndCircleid() throws Exception {

        List<DBObject> list = userRefreshRecordService.getPostViewRecordByUseridAndCircleid(48, 1);

        System.out.printf(list.toString());
    }

}
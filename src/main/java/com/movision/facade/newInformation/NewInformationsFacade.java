package com.movision.facade.newInformation;

import com.movision.mybatis.newInformation.entity.NewInformation;
import com.movision.mybatis.newInformation.service.NewInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/5/22 9:12
 */
@Service
public class NewInformationsFacade {

    @Autowired
    private NewInformationService newInformationService;


    /**
     * 更新用户最新消息
     *
     * @param userid
     * @return
     */
    public void updateNewInformtions(Integer userid) {
        //查询用户是否有最新消息记录
        NewInformation sys = newInformationService.queryNewInformationByUserid(userid);
        if (sys != null) {//更新
            Map map = new HashMap();
            map.put("userid", userid);
            map.put("isread", 1);
            map.put("intime", new Date());
            newInformationService.updateNewInformtions(map);
        } else {//新增
            NewInformation news = new NewInformation();
            news.setUserid(userid);
            news.setIsread(1);
            news.setIntime(new Date());
            newInformationService.insertUserByNewInformation(news);
        }
    }

    /**
     * 查询用户最新消息记录
     *
     * @param userid
     * @return
     */
    public NewInformation queryNewInformations(Integer userid) {
        NewInformation res = newInformationService.queryNewInformationByUserid(userid);
        if (res != null) {
            return res;
        } else {
            NewInformation news = new NewInformation();
            news.setUserid(userid);
            news.setIsread(1);
            news.setIntime(new Date());
            newInformationService.insertUserByNewInformation(news);
            NewInformation ress = newInformationService.queryNewInformationByUserid(userid);
            return ress;
        }
    }
}

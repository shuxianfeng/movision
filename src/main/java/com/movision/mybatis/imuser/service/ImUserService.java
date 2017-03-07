package com.movision.mybatis.imuser.service;

import com.movision.mybatis.imuser.entity.ImUser;
import com.movision.mybatis.imuser.mapper.ImUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/7 17:11
 */
@Service
@Transactional
public class ImUserService {

    private static Logger log = LoggerFactory.getLogger(ImUserService.class);

    @Autowired
    private ImUserMapper imUserMapper;

    public int addImUser(ImUser imUser) {
        try {
            log.info("增加im用户");
            return imUserMapper.insertSelective(imUser);
        } catch (Exception e) {
            log.error("增加im用户失败", e);
            throw e;
        }
    }

    public ImUser selectByUserid(Integer id) {
        try {
            log.info("根据用户id查询IM用户失败");
            return imUserMapper.selectByUserid(id);
        } catch (Exception e) {
            log.error("根据用户id查询IM用户失败", e);
            throw e;
        }
    }


}

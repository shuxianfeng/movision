package com.movision.mybatis.city.service;

import com.movision.mybatis.city.entity.City;
import com.movision.mybatis.city.mapper.CityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author zhuangyuhao
 * @Date 2017/9/21 9:49
 */
@Service
public class CityService {

    private static Logger log = LoggerFactory.getLogger(CityService.class);

    @Autowired
    private CityMapper cityMapper;

    public City selectCityByCode(String code) {
        try {
            log.info("根据code查询城市");
            return cityMapper.selectCityByCode(code);
        } catch (Exception e) {
            log.error("根据code查询城市失败", e);
            throw e;
        }
    }
}

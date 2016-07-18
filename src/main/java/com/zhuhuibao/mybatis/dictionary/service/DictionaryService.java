package com.zhuhuibao.mybatis.dictionary.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.dictionary.mapper.DictionaryMapper;
import com.zhuhuibao.mybatis.memCenter.entity.Area;
import com.zhuhuibao.mybatis.memCenter.entity.City;
import com.zhuhuibao.mybatis.memCenter.entity.Province;
import com.zhuhuibao.mybatis.memCenter.mapper.AreaMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.CityMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 数据字典业务处理
 *
 * @author penglong
 */
@Service
@Transactional
public class DictionaryService {

    private static final Logger log = LoggerFactory.getLogger(DictionaryService.class);

    @Autowired
    DictionaryMapper dm;

    @Autowired
    ProvinceMapper provinceMapper;

    @Autowired
    CityMapper cityMapper;

    @Autowired
    AreaMapper areaMapper;


    public String findMailAddress(String memberMail) {
        String mail = "";
        try {
            mail = dm.findMailAddress(memberMail.substring(memberMail.indexOf("@")));
        } catch (Exception ex) {
            log.error("find mail address", ex);
        }
        return mail != null ? mail : "";
    }

    @Cacheable(value = "provinceCode", key = "#provinceCode")
    public Province selectProvinceByCode(String provinceCode) {
        Province province;
        try {

            province = provinceMapper.getProInfo(provinceCode);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("t_dictionary_province select error : " + e.getMessage());
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return province;
    }

    @Cacheable(value = "cityCode", key = "#cityCode")
    public City selectCityByCode(String cityCode) {
        City city;
        try {

            city = cityMapper.getCityInfo(cityCode);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("t_dictionary_city select error : " + e.getMessage());
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return city;
    }

    @Cacheable(value = "mapcityCode", key = "'map_'+#cityCode")
    public Map<String,String> findCityByCode(String cityCode) {
        Map<String,String>  city;
        try {

            city = cityMapper.findCityByCode(cityCode);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("t_dictionary_city select error : " + e.getMessage());
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return city;
    }

    @Cacheable(value = "areaCode", key = "#areaCode")
    public Area selectAreaByCode(String areaCode) {
        Area area;
        try {

            area = areaMapper.getAreaInfo(areaCode);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("t_dictionary_area select error : " + e.getMessage());
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }

        return area;
    }

}

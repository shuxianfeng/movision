package com.movision.mybatis.bossOrders.mapper;

import com.movision.mybatis.area.entity.Area;
import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import com.movision.mybatis.city.entity.City;
import com.movision.mybatis.province.entity.Province;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface BossOrdersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BossOrders record);

    int insertSelective(BossOrders record);

    BossOrders selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BossOrders record);

    int updateByPrimaryKey(BossOrders record);

    List<BossOrdersVo> findAllOrdersByList(RowBounds rowBounds);

    List<BossOrdersVo> queryOrderByCondition(Map map);

    List<BossOrdersVo> findAllAccuracyConditionByOrder(Map map);

    BossOrdersVo queryOrderParticulars(Integer ordernumber);

    List<Integer> findAllProvince();
    List<Province> findAllProvinceName(Integer id);
    Integer findAllProvinceByNum();

    List<Integer> findAllCity();
    List<City> findAllCityName(Integer id);
    Integer findAllCityByNum();

    List<Integer> findAllArea();
    List<Area> findAllAreaName(Integer id);
    Integer findAllAreaByNum();

    int updateInfo();//编辑其他信息

    BossOrders findAllPerInfo(Integer id);//基本信息

 }
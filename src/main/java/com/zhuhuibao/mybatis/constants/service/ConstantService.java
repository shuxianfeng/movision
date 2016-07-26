package com.zhuhuibao.mybatis.constants.service;


import com.zhuhuibao.mybatis.constants.entity.Constant;
import com.zhuhuibao.mybatis.constants.mapper.ConstantMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.JobMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.PositionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConstantService {
    private static final Logger log = LoggerFactory.getLogger(ConstantService.class);

    @Autowired
    ConstantMapper constantMapper;
    @Autowired
    JobMapper jobMapper;
    @Autowired
    PositionMapper positionMapper;

    /**
     * 根据Type Code查询
     *
     * @param code [1..20...]
     * @return
     */
    @Cacheable(value = "constantCache", key = "#type+'_'+#code")
    public Map<String, String> findByTypeCode(String type, String code) {
        log.warn("select by db.....");
        return constantMapper.selectByTypeCode(type, code);
    }


    /**
     * 根据类型查询
     *
     * @param type [1..20...]
     * @return
     */
    @Cacheable(value = "constantCache", key = "'type_'+#type")
    public List<Map<String, String>> findByType(String type) {
        log.warn("select by db.....");
        return constantMapper.selectByType(type);
    }

    /**
     * 新增
     *
     * @param aConstant [entity]
     * @return
     */
    @CachePut(value = "constantCache", key = "#aConstant.id")
    public int insert(Constant aConstant) {
        return constantMapper.insert(aConstant);
    }

    @CachePut(value = "constantCache", key = "#aConstant.id")
    public int insertSelective(Constant aConstant) {
        return constantMapper.insertSelective(aConstant);
    }

    /**
     * 更新
     *
     * @param aConstant [entity]
     * @return
     */
    @CachePut(value = "constantCache", key = "#aConstant.id")
    public int updateByPrimaryKey(Constant aConstant) {
        return constantMapper.updateByPrimaryKey(aConstant);
    }

    @CachePut(value = "constantCache", key = "#aConstant.id")
    public int updateByPrimaryKeySelective(Constant aConstant) {
        return constantMapper.updateByPrimaryKeySelective(aConstant);
    }


    /**
     * 删除
     *
     * @param id [1....]
     * @return
     */
    @CacheEvict(value = "constantCache", key = "#id")
    public int deleteByPrimaryKey(Integer id) {
        return constantMapper.deleteByPrimaryKey(id);
    }


    /**
     * 根据组合code 和 type 查询对应name 组合
     *
     * @param codes code组合 {1,2,3....}
     * @param type  {1.....21}
     * @return names
     */
    public String selectNameByJoinCode(String codes, String type) {
        return constantMapper.selectNameByJoinCode(codes, type);
    }


    /**
     * 根据id查询职位
     * @param id
     * @return
     */
    @Cacheable(value = "positionCache", key = "#id")
    public Map<String, Object> findPositionById(String id) {
        log.warn("select by db.....");
        return positionMapper.findById(id);
    }
}

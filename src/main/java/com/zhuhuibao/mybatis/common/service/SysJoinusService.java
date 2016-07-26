package com.zhuhuibao.mybatis.common.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.common.entity.SysJoinus;
import com.zhuhuibao.mybatis.common.mapper.SysJoinusMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 加盟
 *
 * @author jianglz
 * @since 16/7/26.
 */
@Service
public class SysJoinusService {
    private static final Logger log = LoggerFactory.getLogger(SuggestService.class);


    @Autowired
    SysJoinusMapper joinusMapper;

    public void insert(SysJoinus joinus) {
        int count;
        try {
            count = joinusMapper.insertSelective(joinus);
            if(count != 1){
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL,"插入失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("插入{}失败","t_sys_joinus");
            throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL,"插入失败");
        }
    }

    public List<SysJoinus> findAllJoinusPager(Paging<SysJoinus> pager, Map<String, Object> condition) {
        List<SysJoinus> list;
        try{
            list  = joinusMapper.findAllPager(pager.getRowBounds(),condition);
        }catch (Exception e){
            e.printStackTrace();
            log.error("分页查询[{}]失败","t_sys_joinus");
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL,"查询失败");
        }
        return list;
    }

    public SysJoinus selectById(String id) {
        SysJoinus joinus;
        try{
            joinus = joinusMapper.selectByPrimaryKey(Integer.valueOf(id));

        } catch (Exception e){
            e.printStackTrace();
            log.error("查询[{}]失败","t_sys_joinus");
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL,"查询失败");
        }
        return joinus;
    }

    public void update(SysJoinus joinus) {
        int count;
        try{
             count = joinusMapper.updateByPrimaryKeySelective(joinus);
            if(count != 1){
                throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL,"更新失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("更新[{}]失败","t_sys_joinus");
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL,"更新失败");
        }
    }
}

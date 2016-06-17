package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.memCenter.entity.SuccessCase;
import com.zhuhuibao.mybatis.memCenter.mapper.SuccessCaseMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/6/17 0017.
 */
@Service
@Transactional
public class SuccessCaseService {

    private static final Logger log = LoggerFactory.getLogger(SuccessCaseService.class);

    @Autowired
    private SuccessCaseMapper successCaseMapper;

    public int addSuccessCase(SuccessCase successCase){
        try {
            return successCaseMapper.addSuccessCase(successCase);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Map<String,String>> findAllSuccessCaseList(Paging<Map<String,String>> pager,Map<String, Object> map){
        try {
            return successCaseMapper.findAllSuccessCaseList(pager.getRowBounds(),map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public SuccessCase querySuccessCaseById(String id){
        try {
            return successCaseMapper.querySuccessCaseById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public int updateSuccessCase(SuccessCase successCase){
        try {
            return successCaseMapper.updateSuccessCase(successCase);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

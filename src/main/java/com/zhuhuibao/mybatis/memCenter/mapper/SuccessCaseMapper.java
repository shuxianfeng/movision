package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.SuccessCase;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/6/17 0017.
 */
public interface SuccessCaseMapper {

    int addSuccessCase(SuccessCase successCase);

    List<Map<String,String>> findAllSuccessCaseList(RowBounds rowBounds,Map<String, Object> map);

    SuccessCase querySuccessCaseById(String id);

    int updateSuccessCase(SuccessCase successCase);
}

package com.zhuhuibao.mybatis.oms.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.zhuhuibao.mybatis.oms.entity.MemberSucCase;
/**
 * 成功案例接口
 * @author Administrator
 *
 */
public interface MemberSucCaseMapper {
    //查询成功案例
	List findAllSucCaseList(RowBounds rowBounds, Map<String, String> map);
    //修改成功案例
	void updateByPrimaryKeySelective(MemberSucCase memberSucCase);
	//查询成功案例详情
	MemberSucCase selectByPrimaryKey(String id); 
}

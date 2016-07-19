package com.zhuhuibao.mybatis.oms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.oms.entity.ComplainSuggest;
import com.zhuhuibao.mybatis.oms.entity.MemberSucCase;
import com.zhuhuibao.mybatis.oms.mapper.MemberSucCaseMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
/**
 *成功案例service
 * @author Administrator
 *
 */
@Service
@Transactional
public class MemberSucCaseService {
	
	private static final Logger log = LoggerFactory.getLogger(ComplainSuggestService.class);
	
	@Autowired
	MemberSucCaseMapper memberSucCaseMapper;
	 /**
     * 查询成功案例
     * @author gmli
     * @param pager 分页
     * @param map 参数
     * @return List
     */
	public List findAllSucCaseList(Paging pager, String title,
			String status) {
		List sucList=null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", title);
		map.put("status", status);
		try {
			sucList = memberSucCaseMapper.findAllSucCaseList(
					pager.getRowBounds(), map);
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return sucList;
	}
	/**
	 * 修改成功案例
	 * @param memberSucCase
	 */
	public void updateSucCase(MemberSucCase memberSucCase) {
		 
		try {
			memberSucCaseMapper.updateByPrimaryKeySelective(memberSucCase);
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 查询成功案例详情
	 * @param id
	 * @return
	 */
	public MemberSucCase queryMemberSucCase(String id) {
		MemberSucCase memberSucCase = null;
		try {
			memberSucCase = memberSucCaseMapper.selectByPrimaryKey(id);
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return memberSucCase;
	}

}

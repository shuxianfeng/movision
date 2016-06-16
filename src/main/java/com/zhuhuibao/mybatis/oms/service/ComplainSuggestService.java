package com.zhuhuibao.mybatis.oms.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.oms.entity.ComplainSuggest;
import com.zhuhuibao.mybatis.oms.mapper.ComplainSuggestMapper;
import com.zhuhuibao.utils.pagination.model.Paging;

@Service
@Transactional
public class ComplainSuggestService {
	private static final Logger log = LoggerFactory.getLogger(ComplainSuggestService.class);
	
	@Autowired
	ComplainSuggestMapper suggestMapper;
	
	public int insert(ComplainSuggest cs)
	{
		log.info("insert complain suggest");
		int result =0;
		try
		{
			result = suggestMapper.insert(cs);
		}
		catch(Exception e)
		{
			log.error("insert complain suggest error!",e);
		}
		return result;
	}
    /**
     * 查询用户建议
     * @author gmli
     * @param pager 分页
     * @param map 参数
     * @return List
     */
	public List<ComplainSuggest> findAllComplaintSuggest(Paging<ComplainSuggest> pager,
			Map<String, Object> map) {
		List<ComplainSuggest> complainSuggestList=null;
		try {
			complainSuggestList = suggestMapper.findAllComplaintSuggest(
					pager.getRowBounds(), map);
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return complainSuggestList;
	}
	/**
	 * 用户建议处理
	 * @author gmli
	 * @param complainSuggest
	 */
	public void updateComplainSuggest(ComplainSuggest complainSuggest) {
		try {
			suggestMapper.updateByPrimaryKeySelective(complainSuggest);
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 用户建议详情
	 * @author gmli
	 * @param id
	 * @return
	 */
	public ComplainSuggest queryComplainSuggestById(Long id) {
		ComplainSuggest complainSuggest = null;
		try {
			complainSuggest = suggestMapper.selectByPrimaryKey(id);
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return complainSuggest;
	}
}

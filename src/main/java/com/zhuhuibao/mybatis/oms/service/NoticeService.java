package com.zhuhuibao.mybatis.oms.service;

import com.zhuhuibao.mybatis.oms.entity.Notice;
import com.zhuhuibao.mybatis.oms.mapper.NoticeMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 平台公告service
 * 
 * @author Administrator
 * @since 2016.05.26
 *
 */
@Service
@Transactional
public class NoticeService {
	
	private static final Logger log = LoggerFactory.getLogger(ComplainSuggestService.class);
	@Autowired
	NoticeMapper noticeMapper;
	
	public int insert(Notice notice)
	{
		log.info("insert Notice ");
		int result =0;
		try
		{
			result = noticeMapper.insert(notice);
		}
		catch(Exception e)
		{
			log.error("insert Notice  error!",e);
		}
		return result;
	}
	/**
	 * 公告信息修改
	 * 
	 * @param notice
	 */
	public void updatePlatformNotice(Notice notice) {
		try {
			noticeMapper.updateByPrimaryKeySelective(notice);
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 平台公告查询
	 * 
	 * @param pager
	 * @param map
	 * @return
	 */
	public List<Notice> findAllPlatformNotice(Paging<Notice> pager,
			Map<String, Object> map) {
		List<Notice> noticeList=null;
		try {
			noticeList = noticeMapper.findAllNotice(
					pager.getRowBounds(), map);
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return noticeList;
	}

	/**
	 * 查询公告详情
	 * @param id
	 * @return
	 */
	public Notice queryPlatformNoticeById(Long id) {
		Notice notice = null;
		try {
			notice = noticeMapper.selectByPrimaryKey(id);
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return notice;
	}
	/**
	 * 发布公告
	 * @param notice
	 */
	public void publishNotice(Notice notice) {
		log.info("publish Notice ");
		try
		{
			noticeMapper.publishNotice(notice);
		}
		catch(Exception e)
		{
			log.error("publish Notice Notice  error!",e);
		}
	}

}

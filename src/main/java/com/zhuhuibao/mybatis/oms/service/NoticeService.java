package com.zhuhuibao.mybatis.oms.service;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MessageTextConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.oms.entity.Notice;
import com.zhuhuibao.mybatis.oms.mapper.NoticeMapper;
import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
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

	@Autowired
	SiteMailService siteMailService;
	/**
	 * 公告信息修改
	 * 
	 * @param notice
	 */
	public void updatePlatformNotice(Notice notice) {
		try {
			noticeMapper.updateByPrimaryKeySelective(notice);
			if("3".equals(notice.getStatus()))
			{
				siteMailService.addRefuseReasonMail(ShiroUtil.getOmsCreateID(),notice.getCreateId(),
						notice.getReason(), MessageTextConstant.NOTICE, notice.getTitle());
			}
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
	/**
	 * 查询平台最新公告
	 */
	public List<Notice> queryPlatformNewNotice() {
		log.info("publish Notice ");
		List<Notice> newNoticeList=null;
		try
		{
			newNoticeList=noticeMapper.queryPlatformNewNotice();
		}
		catch(Exception e)
		{
			log.error("publish Notice Notice  error!",e);
		}
		return newNoticeList;
	}

}

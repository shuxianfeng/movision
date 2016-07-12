package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.mybatis.oms.entity.Notice;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;
/**
 * 接口
 * @author Administrator
 *
 */
public interface NoticeMapper {
	int deleteByPrimaryKey(Long id);

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Notice record);

	List<Notice> findAllNotice(RowBounds rowBounds, Map<String, Object> map);
    //公告发布
	void publishNotice(Notice notice);
    //查询凭条最新公告
	List<Notice> queryPlatformNewNotice();

    List<Map<String,String>> queryNoticeList(Map<String, Object> map);

    List<Map<String,String>> findAllNoticeList(RowBounds rowBounds);

    Map<String,String> queryNoticeById(String id);
}

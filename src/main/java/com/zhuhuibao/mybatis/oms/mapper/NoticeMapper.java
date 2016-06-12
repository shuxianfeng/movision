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

    int insert(Notice record);

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Notice record);

    int updateByPrimaryKey(Notice record);

	List<Notice> findAllNotice(RowBounds rowBounds, Map<String, Object> map);

	void publishNotice(Notice notice);
}

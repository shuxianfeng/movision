package com.movision.mybatis.searchPostRecord.service;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.exception.BusinessException;
import com.movision.mybatis.searchPostRecord.entity.SearchPostRecord;
import com.movision.mybatis.searchPostRecord.mapper.SearchPostRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/4/10 17:14
 */
@Service
@Transactional
public class SearchPostRecordService {

    private static Logger log = LoggerFactory.getLogger(SearchPostRecordService.class);

    @Autowired
    private SearchPostRecordMapper searchPostRecordMapper;

    public int addSingleRecord(SearchPostRecord searchPostRecord) {
        try {
            log.info("插入搜索帖子记录表一条记录");
            searchPostRecordMapper.insertSelective(searchPostRecord);
            return searchPostRecord.getId();
        } catch (Exception e) {
            log.error("插入搜索帖子记录表一条记录失败", e);
            throw e;
        }
    }

    /**
     * 插入搜索帖子记录表一条记录
     *
     * @param keyword
     */
    public void add(String keyword) {
        SearchPostRecord searchPostRecord = new SearchPostRecord();
        searchPostRecord.setUserid(ShiroUtil.getAppUserID());
        searchPostRecord.setKeyword(keyword);
        int id = this.addSingleRecord(searchPostRecord);
        if (id <= 0) {
            throw new BusinessException(MsgCodeConstant.add_search_post_record_fail, "新增搜索帖子记录失败");
        }
    }

    public List<Map<String, Object>> selectPostSearchHotWord() {
        try {
            log.info("查询帖子搜索热门搜索词");
            return searchPostRecordMapper.selectPostSearchHotWord();
        } catch (Exception e) {
            log.error("查询帖子搜索热门搜索词失败", e);
            throw e;
        }
    }

    public List<Map<String, Object>> selectHistoryRecord(int userid) {
        try {
            log.info("查询帖子搜索历史搜索词");
            return searchPostRecordMapper.selectHistoryRecord(userid);
        } catch (Exception e) {
            log.error("查询帖子搜索历史搜索词失败", e);
            throw e;
        }
    }


}

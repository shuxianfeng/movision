package com.movision.mybatis.searchGoodsRecord.service;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.exception.BusinessException;
import com.movision.mybatis.searchGoodsRecord.entity.SearchGoodsRecord;
import com.movision.mybatis.searchGoodsRecord.mapper.SearchGoodsRecordMapper;
import com.movision.mybatis.searchPostRecord.entity.SearchPostRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/4/10 19:10
 */
@Service
@Transactional
public class SearchGoodsRecordService {

    private static Logger log = LoggerFactory.getLogger(SearchGoodsRecordService.class);

    @Autowired
    private SearchGoodsRecordMapper searchGoodsRecordMapper;

    public int addSingleRecord(SearchGoodsRecord searchGoodsRecord) {
        try {
            log.info("插入搜索商品记录表一条记录");
            searchGoodsRecordMapper.insertSelective(searchGoodsRecord);
            return searchGoodsRecord.getId();
        } catch (Exception e) {
            log.error("插入搜索商品记录表一条记录失败", e);
            throw e;
        }
    }

    /**
     * 插入搜索帖子记录表一条记录
     *
     * @param keyword
     */
    public void add(String keyword) {

        SearchGoodsRecord searchGoodsRecord = new SearchGoodsRecord();
        searchGoodsRecord.setUserid(ShiroUtil.getAppUserID());
        searchGoodsRecord.setKeyword(keyword);
        int id = this.addSingleRecord(searchGoodsRecord);

        if (id <= 0) {
            throw new BusinessException(MsgCodeConstant.add_search_good_record_fail, "新增搜索商品记录失败");
        }
    }

    public List<Map<String, Object>> selectPostSearchHotWord() {
        try {
            log.info("查询商品搜索热门搜索词");
            return searchGoodsRecordMapper.selectGoodsSearchHotWord();
        } catch (Exception e) {
            log.error("查询商品搜索热门搜索词失败", e);
            throw e;
        }
    }

    public List<Map<String, Object>> selectHistoryRecord(int userid) {
        try {
            log.info("查询商品搜索历史搜索词");
            return searchGoodsRecordMapper.selectGoodsHistoryRecord(userid);
        } catch (Exception e) {
            log.error("查询商品搜索历史搜索词失败", e);
            throw e;
        }
    }

    public Integer updateSearchIsdel(Integer userid) {
        try {
            log.info("清楚搜索记录");
            return searchGoodsRecordMapper.updateSearchIsdel(userid);
        } catch (Exception e) {
            log.error("清楚搜索记录失败");
            throw e;
        }
    }

}

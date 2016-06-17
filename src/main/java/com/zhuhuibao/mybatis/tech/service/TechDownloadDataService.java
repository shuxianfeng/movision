package com.zhuhuibao.mybatis.tech.service;

import com.zhuhuibao.mybatis.tech.entity.TechDownLoadData;
import com.zhuhuibao.mybatis.tech.mapper.TechDownLoadDataMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *我下载的资料
 *@author pl
 *@create 2016/5/31 0031
 **/
@Service
@Transactional
public class TechDownloadDataService {

    private final static Logger log = LoggerFactory.getLogger(TechDownloadDataService.class);

    @Autowired
    TechDownLoadDataMapper dlMapper;

    /**
     * 插入我下载的数据
     * @param dataId 下载的技术资料ID
     * @param  createId 下载人ID
     * @return
     */
    public int insertDownloadData(String dataId,Long createId)
    {
        log.info("insert download dataId "+dataId+" createId = "+createId);
        int result;
        try{
            TechDownLoadData dlData = new TechDownLoadData();
            dlData.setCreateId(Long.parseLong(dataId));
            dlData.setDataId(createId);
            result = dlMapper.insertSelective(dlData);
        }catch(Exception e)
        {
            log.error("insert download data error!",e);
            throw e;
        }
        return result;
    }

    /**
     * 我下载的资料
     * @param map 查看条件
     * @return
     */
    public List<Map<String,Object>> findAllDownloadData(Paging<Map<String,Object>> pager,Map<String,String> map)
    {
        log.info("find all download data "+ StringUtils.mapToString(map));
        List<Map<String,Object>> techList = null;
        try{
            techList = dlMapper.findAllDownloadData(pager.getRowBounds(),map);
        }catch(Exception e)
        {
            log.error("find all tech cooperation for pager error!",e);
            throw e;
        }
        return techList;
    }
}

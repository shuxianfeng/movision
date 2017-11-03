package com.movision.mybatis.activePart.service;

import com.movision.mybatis.activePart.entity.ActivePart;
import com.movision.mybatis.activePart.entity.ActivePartList;
import com.movision.mybatis.activePart.mapper.ActivePartMapper;
import com.movision.mybatis.post.service.PostService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/13 16:14
 */
@Service
@Transactional
public class ActivePartService {
    private static Logger log = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private ActivePartMapper activePartMapper;
    /**
     * 后台管理-查询活动报名列表
     * @param pager
     * @return
     */
    public List<ActivePartList> queryPostCallActive(Paging<ActivePartList> pager, Integer postid) {

        try{
            log.info("查询报名列表成功");
            return activePartMapper.findAllCllActive(pager.getRowBounds(), postid);
        }catch (Exception e){
            log.error("查询报名列表失败", e);
            throw  e;
        }
    }

    public int insertSelective(ActivePart activePart) {
        try {
            log.info("插入报名记录");
            return activePartMapper.insertSelective(activePart);
        } catch (Exception e) {
            log.error("插入报名记录失败", e);
            throw e;
        }
    }

    public void addRecord(String activeid, Integer userid) {
        if (StringUtils.isNotBlank(activeid)) {
            ActivePart activePart = new ActivePart();
            activePart.setPostid(Integer.valueOf(activeid));
            activePart.setUserid(userid);
            insertSelective(activePart);
        }
    }

}

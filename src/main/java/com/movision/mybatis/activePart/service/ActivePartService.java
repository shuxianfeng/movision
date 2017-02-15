package com.movision.mybatis.activePart.service;

import com.movision.mybatis.activePart.entity.ActivePartList;
import com.movision.mybatis.activePart.mapper.ActivePartMapper;
import com.movision.mybatis.post.entity.PostActiveList;
import com.movision.mybatis.post.mapper.PostMapper;
import com.movision.mybatis.post.service.PostService;
import com.movision.utils.pagination.model.Paging;
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
    private PostMapper postMapper;
    @Autowired
    private ActivePartMapper activePartMapper;
    /**
     * 后台管理-查询活动报名列表
     * @param pager
     * @return
     */
    public List<ActivePartList> queryPostCallActive(Paging<ActivePartList> pager){

        try{
            log.info("查询报名列表成功");
            return activePartMapper.findAllCllActive(pager.getRowBounds());
        }catch (Exception e){
            log.error("查询报名列表失败");
            throw  e;
        }
    }
}

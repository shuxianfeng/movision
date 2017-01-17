package com.movision.facade.index;

import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/17 16:05
 */
@Service
public class FacadeIndex {

    @Autowired
    private PostMapper postMapper;

    public List<PostVo> queryIndexData() {
        return postMapper.queryIndexData();
    }
}

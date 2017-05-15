package com.movision.facade.circle;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.MyCircle;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.post.service.PostService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 15:05
 */
@Service
public class CircleAppFacade {
    @Autowired
    private CircleService circleService;

    @Autowired
    private PostService postService;

    public List<MyCircle> findAllMyFollowCircleList(Paging<MyCircle> paging, int userid) {
        Map map = new HashedMap();
        map.put("userid", userid);
        List<MyCircle> list = circleService.findAllMyFollowCircleList(paging, map);

        for (MyCircle circle : list) {
            int circleid = circle.getId();
            int postnum = postService.queryPostNumByCircleid(circleid);
            circle.setPostnewnum(postnum);
        }
        return list;
    }
}

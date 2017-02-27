package com.movision.facade.circle;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.service.CircleService;
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

    public List<Circle> findAllMyFollowCircleList(Paging<Circle> paging, int userid) {
        Map map = new HashedMap();
        map.put("userid", userid);
        return circleService.findAllMyFollowCircleList(paging, map);
    }
}

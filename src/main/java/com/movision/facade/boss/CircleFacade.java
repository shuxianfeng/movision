package com.movision.facade.boss;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/2/8 17:56
 */
@Service
public class CircleFacade {
    @Autowired
    CircleService circleService;

    public List<CircleVo> queryCircleByList(String pageNo, String pageSize) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<CircleVo> pager = new Paging<CircleVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<CircleVo> list = circleService.queryCircleByList(pager);//获取圈子列表部分数据
        List<CircleVo> circleVoslist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CircleVo vo = new CircleVo();
            Integer circleid = list.get(i).getId();
            String circlemaster = circleService.queryCircleBycirclemaster(list.get(i).getPhone());//查询圈主
            List<User> circlemanagerlist = circleService.querycirclemanagerlist(circleid);//查询圈子管理员
            /*Integer follow*/
            circleVoslist.add(vo);
        }
        return circleVoslist;
    }
}

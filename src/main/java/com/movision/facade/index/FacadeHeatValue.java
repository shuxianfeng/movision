package com.movision.facade.index;

import com.movision.common.constant.HeatValueConstant;
import com.movision.mybatis.post.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhanglei
 * @Date 2017/7/11 11:05
 */
@Service
public class FacadeHeatValue {
    private static Logger log = LoggerFactory.getLogger(FacadeHeatValue.class);

    @Autowired
    private PostService postService;

    /**
     * 增加帖子热度值
     */
    public int addHeatValue(int postid, int type) {
        int new_point = 0;
        if (type == 1) {//首页精选
            int isessence = postService.queryPostIsessenceHeat(postid);
            if (isessence == 1) {//首页精选
                new_point = HeatValueConstant.POINT.home_page_selection.getCode();
                postService.updatePostHeatValue(new_point, postid);
            }
        } else if (type == 2) {//帖子设为圈子精选


        } else if (type == 3) {//发帖人级别


        } else if (type == 4) {//点赞

        } else if (type == 5) {//评论

        } else if (type == 6) {//转发

        } else if (type == 7) {//收藏


        }
        return new_point;
    }
}

package com.movision.facade.label;

import com.movision.facade.index.FacadePost;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.followLabel.entity.FollowLabel;
import com.movision.mybatis.followLabel.service.FollowLabelService;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabel.entity.PostLabelCount;
import com.movision.mybatis.postLabel.entity.PostLabelTz;
import com.movision.mybatis.postLabel.service.PostLabelService;
import com.movision.mybatis.postLabelRelation.service.PostLabelRelationService;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/7/25 14:47
 */
@Service
public class LabelFacade {
    private static Logger log = LoggerFactory.getLogger(LabelFacade.class);
    @Autowired
    private PostLabelService postLabelService;
    @Autowired
    private PostLabelRelationService postLabelRelationService;
    @Autowired
    private FollowLabelService followLabelService;
    @Autowired
    private FacadePost facadePost;

    /**
     * 点击标签页上部分
     *
     * @param labelid
     * @return
     */
    public PostLabelTz labelPage(int labelid) {
        //查询头像和名称
        PostLabelTz postLabel = postLabelService.queryName(labelid);
        //根据id查询帖子数量
        int count = postLabelRelationService.labelPost(labelid);
        postLabel.setCount(count);
        return postLabel;
    }


    /**
     * 点击标签页下部分
     *
     * @param type
     * @param paging
     * @param labelid
     * @return
     */
    public List postLabelList(int type, Paging<PostVo> paging, int labelid) {
        List list = null;
        //根据标签id查询帖子
        List<Integer> postid = postLabelRelationService.postList(labelid);
        if (type == 1) {//推荐
            //根据所有的id查询帖子按热度排序
            list = postLabelRelationService.postHeatValue(postid, paging);
            list = labelResult(list);
        } else if (type == 2) {//最新
            //根据所有的id查询帖子按时间排序
            list = postLabelRelationService.post(postid, paging);
            list = labelResult(list);
        } else if (type == 3) {//精华
            list = postLabelRelationService.postIseecen(postid, paging);
            list = labelResult(list);
        }
        return list;
    }


    /**
     * 关注标签
     *
     * @param userid
     * @param labelid
     * @return
     */
    public int attentionLabel(int userid, int labelid) {
        FollowLabel followLabel = new FollowLabel();
        followLabel.setUserid(userid);
        followLabel.setLabelid(labelid);
        followLabel.setIntime(new Date());
        int result = followLabelService.insertSelective(followLabel);
        return result;
    }

    /**
     * 返回数据
     *
     * @param
     * @param
     * @return
     */
    public List labelResult(List<PostVo> list) {
        if (list != null) {
            facadePost.findUser(list);
            facadePost.findPostLabel(list);
            facadePost.findHotComment(list);
            facadePost.countView(list);
        }
        return list;
    }


    /**
     * 标签达人
     *
     * @param labelid
     * @return
     */
    public List tagMan(int labelid) {
        //根据id查询用的最多的用户
        List<PostLabelCount> labelCounts = postLabelService.queryCountLabelName(labelid);
        return labelCounts;
    }


    /**
     * 圈子标签上半部分
     *
     * @param postid
     * @return
     */
    public CircleVo queryCircleByPostid(String postid) {
        //根据帖子id查询圈子
        CircleVo circleVo = postLabelService.queryCircleByPostid(Integer.parseInt(postid));
        //根据圈子查询今日这个圈子的发帖数
        int circleid = circleVo.getId();
        int todayPost = postLabelService.postInCircle(circleid);
        circleVo.setTodayPost(todayPost);
        //查询圈子下所有帖子用的标签
        List<PostLabel> postLabels = postLabelService.queryLabelCircle(circleid);
        circleVo.setPostLabels(postLabels);
        return circleVo;
    }


}

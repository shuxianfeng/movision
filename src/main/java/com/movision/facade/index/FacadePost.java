package com.movision.facade.index;

import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/19 15:43
 */
@Service
public class FacadePost {

    @Autowired
    private PostService postService;

    public PostVo queryPostDetail(String postid, String type) {
        PostVo vo = postService.queryPostDetail(Integer.parseInt(postid));
        if (type.equals("1")) {
            String url = postService.queryVideoUrl(Integer.parseInt(postid));
            vo.setVideourl(url);
        }
        return vo;
    }

    public Map<String, Object> queryPastPostDetail(String date) {
        Map<String, Object> parammap = new HashMap();
        Map<String, Object> map = new HashMap();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (!StringUtils.isEmpty(date)) {
            parammap.put("date", date);
        } else {
            parammap.put("date", sdf.format(d));
        }
        parammap.put("days", 0);
        List<PostVo> currentList = postService.queryPastPostList(parammap);//选择的日期这天
        parammap.put("days", 1);
        List<PostVo> dayagoList = postService.queryPastPostList(parammap);//选择的日期前一天
        parammap.put("days", 2);
        List<PostVo> twodayagoList = postService.queryPastPostList(parammap);//选择的日期前两天

        map.put("currentList", currentList);
        map.put("dayagoList", dayagoList);
        map.put("twodayagoList", twodayagoList);
        return map;
    }

    public List<PostVo> queryCircleIndex2(String pageNo, String pageSize, String circleid) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Post> pager = new Paging<Post>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));

        return postService.queryPostList(pager, circleid);
    }


    public int releasePost(String circleid, String title, String postcontent, String isactive) {
        Post post = new Post();
        post.setCircleid(Integer.parseInt(circleid));
        post.setTitle(title);
        post.setPostcontent(postcontent);
        post.setZansum(0);//新发帖全部默认为0次
        post.setCommentsum(0);//被评论次数
        post.setForwardsum(0);//被转发次数
        post.setCollectsum(0);//被收藏次数
        post.setIsactive(Integer.parseInt(isactive));//是否为活动 0 帖子 1 活动
        post.setType(0);//帖子类型 0 普通帖 1 原生优质帖
        post.setIshot(0);//是否设为热门：默认0否
        post.setIsessence(0);//是否设为精选：默认0否
        post.setIsessencepool(0);//是否设为精选池中的帖子
        post.setIntime(new Date());//帖子发布时间
        post.setTotalpoint(0);//帖子综合评分
        post.setIsdel(0);//上架

        return postService.releasePost(post);
    }


    public int updatePostByZanSum(String id) {
        int type = postService.updatePostByZanSum(Integer.parseInt(id));
        if (type == 1) {
            return postService.queryPostByZanSum(Integer.parseInt(id));
        }
        return -1;
    }
}

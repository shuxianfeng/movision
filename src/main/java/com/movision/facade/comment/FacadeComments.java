package com.movision.facade.comment;

import com.movision.facade.index.FacadePost;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.post.service.PostService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhurui
 * @Date 2017/1/22 14:34
 */
@Service
public class FacadeComments {

    @Autowired
    public CommentService CommentService;
    @Autowired
    public FacadePost facadePost;
    @Autowired
    public PostService postService;

    /**
     * 帖子评论列表（二级）
     *
     * @param postid
     * @return
     */
    public List<CommentVo> queryCommentsByLsit(Paging<CommentVo> pager, String postid) {

        List<CommentVo> vo = CommentService.queryCommentsByLsit(pager, postid);
        List<CommentVo> resaultvo=new ArrayList();
        for (int i=0;i<vo.size();i++){//遍历所有帖子
            if (vo.get(i).getPid() == null) {//当评论没有子评论的时候（父评论）
                 for(int j=0;j<vo.size();j++) {//遍历所有帖子
                    if (vo.get(i).getId()==vo.get(j).getPid()){//当子评论id和父评论的id相等时，表示该子评论是这个父评论的
                        CommentVo cv=vo.get(j);//这条子评论
                        List<CommentVo> list=new ArrayList();
                        list.add(vo.get(i));//把父评论暂存在List中
                        cv.addVo(list);//把父评论存放在子评论字段中
                 }
                }
            }
        }
        return vo;
    }

    public int updateCommentZanSum(String commentid, String userid) {
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("commentid", Integer.parseInt(commentid));
        parammap.put("intime", new Date());
        CommentService.insertCommentZanRecord(parammap);
        CommentService.updateCommentZanSum(Integer.parseInt(commentid));//更新评论点赞次数
        int sum = CommentService.queryCommentZanSum(Integer.parseInt(commentid));//查询点赞次数
        return sum;
    }

    public int insertComment(String userid, String content, String fuid, String postid) {
        if (content.length() > 1000) {
            return 2;
        } else {
            int type = 0;
            if (fuid == null) {//父id为空 表示此评论是父评论
                CommentVo vo = new CommentVo();
                vo.setContent(content);
                vo.setPostid(Integer.parseInt(postid));
                vo.setUserid(Integer.parseInt(userid));
                vo.setIntime(new Date());
                vo.setZansum(0);
                type = CommentService.insertComment(vo);//添加评论
                postService.updatePostBycommentsum(Integer.parseInt(postid));//更新帖子表的评论次数字段
            } else {//表示是其他评论的子评论，不算评论次数
                CommentVo vo = new CommentVo();
                vo.setContent(content);
                vo.setPostid(Integer.parseInt(postid));
                vo.setUserid(Integer.parseInt(userid));
                vo.setIntime(new Date());
                vo.setZansum(0);
                vo.setPid(Integer.parseInt(fuid));
                type = CommentService.insertComment(vo);//添加评论
            }
            return type;
        }
    }
}

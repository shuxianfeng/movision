package com.movision.facade.comment;

import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/1/22 14:34
 */
@Service
public class FacadeComments {

    @Autowired
    public CommentService CommentService;

    public List<CommentVo> queryCommentsByLsit(String pageNo, String pageSize, String postid){
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<CommentVo> pager = new Paging<CommentVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<CommentVo> vo=CommentService.queryCommentsByLsit(postid,pager);
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
}

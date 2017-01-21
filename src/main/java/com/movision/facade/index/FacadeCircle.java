package com.movision.facade.index;

import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.circleCategory.entity.CircleCategoryVo;
import com.movision.mybatis.circleCategory.service.CircleCategoryService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/20 20:36
 */
@Service
public class FacadeCircle {

    @Autowired
    private CircleService circleService;

    @Autowired
    private CircleCategoryService circleCategoryService;

    @Autowired
    private PostService postService;

    public CircleVo queryCircleIndex1(int circleid) {

        CircleVo circleVo = circleService.queryCircleIndex1(circleid);//查询圈子详情基础数据

        List<Post> postList = postService.queryCircleSubPost(circleid);//查询这个圈子中被设为热帖的5个帖子

        circleVo.setHotPostList(postList);

        return circleVo;

    }

    public List<CircleCategoryVo> queryCircleCategoryList() {

        List<CircleCategoryVo> categoryList = circleCategoryService.queryCircleByCategory();//查询所有圈子的分类

        //递归遍历，查询所有分类下的所有圈子列表放入Vo
        for (int i = 0; i < categoryList.size(); i++) {
            int categoryid = categoryList.get(i).getId();
            List<CircleVo> circlelist = circleService.queryCircleByCategory(categoryid);

            //递归遍历查询当前圈子中总共包含的帖子数量
            for (int j = 0; j < circlelist.size(); j++) {
                int circleid = circlelist.get(j).getId();
                int postnum = postService.queryPostNumByCircleid(circleid);

                //将帖子数量加入圈子对象CircleVo中
                circlelist.get(j).setPostnum(postnum);
            }

            //将圈子列表加入分类对象CircleCategoryVo中
            categoryList.get(i).setCircleList(circlelist);


        }

        //手动将待审核的圈子全部set到返回的对象中
        CircleCategoryVo circleCategoryVo = new CircleCategoryVo();
        circleCategoryVo.setId(-1);//categoryid为-1时查询待审核的圈子
        circleCategoryVo.setCategoryname("待审核");
        List<CircleVo> list = circleService.queryAuditCircle();
        circleCategoryVo.setCircleList(list);
        categoryList.add(circleCategoryVo);

        return categoryList;

    }
}

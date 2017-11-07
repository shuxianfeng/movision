package com.movision.facade.index;

import com.google.gson.Gson;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.test.SpringTestCase;
import com.movision.utils.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/7/25 17:35
 */
public class FacadePostTest extends SpringTestCase {

    @Autowired
    private FacadePost facadePost;

    @Autowired
    private PostService postService;

    @Test
    public void copyPost50() throws Exception {
        //获取原始的帖子
        Post originPost = postService.queryxiaojijiPostForTest("我爱小姐姐");
        for (int i = 0; i < 10; i++) {
            Post post = new Post();
            //这三个字段是变化的
            post.setTitle("我爱小姐姐_" + i); //帖子标题
            post.setHeatvalue(originPost.getHeatvalue() + (i + 1) * 1000);    //热度
            //下面是不变化的
            setXiaojijiAttribute(originPost, post);

            postService.insertPost(post);
        }
    }

    private void setXiaojijiAttribute(Post originPost, Post newPost) {

        newPost.setUserid(originPost.getUserid());
        newPost.setCircleid(originPost.getCircleid());
        newPost.setSubtitle(originPost.getSubtitle());
        newPost.setPostcontent(originPost.getPostcontent());
        newPost.setZansum(originPost.getZansum());
        newPost.setCommentsum(originPost.getCommentsum());
        newPost.setForwardsum(originPost.getForwardsum());
        newPost.setCollectsum(originPost.getCollectsum());
        newPost.setIsactive(originPost.getIsactive());
        newPost.setType(originPost.getType());
        newPost.setIshot(originPost.getIshot());
        newPost.setIsessence(originPost.getIsessence());
        newPost.setOrderid(originPost.getOrderid());
        newPost.setCoverimg(originPost.getCoverimg());
        newPost.setHotimgurl(originPost.getHotimgurl());
        newPost.setIntime(originPost.getIntime());
        newPost.setTotalpoint(originPost.getTotalpoint());
        newPost.setIsdel(originPost.getIsdel());
        newPost.setIsessencepool(originPost.getIsessencepool());
        newPost.setEssencedate(originPost.getEssencedate());
        newPost.setOprtime(originPost.getOprtime());
        newPost.setIsheatoperate(originPost.getIsheatoperate());
        newPost.setActiveid(originPost.getActiveid());
        newPost.setCity(originPost.getCity());
    }


    @Test
    public void getCircleInCatagory() throws Exception {
        facadePost.getCircleInCatagory();
    }


    @Test
    public void releaseModularPost() throws Exception {

        String userid = "326";  //18051989558
        String circleid = "45"; //南林

        List<PostLabel> list = new ArrayList<>();

        list.add(createPostLabel(userid, "ceshi_1_"));
        list.add(createPostLabel(userid, "ceshi_2_"));
        list.add(existLabel());

        Gson gson = new Gson();
        String labelList = gson.toJson(list);
        System.out.println("labelList=" + labelList);

//        facadePost.releaseModularPost(null, userid, circleid, null, null, null, null, null, labelList, null);
    }

    private PostLabel createPostLabel(String userid, String namePrefix) {
        PostLabel p1 = new PostLabel();

        p1.setName(namePrefix + DateUtils.getCurrentDate());
        p1.setUserid(Integer.valueOf(userid));
        p1.setType(3);
        p1.setPhoto("");
        p1.setCitycode("320100");

        return p1;
    }

    private PostLabel existLabel() {
        PostLabel p = new PostLabel();
        p.setId(1);
        p.setName("活动");
        p.setType(1);
        return p;
    }

}
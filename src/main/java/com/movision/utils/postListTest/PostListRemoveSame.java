package com.movision.utils.postListTest;

import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postDes.service.PostDesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 这个只是一个demo，用于比较两个泛型一样的list,去掉重复的对象
 *
 * @Author zhuangyuhao
 * @Date 2017/6/27 9:44
 */
@Service
public class PostListRemoveSame {

    private static Logger log = LoggerFactory.getLogger(PostListRemoveSame.class);

    @Autowired
    private PostService postService;

    @Autowired
    private PostDesService postDesService;

    public void getList() {
        log.debug("[1]" + new Date().getTime());
        List<Post> postList = postService.selectAllPost();  //大
        log.debug("postlist:" + postList.toString());
        log.debug("[size-postlist]:" + postList.size());

        List<Post> desList = postDesService.selectAll(); //小
        log.debug("deslist:" + desList.toString());
        log.debug("[size-deslist] : " + desList.size());

        postList.removeAll(desList);
        log.debug("[2]" + new Date().getTime());

        log.debug("最后的postlist:" + postList.toString());    //大的里面剩下的，去掉了小的。
        log.debug("[size-postlist]:" + postList.size());

    }
}

package com.movision.facade.circle;

import com.mongodb.DBObject;
import com.movision.facade.index.FacadePost;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circle.entity.MyCircle;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private PostService postService;

    @Autowired
    private FacadePost facadePost;

    public List<MyCircle> findAllMyFollowCircleList(Paging<MyCircle> paging, int userid) {
        Map map = new HashedMap();
        map.put("userid", userid);
        List<MyCircle> list = circleService.findAllMyFollowCircleList(paging, map);

        for (MyCircle circle : list) {
            int circleid = circle.getId();
            int postnum = postService.queryPostNumByCircleid(circleid);
            circle.setPostnewnum(postnum);
        }
        return list;
    }

    public List<CircleVo> queryHotCircle(Paging<CircleVo> pager, String userid){
        List<DBObject> listmongodba;
        List<PostVo> posts = new ArrayList<>();

        //查询所有  手动必推的圈子和圈子热度值靠前的所有圈子
        List<CircleVo> hotCircleList = circleService.findAllHotCircleList(pager);

        //循环获取圈子的被关注数和圈子中用户未查看过的总更新数
        for (int i = 0; i < hotCircleList.size(); i++){
            CircleVo vo = hotCircleList.get(i);
            //关注数
            int circleid = vo.getId();
            int follownum = circleService.queryCircleFollownum(circleid);
            vo.setFollownum(follownum);

            //圈子内用户未看过的总更新数
            //根据圈子id查询帖子
            List<PostVo> postVos = postService.queryPostCrile(circleid);

            if (StringUtil.isNotEmpty(userid)){
                //不为空查询当前用户未看过的总更新数
                listmongodba = facadePost.userRefulshListMongodb(Integer.parseInt(userid));//查询mongodb中用户看过的帖子列表

                if (listmongodba.size() != 0) {
                    for (int j = 0; j < listmongodba.size(); j++) {
                        PostVo post = new PostVo();
                        post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                        posts.add(post);//把mongodb转为post实体
                    }
                    postVos.removeAll(posts);
                    vo.setPostnewnum(postVos.size());
                }else{
                    vo.setPostnewnum(postVos.size());
                }
            }else{
                //用户未登录时userid为空时查询这个圈子的总帖子数
                vo.setPostnewnum(postVos.size());
            }
            hotCircleList.set(i, vo);
        }
        return hotCircleList;
    }
}

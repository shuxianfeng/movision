package com.movision.facade.circle;

import com.mongodb.DBObject;
import com.movision.facade.index.FacadePost;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circle.entity.MyCircle;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.followCircle.entity.FollowCircle;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private HomepageManageService homepageManageService;
    @Autowired
    private UserService userService;

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
            List<PostVo> postVos = postService.findAllPostCrile(circleid);

            if (StringUtil.isNotEmpty(userid)){
                //不为空查询当前用户未看过的总更新数
                listmongodba = facadePost.userRefulshListMongodbs(Integer.parseInt(userid));//查询mongodb中用户看过的帖子列表

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

    /**
     * 发现页--关注--关注的圈子列表查询
     */
    public List<CircleVo> getMineFollowCircle(String userid, Paging<CircleVo> pager){

        //首先查询当前用户已关注的圈子列表
        Map<String, Object> paramap = new HashMap<>();
        paramap.put("userid", Integer.parseInt(userid));
        List<CircleVo> myFollowCircleList = circleService.findAllMineFollowCircle(paramap, pager);

        for (int i=0; i<myFollowCircleList.size(); i++){
            CircleVo vo = myFollowCircleList.get(i);
            //循环获取圈子中包含的帖子数
            int circleid = vo.getId();
            int postsum = postService.queryPostNumByCircleid(circleid);
            vo.setPostnum(postsum);

            //循环获取当前用户是否已关注该圈子
            paramap.put("circleid", circleid);
            int count = circleService.queryFollowSum(paramap);
            vo.setIsfollow(count);
            myFollowCircleList.set(i, vo);
        }

        return myFollowCircleList;
    }


    /**
     * 查询所有圈子
     *
     * @param paging
     * @return
     */
    public List<CircleVo> findAllCircle(Paging<CircleVo> paging) {
        return circleService.findAllCircle(paging);
    }


    /**
     * 用户关注的5个圈子
     *
     * @param userid
     * @param circleid
     * @return
     */
    public Integer followCircleUser(int userid, String circleid) {
        Map map = new HashMap();
        String[] str = circleid.split(",");
        for (int i = 0; i < str.length; i++) {
            //查看用户关注的圈子
            map.put("userid", userid);
            map.put("circleid", Integer.parseInt(str[i]));
            int list = circleService.queryCircleid(map);
            if (list == 0) {
                map.put("userid", userid);
                map.put("circleid", Integer.parseInt(str[i]));
                map.put("intime", new Date());
                circleService.followCircle(map);
                //增加用户总关注数attention
                userService.updateUserAttention(userid);//关注人
            }
        }
        return 1;
    }

    /**
     * 获取APP开屏图接口
     */
    public String getOpenAppImg(){
        return homepageManageService.getOpenAppImg();
    }
}

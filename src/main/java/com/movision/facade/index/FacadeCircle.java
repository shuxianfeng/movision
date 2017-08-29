package com.movision.facade.index;

import com.mongodb.DBObject;
import com.movision.common.constant.PointConstant;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circle.entity.MyCircle;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.circleCategory.entity.CircleCategoryVo;
import com.movision.mybatis.circleCategory.service.CircleCategoryService;
import com.movision.mybatis.followCircle.service.FollowCircleService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserVo;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userOperationRecord.entity.UserOperationRecord;
import com.movision.mybatis.userOperationRecord.service.UserOperationRecordService;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.collections.iterators.ObjectArrayIterator;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private FollowCircleService followCircleService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserOperationRecordService userOperationRecordService;

    @Autowired
    private PointRecordFacade pointRecordFacade;

    @Autowired
    private FacadePost facadePost;

    public CircleVo queryCircleIndex1(String circleid, String userid) {

        CircleVo circleVo = circleService.queryCircleIndex1(Integer.parseInt(circleid));//查询圈子详情基础数据

        Map<String, Object> map = new HashMap<>();
        map.put("circleid", Integer.parseInt(circleid));
        map.put("sum", 10);//设置为10条
        List<Post> postList = postService.queryCircleSubPost(map);//查询这个圈子中最新的被设为热帖的10个帖子

        circleVo.setHotPostList(postList);

        if (StringUtils.isEmpty(userid)) {
            circleVo.setIsfollow(0);
        } else {
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("circleid", Integer.parseInt(circleid));
            parammap.put("userid", Integer.parseInt(userid));
            int count = followCircleService.isFollow(parammap);
            if (count == 0) {
                circleVo.setIsfollow(0);//可关注
            } else {
                circleVo.setIsfollow(1);//已关注
            }
        }

        return circleVo;

    }

    public List<CircleCategoryVo> queryCircleCategoryList(String userid) {

        List<CircleCategoryVo> categoryList = circleCategoryService.queryCircleByCategory();//查询所有圈子的分类------------实体add2

        //递归遍历，查询所有分类下的所有圈子列表放入Vo
        for (int i = 0; i < categoryList.size(); i++) {
            int categoryid = categoryList.get(i).getId();
            List<CircleVo> circlelist = circleService.queryCircleByCategory(categoryid);
            circlelist = getFollowsumNew(circlelist, userid);
            //将圈子列表加入分类对象CircleCategoryVo中
            categoryList.get(i).setCircleList(circlelist);
        }

        //手动将待审核的圈子全部set到返回的对象中
        CircleCategoryVo circleCategoryVo = new CircleCategoryVo();
        circleCategoryVo.setId(-1);//categoryid为-1时查询待审核的圈子
        circleCategoryVo.setCategoryname("待审核");
        List<CircleVo> list = circleService.queryAuditCircle();
        if (StringUtils.isEmpty(userid)) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setIssupport(0);
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                int circleid = list.get(i).getId();
                Map<String, Object> parammap = new HashMap<>();
                parammap.put("userid", Integer.parseInt(userid));
                parammap.put("circleid", circleid);
                int issupport = circleService.queryIsSupport(parammap);
                list.get(i).setIssupport(issupport);
            }
        }
        circleCategoryVo.setCircleList(list);//----------------------------------实体add3

        //美番2.0增加 “我关注” 条目
        CircleCategoryVo myFollowCircle = new CircleCategoryVo();
        myFollowCircle.setId(-2);//categoryid为-2时查询我关注的圈子
        myFollowCircle.setCategoryname("我关注");
        List<CircleVo> myfollowlist = new ArrayList<>();
        //查询我关注的圈子列表
        if (StringUtils.isNotEmpty(userid)){
            //登录状态下查询用户关注过的所有圈子列表
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("userid", Integer.parseInt(userid));
            myfollowlist = circleService.queryMyFollowCircleList(parammap);
        }
        myfollowlist = getFollowsumNew(myfollowlist, userid);
        myFollowCircle.setCircleList(myfollowlist);//----------------------------------实体add1

        //给圈子分类中，类目调换顺序---把所有内容重新排序放入输出列表中
        List<CircleCategoryVo> newcategoryList = new ArrayList<>();
        newcategoryList.add(myFollowCircle);//-------add我关注
        for (int i=0; i<categoryList.size(); i++){//--------add普通分类
            newcategoryList.add(i+1,categoryList.get(i));
        }
        newcategoryList.add(circleCategoryVo);//-------add待审核
        return newcategoryList;
    }

    /**
     * 对圈子列表循环获取 圈子关注数、圈子更新数、是否关注过
     * @return
     */
    public List<CircleVo> getFollowsumNew(List<CircleVo> circlelist, String userid){
        List<DBObject> listmongodba;
        List<PostVo> posts = new ArrayList<>();

        for (int i=0; i<circlelist.size(); i++){
            CircleVo vo = circlelist.get(i);
            int circleid = circlelist.get(i).getId();
            //根据圈子id查询圈子被关注数
            int follownum = circleService.queryCircleFollownum(circleid);
            vo.setFollownum(follownum);

            //根据圈子id查询圈子该用户的未读更新帖子数
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

            //再查询该用户是否关注过该圈子
            if (StringUtil.isNotEmpty(userid)) {
                Map<String, Object> paramap = new HashMap<>();
                paramap.put("userid", Integer.parseInt(userid));
                paramap.put("circleid", circleid);
                int count = circleService.queryFollowSum(paramap);
                vo.setIsfollow(count);
            }else{
                vo.setIsfollow(0);
            }
            circlelist.set(i, vo);
        }
        return circlelist;
    }

    public CircleVo queryCircleInfo(String circleid, String userid) {
        //查询基本信息实体
        CircleVo circleVo = circleService.queryCircleInfo(Integer.parseInt(circleid));

        //查询圈子的圈主
        User circlemaster = userService.queryCircleMasterByPhone(circleVo.getPhone());
        circleVo.setCirclemaster(circlemaster);

        //查询圈子的所有管理员
        List<User> circlemanagerList = userService.queryCircleManagerByCircleid(Integer.parseInt(circleid));
        circleVo.setCirclemanagerlist(circlemanagerList);

        if (StringUtil.isNotEmpty(userid)) {
            //查询当前用户有没有支持过该圈子
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("userid", Integer.parseInt(userid));
            parammap.put("circleid", Integer.parseInt(circleid));
            int count = circleService.queryIsSupport(parammap);
            if (count > 0) {
                circleVo.setIssupport(1);//已支持 不可支持
            } else {
                circleVo.setIssupport(0);//为支持 可支持
            }
        } else {
            circleVo.setIssupport(0);//为支持 可支持
        }
        return circleVo;
    }

    public int supportCircle(String userid, String circleid) {
        //首先查询该用户有没有支持过该圈子
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("circleid", Integer.parseInt(circleid));
        int count = circleService.querySupportSum(parammap);
        if (count == 0) {
            circleService.addSupportSum(parammap);
            circleService.addSupportRecored(parammap);
            return 0;//未支持过该圈子
        } else {
            return 1;//已支持过该圈子
        }
    }

    @CacheEvict(value = "indexData", key = "'index_data'")
    public int followCircle(String userid, String circleid) {
        //首先查询该用户有没有关注过该圈子
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("circleid", Integer.parseInt(circleid));
        parammap.put("intime", new Date());
        int count = circleService.queryFollowSum(parammap);
        if (count == 0) {

            //-------------------“我的”模块个人积分任务 增加积分的公共代码----------------------start
            //判断该用户有没有首次关注过圈子或有没有点赞过帖子评论等或有没有收藏过商品帖子活动
            UserOperationRecord entiy = userOperationRecordService.queryUserOperationRecordByUser(Integer.parseInt(userid));
            if (null == entiy || entiy.getIsfollow() == 0) {
                //如果未关注过圈子或者人的话,首次关注赠送积分
                pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.first_focus.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                UserOperationRecord userOperationRecord = new UserOperationRecord();
                userOperationRecord.setUserid(Integer.parseInt(userid));
                userOperationRecord.setIsfollow(1);
                if (null == entiy) {
                    //不存在新增
                    userOperationRecordService.insertUserOperationRecord(userOperationRecord);
                } else if (entiy.getIsfollow() == 0) {
                    //存在更新
                    userOperationRecordService.updateUserOperationRecord(userOperationRecord);
                }
            }
            //-------------------“我的”模块个人积分任务 增加积分的公共代码----------------------end

            circleService.followCircle(parammap);
            return 0;//未关注过该圈子
        } else {
            return 1;//已关注过该圈子
        }
    }

    @CacheEvict(value = "indexData", key = "'index_data'")
    public int cancelFollowCircle(String userid, String circleid) {
        //定义一个返回标志位
        int mark = 0;

        //就美番2.0的改写，在取消关注圈子时需要做还剩几个圈子的判断
        //用户至少需要保留关注一个圈子，当前取消的是最后一个圈子时，不让取消关注
        int sum = circleService.queryFollowSumByUser(Integer.parseInt(userid));

        if (sum > 1) {
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("userid", Integer.parseInt(userid));
            parammap.put("circleid", Integer.parseInt(circleid));
            circleService.cancelFollowCircle(parammap);
        }else{
            mark = -1;
        }
        return mark;
    }
}

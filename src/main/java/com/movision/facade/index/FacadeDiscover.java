package com.movision.facade.index;

import com.movision.common.constant.DiscoverConstant;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.exception.BusinessException;
import com.movision.facade.paging.PageFacade;
import com.movision.fsearch.pojo.spec.NormalSearchSpec;
import com.movision.fsearch.service.exception.ServiceException;
import com.movision.fsearch.service.impl.PostSearchService;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.circleCategory.service.CircleCategoryService;
import com.movision.mybatis.homepageManage.entity.HomepageManageVo;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.mybatis.post.entity.ActiveVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostSearchEntity;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabel.service.PostLabelService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserVo;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userRefreshRecord.entity.UserReflushCount;
import com.movision.mybatis.userRefreshRecord.service.UserRefreshRecordService;
import com.movision.utils.DateUtils;
import com.movision.utils.VideoCoverURL;
import com.movision.utils.pagination.model.Paging;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

/**
 * @Author shuxf
 * @Date 2017/1/18 17:51
 */
@Service
public class FacadeDiscover {

    @Autowired
    private PostLabelService postLabelService;

    @Autowired
    private PageFacade pageFacade;

    @Autowired
    private HomepageManageService homepageManageService;

    @Autowired
    private CircleCategoryService circleCategoryService;

    @Autowired
    private PostService postService;

    @Autowired
    private CircleService circleService;

    @Autowired
    private UserService userService;

    @Autowired
    private FacadePost facadePost;

    @Autowired
    private UserRefreshRecordService userRefreshRecordService;

    @Autowired
    private PostSearchService postSearchService;

    @Autowired
    private VideoCoverURL videoCoverURL;

    public Map<String, Object> queryDiscoverIndexData(String userid) {

        HashMap<String, Object> pmap = new HashMap();
        List<HomepageManageVo> homepageManageList = homepageManageService.queryBannerList(1);//查询发现页顶部banner轮播图 type=1
        List<CircleCategory> circleCategoryList = circleCategoryService.queryCircleCategoryList();//查询发现页次banner所有圈子类别轮播图
        List<ActiveVo> hotActiveList = postService.queryHotActiveList();//查询发现页热门活动列表

        List<CircleVo> hotCircleList;
        hotCircleList = circleService.queryHotCircleList();//查询发现页被设置为发现页展示的圈子

        for (int i = 0; i < hotCircleList.size(); i++) {
            //设置发现页最下面推荐的10个圈子下只推荐最新的5个热帖
            Map<String, Object> map = new HashMap<>();
            int circleid = hotCircleList.get(i).getId();
            map.put("circleid", circleid);
            map.put("sum", 5);//设置为5
            List<Post> postlist = postService.queryCircleSubPost(map);
            hotCircleList.get(i).setHotPostList(postlist);

            //这里需要判断这个圈子是否可以已被当前用户关注
            if (!StringUtils.isEmpty(userid)) {
                //userid不为空，不为空时需要查询该用户有没有关注当前圈子
                HashMap<String, Object> parammap = new HashMap();
                parammap.put("userid", Integer.parseInt(userid));
                parammap.put("circleid", hotCircleList.get(i).getId());
                int count = circleService.queryCountByFollow(parammap);
                if (count == 0) {//未关注
                    hotCircleList.get(i).setIsfollow(0);
                } else {//已关注
                    hotCircleList.get(i).setIsfollow(1);
                }
            } else {
                //未登录时userid为空,全部按钮均显示为可关注（点击跳转到登录页）
                hotCircleList.get(i).setIsfollow(0);
            }
        }

        pmap.put("homepageManageList", homepageManageList);
        pmap.put("circleCategoryList", circleCategoryList);
        pmap.put("hotActiveList", hotActiveList);
        pmap.put("hotCircleList", hotCircleList);
        return pmap;
    }

    public Map<String, Object> queryDiscoverIndexData2Up() throws ParseException {

        Map<String, Object> map = new HashMap<>();
        List<HomepageManageVo> homepageManageList = homepageManageService.queryBannerList(1);//查询发现页顶部banner轮播图
        List<ActiveVo> hotActiveList = postService.queryHotActiveList();//查询发现页热门活动列表
        List<UserVo> hotUserList = userService.queryHotUserList();//查询发现页热门作者列表

        //循环查询作者的发帖数
        for (int i = 0; i < hotUserList.size(); i++) {
            UserVo vo = hotUserList.get(i);
            int userid = vo.getId();
            //根据userid查询该用户的总发帖数
            int postsum = postService.queryPostNumByUserid(userid);
            vo.setPostsum(postsum);
            hotUserList.set(i, vo);
        }

        //循环查询活动的参与人数和活动的距离结束天数
        for (int i = 0; i < hotActiveList.size(); i++) {
            ActiveVo ao = hotActiveList.get(i);
            //计算距离结束时间
            Date begin = ao.getBegintime();
            Date end = ao.getEndtime();
            Date now = new Date();
            int enddays = DateUtils.activeEndDays(now, begin, end);
            ao.setEnddays(enddays);
            //查询活动参与总人数
            int partsum = postService.queryActivePartSum(ao.getId());
            ao.setPartsum(partsum);
            hotActiveList.set(i, ao);
        }

        map.put("homepageManageList", homepageManageList);
        map.put("hotActiveList", hotActiveList);
        map.put("hotUserList", hotUserList);
        return map;
    }

    /**
     * 查询评论最多的帖子
     *
     * @param pager
     * @return
     */
    public List<PostVo> searchHotCommentPostInAll(Paging<PostVo> pager) {
        //1 查找最热帖子集合
        List<PostVo> postVoList = postService.findAllHotCommentPostInAll(pager);

        facadePost.findPostLabel(postVoList);

        return postVoList;
    }

    /**
     * 查询当月评论最多的帖子
     *
     * @param pager
     * @return
     */
    public List<PostVo> searchHotCommentPostInCurrentMonth(Paging<PostVo> pager) {
        List<PostVo> postVoList = postService.findAllHotCommentPostInCurrentMonth(pager);

        facadePost.findPostLabel(postVoList);

        return postVoList;
    }

    public List<PostVo> searchMostZanPostListInAll(Paging<PostVo> paging) {
        List<PostVo> postVoList = postService.findAllMostZanPostInAll(paging);

        facadePost.findPostLabel(postVoList);

        return postVoList;
    }

    public List<PostVo> searchMostZanPostListInCurrentMonth(Paging<PostVo> paging) {
        List<PostVo> postVoList = postService.findAllMostZanPostInCurrentMonth(paging);

        facadePost.findPostLabel(postVoList);

        return postVoList;
    }

    public List<PostVo> searchMostCollectPostListInAll(Paging<PostVo> paging) {
        List<PostVo> postVoList = postService.findAllMostCollectInAll(paging);

        facadePost.findPostLabel(postVoList);

        return postVoList;
    }

    public List<PostVo> searchMostCollectPostListInCurrentMonth(Paging<PostVo> paging) {
        List<PostVo> postVoList = postService.findAllMostCollectInCurrentMonth(paging);

        facadePost.findPostLabel(postVoList);

        return postVoList;
    }

    public List<UserVo> searchMostFansAuthorInAll(Paging<UserVo> paging) {
        List<UserVo> list = userService.findAllMostFansAuthorInAll(paging);

        addIsFollow(list);

        return list;
    }

    /**
     * 增加 UserVo 是否关注 属性
     *
     * @param list
     */
    private void addIsFollow(List<UserVo> list) {
        int userid = ShiroUtil.getAppUserID();
        if (userid == 0) {
            //未登录
            for (UserVo u : list) {
                u.setIsfollow(0);   //未登录，则显示未登录
            }
        } else {
            //已登录，则根据关注用户表获取是否已关注作者
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("userid", userid);

            for (UserVo u : list) {
                parammap.put("id", u.getId());
                //获取当前用户是否关注过该粉丝
                int sum = userService.queryIsFollowAuthor(parammap);
                u.setIsfollow(sum);
            }
        }
    }

    public List<UserVo> searchMostFansAuthorInCurrentMonth(Paging<UserVo> paging) {
        List<UserVo> list = userService.findAllMostFansAuthorInCurrentMonth(paging);
        addIsFollow(list);
        return list;
    }

    public List<UserVo> searchMostCommentAuthorInAll(Paging<UserVo> paging) {
        List<UserVo> list = userService.findAllMostCommentAuthorInAll(paging);
        addIsFollow(list);
        return list;
    }

    public List<UserVo> searchMostCommentAuthorInCurrentMonth(Paging<UserVo> paging) {
        List<UserVo> list = userService.findAllMostCommentAuthorInCurrentMonth(paging);
        addIsFollow(list);
        return list;
    }

    public List<UserVo> searchMostPostAuthorInAll(Paging<UserVo> paging) {
        List<UserVo> list = userService.findAllMostPostAuthorInAll(paging);
        addIsFollow(list);
        return list;
    }

    public List<UserVo> searchMostPostAuthorInCurrentMonth(Paging<UserVo> paging) {
        List<UserVo> list = userService.findAllMostPostAuthorInCurrentMonth(paging);
        addIsFollow(list);
        return list;
    }

    /**
     * 统计浏览最多的帖子排行
     *
     * @param paging
     * @return
     */
    public List<PostVo> searchMostViewPost(Paging<PostVo> paging, String isMonthlyFlag) {
        //查询
        Map map = new HashMap();
        map.put("flag", isMonthlyFlag);
        List<PostVo> postVoList = postService.queryPostInAll(map);

        //计算Paging中的分页参数
        paging.setTotal(postVoList.size());
        //代码层分页操作
        List list = pageFacade.getPageList(postVoList, paging.getCurPage(), paging.getPageSize());
        //统计标签 10条
        facadePost.findPostLabel(list);

        return list;
    }

    /**
     * 处理来自mongoDB中的帖子浏览数据
     * 先筛选出当月的数据，再统计出每个帖子的浏览次数，最后排序
     *
     * @return
     */
    private List<UserReflushCount> handlePostViewListFromMongoDB() {
        //系统的当前的月份的第一天
        String firstDay = DateUtils.getCurrentMonthFirstDay();
//        String firstDay = "2017-07-01";
        //系统的当前月份的最后一天
        String lastDay = DateUtils.getCurrentMonthLastDay();
//        String lastDay = "2017-07-31";
        //统计在一个月内,每个帖子的浏览次数
        List<UserReflushCount> viewListInMongoDB = userRefreshRecordService.getPostViewRecordMonthly(firstDay, lastDay);

        return viewListInMongoDB;
    }


    public Paging<?> searchHotRange(String pageNo, String pageSize, int title, int type) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        //总排行
        if (DiscoverConstant.HOT_RANGE_TYPE.total_range.getCode() == type) {

            if (DiscoverConstant.HOT_RANGE_TITLE.post_comment_list.getCode() == title) {

                Paging<PostVo> pager = new Paging<PostVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<PostVo> list = searchHotCommentPostInAll(pager);
                pager.result(videoCoverURL.getVideoCoverByList(list));
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.post_view_list.getCode() == title) {

                Paging<PostVo> pager = new Paging<PostVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<PostVo> list = searchMostViewPost(pager, "year");
                pager.setRows(videoCoverURL.getVideoCoverByList(list));
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.post_zan_list.getCode() == title) {

                Paging<PostVo> pager = new Paging<PostVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<PostVo> list = searchMostZanPostListInAll(pager);
                pager.result(videoCoverURL.getVideoCoverByList(list));
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.post_collect_list.getCode() == title) {

                Paging<PostVo> pager = new Paging<PostVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<PostVo> list = searchMostCollectPostListInAll(pager);
                pager.result(videoCoverURL.getVideoCoverByList(list));
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.author_fans_list.getCode() == title) {

                Paging<UserVo> pager = new Paging<UserVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<UserVo> list = searchMostFansAuthorInAll(pager);
                pager.result(list);
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.author_comment_list.getCode() == title) {

                Paging<UserVo> pager = new Paging<UserVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<UserVo> list = searchMostCommentAuthorInAll(pager);
                pager.result(list);
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.author_post_list.getCode() == title) {

                Paging<UserVo> pager = new Paging<UserVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<UserVo> list = searchMostPostAuthorInAll(pager);
                pager.result(list);
                return pager;


            } else {
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "发现页-热门排行-导航栏标题传参错误");
            }
        }

        //月排行
        if (DiscoverConstant.HOT_RANGE_TYPE.month_range.getCode() == type) {

            if (DiscoverConstant.HOT_RANGE_TITLE.post_comment_list.getCode() == title) {

                Paging<PostVo> pager = new Paging<PostVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<PostVo> list = searchHotCommentPostInCurrentMonth(pager);
                pager.result(videoCoverURL.getVideoCoverByList(list));
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.post_view_list.getCode() == title) {

                Paging<PostVo> pager = new Paging<PostVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<PostVo> list = searchMostViewPost(pager, "month");
                pager.setRows(videoCoverURL.getVideoCoverByList(list));
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.post_zan_list.getCode() == title) {

                Paging<PostVo> pager = new Paging<PostVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<PostVo> list = searchMostZanPostListInCurrentMonth(pager);
                pager.result(videoCoverURL.getVideoCoverByList(list));
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.post_collect_list.getCode() == title) {

                Paging<PostVo> pager = new Paging<PostVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<PostVo> list = searchMostCollectPostListInCurrentMonth(pager);
                pager.result(videoCoverURL.getVideoCoverByList(list));
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.author_fans_list.getCode() == title) {

                Paging<UserVo> pager = new Paging<UserVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<UserVo> list = searchMostFansAuthorInCurrentMonth(pager);
                pager.result(list);
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.author_comment_list.getCode() == title) {

                Paging<UserVo> pager = new Paging<UserVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<UserVo> list = searchMostCommentAuthorInCurrentMonth(pager);
                pager.result(list);
                return pager;

            } else if (DiscoverConstant.HOT_RANGE_TITLE.author_post_list.getCode() == title) {

                Paging<UserVo> pager = new Paging<UserVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
                List<UserVo> list = searchMostPostAuthorInCurrentMonth(pager);
                pager.result(list);
                return pager;


            } else {
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "发现页-热门排行-导航栏标题传参错误");
            }
        }

        return null;

    }

    public List<Map<String, Object>> searchAll(NormalSearchSpec spec) throws ServiceException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        String name = spec.getQ();
        //帖子/活动
        getPosts(spec, list);
        //作者
        getUsers(list, name);
        //圈子
        getCircles(list, name);
        //标签
        getLabels(list, name);

        return list;
    }

    private void getPosts(NormalSearchSpec spec, List<Map<String, Object>> list) throws ServiceException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        List<PostSearchEntity> postList = postSearchService.searchForPost(spec);
        for (int i = 0; i < postList.size(); i++) {
            PostSearchEntity pe = postList.get(i);
            String postcontent = pe.getPostcontent();
            JSONArray jsonArray = JSONArray.fromObject(postcontent);
            jsonArray = videoCoverURL.getVideoCover(jsonArray);
            //-----将转换完的数据封装返回
            pe.setPostcontent(jsonArray.toString());
            postList.set(i, pe);
        }
        Map postMap = new HashMap();
        postMap.put("name", "帖子");
        if (postList.size() > 3) {
            postMap.put("isMore", true);
            postMap.put("postList", postList.subList(0, 3));
        } else {
            postMap.put("isMore", false);
            postMap.put("postList", postList);
        }
        list.add(postMap);
    }

    private void getUsers(List<Map<String, Object>> list, String name) {
        List<User> userList = userService.queryUserByName(name);
        Map userMap = new HashMap();
        userMap.put("name", "作者");
        if (userList.size() > 3) {
            userMap.put("isMore", true);
            userMap.put("userList", userList.subList(0, 3));
        } else {
            userMap.put("isMore", false);
            userMap.put("userList", userList);
        }
        list.add(userMap);
    }

    private void getLabels(List<Map<String, Object>> list, String name) {
        List<PostLabel> labelList = postLabelService.queryLabelByname(name);
        Map labelMap = new HashMap();
        labelMap.put("name", "标签");
        if (labelList.size() > 3) {
            labelMap.put("isMore", true);
            labelMap.put("labelList", labelList.subList(0, 3));
        } else {
            labelMap.put("isMore", false);
            labelMap.put("labelList", labelList);
        }
        list.add(labelMap);
    }

    private void getCircles(List<Map<String, Object>> list, String name) {
        List<Circle> circleList = circleService.queryCircleByName(name);
        Map circleMap = new HashMap();
        circleMap.put("name", "圈子");
        if (circleList.size() > 3) {
            circleMap.put("isMore", true);
            circleMap.put("circleList", circleList.subList(0, 3));
        } else {
            circleMap.put("isMore", false);
            circleMap.put("circleList", circleList);
        }
        list.add(circleMap);
    }

    public List<Circle> findAllCircleByNameInSearch(Paging<Circle> paging, String name) {
        Map map = new HashMap();
        map.put("name", name);
        return circleService.findAllCircleByNameInSearch(paging, map);
    }

    public List<User> findAllUserByName(Paging<User> paging, String name) {
        Map map = new HashMap();
        map.put("name", name);
        return userService.findAllUserByName(paging, map);
    }

    public List<PostLabel> findAllLabelByName(Paging<PostLabel> paging, String name) {
        Map map = new HashMap();
        map.put("name", name);
        return postLabelService.findAllLabelByName(paging, map);
    }


}

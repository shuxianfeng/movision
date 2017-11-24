package com.movision.facade.index;

import com.mongodb.DBObject;
import com.movision.facade.address.AddressFacade;
import com.movision.facade.paging.PageFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.opularSearchTerms.service.OpularSearchTermsService;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecordVo;
import com.movision.utils.ListUtil;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 该类的方法对应1117版本的接口
 *
 * @Author zhanglei
 * @Date 2017/11/17 10:28
 *
 */
@Service
public class FacadePost1117 {
    private static Logger log = LoggerFactory.getLogger(FacadePost1117.class);

    @Autowired
    private FacadePost facadePost;
    @Autowired
    private UserService userService;
    @Autowired
    private PageFacade pageFacade;

    @Autowired
    private PostService postService;
    @Autowired
    private AddressFacade addressFacade;

    @Autowired
    private OpularSearchTermsService opularSearchTermsService;

    /**
     * 首页-推荐 刷新接口
     *
     * @param userid
     * @param device
     * @return
     */
    public List recommendPost(String userid, String device) {
        long count = facadePost.mongodbCount();
        List<PostVo> list = null;
        List<PostVo> alllist = postService.findAllPostListHeat();//查询所有帖子
        List<PostVo> posts = new ArrayList<>();
        List<DBObject> listmongodb = null;
        if (userid == null) {
            //未登录
            list = postService.findAllPostHeatValue();//根据热度值排序查询帖子
            listmongodb = facadePost.userRefulshListMongodbToDevice(device, 1);//用户有没有看过
            if (listmongodb.size() != 0) {
                for (int j = 0; j < listmongodb.size(); j++) {
                    PostVo post = new PostVo();
                    post.setId(Integer.parseInt(listmongodb.get(j).get("postid").toString()));
                    posts.add(post);//把mongodb转为post实体
                }
                list.removeAll(posts);
            }
            list = NotLoginretuenList(list, 1, device, -1);
        } else {
            //已登录
            if (alllist != null) {
                if (count < 1000) {
                    //mongodb的里面的刷新记录小于1000条记录的时候进行用户分析
                    list = userAnalysisSmall(userid, alllist, posts, device);
                } else if (count >= 1000) {
                    //mongodb的里面的刷新记录大于等于1000条记录的时候进行用户分析
                    list = userAnalysisBig(userid, posts, device);
                }
            }
        }
        return list;
    }

    /**
     * mongodb的里面的刷新记录  大于等于   1000条记录的时候进行用户分析
     *
     * @param userid
     * @param posts
     */
    private List userAnalysisBig(String userid, List<PostVo> posts, String device) {
        List<PostVo> list = null;
        List<UserRefreshRecordVo> result;//查询用户最喜欢的圈子
        List<DBObject> listmongodb;
        //查询用户已经看过的帖子
        listmongodb = facadePost.userRefulshListMongodbToDevice(device, 1);
        if (listmongodb.size() != 0) {
            //统计用户浏览的帖子所属的每个圈子的数量
            result = opularSearchTermsService.userFlush(device);
            int crileid = result.get(0).getCrileid();
            List<PostVo> criclelist = postService.queryPostCricle(crileid);//这个圈子的帖子（根据热度值排序）
            List<PostVo> overPost = postService.queryoverPost(crileid);//查询剩下的所有帖子
            criclelist.addAll(overPost);//所有帖子
            //查询出mongodb中用户刷新的帖子
            for (int j = 0; j < listmongodb.size(); j++) {
                PostVo post = new PostVo();
                post.setId(Integer.parseInt(listmongodb.get(j).get("postid").toString()));
                posts.add(post);//把mongodb转为post实体
            }
            criclelist.removeAll(posts);//剩下的帖子
            list = retuenList(criclelist, userid, 1, device, -1);
            return list;
        } else {
            list = postService.findAllPostHeatValue();
            list = retuenList(list, userid, 1, device, -1);
            return list;
        }
    }

    /**
     * mongodb的里面的刷新记录  小于1000  条记录的时候进行用户分析
     *
     * @param userid
     * @param
     * @param alllist
     * @param posts
     * @return
     */
    private List userAnalysisSmall(String userid, List<PostVo> alllist, List<PostVo> posts, String device) {
        List<DBObject> listmongodba;
        List<PostVo> list = null;
        listmongodba = facadePost.userRefulshListMongodbToDevice(device, 1);//用户有没有看过
        if (listmongodba.size() != 0) {
            for (int j = 0; j < listmongodba.size(); j++) {
                PostVo post = new PostVo();
                post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                posts.add(post);//把mongodb转为post实体
            }
            //把看过的帖子过滤掉
            alllist.removeAll(posts);//alllist是剩余的帖子
            list = retuenList(alllist, userid, 1, device, -1);
        } else {
            //登录情况下但是mongodb里面没有刷新记录
            list = postService.findAllPostHeatValue();
            list = retuenList(list, userid, 1, device, -1);
            return list;
        }
        return list;
    }

    /**
     * 首页-本地 刷新接口
     *
     * @param userid
     * @param lat
     * @param device
     * @param lng
     * @return
     */
    public List localhostPost(String userid, String lat, String device, String lng) {
        List<PostVo> list = null;
        List<DBObject> listmongodba = null;
        List<PostVo> posts = new ArrayList<>();
        Map map = null;
        String citycode = null;
        try {
            map = addressFacade.getAddressByLatAndLng(lat, lng);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int flag = Integer.parseInt(map.get("flag").toString());
        if (flag == 1) {
            citycode = map.get("citycode").toString();
        }
        List<PostVo> cityPost = new ArrayList<>();
        List<PostVo> labelPost = new ArrayList<>();
        //根据传过来的地区去yw_city查代码
        if (lat != null && lng != null) {
            //根据citycode查询城市
            if (citycode != null) {
                String area = userService.areaname(citycode);
                int end = area.lastIndexOf("市");
                String str = area.substring(0, end);
                //标题带南京
                cityPost = postService.queryCityPost(str);
                //标签有本地
                labelPost = postService.queryCityLabel(str);
            }
        }
        if (citycode != null) {
            if (userid == null) {//未登录
                list = postService.findAllCityPost(citycode);//根据热度值排序查询帖子
                listmongodba = facadePost.userRefulshListMongodbToDevice(device, 3);//用户有没有看过
                if (cityPost.size() != 0) {
                    list.addAll(cityPost);
                }
                if (labelPost.size() != 0) {
                    list.addAll(labelPost);
                }
                if (listmongodba.size() != 0) {
                    for (int j = 0; j < listmongodba.size(); j++) {
                        PostVo post = new PostVo();
                        post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                        posts.add(post);//把mongodb转为post实体
                    }
                    list.removeAll(posts);
                    /**Set<PostVo> linkedHashSet = new LinkedHashSet<PostVo>(list);
                     list = new ArrayList<PostVo>(linkedHashSet);
                     ComparatorChain chain = new ComparatorChain();
                     chain.addComparator(new BeanComparator("heatvalue"), true);//true,fase正序反序
                     Collections.sort(list, chain);
                     list = NotLoginretuenList(list, 3, device, -1);*/
                }
                Set<PostVo> linkedHashSet = new LinkedHashSet<PostVo>(list);
                list = new ArrayList<PostVo>(linkedHashSet);
                ComparatorChain chain = new ComparatorChain();
                chain.addComparator(new BeanComparator("heatvalue"), true);//true,fase正序反序
                Collections.sort(list, chain);
                list = NotLoginretuenList(list, 3, device, -1);
                return list;
            } else {//已登录
                //根据地区查询帖子
                //listmongodba = userRefulshListMongodb(Integer.parseInt(userid), 3);//用户有没有看过
                listmongodba = facadePost.userRefulshListMongodbToDevice(device, 3);//用户有没有看过
                //根据city查询帖子
                List<PostVo> postVos = postService.findAllCityPost(citycode);
                if (cityPost.size() != 0) {
                    postVos.addAll(cityPost);
                }
                if (labelPost.size() != 0) {
                    postVos.addAll(labelPost);
                }
                if (listmongodba.size() != 0) {
                    for (int j = 0; j < listmongodba.size(); j++) {
                        PostVo post = new PostVo();
                        post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                        posts.add(post);//把mongodb转为post实体
                    }
                    postVos.removeAll(posts);
                    Set<PostVo> linkedHashSet = new LinkedHashSet<PostVo>(postVos);
                    postVos = new ArrayList<PostVo>(linkedHashSet);
                    ComparatorChain chain = new ComparatorChain();
                    chain.addComparator(new BeanComparator("heatvalue"), true);//true,fase正序反序
                    Collections.sort(postVos, chain);
                    list = retuenList(postVos, userid, 3, device, -1);
                } else {//登录但是刷新列表中没有帖子
                    list = postService.findAllCityPost(citycode);//根据热度值排序查询帖子
                    if (cityPost.size() != 0) {
                        list.addAll(cityPost);
                    }
                    if (labelPost.size() != 0) {
                        list.addAll(labelPost);
                    }
                    Set<PostVo> linkedHashSet = new LinkedHashSet<PostVo>(list);
                    list = new ArrayList<PostVo>(linkedHashSet);
                    ComparatorChain chain = new ComparatorChain();
                    chain.addComparator(new BeanComparator("heatvalue"), true);//true,fase正序反序
                    Collections.sort(list, chain);
                    list = retuenList(list, userid, 3, device, -1);
                    return list;
                }
            }
        }
        return list;
    }

    /**
     * 首页-圈子 刷新接口
     *
     * @param userid
     * @param circleid
     * @param device
     * @return
     */
    public List circleRefulsh(String userid, int circleid, String device) {
        List<DBObject> listmongodba = null;
        List<PostVo> posts = new ArrayList<>();
        List<PostVo> list = null;
        if (userid == null) {
            //这个圈子的帖子
            list = postService.findAllPostCrile(circleid);//根据热度值排序查询帖子
            listmongodba = facadePost.userRefulshListMongodbToDeviceHistory(device, 4, String.valueOf(circleid));//用户有没有看过
            if (listmongodba.size() != 0) {
                for (int j = 0; j < listmongodba.size(); j++) {
                    PostVo post = new PostVo();
                    post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                    posts.add(post);//把mongodb转为post实体

                }
                list.removeAll(posts);
            }
            list = NotLoginretuenList(list, 4, device, -1);
            return list;
        } else {
            listmongodba = facadePost.userRefulshListMongodbToDeviceHistory(device, 4, String.valueOf(circleid));//用户有没有看过
            if (listmongodba.size() != 0) {
                for (int j = 0; j < listmongodba.size(); j++) {
                    PostVo post = new PostVo();
                    post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                    posts.add(post);//把mongodb转为post实体
                }
                //根据圈子id查询帖子
                List<PostVo> postVos = postService.findAllPostCrile(circleid);
                postVos.removeAll(posts);
                list = retuenList(postVos, userid, 4, device, -1);
            } else {
                //登录但是刷新列表中没有帖子
                list = postService.findAllPostCrile(circleid);//根据热度值排序查询帖子
                list = retuenList(list, userid, 4, device, -1);
                return list;
            }
        }
        return list;
    }


    /**
     * 首页-标签 刷新接口
     *
     * @param userid
     * @param labelid
     * @param device
     * @return
     */
    public List labelPost(String userid, int labelid, String device) {
        List<PostVo> list = null;
        List<DBObject> listmongodba = null;
        List<PostVo> posts = new ArrayList<>();
        if (userid == null) {
            list = postService.findAllLabelAllPost(labelid);//根据热度值排序查询帖子
            listmongodba = facadePost.userRefulshListMongodbToDeviceHistoryLabelid(device, 5, labelid);//用户有没有看过
            if (listmongodba.size() != 0) {
                for (int j = 0; j < listmongodba.size(); j++) {
                    PostVo post = new PostVo();
                    post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                    posts.add(post);//把mongodb转为post实体
                }
                list.removeAll(posts);
            }
            list = NotLoginretuenList(list, 5, device, labelid);
            return list;
        } else {
            listmongodba = facadePost.userRefulshListMongodbToDeviceHistoryLabelid(device, 5, labelid);//用户有没有看过
            if (listmongodba.size() != 0) {
                for (int j = 0; j < listmongodba.size(); j++) {
                    PostVo post = new PostVo();
                    post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                    posts.add(post);//把mongodb转为post实体
                }
                //根据标签查询帖子
                List<PostVo> postVos = postService.findAllLabelAllPost(labelid);
                postVos.removeAll(posts);
                list = retuenList(postVos, userid, 5, device, labelid);
            } else {
                //登录但是刷新列表中没有帖子
                list = postService.findAllLabelAllPost(labelid);//根据热度值排序查询帖子
                list = retuenList(list, userid, 5, device, labelid);
                return list;
            }
            return list;
        }
    }


    /**
     * 登录情况下的返回数据
     *
     * @param lists
     * @param userid
     * @return
     */
    public List retuenList(List<PostVo> lists, String userid, int type, String device, int labelid) {
        List<PostVo> list = null;
        if (lists != null) {
            list = pageFacade.getPageList(lists, 1, 10);
            facadePost.findPostLabel(list);    //查询帖子的标签（不好合并）
            facadePost.findHotComment(list);   //查询帖子的最热评论（不好合并）
            facadePost.insertmongo(list, userid, type, device, labelid);   //刷新出来的帖子，依次插入mongoDB中
            facadePost.zanIsPost(Integer.parseInt(userid), list);
        }
        return list;
    }

    /**
     * 未登录情况下的返回数据
     *
     * @param lists 最多10条
     * @param
     * @return
     */
    public List NotLoginretuenList(List<PostVo> lists, int type, String device, int labelid) {
        List<PostVo> list = null;
        if (lists != null) {
            list = pageFacade.getPageList(lists, 1, 10);
            facadePost.findPostLabel(list);    //根据postid查询标签信息
            facadePost.findHotComment(list);   //根据postid查询所有评论
            facadePost.insertmongo(list, "", type, device, labelid);
        }
        return list;
    }


    /***
     * 下拉刷新
     * @param userid
     * @param
     * @param type
     * @param
     * @return
     */
    public List userRefreshListNew_1117(String userid, String device, int type, String lat, String circleid, String labelid, String lng) {
        List<PostVo> list = null;
        if (type == 1) {//推荐
            list = recommendPost(userid, device);
        } else if (type == 2) {//关注
            list = followPost(userid, device);
        } else if (type == 3) {//本地
            list = localhostPost(userid, lat, device, lng);
        } else if (type == 4) {//圈子c
            list = circleRefulsh(userid, Integer.parseInt(circleid), device);
        } else if (type == 5) {//标签
            list = labelPost(userid, Integer.parseInt(labelid), device);
        }
        return list;
    }

    /**
     * 首页-关注 刷新接口
     *
     * @param userid 登录人id
     * @param device 登录设备号
     * @return
     */
    public List followPost(String userid, String device) {

        List<PostVo> list = null;   //返回值

        if (userid != null) {   //登录场景下
            List<PostVo> allList = new ArrayList<>();
            //封装所有关注的帖子
            facadePost.wrapAllFollowPost(userid, allList);

            if (ListUtil.isEmpty(allList)) {
                log.debug("******首页关注的allList是空******");
                return list;
            }
            log.debug("******首页关注的allList不是空******");
            //通过使用LinkedHashSet来去除掉allList中重复的帖子，并且保证帖子的顺序不会发生变化
            Set<PostVo> linkedHashSet = new LinkedHashSet<PostVo>(allList);
            allList = new ArrayList<PostVo>(linkedHashSet);
            //使用ComparatorChain来指定字段排序
            ComparatorChain chain = new ComparatorChain();
            //true,fase正序反序,true表示逆序（默认排序是从小到大）
            chain.addComparator(new BeanComparator("intime"), true);
            Collections.sort(allList, chain);

            //用户【关注】的浏览历史记录，根据userid和设备号查询
            List<DBObject> listmongodba = facadePost.queryUserViewRecordByUseridTypeDevice(Integer.valueOf(userid), 2, device);

            if (listmongodba.size() != 0) {
                //用户存在浏览历史
                log.debug("用户存在浏览历史");
                List<PostVo> posts = new ArrayList<>();
                for (int j = 0; j < listmongodba.size(); j++) {
                    PostVo post = new PostVo();
                    post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                    posts.add(post);    //把mongodb转为post实体
                }
                allList.removeAll(posts);   //过滤掉浏览过的帖子
                list = retuenList(allList, userid, 2, device, -1);
            } else {
                log.debug("该用户无浏览历史");
                //该用户无浏览历史
                list = retuenList(allList, userid, 2, device, -1);
            }
        }
        if (list == null) {
            log.debug("******[followPost]首页关注的list是null******");
        } else {
            log.debug("******[followPost]首页关注的list******" + list.toString());
        }

        return list;
    }


    /**
     * 用户刷新的历史记录列表
     *
     * @param userid
     * @return
     */
    public List<PostVo> userReflushHishtoryRecord_1117(String userid, Paging<PostVo> paging, int type, String device, String labelid, String circleid, String postids) {
        log.debug("首页历史接口的传参postid=" + postids);
        log.debug("首页历史接口的传参pageNo=" + paging.getCurPage());
        List<PostVo> postVo = null;
        if (userid != null) {
            //用户登录下
            postVo = userLoginHistoryRecord(userid, paging, type, labelid, circleid, postids, postVo, device);
        } else {
            //未登录下
            postVo = userNotLoginHistoryRecord(paging, type, device, labelid, circleid, postids, postVo);
        }
        return postVo;
    }

    /**
     * 用户未登录下历史记录
     *
     * @param paging
     * @param type
     * @param device
     * @param labelid
     * @param circleid
     * @param postids
     * @param postVo
     * @param
     * @param
     * @return
     */
    private List<PostVo> userNotLoginHistoryRecord(Paging<PostVo> paging, int type, String device, String labelid, String circleid, String postids, List<PostVo> postVo) {
        if (StringUtil.isEmpty(labelid) && StringUtil.isEmpty(circleid)) {
            postVo = userNotLoginHistoryRecordThird(paging, type, device, postids);
        }
        if (StringUtil.isNotEmpty(labelid)) {
            postVo = userNotLoginHistoryRecordLabel(paging, type, device, labelid, postids);
        } else if (StringUtil.isNotEmpty(circleid)) {
            postVo = userNotLoginHistoryRecordCircle(paging, type, device, circleid, postids);
        }
        return postVo;
    }

    /**
     * 用户未登录状态下圈子历史
     *
     * @param paging
     * @param type
     * @param device
     * @param circleid
     * @param postids
     * @return
     */
    private List<PostVo> userNotLoginHistoryRecordCircle(Paging<PostVo> paging, int type, String device, String circleid, String postids) {
        List<PostVo> postVo;
        List<DBObject> intimePost = null;
        List<DBObject> us = null;
        if (!postids.equals("0")) {
            List<DBObject> onlyPost = facadePost.queryOnlPostNotLoginCircleid(device, type, Integer.parseInt(postids), circleid);
            String intime = onlyPost.get(0).get("intime").toString();
            intimePost = facadePost.queryPosyByImtimeDeviceCircle(intime, device, type, circleid);
        } else {
            //查询用户有无历史
            int count = facadePost.userHistoryDeviceCircleCount(device, type, circleid);
            if (count > 10) {
                us = facadePost.userRefulshListMongodbToDeviceHistory(device, type, circleid);
            } else {
                us = null;
            }
        }
        List<Integer> postVos = new ArrayList<>();
        if (intimePost != null) {
            for (int i = 0; i < intimePost.size(); i++) {
                int postid = Integer.parseInt(intimePost.get(i).get("postid").toString());
                postVos.add(postid);
            }
        }
        if (us != null) {
            for (int i = 0; i < us.size(); i++) {
                int postid = Integer.parseInt(us.get(i).get("postid").toString());
                postVos.add(postid);
            }
        }
        //根据postid查询帖子
        postVo = postService.findAllPostByid(postVos, paging);
        if (postVo != null) {
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
        }
        return postVo;
    }

    /**
     * 用户未登录状态下标签历史
     *
     * @param paging
     * @param type
     * @param device
     * @param labelid
     * @param postids
     * @return
     */
    private List<PostVo> userNotLoginHistoryRecordLabel(Paging<PostVo> paging, int type, String device, String labelid, String postids) {
        List<PostVo> postVo;
        List<DBObject> intimePost = null;
        List<DBObject> us = null;
        if (!postids.equals("0")) {
            List<DBObject> onlyPost = facadePost.queryOnlPostNotLoginLabelid(device, type, Integer.parseInt(postids), Integer.parseInt(labelid));
            String intime = onlyPost.get(0).get("intime").toString();
            intimePost = facadePost.queryPosyByImtimeDeviceLabel(intime, device, type, Integer.parseInt(labelid));
        } else {
            //查询用户有无历史
            int count = facadePost.userHistoryDeviceLabelCount(device, type, Integer.parseInt(labelid));
            if (count > 10) {
                us = facadePost.userRefulshListMongodbToDeviceHistoryLabelid(device, type, Integer.parseInt(labelid));
            } else {
                us = null;
            }
        }
        List<Integer> postVos = new ArrayList<>();
        if (intimePost != null) {
            for (int i = 0; i < intimePost.size(); i++) {
                int postid = Integer.parseInt(intimePost.get(i).get("postid").toString());
                postVos.add(postid);
            }
        }
        if (us != null) {
            for (int i = 0; i < us.size(); i++) {
                int postid = Integer.parseInt(us.get(i).get("postid").toString());
                postVos.add(postid);
            }
        }
        postVo = postService.findAllPostByid(postVos, paging);
        if (postVo != null) {
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
        }
        return postVo;
    }

    /**
     * 用户未登录下历史（关注 推荐 本地）
     *
     * @param paging
     * @param type
     * @param device
     * @param postids
     * @return
     */
    private List<PostVo> userNotLoginHistoryRecordThird(Paging<PostVo> paging, int type, String device, String postids) {
        List<PostVo> postVo;
        List<DBObject> intimePost = null;
        List<DBObject> us = null;
        if (!postids.equals("0")) {
            List<DBObject> onlyPost = facadePost.queryOnlPostNotLogin(device, type, Integer.parseInt(postids));
            String intime = onlyPost.get(0).get("intime").toString();
            intimePost = facadePost.queryPosyByImtimeDevice(intime, device, type);
        } else {
            if (type == 2) {
                //未登录下，【关注】下面不展示历史
                us = null;
            } else {
                int count = facadePost.userHistoryDeviceCount(device, type);
                if (count > 10) {
                    us = facadePost.userRefulshListMongodbToDevice(device, type);
                } else {
                    us = null;
                }
            }
        }
        List<Integer> postVos = new ArrayList<>();
        if (intimePost != null) {
            for (int i = 0; i < intimePost.size(); i++) {
                int postid = Integer.parseInt(intimePost.get(i).get("postid").toString());
                postVos.add(postid);
            }
        }
        if (us != null) {
            for (int i = 0; i < us.size(); i++) {
                int postid = Integer.parseInt(us.get(i).get("postid").toString());
                postVos.add(postid);
            }
        }
        //根据postid查询帖子
        postVo = postService.findAllPostByid(postVos, paging);
        if (postVo != null) {
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
        }
        return postVo;
    }

    /**
     * 用户登录下的历史记录
     *
     * @param userid
     * @param paging
     * @param type
     * @param labelid
     * @param circleid
     * @param postids
     * @param postVo
     * @param device
     * @return
     */
    private List<PostVo> userLoginHistoryRecord(String userid, Paging<PostVo> paging, int type,
                                                String labelid, String circleid, String postids,
                                                List<PostVo> postVo, String device) {
        //推荐、关注、本地
        if (StringUtil.isEmpty(circleid) && StringUtil.isEmpty(labelid)) {
            postVo = userLoginHistoryRecordThird(userid, paging, type, postids, device);
        }

        if (StringUtil.isNotEmpty(labelid)) {
            //首页标签
            postVo = userLoginHistoryRecordLabel(userid, paging, type, labelid, postids, device);
        } else if (StringUtil.isNotEmpty(circleid)) {
            //首页圈子
            postVo = userLoginHistoryRecordCircle(userid, paging, type, circleid, postids, device);
        }
        return postVo;
    }

    /**
     * 用户登录状态下圈子历史
     *
     * @param userid
     * @param paging
     * @param type
     * @param circleid
     * @param postids
     * @return
     */
    private List<PostVo> userLoginHistoryRecordCircle(String userid, Paging<PostVo> paging, int type, String circleid, String postids, String device) {
        List<PostVo> postVo;
        List<DBObject> intimePost = null;
        List<DBObject> us = null;
        if (!postids.equals("0")) {
            List<DBObject> onlyPost = facadePost.queryOnlPostNotLoginCircleid(device, type, Integer.parseInt(postids), circleid);
            String intime = onlyPost.get(0).get("intime").toString();
            intimePost = facadePost.queryPosyByImtimeDeviceCircle(intime, device, type, circleid);
        } else {
            //查询用户有无历史
            int count = facadePost.userHistoryDeviceCircleCount(device, type, circleid);
            if (count > 10) {
                us = facadePost.userRefulshListMongodbToDeviceHistory(device, type, circleid);
            } else {
                us = null;
            }
        }
        // List<DBObject> list = userRefulshListMongodbHistoryCircleid(Integer.parseInt(userid), type, circleid);
        List<DBObject> dontlike = facadePost.queryUserDontLikePost(Integer.parseInt(userid));
        List<Integer> postVos = new ArrayList<>();
        List<Integer> dontlikes = new ArrayList<>();
        if (intimePost != null) {
            for (int i = 0; i < intimePost.size(); i++) {
                int postid = Integer.parseInt(intimePost.get(i).get("postid").toString());
                postVos.add(postid);
            }
        }
        if (us != null) {
            for (int i = 0; i < us.size(); i++) {
                int postid = Integer.parseInt(us.get(i).get("postid").toString());
                postVos.add(postid);
            }
        }
        log.info(postVos + "");
        if (dontlike.size() != 0) {
            for (int i = 0; i < dontlike.size(); i++) {
                int p = Integer.parseInt(dontlike.get(i).get("postid").toString());
                dontlikes.add(p);
            }
        }
        postVos.removeAll(dontlikes);
        log.info(postVos + "***/***///////////////////////////////////////");
        //根据postid查询帖子
        postVo = postService.findAllPostByid(postVos, paging);
        if (postVo != null) {
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
            facadePost.zanIsPost(Integer.parseInt(userid), postVo);
        }
        return postVo;
    }

    /**
     * 用户登录状态下标签历史
     *
     * @param userid
     * @param paging
     * @param type
     * @param labelid
     * @param postids
     * @return
     */
    private List<PostVo> userLoginHistoryRecordLabel(String userid, Paging<PostVo> paging, int type, String labelid, String postids, String device) {
        List<PostVo> postVo;
        List<DBObject> intimePost = null;
        List<DBObject> us = null;
        if (!postids.equals("0")) {
            List<DBObject> onlyPost = facadePost.queryOnlPostNotLoginLabelid(device, type, Integer.parseInt(postids), Integer.parseInt(labelid));
            String intime = onlyPost.get(0).get("intime").toString();
            intimePost = facadePost.queryPosyByImtimeDeviceLabel(intime, device, type, Integer.parseInt(labelid));
        } else {
            //查询用户有无历史
            int count = facadePost.userHistoryDeviceLabelCount(device, type, Integer.parseInt(labelid));
            if (count > 10) {
                us = facadePost.userRefulshListMongodbToDeviceHistoryLabelid(device, type, Integer.parseInt(labelid));
            } else {
                us = null;
            }
        }
        //List<DBObject> list = userRefulshListMongodbHistory(Integer.parseInt(userid), type, Integer.parseInt(labelid));
        List<DBObject> dontlike = facadePost.queryUserDontLikePost(Integer.parseInt(userid));
        List<Integer> postVos = new ArrayList<>();
        List<Integer> dontlikes = new ArrayList<>();
        if (intimePost != null) {
            for (int i = 0; i < intimePost.size(); i++) {
                int postid = Integer.parseInt(intimePost.get(i).get("postid").toString());
                postVos.add(postid);
            }
        }
        if (us != null) {
            for (int i = 0; i < us.size(); i++) {
                int postid = Integer.parseInt(us.get(i).get("postid").toString());
                postVos.add(postid);
            }
        }
        if (dontlike.size() != 0) {
            for (int i = 0; i < dontlike.size(); i++) {
                int p = Integer.parseInt(dontlike.get(i).get("postid").toString());
                dontlikes.add(p);
            }
        }
        postVos.removeAll(dontlikes);
        log.info("***********************************************" + postVos.size());
        //根据postid查询帖子
        postVo = postService.findAllPostByid(postVos, paging);
        if (postVo != null) {
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
            facadePost.zanIsPost(Integer.parseInt(userid), postVo);
        }
        return postVo;
    }

    /**
     * 登录状态下历史记录（推荐  本地  关注）
     *
     * @param userid
     * @param paging
     * @param type
     * @param postids 刚刚刷新过后的最后一条postid，即10条中热度值最小的一条，也是10条中插入mongoDB时间最晚的一条
     * @param device
     * @return
     */
    private List<PostVo> userLoginHistoryRecordThird(String userid, Paging<PostVo> paging, int type, String postids, String device) {
        List<PostVo> postVo;
        List<DBObject> intimePost = null;
        List<DBObject> us = null;
        if (!postids.equals("0")) {
            //【该分支正确】
            //postid传值的情况下
            List<DBObject> onlyPost = facadePost.queryOnlPostNotLogin(device, type, Integer.parseInt(postids));
            String intime = onlyPost.get(0).get("intime").toString();
            //查出在这个时间之前的用户浏览历史
            intimePost = facadePost.queryPosyByImtimeDevice(intime, device, type);
        } else {

            if (type == 2) {
                //【关注】下面的历史
                int count = facadePost.userHistoryCount(Integer.valueOf(userid), type);
                if (count > 0) {
                    //如果是老用户，则查询关注-用户浏览记录
                    us = facadePost.queryUserViewRecordByUseridTypeDevice(Integer.valueOf(userid), type, device);
                } else {
                    us = null;
                }
            } else {
                /**
                 * 【推荐】和【本地】的历史
                 * 下面两种情况，都是用户手机无缓存，所以客户端传0
                 */
                int count = facadePost.userHistoryDeviceCount(device, type);
                if (count > 10) {
                    /**
                     * 【场景】：老用户卸载重装APP，进入app首页-关注，查看历史
                     * 解决方法：1 从第11条开始查询。
                     *          2 正常传入postid, 即刚刚刷新过后的最后一条postid，即10条中热度值最小的一条，也是10条中插入mongoDB时间最晚的一条
                     *          目前选择第2种方法，客户端调第二页。
                     */
                    us = facadePost.userRefulshListMongodbToDevice(device, type);
                } else {
                    /**
                     * 【场景】:新用户刚下载注册APP。
                     * 用户浏览小于等于10条，无历史记录。
                     */
                    us = null;
                }
            }

        }
        List<DBObject> dontlike = facadePost.queryUserDontLikePost(Integer.parseInt(userid));
        List<Integer> postVos = new ArrayList<>();
        List<Integer> dontlikes = new ArrayList<>();
        //处理的是postid != 0的情况
        if (intimePost != null) {
            for (int i = 0; i < intimePost.size(); i++) {
                int postid = Integer.parseInt(intimePost.get(i).get("postid").toString());
                postVos.add(postid);
            }
        }
        //处理的是postid == 0的情况
        if (us != null) {
            for (int i = 0; i < us.size(); i++) {
                int postid = Integer.parseInt(us.get(i).get("postid").toString());  //2618
                postVos.add(postid);
            }
        }
        //封装了不喜欢帖子id集合
        if (dontlike.size() != 0) {
            for (int i = 0; i < dontlike.size(); i++) {
                int p = Integer.parseInt(dontlike.get(i).get("postid").toString());
                dontlikes.add(p);
            }
        }
        //从浏览历史中过滤掉用户不喜欢的帖子id
        postVos.removeAll(dontlikes);
        //根据postid查询帖子
        postVo = postService.findAllPostByid(postVos, paging);
        log.info(postVos.size() + ";;;;;;;;;;;;;;;;;;;;;;;;;;");
        if (postVo != null) {
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
            facadePost.zanIsPost(Integer.parseInt(userid), postVo);
        }
        return postVo;
    }


}

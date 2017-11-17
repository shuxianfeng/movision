package com.movision.facade.index;

import com.mongodb.*;
import com.movision.facade.address.AddressFacade;
import com.movision.facade.paging.PageFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.opularSearchTerms.service.OpularSearchTermsService;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postLabel.service.PostLabelService;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecordVo;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhanglei
 * @Date 2017/11/17 10:28
 * 修改的新接口
 */
@Service
public class FacadePostUpdate {
    private static Logger log = LoggerFactory.getLogger(FacadePostUpdate.class);

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
    @Autowired
    private PostLabelService postLabelService;

    /**
     * 推荐
     *
     * @param userid
     * @param
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
                //list = NotLoginretuenList(list, 1, device, -1);
            }
            list = NotLoginretuenList(list, 1, device, -1);
            return list;
        } else {
            //已登录
            if (alllist != null) {
                if (count < 1000) {
                    //mongodb的里面的刷新记录小于1000条记录的时候进行用户分析
                    list = userAnalysisSmall(userid, alllist, posts, device);
                    if (list != null) return list;
                } else if (count >= 1000) {
                    //mongodb的里面的刷新记录大于等于1000条记录的时候进行用户分析
                    list = userAnalysisBig(userid, posts, device);
                }
            }
        }
        return list;
    }

    /* public List recommendPost(String userid, String device) {
        List<PostVo> list = null;
        List<PostVo> alllist = postService.findAllPostListHeat();//查询所有帖子
        List<PostVo> posts = new ArrayList<>();
        List<DBObject> listmongodb = null;
        List<PostVo> c1=null;
        List<PostVo> c2=null;
        List<PostVo> c3=null;
        List<PostVo> other=null;
        if (userid == null) {
            //未登录
            list = postService.findAllPostHeatValue();//根据热度值排序查询帖子
            listmongodb = userRefulshListMongodbToDevice(device, 1);//用户有没有看过
            if (listmongodb.size() != 0) {
                for (int j = 0; j < listmongodb.size(); j++) {
                    PostVo post = new PostVo();
                    post.setId(Integer.parseInt(listmongodb.get(j).get("postid").toString()));
                    posts.add(post);//把mongodb转为post实体
                }
                list.removeAll(posts);
            }
            list = NotLoginretuenList(list, 1, device, -1);
            return list;
        } else {
            int circle1=0;
            int circle2=0;
            int circle3=0;
            //已登录
            //如果登录人是分析表中的用户
            //查询用户行为记录表存在的就按分析后的推荐不存在的就按热度从高到底没读过的推给他
            //查询用户记录表中的数据
            List<UserBehavior> userBehaviors=userBehaviorService.findAllUserBehavior(Integer.parseInt(userid));
           // listmongodb = userRefulshListMongodbToDevice(device, 1);//用户有没有看过
            List<PostVo> groupPost=new ArrayList<>();
            List<Integer> clist= new ArrayList<>();
            if(userBehaviors.size()!=0){
                for (int i=0;i<userBehaviors.size();i++){
                   circle1=userBehaviors.get(i).getCircle1();
                   circle2=userBehaviors.get(i).getCircle2();
                   circle3=userBehaviors.get(i).getCircle3();
                   if(circle1!=0) {
                       clist.add(circle1);
                   }
                   if(circle2!=0) {
                       clist.add(circle2);
                   }
                   if(circle3!=0) {
                       clist.add(circle3);
                   }
                }
                if(clist.size()==3){
                    c1=postService.findAllPostCrile(circle1);
                    c2=postService.findAllPostCrile(circle2);
                    c3=postService.findAllPostCrile(circle3);
                    //其他圈子
                    other=postService.findAllNotCircle(clist);
                    listmongodb=userRefulshListMongodbHistoryCircleid(Integer.parseInt(userid),1);
                    if (listmongodb.size() != 0) {
                        for (int j = 0; j < listmongodb.size(); j++) {
                            PostVo post = new PostVo();
                            post.setId(Integer.parseInt(listmongodb.get(j).get("postid").toString()));
                            posts.add(post);//把mongodb转为post实体
                        }
                        c1.removeAll(posts);
                        c2.removeAll(posts);
                        c3.removeAll(posts);
                        other.removeAll(posts);
                    }
                    if(c1.size()>=4&&c2.size()>=2&&c3.size()>=1&&other.size()>=3) {
                        list = pageFacade.getPageList(c1, 1, 4);
                        groupPost.addAll(list);
                        list = pageFacade.getPageList(c2, 1, 2);
                        groupPost.addAll(list);
                        list = pageFacade.getPageList(c3, 1, 1);
                        groupPost.addAll(list);
                        list = pageFacade.getPageList(other, 1, 3);
                        groupPost.addAll(list);
                        list = retuenList(groupPost, userid, 1, device, -1);
                        return list;
                    }else if(c1.size()<4||c2.size()<2||c3.size()<1||other.size()<3){
                        if(c1.size()<4){
                            list=pageFacade.getPageList(c1,1,c1.size());
                            if(list!=null) {
                                groupPost.addAll(list);
                            }
                            int c1Size=c1.size();
                            int cha=4-c1Size;
                            if(c2.size()>=2+cha){
                                list=pageFacade.getPageList(c2,1,2+cha);
                                groupPost.addAll(list);
                                list=pageFacade.getPageList(c3,1,1);
                                if(list!=null) {
                                    groupPost.addAll(list);
                                    list=pageFacade.getPageList(other,1,3);
                                    groupPost.addAll(list);
                                    list = retuenList(groupPost, userid, 1, device, -1);
                                    return list;
                                }else {
                                    list=pageFacade.getPageList(other,1,4);
                                    groupPost.addAll(list);
                                    list = retuenList(groupPost, userid, 1, device, -1);
                                    return list;
                                }
                            }else if(c2.size()<2+cha){
                                list=pageFacade.getPageList(c2,1,c2.size());
                                if(list!=null) {
                                    groupPost.addAll(list);
                                    int c2Size=c2.size();
                                    int c2cha=2+cha-c2Size;
                                    if(c3.size()>=1+c2cha){
                                        list=pageFacade.getPageList(c3,1,1+c2cha);
                                        groupPost.addAll(list);
                                        list=pageFacade.getPageList(other,1,3);
                                        if(list!=null) {
                                            groupPost.addAll(list);
                                        }else {
                                            list=pageFacade.getPageList(other,1,other.size());
                                            groupPost.addAll(list);
                                        }
                                    }else if(c3.size()<1+c2cha){
                                        int c3Size=c3.size();
                                        int c3cha=1+c2cha-c3Size;
                                        if(other.size()>=3+c3cha){
                                            list=pageFacade.getPageList(other,1,3+c3cha);
                                            groupPost.addAll(list);
                                        }else {
                                            list=pageFacade.getPageList(other,1,other.size());
                                            if(list!=null) {
                                                groupPost.addAll(list);
                                            }
                                        }
                                    }
                                }else {
                                    int c2Size=c2.size();
                                    int c2cha=2+cha-c2Size;
                                    if(c3.size()>=1+c2cha){
                                        list=pageFacade.getPageList(c3,1,1+c2cha);
                                        groupPost.addAll(list);
                                        list=pageFacade.getPageList(other,1,3);
                                        if(list!=null) {
                                            groupPost.addAll(list);
                                        }else {
                                            list=pageFacade.getPageList(other,1,other.size());
                                            groupPost.addAll(list);
                                        }
                                    }else if(c3.size()<1+c2cha){
                                        list=pageFacade.getPageList(c3,1,c3.size());
                                        if(list!=null) {
                                            groupPost.addAll(list);
                                        }
                                        int c3Size=c3.size();
                                        int c3cha=1+c2cha-c3Size;
                                        if(other.size()>=3+c3cha){
                                            list=pageFacade.getPageList(other,1,3+c3cha);
                                            groupPost.addAll(list);
                                        }else {
                                            list=pageFacade.getPageList(other,1,other.size());
                                            if(list!=null) {
                                                groupPost.addAll(list);
                                            }
                                        }
                                    }
                                }
                            }
                            list = retuenList(groupPost, userid, 1, device, -1);
                            return list;
                        }else  if(c2.size()<2){
                            list=pageFacade.getPageList(c1,1,4);
                            groupPost.addAll(list);
                            list=pageFacade.getPageList(c2,1,c2.size());
                            if(list!=null) {
                                groupPost.addAll(list);
                            }
                            int c2Size=c2.size();
                            int c2cha=2-c2Size;
                            if(c3.size()>=1+c2cha){
                                list=pageFacade.getPageList(c3,1,1+c2cha);
                                groupPost.addAll(list);
                                list=pageFacade.getPageList(other,1,3);
                                groupPost.addAll(list);
                            }else if(c3.size()<1+c2cha){
                                list=pageFacade.getPageList(c3,1,c3.size());
                                if(list!=null) {
                                    groupPost.addAll(list);
                                }
                                int c3size=c3.size();
                                int  c3cha=1+c2cha-c3size;
                                if(other.size()>=3+c3cha){
                                    list=pageFacade.getPageList(other,1,3+c3cha);
                                    groupPost.addAll(list);
                                }else {
                                    list=pageFacade.getPageList(other,1,other.size());
                                    if(list!=null) {
                                        groupPost.addAll(list);
                                    }
                                }
                            }
                            list = retuenList(groupPost, userid, 1, device, -1);
                            return list;
                        }else if(c3.size()<1){
                            list=pageFacade.getPageList(c1,1,4);
                            groupPost.addAll(list);
                            list=pageFacade.getPageList(c2,1,2);
                            groupPost.addAll(list);
                            if(other.size()>=4) {
                                list = pageFacade.getPageList(other, 1, 4);
                                groupPost.addAll(list);
                            }else {
                                list=pageFacade.getPageList(other,1,other.size());
                                if(list!=null) {
                                    groupPost.addAll(list);
                                }
                            }
                            list = retuenList(groupPost, userid, 1, device, -1);
                            return list;
                        }else if(other.size()<3){
                            list=pageFacade.getPageList(c1,1,4);
                            groupPost.addAll(list);
                            list=pageFacade.getPageList(c2,1,2);
                            groupPost.addAll(list);
                            list=pageFacade.getPageList(c3,1,1);
                            groupPost.addAll(list);
                            list=pageFacade.getPageList(other,1,other.size());
                            if(list!=null) {
                                groupPost.addAll(list);
                            }
                            list = retuenList(groupPost, userid, 1, device, -1);
                            return list;
                        }
                    }
                 }
                if(clist.size()==2){
                    c1=postService.findAllPostCrile(circle1);
                    c2=postService.findAllPostCrile(circle2);
                     //其他圈子
                    other=postService.findAllNotCircle(clist);
                    listmongodb=userRefulshListMongodbHistoryCircleid(Integer.parseInt(userid),1);
                    if (listmongodb.size() != 0) {
                        for (int j = 0; j < listmongodb.size(); j++) {
                            PostVo post = new PostVo();
                            post.setId(Integer.parseInt(listmongodb.get(j).get("postid").toString()));
                            posts.add(post);//把mongodb转为post实体
                        }
                        c1.removeAll(posts);
                        c2.removeAll(posts);
                        other.removeAll(posts);
                    }
                    if(c1.size()>=4&&c2.size()>=2&&other.size()>=4) {
                        list = pageFacade.getPageList(c1, 1, 4);
                        groupPost.addAll(list);
                        list = pageFacade.getPageList(c2, 1, 2);
                        groupPost.addAll(list);
                        list = pageFacade.getPageList(other, 1, 4);
                        groupPost.addAll(list);
                        list = retuenList(groupPost, userid, 1, device, -1);
                        return list;
                    }else if(c1.size()<4){
                        list=pageFacade.getPageList(c1,1,c1.size());
                        if(list!=null) {
                            groupPost.addAll(list);
                        }
                        int c1Size=c1.size();
                        int cha=4-c1Size;
                        if(c2.size()>=2+cha){
                            list=pageFacade.getPageList(c2,1,2+cha);
                            groupPost.addAll(list);
                            list=pageFacade.getPageList(other,1,4);
                            if(list!=null) {
                                groupPost.addAll(list);
                            }
                        }else if(c2.size()<2+cha){
                            list=pageFacade.getPageList(c2,1,c2.size());
                            if(list!=null) {
                                groupPost.addAll(list);
                            }
                            int c2Size=c2.size();
                            int c2cha=2+cha-c2Size;
                            if(other.size()>=4+c2cha){
                                    list=pageFacade.getPageList(other,1,4+c2cha);
                                    groupPost.addAll(list);
                              }else {
                                    list=pageFacade.getPageList(other,1,other.size());
                                    if(list!=null) {
                                        groupPost.addAll(list);
                              }
                            }
                         }
                        list = retuenList(groupPost, userid, 1, device, -1);
                        return list;
                    }else if(c2.size()<2){
                        list=pageFacade.getPageList(c1,1,4);
                        groupPost.addAll(list);
                        if(c2.size()<2){
                            list=pageFacade.getPageList(c2,1,c2.size());
                            if(list!=null) {
                                groupPost.addAll(list);
                            }
                            int c2size=c2.size();
                            int c2cha=2-c2size;
                            if(other.size()>=4+c2cha){
                                list=pageFacade.getPageList(other,1,4+c2cha);
                                groupPost.addAll(list);
                            }else {
                                list=pageFacade.getPageList(other,1,other.size());
                                if(list!=null) {
                                    groupPost.addAll(list);
                                }
                            }
                        }
                        list = retuenList(groupPost, userid, 1, device, -1);
                        return list;
                    }else if(other.size()<4){
                        list=pageFacade.getPageList(c1,1,4);
                        groupPost.addAll(list);
                        list=pageFacade.getPageList(c2,1,2);
                        groupPost.addAll(list);
                        list=pageFacade.getPageList(other,1,other.size());
                        if (list!=null) {
                            groupPost.addAll(list);
                        }
                        list = retuenList(groupPost, userid, 1, device, -1);
                        return list;
                    }
                }
                if(clist.size()==1){
                    c1=postService.findAllPostCrile(circle1);
                     //其他圈子
                    other=postService.findAllNotCircle(clist);
                    listmongodb=userRefulshListMongodbHistoryCircleid(Integer.parseInt(userid),1);
                    if (listmongodb.size() != 0) {
                        for (int j = 0; j < listmongodb.size(); j++) {
                            PostVo post = new PostVo();
                            post.setId(Integer.parseInt(listmongodb.get(j).get("postid").toString()));
                            posts.add(post);//把mongodb转为post实体
                        }
                        c1.removeAll(posts);
                        other.removeAll(posts);
                    }
                    if(c1.size()>=4&&other.size()>=6) {
                        list = pageFacade.getPageList(c1, 1, 4);
                        groupPost.addAll(list);
                        list = pageFacade.getPageList(other, 1, 6);
                        groupPost.addAll(list);
                        list = retuenList(groupPost, userid, 1, device, -1);
                        return list;
                    }else  if(c1.size()<4||other.size()<6){
                          if(c1.size()<4){
                            list=pageFacade.getPageList(c1,1,c1.size());
                            if(list!=null) {
                                groupPost.addAll(list);
                            }
                            int c1size=c1.size();
                            int cha=4-c1size;
                            if(other.size()>=6+cha) {
                                list = pageFacade.getPageList(other, 1, 6 + cha);
                                groupPost.addAll(list);
                            }else {
                                list=pageFacade.getPageList(other,1,other.size());
                                if(list!=null) {
                                    groupPost.addAll(list);
                                }
                            }
                            list = retuenList(groupPost, userid, 1, device, -1);
                            return list;
                        }else  if(other.size()<6){
                              list=pageFacade.getPageList(c1,1,4);
                              groupPost.addAll(list);
                              list=pageFacade.getPageList(other,1,other.size());
                              if(list!=null) {
                                  groupPost.addAll(list);
                              }
                              list = retuenList(groupPost, userid, 1, device, -1);
                              return list;
                        }
                    }
                }
             }else {
                if (alllist != null) {
                    listmongodb = userRefulshListMongodbToDevice(device, 1);//用户有没有看过
                    if (listmongodb.size() != 0) {
                        for (int j = 0; j < listmongodb.size(); j++) {
                            PostVo post = new PostVo();
                            post.setId(Integer.parseInt(listmongodb.get(j).get("postid").toString()));
                            posts.add(post);//把mongodb转为post实体
                        }
                        list.removeAll(posts);
                        list = retuenList(list, userid,1, device, -1);
                    }else {
                        //登录情况下但是mongodb里面没有刷新记录
                        list = postService.findAllPostHeatValue();
                        list = retuenList(list, userid, 1, device, -1);
                        return list;
                    }
                    return list;
                }
            }
        }
        return list;
    }*/

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
        //listmongodb = userRefulshListMongodb(Integer.parseInt(userid), 1);//查询用户刷新列表
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
        //listmongodba = userRefulshListMongodb(Integer.parseInt(userid), 1);//查询用户刷新列表
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
     * 本地
     *
     * @param userid
     * @param
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
     * 圈子
     *
     * @param userid
     * @param
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
                //  list = NotLoginretuenList(list, 4, device, -1);
            }
            list = NotLoginretuenList(list, 4, device, -1);
            return list;
        } else {
            // listmongodba = userRefulshListMongodbHistoryCircleid(Integer.parseInt(userid), 4, String.valueOf(circleid));//用户有没有看过
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
     * 关注
     *
     * @return
     */
    public List followPost(String userid, String device) {
        List<PostVo> list = null;
        if (userid != null) {
            List<DBObject> listmongodba = null;
            List<PostVo> posts = new ArrayList<>();
            List<PostVo> crileidPost = new ArrayList<>();
            List<PostVo> userPost = new ArrayList<>();
            List<PostVo> labelPost = new ArrayList<>();
            listmongodba = facadePost.userRefulshListMongodbToDevice(device, 2);//用户有没有看过
            List<Integer> followCricle = postService.queryFollowCricle(Integer.parseInt(userid));//查询用户关注的圈子
            List<Integer> followUsers = postService.queryFollowUser(Integer.parseInt(userid));//用户关注的作者
            List<Integer> followLabel = postLabelService.labelId(Integer.parseInt(userid));//用户关注标签
            if (followCricle.size() != 0 || followUsers.size() != 0 || followLabel.size() != 0) {
                //根据圈子查询所有帖子
                if (followCricle.size() != 0) {
                    crileidPost = postService.queryPostListByIds(followCricle);
                }
                //根据作者查询所有帖子
                if (followUsers.size() != 0) {
                    userPost = postService.queryUserListByIds(followUsers);
                    if (crileidPost != null) {
                        crileidPost.addAll(userPost);
                    }
                }
                if (followLabel.size() != 0) {
                    labelPost = postService.queryLabelListByIds(followLabel);
                    if (labelPost.size() != 0) {
                        crileidPost.addAll(labelPost);
                    }
                }
                Set<PostVo> linkedHashSet = new LinkedHashSet<PostVo>(crileidPost);
                crileidPost = new ArrayList<PostVo>(linkedHashSet);
                ComparatorChain chain = new ComparatorChain();
                chain.addComparator(new BeanComparator("heatvalue"), true);//true,fase正序反序
                Collections.sort(crileidPost, chain);
                if (listmongodba.size() != 0) {//刷新有记录
                    for (int j = 0; j < listmongodba.size(); j++) {
                        PostVo post = new PostVo();
                        post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                        posts.add(post);//把mongodb转为post实体
                    }
                    crileidPost.removeAll(posts);//过滤掉看过的帖子crileidPost就是剩下的帖子
                    list = retuenList(crileidPost, userid, 2, device, -1);
                } else {
                    //list = postService.findAllPostHeatValue();//根据热度值排序查询帖子
                    list = retuenList(crileidPost, userid, 2, device, -1);
                    return list;
                }
            }
        }
        return list;
    }

    /**
     * 标签帖子
     *
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
                //list = NotLoginretuenList(list, 5, device, labelid);
            }
            list = NotLoginretuenList(list, 5, device, labelid);
            return list;
        } else {
            // listmongodba = userRefulshListMongodb(Integer.parseInt(userid), 5);//用户有没有看过
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
            //facadePost.findUser(list);
            facadePost.findPostLabel(list);    //查询帖子的标签（不好合并）
            facadePost.findHotComment(list);   //查询帖子的最热评论（不好合并）
            //facadePost.countView(list);
            // facadePost.findAllCircleName(list);
            facadePost.insertmongo(list, userid, type, device, labelid);   //刷新出来的帖子，依次插入mongoDB中
            facadePost.zanIsPost(Integer.parseInt(userid), list);
            //findAllReturn(list, userid);  //查询一些必要字段
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
            //facadePost.findUser(list); //根据userid查询作者信息
            facadePost.findPostLabel(list);    //根据postid查询标签信息
            facadePost.findHotComment(list);   //根据postid查询所有评论
            //facadePost.countView(list);    //根据postid查询帖子的浏览量
            //facadePost.findAllCircleName(list);
            facadePost.insertmongo(list, "", type, device, labelid);
            //findAllReturn(list, null);  //查询一些必要字段
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
     * 用户刷新的历史记录列表
     *
     * @param userid
     * @return
     */
    public List<PostVo> userReflushHishtoryRecord_1117(String userid, Paging<PostVo> paging, int type, String device, String labelid, String circleid, String postids) {
        log.warn("首页历史接口的传参postid=" + postids);
        log.warn("首页历史接口的传参pageNo=" + paging.getCurPage());
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
        //  List<DBObject> list = userRefulshListMongodbToDeviceHistory(device, type, circleid);
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
            //facadePost.findUser(postVo);
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
            //facadePost. countView(postVo);
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
        //List<DBObject> list = userRefulshListMongodbToDeviceHistoryLabelid(device, type, Integer.parseInt(labelid));
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
            //facadePost.findUser(postVo);
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
            //facadePost.countView(postVo);
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
            //查询用户有无历史
            int count = facadePost.userHistoryDeviceCount(device, type);
            if (count > 10) {
                us = facadePost.userRefulshListMongodbToDevice(device, type);
            } else {
                us = null;
            }
        }
        //  List<DBObject> list = userRefulshListMongodbToDevice(device, type);
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
            //facadePost.findUser(postVo);
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
            //facadePost.countView(postVo);
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
            //facadePost.findUser(postVo);
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
            //facadePost.countView(postVo);
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
            //facadePost.findUser(postVo);
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
            //facadePost.countView(postVo);
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
            //如果用户刷完了帖子，退出再进来 调用历史记录
            //。这种情况下，用户手机无缓存，所以传0
            //先查询用户有无历史
            int count = facadePost.userHistoryDeviceCount(device, type);
            if (count > 10) {
                //情况一：用户刷新之后再卸载APP,再重装APP，本地缓存被清除. 这时候查询出刚刚刷新的帖子。
                //【有问题，查刚刚刷新的数据中热度最大的那个postid】
                // 解决方法：从第11条开始查询。
                us = facadePost.userRefulshListMongodbToDevice(device, type);
            } else {
                //情况二：新用户，只是展示第一批刷新的数据，无历史记录。用户浏览小于等于10条。
                us = null;
            }
        }
        // List<DBObject> list = userRefulshListMongodb(Integer.parseInt(userid), type);
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
            //facadePost.findUser(postVo);
            facadePost.findPostLabel(postVo);
            facadePost.findHotComment(postVo);
            //facadePost.countView(postVo);
            facadePost.zanIsPost(Integer.parseInt(userid), postVo);
        }
        return postVo;
    }


}

package com.movision.facade.index;

import com.mongodb.*;
import com.movision.facade.address.AddressFacade;
import com.movision.facade.paging.PageFacade;
import com.movision.mybatis.opularSearchTerms.service.OpularSearchTermsService;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postLabel.service.PostLabelService;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecordVo;
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

}

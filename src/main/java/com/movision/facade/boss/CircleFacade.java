package com.movision.facade.boss;

import com.movision.common.Response;
import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.entity.CategoryVo;
import com.movision.mybatis.category.entity.CircleAndCircle;
import com.movision.mybatis.category.service.CategoryService;
import com.movision.mybatis.circle.entity.*;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.manager.entity.Manager;
import com.movision.mybatis.manager.service.ManagerServcie;
import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.share.service.SharesService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author zhurui
 * @Date 2017/2/8 17:56
 */
@Service
public class CircleFacade {
    Logger logger = LoggerFactory.getLogger(CircleFacade.class);
    @Value("#{configProperties['bannerimg.domain']}")
    private String imgdomain;

    @Value("#{configProperties['circlecategory.domain']}")
    private String categoryurl;

    @Autowired
    CircleService circleService;
    @Autowired
    PostService postService;
     @Autowired
    UserService userService;

    @Autowired
    SharesService sharesService;

    @Autowired
    RewardedService rewardedService;

    @Autowired
    AccusationService accusationService;

    @Autowired
    ManagerServcie managerService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    MovisionOssClient movisionOssClient;

    /**
     * 圈子首页列表查询
     *
     * @return
     */
    public List<CircleIndexList> queryCircleByList() {
        String str = null;
        Map tm = new HashedMap();
        tm.put("categoryid", null);
        List<CircleIndexList> circlenum = circleService.queryListByCircleCategory(tm);//查询圈子所有分类
        for (int i = 0; i < circlenum.size(); i++) {
            /////////////////////分类列表////////////////////////
            Map map = new HashedMap();
            map.put("type", circlenum.get(i).getCategory());
            List<CircleVo> listt = circleService.queryCircleByLikeList(map);//获取圈子分类列表的圈子列表
            List<CircleVo> circleVoslist = new ArrayList<>();
            List<User> adminlist = new ArrayList();//用于存储类型中所有圈子的管理员
            List username = new ArrayList();//用于存放类型中所有圈主
            int posts = 0;//总帖子数
            int follows = 0;//关注数
            int follownews = 0;//今日关注
            int postnews = 0;//今日新增帖子
            int isessences = 0;//精贴数
            for (int e = 0; e < listt.size(); e++) {
                CircleVo vo = new CircleVo();
                Integer circleid = listt.get(e).getId();
                List<User> userslist = userService.queryCircleManagerList(circleid);//查询出圈子管理员列表

                if (userslist != null) {
                    for (int u = 0; u < userslist.size(); u++) {
                        if (userslist.get(u).getNickname() == null) {
                            userslist.get(u).setNickname("用户" + userslist.get(u).getPhone().substring(7));
                        }
                        adminlist.add(userslist.get(u));//把圈子的管理员遍历出临时存放
                    }
                }
                Map m = new HashedMap();
                m.put("nickname", listt.get(e).getCirclename());
                m.put("userid", listt.get(e).getUserid());
                username.add(m);//把圈子的圈主遍历出来临时存放
                posts += listt.get(e).getPostnum();//圈子帖子和
                postnews += listt.get(e).getPostnewnum();//新增帖子和
                follows += listt.get(e).getFollownum();//关注和
                follownews += listt.get(e).getFollownewnum();//新增关注和
                isessences += listt.get(e).getIsessencenum();//精贴和
                circlenum.get(i).setCirclemanagerlist(adminlist);
                circlenum.get(i).setCirclemaster(username);
                circlenum.get(i).setPostnum(posts);
                circlenum.get(i).setPostnewnum(postnews);
                circlenum.get(i).setFollownum(follows);
                circlenum.get(i).setFollownewnum(follownews);
                circlenum.get(i).setIsessencenum(isessences);


                vo.setPostnum(listt.get(e).getPostnum());//帖子数量
                vo.setPostnewnum(listt.get(e).getPostnewnum());//今日新增帖子数量
                vo.setIsessencenum(listt.get(e).getIsessencenum());//精贴数量
                vo.setOrderid(listt.get(e).getOrderid());//发现推荐
                vo.setFollownum(listt.get(e).getFollownum());//关注数
                vo.setFollownewnum(listt.get(e).getFollownewnum());//今日新增关注数
                vo.setId(listt.get(e).getId());//圈子id
                vo.setName(listt.get(e).getName());//圈子名称
                vo.setCategory(listt.get(e).getCategory());//圈子分类
                vo.setCategoryname(listt.get(e).getCategoryname());//圈子分类名
                vo.setCirclename(listt.get(e).getCirclename());//圈主
                vo.setCategorylevel(listt.get(e).getCategorylevel().toString());//判断是否为大V
                vo.setCirclemanagerlist(userslist);//圈子管理员列表
                vo.setSupportnum(listt.get(e).getSupportnum());//支持数
                vo.setStatus(listt.get(e).getStatus());//圈子状态：0 待审核 1 审核通过 2 审核不通过
                vo.setIsrecommend(listt.get(e).getIsrecommend());//推荐首页
                vo.setIsdiscover(listt.get(e).getIsdiscover());//推荐发现
                vo.setCreatetime(listt.get(e).getCreatetime());//圈子创建时间
                circleVoslist.add(vo);
            }
            circlenum.get(i).setClassify(circleVoslist);
        }
        return circlenum;
    }


    /**
     * 后台管理--查询精贴列表
     * @param pager
     * @return
     */
    public List<PostList> queryPostIsessenceByList(String cirid, String categoryid, Paging<PostList> pager) {
        Map map = new HashedMap();
        map.put("circleid", cirid);
        map.put("categoryid", categoryid);
        return postService.queryPostIsessenceByList(map, pager);
    }


    /**
     * 后台管理-圈子列表-查询圈子可推荐到发现页的排序
     *
     * @return
     */
    public Map<String, List> queryDiscoverList() {
        List<Circle> list = circleService.queryDiscoverList();
        List<Integer> tem = new ArrayList<>();
        Map<String, List> map = new HashedMap();
        for (int i = 1; i < 10; i++) {
            tem.add(i);
        }
        for (int k = 0; k < list.size(); k++) {//用于返回推荐页排序的剩余位置
            for (int l = 0; l < tem.size(); l++) {
                if (list.get(k).getOrderid() == tem.get(l)) {
                    tem.remove(l);
                }
            }
        }
        map.put("resault", tem);
        return map;
    }

    /**
     * 圈子推荐到发现页
     *
     * @param circleid
     * @return
     */
    public Map<String, Integer> updateDiscover(String circleid, String orderid) {
        Integer discover = circleService.queryCircleDiscover(circleid);//查询圈子是否推荐发现页
        Map<String, Integer> map = new HashedMap();
        Map<String, Integer> spread = new HashedMap();
        spread.put("circleid", Integer.parseInt(circleid));
        spread.put("orderid", Integer.parseInt(orderid));
        if (discover == 0) {
            Integer i = circleService.updateDiscover(spread);
            map.put("resault", i);
        } else {
            Integer n = circleService.updateDiscoverDel(circleid);
            map.put("resault", n);
        }
        return map;
    }

    /**
     * 圈子推荐到首页
     *
     * @param circleid
     * @return
     */
    public Map<String, Integer> updateCircleIndex(String circleid) {
        Integer recommend = circleService.queryCircleRecommendIndex(circleid);
        Map<String, Integer> map = new HashedMap();
        if (recommend == 0) {
            Integer l = circleService.updateCircleIndex(Integer.parseInt(circleid));
            map.put("resault", l);
        } else {
            Integer n = circleService.updateCircleIndexDel(circleid);
            map.put("resault", 2);
        }
        return map;
    }

    /**
     * 查看圈子详情
     * @param circleid
     * @return
     */
    public CircleDetails quryCircleDetails(String circleid) {
        return circleService.quryCircleDetails(Integer.parseInt(circleid));
    }


    /**
     * 圈子编辑
     *
     * @param id
     * @param name
     * @param category
     * @param circleadmin
     * @param photo
     * @param introduction
     * @param erweima
     * @param status
     * @param isrecommend
     * @param orderid
     * @param permission
     * @return
     */
    public Map<String, Integer> updateCircle(String id, String name, String category, String circlemanid,
                                             String circleadmin, String photo, String introduction,
                                             String erweima, String status, String isrecommend, String orderid, String permission) {
        CircleDetails circleDetails = new CircleDetails();
        Map<String, Integer> map = new HashedMap();
        Integer circleid = null;
            if (id != null) {
                circleid = Integer.parseInt(id);
                circleDetails.setId(Integer.parseInt(id));
            }
            if (name != null) {
                circleDetails.setName(name);
            }
            if (category != null) {
                circleDetails.setCategory(Integer.parseInt(category));
            }
        if (circleadmin != null && circleadmin != "") {//管理员列表
                //待定
            String[] ary = circleadmin.split(",");//以逗号分隔接收数据
                managerService.deleteManagerToCircleid(circleid);//删除圈子的所有管理员
                for (String itm : ary) {//循环添加
                    Map<String, Integer> mapd = new HashedMap();
                    mapd.put("circleid", circleid);
                    mapd.put("userid", Integer.parseInt(itm));
                    managerService.addManagerToCircleAndUserid(mapd);//添加圈子所用管理员
                }
            }
            if (circlemanid != null) {
                //查询圈主
                String pon = userService.queryUserbyPhoneByUserid(Integer.parseInt(circlemanid));
                circleDetails.setPhone(pon);
            }
            if (photo != null) {
                //savedFileName = movisionOssClient.uploadObject(photo, "img", "circle");
                circleDetails.setPhoto(photo);
            }
            if (introduction != null) {
                circleDetails.setIntroduction(introduction);
            }
            if (erweima != null) {
                circleDetails.setErweima(erweima);
            }
            if (status != null) {
                circleDetails.setStatus(Integer.parseInt(status));
            }
            if (isrecommend != null) {
                circleDetails.setIsrecommend(Integer.parseInt(isrecommend));
            }
            if (orderid != null) {
                if (Integer.parseInt(orderid) > 0) {
                    circleDetails.setIsdiscover(1);
                }
                circleDetails.setOrderid(Integer.parseInt(orderid));
            }
            if (permission != null) {
                circleDetails.setPermission(Integer.parseInt(permission));
            }
            Integer s = circleService.updateCircle(circleDetails);
            if (s == 1) {
                map.put("resault", s);
            } else {
                Integer t = 0;
                map.put("resault", t);
            }
        return map;
    }

    /**
     * 圈子编辑时数据回显
     *
     * @param circleid
     * @return
     */
    public Map<String, CircleDetails> queryCircleByShow(String circleid) {
        Map<String, CircleDetails> map = new HashedMap();
        CircleDetails circleDetails = circleService.queryCircleByShow(Integer.parseInt(circleid));//查询出圈子信息
        List<User> list = userService.queryUserByAdministratorList(Integer.parseInt(circleid));//查询出圈子管理员列表
        circleDetails.setAdmin(list);
        List<Integer> ords = circleService.queryCircleByOrderidList();//查询圈子推荐发现页排序
        List<Integer> h = new ArrayList<>();
        for (int k = 1; k < 10; k++) {
            h.add(k);
        }
        for (int i = 0; i < ords.size(); i++) {
            for (int j = 0; j < h.size(); j++) {
                if (ords.get(i) == h.get(j) && h.get(j) != circleDetails.getOrderid()) {//比较排序并返回空余排序位置
                    h.remove(j);
                }
            }
            circleDetails.setOrderids(h);
        }
        /*String nn = userService.queryUserByNicknameByAdmin(circleDetails.getUserid());
        circleDetails.setUsername(nn);*/
        map.put("resault", circleDetails);
        return map;
    }


    /**
     * 圈子添加
     *
     * @param name
     * @param category
     * @param userid
     * @param circleadmin
     * @param circlemanid
     * @param photo
     * @param introduction
     * @return
     */
    public Map<String, Integer> addCircle(String name, String category, String userid, String circleadmin,
                                          String circlemanid, String photo, String introduction) {
        CircleDetails circleDetails = new CircleDetails();
        Map<String, Integer> map = new HashedMap();
            if (name != null) {
                circleDetails.setName(name);
            }
            if (category != null) {
                circleDetails.setCategory(Integer.parseInt(category));
            }

            if (circlemanid != null) {//添加创建人
                circleDetails.setUserid(circlemanid);
            }
            if (userid != null) {
                //查询圈主手机号
                String pon = userService.queryUserbyPhoneByUserid(Integer.parseInt(userid));
                circleDetails.setPhone(pon);//设置圈主手机号
            }
            circleDetails.setCreatetime(new Date());
            if (photo != null) {
                circleDetails.setPhoto(photo);
            }
            if (introduction != null) {
                circleDetails.setIntroduction(introduction);
            }
            Integer s = circleService.insertCircle(circleDetails);
            Integer cirid = circleDetails.getId();
        if (circleadmin != null && circleadmin != "") {//管理员列表
                //待定
            String[] ary = circleadmin.split(",");//以逗号分隔接收数据
                for (String itm : ary) {//循环添加
                    Map<String, Integer> mapd = new HashedMap();
                    mapd.put("circleid", cirid);
                    mapd.put("userid", Integer.parseInt(itm));
                    managerService.addManagerToCircleAndUserid(mapd);//添加圈子所用管理员
                }
            }
            if (s == 1) {
                map.put("resault", s);
            } else {
                Integer t = 0;
                map.put("resault", t);
            }
        return map;
    }

    /**
     * 查询圈子分类
     *
     * @return
     */
    public List<Category> queryCircleTypeList() {
        List<Category> list = categoryService.queryCircleTypeList();
        return list;
    }

    /**
     * 添加圈子分类
     *
     * @param typename
     * @param discoverpageurl
     * @return
     */
    public Map<String, Integer> addCircleType(String typename, String discoverpageurl) {
        Map<String, Integer> map = new HashedMap();
        Map packaging = new HashedMap();
        packaging.put("categoryname", typename);
        packaging.put("intime", new Date());
        packaging.put("discoverpageurl", discoverpageurl);
        int i = categoryService.addCircleType(packaging);
        map.put("resault", i);
        return map;
    }

    /**
     * 回显圈子类型详情接口
     *
     * @param category
     * @return
     */
    public Category queryCircleCategory(String category) {
        return categoryService.queryCircleCategory(category);
    }

    /**
     * 编辑圈子详情接口
     *
     * @param categoryid
     * @param category
     * @param discoverpageurl
     * @return
     */
    public Map updateCircleCategory(String categoryid, String category, String discoverpageurl) {
        Map map = new HashedMap();
        map.put("categoryid", categoryid);
        map.put("categoryname", category);
        map.put("intime", new Date());
        map.put("imgurl", discoverpageurl);
        int i = categoryService.updateCircleCategory(map);
        Map m = new HashedMap();
        if (i == 1) {
            m.put("resault", i);
            return m;
        } else {
            m.put("resault", 0);
            return m;
        }
    }

    /**
     * 查询圈子下的帖子列表
     *
     * @param circleid
     * @param pager
     * @return
     */
    public List<PostList> findAllQueryCircleByPostList(String categoryid, String circleid, Paging<PostList> pager) {
        Map map = new HashedMap();
        map.put("circleid", circleid);
        map.put("categoryid", categoryid);
        return postService.findAllQueryCircleByPostList(map, pager);
    }

    /**
     * 查询圈主列表
     *
     * @param pager
     * @return
     */
    public List<User> queryCircleManList(Paging<User> pager) {
        return userService.queryCircleManList(pager);
    }


    /**
     * 根据条件查询圈子列表
     *
     * @param circle
     * @param circleman
     * @param type
     * @param begintime
     * @param endtime
     * @return
     */
    public List<CircleIndexList> queryCircleByCondition(String pai, String circle, String circleman, String type, String begintime, String endtime) {
        Map map = new HashedMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beg = null;
        Date end = null;
        if ((begintime != null && begintime != "") && (endtime != null && endtime != "")) {
            try {
                beg = format.parse(begintime);
                end = format.parse(endtime);
            } catch (ParseException e) {
                logger.error("时间格式转换异常", e);
            }
        }
        if (pai != null && pai != "") {
            map.put("pai", pai);
        }
        if (circle != null && circle != "") {
            map.put("circleid", circle);
        }
        if (type != null && type != "") {
            map.put("type", type);
        }
        if (circleman != null && circleman != "") {
            map.put("circleman", circleman);
        }
        map.put("begintime", beg);
        map.put("endtime", end);
        List<CircleIndexList> circlenum = new ArrayList<>();
        if (type == null || type == "") {
            Map tm = new HashedMap();
            type = null;
            tm.put("categoryid", type);
            circlenum = circleService.queryListByCircleCategory(tm);//查询圈子所有分类

            for (int i = 0; i < circlenum.size(); i++) {
                /////////////////////分类列表////////////////////////
                map.put("type", circlenum.get(i).getCategory());
                List<User> adminlist = new ArrayList();//用于存储类型中所有圈子的管理员
                List username = new ArrayList<>();//用于存放类型中所有圈主
                List<CircleVo> listt = circleService.queryCircleByLikeList(map);//获取圈子分类列表的圈子列表
                List<CircleVo> circleVoslist = new ArrayList<>();
                int posts = 0;//总帖子数
                int follows = 0;//关注数
                int follownews = 0;//今日关注
                int postnews = 0;//今日新增帖子
                int isessences = 0;//精贴数
                for (int e = 0; e < listt.size(); e++) {//遍历所有圈子
                    CircleVo vo = new CircleVo();
                    Integer circleid = listt.get(e).getId();
                    List<User> userslist = userService.queryCircleManagerList(circleid);//查询出圈子管理员列表

                    if (userslist != null) {
                        for (int u = 0; u < userslist.size(); u++) {
                            if (userslist.get(u).getNickname() == null) {
                                userslist.get(u).setNickname("用户" + userslist.get(u).getPhone().substring(7));
                            }
                            adminlist.add(userslist.get(u));//把圈子的管理员遍历出临时存放
                        }
                    }
                    Map m = new HashedMap();
                    m.put("nickname", listt.get(e).getCirclename());
                    m.put("userid", listt.get(e).getUserid());
                    username.add(m);//把圈子的圈主遍历出来临时存放
                    posts += listt.get(e).getPostnum();//圈子帖子和
                    postnews += listt.get(e).getPostnewnum();//新增帖子和
                    follows += listt.get(e).getFollownum();//关注和
                    follownews += listt.get(e).getFollownewnum();//新增关注和
                    isessences += listt.get(e).getIsessencenum();//精贴和
                    circlenum.get(i).setCirclemanagerlist(adminlist);
                    circlenum.get(i).setCirclemaster(username);
                    circlenum.get(i).setPostnum(posts);
                    circlenum.get(i).setPostnewnum(postnews);
                    circlenum.get(i).setFollownum(follows);
                    circlenum.get(i).setFollownewnum(follownews);
                    circlenum.get(i).setIsessencenum(isessences);

                    vo.setPostnum(listt.get(e).getPostnum());//帖子数量
                    vo.setPostnewnum(listt.get(e).getPostnewnum());//今日新增帖子数量
                    vo.setIsessencenum(listt.get(e).getIsessencenum());//精贴数量
                    vo.setOrderid(listt.get(e).getOrderid());//发现推荐
                    vo.setFollownum(listt.get(e).getFollownum());//关注数
                    vo.setFollownewnum(listt.get(e).getFollownewnum());//今日新增关注数
                    vo.setId(listt.get(e).getId());//圈子id
                    vo.setName(listt.get(e).getName());//圈子名称
                    vo.setCategory(listt.get(e).getCategory());//圈子分类
                    vo.setCategoryname(listt.get(e).getCategoryname());//圈子分类名
                    vo.setCirclename(listt.get(e).getCirclename());//圈主
                    vo.setCategorylevel(listt.get(e).getCategorylevel().toString());//判断是否为大V
                    vo.setCirclemanagerlist(userslist);//圈子管理员列表
                    vo.setSupportnum(listt.get(e).getSupportnum());//支持数
                    vo.setStatus(listt.get(e).getStatus());//圈子状态：0 待审核 1 审核通过 2 审核不通过
                    vo.setIsrecommend(listt.get(e).getIsrecommend());//推荐首页
                    vo.setIsdiscover(listt.get(e).getIsdiscover());//推荐发现
                    vo.setCreatetime(listt.get(e).getCreatetime());//圈子创建时间
                    circleVoslist.add(vo);
                }
                circlenum.get(i).setClassify(circleVoslist);
            }
            return circlenum;
        } else {
            Map tm = new HashedMap();
            tm.put("categoryid", type);
            /////////////////////分类列表////////////////////////
            circlenum = circleService.queryListByCircleCategory(tm);//查询指定圈子分类
            for (int f = 0; f < circlenum.size(); f++) {
                List<CircleVo> listt = circleService.queryCircleByLikeList(map);//获取圈子分类列表的圈子列表
                List<CircleVo> circleVoslist = new ArrayList<>();
                List<User> adminlist = new ArrayList();//用于存储类型中所有圈子的管理员
                List username = new ArrayList();//用于存放类型中所有圈主
                int posts = 0;//总帖子数
                int follows = 0;//关注数
                int follownews = 0;//今日关注
                int postnews = 0;//今日新增帖子
                int isessences = 0;//精贴数
                for (int e = 0; e < listt.size(); e++) {
                    CircleVo vo = new CircleVo();
                    Integer circleid = listt.get(e).getId();
                    List<User> userslist = userService.queryCircleManagerList(circleid);//查询出圈子管理员列表

                    if (userslist != null) {
                        for (int u = 0; u < userslist.size(); u++) {
                            if (userslist.get(u).getNickname() == null) {
                                userslist.get(u).setNickname("用户" + userslist.get(u).getPhone().substring(7));
                            }
                            adminlist.add(userslist.get(u));//把圈子的管理员遍历出临时存放
                        }
                    }
                    Map m = new HashedMap();
                    m.put("nickname", listt.get(e).getCirclename());
                    m.put("userid", listt.get(e).getUserid());
                    username.add(m);//把圈子的圈主遍历出来临时存放
                    posts += listt.get(e).getPostnum();//圈子帖子和
                    postnews += listt.get(e).getPostnewnum();//新增帖子和
                    follows += listt.get(e).getFollownum();//关注和
                    follownews += listt.get(e).getFollownewnum();//新增关注和
                    isessences += listt.get(e).getIsessencenum();//精贴和
                    circlenum.get(f).setCirclemanagerlist(adminlist);
                    circlenum.get(f).setCirclemaster(username);
                    circlenum.get(f).setPostnum(posts);
                    circlenum.get(f).setPostnewnum(postnews);
                    circlenum.get(f).setFollownum(follows);
                    circlenum.get(f).setFollownewnum(follownews);
                    circlenum.get(f).setIsessencenum(isessences);

                    vo.setPostnum(listt.get(e).getPostnum());//帖子数量
                    vo.setPostnewnum(listt.get(e).getPostnewnum());//今日新增帖子数量
                    vo.setIsessencenum(listt.get(e).getIsessencenum());//精贴数量
                    vo.setOrderid(listt.get(e).getOrderid());//发现推荐
                    vo.setFollownum(listt.get(e).getFollownum());//关注数
                    vo.setFollownewnum(listt.get(e).getFollownewnum());//今日新增关注数
                    vo.setId(listt.get(e).getId());//圈子id
                    vo.setName(listt.get(e).getName());//圈子名称
                    vo.setCategory(listt.get(e).getCategory());//圈子分类
                    vo.setCategoryname(listt.get(e).getCategoryname());//圈子分类名
                    vo.setCirclename(listt.get(e).getCirclename());//圈主
                    vo.setCategorylevel(listt.get(e).getCategorylevel().toString());//判断是否为大V
                    vo.setCirclemanagerlist(userslist);//圈子管理员列表
                    vo.setSupportnum(listt.get(e).getSupportnum());//支持数
                    vo.setStatus(listt.get(e).getStatus());//圈子状态：0 待审核 1 审核通过 2 审核不通过
                    vo.setIsrecommend(listt.get(e).getIsrecommend());//推荐首页
                    vo.setIsdiscover(listt.get(e).getIsdiscover());//推荐发现
                    vo.setCreatetime(listt.get(e).getCreatetime());//圈子创建时间
                    circleVoslist.add(vo);
                }
                circlenum.get(f).setClassify(circleVoslist);
            }

            return circlenum;
        }
    }

    /**
     * 查询待审核圈子
     *
     * @return
     */
    public List<CircleIndexList> queryCircleAwaitAudit(String pai, String circle, String circleman, String type, String begintime, String endtime) {
        //return circleService.queryCircleAwaitAudit();

        Map map = new HashedMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beg = null;
        Date end = null;
        if ((begintime != null && begintime != "") && (endtime != null && endtime != "")) {
            try {
                beg = format.parse(begintime);
                end = format.parse(endtime);
            } catch (ParseException e) {
                logger.error("时间格式转换异常", e);
            }
        }
        if (pai != null && pai != "") {
            map.put("pai", pai);
        }
        if (circle != null && circle != "") {
            map.put("circleid", circle);
        }
        if (type != null && type != "") {
            map.put("type", type);
        }
        if (circleman != null && circleman != "") {
            map.put("circleman", circleman);
        }
        map.put("begintime", beg);
        map.put("endtime", end);
        List<CircleIndexList> circlenum = new ArrayList<>();
        if (type == null || type == "") {
            Map tm = new HashedMap();
            type = null;
            tm.put("categoryid", type);
            circlenum = circleService.queryListByCircleCategory(tm);//查询圈子所有分类

            for (int i = 0; i < circlenum.size(); i++) {
                /////////////////////分类列表////////////////////////
                map.put("type", circlenum.get(i).getCategory());
                List<User> adminlist = new ArrayList();//用于存储类型中所有圈子的管理员
                List username = new ArrayList<>();//用于存放类型中所有圈主
                List<CircleVo> listt = circleService.queryCircleAwaitAudit(map);//获取圈子分类列表的圈子列表
                List<CircleVo> circleVoslist = new ArrayList<>();
                int posts = 0;//总帖子数
                int follows = 0;//关注数
                int follownews = 0;//今日关注
                int postnews = 0;//今日新增帖子
                int isessences = 0;//精贴数
                for (int e = 0; e < listt.size(); e++) {//遍历所有圈子
                    CircleVo vo = new CircleVo();
                    Integer circleid = listt.get(e).getId();
                    List<User> userslist = userService.queryCircleManagerList(circleid);//查询出圈子管理员列表

                    if (userslist != null) {
                        for (int u = 0; u < userslist.size(); u++) {
                            if (userslist.get(u).getNickname() == null) {
                                userslist.get(u).setNickname("用户" + userslist.get(u).getPhone().substring(7));
                            }
                            adminlist.add(userslist.get(u));//把圈子的管理员遍历出临时存放
                        }
                    }
                    Map m = new HashedMap();
                    m.put("nickname", listt.get(e).getCirclename());
                    m.put("userid", listt.get(e).getUserid());
                    username.add(m);//把圈子的圈主遍历出来临时存放
                    posts += listt.get(e).getPostnum();//圈子帖子和
                    postnews += listt.get(e).getPostnewnum();//新增帖子和
                    follows += listt.get(e).getFollownum();//关注和
                    follownews += listt.get(e).getFollownewnum();//新增关注和
                    isessences += listt.get(e).getIsessencenum();//精贴和
                    circlenum.get(i).setCirclemanagerlist(adminlist);
                    circlenum.get(i).setCirclemaster(username);
                    circlenum.get(i).setPostnum(posts);
                    circlenum.get(i).setPostnewnum(postnews);
                    circlenum.get(i).setFollownum(follows);
                    circlenum.get(i).setFollownewnum(follownews);
                    circlenum.get(i).setIsessencenum(isessences);

                    vo.setPostnum(listt.get(e).getPostnum());//帖子数量
                    vo.setPostnewnum(listt.get(e).getPostnewnum());//今日新增帖子数量
                    vo.setIsessencenum(listt.get(e).getIsessencenum());//精贴数量
                    vo.setOrderid(listt.get(e).getOrderid());//发现推荐
                    vo.setFollownum(listt.get(e).getFollownum());//关注数
                    vo.setFollownewnum(listt.get(e).getFollownewnum());//今日新增关注数
                    vo.setId(listt.get(e).getId());//圈子id
                    vo.setName(listt.get(e).getName());//圈子名称
                    vo.setCategory(listt.get(e).getCategory());//圈子分类
                    vo.setCategoryname(listt.get(e).getCategoryname());//圈子分类名
                    vo.setCirclename(listt.get(e).getCirclename());//圈主
                    vo.setCategorylevel(listt.get(e).getCategorylevel().toString());//判断是否为大V
                    vo.setCirclemanagerlist(userslist);//圈子管理员列表
                    vo.setSupportnum(listt.get(e).getSupportnum());//支持数
                    vo.setStatus(listt.get(e).getStatus());//圈子状态：0 待审核 1 审核通过 2 审核不通过
                    vo.setIsrecommend(listt.get(e).getIsrecommend());//推荐首页
                    vo.setIsdiscover(listt.get(e).getIsdiscover());//推荐发现
                    vo.setCreatetime(listt.get(e).getCreatetime());//圈子创建时间
                    circleVoslist.add(vo);
                }
                circlenum.get(i).setClassify(circleVoslist);
            }
            return circlenum;
        } else {
            Map tm = new HashedMap();
            tm.put("categoryid", type);
            /////////////////////分类列表////////////////////////
            circlenum = circleService.queryListByCircleCategory(tm);//查询指定圈子分类
            for (int f = 0; f < circlenum.size(); f++) {
                List<CircleVo> listt = circleService.queryCircleAwaitAudit(map);//获取圈子分类列表的圈子列表
                List<CircleVo> circleVoslist = new ArrayList<>();
                List<User> adminlist = new ArrayList();//用于存储类型中所有圈子的管理员
                List username = new ArrayList();//用于存放类型中所有圈主
                int posts = 0;//总帖子数
                int follows = 0;//关注数
                int follownews = 0;//今日关注
                int postnews = 0;//今日新增帖子
                int isessences = 0;//精贴数
                for (int e = 0; e < listt.size(); e++) {
                    CircleVo vo = new CircleVo();
                    Integer circleid = listt.get(e).getId();
                    List<User> userslist = userService.queryCircleManagerList(circleid);//查询出圈子管理员列表

                    if (userslist != null) {
                        for (int u = 0; u < userslist.size(); u++) {
                            if (userslist.get(u).getNickname() == null) {
                                userslist.get(u).setNickname("用户" + userslist.get(u).getPhone().substring(7));
                            }
                            adminlist.add(userslist.get(u));//把圈子的管理员遍历出临时存放
                        }
                    }
                    Map m = new HashedMap();
                    m.put("nickname", listt.get(e).getCirclename());
                    m.put("userid", listt.get(e).getUserid());
                    username.add(m);//把圈子的圈主遍历出来临时存放
                    posts += listt.get(e).getPostnum();//圈子帖子和
                    postnews += listt.get(e).getPostnewnum();//新增帖子和
                    follows += listt.get(e).getFollownum();//关注和
                    follownews += listt.get(e).getFollownewnum();//新增关注和
                    isessences += listt.get(e).getIsessencenum();//精贴和
                    circlenum.get(f).setCirclemanagerlist(adminlist);
                    circlenum.get(f).setCirclemaster(username);
                    circlenum.get(f).setPostnum(posts);
                    circlenum.get(f).setPostnewnum(postnews);
                    circlenum.get(f).setFollownum(follows);
                    circlenum.get(f).setFollownewnum(follownews);
                    circlenum.get(f).setIsessencenum(isessences);

                    vo.setPostnum(listt.get(e).getPostnum());//帖子数量
                    vo.setPostnewnum(listt.get(e).getPostnewnum());//今日新增帖子数量
                    vo.setIsessencenum(listt.get(e).getIsessencenum());//精贴数量
                    vo.setOrderid(listt.get(e).getOrderid());//发现推荐
                    vo.setFollownum(listt.get(e).getFollownum());//关注数
                    vo.setFollownewnum(listt.get(e).getFollownewnum());//今日新增关注数
                    vo.setId(listt.get(e).getId());//圈子id
                    vo.setName(listt.get(e).getName());//圈子名称
                    vo.setCategory(listt.get(e).getCategory());//圈子分类
                    vo.setCategoryname(listt.get(e).getCategoryname());//圈子分类名
                    vo.setCirclename(listt.get(e).getCirclename());//圈主
                    vo.setCategorylevel(listt.get(e).getCategorylevel().toString());//判断是否为大V
                    vo.setCirclemanagerlist(userslist);//圈子管理员列表
                    vo.setSupportnum(listt.get(e).getSupportnum());//支持数
                    vo.setStatus(listt.get(e).getStatus());//圈子状态：0 待审核 1 审核通过 2 审核不通过
                    vo.setIsrecommend(listt.get(e).getIsrecommend());//推荐首页
                    vo.setIsdiscover(listt.get(e).getIsdiscover());//推荐发现
                    vo.setCreatetime(listt.get(e).getCreatetime());//圈子创建时间
                    circleVoslist.add(vo);
                }
                circlenum.get(f).setClassify(circleVoslist);
            }

            return circlenum;
        }
    }

}

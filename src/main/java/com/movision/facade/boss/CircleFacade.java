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
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
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
    @Value("#{configProperties['bannerimg.domain']}")
    private String imgdomain;
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

    /**
     * 圈子首页列表查询
     *
     * @return
     */
    public List<CircleIndexList> queryCircleByList() {
        List<Integer> circlenum = circleService.queryListByCircleCategory();//查询圈子所有分类
        List<CircleIndexList> list = new ArrayList<>();
        for (int i = 0; i < circlenum.size(); i++) {
            CircleIndexList cir = new CircleIndexList();
            //查询，圈子分类，圈主，管理员列表，关注人数，今日关注人数，帖子数量，今日新增帖子，精贴数量，支持人数，创建日期
            List<User> users = circleService.queryCircleUserList(circlenum.get(i));//查询管理员列表
            List<User> circlemaster = circleService.queryCircleMan(circlenum.get(i));//圈主
            CircleVo circleVoNum = circleService.queryFollowAndNum(circlenum.get(i));//查询圈子帖子总数，精贴总数，关注总数，支持人数和时间
            cir.setCirclemaster(circlemaster);//圈主列表
            cir.setCategory(circlenum.get(i));//圈子分类
            cir.setCirclemanagerlist(users);//管理员列表
            if (circleVoNum != null) {
                cir.setPostnum(circleVoNum.getPostnum());//帖子数量
                cir.setPostnewnum(circleVoNum.getPostnewnum());//今日新增帖子数量
                cir.setIsessencenum(circleVoNum.getIsessencenum());//精贴数量
                cir.setFollownum(circleVoNum.getFollownum());//关注数
                cir.setFollownewnum(circleVoNum.getFollownewnum());//今日新增关注数
                cir.setIntime(circleVoNum.getIntime());//圈子创建时间
                cir.setSupportnum(circleVoNum.getSupportnum());//圈子支持人数
            }
            /////////////////////分类列表////////////////////////
            Map map = new HashedMap();
            map.put("type", circlenum.get(i));
            List<CircleVo> listt = circleService.queryCircleByLikeList(map);//获取圈子分类列表的圈子列表
            List<CircleVo> circleVoslist = new ArrayList<>();
            for (int e = 0; e < listt.size(); e++) {
                CircleVo vo = new CircleVo();
                Integer circleid = listt.get(e).getId();
                List<User> userslist = userService.queryCircleManagerList(circleid);//查询出圈子管理员列表
                for (int j = 0; j < userslist.size(); j++) {
                    if (userslist.get(j).getNickname() == null) {
                        userslist.get(j).setNickname("用户" + userslist.get(j).getPhone().substring(7));
                    }
                }
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
            cir.setClassify(circleVoslist);
            list.add(cir);
        }
        return list;
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
        List<PostList> list = postService.queryPostIsessenceByList(map, pager);
        List<PostList> rewardeds = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            PostList postList = new PostList();
            Integer id = list.get(i).getId();
            Integer circleid = list.get(i).getCircleid();//获取到圈子id
            String nickname = userService.queryUserByNickname(list.get(i).getUserid());//获取发帖人
            Integer share = sharesService.querysum(id);//获取分享数
            Integer rewarded = rewardedService.queryRewardedBySum(id);//获取打赏积分
            Integer accusation = accusationService.queryAccusationBySum(id);//查询帖子举报次数
            Circle circlename = circleService.queryCircleByName(circleid);//获取圈子名称
            postList.setId(list.get(i).getId());
            postList.setTitle(list.get(i).getTitle());
            postList.setNickname(nickname);
            postList.setCollectsum(list.get(i).getCollectsum());
            if (share != null) {
                postList.setShare(share);
            } else {
                postList.setShare(0);
            }
            postList.setCommentsum(list.get(i).getCommentsum());
            postList.setZansum(list.get(i).getZansum());//点赞
            if (rewarded != null) {
                postList.setRewarded(rewarded);//打赏积分
            } else {
                postList.setRewarded(0);
            }
            if (accusation != null) {
                postList.setAccusation(accusation);//举报
            } else {
                postList.setAccusation(0);
            }
            postList.setIntime(list.get(i).getIntime());//帖子发布时间
            postList.setTotalpoint(list.get(i).getTotalpoint());//帖子综合评分
            postList.setIsessence(list.get(i).getIsessence());
            postList.setCirclename(circlename.getName());//帖子所属圈子
            postList.setOrderid(list.get(i).getOrderid());//获取排序
            postList.setEssencedate(list.get(i).getEssencedate());//获取精选日期
            rewardeds.add(postList);
        }


        return rewardeds;
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
     * @param request
     * @param id
     * @param name
     * @param category
     * @param admin
     * @param createtime
     * @param photo
     * @param introduction
     * @param erweima
     * @param status
     * @param isrecommend
     * @param orderid
     * @param permission
     * @return
     */
    public Map<String, Integer> updateCircle(HttpServletRequest request, String id, String name, String category, String circlemanid, String admin,
                                             String createtime, MultipartFile photo, String introduction,
                                             String erweima, String status, String isrecommend, String orderid, String permission) {
        CircleDetails circleDetails = new CircleDetails();
        Map<String, Integer> map = new HashedMap();
        Integer circleid = null;
        try {
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
            if (admin != null) {//管理员列表
                //待定
                String[] ary = admin.split(",");//以逗号分隔接收数据
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
            Date time = null;
            if (createtime != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    time = format.parse(createtime);
                    circleDetails.setCreatetime(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String savedFileName = "";
            String imgurl = "";
            if (photo != null) {
                if (!photo.isEmpty()) {
                    String fileRealName = photo.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedFileName = FileId.toString().replace("-", "").concat(fileSuffix);
                    String savedDir = request.getSession().getServletContext().getRealPath("");
                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision后缀movision去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);
                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/circle/banner/";
                    File savedFile = new File(combinpath, savedFileName);
                    System.out.println("文件url：" + combinpath + "" + savedFileName);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        photo.transferTo(savedFile);  //转存文件
                    }
                }
                imgurl = imgdomain + savedFileName;
                circleDetails.setPhoto(imgurl);
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
        } catch (IOException e) {
            e.printStackTrace();
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
        List<Integer> ords = circleService.queryCircleByOrderidList();
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
        map.put("resault", circleDetails);
        return map;
    }


    /**
     * 圈子添加
     *
     * @param request
     * @param name
     * @param category
     * @param userid
     * @param admin
     * @param circlemanid
     * @param photo
     * @param introduction
     * @param erweima
     * @param isrecommend
     * @param orderid
     * @return
     */
    public Map<String, Integer> addCircle(HttpServletRequest request, String name, String category, String userid, String admin,
                                          String circlemanid, MultipartFile photo, String introduction,
                                          String erweima, String isrecommend, String orderid, String scope) {
        CircleDetails circleDetails = new CircleDetails();
        Map<String, Integer> map = new HashedMap();
        Integer circleid = null;
        try {

            if (name != null) {
                circleDetails.setName(name);
            }
            if (category != null) {
                circleDetails.setCategory(Integer.parseInt(category));
            }

            if (circlemanid != null) {

            }
            if (userid != null) {
                //查询圈主手机号
                String pon = userService.queryUserbyPhoneByUserid(Integer.parseInt(userid));
                circleDetails.setPhone(pon);//设置圈主手机号
            }
            circleDetails.setCreatetime(new Date());
            String savedFileName = "";
            String imgurl = "";
            if (photo != null) {
                if (!photo.isEmpty()) {
                    String fileRealName = photo.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedFileName = FileId.toString().replace("-", "").concat(fileSuffix);
                    String savedDir = request.getSession().getServletContext().getRealPath("");
                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision后缀movision去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);
                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/circle/banner/";
                    File savedFile = new File(combinpath, savedFileName);
                    System.out.println("文件url：" + combinpath + "" + savedFileName);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        photo.transferTo(savedFile);  //转存文件
                    }
                }
                imgurl = imgdomain + savedFileName;
                circleDetails.setPhoto(imgurl);
            }
            if (introduction != null) {
                circleDetails.setIntroduction(introduction);
            }
            if (erweima != null) {
                circleDetails.setErweima(erweima);
            }
            circleDetails.setStatus(0);//初始化审核状态
            if (isrecommend != null) {
                circleDetails.setIsrecommend(Integer.parseInt(isrecommend));
            }
            if (orderid != null) {
                circleDetails.setIsdiscover(1);
                circleDetails.setOrderid(Integer.parseInt(orderid));
            }
            if (scope != null) {
                circleDetails.setScope(Integer.parseInt(scope));
            }
            Integer s = circleService.insertCircle(circleDetails);
            Integer cirid = circleDetails.getId();
            if (admin != null) {//管理员列表
                //待定
                String[] ary = admin.split(",");//以逗号分隔接收数据
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
        } catch (IOException e) {
            e.printStackTrace();
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
     * @return
     */
    public Map<String, Integer> addCircleType(String typename) {
        Map<String, Integer> map = new HashedMap();
        Map packaging = new HashedMap();
        packaging.put("categoryname", typename);
        packaging.put("intime", new Date());
        int i = categoryService.addCircleType(packaging);
        map.put("resault", i);
        return map;
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
     * @param name
     * @param circleman
     * @param type
     * @param begintime
     * @param endtime
     * @return
     */
    public List<CircleIndexList> queryCircleByCondition(String pai, String intime, String popularity, String name, String circleman, String type, String begintime, String endtime) {
        Map map = new HashedMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String beg = null;
        String end = null;
        if (begintime != null && endtime != null) {
            Long l = new Long(begintime);
            Long o = new Long(endtime);
            beg = format.format(l);
            end = format.format(o);
        }
        map.put("pai", pai);
        map.put("intiem", intime);
        map.put("popularity", popularity);
        map.put("name", name);
        map.put("type", type);
        map.put("circleman", circleman);
        map.put("begintime", beg);
        map.put("endtime", end);
        List<CircleIndexList> list = new ArrayList<>();
        if (type == null) {
            List<Integer> circlenum = circleService.queryListByCircleCategory();//查询圈子所有分类
            for (int i = 0; i < circlenum.size(); i++) {
                CircleIndexList cir = new CircleIndexList();
                //查询，圈子分类，圈主，管理员列表，关注人数，今日关注人数，帖子数量，今日新增帖子，精贴数量，支持人数，创建日期
                List<User> users = circleService.queryCircleUserList(circlenum.get(i));//查询管理员列表
                List<User> circlemaster = circleService.queryCircleMan(circlenum.get(i));//圈主
                CircleVo circleVoNum = circleService.queryFollowAndNum(circlenum.get(i));//查询圈子帖子总数，精贴总数，关注总数，支持人数和时间
                cir.setCirclemaster(circlemaster);//圈主列表
                cir.setCategory(circlenum.get(i));//圈子分类
                cir.setCirclemanagerlist(users);//管理员列表
                if (circleVoNum != null) {
                    cir.setPostnum(circleVoNum.getPostnum());//帖子数量
                    cir.setPostnewnum(circleVoNum.getPostnewnum());//今日新增帖子数量
                    cir.setIsessencenum(circleVoNum.getIsessencenum());//精贴数量
                    cir.setFollownum(circleVoNum.getFollownum());//关注数
                    cir.setFollownewnum(circleVoNum.getFollownewnum());//今日新增关注数
                    cir.setIntime(circleVoNum.getIntime());//圈子创建时间
                    cir.setSupportnum(circleVoNum.getSupportnum());//圈子支持人数
                }
                /////////////////////分类列表////////////////////////
                map.put("type", circlenum.get(i));
                List<CircleVo> listt = circleService.queryCircleByLikeList(map);//获取圈子分类列表的圈子列表
                List<CircleVo> circleVoslist = new ArrayList<>();
                for (int e = 0; e < listt.size(); e++) {
                    CircleVo vo = new CircleVo();
                    Integer circleid = listt.get(e).getId();
                    List<User> userslist = userService.queryCircleManagerList(circleid);//查询出圈子管理员列表
                    for (int j = 0; j < userslist.size(); j++) {
                        if (userslist.get(j).getNickname() == null) {
                            userslist.get(j).setNickname("用户" + userslist.get(j).getPhone().substring(7));
                        }
                    }
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
                cir.setClassify(circleVoslist);
                list.add(cir);
            }
        } else {
            CircleIndexList cir = new CircleIndexList();
            //查询，圈子分类，圈主，管理员列表，关注人数，今日关注人数，帖子数量，今日新增帖子，精贴数量，支持人数，创建日期
            List<User> users = circleService.queryCircleUserList(Integer.parseInt(type));//查询管理员列表
            List<User> circlemaster = circleService.queryCircleMan(Integer.parseInt(type));//圈主
            CircleVo circleVoNum = circleService.queryFollowAndNum(Integer.parseInt(type));//查询圈子帖子总数，精贴总数，关注总数，支持人数和时间
            cir.setCirclemaster(circlemaster);//圈主列表
            cir.setCategory(Integer.parseInt(type));//圈子分类
            cir.setCirclemanagerlist(users);//管理员列表
            if (circleVoNum != null) {
                cir.setPostnum(circleVoNum.getPostnum());//帖子数量
                cir.setPostnewnum(circleVoNum.getPostnewnum());//今日新增帖子数量
                cir.setIsessencenum(circleVoNum.getIsessencenum());//精贴数量
                cir.setFollownum(circleVoNum.getFollownum());//关注数
                cir.setFollownewnum(circleVoNum.getFollownewnum());//今日新增关注数
                cir.setIntime(circleVoNum.getIntime());//圈子创建时间
                cir.setSupportnum(circleVoNum.getSupportnum());//圈子支持人数
            }
            /////////////////////分类列表////////////////////////
            List<CircleVo> listt = circleService.queryCircleByLikeList(map);//获取圈子分类列表的圈子列表
            List<CircleVo> circleVoslist = new ArrayList<>();
            for (int e = 0; e < listt.size(); e++) {
                CircleVo vo = new CircleVo();
                Integer circleid = listt.get(e).getId();
                List<User> userslist = userService.queryCircleManagerList(circleid);//查询出圈子管理员列表
                for (int j = 0; j < userslist.size(); j++) {
                    if (userslist.get(j).getNickname() == null) {
                        userslist.get(j).setNickname("用户" + userslist.get(j).getPhone().substring(7));
                    }
                }
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
            cir.setClassify(circleVoslist);
            list.add(cir);
        }
        return list;
    }

}

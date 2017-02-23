package com.movision.facade.boss;

import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.entity.CategoryVo;
import com.movision.mybatis.category.entity.CircleAndCircle;
import com.movision.mybatis.category.service.CategoryService;
import com.movision.mybatis.circle.entity.*;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.manager.service.ManagerServcie;
import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.share.service.SharesService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
            List<User> users = circleService.queryCircleUserList(i);//查询管理员列表
            List<User> circlemaster = users;//圈主
            CircleFollowNum followNum = circleService.queryFollowAndNewNum(i);//返回关注数,今日新增关注人数
            CirclePostNum postnum = circleService.queryCirclePostNum(i);//返回帖子数量，今日新增帖子，精贴数
            CircleVo supportnum = circleService.queryCircleSupportnum(i);//返回圈子分类创建时间，支持人数
            cir.setCirclemaster(circlemaster);//圈主列表和管理员相同
            cir.setCategory(i);//圈子分类
            cir.setCirclemanagerlist(users);//管理员列表
            if (postnum != null) {
                cir.setPostnum(postnum.getPostnum());//帖子数量
                cir.setPostnewnum(postnum.getNewpostnum());//今日新增帖子数量
                cir.setIsessencenum(postnum.getIsessencenum());//精贴数量
            }
            if (followNum != null) {
                cir.setFollownum(followNum.getNum());//关注数
                cir.setFollownewnum(followNum.getNewnum());//今日新增关注数
            }
            cir.setIntime(supportnum.getIntime());//圈子创建时间
            cir.setSupportnum(supportnum.getSupportnum());//圈子支持人数
            /////////////////////分类列表////////////////////////
            List<CircleVo> listt = circleService.queryCircleByList(i);//获取圈子分类列表的圈子列表
            List<CircleVo> circleVoslist = new ArrayList<>();
            for (int e = 0; e < listt.size(); e++) {
                CircleVo vo = new CircleVo();
                Integer circleid = listt.get(e).getId();
                String circlemasterlist = circleService.queryCircleBycirclemaster(listt.get(e).getPhone());//查询圈主
                List<User> userslist = userService.queryCircleManagerList(circleid);//查询出圈子管理员列表
                for (int j = 0; j < userslist.size(); j++) {
                    if (userslist.get(e).getNickname() == null) {
                        userslist.get(e).setNickname("用户" + userslist.get(e).getPhone().substring(7));
                    }
                }
                CircleFollowNum followNumt = circleService.queryFollowAndNewNumt(circleid);//返回关注数,今日新增关注人数
                CirclePostNum postnumt = circleService.queryCirclePostNumt(circleid);//返回帖子数量，今日新增帖子，精贴数
                if (postnumt != null) {
                    vo.setPostnum(postnumt.getPostnum());//帖子数量
                    vo.setPostnewnum(postnumt.getNewpostnum());//今日新增帖子数量
                    vo.setIsessencenum(postnumt.getIsessencenum());//精贴数量
                }
                if (followNumt != null) {
                    vo.setFollownum(followNumt.getNum());//关注数
                    vo.setFollownewnum(followNumt.getNewnum());//今日新增关注数
                }
                vo.setId(listt.get(e).getId());//圈子id
                vo.setName(listt.get(e).getName());//圈子名称
                vo.setCategory(listt.get(e).getCategory());//圈子分类
                vo.setCategoryname(circlemasterlist);//圈主
                vo.setCirclemanagerlist(users);//圈子管理员列表
                vo.setSupportnum(listt.get(e).getSupportnum());//支持数
                vo.setStatus(listt.get(e).getStatus());//圈子状态：0 待审核 1 审核通过 2 审核不通过
                vo.setIsrecommend(listt.get(e).getIsrecommend());//推荐
                vo.setIsdiscover(listt.get(e).getIsdiscover());//首页
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
    public List<PostList> queryPostIsessenceByList(Paging<PostList> pager){
        List<PostList> list=postService.queryPostIsessenceByList(pager);
        List<PostList> rewardeds = new ArrayList<>();
        for (int i =0;i<list.size();i++){
            PostList postList = new PostList();
            Integer id= list.get(i).getId();
            Integer circleid=list.get(i).getCircleid();
            String nickname=userService.queryUserByNickname(circleid);
            Integer share = sharesService.querysum(id);//获取分享数
            Integer rewarded = rewardedService.queryRewardedBySum(id);//获取打赏积分
            Integer accusation = accusationService.queryAccusationBySum(id);//查询帖子举 报次数
            postList.setId(list.get(i).getId());
            postList.setTitle(list.get(i).getTitle());
            postList.setNickname(nickname);
            postList.setCollectsum(list.get(i).getCollectsum());
            postList.setShare(share);
            postList.setCommentsum(list.get(i).getCommentsum());
            postList.setZansum(list.get(i).getZansum());
            postList.setRewarded(rewarded);
            postList.setAccusation(accusation);
            postList.setIsessence(list.get(i).getIsessence());
            postList.setEssencedate(list.get(i).getEssencedate());
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
        Map<String, Integer> map = new HashedMap();
        Map<String, Integer> spread = new HashedMap();
        spread.put("circleid", Integer.parseInt(circleid));
        spread.put("orderid", Integer.parseInt(orderid));
        Integer i = circleService.updateDiscover(spread);
        map.put("resault", i);
        return map;
    }

    /**
     * 圈子推荐到首页
     *
     * @param circleid
     * @return
     */
    public Map<String, Integer> updateCircleIndex(String circleid) {
        Map<String, Integer> map = new HashedMap();
        Integer l = circleService.updateCircleIndex(Integer.parseInt(circleid));
        map.put("resault", l);
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
     * @param isdiscover
     * @param orderid
     * @param permission
     * @return
     */
    public Map<String, Integer> updateCircle(HttpServletRequest request, String id, String name, String category, String circlemanid, String admin,
                                             String createtime, MultipartFile photo, String introduction,
                                             String erweima, String status, String isdiscover, String orderid, String permission) {
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
            if (isdiscover != null) {
                circleDetails.setIsdiscover(Integer.parseInt(isdiscover));
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

}

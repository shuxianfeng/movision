package com.movision.facade.boss;

import com.movision.common.Response;
import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.activePart.entity.ActivePart;
import com.movision.mybatis.activePart.entity.ActivePartList;
import com.movision.mybatis.activePart.service.ActivePartService;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.period.entity.Period;
import com.movision.mybatis.period.service.PeriodService;
import com.movision.mybatis.post.entity.*;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.rewarded.entity.Rewarded;
import com.movision.mybatis.rewarded.entity.RewardedVo;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.share.entity.SharesVo;
import com.movision.mybatis.share.service.SharesService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserLike;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.video.entity.Video;
import com.movision.mybatis.video.service.VideoService;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author zhurui
 * @Date 2017/2/7 9:16
 */
@Service
public class PostFacade {
    @Value("#{configProperties['img.domain']}")
    private String imgdomain;

    @Autowired
    private PostService postService;

    @Autowired
    private CircleService circleService;

    @Autowired
    private UserService userService;

    @Autowired
    private SharesService sharesService;

    @Autowired
    private RewardedService rewardedService;

    @Autowired
    private ActivePartService activePartService;

    @Autowired
    private AccusationService accusationService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private VideoService videoService;

    private static Logger log = LoggerFactory.getLogger(PostFacade.class);


    /**
     * 后台管理-查询帖子列表
     * @param pager
     * @return
     */
    public List<PostList> queryPostByList(Paging<PostList> pager) {
        List<PostList> list = postService.queryPostByList(pager);
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
                postList.setShare(share);
                postList.setCommentsum(list.get(i).getCommentsum());
                postList.setZansum(list.get(i).getZansum());
                postList.setRewarded(rewarded);
                postList.setAccusation(accusation);
            postList.setIshot(list.get(i).getIshot());
            postList.setCirclename(circlename.getName());//帖子所属圈子
            postList.setOrderid(list.get(i).getOrderid());//获取排序
            postList.setEssencedate(list.get(i).getEssencedate());//获取精选日期
                rewardeds.add(postList);
        }
        return rewardeds;
    }

    /**
     * 后台管理-查询活动列表（草稿箱）
     * @param pager
     * @return
     */
    public  List<PostList> queryPostActiveByList(Paging<PostList> pager){
        List<PostList> list = postService.queryPostActiveByList(pager);
        List<PostList> rewardeds=new ArrayList<>();
        for (int i =0; i<list.size(); i++){
            PostList postList = new PostList();
            Integer circleid = list.get(i).getCircleid();//获取到圈子id
            String nickname = userService.queryUserByNickname(circleid);//获取发帖人
            postList.setId(list.get(i).getId());
            postList.setTitle(list.get(i).getTitle());
            postList.setNickname(nickname);
            postList.setIntime(list.get(i).getIntime());
            rewardeds.add(postList);
        }
        return rewardeds;
    }

    /**
     * 后台管理-查询活动列表
     * @param pager
     * @return
     */
     public List<PostActiveList> queryPostActiveToByList(Paging<PostActiveList> pager){
        List<PostActiveList> list = postService.queryPostActiveToByList(pager);
         List<PostActiveList> rewardeds = new ArrayList<>();
         Date date = new Date();
         long str=date.getTime();
              for(int i=0; i<list.size(); i++){
             PostActiveList postList = new PostActiveList();
             Integer postid=list.get(i).getId();//获取到帖子id
             Integer persum=postService.queryPostPerson(postid);
                  String nickname = userService.queryUserByNicknameBy(postid);
             Period periods= periodService.queryPostPeriod(postid);
             Double activefee=list.get(i).getActivefee();
                  Double sumfree = 0.0;
                  if (activefee != null) {
                      sumfree = persum * activefee;
                  }

             Date begintime=periods.getBegintime();
             Date endtime=periods.getEndtime();
             postList.setTitle(list.get(i).getTitle());//主题
             postList.setNickname(nickname);//昵称
             postList.setActivetype(list.get(i).getActivetype());//活动类型
             postList.setActivefee(activefee);//活动费用
             postList.setEssencedate(list.get(i).getEssencedate());//精选日期
             postList.setOrderid(list.get(i).getOrderid());//精选排序
             postList.setBegintime(begintime);//开始时间
             postList.setEndtime(endtime);//结束时间
             postList.setPersum(persum);//报名人数
             postList.setSumfree(sumfree);//总费用
                  String activeStatue = "";
                  if (begintime != null && endtime != null) {
                      long begin = begintime.getTime();
                      long end = endtime.getTime();
                      if (str > begin && str < end) {
                          activeStatue = "报名中";
                      } else if (str < begin) {
                          activeStatue = "未开始";
                      } else if (str > end) {
                          activeStatue = "已结束";
                      }
                  }
             postList.setActivestatue(activeStatue);//活动状态
             rewardeds.add(postList);
         }
        return rewardeds;
     }

    /**
     * 后台管理-查询报名列表记录
     * @param pager
     * @return
     */
    public List<ActivePartList> queyPostCallActive(Paging<ActivePartList> pager){
        List<ActivePartList> list=activePartService.queryPostCallActive(pager);
        List<ActivePartList> rewardeds = new ArrayList<>();
        Date date = new Date();
        long str=date.getTime();
        for (int i=0;i<list.size();i++){
            ActivePartList postActiveList = new ActivePartList();
            Integer postid=list.get(i).getPostid();//获取到帖子id
            Double activefee=postService.queryPostActiveFee(postid);
            Period periods= periodService.queryPostPeriod(postid);
            postActiveList.setActivefee(activefee);//活动单价
            Integer id=list.get(i).getUserid();//用户id
            User user = userService.queryUserB(id);
            Date begintime=periods.getBegintime();
            Date endtime=periods.getEndtime();
            postActiveList.setBegintime(begintime);
            postActiveList.setEndtime(endtime);
            long begin= begintime.getTime();
            long end=endtime.getTime();
            String activeStatue="";
            if(str>begin&&str<end){
                activeStatue="报名中";
            }else if(str<begin){
                activeStatue="未开始";
            }else if(str>end){
                activeStatue="已结束";
            }
            postActiveList.setActivestatue(activeStatue);//活动状态
            postActiveList.setIntime(list.get(i).getIntime());//报名时间
            postActiveList.setNickname(user.getNickname());//用户名
            postActiveList.setPhone(user.getPhone());//联系方式
             postActiveList.setPayStatue(list.get(i).getPayStatue());//支付方式
            postActiveList.setMoneypay(list.get(i).getMoneypay());//实付金额
            postActiveList.setMoneyying(list.get(i).getMoneyying());//应付金额
            rewardeds.add(postActiveList);
        }
        return  rewardeds;
    }

    /**
     * 后台管理-帖子列表-发帖人信息
     *
     * @param postid
     * @return
     */
    public User queryPostByPosted(String postid) {
        Integer circleid = postService.queryPostByCircleid(postid);
        String phone = circleService.queryCircleByPhone(circleid);//获取圈子中的用户手机号
        User user = userService.queryUser(phone);
        return user;
    }

    /**
     * 后台管理-帖子列表-删除帖子
     *
     * @param postid
     * @return
     */
    public Map<String, Integer> deletePost(String postid) {
        int resault = postService.deletePost(Integer.parseInt(postid));
        Map<String, Integer> map = new HashedMap();
        map.put("resault", resault);
        return map;
    }



    /**
     * 后台管理-帖子列表-查看评论
     * @param postid
     * @param pager
     * @return
     */
    public List<CommentVo> queryPostAppraise(String postid, Paging<CommentVo> pager) {
        List<CommentVo> list = commentService.queryCommentsByLsit(postid, pager);
        return list;
    }

    /**
     * 后台管理-帖子列表-帖子评论列表-帖子详情
     *
     * @param commentid
     * @param pager
     * @return
     */
    public List<CommentVo> queryPostByCommentParticulars(String commentid, Paging<CommentVo> pager) {
        List<CommentVo> list = commentService.queryPostByCommentParticulars(Integer.parseInt(commentid), pager);
        return list;
    }

    /**
     * 后台管理-帖子列表-帖子评论列表-添加评论
     *
     * @param postid
     * @param userid
     * @param content
     * @return
     */
    public Map<String, Integer> addPostAppraise(String postid, String userid, String content) {
        CommentVo comm = new CommentVo();
        comm.setPostid(Integer.parseInt(postid));
        comm.setUserid(Integer.parseInt(userid));
        comm.setIntime(new Date());
        comm.setContent(content);
        Map<String, Integer> map = new HashedMap();
        int status;
        int c = commentService.insertComment(comm);
        if (c == 1) {
            postService.updatePostBycommentsum(Integer.parseInt(postid));//更新帖子的评论数
            status = 2;
            map.put("status", status);
        }
        return map;
    }

    /**
     * 后台管理-帖子列表-删除帖子评论
     *
     * @param id
     * @return
     */
    public int deletePostAppraise(String id) {
        return commentService.deletePostAppraise(Integer.parseInt(id));
    }

    /**
     * 后台管理-帖子列表-帖子打赏列表
     *
     * @param postid
     * @param pager
     * @return
     */
    public List<RewardedVo> queryPostAward(String postid, Paging<RewardedVo> pager) {
        return rewardedService.queryPostAward(Integer.parseInt(postid), pager);//分页返回帖子打赏列表
    }

    /**
     * 后台管理-帖子列表-帖子预览
     *
     * @param postid
     * @return
     */
    public PostList queryPostParticulars(String postid) {
        PostList postList = postService.queryPostParticulars(Integer.parseInt(postid));
        Integer circleid = postList.getCircleid();//获取到圈子id
        Integer id = postList.getId();//获取帖子id
        Integer share = sharesService.querysum(id);//获取分享数
        Integer rewarded = rewardedService.queryRewardedBySum(id);//获取打赏积分
        Integer accusation = accusationService.queryAccusationBySum(id);//查询帖子举报次数
        String nickname = userService.queryUserByNickname(circleid);//获取发帖人
        Circle circle = circleService.queryCircleByName(circleid);//获取圈子名称
        postList.setNickname(nickname);
        postList.setShare(share);//分享次数
        postList.setRewarded(rewarded);//打赏积分
        postList.setAccusation(accusation);//举报次数
        postList.setCirclename(circle.getName());//帖子所属圈子
        postList.setCategory(circle.getCategory());//
        return postList;
    }

    /**
     * 后台管理*-活动预览
     * @param postid
     * @return
     */
    public PostList queryPostActiveQ(String postid){
        PostList postList = postService.queryPostParticulars(Integer.parseInt(postid));
        Integer id = postList.getId();
        Date date = new Date();
        long str=date.getTime();
        Integer share =sharesService.querysum(id);
        Period periods= periodService.queryPostPeriod(Integer.parseInt(postid));
        String nickname = userService.queryUserByNicknameBy(Integer.parseInt(postid));//获取发帖人
        postList.setNickname(nickname);
        postList.setShare(share);//分享次数
        postList.setTitle(postList.getTitle());//主标题
        postList.setSubtitle(postList.getSubtitle());//副标题
        postList.setEssencedate(postList.getEssencedate());//精选日期
        postList.setZansum(postList.getZansum());//赞
        postList.setCommentsum(postList.getCommentsum());//评论
        postList.setCollectsum(postList.getCollectsum());//收藏
        postList.setActivetype(postList.getActivetype());//活动类型
        Date begintime=periods.getBegintime();
        Date endtime=periods.getEndtime();
        long begin= begintime.getTime();
        long end=endtime.getTime();
        String activeStatue="";
        if(str>begin&&str<end){
            activeStatue="报名中";
        }else if(str<begin){
            activeStatue="未开始";
        }else if(str>end){
            activeStatue="已结束";
        }
        postList.setActivestatue(activeStatue);//活动状态
        postList.setActivefee(postList.getActivefee());//活动单价
        Integer persum=postService.queryPostPerson(Integer.parseInt(postid));
        postList.setPersum(persum);//报名人数
        postList.setPostcontent(postList.getPostcontent());//活动内容
        postList.setIntime(postList.getIntime());//发布时间
        return postList;
    }

    /**
     * 添加帖子
     *
     * @param title
     * @param subtitle
     * @param type
     * @param circleid
     * @param coverimg
     * @param postcontent
     * @param isessence
     * @param time
     * @return
     */
    public Map<String, Integer> addPost(HttpServletRequest request, String title, String subtitle, String type, String circleid, MultipartFile vid, MultipartFile bannerimgurl,
                                        String userid, MultipartFile coverimg, String postcontent, String isessence, String ishot, String orderid, String time) {
        Post post = new Post();
        Map<String, Integer> map = new HashedMap();
        try {
        post.setTitle(title);//帖子标题
        post.setSubtitle(subtitle);//帖子副标题
        post.setType(Integer.parseInt(type));//帖子类型
        post.setCircleid(Integer.parseInt(circleid));//圈子id
            // post.setCoverimg(coverimg);//帖子封面
            //上传图片到本地服务器
            String savedFileName = "";
            String savedVideo = "";
            String savedbannerimgurl = "";
            /*boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;*/
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (coverimg != null && isMultipart) {
                if (!coverimg.isEmpty()) {
                    String fileRealName = coverimg.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedFileName = FileId.toString().replace("-", "").concat(fileSuffix);
                    String savedDir = request.getSession().getServletContext().getRealPath("");
                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision后缀movision去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);
                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/post/coverimg/";
                    File savedFile = new File(combinpath, savedFileName);
                    System.out.println("文件url：" + combinpath + "" + savedFileName);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        coverimg.transferTo(savedFile);  //转存文件
                    }
                }
            }

            if (vid != null && isMultipart) {
                if (!vid.isEmpty()) {
                    String fileRealName = vid.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedVideo = FileId.toString().replace("-", "").concat(fileSuffix);
                    String savedDir = request.getSession().getServletContext().getRealPath("");
                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision后缀movision去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);
                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/post/video/";
                    File savedFile = new File(combinpath, savedVideo);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        vid.transferTo(savedFile);  //转存文件
                    }
                }

            }

            if (bannerimgurl != null && isMultipart) {
                if (!bannerimgurl.isEmpty()) {
                    String fileRealName = bannerimgurl.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedbannerimgurl = FileId.toString().replace("-", "").concat(fileSuffix);
                    String savedDir = request.getSession().getServletContext().getRealPath("");
                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision后缀movision去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);
                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/post/vidbanner/";
                    File savedFile = new File(combinpath, savedbannerimgurl);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        bannerimgurl.transferTo(savedFile);  //转存文件
                    }
                }
            }

            String voidurl = imgdomain + savedVideo;
            String bannervoidurl = imgdomain + savedbannerimgurl;
            String imgurl = imgdomain + savedFileName;
            post.setCoverimg(imgurl);//添加帖子封面
            post.setIsactive(0);//设置状态为帖子
            post.setPostcontent(postcontent);//帖子内容
            if (isessence != null) {
                post.setIsessence(Integer.parseInt(isessence));//是否为首页精选
            }
            if (ishot != null) {
                post.setIshot(Integer.parseInt(ishot));//是否为圈子精选
            }
            post.setIntime(new Date());
            if (orderid != null) {
                post.setOrderid(Integer.parseInt(orderid));
            } else {
                post.setOrderid(0);
            }
            /*Date isessencetime = null;//加精时间
            if (time != null) {
                Long da = Long.parseLong(time);
                isessencetime = new Date(da);
            }*/
            Date d = null;
            if (time != null) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                d = df.parse(time);
            }
            post.setEssencedate(d);

            post.setUserid(Integer.parseInt(userid));
            int result = postService.addPost(post);//添加帖子
            Integer pid = post.getId();//获取到刚刚添加的帖子id
            Video vide = new Video();
            vide.setPostid(pid);
            vide.setVideourl(voidurl);
            vide.setVideourl(bannervoidurl);
            vide.setIntime(new Date());
            Integer in = videoService.insertVideoById(vide);//添加视频表
            map.put("result", result);
        } catch (Exception e) {
            log.error("帖子添加异常", e);
        }
        return map;

    }

    /**
     * 后台管理--增加活动
     * @param title
     * @param subtitle
     * @param type
     * @param money
     * @param coverimg
     * @param postcontent
     * @param isessence
     * @param orderid
     * @param time
     * @param begintime
     * @param endtime
     * @param userid
     * @return
     */
    public Map<String ,Integer> addPostActive(HttpServletRequest request,String title, String subtitle, String type,String money,
                                              MultipartFile coverimg, String postcontent, String isessence,String orderid, String time,String begintime,String endtime,String userid ){
        Post post = new Post();
        Map<String, Integer> map = new HashedMap();
        try {
            post.setTitle(title);//帖子标题
            post.setSubtitle(subtitle);//帖子副标题
            Integer typee = Integer.parseInt(type);
            post.setActivetype(typee);
            if (typee == 0) {
                post.setActivefee(Double.parseDouble(money));//金额
            }

            String savedFileName = "";
            if (!coverimg.isEmpty()) {
                String fileRealName = coverimg.getOriginalFilename();
                int pointIndex = fileRealName.indexOf(".");
                String fileSuffix = fileRealName.substring(pointIndex);
                UUID FileId = UUID.randomUUID();
                savedFileName = FileId.toString().replace("-", "").concat(fileSuffix);
//                    String savedDir = request.getSession().getServletContext().getRealPath("/images/post/coverimg");
                String savedDir = request.getSession().getServletContext().getRealPath("/");

                //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision-1.0.0后缀movision-1.0.0去除
                //不保存到项目中,防止部包把图片覆盖掉了
                String path = savedDir.substring(0, savedDir.length() - 9);

                //这里组合出真实的图片存储路径
                String combinpath = path + "/images/post/coverimg";

                File savedFile = new File(combinpath, savedFileName);
                boolean isCreateSuccess = savedFile.createNewFile();
                if (isCreateSuccess) {
                    coverimg.transferTo(savedFile);  //转存文件
                }
            }

            post.setCoverimg(savedFileName);
            post.setPostcontent(postcontent);//帖子内容
            if (isessence != null) {
                post.setIsessence(Integer.parseInt(isessence));//是否为首页精选
            }
            post.setIntime(new Date());
            Date isessencetime = null;//加精时间
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            if (time != null) {
                try {
                    isessencetime = format.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Date begin = null;//开始时间
            if (begintime != null) {
                try {
                    begin = format.parse(begintime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Date end = null;
            if (endtime != null) {
                try {
                    end = format.parse(endtime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            post.setEssencedate(isessencetime);
            if (orderid != null) {
                post.setOrderid(Integer.parseInt(orderid));//排序精选
            }
            post.setUserid(Integer.parseInt(userid));//发帖人
            post.setIsactive(1);
            int result = postService.addPostActiveList(post);//添加帖子
            Period period = new Period();
            period.setBegintime(begin);
            period.setEndtime(end);
            Integer id = post.getId();
            period.setPostid(id);
            int r = postService.addPostPeriod(period);
            map.put("result", result);
            map.put("result", r);
        }catch (IOException e){
            e.printStackTrace();
        }
        return map;
    }
    /**
     * 帖子加精
     *
     * @param postid
     * @return
     */
    public Map<String, Integer> addPostChoiceness(String postid, String essencedate, String orderid) {
        Map<String, Integer> map = new HashedMap();
        Post p = new Post();
        if (Integer.parseInt(orderid) != 0) {//加精动作
            p.setId(Integer.parseInt(postid));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = null;
            if (essencedate != null) {
                try {
                    d = df.parse(essencedate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                d = new Date();
            }
            p.setEssencedate(d);//精选日期
            p.setOrderid(Integer.parseInt(orderid));
            Integer result = postService.addPostChoiceness(p);
            map.put("result", result);
            return map;
        } else {//取消加精
            postService.deletePostChoiceness(Integer.parseInt(postid));
            map.put("result", 2);
            return map;
        }
    }

    /**
     * 查询加精排序
     *
     * @return
     */
    public Map<String, Object> queryPostChoiceness() {
        Map<String, Object> map = new HashedMap();
        List<Post> posts = postService.queryPostChoiceness();//返回当天有几条加精
        if (posts.size() <= 5) {//判断当天是否还可以加精
            List<Integer> lou = new ArrayList();
            for (int e = 1; e < 6; e++) {//赋值一个的集合，用于返回排序
                lou.add(e);
            }
            for (int i = 0; i < posts.size(); i++) {
                for (int j = 0; j < lou.size(); j++) {
                    if (posts.get(i).getOrderid() == lou.get(j)) {
                        lou.remove(j);
                    }
                }
            }
            map.put("result", lou);
            return map;
        } else {
            map.put("result", 0);
            return map;//当天加精已达上限
        }
    }


    /**
     * 查询帖子分享列表
     *
     * @param postid
     * @param pager
     * @return
     */
    public List<SharesVo> queryPostShareList(String postid, Paging<SharesVo> pager) {
        return sharesService.queryPostShareList(pager, Integer.parseInt(postid));
    }

    /**
     * 查询名称
     *
     * @param name
     * @param pager
     * @return
     */
    public List<UserLike> likeQueryPostByNickname(String name, Paging<UserLike> pager) {
        return userService.likeQueryPostByNickname(name, pager);
    }

    /**
     * 查询圈子名称二级菜单列表
     * @return
     */
    public Map<String, Object> queryListByCircleType() {
        Map<String, Object> map = new HashedMap();
        List<Integer> list = circleService.queryListByCircleCategory();//查询圈子所有的所属分类
        List<List<Circle>> circlename = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {//根据圈子的所属分类添加二级菜单
            List<Circle> circle = circleService.queryListByCircleList(list.get(i));//用于查询圈子名称
            circlename.add(circle);
        }
        Integer num = circleService.queryCircleByNum();
        map.put("resault", circlename);
        map.put("num", num);
        return map;
    }

    /**
     * 帖子编辑数据回显
     *
     * @param postid
     * @return
     */
    public PostCompile queryPostByIdEcho(String postid) {
        return postService.queryPostByIdEcho(Integer.parseInt(postid));
    }

    /**
     * 编辑帖子
     *
     * @param request
     * @param title
     * @param subtitle
     * @param type
     * @param userid
     * @param circleid
     * @param vid
     * @param coverimg
     * @param postcontent
     * @param isessence
     * @param orderid
     * @param time
     * @return
     */
    public Map<String, Integer> updatePostById(HttpServletRequest request, String postid, String title, String subtitle, String type, String userid, String circleid, MultipartFile vid, MultipartFile bannerimgurl,
                                               MultipartFile coverimg, String postcontent, String isessence, String ishot, String orderid, String time) {
        Post post = new Post();
        Map<String, Integer> map = new HashedMap();
        try {
            post.setId(Integer.parseInt(postid));//帖子id
            post.setTitle(title);//帖子标题
            post.setSubtitle(subtitle);//帖子副标题
            post.setType(Integer.parseInt(type));//帖子类型
            post.setCircleid(Integer.parseInt(circleid));//圈子id
            // post.setCoverimg(coverimg);//帖子封面
            //上传图片到本地服务器
            String savedFileName = "";
            String savedVideo = "";
            String savedbannerimgurl = "";
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            if (coverimg != null && isMultipart) {
                if (!coverimg.isEmpty()) {
                    String fileRealName = coverimg.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedFileName = FileId.toString().replace("-", "").concat(fileSuffix);
                    String savedDir = request.getSession().getServletContext().getRealPath("");
                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision后缀movision去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);
                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/post/coverimg/";
                    File savedFile = new File(combinpath, savedFileName);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        coverimg.transferTo(savedFile);  //转存文件
                    }
                }
            }

            if (vid != null && isMultipart) {
                if (!vid.isEmpty()) {
                    String fileRealName = vid.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedVideo = FileId.toString().replace("-", "").concat(fileSuffix);
                    String savedDir = request.getSession().getServletContext().getRealPath("");
                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision后缀movision去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);
                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/post/video/";
                    File savedFile = new File(combinpath, savedVideo);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        vid.transferTo(savedFile);  //转存文件
                    }
                }
            }

            if (bannerimgurl != null && isMultipart) {
                if (!bannerimgurl.isEmpty()) {
                    String fileRealName = bannerimgurl.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedbannerimgurl = FileId.toString().replace("-", "").concat(fileSuffix);
                    String savedDir = request.getSession().getServletContext().getRealPath("");
                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision后缀movision去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);
                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/post/vidbanner/";
                    File savedFile = new File(combinpath, savedbannerimgurl);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        bannerimgurl.transferTo(savedFile);  //转存文件
                    }
                }
            }
            String voidurl = imgdomain + savedVideo;
            String bannervoidurl = imgdomain + savedbannerimgurl;
            Video vide = new Video();
            vide.setPostid(Integer.parseInt(postid));
            vide.setVideourl(voidurl);
            vide.setVideourl(bannervoidurl);
            vide.setIntime(new Date());
            Integer in = videoService.updateVideoById(vide);
            String img = imgdomain + savedFileName;
            post.setCoverimg(img);//添加帖子封面
            post.setIsactive(0);//设置状态为帖子
            post.setPostcontent(postcontent);//帖子内容
            if (isessence != null) {
                post.setIsessence(Integer.parseInt(isessence));//是否为首页精选
            }
            if (ishot != null) {
                post.setIshot(Integer.parseInt(ishot));//是否为圈子精选
            }
            post.setIntime(new Date());
            if (orderid != null) {
                post.setOrderid(Integer.parseInt(orderid));
            }
            Date d = null;
            if (time != null) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    d = df.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                post.setEssencedate(d);
            }
            post.setEssencedate(d);
            post.setUserid(Integer.parseInt(userid));
            int result = postService.updatePostById(post);//编辑帖子
            map.put("result", result);
        } catch (IOException e) {
            log.error("帖子添加异常", e);
        }
        return map;
    }




/*    *//**
     * 帖子按条件查询
     * @param title
     * @param circleid
     * @param name
     * @param date
     * @return
     *//*
    public List<Object> postSearch(String title, String circleid, String name, Date date,String pageNo,String pageSize){
        if (title!=null&&circleid!=null&&name!=null&&date!=null){//当没有添加条件的情况下执行全部搜索
            Map<String ,Object> map=new HashedMap();
            map.put("title",title);
            map.put("circleid",circleid);
            map.put("name",name);
            map.put("date",date);
            return postService.postSearch(map);
        }else{
            return queryPostByList(pageNo,pageSize);
        }

    }*/

}

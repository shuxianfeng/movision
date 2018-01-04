package com.movision.facade.index;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.*;
import com.movision.common.constant.ImConstant;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.PointConstant;
import com.movision.common.constant.PostLabelConstants;
import com.movision.common.util.ShiroUtil;
import com.movision.exception.BusinessException;
import com.movision.facade.address.AddressFacade;
import com.movision.facade.boss.XmlParseFacade;
import com.movision.facade.comment.FacadeComments;
import com.movision.facade.im.ImFacade;
import com.movision.facade.paging.PageFacade;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.fsearch.utils.CollectionUtil;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.accusation.entity.Accusation;
import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.activePart.entity.ActivePart;
import com.movision.mybatis.activePart.service.ActivePartService;
import com.movision.mybatis.activeTake.entity.ActiveTake;
import com.movision.mybatis.activeTake.service.ActiveTakeService;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.CirclePost;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.circleCategory.service.CircleCategoryService;
import com.movision.mybatis.city.entity.City;
import com.movision.mybatis.city.service.CityService;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.compressImg.entity.CompressImg;
import com.movision.mybatis.compressImg.service.CompressImgService;
import com.movision.mybatis.followCircle.entity.FollowCircle;
import com.movision.mybatis.followCircle.service.FollowCircleService;
import com.movision.mybatis.followUser.entity.FollowUser;
import com.movision.mybatis.followUser.service.FollowUserService;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.mybatis.labelSearchTerms.entity.LabelSearchTerms;
import com.movision.mybatis.labelSearchTerms.service.LabelSearchTermsService;
import com.movision.mybatis.opularSearchTerms.service.OpularSearchTermsService;
import com.movision.mybatis.pageHelper.entity.Datagrid;
import com.movision.mybatis.period.entity.Period;
import com.movision.mybatis.post.entity.ActiveVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostReturnAll;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.entity.*;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postAndUserRecord.entity.PostAndUserRecord;
import com.movision.mybatis.postAndUserRecord.service.PostAndUserRecordService;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabel.entity.PostLabelVo;
import com.movision.mybatis.postLabel.service.PostLabelService;
import com.movision.mybatis.postLabelRelation.service.PostLabelRelationService;
import com.movision.mybatis.postShareGoods.entity.PostShareGoods;
import com.movision.mybatis.systemLayout.service.SystemLayoutService;
import com.movision.mybatis.testintime.entity.TestIntime;
import com.movision.mybatis.testintime.service.TestIntimeService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserAll;
import com.movision.mybatis.user.entity.UserLike;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userOperationRecord.entity.UserOperationRecord;
import com.movision.mybatis.userOperationRecord.service.UserOperationRecordService;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecord;
import com.movision.mybatis.userRefreshRecord.entity.UserRefreshRecordVo;
import com.movision.mybatis.userRefreshRecord.service.UserRefreshRecordService;
import com.movision.utils.*;
import com.movision.utils.oss.AliOSSClient;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.model.ServicePaging;
import com.movision.utils.propertiesLoader.MongoDbPropertiesLoader;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import com.movision.zookeeper.DistributedLock;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author shuxf
 * @Date 2017/1/19 15:43
 */
@Service
public class FacadePost {

    private static Logger log = LoggerFactory.getLogger(FacadePost.class);

    public static final String LOCK_NAME = "release_post";

    @Autowired
    private LabelSearchTermsService labelSearchTermsService;

    @Autowired
    private PageFacade pageFacade;

    @Autowired
    private PostService postService;

    @Autowired
    private AccusationService accusationService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CircleService circleService;

    @Autowired
    private MovisionOssClient movisionOssClient;

    @Autowired
    private JsoupCompressImg jsoupCompressImg;

    @Autowired
    private UserOperationRecordService userOperationRecordService;

    @Autowired
    private PointRecordFacade pointRecordFacade;

    @Autowired
    private DesensitizationUtil desensitizationUtil;

    @Autowired
    private PostAndUserRecordService postAndUserRecordService;

    @Autowired
    private FacadeIndex facadeIndex;

    @Autowired
    private UserRefreshRecordService userRefreshRecordService;

    @Autowired
    private ImFacade imFacade;

    @Autowired
    private CoverImgCompressUtil coverImgCompressUtil;

    @Autowired
    private CompressImgService compressImgService;

    @Autowired
    private AliOSSClient aliOSSClient;

    @Autowired
    private AddressFacade addressFacade;

    @Autowired
    private VideoCoverURL videoCoverURL;
    @Autowired
    private OpularSearchTermsService opularSearchTermsService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostLabelRelationService postLabelRelationService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostLabelService postLabelService;
    @Autowired
    private FollowUserService followUserService;
    @Autowired
    private FacadeComments facadeComments;
    @Autowired
    private CircleCategoryService circleCategoryService;
    @Autowired
    private ActivePartService activePartService;
    @Autowired
    private FacadeHeatValue facadeHeatValue;
    @Autowired
    private TestIntimeService testIntimeService;
    @Autowired
    private CityService cityService;
 /*
    @Autowired
    private UserBehaviorService userBehaviorService;
*/
    @Autowired
    private ActiveTakeService activeTakeService;

    @Autowired
    private SystemLayoutService systemLayoutService;

    @Autowired
    private XmlParseFacade xmlParseFacade;
    @Autowired
    private HomepageManageService homepageManageService;

    @Autowired
    private FollowCircleService followCircleService;

    public PostVo queryPostDetail(String postid, String userid) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        //通过userid、postid查询该用户有没有关注该圈子的权限
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(postid));
        if (!StringUtils.isEmpty(userid)) {
            parammap.put("userid", Integer.parseInt(userid));
        }
        PostVo vo = postService.queryPostDetail(parammap);
        log.info("帖子详情中的pistid:-----------" + postid);
        if (vo != null) {
            int sum = 0;
            if (!StringUtils.isEmpty(userid)) {
                Map map = new HashMap();
                int userids = vo.getUserid();
                map.put("id", userids);//被关注的
                map.put("userid", Integer.parseInt(userid));//关注的人
                sum = userService.queryIsFollowAuthor(map);
            }
            vo.setIsfollow(sum + "");

            List<PostVo> array = new ArrayList<>();
            array.add(vo);
            findPostLabel(array);

            /**List<PostLabel> postLabels = postService.queryPostLabel(Integer.parseInt(postid));
             if (postLabels.size() != 0) {
             vo.setPostLabels(postLabels);
             }*/
            //-----帖子内容格式转换
            String str = vo.getPostcontent();
            JSONArray jsonArray = JSONArray.fromObject(str);

            //因为视频封面会有播放权限失效限制，过期失效，所以这里每请求一次都需要对帖子内容中包含的视频封面重新请求
            //增加这个工具类 videoCoverURL.getVideoCover(jsonArray); 进行封面url重新请求
            jsonArray = videoCoverURL.getVideoCover(jsonArray);
            //-----将转换完的数据封装返回
            vo.setPostcontent(jsonArray.toString());
            //评论
            List<CommentVo> co = facadeComments.postDetailComment(Integer.parseInt(postid), userid);
            if (co != null) {
                vo.setCommentVos(co);
            }
            if (null != vo) {
                //根据帖子封面原图url查询封面压缩图url，如果存在替换，不存在就用原图
                String compressurl = postService.queryCompressUrl(vo.getCoverimg());
                if (null != compressurl && !compressurl.equals("") && !compressurl.equals("null")) {
                    vo.setCoverimg(compressurl);
                }

                /** int rewardsum = postService.queryRewardSum(postid);//查询帖子被打赏的次数
                 vo.setRewardsum(rewardsum);
                 List<UserLike> nicknamelist = postService.queryRewardPersonNickname(postid);
                 vo.setRewardpersonnickname(nicknamelist);*/
                /**   if (type.equals("1") || type.equals("2")) {
                 Video video = postService.queryVideoUrl(Integer.parseInt(postid));
                 vo.setVideourl(video.getVideourl());
                 vo.setVideocoverimgurl(video.getBannerimgurl());
                 }*/
                if (vo.getUserid() != -1) {//发帖人为普通用户时查询发帖人昵称和手机号
                    User user = userService.queryUserB(vo.getUserid());
                    if (user != null) {
                        vo.setUserid(user.getId());
                        vo.setNickname(user.getNickname());
                        vo.setPhone(user.getPhone());
                        vo.setPhoto(user.getPhoto());
                        vo.setNickname((String) desensitizationUtil.desensitization(vo.getNickname()).get("str"));//昵称脱敏
                    }
                } else {
                    User user = userService.queryUserB(vo.getUserid());
                    if (user != null) {
                        vo.setUserid(user.getId());
                        vo.setNickname(user.getNickname());
                        vo.setPhoto(user.getPhoto());
                        vo.setNickname((String) desensitizationUtil.desensitization(vo.getNickname()).get("str"));//昵称脱敏
                    }
                }
                Integer circleid = vo.getCircleid();
                //查询帖子详情最下方推荐的4个热门圈子
                /**List<Circle> hotcirclelist = circleService.queryHotCircle();
                 vo.setHotcirclelist(hotcirclelist);*/
                //查询帖子中分享的商品
                List<GoodsVo> shareGoodsList = goodsService.queryShareGoodsList(Integer.parseInt(postid));
                vo.setShareGoodsList(shareGoodsList);

                //对帖子内容进行脱敏处理
                vo.setTitle((String) desensitizationUtil.desensitization(vo.getTitle()).get("str"));//帖子主标题脱敏
                if (null != vo.getSubtitle()) {
                    vo.setSubtitle((String) desensitizationUtil.desensitization(vo.getSubtitle()).get("str"));//帖子副标题脱敏
                }
                vo.setPostcontent((String) desensitizationUtil.desensitization(vo.getPostcontent()).get("str"));//帖子正文文字脱敏

                //数据插入mongodb
                if (StringUtil.isNotEmpty(userid)) {
                    PostAndUserRecord postAndUserRecord = new PostAndUserRecord();
                    postAndUserRecord.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
                    postAndUserRecord.setCrileid(circleid);
                    postAndUserRecord.setPostid(Integer.parseInt(postid));
                    postAndUserRecord.setUserid(Integer.parseInt(userid));
                    postAndUserRecord.setIntime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    postAndUserRecordService.insert(postAndUserRecord);
                    //同步mysql的浏览量
                    postService.updateCountView(Integer.parseInt(postid));
                }
            }
        }
        return vo;
    }

    public PostVo queryPostDetail_20171226(String postid, String userid, String device) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        //通过userid、postid查询该用户有没有关注该圈子的权限
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(postid));
        if (!StringUtils.isEmpty(userid)) {
            parammap.put("userid", Integer.parseInt(userid));
        }
        PostVo vo = postService.queryPostDetail(parammap);
        log.info("帖子详情中的pistid:-----------" + postid);
        if (vo != null) {
            int sum = 0;
            if (!StringUtils.isEmpty(userid)) {
                Map map = new HashMap();
                int userids = vo.getUserid();
                map.put("id", userids);//被关注的
                map.put("userid", Integer.parseInt(userid));//关注的人
                sum = userService.queryIsFollowAuthor(map);
            }
            vo.setIsfollow(sum + "");

            List<PostVo> array = new ArrayList<>();
            array.add(vo);
            findPostLabel(array);

            /**List<PostLabel> postLabels = postService.queryPostLabel(Integer.parseInt(postid));
             if (postLabels.size() != 0) {
             vo.setPostLabels(postLabels);
             }*/
            //-----帖子内容格式转换
            String str = vo.getPostcontent();
            JSONArray jsonArray = JSONArray.fromObject(str);

            //因为视频封面会有播放权限失效限制，过期失效，所以这里每请求一次都需要对帖子内容中包含的视频封面重新请求
            //增加这个工具类 videoCoverURL.getVideoCover(jsonArray); 进行封面url重新请求
            jsonArray = videoCoverURL.getVideoCover(jsonArray);
            //-----将转换完的数据封装返回
            vo.setPostcontent(jsonArray.toString());
            //评论
            List<CommentVo> co = facadeComments.postDetailComment(Integer.parseInt(postid), userid);
            if (co != null) {
                vo.setCommentVos(co);
            }
            if (null != vo) {
                //根据帖子封面原图url查询封面压缩图url，如果存在替换，不存在就用原图
                String compressurl = postService.queryCompressUrl(vo.getCoverimg());
                if (null != compressurl && !compressurl.equals("") && !compressurl.equals("null")) {
                    vo.setCoverimg(compressurl);
                }

                /** int rewardsum = postService.queryRewardSum(postid);//查询帖子被打赏的次数
                 vo.setRewardsum(rewardsum);
                 List<UserLike> nicknamelist = postService.queryRewardPersonNickname(postid);
                 vo.setRewardpersonnickname(nicknamelist);*/
                /**   if (type.equals("1") || type.equals("2")) {
                 Video video = postService.queryVideoUrl(Integer.parseInt(postid));
                 vo.setVideourl(video.getVideourl());
                 vo.setVideocoverimgurl(video.getBannerimgurl());
                 }*/
                if (vo.getUserid() != -1) {//发帖人为普通用户时查询发帖人昵称和手机号
                    User user = userService.queryUserB(vo.getUserid());
                    if (user != null) {
                        vo.setUserid(user.getId());
                        vo.setNickname(user.getNickname());
                        vo.setPhone(user.getPhone());
                        vo.setPhoto(user.getPhoto());
                        vo.setNickname((String) desensitizationUtil.desensitization(vo.getNickname()).get("str"));//昵称脱敏
                    }
                } else {
                    User user = userService.queryUserB(vo.getUserid());
                    if (user != null) {
                        vo.setUserid(user.getId());
                        vo.setNickname(user.getNickname());
                        vo.setPhoto(user.getPhoto());
                        vo.setNickname((String) desensitizationUtil.desensitization(vo.getNickname()).get("str"));//昵称脱敏
                    }
                }
                Integer circleid = vo.getCircleid();
                //查询帖子详情最下方推荐的4个热门圈子
                /**List<Circle> hotcirclelist = circleService.queryHotCircle();
                 vo.setHotcirclelist(hotcirclelist);*/
                //查询帖子中分享的商品
                List<GoodsVo> shareGoodsList = goodsService.queryShareGoodsList(Integer.parseInt(postid));
                vo.setShareGoodsList(shareGoodsList);

                //对帖子内容进行脱敏处理
                vo.setTitle((String) desensitizationUtil.desensitization(vo.getTitle()).get("str"));//帖子主标题脱敏
                if (null != vo.getSubtitle()) {
                    vo.setSubtitle((String) desensitizationUtil.desensitization(vo.getSubtitle()).get("str"));//帖子副标题脱敏
                }
                vo.setPostcontent((String) desensitizationUtil.desensitization(vo.getPostcontent()).get("str"));//帖子正文文字脱敏

                //判断帖子详情页是否显示投票按钮：0 不显示 1 不在投票时间段 2 已投票 3 可投票----2017.12.26shuxf，随投稿新需求一同增加
                int tmark = 0;
                if (null != vo.getActiveid()){
                    int activeid = vo.getActiveid();
                    PostActiveList activeVo = postService.queryActiveById(activeid);
                    //计算距离结束时间
                    Date begin = activeVo.getBegintime();
                    Date end = activeVo.getEndtime();
                    Date now = new Date();
                    int enddays = DateUtils.activeEndDays(now, begin, end);
                    //先判断时间是否在投票时间段
                    if (enddays == 0 || enddays == -1){
                        tmark = 1;//不在投票时间段
                    }else {
                        tmark = 3;//在投票时间段，可投票
                    }
                    //判断当前用户是否已投票
                    if (StringUtil.isNotEmpty(device)){//APP发起的请求，device不为空
                        Map<String, Object> map = new HashMap<>();
                        map.put("device", device);
                        map.put("activeid", activeid);
                        map.put("postid", Integer.parseInt(postid));
                        int takenum = activeTakeService.postidCount(map);
                        if (takenum > 0){
                            tmark = 2;//已投票
                        }
                    }
                }
                vo.setTakemark(tmark);

                //数据插入mongodb
                if (StringUtil.isNotEmpty(userid)) {
                    PostAndUserRecord postAndUserRecord = new PostAndUserRecord();
                    postAndUserRecord.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
                    postAndUserRecord.setCrileid(circleid);
                    postAndUserRecord.setPostid(Integer.parseInt(postid));
                    postAndUserRecord.setUserid(Integer.parseInt(userid));
                    postAndUserRecord.setIntime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    postAndUserRecordService.insert(postAndUserRecord);
                    //同步mysql的浏览量
                    postService.updateCountView(Integer.parseInt(postid));
                }
            }
        }
        return vo;
    }

    public Datagrid queryAllPageHelper(String pageNo, String pageSize){
        return postService.queryAllPageHelper(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
    }

    /**
     * 相关帖子
     *
     * @return
     */
    public List queryRelatedPosts(String postid, ServicePaging<PostVo> paging) {
        //根据帖子查询发帖用户
        int postuserid = postService.postUserId(Integer.parseInt(postid));
        //根据帖子id查询圈子
        int circleid = postService.queryCrileid(Integer.parseInt(postid));

        List<PostVo> ps = postService.findAllPostCrile(circleid);   //圈子中的帖子
        //这个用户发的帖子
        List<PostVo> userPost = postService.findUserPost(postuserid);
        //这个帖子的标签id
        List<Integer> labids = postService.findPostByLabelId(Integer.parseInt(postid));
        List<PostVo> labpost = null;
        //根据id查帖子
        if (labids.size() != 0) {
            labpost = postService.findUserByLabelPost(labids);
            ps.addAll(labpost);
        }
        List<PostVo> postVo = postService.queryPost(Integer.parseInt(postid));
        ps.addAll(userPost);
        ps.removeAll(postVo);
        Set<PostVo> linkedHashSet = new LinkedHashSet<PostVo>(ps);
        ps = new ArrayList<PostVo>(linkedHashSet);
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(new BeanComparator("heatvalue"), true);//true,fase正序反序
        Collections.sort(ps, chain);
        List<PostVo> finpost = NotLoginretuenListPo(ps, paging);
        return finpost;

    }

    public ActiveVo queryActiveDetail(String postid, String userid, String activetype) {

        //告知类活动
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(postid));
        if (!StringUtils.isEmpty(userid)) {
            parammap.put("userid", Integer.parseInt(userid));
        }
        ActiveVo active = postService.queryNoticeActive(parammap);
        if (active != null) {
            /**List<PostLabel> postLabels = postService.queryPostLabel(Integer.parseInt(postid));
             active.setPostLabels(postLabels);*/
            //计算距离结束时间
            Date begin = active.getBegintime();
            Date end = active.getEndtime();
            Date now = new Date();
            int enddays = DateUtils.activeEndDays(now, begin, end);
            active.setEnddays(enddays);
            //List<PostVo> postVos=postService
            //查询活动参与总人数
            int partsum = postService.queryActivePartSum(Integer.parseInt(postid));
            active.setPartsum(partsum);
            //用户有没有投过稿
            /**Map map = new HashMap();
             map.put("id", Integer.parseInt(postid));
             map.put("uid", ShiroUtil.getAppUserID());
             int ispart = postService.isUserContribe(map);
             active.setIsPart(ispart);*/
            //如果为商城促销类活动，需要在此基础上增加促销类商品列表
            if (activetype.equals("1")) {

                List<GoodsVo> goodsList = goodsService.queryActiveGoods(postid);
                active.setPromotionGoodsList(goodsList);

            }

            //增加活动详情最下方推荐的四个热门活动
            //  active.setHotActiveList(postService.queryFourHotActive());

            //对活动内容进行脱敏处理
            active.setTitle((String) desensitizationUtil.desensitization(active.getTitle()).get("str"));//活动主标题脱敏
            active.setSubtitle((String) desensitizationUtil.desensitization(active.getSubtitle()).get("str"));//活动副标题脱敏
            active.setPostcontent((String) desensitizationUtil.desensitization(active.getPostcontent()).get("str"));//活动正文文字脱敏

            //插入mongodb
            if (StringUtil.isNotEmpty(userid)) {
                PostAndUserRecord postAndUserRecord = new PostAndUserRecord();
                postAndUserRecord.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
                postAndUserRecord.setUserid(Integer.parseInt(userid));
                postAndUserRecord.setPostid(Integer.parseInt(postid));
                postAndUserRecord.setCrileid(null);
                postAndUserRecord.setIntime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                postAndUserRecordService.insert(postAndUserRecord);
                //同步mysql的浏览量
                postService.updateCountView(Integer.parseInt(postid));

            }
        }
        return active;
    }

    public List<Date> queryDateSelect(Paging<Date> pager) {
        return postService.queryDateSelect(pager);
    }

    public Map<String, Object> queryPastPostDetail(String date) {
        Map<String, Object> parammap = new HashMap();
        Map<String, Object> map = new HashMap();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (!StringUtils.isEmpty(date)) {
            parammap.put("date", date);
        } else {
            parammap.put("date", sdf.format(d));
        }
        parammap.put("days", 0);
        List<PostVo> currentList = postService.queryPastPostList(parammap);//选择的日期这天
        parammap.put("days", 1);
        List<PostVo> dayagoList = postService.queryPastPostList(parammap);//选择的日期前一天
        parammap.put("days", 2);
        List<PostVo> twodayagoList = postService.queryPastPostList(parammap);//选择的日期前两天

        map.put("currentList", facadeIndex.processEnddaysPartsum(currentList));//经过processEnddaysPartsum方法处理
        map.put("dayagoList", facadeIndex.processEnddaysPartsum(dayagoList));//经过processEnddaysPartsum方法处理
        map.put("twodayagoList", facadeIndex.processEnddaysPartsum(twodayagoList));//经过processEnddaysPartsum方法处理
        return map;
    }

    public List<PostVo> pastHotPostList(Paging<PostVo> pager, String circleid) {

        return postService.pastHotPostList(pager, Integer.parseInt(circleid));
    }

    public List<PostVo> queryCircleIndex2(Paging<PostVo> pager, String circleid) {
        //前期暂时先按照发帖时间排倒序（后续再优化）
        return postService.queryPostList(pager, circleid);
    }

    public int checkReleasePostPermission(String userid, String circleid) {
        //这个根据userid和圈子id判断该用户有没有该圈子的发帖权限
        //查询当前圈子的开放范围
        int scope = circleService.queryCircleScope(Integer.parseInt(circleid));
        //查询当前圈子的所有者(返回所有者的用户id)
        User owner = circleService.queryCircleOwner(Integer.parseInt(circleid));
        //通过userid查询当前登录用户的用户等级
        UserAll user = userService.queryUserById(Integer.parseInt(userid));
        //查询当前圈子的所有管理员列表
        List<User> manageList = circleService.queryCircleManage(Integer.parseInt(circleid));

        int flag = getMarkIsCircleAdmin(userid, manageList);
        int lev = user.getLevel();//用户等级
        //所有人均可发或当前用户为圈子所有者或管理员或当前圈子只有大V可发而当前用户正式大V
        if (scope == 2 || (Integer.parseInt(userid) == owner.getId() || flag == 1) || (scope == 1 && lev >= 1)) {
            return 1;//有发帖权限
        } else {
            return -1;//无发帖权限
        }
    }

    public int updatePostIsdel(String vid) {
        return postService.updatePostIsdel(vid);
    }

    public Map uploadPostFacePic(MultipartFile file) {
        Map m = new HashMap();
        //上传到本地服务器
        m = movisionOssClient.uploadMultipartFileObject(file, "img");
        String url = String.valueOf(m.get("url"));
        //把本地服务器原图上传至阿里云
        //对上传到阿里云的图片url重拼
        String newalurl = "";//原图
        Map al = aliOSSClient.uploadInciseStream(url, "img", "coverIncise");
        String alurl = String.valueOf(al.get("url"));
        for (int j = 0; j < 3; j++) {
            alurl = alurl.substring(alurl.indexOf("/") + 1);
        }
        newalurl = PropertiesLoader.getValue("formal.img.domain") + "/" + alurl;//拿实际url第三个斜杠后面的内容和formal.img.domain进行拼接，如："http://pic.mofo.shop" + "/upload/postCompressImg/img/yDi0T2nY1496812117357.png"

        Map map = new HashMap();
        int wt = 0;//图片压缩后的宽度
        int ht = 0;//图片压缩后的高度440
        try {
            File file1 = new File(url);
            InputStream is = new FileInputStream(file1);
            BufferedImage bi = ImageIO.read(is);
            //获取图片压缩比例
//            Double ratio = systemLayoutService.queryFileRatio("file_compress_ratio");
            Double rate = jsoupCompressImg.getWH(bi.getWidth(), bi.getHeight());//计算压缩比例
            wt = (int) Math.ceil(bi.getWidth() * rate);
            ht = (int) Math.ceil(bi.getHeight() * rate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file1 = new File(url);
        Long fsize = file1.length();//获取文件大小
        String compressUrl = "";
        if (fsize > 800 * 1024) {
            compressUrl = coverImgCompressUtil.ImgCompress(url, wt, ht);
        } else {
            compressUrl = url;
        }
        System.out.println("压缩完的切割图片url==" + compressUrl);
        // 对压缩完的图片上传到阿里云
        Map compressmap = aliOSSClient.uploadInciseStream(compressUrl, "img", "coverIncise");
        String newurl = String.valueOf(compressmap.get("url"));
        //对上传到阿里云的图片url重拼
        String newimgurl = "";//压缩图
        //拿实际url第三个斜杠后面的内容和formal.img.domain进行拼接
        for (int j = 0; j < 3; j++) {
            newurl = newurl.substring(newurl.indexOf("/") + 1);
        }
        newimgurl = PropertiesLoader.getValue("formal.img.domain") + "/" + newurl;//拿实际url第三个斜杠后面的内容和formal.img.domain进行拼接，如："http://pic.mofo.shop" + "/upload/postCompressImg/img/yDi0T2nY1496812117357.png"

        //6删除本地服务器切割的图片文件

        //----(2)
        File fdel = new File(url);
        long l = fdel.length();
        float size = (float) l / 1024 / 1024;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        String filesize = df.format(size);//返回的是String类型的
        //把切割好的原图和压缩图分别存放数据库中
        CompressImg compressImg = new CompressImg();
        compressImg.setCompressimgurl(newimgurl);
        compressImg.setProtoimgsize(filesize);
        compressImg.setProtoimgurl(newalurl);
        compressImg.setIntime(new Date());
        compressImgService.insert(compressImg);
        map.put("compressmap", newimgurl);
        //----(1)
        File fdel2 = new File(compressUrl);
        fdel2.delete();//删除压缩图
        fdel.delete();//删除上传到本地的原图片文件
        //----(3)
        File fdel3 = new File(compressUrl);
        fdel3.delete();//删除压缩完成的图片
        return map;
    }


    @Transactional
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map releasePostByPCTest(HttpServletRequest request, String userid, String circleid, String title,
                                   String postcontent, String coverimg, String labelid, String proids, String category) {
        Map map = new HashMap();

        //这里需要根据userid判断当前登录的用户是否有发帖权限
        //查询当前圈子的开放范围
        int scope = circleService.queryCircleScope(Integer.parseInt(circleid));
        //查询当前圈子的所有者(返回所有者的用户id)
        User owner = circleService.queryCircleOwner(Integer.parseInt(circleid));
        //查询当前圈子的所有管理员列表
        List<User> manageList = circleService.queryCircleManage(Integer.parseInt(circleid));
        //通过userid查询当前登录用户的用户等级
        UserAll user = userService.queryUserById(Integer.parseInt(userid));
        //判断该用户是否是圈子管理员
        int mark = getMarkIsCircleAdmin(userid, manageList);
        int lev = user.getLevel();//当前登录用户的等级
        //拥有权限的：1.该圈所有人均可发帖 2.该用户是该圈所有者 3.所有者和大V可发时，发帖用户即为大V
        if (scope == 2 || (Integer.parseInt(userid) == owner.getId() || mark == 1) || (scope == 1 && lev >= 1)) {

            try {
                log.info("APP前端用户开始请求发帖");

                Post post = preparePost4PC(request, userid, circleid, title, postcontent, coverimg, category);
                //插入帖子
                postService.releasePost(post);

                //帖子使用的标签
                if (StringUtil.isNotEmpty(labelid)) {
                    String[] str = labelid.split(",");
                    Map postlabelrelationMap = new HashMap();
                    List<Integer> newLabelIdList = new ArrayList<>();
                    for (int i = 0; i < str.length; i++) {
                        newLabelIdList.add(Integer.parseInt(str[i]));
                    }
                    postlabelrelationMap.put("postid", post.getId());
                    postlabelrelationMap.put("labelids", newLabelIdList.toArray());
                    //批量新增帖子、标签关系
                    postLabelRelationService.batchAdd(postlabelrelationMap);
                }

                int flag = post.getId();//返回的主键--帖子id
                //再保存帖子中分享的商品列表(如果商品id字段不为空)
                insertPostShareGoods(proids, flag);

                pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.post.getCode(), Integer.parseInt(userid));//完成积分任务根据不同积分类型赠送积分的公共方法（包括总分和流水）

                map.put("flag", flag);
                return map;

            } catch (Exception e) {
                log.error("系统异常，APP发帖失败");
                map.put("flag", -2);
                e.printStackTrace();
                return map;
            }
        } else {
            log.info("该用户不具备发帖权限");
            map.put("flag", -1);
            return map;
        }
    }

    /**
     * 官网模块发帖-准备post实体
     *
     * @param request
     * @param userid
     * @param circleid
     * @param title
     * @param postcontent
     * @param coverimg
     * @return
     */
    private Post preparePost4PC(HttpServletRequest request, String userid, String circleid, String title, String postcontent, String coverimg, String category) {
        Post post = new Post();
        post.setCircleid(Integer.parseInt(circleid));
        post.setTitle(title);
        Map con = null;
        if (StringUtil.isNotEmpty(postcontent)) {
            //内容转换
            con = jsoupCompressImg.newCompressImg(request, postcontent);
            System.out.println(con);
            if ((int) con.get("code") == 200) {
                String str = con.get("content").toString();
                postcontent = str;
            } else {
                log.error("APP端帖子图片内容转换异常");
            }
        }

        post.setPostcontent(postcontent);//帖子内容
        post.setZansum(0);//新发帖全部默认为0次
        post.setCommentsum(0);//被评论次数
        post.setForwardsum(0);//被转发次数
        post.setCollectsum(0);//被收藏次数
        post.setCoverimg(coverimg);//帖子封面
        post.setIsactive(0);//是否为活动 0 帖子 1活动
        post.setIshot(0);//是否设为热门：默认0否
        post.setIsessence(0);//是否设为精选：默认0否
        post.setIsessencepool(0);//是否设为精选池中的帖子
        post.setIntime(new Date());//帖子发布时间
        post.setTotalpoint(0);//帖子综合评分
        if (StringUtil.isNotEmpty(category)) {
            //0图文发帖1纯图片发帖2视频贴
            post.setCategory(Integer.parseInt(category));
        }
        if ((int) con.get("flag") != 0) {
            post.setIsdel(2);//视频
        } else {
            post.setIsdel(0);//图文
        }

        post.setUserid(Integer.parseInt(userid));
        return post;
    }

    @CacheEvict(value = "indexData", key = "'index_data'")
    public int delPost(String userid, String postid) {

        int flag = 0;

        //首先校验用户的删除权限
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("postid", Integer.parseInt(postid));

        flag = postService.delPost(parammap);
        //减少用户的热度
        facadeHeatValue.lessUserHeatValue(2, Integer.parseInt(userid));
        return flag;
    }

    public Map<String, Object> queryRecommendGoodsList(String userid, String pageNo, String pageSize) {

        Paging<Goods> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));

        Map<String, Object> map = new HashMap<>();

        //查询用户收藏的商品(不做分页)
        List<Goods> collectGoodsList = postService.queryCollectGoodsList(Integer.parseInt(userid));
        //用户id为空时，默认查询所有商品（有分页）
        List<Goods> allGoodsList = postService.queryAllGoodsList(pager);

        pager.result(allGoodsList);
        map.put("collectGoodsList", collectGoodsList);
        map.put("allGoodsList", pager);
        return map;
    }

    public CompressImg getProtoImg(String imgurl) {
        return postService.getProtoImg(imgurl);
    }


    /**
     * 帖子点赞操作处理
     *
     * @param id     帖子id
     * @param userid
     * @return
     */
    public int doZanWithPost(String id, String userid) {

        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(id));
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("intime", new Date());
        //查询当前用户是否已点赞该帖
        int count = postService.queryIsZanPost(parammap);
        if (count != 0) return -1;
        //增加帖子热度
        facadeHeatValue.addHeatValue(Integer.parseInt(id), 3, Integer.valueOf(userid));

        //查看用户点赞操作行为，并记录积分流水
        UserOperationRecord entiy = userOperationRecordService.queryUserOperationRecordByUser(Integer.parseInt(userid));
        handleZanStatusAndZanPoint(userid, entiy);

        //插入点赞历史记录
        postService.insertZanRecord(parammap);
        //更新帖子点赞数量字段
        int updateColumn = postService.updatePostByZanSum(Integer.parseInt(id));
        //推送点赞事件
        if (sendPushInfoByZan(id, userid, updateColumn)) return 1;

        return -1;
    }

    /**
     * 推送赞消息
     *
     * @param postid
     * @param userid
     * @param type
     * @return
     */
    private boolean sendPushInfoByZan(String postid, String userid, int type) {
        if (type == 1) {
            imFacade.sendPushByCommonWay(userid, postid, ImConstant.PUSH_TYPE.zan.getCode(), null);
            return true;
        }
        return false;
    }

    /**
     * “我的”模块个人积分任务 增加积分的公共代码
     * 判断该用户有没有首次关注过圈子，或有没有点赞过帖子评论，或有没有收藏过商品帖子活动
     *
     * @param userid
     * @param entiy
     */
    public void handleZanStatusAndZanPoint(String userid, UserOperationRecord entiy) {
        if (null == entiy || entiy.getIszan() == 0) {
            //首次点赞，记录积分流水
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.first_support.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
            //用来记录是否首次收藏
            UserOperationRecord userOperationRecord = new UserOperationRecord();
            userOperationRecord.setUserid(Integer.parseInt(userid));
            userOperationRecord.setIszan(1);
            if (null == entiy) {
                //不存在，新增
                userOperationRecordService.insertUserOperationRecord(userOperationRecord);
            } else if (entiy.getIszan() == 0) {
                //存在，更新
                userOperationRecordService.updateUserOperationRecord(userOperationRecord);
            }
        }
    }

    /**
     * 帖子举报
     *
     * @param userid
     * @param postid
     * @return
     */
    public int insertPostByAccusation(String userid, String postid) {
        Accusation acc = new Accusation();
        Map<String, Integer> map = new HashedMap();
        map.put("userid", Integer.parseInt(userid));
        map.put("postid", Integer.parseInt(postid));
        //先去举报表中查询用户是否已经被举报过改帖子
        List<Accusation> sum = accusationService.queryAccusationByUserSum(map);
       /* System.out.print(sum);*/
        if (sum.size() == 0) {//用户没有被举报过该帖子,在表中插入数据
            int circleid = postService.queryPostByCircleid(postid);//查询该帖子所属圈子
            acc.setPostid(Integer.parseInt(postid));
            acc.setUserid(Integer.parseInt(userid));
            acc.setCircleid(circleid);
            acc.setNum(1);
            acc.setIntime(new Date());
            acc.setType(1);
            return accusationService.insertPostByAccusation(acc);//插入数据
        } else {
            //用户已经被举报过该帖子，举报成功返回状态200
            return 200;
        }
    }

    public List<PostVo> queryAllActive(Paging<PostVo> pager) {

        List<PostVo> activeList = postService.queryAllActive(pager);

        for (int i = 0; i < activeList.size(); i++) {

            //遍历所有的活动开始时间和结束时间，计算活动距离结束的剩余天数
            Date begin = activeList.get(i).getBegintime();//活动开始时间
            Date end = activeList.get(i).getEndtime();//活动结束时间
            Date now = new Date();//活动当前时间
            if (now.before(begin)) {
                activeList.get(i).setEnddays(-1);//活动还未开始
            } else if (end.before(now)) {
                activeList.get(i).setEnddays(0);//活动已结束
            } else if (begin.before(now) && now.before(end)) {
                try {
                    log.error("计算活动剩余结束天数");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date a = sdf.parse(sdf.format(now));
                    Date b = sdf.parse(sdf.format(end));
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(a);
                    long time1 = cal.getTimeInMillis();
                    cal.setTime(b);
                    long time2 = cal.getTimeInMillis();
                    long between_days = (time2 - time1) / (1000 * 3600 * 24);
                    activeList.get(i).setEnddays(Integer.parseInt(String.valueOf(between_days)));
                } catch (Exception e) {
                    log.error("计算活动剩余结束天数失败");
                    e.printStackTrace();
                }
            }

            //计算已投稿总数
            int postid = activeList.get(i).getId();//获取活动id
            int partsum = postService.queryActivePartSum(postid);
            activeList.get(i).setPartsum(partsum);
        }
        return activeList;
    }

    public int partActive(String postid, String userid, String title, String email, String introduction) {

        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(postid));
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("title", title);
        parammap.put("email", email);
        parammap.put("introduction", introduction);

        //先校验该用户有没有参与该活动
        int sum = postService.queryUserPartSum(parammap);

        if (sum == 0) {
            //插入一条参与记录
            return postService.saveActiveRecord(parammap);
        } else {
            return -1;
        }
    }


    /**
     * zk控制下的发帖
     *
     * @param request
     * @param userid
     * @param circleid
     * @param title
     * @param postcontent
     * @param isactive
     * @param coverimg
     * @param proids
     * @param labellist
     * @param activeid
     * @return
     */
    public Map postUnderZk(HttpServletRequest request, String userid, String circleid, String title,
                           String postcontent, String isactive, String coverimg, String proids, String labellist,
                           String activeid, Integer mark, String category) {
        DistributedLock lock = null;
        System.out.println("测试标签json》》》》》》》》》》》》》》" + labellist);
        try {
            lock = new DistributedLock(LOCK_NAME);
            //加锁
            lock.lock();
            //发帖操作
            return releaseModularPost(request, userid, circleid, title, postcontent, isactive, coverimg, proids, labellist, activeid, category);

        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "zk控制下的发帖异常");
        } finally {
            if (lock != null) {
                //释放锁
                lock.unlock();
            }
        }
    }


    /**
     * 模块化发帖
     *
     * @param request
     * @param userid
     * @param circleid
     * @param title
     * @param postcontent
     * @param isactive
     * @param coverimg
     * @param proids
     * @return
     */
    @Transactional
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map releaseModularPost(HttpServletRequest request, String userid, String circleid, String title,
                                  String postcontent, String isactive, String coverimg, String proids, String labellist,
                                  String activeid, String category) {
        Map map = new HashMap();
        validateNotNullUseridAndCircleid(userid, circleid);

        int cid = Integer.parseInt(circleid);
        int uid = Integer.parseInt(userid);

        /**
         *  这里需要根据userid判断当前登录的用户是否有发帖权限
         */
        //查询当前圈子的开放范围
        int scope = circleService.queryCircleScope(cid);
        //查询当前圈子的所有者
        User owner = circleService.queryCircleOwner(cid);
        //查询当前圈子的所有管理员列表
        List<User> manageList = circleService.queryCircleManage(cid);
        //判断该用户是否是圈子管理员
        int mark = getMarkIsCircleAdmin(userid, manageList);
        int lev = ShiroUtil.getUserLevel();     //用户等级

        log.debug("发帖用户的id是：" + uid);
        log.debug("发帖的圈子的所有者的id是：" + owner.getId());
        log.debug("发帖的圈子的scope是：" + scope);
        log.debug("发帖用户的mark是：" + mark);
        log.debug("发帖用户的等级level是：" + lev);

        //拥有权限的：1.该圈所有人均可发帖 2.该用户是该圈所有者 3.该用户是圈子管理员  4.所有者和大V可发时，发帖用户即为大V
        if (scope == 2
                || uid == owner.getId()
                || mark == 1
                || (scope == 1 && lev >= 1)) {

            try {
                log.info("APP前端用户开始请求发帖");
                Map contentMap = null;
                //封装帖子实体
                Post post = preparePostJavaBean(request, uid, cid, title, postcontent, isactive, coverimg, contentMap, activeid, category);
                //1 插入帖子
                postService.releaseModularPost(post);
                //返回的主键--帖子id
                int flag = post.getId();
                //2 再保存帖子中分享的商品列表(如果商品id字段不为空)
                insertPostShareGoods(proids, flag);
                //3 标签业务逻辑处理
                if (StringUtils.isNotBlank(labellist)) {
                    addLabelProcess(labellist, flag);
                }
                //4 积分处理
                pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.post.getCode(), uid);//完成积分任务根据不同积分类型赠送积分的公共方法（包括总分和流水）
                //5 增加用户热度
                facadeHeatValue.addUserHeatValue(2, uid);
                //6 如果是参与活动发帖，则需要记录流水
                activePartService.addRecord(activeid, uid);

                map.put("flag", flag);
                return map;

            } catch (Exception e) {
                log.error("系统异常，APP发帖失败");
                map.put("flag", -2);
                e.printStackTrace();
                return map;
            }
        } else {
            log.info("该用户不具备发帖权限");
            map.put("flag", -1);
            return map;
        }
    }

    /**
     * 新版发帖--视频帖
     * @param request
     * @param userid
     * @param circleid
     * @param activeid
     * @param title
     * @param subtitle
     * @param postcontent
     * @param isactive
     * @param labellist
     * @param proids
     * @return
     */
    public Map releaseVideoPost(HttpServletRequest request, String userid, String circleid, String activeid, String title,
                                String subtitle, String postcontent, String isactive, String labellist, String proids) {
        DistributedLock lock = null;
        System.out.println("测试标签json》》》》》》》》》》》》》》" + labellist);
        try {
            lock = new DistributedLock(LOCK_NAME);
            //加锁
            lock.lock();
            //发帖操作
            return releaseModularVideoPost(request, userid, circleid, activeid, title, subtitle, postcontent, isactive, labellist, proids);

        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "zk控制下的发视频帖异常");
        } finally {
            if (lock != null) {
                //释放锁
                lock.unlock();
            }
        }
    }

    /**
     * 模块化发帖（新版发帖--视频帖）
     * @param request
     * @param userid
     * @param circleid
     * @param title
     * @param subtitle
     * @param postcontent
     * @param isactive
     * @param proids
     * @param labellist
     * @param activeid
     * @return
     */
    @Transactional
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map releaseModularVideoPost(HttpServletRequest request, String userid, String circleid, String activeid, String title,
                                       String subtitle, String postcontent, String isactive, String labellist, String proids) {
        Map map = new HashMap();
        validateNotNullUseridAndCircleid(userid, circleid);

        int cid = Integer.parseInt(circleid);
        int uid = Integer.parseInt(userid);

        /**
         *  这里需要根据userid判断当前登录的用户是否有发帖权限
         */
        //查询当前圈子的开放范围
        int scope = circleService.queryCircleScope(cid);
        //查询当前圈子的所有者
        User owner = circleService.queryCircleOwner(cid);
        //查询当前圈子的所有管理员列表
        List<User> manageList = circleService.queryCircleManage(cid);
        //判断该用户是否是圈子管理员
        int mark = getMarkIsCircleAdmin(userid, manageList);
        int lev = ShiroUtil.getUserLevel();     //用户等级

        log.debug("发帖用户的id是：" + uid);
        log.debug("发帖的圈子的所有者的id是：" + owner.getId());
        log.debug("发帖的圈子的scope是：" + scope);
        log.debug("发帖用户的mark是：" + mark);
        log.debug("发帖用户的等级level是：" + lev);

        //拥有权限的：1.该圈所有人均可发帖 2.该用户是该圈所有者 3.该用户是圈子管理员  4.所有者和大V可发时，发帖用户即为大V
        if (scope == 2
                || uid == owner.getId()
                || mark == 1
                || (scope == 1 && lev >= 1)) {

            try {
                log.info("APP前端用户开始请求发视频帖");
                Map contentMap = null;
                //封装帖子实体
                Post post = preparePostJavaBeanNew(request, uid, cid, title, subtitle, postcontent, isactive, contentMap, activeid, 2);//最后一个入参category为2：视频贴
                //1 插入帖子
                postService.insertSelective(post);
                //返回的主键--帖子id
                int flag = post.getId();
                //2 再保存帖子中分享的商品列表(如果商品id字段不为空)
                insertPostShareGoods(proids, flag);
                //3 标签业务逻辑处理
                if (StringUtils.isNotBlank(labellist)) {
                    addLabelProcess(labellist, flag);
                }
                //4 积分处理
                pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.post.getCode(), uid);//完成积分任务根据不同积分类型赠送积分的公共方法（包括总分和流水）
                //5 增加用户热度
                facadeHeatValue.addUserHeatValue(2, uid);
                //6 如果是参与活动发帖，则需要记录流水
                activePartService.addRecord(activeid, uid);

                map.put("flag", flag);
                return map;

            } catch (Exception e) {
                log.error("系统异常，APP发帖失败");
                map.put("flag", -2);
                e.printStackTrace();
                return map;
            }
        } else {
            log.info("该用户不具备发帖权限");
            map.put("flag", -1);
            return map;
        }
    }

    /**
     * 根据请求中的参数准备Post实体(无封面coverimg)
     *
     * @param request
     * @param userid
     * @param circleid
     * @param title
     * @param subtitle
     * @param postcontent
     * @param isactive
     * @param contentMap
     * @return
     */
    private Post preparePostJavaBeanNew(HttpServletRequest request, Integer userid, Integer circleid, String title,
                                        String subtitle, String postcontent, String isactive,
                                        Map contentMap, String activeid, Integer category) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Post post = new Post();
        post.setCircleid(circleid);
        post.setTitle(title);
        post.setSubtitle(subtitle); //帖子副标题（作品描述）

        contentMap = setPostContent(request, postcontent, contentMap, post);
        post.setZansum(0);//新发帖全部默认为0次
        post.setCommentsum(0);//被评论次数
        post.setForwardsum(0);//被转发次数
        post.setCollectsum(0);//被收藏次数
        post.setIsactive(Integer.parseInt(isactive));//是否为活动 0 帖子 1 活动
        post.setIshot(0);//是否设为热门：默认0否
        post.setIsessence(0);//是否设为精选：默认0否
        post.setIsessencepool(0);//是否设为精选池中的帖子
        post.setIntime(new Date());//帖子发布时间
        post.setTotalpoint(0);//帖子综合评分
        if ((int) contentMap.get("flag") == 0) {
            post.setIsdel(0);//上架
        } else if ((int) contentMap.get("flag") > 0) {
            post.setIsdel(2);
        }
        post.setUserid(userid);
        post.setHeatvalue(3000); //默认的帖子热度值
        //城市编码
        String citycode = wrapCitycode();
        post.setCity(citycode);    //使用登录时的城市一样

        if (StringUtils.isNotBlank(activeid)) {
            post.setActiveid(Integer.parseInt(activeid));
        }
        post.setCategory(category);
        return post;
    }

    /**
     * 非空校验 userid circleid
     *
     * @param userid
     * @param circleid
     */
    private void validateNotNullUseridAndCircleid(String userid, String circleid) {
        if (StringUtils.isBlank(circleid)) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "发帖的圈子id不能为空");
        }
        if (StringUtils.isBlank(userid)) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "发帖的用户id不能为空");
        }
    }

    /**
     * 发帖-标签业务逻辑处理
     *
     * @param labellist
     * @param flag
     */
    private void addLabelProcess(String labellist, int flag) {
        //1 解析数据
        Gson gson = new Gson();
        List<PostLabel> postLabelList = gson.fromJson(labellist, new TypeToken<List<PostLabel>>() {
        }.getType());    //字符串为为list
        log.debug("接口传入的标签集合：" + postLabelList.toString());

        //2 需要过滤出新建的标签和已经存在的标签
        List<PostLabel> newLabels = new ArrayList<>();  //这是新建的标签集合
        List<PostLabel> existLabels = new ArrayList<>();  //这是非新建的标签集合

        for (PostLabel p : postLabelList) {
            if (null == p.getId() || p.getId() == 0) {
                newLabels.add(p);
            } else {
                existLabels.add(p);
            }
        }

        log.debug("新建的标签集合：" + newLabels.toString());
        log.debug("非新建的标签集合：" + existLabels.toString());

        //3 下面是对新建的标签集合操作
        List<PostLabel> newPostLabelsInDB = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(newLabels)) {
            List<PostLabel> list = processForNewLabel(flag, newLabels);
            newPostLabelsInDB.addAll(list);
        }

        if (CollectionUtil.isNotEmpty(existLabels)) {
            //4 下面是对非新建的标签集合操作
            processForExistLabel(flag, existLabels);
        }
        //5 记录此次发帖标签使用的历史
        List<PostLabel> allLabels = new ArrayList<>();  //所有的标签集合
        allLabels.addAll(newPostLabelsInDB);
        allLabels.addAll(existLabels);
        for (PostLabel label : allLabels) {
            //使用过的标签，插入mongoDB
            saveKeywordsInMongoDB(label);
            //增加标签热度,同时老标签的使用次数+1
            facadeHeatValue.addLabelHeatValue(2, label.getId(), null);
        }

    }

    /**
     * 把使用过的标签 存入mongoDB
     */
    private void saveKeywordsInMongoDB(PostLabel label) {
        if (StringUtil.isNotBlank(label.getName())) {
            LabelSearchTerms labelSearchTerms = new LabelSearchTerms();

            labelSearchTerms.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
            labelSearchTerms.setIntime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));  //日期
            labelSearchTerms.setName(label.getName());  //标签名称
            labelSearchTerms.setType(label.getType());  //标签类型
            labelSearchTerms.setLabelid(label.getId());  //标签id
            labelSearchTerms.setUserid(ShiroUtil.getAppUserID());  //不登录的情况下，返回0
            labelSearchTerms.setIsdel(0);
            labelSearchTermsService.insert(labelSearchTerms);
        }
    }

    private void processForExistLabel(int flag, List<PostLabel> existLabels) {
        //4.1 整理非新建标签的id集合
        List<Integer> existLabelIdList = new ArrayList<>();
        for (PostLabel p : existLabels) {
            existLabelIdList.add(p.getId());
        }
        //4.2 插入标签和帖子关系数据
        batchAddPostLabelRealtionByList(flag, existLabelIdList);
    }

    private List<PostLabel> processForNewLabel(int flag, List<PostLabel> newLabels) {
        //3.1 从缓存中获取userid，citycode, 并且根据标签类型，获取标签的头像
        for (PostLabel p : newLabels) {
            //标签创建人
            p.setUserid(ShiroUtil.getAppUserID());
            //标签的头像
            setDefaultPhotoByLabelType(p);
        }
        //3.2 插入标签表数据
        postLabelService.batchInsert(newLabels);
        //3.3 插入标签和帖子关系数据
        String[] labelNameStr = new String[newLabels.size()];
        for (int i = 0; i < newLabels.size(); i++) {
            labelNameStr[i] = newLabels.get(i).getName();
        }
        log.debug("新插入的标签的名称集合是：" + labelNameStr.toString());

        List<Integer> newLabelIdList = postLabelService.queryLabelIdList(labelNameStr);
        log.debug("新插入的标签的id集合是：" + newLabelIdList.toString());

        batchAddPostLabelRealtionByList(flag, newLabelIdList);
        //返回新增的标签实体集合
        return postLabelService.queryLabelList(labelNameStr);
    }

    /**
     * 根据标签类型，获取标签的头像
     *
     * @param p
     */
    private void setDefaultPhotoByLabelType(PostLabel p) {
        int type = p.getType();
        p.setType(type);
        if (type == PostLabelConstants.TYPE.geog.getCode()) {
            p.setPhoto(PostLabelConstants.DEFAULT_GEOG_PHOTO);

        } else if (type == PostLabelConstants.TYPE.normal.getCode()) {
            p.setPhoto(PostLabelConstants.DEFAULT_NORMAL_PHOTO);
        } else {
            log.error("不支持的标签类型");
        }
    }

    private void batchAddPostLabelRealtionByList(int flag, List<Integer> newLabelIdList) {
        Map postlabelrelationMap = new HashedMap();
        postlabelrelationMap.put("postid", flag);
        postlabelrelationMap.put("labelids", newLabelIdList.toArray());
        postLabelRelationService.batchAdd(postlabelrelationMap);
    }

    /**
     * 判断该用户是否是圈子管理员
     *
     * @param userid
     * @param manageList
     * @return
     */
    private int getMarkIsCircleAdmin(String userid, List<User> manageList) {
        int mark = 0;//定义一个userid比对标志位
        if (manageList.size() > 0) {
            for (int i = 0; i < manageList.size(); i++) {
                if (manageList.get(i).getId() == Integer.parseInt(userid)) {
                    //是圈子管理员时赋值为1
                    mark = 1;
                }
            }
        }
        return mark;
    }

    /**
     * 根据请求中的参数准备Post实体(有封面coverimg)
     *
     * @param request
     * @param userid
     * @param circleid
     * @param title
     * @param postcontent
     * @param isactive
     * @param coverimg
     * @param contentMap
     * @return
     */
    private Post preparePostJavaBean(HttpServletRequest request, Integer userid, Integer circleid, String title,
                                     String postcontent, String isactive,
                                     String coverimg, Map contentMap, String activeid, String category) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Post post = new Post();
        post.setCircleid(circleid);
        post.setTitle(title);

        contentMap = setPostContent(request, postcontent, contentMap, post);
        post.setZansum(0);//新发帖全部默认为0次
        post.setCommentsum(0);//被评论次数
        post.setForwardsum(0);//被转发次数
        post.setCollectsum(0);//被收藏次数
        post.setIsactive(Integer.parseInt(isactive));//是否为活动 0 帖子 1 活动
        // post.setType(Integer.parseInt(type));//帖子类型 0 普通图文帖 1 原生视频帖 2 分享视频帖
        post.setIshot(0);//是否设为热门：默认0否
        post.setIsessence(0);//是否设为精选：默认0否
        post.setIsessencepool(0);//是否设为精选池中的帖子
        post.setIntime(new Date());//帖子发布时间
        post.setTotalpoint(0);//帖子综合评分
        if (StringUtil.isNotEmpty(category)) {
            post.setCategory(Integer.parseInt(category));
        }
        if ((int) contentMap.get("flag") == 0) {
            post.setIsdel(0);//上架
        } else if ((int) contentMap.get("flag") > 0) {
            post.setIsdel(2);
        }
        if (StringUtil.isNotEmpty(coverimg)) {
            post.setCoverimg(coverimg); //帖子封面
        }
        post.setUserid(userid);
        post.setHeatvalue(3000); //默认的帖子热度值
        //城市编码
        String citycode = wrapCitycode();
        post.setCity(citycode);    //使用登录时的城市一样

        if (StringUtils.isNotBlank(activeid)) {
            post.setActiveid(Integer.parseInt(activeid));
        }
        return post;
    }

    /**
     * 内容转换
     *
     * @param request
     * @param postcontent
     * @param contentMap
     * @param post
     * @return
     */
    private Map setPostContent(HttpServletRequest request, String postcontent, Map contentMap, Post post) {
        if (StringUtil.isNotEmpty(postcontent)) {
            //内容转换
            contentMap = jsoupCompressImg.newCompressImg(request, postcontent);
            log.debug("转换后的帖子内容是：" + contentMap);
            if ((int) contentMap.get("code") == 200) {
                String str = contentMap.get("content").toString();
                postcontent = str;
            } else {
                log.error("APP端帖子图片内容转换异常");
            }
        }
        post.setPostcontent(postcontent);//帖子内容
        return contentMap;
    }

    /**
     * 封装citycode
     * 来源：1 经纬度 2 ip
     *
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private String wrapCitycode() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String lat = ShiroUtil.getLatitude();
        String lng = ShiroUtil.getLongitude();
        log.debug("缓存中的lat=" + lat);
        log.debug("缓存中的lng=" + lng);
        Map<String, Object> map = addressFacade.getAddressByLatAndLng(lat, lng);
        int flag = Integer.valueOf(String.valueOf(map.get("flag")));
        String citycode = null;
        if (flag == 1) {
            //根据经纬度获取城市code
            citycode = String.valueOf(map.get("citycode"));
            log.info("根据经纬度获取城市code=" + citycode);
        } else {
            //根据ip获取城市code
            citycode = ShiroUtil.getIpCity();
            log.info("根据ip获取城市code=" + citycode);
        }
        return citycode;
    }

    /**
     * 保存帖子中分享的商品列表(如果商品id字段不为空)
     *
     * @param proids
     * @param flag
     */
    private void insertPostShareGoods(String proids, int flag) {
        if (!StringUtils.isEmpty(proids)) {
            String[] proidstr = proids.split(",");
            List<PostShareGoods> postShareGoodsList = new ArrayList<>();
            for (int i = 0; i < proidstr.length; i++) {
                PostShareGoods postShareGoods = new PostShareGoods();
                int postid = flag;
                int goodsid = Integer.parseInt(proidstr[i]);
                postShareGoods.setPostid(postid);
                postShareGoods.setGoodsid(goodsid);
                postShareGoodsList.add(postShareGoods);
            }
            postService.insertPostShareGoods(postShareGoodsList);
        }
    }

    /**
     * @param file
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public Map updateAppUpimg(MultipartFile file, String x, String y, String w, String h) {

        //1上传到服务器
        Map m = movisionOssClient.uploadMultipartFileObject(file, "img");
        String url = String.valueOf(m.get("url"));//获取上传到服务器上的原图
        System.out.println("上传封面的原图url==" + url);

        Map compressmap = new HashMap();
        //2从服务器获取文件并剪切,上传剪切后图片上传阿里云
        //Map map = movisionOssClient.uploadImgerAndIncision(url, x, y, w, h);
        //切割图片上传到阿里云
        Map whs = new HashMap();
        whs.put("w", w);
        whs.put("h", h);
        whs.put("x", x);
        whs.put("y", y);
        Map tmpurl = xmlParseFacade.imgCuttingUpload(url, whs);
        compressmap.put("url", tmpurl.get("new"));
        return compressmap;
    }

    /**
     * 上传帖子封面图片
     *
     * @param file 上传文件
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public Map updateCoverImgByPC(MultipartFile file, String x, String y, String w, String h, String type) {
        //1上传到服务器
        Map m = movisionOssClient.uploadMultipartFileObject(file, "img");
        String url = String.valueOf(m.get("url"));//获取上传到服务器上的原图
        System.out.println("上传封面的原图url==" + url);

        Map compressmap = null;
        //2从服务器获取文件并剪切,上传剪切后图片上传阿里云
        //Map map = movisionOssClient.uploadImgerAndIncision(url, x, y, w, h);
        //切割图片上传到阿里云
        Map whs = new HashMap();
        whs.put("w", w);
        whs.put("h", h);
        whs.put("x", x);
        whs.put("y", y);
        Map tmpurl = xmlParseFacade.imgCuttingUpload(url, whs);

        //3获取本地服务器中切割完成后的图片
        String tmpurls = String.valueOf(tmpurl.get("to"));
        System.out.println("切割完成后的本地图片绝对路径===" + tmpurls);
        String rawimg = String.valueOf(tmpurl.get("new"));

        //4对本地服务器中切割好的图片进行压缩处理
        int wt = 0;//图片压缩后的宽度
        int ht = 0;//图片压缩后的高度440
        if (type.equals(1) || type.equals("1")) {//用于区分上传帖子封面还是活动方形图
            InputStream is = null;
            try {
            /*wt = 750;
            ht = 440;*/
                //返回图片的宽高
                //BufferedImage bi = ImageIO.read(file.getInputStream());
                File file1 = new File(tmpurls);
                is = new FileInputStream(file1);
                BufferedImage bi = ImageIO.read(is);
                //获取图片压缩比例
                Double ratio = systemLayoutService.queryFileRatio("file_compress_ratio");
                wt = (int) Math.ceil(bi.getWidth() * ratio);
                ht = (int) Math.ceil(bi.getHeight() * ratio);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            wt = 440;
            ht = 440;
        }
        //新增压缩部分
        File fs = new File(tmpurls);
        Long fsize = fs.length();//获取文件大小
        String compressUrl = null;
        if (fsize > 800 * 1024) {
            //对图片压缩处理
            compressUrl = coverImgCompressUtil.ImgCompress(tmpurls, wt, ht);
            System.out.println("压缩完的切割图片url==" + compressUrl);
        } else {
            //对宽高值去除小数点
            String ww = null;
            String hh = null;
            if (StringUtil.isNotEmpty(w) && StringUtil.isNotEmpty(h)) {
                if (w.indexOf(".") != -1) {
                    ww = w.substring(0, w.lastIndexOf("."));
                } else {
                    ww = w;
                }

                if (h.indexOf(".") != -1) {
                    hh = h.substring(0, h.lastIndexOf("."));
                } else {
                    hh = h;
                }
                compressUrl = coverImgCompressUtil.ImgCompress(tmpurls, Integer.parseInt(ww), Integer.parseInt(hh));
                System.out.println("压缩完的切割图片url==" + compressUrl);
            }
        }
        //5对压缩完的图片上传到阿里云
        compressmap = aliOSSClient.uploadInciseStream(compressUrl, "img", "coverIncise");
        String newurl = String.valueOf(compressmap.get("url"));
        compressmap.put("url", newurl);

        File f = new File(url);
        f.length();
        File fdel = new File(url);
        long l = fdel.length();
        float size = (float) l / 1024 / 1024;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        String filesize = df.format(size);//返回的是String类型的
        //把切好的原图和压缩图存放表中
        CompressImg compressImg = new CompressImg();
        compressImg.setCompressimgurl(newurl);
        compressImg.setProtoimgsize(filesize);
        compressImg.setProtoimgurl(rawimg);
        compressImg.setIntime(new Date());
        compressImgService.insert(compressImg);
        log.info("帖子上传封面本地原图=========", url);
        log.info("帖子上传封面本地切割图=========", tmpurls);
        log.info("帖子上传封面本地压缩图=========", compressUrl);

        //6删除本地原图，切割图，压缩图
        File f1 = new File(url);
        f1.delete();
        File f2 = new File(tmpurls);
        f2.delete();
        File f3 = new File(compressUrl);
        f3.delete();
        return compressmap;
    }

    /**
     * 推荐
     *
     * @param userid
     * @param
     * @return
     */
    public List recommendPost(String userid, String device) {
        long count = mongodbCount();
        List<PostVo> list = null;
        List<PostVo> alllist = postService.findAllPostListHeat();//查询所有帖子
        List<PostVo> posts = new ArrayList<>();
        List<DBObject> listmongodb = null;
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
                    listmongodb=userRefulshListMongodb(Integer.parseInt(userid),1);
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
                    listmongodb=userRefulshListMongodb(Integer.parseInt(userid),1);
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
                    listmongodb=userRefulshListMongodb(Integer.parseInt(userid),1);
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
        listmongodb = userRefulshListMongodbToDevice(device, 1);
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
        listmongodba = userRefulshListMongodbToDevice(device, 1);//用户有没有看过
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
                listmongodba = userRefulshListMongodbToDevice(device, 3);//用户有没有看过
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
                }
                Set<PostVo> linkedHashSet = new LinkedHashSet<PostVo>(list);
                list = new ArrayList<PostVo>(linkedHashSet);
                ComparatorChain chain = new ComparatorChain();
                chain.addComparator(new BeanComparator("intime"), true);//true,fase正序反序
                Collections.sort(list, chain);
                list = NotLoginretuenList(list, 3, device, -1);
                return list;
            } else {//已登录
                //根据地区查询帖子
                //listmongodba = userRefulshListMongodb(Integer.parseInt(userid), 3);//用户有没有看过
                listmongodba = userRefulshListMongodbToDevice(device, 3);//用户有没有看过
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
                    chain.addComparator(new BeanComparator("intime"), true);//true,fase正序反序
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
                    chain.addComparator(new BeanComparator("intime"), true);//true,fase正序反序
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
            listmongodba = userRefulshListMongodbToDeviceHistory(device, 4, String.valueOf(circleid));//用户有没有看过
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
            listmongodba = userRefulshListMongodbToDeviceHistory(device, 4, String.valueOf(circleid));//用户有没有看过
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
     * @param userid 登录人id
     * @param device 登录设备号
     * @return
     */
    public List followPost(String userid, String device) {

        List<PostVo> list = null;   //返回值

        if (userid != null) {   //登录场景下
            List<PostVo> allList = new ArrayList<>();
            //封装所有关注的帖子
            wrapAllFollowPost(userid, allList);

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
            List<DBObject> listmongodba = queryUserViewRecordByUseridTypeDevice(Integer.valueOf(userid), 2, device);

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
     * 封装所有关注的帖子
     *
     * @param userid
     * @param allList
     */
    public void wrapAllFollowPost(String userid, List<PostVo> allList) {
        log.debug("******首页-关注*****的userid:" + userid);
        List<Integer> followCricle = postService.queryFollowCricle(Integer.parseInt(userid));//查询用户关注的圈子
        List<Integer> followUsers = postService.queryFollowUser(Integer.parseInt(userid));//用户关注的作者
        List<Integer> followLabel = postLabelService.labelId(Integer.parseInt(userid));//用户关注标签

        //先查询该用户自己的帖子
        List<PostVo> selfPost = postService.findUserPost(Integer.valueOf(userid));
        if (ListUtil.isNotEmpty(selfPost)) {
            allList.addAll(selfPost);
        }

        //根据圈子查询所有帖子
        if (followCricle.size() != 0) {
            List<PostVo> circleList = postService.queryPostListByIds(followCricle);
            allList.addAll(circleList);
        }
        //根据作者查询所有帖子
        if (followUsers.size() != 0) {
            List<PostVo> userPost = postService.queryUserListByIds(followUsers);
            if (allList != null) {
                allList.addAll(userPost);
            }
        }
        //根据标签查询所有的帖子
        if (followLabel.size() != 0) {
            List<PostVo> labelPost = postService.queryLabelListByIds(followLabel);
            if (labelPost.size() != 0) {
                allList.addAll(labelPost);
            }
        }
        log.debug("**********[wrapAllFollowPost]首页关注的allList**********" + allList.toString());
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
            listmongodba = userRefulshListMongodbToDeviceHistoryLabelid(device, 5, labelid);//用户有没有看过
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
            listmongodba = userRefulshListMongodbToDeviceHistoryLabelid(device, 5, labelid);//用户有没有看过
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
     * 查询用户不喜欢的帖子
     *
     * @param userid
     * @return
     */
    public List queryUserDontLikePost(int userid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor cursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userDontLike");

            BasicDBObject queryObject = new BasicDBObject("userid", userid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            cursor = table.find(queryObject, keys);
            list = cursor.toArray();
            cursor.close();
        } catch (Exception e) {
            log.error("查询用户浏览历史失败", e);
        } finally {
            if (null != db) {
                closeMongoDBConnect(mongoClient, db, cursor);
            }
        }
        return list;
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
            //代码分页
            list = pageFacade.getPageList(lists, 1, 10);

            findUser(list);
            findPostLabel(list);    //查询帖子的标签（不好合并）
            findHotComment(list);   //查询帖子的最热评论（不好合并）
            countView(list);
            findAllCircleName(list);
            insertmongo(list, userid, type, device, labelid);   //刷新出来的帖子，依次插入mongoDB中
            zanIsPost(Integer.parseInt(userid), list);  //查询用户有没有点赞该帖子
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
            findUser(list); //根据userid查询作者信息
            findPostLabel(list);    //根据postid查询标签信息
            findHotComment(list);   //根据postid查询所有评论
            countView(list);    //根据postid查询帖子的浏览量
            findAllCircleName(list);
            insertmongo(list, "", type, device, labelid);
        }
        return list;
    }

    /**
     * 返回数据
     *
     * @param lists
     * @param
     * @return
     */
    public List NotLoginretuenListPo(List<PostVo> lists, ServicePaging<PostVo> paging) {
        List<PostVo> list = null;
        if (lists != null) {
            paging.setTotal(lists.size());
            list = pageFacade.getPageList(lists, paging.getCurPage(), paging.getPageSize());
            findUser(list);
            findPostLabel(list);
            findHotComment(list);
            countView(list);
            findAllCircleName(list);
        }
        return list;
    }

    /**
     * 查询用户信息
     *
     * @param
     * @return
     */
    public List findUser(List<PostVo> list) {
        UserLike userLikes = null;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                int userid = list.get(i).getUserid();
                userLikes = userService.findUser(userid);
                list.get(i).setUserlike(userLikes);
            }
        }
        return list;
    }

    /**
     * 优化
     *
     * @param list
     * @param userid
     * @return
     */
    public List findAllReturn(List<PostVo> list, String userid) {
        Map map = new HashMap();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                int postid = list.get(i).getId();
                map.put("postid", postid);
                map.put("userid", userid);
                //查出展示的一些必要字段，比如作者信息，点赞数，浏览数等
                PostReturnAll postReturnAlls = postService.postReAll(map);
                list.get(i).setPostReturnAlls(postReturnAlls);
            }
        }
        return list;
    }


    /**
     * 帖子浏览量 (公共方法)
     *
     * @param list
     * @return
     */
    public List countView(List<PostVo> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                int id = list.get(i).getId();
                int count = postService.queryPostCountView(id);
                list.get(i).setCountview(count);
            }
        }
        return list;
    }

/*
        if (list != null) {
            List<Integer> iList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                int postid = list.get(i).getId();
                iList.add(postid);
            }
            List<PostVo> resultList = new ArrayList<>();
            List<UserReflushCount> userReflushCountList = userRefreshRecordService.countPostViewCountByUserid(iList);
            for (int j = 0; j < userReflushCountList.size(); j++) {
                for (int i = 0; i < list.size(); i++) {
                    int poscount = postAndUserRecordService.postcount(list.get(i).getId());
                    if (userReflushCountList.get(j).getPostid().intValue() == list.get(i).getId().intValue()) {
                        list.get(i).setCountview(userReflushCountList.get(j).getCount() + poscount);
                        resultList.add(list.get(i));
                    }
                }
            }
          *//*  for (int i = 0; i < list.size(); i++) {
                int postid = list.get(i).getId();
                int uesrreflushCounts = userRefreshRecordService.postcount(postid);
                int poscount = postAndUserRecordService.postcount(postid);
                int count = uesrreflushCounts + poscount;
                list.get(i).setCountview(count);
            }*//*

        }
        return list;
    }*/


    /**
     * 查询帖子标签
     *
     * @param list
     * @return
     */
    public List<PostVo> findPostLabel(List<PostVo> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                List<PostLabel> postLabels = new ArrayList<>();
                //帖子的id
                int postid = list.get(i).getId();
                //获取帖子对应的圈子，加入到标签展示栏
                addCircleToLabellist(list, i, postLabels);
                //获取帖子对应的活动，加入到标签展示栏
                addActiveToLabellist(list, i, postLabels);
                //获取帖子对应的标签
                List<PostLabel> labelInDB = postService.queryPostLabel(postid);
                postLabels.addAll(labelInDB);
                list.get(i).setPostLabels(postLabels);
            }
        }
        return list;
    }

    /**
     * 把该帖子所属于的活动，封装成标签实体，加入到标签列表，方便app端展示。
     *
     * @param list
     * @param i
     * @param postLabels
     */
    private void addActiveToLabellist(List<PostVo> list, int i, List<PostLabel> postLabels) {
        Integer activeid = list.get(i).getActiveid();
        if (activeid != null) {
            PostLabel label = new PostLabel();
            label.setId(activeid);
            label.setType(PostLabelConstants.TYPE.active.getCode());
            //查询该帖子属于活动的名称
            Post active = postService.selectTitleById(activeid);
            label.setName(active.getTitle());
            postLabels.add(label);
        }
    }

    /**
     * 把该帖子所属于的圈子，封装成标签实体，加入到标签列表，方便app端展示。
     *
     * @param list
     * @param i
     * @param postLabels
     */
    private void addCircleToLabellist(List<PostVo> list, int i, List<PostLabel> postLabels) {
        Integer circleid = list.get(i).getCircleid();
        if (circleid != null) {
            PostLabel label = new PostLabel();
            label.setId(circleid);
            label.setType(PostLabelConstants.TYPE.circle.getCode());
            label.setName(list.get(i).getCirclename());
            postLabels.add(label);
        }
    }


    /**
     * 查询用户有没有点赞该帖子
     *
     * @param userid
     * @param list
     * @return
     */
    public List zanIsPost(int userid, List<PostVo> list) {
        //根据userid查询该用户有没有点赞
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Map map = new HashMap();
                map.put("userid", userid);
                map.put("postid", list.get(i).getId());
                int iszan = postService.zanIsPost(map);
                if (iszan > 0) {
                    list.get(i).setIsZan(1);
                } else {
                    list.get(i).setIsZan(0);
                }
            }
        }
        return list;
    }

    /**
     * 查询圈子名称
     *
     * @param list
     * @return
     */
    public List findAllCircleName(List<PostVo> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Integer circleid = list.get(i).getCircleid();
                if (circleid != null) {
                    String circlename = circleService.queryCircleName(circleid);
                    list.get(i).setCirclename(circlename);
                }
            }
        }
        return list;
    }

    /**
     * 精選評論(找到每个帖子热度最高的评论)
     *
     * @param list
     * @return
     */
    public List<PostVo> findHotComment(List<PostVo> list) {

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                int postid = list.get(i).getId();
                //根據帖子id去查热度最高的評論
                CommentVo comments = commentService.queryCommentByPost(postid);
                list.get(i).setComments(comments);
            }
        }

        return list;
    }

    /***
     * 下拉刷新下拉刷新_20180103修改
     * @param userid
     * @param
     * @param type
     * @param
     * @return
     */
    public List userRefreshListNew_20180103(String userid, String device, int type, String lat, String circleid, String lng) {
        List<PostVo> list = null;
        if (type == 1) {//推荐
            list = recommendPost(userid, device);
        } else if (type == 2) {//关注
            list = followPost(userid, device);
        } else if (type == 3) {//本地
            list = localhostPost(userid, lat, device, lng);
        } else if (type == 4) {//圈子c
            list = circleRefulsh(userid, Integer.parseInt(circleid), device);
        }
        return list;
    }

    /**
     * 插入
     *
     * @param list
     * @param userid
     */
    public void insertmongo(List<PostVo> list, String userid, int type, String device, int labelid) {
        //int crileid = 0;//圈子id
        if (list != null && userid != null) {
            for (int i = 0; i < list.size(); i++) {
                int id = list.get(i).getId();
                Object circleid = list.get(i).getCircleid();
                if (circleid == null) {
                    circleid = 0;
                }
                //查询帖子是哪个圈子
                //crileid = postService.queryCrileid(id);
                //刷新记录插入mongodb
                insertMongoDB(userid, id, Integer.parseInt(circleid.toString()), type, device, labelid);
                //增加帖子浏览记录
                facadeHeatValue.addHeatValue(list.get(i).getId(), 8, 666666);
            }
        }
    }


    /**
     * 在mongodb中查询用户刷新浏览过的列表
     *
     * @param userid
     * @return
     */
    public List userRefulshListMongodb(int userid, int type) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("type", type);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 根据设备号，用户id, 首页类型查询用户的浏览历史记录
     *
     * @param userid
     * @param type
     * @param device
     * @return
     */
    public List queryUserViewRecordByUseridTypeDevice(int userid, int type, String device) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("type", type).append("device", device);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("根据设备号，用户id, 首页类型查询用户的浏览历史记录失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 根据用户id查询帖子浏览历史
     *
     * @param userid
     * @return
     */
    public List userRefulshListMongodbs(int userid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("userid", userid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("根据用户id查询帖子浏览历史失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 在mongodb中查询用户刷新浏览过的列表
     *
     * @param device
     * @param type
     * @return
     */
    public List userRefulshListMongodbToDevice(String device, int type) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 公共方法 关闭mongoDB的连接
     *
     * @param mongoClient
     * @param db
     * @param dbCursor
     */
    public void closeMongoDBConnection(MongoClient mongoClient, DB db, DBCursor dbCursor) {
        if (null != db) {
            db.requestDone();
            db = null;
            dbCursor.close();
            mongoClient.close();
        }
    }


    /**
     * 根据设备号和首页类型查询浏览记录，从第11条开始返回数据(历史记录用)
     *
     * @param device
     * @param type
     * @return
     */
    public List userRefulshListMongodbToDeviceSkip(String device, int type) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).skip(10).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("根据设备号和首页类型查询浏览记录，从第11条开始返回数据(历史记录用)失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }


    /**
     * 根据设备号，首页类型，圈子id查询帖子浏览量(场景：未登录-圈子)
     *
     * @param device
     * @param type
     * @param circleid
     * @return
     */
    public Integer userHistoryDeviceCircleCount(String device, int type, String circleid) {
        MongoClient mongoClient = null;
        int obj = 0;
        DB db = null;
        DBCursor cursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type).append("crileid", circleid);
            cursor = collection.find(queryObject);
            obj = cursor.count();
            cursor.close();
        } catch (Exception e) {
            log.error("根据设备号，首页类型，圈子id查询帖子浏览量(场景：未登录-圈子)失败", e);
        } finally {
            if (null != db) {
                closeMongoDBConnect(mongoClient, db, cursor);
            }
        }
        return obj;
    }

    /**
     * 根据设备号，首页类型，标签id查询帖子浏览量(场景：未登录标签)
     *
     * @param device
     * @param type
     * @param labelid
     * @return
     */
    public Integer userHistoryDeviceLabelCount(String device, int type, int labelid) {
        MongoClient mongoClient = null;
        int obj = 0;
        DB db = null;
        DBCursor cursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type).append("labelid", labelid);
            cursor = collection.find(queryObject);
            obj = cursor.count();
            cursor.close();
        } catch (Exception e) {
            log.error("根据设备号，首页类型，标签id查询帖子浏览量(场景：未登录标签)失败", e);
        } finally {
            if (null != db) {
                closeMongoDBConnect(mongoClient, db, cursor);
            }
        }
        return obj;
    }

    /**
     * 根据设备号，首页类型查询帖子浏览量(场景：未登录)
     *
     * @param device
     * @param type
     * @return
     */
    public Integer userHistoryDeviceCount(String device, int type) {
        MongoClient mongoClient = null;
        int obj = 0;
        DB db = null;
        DBCursor cursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type);
            cursor = collection.find(queryObject);
            obj = cursor.count();
            cursor.close();
        } catch (Exception e) {
            log.error("根据设备号，首页类型查询帖子浏览量(场景：未登录)失败", e);
        } finally {
            if (null != db) {
                closeMongoDBConnect(mongoClient, db, cursor);
            }
        }
        return obj;
    }

    /**
     * 根据userid,type查询帖子浏览量
     *
     * @param userid
     * @param type
     * @return
     */
    public Integer userHistoryCount(int userid, int type) {
        MongoClient mongoClient = null;
        int obj = 0;
        DB db = null;
        DBCursor cursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("type", type);
            cursor = collection.find(queryObject);
            obj = cursor.count();
            cursor.close();
        } catch (Exception e) {
            log.error("根据userid,type查询帖子浏览量失败", e);
        } finally {
            if (null != db) {
                closeMongoDBConnect(mongoClient, db, cursor);
            }
        }
        return obj;
    }

    /**
     * 关闭与mongoDB的连接
     *
     * @param mongoClient
     * @param db
     * @param cursor
     */
    private void closeMongoDBConnect(MongoClient mongoClient, DB db, DBCursor cursor) {
        db.requestDone();
        cursor.close();
        mongoClient.close();
    }

    /**
     * 根据userid，type,labelid查询帖子浏览量
     *
     * @param userid
     * @param type
     * @param labelid
     * @return
     */
    public Integer userHistoryLabelCount(int userid, int type, int labelid) {
        MongoClient mongoClient = null;
        int obj = 0;
        DB db = null;
        DBCursor cursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("type", type).append("labelid", labelid);
            cursor = collection.find(queryObject);
            obj = cursor.count();
            cursor.close();
        } catch (Exception e) {
            log.error("根据userid，type,labelid查询帖子浏览量失败", e);
        } finally {
            if (null != db) {
                closeMongoDBConnect(mongoClient, db, cursor);
            }
        }
        return obj;
    }

    /**
     * 根据userid，type, circleid查询帖子浏览量
     *
     * @param userid
     * @param type
     * @param circleid
     * @return
     */
    public Integer userHistoryCircleCount(int userid, int type, int circleid) {
        MongoClient mongoClient = null;
        int obj = 0;
        DB db = null;
        DBCursor cursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection collection = db.getCollection("userRefreshRecord");
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("type", type).append("crileid", circleid);
            cursor = collection.find(queryObject);
            obj = cursor.count();
            cursor.close();
        } catch (Exception e) {
            log.error("根据userid，type, circleid查询帖子浏览量失败", e);
        } finally {
            if (null != db) {
                closeMongoDBConnect(mongoClient, db, cursor);
            }
        }
        return obj;
    }

    /**
     * 查询用户浏览记录表的总记录数
     *
     * @return
     */
    public long mongodbCount() {
        MongoClient mongoClient = null;
        long count = 0;
        DB db = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            count = table.count();
        } catch (Exception e) {
            log.error("查询用户刷新记录表的总记录数失败", e);
        } finally {
            if (null != db) {
                db.requestDone();
                mongoClient.close();
            }
        }
        return count;
    }


    /**
     * 插入刷新记录
     *
     * @param userid
     * @param postid
     * @param crileid
     */
    public void insertMongoDB(String userid, int postid, int crileid, int type, String device, int labelid) {
        //把刷新记录插入mongodb
        UserRefreshRecord userRefreshRecord = new UserRefreshRecord();

        if (StringUtil.isNotEmpty(userid)) {
            //用户登录情况下
            userRefreshRecord.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
            userRefreshRecord.setUserid(Integer.parseInt(userid));
            userRefreshRecord.setPostid(postid);
            userRefreshRecord.setCrileid(String.valueOf(crileid));
            userRefreshRecord.setIntime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss:SSS"));
            userRefreshRecord.setType(type);
            userRefreshRecord.setDevice(device);
            userRefreshRecord.setLabelid(labelid);
        } else {
            //未登录
            userRefreshRecord.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
            userRefreshRecord.setPostid(postid);
            userRefreshRecord.setCrileid(String.valueOf(crileid));
            userRefreshRecord.setIntime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss:SSS"));
            userRefreshRecord.setType(type);
            userRefreshRecord.setDevice(device);
            userRefreshRecord.setLabelid(labelid);
        }
        userRefreshRecordService.insert(userRefreshRecord);
        //同步到mysql中帖子浏览量加1
        postService.updateCountView(postid);
    }

    public List<Map> queryPostImgById(String postid) {
        List<Map> list = new ArrayList<>();
        //查询出帖子内容
        String postcontent = postService.queryPostContentById(postid);
        JSONArray array = JSONArray.fromObject(postcontent);
        for (int i = 0; i < array.size(); i++) {
            Map map = new HashMap();//用于做内层封装
            JSONObject object = JSONObject.fromObject(array.get(i));
            if (object.getString("type").equals("1")) {//代表模块是图片
                CompressImg compressImg = new CompressImg();
                compressImg.setCompressimgurl(object.getString("value"));
                //根据压缩图片查询出原图和图片大小
                compressImg = compressImgService.queryProtoBycompress(compressImg);
                if (null != compressImg) {
                    map.put("protoimgurl", compressImg.getProtoimgurl());//原图
                    map.put("protoimgsize", compressImg.getProtoimgsize());//原图大小
                    map.put("compressimgurl", object.getString("value"));//缩略图大小
                    map.put("wh", object.getString("wh"));//图片宽高
                } else {
                    map.put("protoimgurl", object.getString("value"));//直接去压缩图的url
                    map.put("protoimgsize", "");//直接去压缩图的url
                    map.put("compressimgurl", object.getString("value"));//缩略图大小
                    map.put("wh", "");//图片宽高
                }
                list.add(map);
            }
        }

        return list;
    }


    /**
     * 首页头部列表
     *
     * @return
     */
    public List indexHomeList() {
        int sum = 0;
        Map map = new HashMap();
        //查询所有圈子
        List<CircleVo> circleVos = circleService.queryAllCircle();
        for (int i = 0; i < circleVos.size(); i++) {
            int id = circleVos.get(i).getId();//圈子id
            //根据圈子id查询圈子中所有帖子的热度合
            List<Integer> postVos = circleService.queryHeatValueById(id);
            for (int j = 0; j < postVos.size(); j++) {
                sum += postVos.get(j);
            }
            map.put("id", id);
            map.put("sum", sum);
            sum = 0;
            //根据圈子id改变圈子表热度值
            int result = circleService.updateCircleHeatValue(map);
            log.info("结构钢事实宿舍上" + result);
        }
        //查询所有标签
        /** List<PostLabel> postLabels = postLabelService.queryLabelName();
         for (int i = 0; i < postLabels.size(); i++) {
         int labelid = postLabels.get(i).getId();
         //根据标签id查询标签的帖子
         List<Integer> labelpost = postService.queryLabelPost(labelid);
         for (int j = 0; j < labelpost.size(); j++) {
         sum += labelpost.get(j);
         }
         map.put("labelid", labelid);
         map.put("sum", sum);
         sum = 0;
         //根据id改变标签的热度值
         int result = postLabelService.updateLabelHeatValue(map);
         log.info("结构钢事实上" + result);
         }*/
        //查询有没有手都推荐到首页的
        List<PostLabel> isrecommend = postLabelService.isrecommendLabel();
        //根据热度排序查询圈子
        List<CircleVo> list = circleService.queryHeatValue();
        //根据热度排序查询标签
        List<PostLabel> postLabelss = postLabelService.queryLabelHeatValue();
        List<PostLabelVo> postvo = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            PostLabelVo pos = new PostLabelVo();
            int circleid = list.get(i).getId();
            String circleName = list.get(i).getName();
            int circleHeatvalue = list.get(i).getHeatvalue();
            pos.setHeatvalue(circleHeatvalue);
            pos.setCircleid(circleid);
            pos.setName(circleName);
            postvo.add(pos);
        }
        for (int i = 0; i < postLabelss.size(); i++) {
            PostLabelVo pos = new PostLabelVo();
            int labelid = postLabelss.get(i).getId();
            String labelName = postLabelss.get(i).getName();
            int labelHeatvalue = postLabelss.get(i).getHeatValue();
            pos.setHeatvalue(labelHeatvalue);
            pos.setLabelid(labelid);
            pos.setName(labelName);
            postvo.add(pos);
        }

        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(new BeanComparator("heatvalue"), true);//true,fase正序反序
        Collections.sort(postvo, chain);
        if (isrecommend != null) {
            for (int i = 0; i < isrecommend.size(); i++) {
                PostLabelVo pos = new PostLabelVo();
                String name = isrecommend.get(i).getName();
                int heatvalue = isrecommend.get(i).getHeatValue();
                int id = isrecommend.get(i).getId();
                pos.setName(name);
                pos.setLabelid(id);
                pos.setHeatvalue(heatvalue);
                postvo.add(0, pos);
            }
        }
        List<PostLabelVo> finals = pageFacade.getPageList(postvo, 1, 15);
        return finals;
    }

    /**
     * 根据userid,type,labelid查询浏览记录
     *
     * @param userid
     * @param type
     * @param labelid
     * @return
     */
    public List userRefulshListMongodbHistory(int userid, int type, int labelid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("type", type).append("labelid", labelid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("根据userid,type,labelid查询浏览记录失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 在mongodb中查询用户刷新浏览过的列表(圈子)
     *
     * @param userid
     * @return
     */
    public List userRefulshListMongodbHistoryCircleid(int userid, int type, String circleid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("type", type).append("crileid", circleid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 在mongodb中查询用户刷新浏览过的列表(圈子)
     *
     * @param
     * @return
     */
    public List userRefulshListMongodbToDeviceHistory(String device, int type, String circleid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type).append("crileid", circleid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 在mongodb中查询用户刷新浏览过的列表(圈子)
     * 用户历史记录
     *
     * @param
     * @return
     */
    public List userRefulshListMongodbToDeviceHistorySkip(String device, int type, String circleid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type).append("crileid", circleid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 在mongodb中查询用户刷新浏览过的列表(标签)
     *
     * @param
     * @return
     */
    public List userRefulshListMongodbToDeviceHistoryLabelid(String device, int type, int labelid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type).append("labelid", labelid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 在mongodb中查询用户刷新浏览过的列表(标签)
     * 历史专用
     *
     * @param
     * @return
     */
    public List userRefulshListMongodbToDeviceHistoryLabelidSkip(String device, int type, int labelid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type).append("labelid", labelid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).skip(10).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 查询出一条记录(登录状态下)
     *
     * @param userid
     * @param type
     * @param postids
     * @return
     */
    public List queryOnlPost(int userid, int type, int postids) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("type", type).append("postid", postids);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            keys.put("intime", 1);
            dbCursor = table.find(queryObject, keys);
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 查询出一条记录(登录状态下标签的情况)
     *
     * @param userid
     * @param type
     * @param postids
     * @return
     */
    public List queryOnlPostLabel(int userid, int type, int postids, int labelid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("type", type).append("postid", postids).append("labelid", labelid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            keys.put("intime", 1);
            dbCursor = table.find(queryObject, keys);
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 查询出一条记录(登录状态下圈子的情况)
     *
     * @param userid
     * @param type
     * @param postids
     * @return
     */
    public List queryOnlPostCircleid(int userid, int type, int postids, int circleid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("userid", userid).append("type", type).append("postid", postids).append("crileid", circleid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            keys.put("intime", 1);
            dbCursor = table.find(queryObject, keys);
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 查询出一条记录(未登录状态下)
     *
     * @param device
     * @param type
     * @param postids
     * @return
     */
    public List queryOnlPostNotLogin(String device, int type, int postids) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type).append("postid", postids);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            keys.put("intime", 1);
            dbCursor = table.find(queryObject, keys);
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 查询出一条记录(未登录状态下标签)
     *
     * @param device
     * @param type
     * @param postids
     * @return
     */
    public List queryOnlPostNotLoginLabelid(String device, int type, int postids, int labelid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type).append("postid", postids).append("labelid", labelid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            keys.put("intime", 1);
            dbCursor = table.find(queryObject, keys);
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 查询出一条记录(未登录状态下圈子)
     *
     * @param device
     * @param type
     * @param postids
     * @return
     */
    public List queryOnlPostNotLoginCircleid(String device, int type, int postids, String circleid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject queryObject = new BasicDBObject("device", device).append("type", type).append("postid", postids).append("crileid", circleid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            keys.put("intime", 1);
            dbCursor = table.find(queryObject, keys);
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 根据时间查询list
     *
     * @param intime
     * @return
     */
    public List queryPosyByImtime(String intime, int userid, int type) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject gt = new BasicDBObject("$lte", intime);
            BasicDBObject queryObject = new BasicDBObject("intime", gt).append("userid", userid).append("type", type);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }


    /**
     * 根据时间查询list
     * （标签）
     *
     * @param intime
     * @return
     */
    public List queryPosyByImtimeLabel(String intime, int userid, int type, int labelid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject gt = new BasicDBObject("$lt", intime);
            BasicDBObject queryObject = new BasicDBObject("intime", gt).append("userid", userid).append("type", type).append("labelid", labelid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 根据时间查询list
     * (圈子)
     *
     * @param intime
     * @return
     */
    public List queryPosyByImtimeCircleid(String intime, int userid, int type, int circleid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject gt = new BasicDBObject("$lt", intime);
            BasicDBObject queryObject = new BasicDBObject("intime", gt).append("userid", userid).append("type", type).append("crileid", circleid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }


    /**
     * 根据时间查询list
     * (未登录)
     *
     * @param intime
     * @return
     */
    public List queryPosyByImtimeDevice(String intime, String device, int type) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject gt = new BasicDBObject("$lte", intime); //小于等于intime
            BasicDBObject queryObject = new BasicDBObject("intime", gt).append("device", device).append("type", type);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 根据时间查询list
     * (未登录)
     *
     * @param intime
     * @return
     */
    public List queryPosyByImtimeDeviceLabel(String intime, String device, int type, int labelid) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject gt = new BasicDBObject("$lte", intime);
            BasicDBObject queryObject = new BasicDBObject("intime", gt).append("device", device).append("type", type).append("labelid", labelid);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 根据时间查询list
     * (未登录)
     *
     * @param intime
     * @return
     */
    public List queryPosyByImtimeDeviceCircle(String intime, String device, int type, String circle) {
        MongoClient mongoClient = null;
        List<DBObject> list = null;
        DB db = null;
        DBCursor dbCursor = null;
        try {
            mongoClient = new MongoClient(MongoDbPropertiesLoader.getValue("mongo.hostport"));
            db = mongoClient.getDB("searchRecord");
            DBCollection table = db.getCollection("userRefreshRecord");//表名
            BasicDBObject gt = new BasicDBObject("$lte", intime);
            BasicDBObject queryObject = new BasicDBObject("intime", gt).append("device", device).append("type", type).append("crileid", circle);
            //指定需要显示列
            BasicDBObject keys = new BasicDBObject();
            keys.put("_id", 0);
            keys.put("postid", 1);
            dbCursor = table.find(queryObject, keys).sort(new BasicDBObject("intime", -1));
            list = dbCursor.toArray();
            dbCursor.close();
        } catch (Exception e) {
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            closeMongoDBConnection(mongoClient, db, dbCursor);
        }
        return list;
    }

    /**
     * 用户刷新的历史记录列表
     *
     * @param userid
     * @return
     */
    public List<PostVo> userReflushHishtoryRecord_20180103(String userid, Paging<PostVo> paging, int type, String device, String circleid, String postids) {
        log.debug("首页历史接口的传参postid=" + postids);
        log.debug("首页历史接口的传参pageNo=" + paging.getCurPage());
        List<PostVo> postVo = null;
        if (userid != null) {
            //用户登录下
            postVo = userLoginHistoryRecord(userid, paging, type, circleid, postids, postVo, device);
        } else {
            //未登录下
            postVo = userNotLoginHistoryRecord(paging, type, device, circleid, postids, postVo);
        }
        return postVo;
    }

    /**
     * 用户未登录下历史记录
     *
     * @param paging
     * @param type
     * @param device
     * @param
     * @param circleid
     * @param postids
     * @param postVo
     * @param
     * @param
     * @return
     */
    private List<PostVo> userNotLoginHistoryRecord(Paging<PostVo> paging, int type, String device, String circleid, String postids, List<PostVo> postVo) {
        if ( StringUtil.isEmpty(circleid)) {
            postVo = userNotLoginHistoryRecordThird(paging, type, device, postids);
        }
        if (StringUtil.isNotEmpty(circleid)) {
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
            List<DBObject> onlyPost = queryOnlPostNotLoginCircleid(device, type, Integer.parseInt(postids), circleid);
            if(onlyPost.size()!=0) {
                String intime = onlyPost.get(0).get("intime").toString();
                intimePost = queryPosyByImtimeDeviceCircle(intime, device, type, circleid);
            }
        } else {
            //查询用户有无历史
            int count = userHistoryDeviceCircleCount(device, type, circleid);
            if (count > 0) {
                us = userRefulshListMongodbToDeviceHistory(device, type, circleid);
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
            findUser(postVo);
            findPostLabel(postVo);
            findHotComment(postVo);
            countView(postVo);
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
            List<DBObject> onlyPost = queryOnlPostNotLoginLabelid(device, type, Integer.parseInt(postids), Integer.parseInt(labelid));
            if(onlyPost.size()!=0) {
                String intime = onlyPost.get(0).get("intime").toString();
                intimePost = queryPosyByImtimeDeviceLabel(intime, device, type, Integer.parseInt(labelid));
            }
        } else {
            //查询用户有无历史
            int count = userHistoryDeviceLabelCount(device, type, Integer.parseInt(labelid));
            if (count > 0) {
                us = userRefulshListMongodbToDeviceHistoryLabelid(device, type, Integer.parseInt(labelid));
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
            findUser(postVo);
            findPostLabel(postVo);
            findHotComment(postVo);
            countView(postVo);
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
            List<DBObject> onlyPost = queryOnlPostNotLogin(device, type, Integer.parseInt(postids));
            if(onlyPost.size()!=0) {
                String intime = onlyPost.get(0).get("intime").toString();
                intimePost = queryPosyByImtimeDevice(intime, device, type);
            }
        } else {

            if (type == 2) {
                //未登录下，【关注】下面不展示历史
                us = null;
            } else {

                int count = userHistoryDeviceCount(device, type);
                if (count > 10) {
                    us = userRefulshListMongodbToDevice(device, type);
                } else {
                    us = null;
                }
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
            findUser(postVo);
            findPostLabel(postVo);
            findHotComment(postVo);
            countView(postVo);
        }
        return postVo;
    }

    /**
     * 用户登录下的历史记录
     *
     * @param userid
     * @param paging
     * @param type
     * @param
     * @param circleid
     * @param postids
     * @param postVo
     * @param device
     * @return
     */
    private List<PostVo> userLoginHistoryRecord(String userid, Paging<PostVo> paging, int type,
                                               String circleid, String postids,
                                                List<PostVo> postVo, String device) {
        //推荐、关注、本地
        if (StringUtil.isEmpty(circleid) ) {
            postVo = userLoginHistoryRecordThird(userid, paging, type, postids, device);
        }

        if (StringUtil.isNotEmpty(circleid)) {
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
            List<DBObject> onlyPost = queryOnlPostNotLoginCircleid(device, type, Integer.parseInt(postids), circleid);
            if(onlyPost.size()!=0) {
                String intime = onlyPost.get(0).get("intime").toString();
                intimePost = queryPosyByImtimeDeviceCircle(intime, device, type, circleid);
            }
        } else {
            //查询用户有无历史
            int count = userHistoryDeviceCircleCount(device, type, circleid);
            if (count > 0) {
                us = userRefulshListMongodbToDeviceHistory(device, type, circleid);
            } else {
                us = null;
            }
        }
        //List<DBObject> list = userRefulshListMongodbHistoryCircleid(Integer.parseInt(userid), type, circleid);
        List<DBObject> dontlike = queryUserDontLikePost(Integer.parseInt(userid));
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
            findUser(postVo);
            findPostLabel(postVo);
            findHotComment(postVo);
            countView(postVo);
            zanIsPost(Integer.parseInt(userid), postVo);
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
            List<DBObject> onlyPost = queryOnlPostNotLoginLabelid(device, type, Integer.parseInt(postids), Integer.parseInt(labelid));
            if(onlyPost.size()!=0) {
                String intime = onlyPost.get(0).get("intime").toString();
                intimePost = queryPosyByImtimeDeviceLabel(intime, device, type, Integer.parseInt(labelid));
            }
        } else {
            //查询用户有无历史
            int count = userHistoryDeviceLabelCount(device, type, Integer.parseInt(labelid));
            if (count > 0) {
                us = userRefulshListMongodbToDeviceHistoryLabelid(device, type, Integer.parseInt(labelid));
            } else {
                us = null;
            }
        }
        //List<DBObject> list = userRefulshListMongodbHistory(Integer.parseInt(userid), type, Integer.parseInt(labelid));
        List<DBObject> dontlike = queryUserDontLikePost(Integer.parseInt(userid));
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
            findUser(postVo);
            findPostLabel(postVo);
            findHotComment(postVo);
            countView(postVo);
            zanIsPost(Integer.parseInt(userid), postVo);
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
            List<DBObject> onlyPost = queryOnlPostNotLogin(device, type, Integer.parseInt(postids));
            if(onlyPost.size()!=0) {
                String intime = onlyPost.get(0).get("intime").toString();
                //查出在这个时间之前的用户浏览历史
                intimePost = queryPosyByImtimeDevice(intime, device, type);
            }
        } else {    //传0，是因为app本地无缓存

            if (type == 2) {
                //【关注】下面的历史
                int count = userHistoryCount(Integer.valueOf(userid), type);
                if (count > 0) {
                    //如果是老用户，则查询关注-用户浏览记录
                    us = queryUserViewRecordByUseridTypeDevice(Integer.valueOf(userid), type, device);
                } else {
                    us = null;
                }
            } else {
                /**
                 *  【推荐】和【本地】 的历史
                 * 下面两种情况，都是用户手机无缓存，所以客户端传0
                 */
                int count = userHistoryDeviceCount(device, type);
                if (count > 10) {
                    /**
                     * 【场景】：老用户卸载重装APP，进入app首页-关注，查看历史
                     * 解决方法：1 从第11条开始查询。
                     *          2 正常传入postid, 即刚刚刷新过后的最后一条postid，即10条中热度值最小的一条，也是10条中插入mongoDB时间最晚的一条
                     *          目前选择第2种方法，客户端调第二页。
                     */
                    us = userRefulshListMongodbToDevice(device, type);
                } else {
                    /**
                     * 【场景】:新用户刚下载注册APP。
                     * 用户浏览小于等于10条，无历史记录。
                     */
                    us = null;
                }
            }

        }
        List<DBObject> dontlike = queryUserDontLikePost(Integer.parseInt(userid));
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
            findUser(postVo);
            findPostLabel(postVo);
            findHotComment(postVo);
            countView(postVo);
            zanIsPost(Integer.parseInt(userid), postVo);
        }
        return postVo;
    }

    /**
     * 活动详情里的最热最新
     *
     * @param type
     * @param postid
     * @return
     */
    public List activePostDetailHot(int type, String postid, Paging<PostVo> paging) {
        List<PostVo> list = null;
        if (type == 0) {//最热
            list = postService.findAllActivePost(Integer.parseInt(postid), paging);
            countView(list);
            findPostLabel(list);
            findAllCircleName(list);
         } else if (type == 1) {//最新
            list = postService.findAllActivePostIntime(Integer.parseInt(postid), paging);
            countView(list);
            findPostLabel(list);
            findAllCircleName(list);
        }
        return list;
    }

    /**
     * 活动详情里的最热最新
     *2017-12-20 修改 增加帖子投票总数和是否已投票
     * @param type
     * @param postid
     * @return
     */
    public List activePostDetailHot_20171220(int type, String postid, Paging<PostVo> paging,String device) {
        List<PostVo> list = null;
        if (type == 0) {//最热
            list = postService.findAllActivePost_20171220(postid, paging,device);
            countView(list);
            findPostLabel(list);
            findAllCircleName(list);
        } else if (type == 1) {//最新
            list = postService.findAllActivePostIntime_20171220(postid, paging,device);
            countView(list);
            findPostLabel(list);
            findAllCircleName(list);
        }else if(type==2){//排行榜
            list = postService.findAllActivePostTake_20171220(postid, paging,device);
            countView(list);
            findPostLabel(list);
            findAllCircleName(list);
        }
        return list;
    }


    /**
     * 关注用户
     *
     * @param userid
     * @param postid
     * @return
     */
    public int concernedAuthor(int userid, String postid) {
        Map map = new HashMap();
        //根据postid查询发帖ren
        int interestedusers = postService.postUserId(Integer.parseInt(postid));
        map.put("userid", userid);
        map.put("interestedusers", interestedusers);
        //查询该用户有没有关注过
        int result = followUserService.yesOrNo(map);
        if (result == 0) {
            FollowUser followUser = new FollowUser();
            followUser.setIntime(new Date());
            followUser.setInterestedusers(interestedusers);
            followUser.setUserid(userid);
            int count = followUserService.insertSelective(followUser);
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 获取有发帖权限的圈子
     * <p>
     * 拥有权限的：1.该圈所有人均可发帖 2.该用户是该圈所有者 3.该用户是圈子管理员  4.所有者和大V可发+发帖用户即为大V
     *
     * @return
     */
    public List<Map> getCircleInCatagory() {
        //获取有权限的圈子
        List<CirclePost> anyoneCanPostCircles = getPrivilegeCircles();
        log.debug("最终的该用户有权限的圈子列表是：" + anyoneCanPostCircles.toString());
        // 所有的catagory
        List<CircleCategory> circleCategoryVoList = circleCategoryService.queryCircleCategoryList();

        List<Map> resultList = new ArrayList<>();
        //1 需要把圈子按照不同的category进行分组
        for (CircleCategory c : circleCategoryVoList) {

            List<Map> circlelist = new ArrayList<>();
            for (CirclePost m : anyoneCanPostCircles) {
                //若是同一个category，则放到一个集合中
                if (c.getId() == (int) m.getCid()) {

                    Map map = new HashedMap();
                    map.put("circle_id", m.getCircleid());
                    map.put("circle_name", m.getCirclename());
                    circlelist.add(map);
                }
            }
            Map categoryMap = new HashedMap();
            categoryMap.put("category_id", c.getId());
            categoryMap.put("category_name", c.getCategoryname());
            categoryMap.put("category_circles", circlelist);

            resultList.add(categoryMap);
        }
        log.debug("返回的resultList：" + resultList);
        return resultList;
    }

    /**
     * 获取有权限的圈子
     *
     * @return
     */
    private List<CirclePost> getPrivilegeCircles() {
        //1 查询所有人都可以发帖的圈子
        List<CirclePost> anyoneCanPostCircles = circleService.selectCircleScopeEquals2();
        //2 查询用户是该圈子的所有者的圈子
        int userid = ShiroUtil.getAppUserID();
        List<CirclePost> createCircles = circleService.selectCircleWhoCreate(userid);
        //第一次排重合并
        anyoneCanPostCircles.removeAll(createCircles);
        anyoneCanPostCircles.addAll(createCircles);
        //3 查询用户是圈子管理员的圈子
        List<CirclePost> manageCircles = circleService.selectCircleWhoManage(userid);
        //第二次排重合并
        anyoneCanPostCircles.removeAll(manageCircles);
        anyoneCanPostCircles.addAll(manageCircles);
        //4 查询所有者可发+发帖用户为大v
        User user = userService.selectByPrimaryKey(userid);
        if (user.getLevel() >= 1) {
            List<CirclePost> ownerAndBigVCanPostCircles = circleService.selectCircleScopeEquals1();
            //第三次去重合并
            anyoneCanPostCircles.removeAll(ownerAndBigVCanPostCircles);
            anyoneCanPostCircles.addAll(ownerAndBigVCanPostCircles);
        }
        return anyoneCanPostCircles;
    }

    /**
     * 查询热门标签，
     * 1 第一个标签是用户所在城市的地理标签；
     * 如果该标签存在，则正常查出；
     * 如果该标签不存在，则使用次数0，关注数0；
     * <p>
     * 2只查询普通标签，按照热度排序
     *
     * @return
     */
    public List<PostLabel> queryHotValueLabelList() {
        //从缓存中获取当前用户的城市编码
        String ipcity = ShiroUtil.getIpCity();
        log.debug("当前登录人的登录城市代码是：" + ipcity);
        City city = cityService.selectCityByCode(ipcity);
        String cityname = city.getName();
        log.debug("当前登录人的登录城市名称是：" + cityname);

        List<PostLabel> list = new ArrayList<>();
        //查询该citycode对应的地理标签是否已经被使用过
        PostLabel geogPostLabel = postLabelService.selectGeogLabelByCitycode(ipcity);
        if (null == geogPostLabel) {
            //如果该地理标签没有被使用过，则需要实例化一个标签
            PostLabel p = new PostLabel();
            p.setCitycode(ipcity);
            p.setName(cityname);
            p.setType(PostLabelConstants.TYPE.geog.getCode());
            p.setPhoto(PostLabelConstants.DEFAULT_GEOG_PHOTO);
            p.setFans(0);
            p.setUseCount(0);
            log.debug("【标签未使用】未使用当前用户登录城市，对应的热门地理标签：" + p.toString());
            //置顶
            list.add(p);
        } else {
            log.debug("【标签已使用】未使用当前用户登录城市，对应的热门地理标签：" + geogPostLabel.toString());
            //置顶
            list.add(geogPostLabel);
        }

        //普通热门标签
        List<PostLabel> normalLabelList = postLabelService.queryHotValueLabelList(10);
        //添加到热门标签总集合中
        list.addAll(normalLabelList);

        return list;
    }


    /**
     * 关注作者
     *
     * @param userid          用户
     * @param interestedusers 被关注的用户
     * @return
     */
    public int concernedAuthorUser(int userid, int interestedusers) {
        Map map = new HashMap();
        if (userid != interestedusers) {
            map.put("userid", userid);
            map.put("interestedusers", interestedusers);
            //查询该用户有没有关注过
            int result = followUserService.yesOrNo(map);

            if (result == 0) {
                //1 插入关注用户流水
                insertUserFollowRecord(userid, interestedusers);
                //2 被关注人的粉丝数加1
                followUserService.insertUserFans(interestedusers);//被关注人
                //3 增加用户总关注数 attention
                userService.updateUserAttention(userid);//关注人
                //4 被关注人增加热度
                facadeHeatValue.addUserHeatValue(1, interestedusers);
                //5 给被关注人推送消息
                imFacade.sendPushByCommonWay(String.valueOf(userid), null, ImConstant.PUSH_TYPE.focus.getCode(), String.valueOf(interestedusers));

                return 0;
            } else {
                return 1;
            }
        }
        return 2;
    }

    /**
     * 插入关注用户流水
     *
     * @param userid          用户
     * @param interestedusers 被关注的用户
     */
    private void insertUserFollowRecord(int userid, int interestedusers) {
        FollowUser followUser = new FollowUser();
        followUser.setIntime(new Date());
        followUser.setInterestedusers(interestedusers);
        followUser.setUserid(userid);
        followUserService.insertSelective(followUser);
    }


    /**
     * 取消关注用户
     *
     * @param userid
     * @param interestedusers
     * @return
     */
    public int cancelFollowUser(int userid, int interestedusers) {
        //定义一个返回标志位
        int mark = 0;
        Map map = new HashMap();
        map.put("userid", userid);
        map.put("interestedusers", interestedusers);
        int result = followUserService.cancleFollowUser(map);
        if (result == 1) {
            //自己关注的用户-1
            followUserService.updateUserAttention(userid);
            //被关注人的粉丝-1
            followUserService.insertUserFansLess(interestedusers);
            //减少用户热度
            facadeHeatValue.lessUserHeatValue(1, interestedusers);
            mark = 1;
        } else {
            mark = -1;
        }
        return mark;
    }


    /**
     * 告知类活动的报名
     *
     * @param userid
     * @param
     * @param
     * @param
     * @return
     */
    public int takePartInPost(String userid, String name, String postid, String phone) {
        ActivePart activePart = new ActivePart();
        activePart.setUserid(Integer.parseInt(userid));
        activePart.setPostid(Integer.parseInt(postid));
        activePart.setName(name);
        activePart.setPhone(phone);
        activePart.setIntime(new Date());
        int result = activePartService.insertSelective(activePart);
        return result;
    }

    public List<PostLabel> queryCityListByCityname(String name) {
        return postLabelService.queryCityListByCityname(name);
    }

    public List<PostLabel> queryGeogLabelByName(String name) {
        return postLabelService.queryGeogLabelByName(name);
    }


    public int insert(String type) {
        TestIntime testIntime = new TestIntime();
        testIntime.setIntime(new Date());
        testIntime.setType(type);
        return testIntimeService.insert(testIntime);
    }


    /**
     * 更多活动
     *
     * @return
     */
    public List ActivePost(String id, Paging<PostVo> paging) {
        List<PostVo> postVos = postService.findAllActivePostD(Integer.parseInt(id), paging);
        for (int i = 0; i < postVos.size(); i++) {
            int postid = postVos.get(i).getId();
            int partsum = postService.queryActivePartSum(postid);
            postVos.get(i).setPartsum(partsum);
        }
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(new BeanComparator("partsum"), true);//true,fase正序反序
        Collections.sort(postVos, chain);
        return postVos;
    }

    /**
     * 更新线上所有帖子的热度值
     * (后续会用到，不能删除)
     */
    /*public void updateOnlinePostHeatvalue() {
        List<PostVo> list = postService.queryPostListByHeatValue();

        for (PostVo postVo : list) {
            int count = 0;
            int originHeatValue = postVo.getHeatvalue();
            int postid = postVo.getId();
            //判断是否是首页精选 isessence
            int isessence = postVo.getIsessence();
            if (isessence == 1) {
                count += HeatValueConstant.POINT.home_page_selection.getCode();
            }

            //判断是否是帖子精选 ishot
            int ishot = postVo.getIshot();
            if (ishot == 1) {
                count += HeatValueConstant.POINT.circle_selection.getCode();
            }

            //查出该帖子的评论数量，进行热度操作
            int commentsum = postVo.getCommentsum();
            int comPoint = commentsum * HeatValueConstant.POINT.comments_number.getCode();

            //查出该帖子的点赞数量，进行热度操作
            int zansum = postVo.getZansum();
            int zanPoint = zansum * HeatValueConstant.POINT.zan_number.getCode();

            //查看转发数
            int forwardsum = postVo.getForwardsum();
            int forwardPoint = forwardsum * HeatValueConstant.POINT.forwarding_number.getCode();

            //查出该帖子的收藏数量，进行热度操作
            int collectsum = postVo.getCollectsum();
            int collectPoint = collectsum * HeatValueConstant.POINT.collection_number.getCode();

            //查出该帖子的浏览数量，进行热度操作
            int countView = userRefreshRecordService.postcount(postid);
            int viewPoint = countView * HeatValueConstant.POINT.view_post.getCode();

            count = count + comPoint + zanPoint + forwardPoint + collectPoint + viewPoint;

            PostTo postTo = new PostTo();
            postTo.setId(postid);
            postTo.setHeatvalue(originHeatValue + count);

            postService.updatePostById(postTo);
        }
    }*/

    /**
     * 商城首页——性价比推荐的两篇商品推荐贴
     *
     * @return
     */
    public List<Post> getCostRecommendPost() {
        List<Post> costRecommendPostList = postService.getCostRecommendPost();

        return costRecommendPostList;
    }

    /**
     * 点击商城性价比推荐进入性价比推荐帖子列表接口
     *
     * @return
     */
    public List<Post> findAllCostRecommendPostList(Paging<Post> pager) {
        List<Post> allCostRecommendPostList = postService.findAllCostRecommendPostList(pager);

        return allCostRecommendPostList;
    }

    /**
     * 按照传入的帖子顺序查询出指定帖子
     *
     * @param ids
     * @return
     */
    public List<PostVo> querySelectedSortedPosts(String ids) {
        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "传入的帖子id字符串不能为空");
        }
        String[] strArr = ids.split(",");
        Integer[] intArr = new Integer[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = Integer.valueOf(strArr[i]);
        }
        return postService.querySelectedSortedPosts(intArr);
    }


    /**
     * 跨年活动投票
     * @param
     * @return
     */
    public Map takeActive(String device ,String postid){
        //根据帖子获取活动id
        int activeid= activeTakeService.activeid(Integer.parseInt(postid));
        //查询这个设备号有没有投过10次
        Map map = new HashMap();
        map.put("device",device);
        map.put("activeid",activeid);
        int count=activeTakeService.deviceCount(map);
        //查看这个设备号对这个帖子有没有投过票
        Map map1 = new HashMap();
        map1.put("device",device);
        map1.put("activeid",activeid);
        map1.put("postid",Integer.parseInt(postid));
        int postidCoun=activeTakeService.postidCount(map1);
        Map map2 = new HashMap();
        int takecount=0;
        int ok=0;
        Period period=activeTakeService.queryActiveTime(activeid);
        Date beg=period.getBegintime();
        Date end=period.getEndtime();
        Date date = new Date();
        long beding=beg.getTime();
        long endtime=end.getTime();
        long str= date.getTime();
        if(str > beding && str < endtime) {
            if (count < 3) {
                if (postidCoun == 0) {
                    ActiveTake activeTake = new ActiveTake();
                    activeTake.setDevice(device);
                    activeTake.setPostid(Integer.parseInt(postid));
                    activeTake.setIntime(new Date());
                    activeTake.setActiveid(activeid);
                    ok = activeTakeService.takeActive(activeTake);
                    Map map3 = new HashMap();
                    map3.put("postid", postid);
                    map3.put("activeid", activeid);
                    takecount = activeTakeService.takeCount(map3);
                    map2.put("code", 200);
                    map2.put("takecount", takecount);
                } else {
                    map2.put("code", 300);
                    map2.put("takecount", takecount);
                }
            } else {
                map2.put("code", 400);
                map2.put("takecount", takecount);
            }
        }else {
            map2.put("code", 600);
            map2.put("takecount", takecount);
        }
        return map2;
    }

    /**
     * 机器人刷票
     * @param postid
     * @param num
     * @return
     */
    public Integer Brushvotes(String postid,Integer num){
        //根据帖子id查活动
        int ok=0;
       int activeid= activeTakeService.activeid(Integer.parseInt(postid));
        if(num<1){
            ok=-1;
        }else {
            for (int i = 0; i < num; i++) {
                ActiveTake activeTake = new ActiveTake();
                activeTake.setDevice(UUID.randomUUID().toString().replaceAll("\\-", ""));
                activeTake.setPostid(Integer.parseInt(postid));
                activeTake.setIntime(new Date());
                activeTake.setActiveid(activeid);
                ok = activeTakeService.takeActive(activeTake);
            }
        }
        return ok;
    }


    /**
     * 获取首页活动图片
     */
    public List<HomepageManage> queryIndexPic(){
        List<HomepageManage> url =  homepageManageService.queryIndexPic();
        return url;
    }


    /**
     * 首页头部列表_2018年1月2号改
     * @param userid
     * @return
     */
    public List indexHomeList_20180102(String userid){
        //查询用户关注的圈子
        List<Circle> list=null;
        if(userid!=null){
            list=followCircleService.queryUserFollowCircle(Integer.parseInt(userid));
        }else {
            list=null;
        }
        return list;

    }


    /**
     * 新版发帖--长图贴
     * @param request
     * @param userid
     * @param circleid
     * @param activeid
     * @param title
     * @param subtitle
     * @param postcontent
     * @param isactive
     * @param labellist
     * @param proids
     * @return
     */
    public Map releasePicturePost(HttpServletRequest request, String userid, String circleid, String activeid, String title,
                                String subtitle, String postcontent, String isactive, String labellist, String proids) {
        DistributedLock lock = null;
        System.out.println("测试标签json》》》》》》》》》》》》》》" + labellist);
        try {
            lock = new DistributedLock(LOCK_NAME);
            //加锁
            lock.lock();
            //发帖操作
            return releaseModularPicturePost(request, userid, circleid, activeid, title, subtitle, postcontent, isactive, labellist, proids);

        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "zk控制下的发视频帖异常");
        } finally {
            if (lock != null) {
                //释放锁
                lock.unlock();
            }
        }
    }

    /**
     * 模块化发帖（新版发帖--长图贴）
     * @param request
     * @param userid
     * @param circleid
     * @param title
     * @param subtitle
     * @param postcontent
     * @param isactive
     * @param proids
     * @param labellist
     * @param activeid
     * @return
     */
    @Transactional
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map releaseModularPicturePost(HttpServletRequest request, String userid, String circleid, String title,
                                       String subtitle, String postcontent, String isactive, String proids,
                                       String labellist, String activeid) {
        Map map = new HashMap();
        validateNotNullUseridAndCircleid(userid, circleid);

        int cid = Integer.parseInt(circleid);
        int uid = Integer.parseInt(userid);

        /**
         *  这里需要根据userid判断当前登录的用户是否有发帖权限
         */
        //查询当前圈子的开放范围
        int scope = circleService.queryCircleScope(cid);
        //查询当前圈子的所有者
        User owner = circleService.queryCircleOwner(cid);
        //查询当前圈子的所有管理员列表
        List<User> manageList = circleService.queryCircleManage(cid);
        //判断该用户是否是圈子管理员
        int mark = getMarkIsCircleAdmin(userid, manageList);
        int lev = ShiroUtil.getUserLevel();     //用户等级

        log.debug("发帖用户的id是：" + uid);
        log.debug("发帖的圈子的所有者的id是：" + owner.getId());
        log.debug("发帖的圈子的scope是：" + scope);
        log.debug("发帖用户的mark是：" + mark);
        log.debug("发帖用户的等级level是：" + lev);

        //拥有权限的：1.该圈所有人均可发帖 2.该用户是该圈所有者 3.该用户是圈子管理员  4.所有者和大V可发时，发帖用户即为大V
        if (scope == 2
                || uid == owner.getId()
                || mark == 1
                || (scope == 1 && lev >= 1)) {

            try {
                log.info("APP前端用户开始请求发长图帖");
                Map contentMap = null;
                //封装帖子实体
                Post post = preparePostJavaBeanNew(request, uid, cid, title, subtitle, postcontent, isactive, contentMap, activeid, 1);//最后一个入参category为1：长图贴
                //1 插入帖子
                postService.insertSelective(post);
                //返回的主键--帖子id
                int flag = post.getId();
                //2 再保存帖子中分享的商品列表(如果商品id字段不为空)
                insertPostShareGoods(proids, flag);
                //3 标签业务逻辑处理
                if (StringUtils.isNotBlank(labellist)) {
                    addLabelProcess(labellist, flag);
                }
                //4 积分处理
                pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.post.getCode(), uid);//完成积分任务根据不同积分类型赠送积分的公共方法（包括总分和流水）
                //5 增加用户热度
                facadeHeatValue.addUserHeatValue(2, uid);
                //6 如果是参与活动发帖，则需要记录流水
                activePartService.addRecord(activeid, uid);

                map.put("flag", flag);
                return map;

            } catch (Exception e) {
                log.error("系统异常，APP发帖失败");
                map.put("flag", -2);
                e.printStackTrace();
                return map;
            }
        } else {
            log.info("该用户不具备发帖权限");
            map.put("flag", -1);
            return map;
        }
    }



}





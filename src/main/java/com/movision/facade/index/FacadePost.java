package com.movision.facade.index;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.*;
import com.movision.common.constant.PointConstant;
import com.movision.common.constant.PostLabelConstants;
import com.movision.common.util.ShiroUtil;
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
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.circleCategory.service.CircleCategoryService;
import com.movision.mybatis.comment.entity.CommentCount;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.compressImg.entity.CompressImg;
import com.movision.mybatis.compressImg.service.CompressImgService;
import com.movision.mybatis.followUser.entity.FollowUser;
import com.movision.mybatis.followUser.service.FollowUserService;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.labelSearchTerms.entity.LabelSearchTerms;
import com.movision.mybatis.labelSearchTerms.service.LabelSearchTermsService;
import com.movision.mybatis.newInformation.entity.NewInformation;
import com.movision.mybatis.newInformation.service.NewInformationService;
import com.movision.mybatis.opularSearchTerms.service.OpularSearchTermsService;
import com.movision.mybatis.post.entity.ActiveVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postAndUserRecord.entity.PostAndUserRecord;
import com.movision.mybatis.postAndUserRecord.service.PostAndUserRecordService;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabel.entity.PostLabelVo;
import com.movision.mybatis.postLabel.service.PostLabelService;
import com.movision.mybatis.postLabelRelation.service.PostLabelRelationService;
import com.movision.mybatis.postShareGoods.entity.PostShareGoods;
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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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
    private NewInformationService newInformationService;

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
    private VideoCoverURL videoCoverURL;
    @Autowired
    private  OpularSearchTermsService opularSearchTermsService;
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

    public PostVo queryPostDetail(String postid, String userid) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        CommentCount commentCounts = null;
        //通过userid、postid查询该用户有没有关注该圈子的权限
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(postid));
        if (!StringUtils.isEmpty(userid)) {
            parammap.put("userid", Integer.parseInt(userid));
        }
        PostVo vo = postService.queryPostDetail(parammap);

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
        List<CommentVo> co = facadeComments.queryPostNewComment(Integer.parseInt(postid));
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
                    vo.setNickname((String) desensitizationUtil.desensitization(vo.getNickname()).get("str"));//昵称脱敏
                }
            } else {
                User user = userService.queryUserB(vo.getUserid());
                if (user != null) {
                    vo.setUserid(user.getId());
                    vo.setNickname(user.getNickname());
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
            }
        }
        return vo;
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
        List<PostVo> ps = postService.findAllPostCrile(circleid);//圈子中的帖子
        //这个用户发的帖子
        List<PostVo> userPost = postService.findUserPost(postuserid);
        //这个帖子的标签id
        List<Integer> labids = postService.findPostByLabelId(Integer.parseInt(postid));
        //根据id查帖子
        List<PostVo> labpost = postService.findUserByLabelPost(labids);
        ps.addAll(userPost);
        ps.addAll(labpost);
        Set<PostVo> linkedHashSet = new LinkedHashSet<PostVo>(ps);
        ps = new ArrayList<PostVo>(linkedHashSet);
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(new BeanComparator("heatvalue"), true);//true,fase正序反序
        Collections.sort(ps, chain);
        List<PostVo> finpost = NotLoginretuenList(ps, paging);
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
        //int postpartsum = postService.activeSum(Integer.parseInt(postid));//投稿类参与人数
        active.setPartsum(partsum);
        //active.setPostpartsum(postpartsum);
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
        int wt = 750;//图片压缩后的宽度
        int ht = 440;//图片压缩后的高度440
        String compressUrl = coverImgCompressUtil.ImgCompress(url, wt, ht);
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
        //----(1)
        File fdel2 = new File(compressUrl);
        fdel2.delete();//删除压缩图
        //----(2)
        File fdel = new File(String.valueOf(url));
        long l = fdel.length();
        float size = (float) l / 1024 / 1024;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        String filesize = df.format(size);//返回的是String类型的
        fdel.delete();//删除上传到本地的原图片文件
        //----(3)
        File fdel3 = new File(compressUrl);
        fdel3.delete();//删除压缩完成的图片
        //把切割好的原图和压缩图分别存放数据库中
        CompressImg compressImg = new CompressImg();
        compressImg.setCompressimgurl(newimgurl);
        compressImg.setProtoimgsize(filesize);
        compressImg.setProtoimgurl(newalurl);
        compressImgService.insert(compressImg);
        map.put("compressmap", newimgurl);
        return map;
    }


    @Transactional
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map releasePostByPCTest(HttpServletRequest request, String userid, String circleid, String title,
                                   String postcontent, String coverimg, String labelid, String proids) {
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

                Post post = preparePost4PC(request, userid, circleid, title, postcontent, coverimg);
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
    private Post preparePost4PC(HttpServletRequest request, String userid, String circleid, String title, String postcontent, String coverimg) {
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

    public int updatePostByZanSum(String id, String userid) {
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(id));
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("intime", new Date());
        //查询当前用户是否已点赞该帖
        int count = postService.queryIsZanPost(parammap);
        if (count == 0) {
            //增加热度
            facadeHeatValue.addHeatValue(Integer.parseInt(id), 3, userid);
            //-------------------“我的”模块个人积分任务 增加积分的公共代码----------------------start
            //判断该用户有没有首次关注过圈子或有没有点赞过帖子评论等或有没有收藏过商品帖子活动
            UserOperationRecord entiy = userOperationRecordService.queryUserOperationRecordByUser(Integer.parseInt(userid));
            if (null == entiy || entiy.getIszan() == 0) {
                //如果未收藏过帖子或商品的话,首次收藏赠送积分
                pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.first_support.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                UserOperationRecord userOperationRecord = new UserOperationRecord();
                userOperationRecord.setUserid(Integer.parseInt(userid));
                userOperationRecord.setIszan(1);
                if (null == entiy) {
                    //不存在新增
                    userOperationRecordService.insertUserOperationRecord(userOperationRecord);
                } else if (entiy.getIszan() == 0) {
                    //存在更新
                    userOperationRecordService.updateUserOperationRecord(userOperationRecord);
                }
            }
            //****************************************
            //查询被点赞人的帖子是否被设为最新消息通知用户
            Integer isread = newInformationService.queryUserByNewInformation(Integer.parseInt(id));
            NewInformation news = new NewInformation();

            //更新被点赞人的帖子最新消息
            if (isread != null) {
                news.setIsread(0);
                news.setIntime(new Date());
                news.setUserid(isread);
                newInformationService.updateUserByNewInformation(news);
            } else {
                Integer uid = postService.queryPosterActivity(Integer.parseInt(id));  //查询被点赞的帖子发帖人
                //新增被点在人的帖子最新消息
                news.setIsread(0);
                news.setIntime(new Date());
                news.setUserid(uid);
                newInformationService.insertUserByNewInformation(news);
            }
            //*****************************************


            //-------------------“我的”模块个人积分任务 增加积分的公共代码----------------------end

            postService.insertZanRecord(parammap);
            int type = postService.updatePostByZanSum(Integer.parseInt(id));
            if (type == 1) {
                postService.queryPostByZanSum(Integer.parseInt(id));
                try {
                    String fromaccid = userOperationRecordService.selectAccid(userid);
                    String to = postService.selectToAccid(Integer.parseInt(id));
                    String nickname = userOperationRecordService.selectNickname(userid);
                    String pinnickname = nickname + "赞了你";
                    Map map = new HashMap();
                    map.put("body", pinnickname);
                    Gson gson = new Gson();
                    String json = gson.toJson(map);
                    String pushcontent = nickname + "赞了你";
                    imFacade.sendMsgInform(json, fromaccid, to, pushcontent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 1;
            }
        }


        return -1;
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
                                  String postcontent, String isactive, String coverimg, String proids, String labellist) {
        Map map = new HashMap();
        /**
         *  这里需要根据userid判断当前登录的用户是否有发帖权限
         */
        //查询当前圈子的开放范围
        int scope = circleService.queryCircleScope(Integer.parseInt(circleid));
        //查询当前圈子的所有者
        User owner = circleService.queryCircleOwner(Integer.parseInt(circleid));
        //查询当前圈子的所有管理员列表
        List<User> manageList = circleService.queryCircleManage(Integer.parseInt(circleid));
        //判断该用户是否是圈子管理员
        int mark = getMarkIsCircleAdmin(userid, manageList);
        int lev = ShiroUtil.getUserLevel();     //用户等级

        //拥有权限的：1.该圈所有人均可发帖 2.该用户是该圈所有者 3.该用户是圈子管理员  4.所有者和大V可发时，发帖用户即为大V
        if (scope == 2
                || Integer.parseInt(userid) == owner.getId()
                || mark == 1
                || (scope == 1 && lev >= 1)) {

            try {
                log.info("APP前端用户开始请求发帖");
                Map contentMap = null;
                //封装帖子实体
                Post post = preparePostJavaBean(request, userid, circleid, title, postcontent, isactive, coverimg, contentMap);
                /*Post post = new Post();
                post.setTitle("测试标签发帖" + DateUtils.getCurrentDate());*/
                //插入帖子
                postService.releaseModularPost(post);
                //返回的主键--帖子id
                int flag = post.getId();
                //再保存帖子中分享的商品列表(如果商品id字段不为空)
                insertPostShareGoods(proids, flag);
                //标签业务逻辑处理
                addLabelProcess(labellist, flag);
                //积分处理
                pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.post.getCode(), Integer.parseInt(userid));//完成积分任务根据不同积分类型赠送积分的公共方法（包括总分和流水）
                //增加用户热度
                facadeHeatValue.addUserHeatValue(2, Integer.parseInt(userid));
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
            if (null == p.getId()) {
                newLabels.add(p);
            } else {
                existLabels.add(p);
            }
        }

        log.debug("新建的标签集合：" + newLabels.toString());
        log.debug("非新建的标签集合：" + existLabels.toString());

        //3 下面是对新建的标签集合操作
        if (CollectionUtil.isNotEmpty(newLabels)) {
            processForNewLabel(flag, newLabels);
        }

        if (CollectionUtil.isNotEmpty(existLabels)) {
            //4 下面是对非新建的标签集合操作
            processForExistLabel(flag, existLabels);
            //5 记录此次发帖标签使用的历史
            existLabels.addAll(newLabels);
            for (PostLabel label : existLabels) {
                saveKeywordsInMongoDB(label);
                //增加标签热度
                facadeHeatValue.addLabelHeatValue(2, label.getId(), null);
            }
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

    private void processForNewLabel(int flag, List<PostLabel> newLabels) {
        //3.1 从缓存中获取userid，citycode, 并且根据标签类型，获取标签的头像
        for (PostLabel p : newLabels) {
            p.setUserid(ShiroUtil.getAppUserID());
            p.setCitycode(ShiroUtil.getIpCity());
            //根据标签类型，获取标签的头像
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
     * 根据请求中的参数准备Post实体
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
    private Post preparePostJavaBean(HttpServletRequest request, String userid, String circleid, String title,
                                     String postcontent, String isactive, String coverimg, Map contentMap) {
        Post post = new Post();
        post.setCircleid(Integer.parseInt(circleid));
        post.setTitle(title);
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
        if ((int) contentMap.get("flag") == 0) {
            post.setIsdel(0);//上架
        } else if ((int) contentMap.get("flag") > 0) {
            post.setIsdel(2);
        }
        post.setCoverimg(coverimg);//帖子封面
        post.setUserid(Integer.parseInt(userid));
        post.setCity(ShiroUtil.getIpCity());

        return post;
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
        Map map = movisionOssClient.uploadImgerAndIncision(url, x, y, w, h);

        //3获取本地服务器中切割完成后的图片
        String tmpurl = String.valueOf(map.get("file"));
        System.out.println("切割完成后的本地图片绝对路径===" + tmpurl);
        String rawimg = String.valueOf(map.get("incise"));

        //4对本地服务器中切割好的图片进行压缩处理
        int wt = 0;//图片压缩后的宽度
        int ht = 0;//图片压缩后的高度440
        if (type.equals(1) || type.equals("1")) {//用于区分上传帖子封面还是活动方形图
            wt = 750;
            ht = 440;
        } else {
            wt = 440;
            ht = 440;
        }
        //新增压缩部分
        File fs = new File(tmpurl);
        Long fsize = fs.length();//获取文件大小
        String compressUrl = null;
        if (fsize > 200 * 1024) {
            compressUrl = coverImgCompressUtil.ImgCompress(tmpurl, wt, ht);
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
                compressUrl = coverImgCompressUtil.ImgCompress(tmpurl, Integer.parseInt(ww), Integer.parseInt(hh));
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
        compressImgService.insert(compressImg);
        log.info("帖子上传封面本地原图=========", url);
        log.info("帖子上传封面本地切割图=========", tmpurl);
        log.info("帖子上传封面本地压缩图=========", compressUrl);

        //6删除本地原图，切割图，压缩图
        File f1 = new File(url);
        f1.delete();
        File f2 = new File(tmpurl);
        f2.delete();
        File f3 = new File(compressUrl);
        f3.delete();
        return compressmap;
    }

    /**
     * 推荐
     *
     * @param userid
     * @param paging
     * @return
     */
    public List recommendPost(String userid, ServicePaging<PostVo> paging) {
        long count = mongodbCount();
        List<PostVo> list = null;
        List<PostVo> alllist = postService.findAllPostListHeat();//查询所有帖子
        List<PostVo> posts = new ArrayList<>();

        if (userid == null) {
            //未登录
            list = postService.findAllPostHeatValue();//根据热度值排序查询帖子
            list = NotLoginretuenList(list, paging);
            return list;
        } else {
            //已登录
            if (alllist != null) {
                if (count < 1000) {
                    //mongodb的里面的刷新记录小于1000条记录的时候进行用户分析
                    list = userAnalysisSmall(userid, paging, alllist, posts);
                    if (list != null) return list;
                } else if (count >= 1000) {
                    //mongodb的里面的刷新记录大于等于1000条记录的时候进行用户分析
                    list = userAnalysisBig(userid, posts, paging);
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
    private List userAnalysisBig(String userid, List<PostVo> posts, ServicePaging<PostVo> paging) {
        List<PostVo> list = null;
        List<UserRefreshRecordVo> result;//查询用户最喜欢的圈子
        List<DBObject> listmongodb;
        listmongodb = userRefulshListMongodb(Integer.parseInt(userid));//查询用户刷新列表
        if (listmongodb.size() != 0) {
            result = opularSearchTermsService.userFlush(Integer.parseInt(userid));
            int crileid = 0;
            for (int i = 0; i < result.size(); i++) {
                crileid = result.get(i).getCrileid();
            }
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
            list = retuenList(criclelist, userid, paging);
            return list;
        } else {
            list = postService.findAllPostHeatValue();
            list = retuenList(list, userid, paging);
            return list;
        }
    }

    /**
     * mongodb的里面的刷新记录  小于1000  条记录的时候进行用户分析
     *
     * @param userid
     * @param paging
     * @param alllist
     * @param posts
     * @return
     */
    private List userAnalysisSmall(String userid, ServicePaging<PostVo> paging, List<PostVo> alllist, List<PostVo> posts) {
        List<DBObject> listmongodba;
        List<PostVo> list = null;
        listmongodba = userRefulshListMongodb(Integer.parseInt(userid));//查询用户刷新列表
        if (listmongodba.size() != 0) {
            for (int j = 0; j < listmongodba.size(); j++) {
                PostVo post = new PostVo();
                post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                posts.add(post);//把mongodb转为post实体
            }
            //把看过的帖子过滤掉
            alllist.removeAll(posts);//alllist是剩余的帖子
            list = retuenList(alllist, userid, paging);
        } else {
            //登录情况下但是mongodb里面没有刷新记录
            list = postService.findAllPostHeatValue();
            list = retuenList(list, userid, paging);
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
    public List localhostPost(String userid, ServicePaging<PostVo> paging, String area) {
        List<PostVo> list = null;
        List<DBObject> listmongodba = null;
        List<PostVo> posts = new ArrayList<>();
        String citycode = "";
        //根据传过来的地区去yw_city查代码
        if (area != null) {
            citycode = postService.queryCityCode(area);
        } /**else {
            citycode = postService.queryCityUserCode(Integer.parseInt(userid));
         }*/
        if (userid == null) {//未登录
            list = postService.findAllCityPost(citycode);//根据热度值排序查询帖子
            list = NotLoginretuenList(list, paging);
            return list;
        } else {//已登录
            //根据地区查询帖子
            listmongodba = userRefulshListMongodb(Integer.parseInt(userid));//用户有没有看过
            if (listmongodba.size() != 0) {
                for (int j = 0; j < listmongodba.size(); j++) {
                    PostVo post = new PostVo();
                    post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                    posts.add(post);//把mongodb转为post实体
                }
                //根据city查询帖子
                List<PostVo> postVos = postService.findAllCityPost(citycode);
                postVos.removeAll(posts);
                list = retuenList(postVos, userid, paging);
            } else {//登录但是刷新列表中没有帖子
                list = postService.findAllCityPost(citycode);//根据热度值排序查询帖子
                list = retuenList(list, userid, paging);
                return list;
            }
        }
        return list;
    }

    /**
     * 圈子
     *
     * @param userid
     * @param paging
     * @return
     */
    public List circleRefulsh(String userid, ServicePaging<PostVo> paging, int circleid) {
        List<DBObject> listmongodba = null;
        List<PostVo> posts = new ArrayList<>();
        List<PostVo> list = null;
        if (userid == null) {
            //这个圈子的帖子
            list = postService.findAllPostCrile(circleid);//根据热度值排序查询帖子
            list = NotLoginretuenList(list, paging);
            return list;
        } else {
            listmongodba = userRefulshListMongodb(Integer.parseInt(userid));//用户有没有看过
            if (listmongodba.size() != 0) {
                for (int j = 0; j < listmongodba.size(); j++) {
                    PostVo post = new PostVo();
                    post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                    posts.add(post);//把mongodb转为post实体
                }
                //根据圈子id查询帖子
                List<PostVo> postVos = postService.findAllPostCrile(circleid);
                postVos.removeAll(posts);
                list = retuenList(postVos, userid, paging);
            } else {
                //登录但是刷新列表中没有帖子
                list = postService.findAllPostCrile(circleid);//根据热度值排序查询帖子
                list = retuenList(list, userid, paging);
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
    public List followPost(String userid, ServicePaging<PostVo> paging) {
        List<PostVo> list = null;
        List<DBObject> listmongodba = null;
        List<PostVo> posts = new ArrayList<>();
        List<PostVo> crileidPost = null;
        List<PostVo> userPost = null;
        List<PostVo> labelPost = null;
        listmongodba = userRefulshListMongodb(Integer.parseInt(userid));//用户有没有看过
        List<Integer> followCricle = postService.queryFollowCricle(Integer.parseInt(userid));//查询用户关注的圈子
        List<Integer> followUsers = postService.queryFollowUser(Integer.parseInt(userid));//用户关注的作者
        List<Integer> followLabel = postLabelService.labelId(Integer.parseInt(userid));//用户关注标签
        if (followCricle.size() == 0 && followUsers.size() == 0 && followLabel.size() == 0) {
            list = postService.findAllPostHeatValue();//根据热度值排序查询帖子
            list = NotLoginretuenList(list, paging);
            return list;
        } else {
            //根据圈子查询所有帖子
            if (followCricle.size() != 0) {
                crileidPost = postService.queryPostListByIds(followCricle);
            }
            //根据作者查询所有帖子
            if (followUsers.size() != 0) {
                userPost = postService.queryUserListByIds(followUsers);
            }
            if (followLabel.size() != 0) {
                labelPost = postService.queryLabelListByIds(followLabel);
            }
            crileidPost.addAll(userPost);
            crileidPost.addAll(labelPost);
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
                list = retuenList(crileidPost, userid, paging);
            } else {
                //list = postService.findAllPostHeatValue();//根据热度值排序查询帖子
                list = retuenList(crileidPost, userid, paging);
                return list;
            }
        }
        return list;
    }

    /**
     * 标签帖子
     *
     * @return
     */
    public List labelPost(String userid, int labelid, ServicePaging<PostVo> paging) {
        List<PostVo> list = null;
        List<DBObject> listmongodba = null;
        List<PostVo> posts = new ArrayList<>();
        if (userid == null) {
            list = postService.findAllLabelAllPost(labelid);//根据热度值排序查询帖子
            list = NotLoginretuenList(list, paging);
            return list;
        } else {
            listmongodba = userRefulshListMongodb(Integer.parseInt(userid));//用户有没有看过
            if (listmongodba.size() != 0) {
                for (int j = 0; j < listmongodba.size(); j++) {
                    PostVo post = new PostVo();
                    post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                    posts.add(post);//把mongodb转为post实体
                }
                //根据标签查询帖子
                List<PostVo> postVos = postService.findAllLabelAllPost(labelid);
                postVos.removeAll(posts);
                list = retuenList(postVos, userid, paging);
            } else {
                //登录但是刷新列表中没有帖子
                list = postService.findAllLabelAllPost(labelid);//根据热度值排序查询帖子
                list = retuenList(list, userid, paging);
                return list;
            }
            return list;
        }
    }


    /**
     * 返回数据
     *
     * @param lists
     * @param userid
     * @return
     */
    public List retuenList(List<PostVo> lists, String userid, ServicePaging<PostVo> paging) {
        List<PostVo> list = null;
        if (lists != null) {
            paging.setTotal(lists.size());
            list = pageFacade.getPageList(lists, paging.getCurPage(), paging.getPageSize());
            findUser(list);
            findPostLabel(list);
            findHotComment(list);
            countView(list);
            findAllCircleName(list);
            insertmongo(list, userid);
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
    public List NotLoginretuenList(List<PostVo> lists, ServicePaging<PostVo> paging) {
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
     * 帖子浏览量 (公共方法)
     *
     * @param list
     * @return
     */
    public List countView(List<PostVo> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                int postid = list.get(i).getId();
                int uesrreflushCounts = userRefreshRecordService.postcount(postid);
                list.get(i).setCountview(uesrreflushCounts);
            }
        }
        return list;
    }

    /**
     * 查询帖子标签
     *
     * @param list
     * @return
     */
    public List findPostLabel(List<PostVo> list) {

        if (list != null) {

            for (int i = 0; i < list.size(); i++) {
                List<PostLabel> postLabels = new ArrayList<>();
                //帖子的id
                int postid = list.get(i).getId();
                //先获取帖子对应的圈子
                addCircleToLabellist(list, i, postLabels);
                //帖子对应的活动
                addActiveToLabellist(list, i, postLabels);
                //根据帖子id查询对应的标签
                List<PostLabel> labelInDB = postService.queryPostLabel(postid);

                postLabels.addAll(labelInDB);

                list.get(i).setPostLabels(postLabels);
            }
        }
        return list;
    }

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
     * 查询圈子名称
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
     * 精選評論
     *
     * @param list
     * @return
     */
    public List findHotComment(List<PostVo> list) {
        List<CommentVo> comments = null;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                int postid = list.get(i).getId();
                //根據帖子id去查所有評論
                comments = commentService.queryCommentByPost(postid);
                for (int j = 0; j < comments.size(); j++) {
                    int heatvalue = comments.get(j).getHeatvalue();
                    if (heatvalue >= 50) {
                        list.get(i).setComments(comments.get(j));
                    }
                }
            }
        }

        return list;
    }
    /***
     * 下拉刷新
     * @param userid
     * @param paging
     * @param type
     * @param area
     * @return
     */
    public List userRefreshListNew(String userid, ServicePaging<PostVo> paging, int type, String area, String circleid, String labelid) {
        List<PostVo> list = null;
        if (type == 1) {//推荐
            list = recommendPost(userid, paging);
        } else if (type == 2) {//关注
            list = followPost(userid, paging);
        } else if (type == 3) {//本地
            list = localhostPost(userid, paging, area);
        } else if (type == 4) {//圈子c
            list = circleRefulsh(userid, paging, Integer.parseInt(circleid));
        } else if (type == 5) {//标签
            list = labelPost(userid, Integer.parseInt(labelid), paging);
        }
        return list;
     }

    /**
     * 插入
     *
     * @param list
     * @param userid
     */
    public void insertmongo(List<PostVo> list, String userid) {
        int crileid = 0;//圈子id
        if (list != null && userid != null) {
            for (int i = 0; i < list.size(); i++) {
                int id = list.get(i).getId();
                //查询帖子是哪个圈子
                try {
                    crileid = postService.queryCrileid(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //刷新记录插入mongodb
                insertMongoDB(userid, id, crileid);
             }
        }
    }




    /**
     * 在mongodb中查询用户刷新浏览过的列表
     *
     * @param userid
     * @return
     */
    public List userRefulshListMongodb(int userid) {
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
            log.error("在mongodb中查询用户刷新浏览过的列表失败", e);
        } finally {
            if (null != db) {
                db.requestDone();
                db = null;
                dbCursor.close();
                mongoClient.close();
            }
        }
        return list;
    }

    /**
     * 查询用户刷新记录表的总记录数
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
                db = null;
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
    public void insertMongoDB(String userid, int postid, int crileid) {
        //把刷新记录插入mongodb
        if (StringUtil.isNotEmpty(userid)) {
            UserRefreshRecord userRefreshRecord = new UserRefreshRecord();
            userRefreshRecord.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
            userRefreshRecord.setUserid(Integer.parseInt(userid));
            userRefreshRecord.setPostid(postid);
            userRefreshRecord.setCrileid(String.valueOf(crileid));
            userRefreshRecord.setIntime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            userRefreshRecordService.insert(userRefreshRecord);
        }
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
     * 用户刷新的历史记录列表
     *
     * @param userid
     * @return
     */
    public List userReflushHishtoryRecord(String userid, Paging<PostVo> paging) {
        List<DBObject> list = userRefulshListMongodb(Integer.parseInt(userid));
        List<Integer> postVos = new ArrayList<>();
        List<PostVo> postVo = null;
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                int postid = Integer.parseInt(list.get(i).get("postid").toString());
                postVos.add(postid);
            }
            //根据postid查询帖子
            postVo = postService.findAllPostByid(postVos, paging);
            if (postVo != null) {
                findUser(postVo);
                findPostLabel(postVo);
                findHotComment(postVo);
                countView(postVo);
            }
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

    public List<Map> getCircleInCatagory() {
        // 所有当前用户有权限的圈子类型
        List<Map<String, Object>> list = circleService.selectCircleInCatagory(ShiroUtil.getAppUserID());
        // 所有的catagory
        List<CircleCategory> circleCategoryVoList = circleCategoryService.queryCircleCategoryList();

        List<Map> resultList = new ArrayList<>();
        //1 需要把圈子按照不同的category进行分组
        for (CircleCategory c : circleCategoryVoList) {

            List<Map> circlelist = new ArrayList<>();
            for (Map m : list) {
                //若是同一个category，则放到一个集合中
                if (c.getId() == (int) m.get("cid")) {

                    Map map = new HashedMap();
                    map.put("circle_id", m.get("circleid"));
                    map.put("circle_name", m.get("circlename"));
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

    public Boolean isExistSameNameLabel(String name) {
        int sameLabelCount = postLabelService.countSameNameLabel(name);
        if (sameLabelCount > 0) {
            log.debug("存在相同的标签名");
            return true;
        } else {
            log.debug("不存在相同的标签名");
            return false;
        }
    }


    public List<PostLabel> queryHotValueLabelList() {
        return postLabelService.queryHotValueLabelList(10);
    }


    /**
     * 关注作者
     *
     * @param userid
     * @param
     * @return
     */
    public int concernedAuthorUser(int userid, int interestedusers) {
        Map map = new HashMap();
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
            //该用户的粉丝数加1
            int fans = followUserService.insertUserFans(interestedusers);
            //被关注人增加热度
            facadeHeatValue.addUserHeatValue(1, interestedusers);
            return 0;
        } else {
            return 1;
        }
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
     * @param weixin
     * @param QQ
     * @param phone
     * @return
     */
    public int takePartInPost(String userid, String weixin, String QQ, String phone, String postid) {
        ActivePart activePart = new ActivePart();
        activePart.setUserid(Integer.parseInt(userid));
        activePart.setPostid(Integer.parseInt(postid));
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

}





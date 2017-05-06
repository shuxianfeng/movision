package com.movision.facade.index;

import com.movision.common.constant.PointConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.accusation.entity.Accusation;
import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.compressImg.entity.CompressImg;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.post.entity.ActiveVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postAndUserRecord.entity.PostAndUserRecord;
import com.movision.mybatis.postAndUserRecord.service.PostAndUserRecordService;
import com.movision.mybatis.postShareGoods.entity.PostShareGoods;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userOperationRecord.entity.UserOperationRecord;
import com.movision.mybatis.userOperationRecord.service.UserOperationRecordService;
import com.movision.mybatis.video.entity.Video;
import com.movision.mybatis.video.service.VideoService;
import com.movision.utils.DateUtils;
import com.movision.utils.DesensitizationUtil;
import com.movision.utils.JsoupCompressImg;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.pagination.model.Paging;
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
    private PostService postService;

    @Autowired
    private AccusationService accusationService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CircleService circleService;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

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

    public PostVo queryPostDetail(String postid, String userid, String type) {

        //通过userid、postid查询该用户有没有关注该圈子的权限
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(postid));
        if (!StringUtils.isEmpty(userid)) {
            parammap.put("userid", Integer.parseInt(userid));
        }
        PostVo vo = postService.queryPostDetail(parammap);
        if (type.equals("1") || type.equals("2")) {
            Video video = postService.queryVideoUrl(Integer.parseInt(postid));
            vo.setVideourl(video.getVideourl());
            vo.setVideocoverimgurl(video.getBannerimgurl());
        }
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
        Integer circleid=vo.getCircleid();
        //查询帖子详情最下方推荐的4个热门圈子
        List<Circle> hotcirclelist = circleService.queryHotCircle();
        vo.setHotcirclelist(hotcirclelist);
        //查询帖子中分享的商品
        List<GoodsVo> shareGoodsList = goodsService.queryShareGoodsList(Integer.parseInt(postid));
        vo.setShareGoodsList(shareGoodsList);

        //对帖子内容进行脱敏处理
        vo.setTitle((String) desensitizationUtil.desensitization(vo.getTitle()).get("str"));//帖子主标题脱敏
        vo.setSubtitle((String) desensitizationUtil.desensitization(vo.getSubtitle()).get("str"));//帖子副标题脱敏
        vo.setPostcontent((String) desensitizationUtil.desensitization(vo.getPostcontent()).get("str"));//帖子正文文字脱敏
        //数据插入mongodb
        if (StringUtil.isNotEmpty(userid)) {
            PostAndUserRecord postAndUserRecord = new PostAndUserRecord();
            postAndUserRecord.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
            postAndUserRecord.setCrileid(circleid);
            postAndUserRecord.setPostid(Integer.parseInt(postid));
            postAndUserRecord.setUserid(Integer.parseInt(userid));
            postAndUserRecord.setIntime(new Date().toLocaleString());
            postAndUserRecordService.insert(postAndUserRecord);
        }
        return vo;
    }

    public ActiveVo queryActiveDetail(String postid, String userid, String activetype) {

        //告知类活动
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(postid));
        if (!StringUtils.isEmpty(userid)) {
            parammap.put("userid", Integer.parseInt(userid));
        }
        ActiveVo active = postService.queryNoticeActive(parammap);

        //计算距离结束时间
        Date begin = active.getBegintime();
        Date end = active.getEndtime();
        Date now = new Date();
        int enddays = DateUtils.activeEndDays(now, begin, end);
        active.setEnddays(enddays);

        //查询活动参与总人数
        int partsum = postService.queryActivePartSum(Integer.parseInt(postid));
        active.setPartsum(partsum);

        //如果为商城促销类活动，需要在此基础上增加促销类商品列表
        if (activetype.equals("1")) {

            List<GoodsVo> goodsList = goodsService.queryActiveGoods(postid);
            active.setPromotionGoodsList(goodsList);

        }

        //增加活动详情最下方推荐的四个热门活动
        active.setHotActiveList(postService.queryFourHotActive());

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
            postAndUserRecord.setIntime(new Date().toLocaleString());
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

        map.put("currentList", currentList);
        map.put("dayagoList", dayagoList);
        map.put("twodayagoList", twodayagoList);
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
        int lev = owner.getLevel();//用户等级
        if (scope == 0 || Integer.parseInt(userid) == owner.getId() || (scope == 1 && lev >= 1)) {
            return 1;//有发帖权限
        } else {
            return -1;//无发帖权限
        }
    }

    @Transactional
    @CacheEvict(value = "indexData", key = "'index_data'")
    public int releasePost(HttpServletRequest request, String userid, String type, String circleid, String title, String postcontent, String isactive, MultipartFile coverimg,
                           MultipartFile videofile, String videourl, String proids) {

        String url = "";//定义原生视频地址
        if (videofile != null) {
            //首先调用庄总的视频上传接口
            Map m = movisionOssClient.uploadObject(videofile, "video", "post");
            url = String.valueOf(m.get("url"));//原生视频上传地址
        }

        //上传帖子封面图片
        Map m = movisionOssClient.uploadObject(coverimg, "img", "post");
        String coverurl = String.valueOf(m.get("url"));

        //这里需要根据userid判断当前登录的用户是否有发帖权限
        //查询当前圈子的开放范围
        int scope = circleService.queryCircleScope(Integer.parseInt(circleid));
        //查询当前圈子的所有者(返回所有者的用户id)
        User owner = circleService.queryCircleOwner(Integer.parseInt(circleid));
        int lev = owner.getLevel();//用户等级

        //拥有权限的：1.该圈所有人均可发帖 2.该用户是该圈所有者 3.所有者和大V可发时，发帖用户即为大V
        if (scope == 0 || Integer.parseInt(userid) == owner.getId() || (scope == 1 && lev >= 1)) {

            try {
                log.info("APP前端用户开始请求发帖");

                Post post = new Post();
                post.setCircleid(Integer.parseInt(circleid));
                post.setTitle(title);
                if (StringUtil.isNotEmpty(postcontent)) {
                    //内容转换
                    Map con = jsoupCompressImg.compressImg(request, postcontent);
                    System.out.println(con);
                    if ((int) con.get("code") == 200) {
                        String str = con.get("content").toString();
                        str = str.replace("\\", "");
                        post.setPostcontent(str);//帖子内容
                    } else {
                        log.error("APP端帖子图片内容转换异常");
                        post.setPostcontent(postcontent);
                    }
                }
                post.setZansum(0);//新发帖全部默认为0次
                post.setCommentsum(0);//被评论次数
                post.setForwardsum(0);//被转发次数
                post.setCollectsum(0);//被收藏次数
                post.setIsactive(Integer.parseInt(isactive));//是否为活动 0 帖子 1 活动
                post.setType(Integer.parseInt(type));//帖子类型 0 普通图文帖 1 原生视频帖 2 分享视频帖
                post.setIshot(0);//是否设为热门：默认0否
                post.setIsessence(0);//是否设为精选：默认0否
                post.setIsessencepool(0);//是否设为精选池中的帖子
                post.setIntime(new Date());//帖子发布时间
                post.setTotalpoint(0);//帖子综合评分
                post.setIsdel(0);//上架
                post.setCoverimg(coverurl);//帖子封面
                post.setUserid(Integer.parseInt(userid));
                //插入帖子
                postService.releasePost(post);

                int flag = post.getId();//返回的主键--帖子id

                if (!type.equals("0")) {
                    Video video = new Video();
                    video.setPostid(flag);
                    video.setIsrecommend(0);
                    video.setIsbanner(0);
                    video.setBannerimgurl(coverurl);//简化APP，直接取帖子封面图片为原生视频的封面(运营后台不变)
                    if (type.equals("1")) {
                        video.setVideourl(url);//原生视频上传链接
                    } else if (type.equals("2")) {
                        video.setVideourl(videourl);//分享视频链接
                    }
                    video.setIntime(new Date());
                    //向帖子视频表中插入一条视频记录
                    videoService.insertVideoById(video);
                }

                //再保存帖子中分享的商品列表(如果商品id字段不为空)
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

                pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.post.getCode(), Integer.parseInt(userid));//完成积分任务根据不同积分类型赠送积分的公共方法（包括总分和流水）

                return flag;

            } catch (Exception e) {
                log.error("系统异常，APP发帖失败");
                e.printStackTrace();
                return 0;
            }
        } else {
            log.info("该用户不具备发帖权限");
            return -1;
        }
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
            //-------------------“我的”模块个人积分任务 增加积分的公共代码----------------------end

            postService.insertZanRecord(parammap);
            int type = postService.updatePostByZanSum(Integer.parseInt(id));
            if (type == 1) {
                return postService.queryPostByZanSum(Integer.parseInt(id));
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
}

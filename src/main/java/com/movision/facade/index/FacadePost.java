package com.movision.facade.index;

import com.movision.mybatis.accusation.entity.Accusation;
import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.post.entity.ActiveVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postShareGoods.entity.PostShareGoods;
import com.movision.utils.DateUtils;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author shuxf
 * @Date 2017/1/19 15:43
 */
@Service
public class FacadePost {

    private static Logger log = LoggerFactory.getLogger(FacadePost.class);

    @Value("#{configProperties['img.domain']}")
    private String imgdomain;

    @Autowired
    private PostService postService;

    @Autowired
    private AccusationService accusationService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CircleService circleService;

    public PostVo queryPostDetail(String postid, String userid, String type) {

        //通过userid、postid查询该用户有没有关注该圈子的权限
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(postid));
        if (!StringUtils.isEmpty(userid)) {
            parammap.put("userid", Integer.parseInt(userid));
        }
        PostVo vo = postService.queryPostDetail(parammap);
        if (type.equals("1")) {
            String url = postService.queryVideoUrl(Integer.parseInt(postid));
            vo.setVideourl(url);
        }
        //查询帖子详情最下方推荐的4个热门圈子
        List<Circle> hotcirclelist = circleService.queryHotCircle();
        vo.setHotcirclelist(hotcirclelist);
        return vo;
    }

    public ActiveVo queryActiveDetail(String postid, String activetype) {

        //告知类活动
        ActiveVo active = postService.queryNoticeActive(postid);

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
        return active;
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

        return postService.queryPostList(pager, circleid);
    }


    public int releasePost(HttpServletRequest request, String userid, String level, String circleid, String title,
                           String postcontent, String isactive, MultipartFile file, String proids) {

        //这里需要根据userid判断当前登录的用户是否有发帖权限
        //查询当前圈子的开放范围
        int scope = circleService.queryCircleScope(Integer.parseInt(circleid));
        //查询当前圈子的所有者(返回所有者的用户id)
        int owner = circleService.queryCircleOwner(Integer.parseInt(circleid));
        int lev = Integer.parseInt(level);//用户等级

        //拥有权限的：1.该圈所有人均可发帖 2.该用户是该圈所有者 3.所有者和大V可发时，发帖用户即为大V
        if (scope == 0 || Integer.parseInt(userid) == owner || (scope == 1 && lev >= 1)) {

            try {
                log.info("APP前端用户开始请求发普通帖");

                Post post = new Post();
                post.setCircleid(Integer.parseInt(circleid));
                post.setTitle(title);
                post.setPostcontent(postcontent);
                post.setZansum(0);//新发帖全部默认为0次
                post.setCommentsum(0);//被评论次数
                post.setForwardsum(0);//被转发次数
                post.setCollectsum(0);//被收藏次数
                post.setIsactive(Integer.parseInt(isactive));//是否为活动 0 帖子 1 活动
                post.setType(0);//帖子类型 0 普通帖 1 原生优质帖
                post.setIshot(0);//是否设为热门：默认0否
                post.setIsessence(0);//是否设为精选：默认0否
                post.setIsessencepool(0);//是否设为精选池中的帖子
                post.setIntime(new Date());//帖子发布时间
                post.setTotalpoint(0);//帖子综合评分
                post.setIsdel(0);//上架

                //上传图片到本地服务器
                String savedFileName = "";
                if (!file.isEmpty()) {
                    String fileRealName = file.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedFileName = FileId.toString().replace("-", "").concat(fileSuffix);
//                    String savedDir = request.getSession().getServletContext().getRealPath("/images/post/coverimg");
                    String savedDir = request.getSession().getServletContext().getRealPath("");

                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision-1.0.0后缀movision-1.0.0去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);

                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/post/coverimg";

//                    File savedFile = new File(savedDir, savedFileName);
                    File savedFile = new File(combinpath, savedFileName);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        file.transferTo(savedFile);  //转存文件
                    }
                }

                //暂定为tomcat本地tomcat服务器上的路径 imgdomain==>"http://120.77.214.187:8100/images/post/coverimg/"
                String imgurl = imgdomain + savedFileName;

                post.setCoverimg(imgurl);

                postService.releasePost(post);

                int flag = post.getId();

                //再保存帖子中分享的商品列表(如何商品id字段不为空)
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

                return flag;

            } catch (Exception e) {
                log.error("系统异常，APP普通帖发布失败");
                e.printStackTrace();
                return 0;
            }
        } else {
            log.info("该用户不具备发帖权限");
            return -1;
        }
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

    public int updatePostByZanSum(String id, String userid) {
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(id));
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("intime", new Date());
        postService.insertZanRecord(parammap);
        int type = postService.updatePostByZanSum(Integer.parseInt(id));
        if (type == 1) {
            return postService.queryPostByZanSum(Integer.parseInt(id));
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

    public int partActive(String postid, String userid, String title, String email, String videourl, String introduction) {

        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", Integer.parseInt(postid));
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("title", title);
        parammap.put("email", email);
        parammap.put("videourl", videourl);
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

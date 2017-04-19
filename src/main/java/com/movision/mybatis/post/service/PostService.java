package com.movision.mybatis.post.service;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.compressImg.entity.CompressImg;
import com.movision.mybatis.compressImg.mapper.CompressImgMapper;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.period.entity.Period;
import com.movision.mybatis.post.entity.*;
import com.movision.mybatis.post.mapper.PostMapper;
import com.movision.mybatis.postShareGoods.entity.PostShareGoods;
import com.movision.mybatis.video.entity.Video;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/18 14:27
 */
@Service
@Transactional
public class PostService {
    private static Logger log = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CompressImgMapper compressImgMapper;

    public List<PostVo> queryTodayEssence() {
        return postMapper.queryTodayEssence();
    }

    public List<PostVo> queryDayAgoEssence(int dayago) {
        return postMapper.queryDayAgoEssence(dayago);
    }

    public List<Circle> queryMayLikeCircle(int userid) {
        return postMapper.queryMayLikeCircle(userid);
    }

    public List<Circle> queryRecommendCircle() {
        return postMapper.queryRecommendCircle();
    }

    public List<Post> queryHotActiveList() {
        return postMapper.queryHotActiveList();
    }

    public List<Post> queryCircleSubPost(Map<String, Object> map) {
        return postMapper.queryCircleSubPost(map);
    }



    public int queryPostNumByCircleid(int circleid) {
        try {
            log.info("查询圈子中更新的帖子数");
            return postMapper.queryPostNumByCircleid(circleid);
        } catch (Exception e) {
            log.error("查询圈子中更新的帖子数失败");
            throw e;
        }
    }

    public PostVo queryPostDetail(Map<String, Object> parammap) {
        try {
            log.info("查询帖子详情");
            return postMapper.queryPostDetail(parammap);
        } catch (Exception e) {
            log.error("查询帖子详情失败");
            throw e;
        }
    }

    public Video queryVideoUrl(int postid) {
        try {
            log.info("查询原生帖子详情");
            return postMapper.queryVideoUrl(postid);
        } catch (Exception e) {
            log.error("查询原生帖子详情失败");
            throw e;
        }
    }

    public List<PostVo> queryPastPostList(Map<String, Object> parammap) {
        return postMapper.queryPastPostList(parammap);
    }

    public List<PostVo> queryPostList(Paging<PostVo> pager, String circleid) {
        try {
            log.info("查询某个圈子发出的所有帖子列表");
            return postMapper.findAllPostList(pager.getRowBounds(), Integer.parseInt(circleid));
        } catch (Exception e) {
            log.error("查询帖子列表失败");
            throw e;
        }
    }

    public List<PostVo> pastHotPostList(Paging<PostVo> pager, int circleid) {
        try {
            log.info("查询圈子中最新的10个热帖");
            return postMapper.findAllPastHotPostList(pager.getRowBounds(), circleid);
        } catch (Exception e) {
            log.error("查询圈子中最新的10个热帖失败");
            throw e;
        }
    }

    public List<PostVo> queryAllActive(Paging<PostVo> pager) {
        try {
            log.info("查询所有活动列表");
            return postMapper.findAllActive(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询所有活动列表失败");
            throw e;
        }
    }

    public int queryActivePartSum(int postid) {
        try {
            log.info("查询该活动的参与总人数");
            return postMapper.queryActivePartSum(postid);
        } catch (Exception e) {
            log.error("查询该活动的参与总人数失败");
            throw e;
        }
    }

    public int queryUserPartSum(Map<String, Object> parammap) {
        try {
            log.info("查询该用户有没有参与过该活动");
            return postMapper.queryUserPartSum(parammap);
        } catch (Exception e) {
            log.error("查询该用户有没有参与过该活动失败");
            throw e;
        }
    }


    public int saveActiveRecord(Map<String, Object> parammap) {
        try {
            log.info("保存用户参与告知类活动的记录");
            return postMapper.saveActiveRecord(parammap);
        } catch (Exception e) {
            log.error("保存用户参与告知类活动的记录失败");
            throw e;
        }
    }

    public ActiveVo queryNoticeActive(Map<String, Object> parammap) {
        try {
            log.info("查询告知类活动详情");
            return postMapper.queryNoticeActive(parammap);
        } catch (Exception e) {
            log.error("查询告知类活动详情失败");
            throw e;
        }

    }

    public List<ActiveVo> queryFourHotActive() {
        try {
            log.info("查询活动详情最下方推荐的四个热门活动");
            return postMapper.queryFourHotActive();
        } catch (Exception e) {
            log.error("查询活动详情最下方推荐的四个热门活动失败");
            throw e;
        }
    }

    public List<Date> queryDateSelect(Paging<Date> pager) {
        try {
            log.info("查询往期精选右上角点击选择日期枚举");
            return postMapper.findAllDateSelect(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询往期精选右上角点击选择日期枚举失败");
            throw e;
        }
    }

    //查询某个帖子所属圈子
    public int queryPostByCircleid(String postid) {
        try {
            log.info("查询某个帖子所属圈子");
            return postMapper.queryPostByCircleid(Integer.parseInt(postid));
        } catch (Exception e) {
            log.error("查询某个帖子所属圈子失败");
            throw e;
        }
    }

    //APP端发布普通帖
    public int releasePost(Post post) {
        try {
            log.info("APP端普通帖发布成功");

            return postMapper.releasePost(post);

        } catch (Exception e) {
            log.error("APP端普通帖发布失败");
            throw e;
        }
    }

    //保存发布的帖子中分享的所有商品
    public void insertPostShareGoods(List<PostShareGoods> postShareGoodsList) {
        try {
            log.info("保存发布的帖子中的商品");
            postMapper.insertPostShareGoods(postShareGoodsList);
        } catch (Exception e) {
            log.error("保存发布的帖子中的商品失败");
        }
    }

    //APP端用户删除帖子接口
    public int delPost(Map<String, Object> parammap) {
        try {
            log.info("APP端用户删除帖子接口");
            return postMapper.delPost(parammap);
        } catch (Exception e) {
            log.error("APP端用户删除帖子接口失败");
            throw e;
        }
    }

    //根据userid查询用户收藏的所有商品列表
    public List<Goods> queryCollectGoodsList(int userid) {
        try {
            log.info("根据userid查询用户所有收藏的商品列表");
            return postMapper.queryCollectGoodsList(userid);
        } catch (Exception e) {
            log.error("根据userid查询用户所有收藏的商品列表失败");
            throw e;
        }
    }

    //userid为空时查询所有商品列表
    public List<Goods> queryAllGoodsList(Paging<Goods> pager) {
        try {
            log.info("userid为空时查询所有商品列表");
            return postMapper.findAllGoodsList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("userid为空时查询所有商品列表失败");
            throw e;
        }
    }

    public CompressImg getProtoImg(String imgurl) {
        try {
            log.info("根据缩略图url查询原图url和大小");
            return compressImgMapper.getProtoImg(imgurl);
        } catch (Exception e) {
            log.error("根据缩略图url查询原图url和大小失败", e);
            throw e;
        }
    }

    //查询当前用户有没有点赞过该帖子
    public int queryIsZanPost(Map<String, Object> parammap) {
        try {
            log.info("查询当前用户有没有点赞过该帖子");
            return postMapper.queryIsZanPost(parammap);
        } catch (Exception e) {
            log.error("查询当前用户有没有点赞过该帖子失败", e);
            throw e;
        }
    }

    //插入一条用户点赞帖子的记录
    public void insertZanRecord(Map<String, Object> parammap) {
        try {
            log.info("插入一条用户点赞帖子的记录");
            postMapper.insertZanRecord(parammap);
        } catch (Exception e) {
            log.error("插入一条用户点赞帖子的记录失败", e);
            throw e;
        }
    }

    //跟新帖子点赞次数
    public int updatePostByZanSum(int id) {
        try {
            log.info("更新帖子点赞次数 id=" + id);
            return postMapper.updatePostByZanSum(id);
        } catch (Exception e) {
            log.error("帖子点赞次数更新异常 id=" + id, e);
            throw e;
        }
    }

    public int queryPostByZanSum(int id) {
        try {
            log.info("查询帖子点赞次数 id=", id);
            return postMapper.queryPostByZanSum(id);
        } catch (Exception e) {
            log.info("查看帖子点赞次数异常 id=" + id, e);
            throw e;
        }
    }

    public void updatePostCollectCount(Map<String, Object> parammap) {
        try {
            log.info("取消帖子收藏后更新帖子被收藏总次数");
            postMapper.updatePostCollectCount(parammap);
        } catch (Exception e) {
            log.error("取消帖子收藏后更新帖子被收藏总次数失败");
            throw e;
        }
    }

    /**
     * 更新帖子评论次数字段
     *
     * @param postid
     * @return int
     */
    public int updatePostBycommentsum(int postid) {
        try {
            log.info("更新帖子的评论数量 postid=" + postid);
            return postMapper.updatePostBycommentsum(postid);
        } catch (Exception e) {
            log.error("帖子更新评论数量异常 postid=" + postid, e);
            throw e;
        }
    }

    /**
     * 更新帖子评论次数字段
     *
     * @param postid
     * @return
     */
    public int updatePostBycommentsumT(int postid) {
        try {
            log.info("更新帖子的评论数量 postid=" + postid);
            return postMapper.updatePostBycommentsum(postid);
        } catch (Exception e) {
            log.error("帖子更新评论数量异常 postid=" + postid);
            throw e;
        }
    }

     /**
     * 后台管理-查询帖子列表
     *
     * @param pager
      * @return List
     */
     public List<PostList> queryPostByList(Paging<PostList> pager) {
        try {
            log.info("查询帖子列表");
            return postMapper.findAllqueryPostByList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子列表异常", e);
            throw e;
        }
     }

    /**
     * 查询帖子列表
     *
     * @param userid
     * @param pager
     * @return list
     */
    public List<PostList> queryPostByList2(Integer userid, Paging<PostList> pager) {
        try {
            log.info("查询帖子列表 userid=" + userid);
            return postMapper.findAllqueryPostByList2(userid, pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子列表异常 userid=" + userid);
            throw e;
        }
    }

    /**
     * 查询帖子列表
     *
     * @param userid
     * @param pager
     * @return list
     */
    public List<PostList> queryPostByManageByList(Integer userid, Paging<PostList> pager) {
        try {
            log.info("查询帖子列表userid=" + userid);
            return postMapper.findAllqueryPostByManageByList(userid, pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子列表异常 userid=" + userid);
            throw e;
        }
    }

    /**
     * 根据圈子id查询帖子列表
     *
     * @param map
     * @return
     */
    public List<PostList> queryPostByCircleId(Map map, Paging<PostList> pager) {
        try {
            log.info("根据圈子id查询帖子列表");
            return postMapper.findAllqueryPostByCircleId(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("根据圈子id查询帖子列表异常",e);
            throw e;
        }
    }

    /**
     * 根据用户id查询帖子列表
     * @param map
     * @return
     */
    public List<PostList> queryPostListByUserid(Map map, Paging<PostList> pager) {
        try {
            log.info("根据用户id查询帖子列表");
            return postMapper.findAllQueryPostListByUserid(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("根据用户id查询帖子列表",e);
            throw e;
        }
    }

    /**
     * 根据用户id查询用户被收藏帖子列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<PostList> queryCollectionListByUserid(String userid, Paging<PostList> pager) {
        try {
            log.info("根据用户id查询用户收藏帖子列表 userid=" + userid);
            return postMapper.findAllQueryCollectionListByUserid(userid, pager.getRowBounds());
        } catch (Exception e) {
            log.error("根据用户id查询用户收藏帖子列表异常 userid=" + userid,e);
            throw e;
        }
    }

    /**
     * 后台管理-查询精贴列表
     * @param pager
     * @return
     */
    public List<PostList> queryPostIsessenceByList(Map map, Paging<PostList> pager) {

        try{
            log.info("查询精贴列表");
            return postMapper.findAllIsessenceByList(map, pager.getRowBounds());

        }catch (Exception e) {
            log.error("查询精贴列表异常",e);
            throw  e;
        }
    }

    /**
     * 后台管理--查询活动列表（草稿箱）
     * @param pager
     * @return
     */
    public List<PostList> queryPostActiveByList(Paging<PostList> pager){
        try{
            log.info("查询活动列表");
            return postMapper.findAllActiveByList(pager.getRowBounds());
        }catch (Exception e) {
            log.error("查询活动列表失败",e);
            throw  e;
        }
    }

    /**
     * 后台管理*-查询活动列表
     * @param pager
     * @return
     */
    public List<PostActiveList> queryPostActiveToByList(Paging<PostActiveList> pager){
            try{
                log.info("查询活动列表");
                return  postMapper.findAllActiveTOByList(pager.getRowBounds());
            }catch (Exception e) {
                log.error("查询活动列表失败",e);
                throw e;
            }

    }


    /**
     * 后台管理-查询单价
     * @param postid
     * @return
     */
    public Double queryPostActiveFee(Integer postid){
        try {
            log.info("查询单价postid=" + postid);
            return postMapper.findAllActivefee(postid);
        }catch (Exception e) {
            log.error("查询单价失败 postid=" + postid,e);
            throw  e;
        }

    }
    /**
     * 后台管理-查询报名人数
     * @param postid
     * @return
     */
    public int queryPostPerson(Integer postid){

        try {
            log.info("查询报名人数 postid=" + postid);
                return postMapper.findAllPerson(postid);
            }catch(Exception e) {
            log.error("查询报名人数失败 postid=" + postid,e);
                throw  e;
            }
    }


    /**
     * 后台管理-增加活动
     * @param post
     * @return
     */
    public int addPostActiveList(PostTo post) {
        try {
            log.info("增加活动成功 post="+post);
            return postMapper.insertActivet(post);
        }catch (Exception e) {
            log.error("增加活动失败 post=" + post,e);
            throw e;
        }
    }

    /**
     * 后台管理-增加活动周期
     * @param period
     * @return
     */
    public  int addPostPeriod(Period period){
        try {
            log.info("增加活动周期 period" + period);
            return postMapper.insertPerid(period);
        }catch (Exception e) {
            log.error("增加活动失败 period=" + period,e);
            throw e;
        }

    }
    /**
     * 删除帖子
     *
     * @param postid
     * @return
     */
    public int deletePost(Integer postid) {
        try {
            log.info("逻辑删除帖子 postid=" + postid);
            return postMapper.deletePost(postid);
        } catch (Exception e) {
            log.error("逻辑删除帖子失败 postid=" + postid,e);
            throw e;
        }
    }



    /**
     * 帖子预览
     * @param postid
     * @return
     */
    public PostList queryPostParticulars(Integer postid) {
        try {
            log.info("帖子预览 postid=" + postid);
            return postMapper.queryPostParticulars(postid);
        } catch (Exception e) {
            log.error("帖子预览异常 postid=" + postid,e);
            throw e;
        }
    }

    /**
     * 活动预览
     *
     * @param postid
     * @return
     */
    public PostList queryActivityParticulars(Integer postid) {
        try {
            log.info("活动预览");
            return postMapper.queryActivityParticulars(postid);
        } catch (Exception e) {
            log.error("活动预览异常 postid=" + postid,e);
            throw e;
        }
    }
     /**
     * 添加帖子
     *
     * @param map
     * @return
     */
     public int addPost(PostTo map) {
        try {
            log.info("添加帖子");
            return postMapper.insertSelectivet(map);
        } catch (Exception e) {
            log.error("添加帖子异常",e);
            throw e;
        }
    }

    /**
     * 帖子添加商品
     * @param typ
     * @return
     */
    public int insertGoods(Map typ) {
        try {
            log.info("帖子添加商品");
            return postMapper.insertGoods(typ);
        } catch (Exception e) {
            log.error("帖子添加商品异常",e);
            throw e;
        }
    }

    /**
     * 添加商城促销类商品
     *
     * @param map
     * @return
     */
    public int insertPromotionGoods(Map map) {
        try {
            log.info("添加商城促销类商品");
            return postMapper.insertPromotionGoods(map);
        } catch (Exception e) {
            log.error("添加商城促销类商品异常",e);
            throw e;
        }
    }

    /**
     * 查询帖子的总和和精贴数量
     *
     * @param circleid
     * @return
     */
    public PostNum queryPostNumAndisessenceByCircleid(Integer circleid) {
        try {
            log.info("查询帖子的总数和精贴数量");
            return postMapper.queryPostNumAndisessenceByCircleid(circleid);
        } catch (Exception e) {
            log.error("查询帖子的总数和精贴数量异常");
            throw e;
        }
    }

    /**
     * 根据id查询帖子是否加精
     * @param id
     * @return
     */
    public int queryPostByIsessence(String id) {
        try {
            log.info("根据id查询帖子是否加精");
            return postMapper.queryPostByIsessence(id);
        } catch (Exception e) {
            log.error("根据id查询帖子是否加精异常 id="+id,e);
            throw e;
        }
    }

    /**
     * 修改帖子加精
     *
     * @param post
     * @return
     */
    public int updatePostChoiceness(PostTo post) {
        try {
            log.info("修改帖子加精");
            return postMapper.updatePostChoiceness(post);
        } catch (Exception e) {
            log.error("修改帖子加精异常 post=" + post,e);
            throw e;
        }
    }

    /**
     * 帖子添加精选
     *
     * @param postid
     * @return
     */
    public int addPostChoiceness(PostTo postid) {
        try {
            log.info("帖子添加精选");
            return postMapper.addPostChoiceness(postid);
        } catch (Exception e) {
            log.error("帖子添加精选异常 postid=" + postid,e);
            throw e;
        }
    }

    /**
     * 帖子取消加精
     *
     * @param postid
     * @return
     */
    public int deletePostChoiceness(Integer postid) {
        try {
            log.info("帖子取消加精");
            return postMapper.deletePostChoiceness(postid);
        } catch (Exception e) {
            log.error("帖子取消加精异常 postid=" + postid,e);
            throw e;
        }
    }

    /**
     * 查询帖子是否加精
     *
     * @return
     */
    public List<PostTo> queryPostChoicenesslist(Map essencedate) {
        try {
            log.info("查询当日帖子加精排序列表");
            return postMapper.queryPostChoicenesslist(essencedate);
        } catch (Exception e) {
            log.error("查询当日帖子加精排序列表异常",e);
            throw e;
        }
    }

    /**
     * 查询今日加精
     *
     * @return
     */
    public List<Post> queryPostIsessence() {
        try {
            log.info("添加编辑时查询今日可加精排序");
            return postMapper.queryPostIsessence();
        } catch (Exception e) {
            log.error("添加编辑时查询今日可加精排序异常");
            throw e;
        }
    }

    /**
     * 查询帖子加精回显数据
     *
     * @param postid
     * @return
     */
    public PostChoiceness queryPostChoiceness(Integer postid) {
        try {
            log.info("查询帖子，返回加精回填数据");
            return postMapper.queryPostChoiceness(postid);
        } catch (Exception e) {
            log.error("加精数据查询异常 postid=" + postid,e);
            throw e;
        }
    }

    /**
     * 帖子编辑数据回显
     *
     * @param postid
     * @return
     */
    public PostCompile queryPostByIdEcho(Integer postid) {
        try {
            log.info("帖子编辑数据回显");
            return postMapper.queryPostByIdEcho(postid);
        } catch (Exception e) {
            log.error("帖子编辑数据回显异常 postid=" + postid,e);
            throw e;
        }
    }

    /**
     * 编辑帖子
     * @param post
     * @return
     */
    public Integer updatePostById(PostTo post) {
        try {
            log.info("编辑帖子");
            return postMapper.updateByPrimaryKeySelectiveById(post);
        } catch (Exception e) {
            log.error("编辑帖子异常 postid=" + post, e);
            throw e;
        }
    }

    /**
     * 编辑活动帖子
     *
     * @param postActiveList
     * @return
     */
    public Integer updateActivePostById(PostActiveList postActiveList) {
        return postMapper.updateActiveById(postActiveList);
    }

    /**
     * 编辑活动帖子
     *
     * @param period
     * @return
     */
    public Integer updateActivePostPerById(Period period) {
        return postMapper.updateActiveByIdP(period);
    }

    /**
     * 帖子按条件查询
     * @param
     * @return
     */
    public List<PostList> postSearch(Map spread, Paging<PostList> pager) {
        try {
            log.info("帖子条件查询");
            return postMapper.findAllpostSearch(spread, pager.getRowBounds());
        } catch (Exception e) {
            log.error("帖子条件查询异常",e);
            throw e;
        }
    }

    /**
     * 查询圈子下的帖子列表
     *
     * @param map
     * @param pager
     * @return
     */
    public List<PostList> findAllQueryCircleByPostList(Map map, Paging<PostList> pager) {
        try {
            log.info("查询圈子下的帖子列表");
            return postMapper.findAllQueryCircleByPostList(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询圈子下的帖子列表异常",e);
            throw e;
        }
    }

    /**
     * 条件查询
     *
     * @param map
     * @param pager
     * @return
     */
    public List<PostActiveList> queryAllActivePostCondition(Map map, Paging<PostActiveList> pager) {
        try {
            log.info("条件查询成功");
            return postMapper.findAllActivePostCondition(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件查询失败",e);
            throw e;
        }
    }

    /**
     * 查询我收藏的帖子/活动列表
     *
     * @param paging
     * @param map
     * @return
     */
    public List<Post> findAllMyCollectPost(Paging<Post> paging, Map map) {
        try {
            log.info("查询我收藏的帖子/活动列表");
            return postMapper.findAllMyCollectPost(paging.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询我收藏的帖子/活动列表失败", e);
            throw e;
        }
    }

    public List<Post> queryMyPostList(Map map) {
        try {
            log.info("查询我的全部帖子");
            return postMapper.queryMyPostList(map);
        } catch (Exception e) {
            log.error("查询我的全部帖子失败", e);
            throw e;
        }
    }

    /**
     * 根据id查询活动帖子
     *
     * @param id
     * @return
     */
    public PostActiveList queryActiveById(Integer id) {
        try {
            log.info("根据id查询活动帖子");
            return postMapper.queryActiveById(id);
        } catch (Exception e) {
            log.error("根据id查询活动帖子失败 id=" +id, e);
            throw e;
        }
    }

    /**
     * 查询用户收藏的帖子列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<PostList> queryCollectPostList(String userid, Paging<PostList> pager) {
        try {
            log.info("查询用户收藏的帖子列表");
            return postMapper.findAllQueryCollectPostList(userid, pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询用户收藏的帖子列表 userid=" + userid,e);
            throw e;
        }
    }

    /**
     * 遍历帖子中所有图片进行压缩处理前先查询该图片有没有压缩过
     */
    public int queryIsHaveCompress(String imgurl) {
        try {
            log.info("判断帖子中当前遍历的图片有没有压缩过");
            return compressImgMapper.queryIsHaveCompress(imgurl);
        } catch (Exception e) {
            log.error("判断帖子中当前遍历的图片有没有压缩过失败 imgurl=" + imgurl,e);
            throw e;
        }
    }

    /**
     * 查询帖子中的这张图片在压缩映射关系表中是否存在
     */
    public int queryCount(CompressImg compressImg) {
        try {
            log.info("查询帖子中的这张图片在压缩映射关系表中是否存在");
            return compressImgMapper.queryCount(compressImg);
        } catch (Exception e) {
            log.error("查询帖子中的这张图片在压缩映射关系表中是否存在失败 compressImg=" + compressImg,e);
            throw e;
        }
    }

    /**
     * 帖子中高清图片压缩处理后保存原图和压缩图url对应关系
     */
    public void addCompressImg(CompressImg compressImg) {
        try {
            log.info("保存原图和压缩图url对应关系");
            compressImgMapper.insertSelective(compressImg);
        } catch (Exception e) {
            log.error("保存原图和压缩图url对应关系失败 compressImg=" + compressImg,e);
            throw e;
        }
    }

    /**
     * 是热门
     *
     * @param id
     * @return
     */
    public Integer updateIshot(Integer id) {
        try {
            log.info("是否设为热门");
            return postMapper.updateIshot(id);
        } catch (Exception e) {
            log.error("是否设为热门失败 id=" +id, e);
            throw e;
        }
    }

    /**
     * 是否设为热门
     *
     * @param id
     * @return
     */
    public Integer activeIsHot(Integer id) {
        try {
            log.info("是否设为热门");
            return postMapper.activeIsHot(id);
        } catch (Exception e) {
            log.error("是否设为热门失败 id=" +id, e);
            throw e;
        }
    }

    /**
     * 不是热门
     *
     * @param id
     * @return
     */
    public Integer updateNoIshot(Integer id) {
        try {
            log.info("不是热门");
            return postMapper.updateNoIshot(id);
        } catch (Exception e) {
            log.error("不是热门热门失败 id=" +id, e);
            throw e;
        }
    }

    /**
     * 特邀嘉宾操作帖子，加入精选池
     * @param postid
     * @return
     */
    public Integer addPostByisessencepool(Integer postid) {
        try {
            log.info("特邀嘉宾操作帖子，加入精选池");
            return postMapper.addPostByisessencepool(postid);
        } catch (Exception e) {
            log.error("特邀嘉宾操作帖子，加入精选池异常 postid=" + postid,e);
            throw e;
        }
    }

    /**
     * 根据帖子id查询用户id
     *
     * @param postid
     * @return
     */
    public int queryUserByPostid(String postid) {
        try {
            log.info("根据帖子id查询用户id");
            return postMapper.queryUserByPostid(postid);
        } catch (Exception e) {
            log.error("根据帖子id查询用户id异常 postid=" + postid,e);
            throw e;
        }
    }

    /**
     * 查询精选池帖子列表
     *
     * @param pager
     * @return
     */
    public List<PostList> queryPostByIsessencepoolList(Paging<PostList> pager) {
        try {
            log.info("查询精选池帖子列表");
            return postMapper.findAllqueryPostByIsessencepoolList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询精选池帖子列表异常",e);
            throw e;
        }
    }
}

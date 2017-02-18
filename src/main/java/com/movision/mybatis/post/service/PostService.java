package com.movision.mybatis.post.service;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.period.entity.Period;
import com.movision.mybatis.post.entity.*;
import com.movision.mybatis.post.mapper.PostMapper;
import com.movision.mybatis.postShareGoods.entity.PostShareGoods;
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

    public String queryVideoUrl(int postid) {
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

    public List<PostVo> queryPostList(Paging<Post> pager, String circleid) {
        try {
            log.info("查询某个圈子发出的所有帖子列表");
            return postMapper.queryPostList(pager.getRowBounds(), Integer.parseInt(circleid));
        } catch (Exception e) {
            log.error("查询帖子列表失败");
            throw e;
        }
    }

    public List<PostVo> pastHotPostList(Paging<PostVo> pager, int circleid) {
        try {
            log.info("查询圈子中最新的10个热帖");
            return postMapper.pastHotPostList(pager.getRowBounds(), circleid);
        } catch (Exception e) {
            log.error("查询圈子中最新的10个热帖失败");
            throw e;
        }
    }

    public List<PostVo> queryAllActive(Paging<Post> pager) {
        try {
            log.info("查询所有活动列表");
            return postMapper.queryAllActive(pager.getRowBounds());
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

    public ActiveVo queryNoticeActive(String postid) {
        try {
            log.info("查询告知类活动详情");
            return postMapper.queryNoticeActive(Integer.parseInt(postid));
        } catch (Exception e) {
            log.error("查询告知类活动详情失败");
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

    //根据userid查询用户收藏的所有商品列表
    public List<Goods> queryCollectGoodsList(Paging<Goods> pager, int userid) {
        try {
            log.info("根据userid查询用户所有收藏的商品列表");
            return postMapper.queryCollectGoodsList(pager.getRowBounds(), userid);
        } catch (Exception e) {
            log.error("根据userid查询用户所有收藏的商品列表失败");
            throw e;
        }
    }

    //userid为空时查询所有商品列表
    public List<Goods> queryAllGoodsList(Paging<Goods> pager) {
        try {
            log.info("userid为空时查询所有商品列表");
            return postMapper.queryAllGoodsList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("userid为空时查询所有商品列表失败");
            throw e;
        }
    }

    //跟新帖子点赞次数
    public int updatePostByZanSum(int id) {
        try {
            log.info("更新帖子点赞次数");
            return postMapper.updatePostByZanSum(id);
        } catch (Exception e) {
            log.error("帖子点赞次数更新异常");
            throw e;
        }
    }

    public int queryPostByZanSum(int id) {
        try {
            log.info("查询帖子点赞次数");
            return postMapper.queryPostByZanSum(id);
        } catch (Exception e) {
            log.info("查看帖子点赞次数异常");
            throw e;
        }
    }

    public int updatePostBycommentsum(int postid) {
        try {
            log.info("更新帖子的评论数量");
            return postMapper.updatePostBycommentsum(postid);
        } catch (Exception e) {
            log.error("帖子更新评论数量异常");
            throw e;
        }
    }
     /**
     * 后台管理-查询帖子列表
     *
     * @param pager
     * @return
     */
    public List<PostList> queryPostByList(Paging<PostList> pager) {
        try {
            log.info("查询帖子列表");
            return postMapper.findAllqueryPostByList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子列表异常");
            throw e;
        }
    }

    /**
     * 后台管理-查询精贴列表
     * @param pager
     * @return
     */
    public List<PostList> queryPostIsessenceByList(Paging<PostList> pager){

        try{
            log.info("查询精贴列表");
            return postMapper.findAllIsessenceByList(pager.getRowBounds());

        }catch (Exception e){
            log.error("查询精贴列表异常");
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
        }catch (Exception e){
            log.error("查询活动列表失败");
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
            }catch (Exception e){
                log.error("查询活动列表失败");
                throw e;
            }

    }
    /**
     * 后台管理-查询帖子总数
     * @return
     */
    public Integer queryPostNum() {
        try {
            log.info("查询帖子总数");
            return postMapper.queryPostNum();
        } catch (Exception e) {
            log.error("查询帖子总数失败");
            throw e;
        }
    }

    /**
     * 后台管理-查询单价
     * @param postid
     * @return
     */
    public Double queryPostActiveFee(Integer postid){
        try{
            log.info("查询单价");
            return postMapper.findAllActivefee(postid);
        }catch (Exception e){
            log.error("查询单价失败");
            throw  e;
        }

    }
    /**
     * 后台管理-查询报名人数
     * @param postid
     * @return
     */
    public int queryPostPerson(Integer postid){

            try{
                log.info("查询报名人数");
                return postMapper.findAllPerson(postid);
            }catch(Exception e){
                log.error("查询报名人数失败");
                throw  e;
            }

    }

    /**
     * 后台管理-增加活动
     * @param post
     * @return
     */
    public int addPostActiveList(Post post){
        try{
            log.info("增加活动成功");
            return postMapper.insertActive(post);
        }catch (Exception e){
            log.error("增加活动失败");
            throw e;
        }
    }

    /**
     * 后台管理-增加活动周期
     * @param period
     * @return
     */
    public  int addPostPeriod(Period period){
        try{
            log.info("增加活动周期");
            return postMapper.insertPerid(period);
        }catch (Exception e){
            log.error("增加活动失败");
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
            log.info("逻辑删除帖子");
            return postMapper.deletePost(postid);
        } catch (Exception e) {
            log.error("逻辑删除帖子失败");
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
            log.info("帖子预览");
            return postMapper.queryPostParticulars(postid);
        } catch (Exception e) {
            log.error("帖子预览异常");
            throw e;
        }
    }
     /**
     * 添加帖子
     *
     * @param map
     * @return
     */
    public int addPost(Post map) {
        try {
            log.info("添加帖子");
            return postMapper.insertSelective(map);
        } catch (Exception e) {
            log.error("添加帖子异常");
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
     * 帖子添加精选
     *
     * @param postid
     * @return
     */
    public int addPostChoiceness(Post postid) {
        try {
            log.info("帖子添加精选");
            return postMapper.addPostChoiceness(postid);
        } catch (Exception e) {
            log.error("帖子添加精选异常");
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
            log.error("帖子取消加精异常");
            throw e;
        }
    }

    /**
     * 查询帖子是否加精
     *
     * @return
     */
    public List<Post> queryPostChoiceness() {
        try {
            log.info("查询帖子是否加精");
            return postMapper.queryPostChoiceness();
        } catch (Exception e) {
            log.error("查询帖子是否加精异常");
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
            log.error("帖子编辑数据回显异常");
            throw e;
        }
    }

    public Integer updatePostById(Post post) {
        return postMapper.updateByPrimaryKeySelectiveById(post);
    }



    /**
     * 帖子按条件查询
     * @param map
     * @return
     *//*
    public List<Object> postSearch(Map<String,Object> map){
        try {
            log.info("帖子条件查询");
            return postMapper.postSearch(map);
        } catch (Exception e) {
            log.error("帖子条件查询异常");
            throw e;
        }
    }*/

}

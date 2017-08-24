package com.movision.mybatis.postLabel.service;

import com.movision.mybatis.circle.entity.CircleCount;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.footRank.entity.FootRank;
import com.movision.mybatis.footRank.mapper.FootRankMapper;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.postLabel.entity.*;
import com.movision.mybatis.postLabel.mapper.PostLabelMapper;
import com.movision.mybatis.postLabelRelation.entity.PostLabelRelation;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.pagination.model.Paging;
import javafx.geometry.Pos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.peer.LabelPeer;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/7/19 15:50
 */
@Service
public class PostLabelService {

    private static Logger log = LoggerFactory.getLogger(PostLabelService.class);
    @Autowired
    private PostLabelMapper postLabelMapper;

    @Autowired
    private FootRankMapper footRankMapper;

    public List<PostLabelVo> getMineFollowLabel(Map<String, Object> parammap, Paging<PostLabelVo> pager){
        try {
            log.info("查询我的--关注--标签（我关注的标签列表）");
            return postLabelMapper.getMineFollowLabel(parammap, pager.getRowBounds());
        }catch (Exception e){
            log.error("查询我的--关注--标签（我关注的标签列表）", e);
            throw e;
        }
    }

    public List<PostLabel> queryLabelName() {
        try {
            log.info("查询所有标签");
            return postLabelMapper.queryLableName();
        } catch (Exception e) {
            log.error("查询所有标签失败", e);
            throw e;
        }
    }

    public Integer updateLabelHeatValue(Map map) {
        try {
            log.info("根据标签修改热度");
            return postLabelMapper.updateLabelHeatValue(map);
        } catch (Exception e) {
            log.error("根据标签修改热度失败", e);
            throw e;
        }
    }

    public List<PostLabel> queryLabelHeatValue() {
        try {
            log.info("根据热度排序");
            return postLabelMapper.queryLabelHeatValue();
        } catch (Exception e) {
            log.error("根据热度排序失败", e);
            throw e;
        }
    }

    public PostLabelTz queryName(int labelid) {
        try {
            log.info("查询名称");
            return postLabelMapper.queryName(labelid);
        } catch (Exception e) {
            log.error("查询名称失败", e);
            throw e;
        }
    }

    /**
     * 查询帖子标签列表
     *
     * @param label
     * @param pag
     * @return
     */
    public List<PostLabelDetails> findAllQueryPostLabelList(PostLabelVo label, Paging<PostLabelDetails> pag) {
        try {
            log.info("查询帖子标签列表");
            return postLabelMapper.findAllQueryPostLabelList(label, pag.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子标签列表异常", e);
            throw e;
        }
    }

    public int batchInsert(List<PostLabel> postLabelList) {
        try {
            log.info("新增帖子标签");
            return postLabelMapper.batchInsert(postLabelList);
        } catch (Exception e) {
            log.error("新增帖子标签失败", e);
            throw e;
        }
    }

    /**
     * 新增标签
     *
     * @param label
     */
    public void insertPostLabel(PostLabel label) {
        try {
            log.info("新增标签");
            postLabelMapper.insertSelective(label);
        } catch (Exception e) {
            log.error("新增标签异常", e);
            throw e;
        }
    }

    /**
     * 查询标签详情
     *
     * @param label
     * @return
     */
    public PostLabelDetails queryPostLabelById(PostLabel label) {
        try {
            log.info("查询标签详情");
            return postLabelMapper.queryPostLabelById(label);
        } catch (Exception e) {
            log.error("查询标签详情异常", e);
            throw e;
        }
    }

    /**
     * 修改帖子标签
     *
     * @param label
     */
    public void updatePostLabel(PostLabel label) {
        try {
            log.info("修改帖子标签");
            postLabelMapper.updateByPrimaryKeySelective(label);
        } catch (Exception e) {
            log.error("修改帖子标签异常", e);
            throw e;
        }
    }

    /**
     * 删除帖子标签
     *
     * @param label
     */
    public void deletePostLabel(PostLabel label) {
        try {
            log.info("删除帖子标签");
            postLabelMapper.deletePostLabel(label);
        } catch (Exception e) {
            log.error("删除帖子标签异常", e);
            throw e;
        }
    }

    /**
     * 查询标签是否推荐到首页
     *
     * @param label
     * @return
     */
    public Integer queryPostLabeIsRecommend(PostLabel label) {
        try {
            log.info("查询标签是否推荐到首页");
            return postLabelMapper.queryPostLabeIsRecommend(label);
        } catch (Exception e) {
            log.error("查询标签是否推荐到首页异常", e);
            throw e;
        }
    }


    /**
     * 标签推荐、取消推荐到发现页
     *
     * @param label
     */
    public void updatePostLabelIsRecommend(PostLabel label) {
        try {
            log.info("标签推荐、取消推荐到发现页");
            postLabelMapper.updatePostLabelIsRecommend(label);
        } catch (Exception e) {
            log.error("标签推荐、取消推荐到发现页异常", e);
            throw e;
        }
    }

    public List<Integer> queryLabelIdList(String[] strings) {
        try {
            log.info("根据标签名称集合查询标签id集合");
            return postLabelMapper.queryLabelIdList(strings);
        } catch (Exception e) {
            log.error("根据标签名称集合查询标签id集合失败", e);
            throw e;
        }
    }


    public List<PostLabelCount> queryCountLabelName(int labelid) {
        try {
            log.info("查询帖子标签列表");
            return postLabelMapper.queryCountLabelName(labelid);
        } catch (Exception e) {
            log.error("查询帖子标签列表异常", e);
            throw e;
        }
    }

    public CircleVo queryCircleByPostid(int circleid) {
        try {
            log.info("根据帖子id查询圈子标签");
            return postLabelMapper.queryCircleByPostid(circleid);
        } catch (Exception e) {
            log.error("根据帖子id查询圈子标签异常", e);
            throw e;
        }
    }

    public List<User> queryCircleManager(int circleid) {
        try {
            log.info("圈子管理员");
            return postLabelMapper.queryCircleManager(circleid);
        } catch (Exception e) {
            log.error("圈子管理员异常", e);
            throw e;
        }
    }

    public Integer postInCircle(int circleid) {
        try {
            log.info("今日在圈子发的帖子");
            return postLabelMapper.postInCircle(circleid);
        } catch (Exception e) {
            log.error("今日在圈子发的帖子异常", e);
            throw e;
        }
    }

    public List<PostLabel> queryLabelCircle(int circleid) {
        try {
            log.info("圈子里的标签");
            return postLabelMapper.queryLabelCircle(circleid);
        } catch (Exception e) {
            log.error("圈子里的标签异常", e);
            throw e;
        }
    }

    public List<PostVo> findAllHotPost(int circleid, Paging<PostVo> paging) {
        try {
            log.info("圈子里的最热帖子");
            return postLabelMapper.findAllHotPost(circleid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("圈子里的最热帖子异常", e);
            throw e;
        }
    }

    public List<PostVo> findAllNewPost(int circleid, Paging<PostVo> paging) {
        try {
            log.info("圈子里的最新帖子");
            return postLabelMapper.findAllNewPost(circleid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("圈子里的最新帖子失败", e);
            throw e;
        }
    }

    public List<PostVo> findAllIsencePost(int circleid, Paging<PostVo> paging) {
        try {
            log.info("圈子里的精华帖子");
            return postLabelMapper.findAllIsencePost(circleid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("圈子里的精华帖子失败", e);
            throw e;
        }
    }

    public List<CircleCount> queryCirclePeople(int circleid) {
        try {
            log.info("圈子达人");
            return postLabelMapper.queryCirclePeople(circleid);
        } catch (Exception e) {
            log.error("圈子达人失败", e);
            throw e;
        }
    }

    public Integer countSameNameLabel(String name) {
        try {
            log.info("统计同名标签数量");
            return postLabelMapper.countSameNameLabel(name);
        } catch (Exception e) {
            log.error("统计同名标签数量失败", e);
            throw e;
        }
    }

    public Integer countSameNormalNameLabel(String name) {
        try {
            log.info("统计同名普通标签数量");
            return postLabelMapper.countSameNormalNameLabel(name);
        } catch (Exception e) {
            log.error("统计同名普通标签数量失败", e);
            throw e;
        }
    }


    public List<PostLabel> isrecommendLabel() {
        try {
            log.info("有没有推荐标签");
            return postLabelMapper.isrecommendLabel();
        } catch (Exception e) {
            log.error("有没有推荐标签失败", e);
            throw e;
        }
    }


    public List<Integer> labelId(int userid) {
        try {
            log.info("用户关注标签");
            return postLabelMapper.labelId(userid);
        } catch (Exception e) {
            log.error("用户关注标签失败", e);
            throw e;
        }
    }

    public List<PostLabel> queryHotValueLabelList(Integer n) {
        try {
            log.info("查询热门的标签集合");
            return postLabelMapper.queryHotValueLabelList(n);
        } catch (Exception e) {
            log.error("查询热门的标签集合失败", e);
            throw e;
        }
    }

    /**
     * 查询标签名称列表
     *
     * @param postLabel
     * @return
     */
    public List<PostLabel> queryPostLabelByName(PostLabel postLabel) {
        try {
            log.info("查询标签名称列表(boss)");
            return postLabelMapper.queryPostLabelByName(postLabel);
        } catch (Exception e) {
            log.error("查询标签名称列表（boss）异常", e);
            throw e;
        }
    }

    public int updatePostHeatValue(int postid) {
        try {
            log.info("根据帖子id减低热度值");
            return postLabelMapper.updatePostHeatValue(postid);
        } catch (Exception e) {
            log.error("根据帖子id减低热度值）异常", e);
            throw e;
        }
    }

    public int updateUserHeatValue(int userids) {
        try {
            log.info("根据id减低热度值");
            return postLabelMapper.updateUserHeatValue(userids);
        } catch (Exception e) {
            log.error("根据id减低热度值）异常", e);
            throw e;
        }
    }

    public int queryPostHeatValue(int postid) {
        try {
            log.info("查询贴热度值");
            return postLabelMapper.queryPostHeatValue(postid);
        } catch (Exception e) {
            log.error("查询贴热度值异常", e);
            throw e;
        }
    }

    public int heatvale(int postid) {
        try {
            log.info("查询贴热度值");
            return postLabelMapper.heatvale(postid);
        } catch (Exception e) {
            log.error("查询贴热度值异常", e);
            throw e;
        }
    }

    public int userHeatVale(int userids) {
        try {
            log.info("查询贴热度值");
            return postLabelMapper.userHeatVale(userids);
        } catch (Exception e) {
            log.error("查询贴热度值异常", e);
            throw e;
        }
    }

    public List<GeographicLabel> getfootmap(int userid){
        try {
            log.info("查询该作者发布的所有帖子中包含的地理标签列表");
            return postLabelMapper.getfootmap(userid);
        }catch (Exception e){
            log.error("查询该作者发布的所有帖子中包含的地理标签列表失败");
            throw e;
        }
    }

    public List<FootRank> queryFootMapRank(int userid){
        try {
            log.info("查询当前用户足迹点数目排名");
            return footRankMapper.queryFootMapRank(userid);
        }catch (Exception e){
            log.error("查询当前用户足迹点数目排名失败", e);
            throw e;
        }
    }

    public int queryCircleid(int userids) {
        try {
            log.info("根据帖子id查询圈子");
            return postLabelMapper.queryCircleid(userids);
        } catch (Exception e) {
            log.error("根据帖子id查询圈子异常", e);
            throw e;
        }
    }

    public List<PostVo> queryCircleByPost(int circleid) {
        try {
            log.info("根据帖子id查询圈子");
            return postLabelMapper.queryCircleByPost(circleid);
        } catch (Exception e) {
            log.error("根据帖子id查询圈子异常", e);
            throw e;
        }
    }


    public int queryUserid(int postid) {
        try {
            log.info("根据帖子id查询作者");
            return postLabelMapper.queryUserid(postid);
        } catch (Exception e) {
            log.error("根据帖子id查询作者异常", e);
            throw e;
        }
    }


    public int queryUserHeatValue(int userids) {
        try {
            log.info("根据帖子id查询作者");
            return postLabelMapper.queryUserHeatValue(userids);
        } catch (Exception e) {
            log.error("根据帖子id查询作者异常", e);
            throw e;
        }
    }


    public int followCircle(int circleid) {
        try {
            log.info("圈子关注数");
            return postLabelMapper.followCircle(circleid);
        } catch (Exception e) {
            log.error("圈子关注数异常", e);
            throw e;
        }
    }

    public List<PostLabel> queryCityListByCityname(String name) {
        try {
            log.info("根据城市名称查询列表");
            return postLabelMapper.queryCityListByCityname(name);
        } catch (Exception e) {
            log.error("根据城市名称查询列表失败", e);
            throw e;
        }
    }

    public List<PostLabel> queryGeogLabelByName(String name) {
        try {
            log.info("根据地理名称查询地理标签");
            return postLabelMapper.queryGeogLabelByName(name);
        } catch (Exception e) {
            log.error("根据地理名称查询地理标签失败", e);
            throw e;
        }
    }


    public int isFollowCircleid(Map map) {
        try {
            log.info("当前用户有没有关注该圈子");
            return postLabelMapper.isFollowCircleid(map);
        } catch (Exception e) {
            log.error("当前用户有没有关注该圈子失败", e);
            throw e;
        }
    }


    public List<PostVo> findAllLabelHotPost(Map map, Paging<PostVo> paging) {
        try {
            log.info("标签帖子");
            return postLabelMapper.findAllLabelHotPost(map, paging.getRowBounds());
        } catch (Exception e) {
            log.error("标签帖子失败", e);
            throw e;
        }
    }

    public List<PostVo> findAllLabelNewPost(Map map, Paging<PostVo> paging) {
        try {
            log.info("标签最新帖子");
            return postLabelMapper.findAllLabelNewPost(map, paging.getRowBounds());
        } catch (Exception e) {
            log.error("标签最新帖子失败", e);
            throw e;
        }
    }

    public List<PostVo> findAllLabelIsessenPost(Map map, Paging<PostVo> paging) {
        try {
            log.info("标签精选帖子");
            return postLabelMapper.findAllLabelIsessenPost(map, paging.getRowBounds());
        } catch (Exception e) {
            log.error("标签精选帖子失败", e);
            throw e;
        }
    }

    public List<PostLabel> queryLabelByname(String name) {
        try {
            log.info("根据名称查询帖子标签");
            return postLabelMapper.queryLabelByname(name);
        } catch (Exception e) {
            log.error("根据名称查询帖子标签失败", e);
            throw e;
        }
    }

    public List<PostLabel> findAllLabelByName(Paging<PostLabel> paging, Map map) {
        try {
            log.info("分页，根据名称查询帖子");
            return postLabelMapper.findAllLabelByName(map, paging.getRowBounds());
        } catch (Exception e) {
            log.error("分页，根据名称查询帖子失败", e);
            throw e;
        }
    }
}

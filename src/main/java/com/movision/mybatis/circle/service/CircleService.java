package com.movision.mybatis.circle.service;

import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.entity.CircleAndCircle;
import com.movision.mybatis.circle.entity.*;
import com.movision.mybatis.circle.mapper.CircleMapper;
import com.movision.mybatis.followCircle.mapper.FollowCircleMapper;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/18 18:22
 */
@Service
@Transactional
public class CircleService {

    private static Logger log = LoggerFactory.getLogger(CircleService.class);

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private FollowCircleMapper followCircleMapper;

    public List<CircleVo> queryHotCircleList() {
        try {
            log.info("查询热门圈子列表");
            return circleMapper.queryHotCircleList();
        } catch (Exception e) {
            log.error("查询热门圈子列表失败");
            throw e;
        }
    }

    public CircleVo queryCircleIndex1(int circleid) {
        try {
            log.info("查询圈子详情上半部分数据");
            return circleMapper.queryCircleIndex1(circleid);
        } catch (Exception e) {
            log.error("查询圈子详情上半部分数据失败");
            throw e;
        }
    }

    public List<CircleVo> queryCircleByCategory(int categoryid) {
        try {
            log.info("通过类型查询圈子列表");
            return circleMapper.queryCircleByCategory(categoryid);
        } catch (Exception e) {
            log.error("通过类型查询圈子列表失败");
            throw e;
        }
    }

    public List<CircleVo> queryAuditCircle() {
        try {
            log.info("查询待审核圈子列表");
            return circleMapper.queryAuditCircle();
        } catch (Exception e) {
            log.error("查询待审核圈子列表失败");
            throw e;
        }
    }

    public int queryIsSupport(Map<String, Object> parammap) {
        try {
            log.info("查询当前用户是否已支持该圈子");
            return circleMapper.queryIsSupport(parammap);
        } catch (Exception e) {
            log.error("查询当前用户是否已支持该圈子失败");
            throw e;
        }
    }

    public CircleVo queryCircleInfo(int circleid) {
        try {
            log.info("查询圈子信息（包括公告和简介等）");
            return circleMapper.queryCircleInfo(circleid);
        } catch (Exception e) {
            log.error("查询圈子信息（包括公告和简介等）失败");
            throw e;
        }
    }

    public int querySupportSum(Map<String, Object> parammap) {
        try {
            log.info("查询当前用户是否支持过该圈子");
            return circleMapper.queryIsSupport(parammap);
        } catch (Exception e) {
            log.error("查询当前用户是否支持过该圈子失败");
            throw e;
        }
    }

    public void addSupportSum(Map<String, Object> parammap) {
        try {
            log.info("增加圈子中的支持数+1成功");
            circleMapper.addSupportSum(parammap);
        } catch (Exception e) {
            log.error("增加圈子中的支持数失败");
            throw e;
        }
    }

    public void addSupportRecored(Map<String, Object> parammap) {
        try {
            log.info("增加该用户支持该圈子的记录");
            circleMapper.addSupportRecored(parammap);
        } catch (Exception e) {
            log.error("增加该用户支持该圈子的记录失败");
            throw e;
        }
    }

    public int queryFollowSum(Map<String, Object> parammap) {
        try {
            log.info("查询当前用户是否关注过该圈子");
            return followCircleMapper.queryCountByFollow(parammap);
        } catch (Exception e) {
            log.error("查询当前用户是否关注过该圈子失败");
            throw e;
        }
    }

    public void followCircle(Map<String, Object> parammap) {
        try {
            log.info("当前用户关注该圈子");
            followCircleMapper.followCircle(parammap);
        } catch (Exception e) {
            log.error("当前用户关注该圈子失败");
            throw e;
        }
    }

    public void cancelFollowCircle(Map<String, Object> parammap) {
        try {
            log.info("用户取消关注圈子");
            followCircleMapper.cancelFollowCircle(parammap);
        } catch (Exception e) {
            log.error("用户取消关注圈子失败");
            throw e;
        }
    }

    public int queryCountByFollow(Map<String, Object> parammap) {
        try {
            log.info("查询该用户对当前圈子关注的次数");
            return followCircleMapper.queryCountByFollow(parammap);
        } catch (Exception e) {
            log.info("查询该用户对当前圈子关注的次数失败");
            throw e;
        }
    }

    public String queryCircleByPhone(int circleid) {
        try {
            log.info("查询贴主手机号");
            return circleMapper.queryCircleByPhone(circleid);
        } catch (Exception e) {
            log.error("查询手机号失败");
            throw e;
        }
    }

    public List<Circle> queryHotCircle() {
        try {
            log.info("查询帖子详情最下方推荐的4个热门圈子");
            return circleMapper.queryHotCircle();
        } catch (Exception e) {
            log.error("查询帖子详情最下方推荐的4个热门圈子失败");
            throw e;
        }
    }

    public int queryCircleScope(int circleid) {
        try {
            log.info("查询圈子的开放范围scope");
            return circleMapper.queryCircleScope(circleid);
        } catch (Exception e) {
            log.error("查询圈子的开放范围scope失败");
            throw e;
        }
    }

    public User queryCircleOwner(int circleid) {
        try {
            log.info("查询圈子的所有者userid");
            return circleMapper.queryCircleOwner(circleid);
        } catch (Exception e) {
            log.error("查询圈子的所有者userid失败");
            throw e;
        }
    }



    /**
     * 根据条件查询圈子列表
     *
     * @param map
     * @return
     */
    public List<CircleVo> queryCircleByLikeList(Map map) {
        try {
            log.info("根据条件查询圈子列表");
            return circleMapper.queryCircleByLikeList(map);
        } catch (Exception e) {
            log.error("根据条件查询圈子列表");
            throw e;
        }
    }

    public List<CircleVo> queryCircleManagementByLikeList(Map map) {
        try {
            log.info("根据条件查询圈子列表");
            return circleMapper.queryCircleManagementByLikeList(map);
        } catch (Exception e) {
            log.error("根据条件查询圈子列表异常");
            throw e;
        }
    }


    /**
     * 查询圈子中所有圈子所属分类
     *
     * @return
     */
    public List<CircleIndexList> queryListByCircleCategory(Map map) {
        try {
            log.info("查询圈子中所有圈子所属分类");
            return circleMapper.queryListByCircleCategory(map);
        } catch (Exception e) {
            log.error("查询圈子中所有圈子所属分类异常");
            throw e;
        }
    }


    /**
     * 查询圈子所有的名称和id
     *
     * @return
     */
    public List<Circle> queryListByCircleList(Map categoryid) {
        try {
            log.info("查询圈子名称");
            return circleMapper.queryListByCircleList(categoryid);
        } catch (Exception e) {
            log.error("查询圈子名称异常");
            throw e;
        }
    }

    /**
     * 根据用户id查询所属圈子名称
     *
     * @param map
     * @return
     */
    public List<Circle> queryListByCircleListByUserid(Map map) {
        try {
            log.info("根据用户id查询所属圈子名称");
            return circleMapper.queryListByCircleListByUserid(map);
        } catch (Exception e) {
            log.error("根据用户id查询所属圈子名称异常");
            throw e;
        }
    }

    /**
     * 根据用户id查询所属圈子名称
     *
     * @param map
     * @return
     */
    public List<Circle> queryListByCircleManageListByUserid(Map map) {
        try {
            log.info("根据用户id查询所属圈子名称");
            return circleMapper.queryListByCircleManageListByUserid(map);
        } catch (Exception e) {
            log.error("根据用户id查询所属圈子名称异常");
            throw e;
        }
    }

    /**
     * 查询发现页排序
     *
     * @return
     */
    public List<Circle> queryDiscoverList() {
        try {
            log.info("查询发现页排序");
            return circleMapper.queryDiscoverList();
        } catch (Exception e) {
            log.error("查询发现页排序异常");
            throw e;
        }
    }

    /**
     * 查询圈子是否推荐发现页
     *
     * @param circleid
     * @return
     */
    public int queryCircleDiscover(String circleid) {
        try {
            log.info("查询圈子是否推荐发现页");
            return circleMapper.queryCircleDiscover(circleid);
        } catch (Exception e) {
            log.error("查询圈子是否推荐发现页异常");
            throw e;
        }
    }

    /**
     * 圈子推荐发现页
     *
     * @return
     */
    public int updateDiscover(Map<String, Integer> map) {
        try {
            log.info("圈子推荐到发现页");
            return circleMapper.updateDiscover(map);
        } catch (Exception e) {
            log.error("圈子推荐到发现页异常");
            throw e;
        }
    }

    /**
     * 取消圈子推荐到发现页
     *
     * @param circleid
     * @return
     */
    public int updateDiscoverDel(String circleid) {
        try {
            log.info("取消圈子推荐到发现页");
            return circleMapper.updateDiscoverDel(circleid);
        } catch (Exception e) {
            log.error("取消圈子推荐到发现页异常");
            throw e;
        }
    }

    /**
     * 查询圈子是否推荐到首页
     *
     * @param circleid
     * @return
     */
    public Integer queryCircleRecommendIndex(String circleid) {
        try {
            log.info("查询圈子是否推荐到首页");
            return circleMapper.queryCircleRecommendIndex(circleid);
        } catch (Exception e) {
            log.error("查询圈子是否推荐到首页异常");
            throw e;
        }
    }

    /**
     * 圈子推荐到首页
     *
     * @param circleid
     * @return
     */
    public int updateCircleIndex(Integer circleid) {
        try {
            log.info("圈子推荐到首页");
            return circleMapper.updateCircleIndex(circleid);
        } catch (Exception e) {
            log.error("圈子推荐到首页异常");
            throw e;
        }
    }

    /**
     * 解除圈子推荐到首页
     *
     * @param circleid
     * @return
     */
    public int updateCircleIndexDel(String circleid) {
        try {
            log.info("解除圈子推荐到首页");
            return circleMapper.updateCircleIndexDel(circleid);
        } catch (Exception e) {
            log.error("解除圈子推荐到首页异常");
            throw e;
        }
    }

    /**
     * 查看圈子详情
     *
     * @param circleid
     * @return
     */
    public CircleDetails quryCircleDetails(Integer circleid) {
        try {
            log.info("查看圈子详情");
            return circleMapper.quryCircleDetails(circleid);
        } catch (Exception e) {
            log.error("圈子详情异常");
            throw e;
        }
    }

    /**
     * 编辑圈子
     *
     * @param circleDetails
     * @return
     */
    public int updateCircle(CircleDetails circleDetails) {
        try {
            log.info("编辑圈子");
            return circleMapper.updateCircle(circleDetails);
        } catch (Exception e) {
            log.error("编辑圈子异常");
            throw e;
        }
    }

    /**
     * 编辑圈子数据回显
     *
     * @param circleid
     * @return
     */
    public CircleDetails queryCircleByShow(Integer circleid) {
        try {
            log.info("编辑圈子数据回显");
            return circleMapper.queryCircleByShow(circleid);
        } catch (Exception e) {
            log.error("编辑圈子数据回显");
            throw e;
        }
    }

    /**
     * 圈子推荐到发现页排序
     *
     * @return
     */
    public List<Integer> queryCircleByOrderidList() {
        try {
            log.info("查询圈子推荐发现页排序");
            return circleMapper.queryCircleByOrderidList();
        } catch (Exception e) {
            log.error("查询圈子推荐发现页排序异常");
            throw e;
        }
    }

    /**
     * 添加圈子
     *
     * @param circleDetails
     * @return
     */
    public int insertCircle(CircleDetails circleDetails) {
        try {
            log.info("添加圈子");
            return circleMapper.insertCircle(circleDetails);
        } catch (Exception e) {
            log.error("添加圈子异常");
            throw e;
        }
    }

    public List<Circle> findAllMyFollowCircleList(Paging<Circle> paging, Map map) {
        try {
            log.info("查询我关注的圈子");
            return circleMapper.findAllMyFollowCircleList(paging.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询我关注的圈子失败", e);
            throw e;
        }
    }


    /**
     * 审核圈子
     *
     * @param map
     * @return
     */
    public int updateAuditCircle(Map map) {
        try {
            log.info("审核圈子");
            return circleMapper.updateAuditCircle(map);
        } catch (Exception e) {
            log.error("审核圈子异常");
            throw e;
        }
    }

    /**
     * 修改圈子分类
     *
     * @param map
     * @return
     */
/*    public int updateCircleCategoryClassify(Map map) {
        try {
            log.info("修改圈子分类");
            return circleMapper.updateCircleCategoryClassify(map);
        } catch (Exception e) {
            log.error("修改圈子分类异常");
            throw e;
        }
    }*/

    /**
     * 根据类型Id查询圈子类型
     *
     * @param categoryid
     * @return
     */
    public Category queryCircleCategoryClassify(String categoryid) {
        try {
            log.info("根据类型id查询圈子类型");
            return circleMapper.queryCircleCategoryClassify(categoryid);
        } catch (Exception e) {
            log.error("根据类型id查询圈子类型异常");
            throw e;
        }
    }

    /**
     * 根据用户id查询圈子id
     *
     * @param userid
     * @return
     */
    public List<Integer> queryCIrcleIdByUserId(Integer userid) {
        try {
            log.info("根据用户id查询圈子id");
            return circleMapper.queryCIrcleIdByUserId(userid);
        } catch (Exception e) {
            log.error("根据用户id查询圈子id异常");
            throw e;
        }
    }
}

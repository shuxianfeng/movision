package com.movision.mybatis.homepageManage.service;

import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.entity.HomepageManageVo;
import com.movision.mybatis.homepageManage.mapper.HomepageManageMapper;
import com.movision.mybatis.manageType.entity.ManageType;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/18 14:32
 */
@Service
@Transactional
public class HomepageManageService {
    private static Logger log = LoggerFactory.getLogger(HomepageManageService.class);

    @Autowired
    private HomepageManageMapper homepageManageMapper;

    public HomepageManage queryBanner(int type) {
        try {
            log.info("根据类型查询banner图");
            return homepageManageMapper.queryBanner(type);
        } catch (Exception e) {
            log.error("根据类型查询banner图失败");
            throw e;
        }
    }

    public List<HomepageManage> queryBannerList(int topictype) {
        try {
            log.info("根据类型查询banner图列表");
            return homepageManageMapper.queryBannerList(topictype);
        } catch (Exception e) {
            log.error("根据类型查询banner图列表失败");
            throw e;
        }
    }

    /**
     * 查询广告列表
     *
     * @param pager
     * @return
     */
    public List<HomepageManageVo> queryAdvertisementList(Paging<HomepageManageVo> pager) {
        try {
            log.info("查询广告列表");
            return homepageManageMapper.findAllqueryAdvertisementList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询广告列表");
            throw e;
        }
    }

    /**
     * 用于查看广告详情
     *
     * @param id
     * @return
     */
    public HomepageManageVo queryAvertisementById(String id) {
        try {
            log.info("查询广告详情");
            return homepageManageMapper.queryAvertisementById(id);
        } catch (Exception e) {
            log.error("查询广告详情");
            throw e;
        }
    }


    /**
     * 查询广告位置是否可以添加广告
     *
     * @param manage
     * @return
     */
    public int queryIsAdd(HomepageManage manage) {
        try {
            log.info("查询广告位置是否可以添加广告");
            return homepageManageMapper.queryIsAdd(manage);
        } catch (Exception e) {
            log.error("查询广告位置是否可以添加广告异常", e);
            throw e;
        }
    }


    /**
     * 添加广告
     *
     * @param manage
     * @return
     */
    public int addAdvertisement(HomepageManage manage) {
        try {
            log.info("添加广告");
            return homepageManageMapper.addAdvertisement(manage);
        } catch (Exception e) {
            log.error("添加广告");
            throw e;
        }
    }

    /**
     * 编辑广告
     *
     * @param manage
     * @return
     */
    public int updateAdvertisement(HomepageManage manage) {
        try {
            log.info("编辑广告");
            return homepageManageMapper.updateAdvertisement(manage);
        } catch (Exception e) {
            log.error("编辑广告异常");
            throw e;
        }
    }

    /**
     * 根据条件查询广告列表
     *
     * @param map
     * @param pager
     * @return
     */
    public List<HomepageManageVo> queryAdvertisementLike(Map map, Paging<HomepageManageVo> pager) {
        try {
            log.info("根据条件查询广告列表");
            return homepageManageMapper.findAllQueryAdvertisementLike(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("根据条件查询广告列表异常");
            throw e;
        }
    }

    /**
     * 查询此广告位置下已经有几条广告
     *
     * @return
     */
    public List<Integer> queryAdvertisementLocation(String type) {
        try {
            log.info("查询此广告位置下已经有几条广告");
            return homepageManageMapper.queryAdvertisementLocation(type);
        } catch (Exception e) {
            log.error("查询此广告位置下已经有几条广告异常");
            throw e;
        }
    }

    /**
     * 删除广告排序
     *
     * @param map
     * @return
     */
    public int deleteAdvertisementOrderid(Map map) {
        try {
            log.info("删除广告排序");
            return homepageManageMapper.deleteAdvertisementOrderid(map);
        } catch (Exception e) {
            log.error("删除广告排序异常");
            throw e;
        }
    }

    /**
     * 修改广告排序
     *
     * @param map
     * @return
     */
    public int updateAtionAdvertisementOrderid(Map map) {
        try {
            log.info("修改广告排序");
            return homepageManageMapper.updateAtionAdvertisementOrderid(map);
        } catch (Exception e) {
            log.error("修改广告排序异常");
            throw e;
        }
    }

    /**
     * 根据id删除广告
     *
     * @param id
     * @return
     */
    public int deleteAdvertisement(Integer id) {
        try {
            log.info("根据id删除广告");
            return homepageManageMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            log.error("根据id删除广告异常");
            throw e;
        }
    }

    /**
     * 美番2.0邀请好友详情页面--邀请送现金页查询宣传图片和邀请规则图片url
     */
    public String myinvite(int topictype){
        try {
            log.info("邀请好友宣传图片送现金宣传图url获取");
            return homepageManageMapper.myinvite(topictype);
        }catch (Exception e){
            log.error("邀请好友宣传图片送现金宣传图url获取失败", e);
            throw e;
        }
    }

    /**
     * 获取APP开屏图
     * @return
     */
    public String getOpenAppImg(){
        try {
            log.info("获取APP开屏图");
            return homepageManageMapper.getOpenAppImg();
        }catch (Exception e){
            log.error("获取APP开屏图失败");
            throw e;
        }
    }

}

package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.MemberDetails;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface JobMapper {
    //发布职位
    int publishPosition(Job job);

    //查询公司已发布的职位
    List<Job> findAllByPager(RowBounds rowBounds, @Param("id") String id);

    //查询公司发布的某条职位的信息
    Job getPositionByPositionId(@Param("id") String id);

    //删除已发布的职位
    int deletePosition(@Param("id") String id);

    //更新编辑已发布的职位
    int updatePosition(Job job);

    //查询最新招聘职位
    List<Job> searchNewPosition(int count);

    //查询推荐职位
    List<Job> searchRecommendPosition(String id,int count);

    //查询相同职位
    List<Job> searchSamePosition(String id);

    //查询最新发布的职位
    List<Job> searchLatestPublishPosition();

    //查询发布职位企业的信息
    MemberDetails queryCompanyInfo(Long id);

    List<MemberDetails> queryAdvertisingPosition(Map<String,Object> map);

    Job queryPositionInfoByID(Long id);

    List<Job> findAllOtherPosition(RowBounds rowBounds,Map<String,Object> map);

    List<Job> findAllMyApplyPosition(RowBounds rowBounds,@Param("id") String id);

    //热门招聘
    List<Job> queryHotPosition(int count);

    //最新招聘（按分类查询）
    List<Job> queryLatestJob(@Param("id")String id,@Param("count")int count);

    //名企招聘
    List<ResultBean> greatCompanyPosition();

    //更新点击率
    int updateViews(Long jobID);

    //相似企业
    List<Job> querySimilarCompany(@Param("enterpriseType")int enterpriseType,@Param("id")String id,@Param("count")int count);

    //相似企业信息
    Job querySimilarCompanyInfo(@Param("id")String id);
}
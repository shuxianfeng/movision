package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.MemberDetails;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface JobMapper {
    //发布职位
    int publishPosition(Job job);

    //查询公司已发布的职位
    List<Map<String, Object>> findAllByPager(RowBounds rowBounds, @Param("id") String id);

    //查询公司发布的某条职位的信息
    Job getPositionByPositionId(@Param("id") String id);

    //删除已发布的职位
    int deletePosition(@Param("id") String id);

    //更新编辑已发布的职位
    int updatePosition(Job job);

    //查询最新招聘职位
    List<Map<String, Object>> searchNewPosition(int count);

    //查询推荐职位
    List<Job> searchRecommendPosition(@Param("id") String id, @Param("count") int count);

    //查询相同职位
    List<Job> searchSamePosition(Map<String, Object> map);

    //查询最新发布的职位
    List<Job> searchLatestPublishPosition();

    //查询发布职位企业的信息
    MemberDetails queryCompanyInfo(Long id);

    /*List<MemberDetails> queryAdvertisingPosition(Map<String,Object> map);*/

    Map<String, Object> queryPositionInfoByID(Map<String, Object> map);

    List<Map<String, Object>> findAllOtherPosition(RowBounds rowBounds, Map<String, Object> map);

    List<Map<String, Object>> findAllMyApplyPosition(RowBounds rowBounds, @Param("id") String id);

    List<Map<String, Object>> findAllMyApplyPosition(@Param("id") String id);

    //热门招聘
    List<Job> queryHotPosition(Map<String, Object> map);

    //最新招聘（按分类查询）
    List<Map<String, Object>> queryLatestJob(@Param("id") String id, @Param("count") int count);

    //名企招聘
    List<ResultBean> greatCompanyPosition();

    //更新点击率
    int updateViews(Long jobID);

    //相似企业ID
    List<String> querySimilarCompany(@Param("employeeNumber") String employeeNumber, @Param("enterpriseType") int enterpriseType, @Param("id") String id, @Param("count") int count);

    //相似企业信息
    Job querySimilarCompanyInfo(@Param("id") String id);

    //查询名企热门职位
    List<Job> queryEnterpriseHotPosition(Map<String, Object> map);

    //是否收藏该简历

    Map<String, Object> findJobByID(String id);

    List<Map<String, String>> queryPublishJobCity(Map<String, Object> map);

    Map<String, Object> findJobByJobID(@Param("jobID") String jobID);

    List<Map<String, Object>> findNewPositions(@Param("count") int count);

    List<Map<String, String>> findAllJobByCompanyId(RowBounds rowBounds, Map<String, Object> map);

    List<Map<String, Object>> tmpMap(String id);

    //触屏端是否收藏该简历
    Integer findcollectionResumeById(Map<String, Object> map);

    //触屏端的发布职位企业的信息
    MemberDetails queryCompanyById(long id);

    //公司招聘的其他职位
    List<Map<String,Object>> findAllOthersPositionByMemId(Long id);

    Long querycompanyByJobId(Long jobID);

    String queryJobNameByJobId(Long jobID);

    List<Map<String,Object>> findAllOtherPositionById(String id);


    List<Map<String, Object>> findAllPositionForMobile(RowBounds rowBounds, Map<String, Object> map);

    String findIdentifyById(Long createId);
}
package com.movision.mybatis.goodsAssessment.mapper;

import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentCategery;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import com.movision.mybatis.goodsAssessmentImg.entity.GoodsAssessmentImg;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsAssessmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsAssessment record);

    int insertSelective(GoodsAssessment record);

    int insertGoodAssessment(GoodsAssessment goodsAssessment);

    void insertGoodAssessmentImg(List<GoodsAssessmentImg> goodsAssessmentImgList);

    GoodsAssessment selectByPrimaryKey(Integer id);

    List<GoodsAssessmentVo> findAllGoodsAssessment(RowBounds rowBounds, int goodsid);

    List<GoodsAssessmentVo> findAllImgGoodsAssessment(RowBounds rowBounds, int goodsid);

    List<GoodsAssessmentVo> findAllQualityGoodsAssessment(RowBounds rowBounds, int goodsid);

    List<GoodsAssessmentVo> findAllFastGoodsAssessment(RowBounds rowBounds, int goodsid);

    List<GoodsAssessmentVo> findAllAttitudeGoodsAssessment(RowBounds rowBounds, int goodsid);

    List<GoodsAssessmentVo> findAllQualityGeneral(RowBounds rowBounds, int goodsid);

    List<GoodsAssessmentVo> queryAllOfficialReply(int goodsid);

    GoodsAssessmentVo queryPassessment(int pid);

    List<GoodsAssessmentImg> queryGoodsAssessmentImg(int assessmentid);

    GoodsAssessmentCategery queryAssessmentCategorySum(int goodsid);

    int updateByPrimaryKeySelective(GoodsAssessment record);

    int updateByPrimaryKey(GoodsAssessment record);
}
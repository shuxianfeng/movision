package com.movision.mybatis.goodsAssessment.mapper;

import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import com.movision.mybatis.goodsAssessmentImg.entity.GoodsAssessmentImg;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface GoodsAssessmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsAssessment record);

    int insertSelective(GoodsAssessment record);

    GoodsAssessment selectByPrimaryKey(Integer id);

    List<GoodsAssessmentVo> queryGoodsAssessment(RowBounds rowBounds, int goodsid);

    List<GoodsAssessmentVo> queryImgGoodsAssessment(RowBounds rowBounds, int goodsid);

    List<GoodsAssessmentVo> queryQualityGoodsAssessment(RowBounds rowBounds, int goodsid);

    List<GoodsAssessmentVo> queryFastGoodsAssessment(RowBounds rowBounds, int goodsid);

    List<GoodsAssessmentVo> queryAttitudeGoodsAssessment(RowBounds rowBounds, int goodsid);

    List<GoodsAssessmentVo> queryQualityGeneral(RowBounds rowBounds, int goodsid);

    GoodsAssessmentVo queryPassessment(int pid);

    List<GoodsAssessmentImg> queryGoodsAssessmentImg(int assessmentid);

    int updateByPrimaryKeySelective(GoodsAssessment record);

    int updateByPrimaryKey(GoodsAssessment record);
}
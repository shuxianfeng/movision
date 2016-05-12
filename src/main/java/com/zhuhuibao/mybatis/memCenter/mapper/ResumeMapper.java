package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ResumeMapper {

    //发布简历
    int setUpResume(Resume resume);

    //查询我创建的简历
    Resume searchMyResume(@Param("id") String id);

    //查询我创建的简历的全部信息
    Resume searchMyResumeAllInfo(@Param("id") String id);

    //更新简历,刷新简历
    int updateResume(Resume resume);

    //预览简历
    Resume previewResume(@Param("id")String id);

    List<Resume> findAllResume(RowBounds rowBounds, Map<String,Object> map);

    //我收到的简历
    List<Resume> findAllReceiveResume(RowBounds rowBounds,@Param("id")String id);

    //根据id查简历
    Resume searchResumeById(@Param("id")String id);

    List<Resume> queryResumeByCreateId(Long createID);

    //最新求职
    List<Resume> queryLatestResume(int count);

    //是否存在简历
    int isExistResume(Long createID);
}
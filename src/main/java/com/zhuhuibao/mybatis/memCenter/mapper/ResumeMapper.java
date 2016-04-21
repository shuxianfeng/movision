package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import org.apache.ibatis.annotations.Param;

public interface ResumeMapper {

    //发布简历
    int setUpResume(Resume resume);

    //查询我创建的简历
    Resume searchMyResume(@Param("id") String id);

    //更新简历,刷新简历
    int updateResume(Resume resume);

    //预览简历
    Resume previewResume(@Param("id")String id);
}
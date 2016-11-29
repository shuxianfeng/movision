package com.zhuhuibao.mybatis.memCenter.mapper;

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
    Map<String,Object> searchMyResumeAllInfo(@Param("id") String id);

    //更新简历,刷新简历
    int updateResume(Resume resume);

    //预览简历
    Resume previewResume(Map<String,Object> map);

    List<Map<String,Object>> findAllResume(RowBounds rowBounds, Map<String,Object> map);

    List<Map<String,Object>> findAllResume4Mobile(Map<String,Object> map);

    //我收到的简历
    List<Map<String,String>> findAllReceiveResume(RowBounds rowBounds,@Param("id")String id);

    //根据id查简历
    Resume searchResumeById(@Param("id")String id);

    List<Resume> queryResumeByCreateId(Long createID);

    //最新求职
    List<Map<String,Object>> queryLatestResume(Map<String,Object> map);

    //是否存在简历
    int isExistResume(Long createID);

    List<Map<String,Object>> queryJobCount(Long createId);

    //我下载的简历
    List<Map<String,String>> findAllDownloadResume(RowBounds rowBounds,@Param("id")String id);

    //预览简历
    Resume previewResumeNew(@Param("id")String id);
    
    //简历是否被收藏或下载
	Map isDownOrColl(Map<String, Object> con);
	
    //记录下载记录
	void insertDownRecord(Map<String, String> recordMap);
	
    //删除收藏夹简历
	void delCollRecord(Map<String, String> recordMap);
    //添加收藏
	int insertCollRecord(Map<String, Object> con);
    //我收藏的简历
    List<Map<String,String>> findAllCollectResume(RowBounds rowBounds,@Param("id")String id);
    //记录预览
	void insertViewGoods(Map<String, String> recordMap);

    List<String> selectIdsByCreateId(@Param("createid") Long createid);
    //获取简历收藏最大值
	int getMaxCollCount(Long memberId);

    //更新简历,刷新简历
    int updateResumeIsPublic(Resume resume);

    //预览简历
    Map<String,Object> previewMyResume(Map<String,Object> map);

    Long queryResumeIdById(Long createID);
}
package com.movision.mybatis.systemLayout.service;

import com.movision.mybatis.systemLayout.entity.SystemLayout;
import com.movision.mybatis.systemLayout.mapper.SystemLayoutMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/10/9 13:50
 */
@Service
public class SystemLayoutService {

    private static Logger log = LoggerFactory.getLogger(SystemLayoutService.class);

    @Autowired
    private SystemLayoutMapper systemLayoutMapper;

    /**
     * 获取系统配置参数方法
     *
     * @param key
     * @return
     */
    public String getValue(String key){
        try {
            log.info("获取系统配置参数");
            return systemLayoutMapper.getValue(key);
        }catch (Exception e){
            log.error("获取系统参数失败", e);
            throw e;
        }
    }

    /**
     * 查询机器人分隔数量
     *
     * @param separate
     * @return
     */
    public Integer queryRobotSeparate(String separate) {
        try {
            log.info("查询机器人分隔数量");
            return systemLayoutMapper.queryRobotSeparate(separate);
        } catch (Exception e) {
            log.error("查询机器人分隔数量异常", e);
            throw e;
        }
    }

    /**
     * 查询评论占比率
     *
     * @param percentage
     * @return
     */
    public Double queryRobotPercentage(String percentage) {
        try {
            log.info("查询评论占比率");
            return systemLayoutMapper.queryRobotpercentage(percentage);
        } catch (Exception e) {
            log.error("查询评论占比率异常", e);
            throw e;
        }
    }

    /**
     * 查询资源服务器url
     *
     * @param str
     * @return
     */
    public String queryServiceUrl(String str) {
        try {
            log.info("查询资源服务器url");
            return systemLayoutMapper.queryServiceUrl(str);
        } catch (Exception e) {
            log.error("查询资源服务器url异常", e);
            throw e;
        }
    }


    /**
     * 查询资源服务器iphonex
     *
     * @param str
     * @return
     */
    public String queryIphonexUrl(String str) {
        try {
            log.info("查询资源服务器iphonex");
            return systemLayoutMapper.queryIphonexUrl(str);
        } catch (Exception e) {
            log.error("查询资源服务器iphonex异常", e);
            throw e;
        }
    }

    /**
     * 查询图片压缩比例
     *
     * @param ratio
     * @return
     */
    public Double queryFileRatio(String ratio) {
        try {
            log.info("查询图片压缩比例");
            return systemLayoutMapper.queryFileRatio(ratio);
        } catch (Exception e) {
            log.error("查询图片压缩比例异常", e);
            throw e;
        }
    }

    /**
     * 查询文件头部名称
     *
     * @param img
     * @return
     */
    public String queryImgBucket(String img) {
        try {
            log.info("查询文件头部名称");
            return systemLayoutMapper.queryImgBucket(img);
        } catch (Exception e) {
            log.error("查询文件头部名称异常", e);
            throw e;
        }
    }

    /**
     * 新增系统配置
     *
     * @param systemLayout
     */
    public void insertSystemLayout(SystemLayout systemLayout) {
        try {
            log.info("新增系统配置");
            systemLayoutMapper.insertSelective(systemLayout);
        } catch (Exception e) {
            log.error("新增系统配置异常", e);
            throw e;
        }
    }

    /**
     * 查询所有系统配置
     *
     * @param type
     * @param pag
     * @return
     */
    public List<SystemLayout> querySystemLayoutAll(String type, Paging<SystemLayout> pag) {
        try {
            log.info("查询所有系统配置");
            return systemLayoutMapper.findAllQuerySystemLayotAll(type, pag.getRowBounds());
        } catch (Exception e) {
            log.error("查询所有系统配置异常", e);
            throw e;
        }
    }

    /**
     * 根据id查询系统配置详情
     *
     * @param id
     * @return
     */
    public SystemLayout querySystemLayoutById(Integer id) {
        try {
            log.info("根据id查询系统配置详情");
            return systemLayoutMapper.querySystemLayoutById(id);
        } catch (Exception e) {
            log.error("根据id查询系统配置详情异常", e);
            throw e;
        }
    }

    /**
     * 修改系统配置
     *
     * @param systemLayout
     */
    public void updateSystemLayoutById(SystemLayout systemLayout) {
        try {
            log.info("修改系统配置");
            systemLayoutMapper.updateByPrimaryKeySelective(systemLayout);
        } catch (Exception e) {
            log.error("修改系统配置异常", e);
            throw e;
        }
    }

    /**
     * 根据id删除系统配置
     *
     * @param id
     */
    public void deleteSystemLayoutById(Integer id) {
        try {
            log.info("根据id删除系统配置");
            systemLayoutMapper.deleteByPrimaryKey(id);
        } catch (NumberFormatException e) {
            log.error("根据id删除系统配置异常", e);
            throw e;
        }
    }

    /**
     * 查询热度值
     *
     * @param value
     * @return
     */
    public int queryHaetValue(String value) {
        try {
            log.info("查询热度值");
            return systemLayoutMapper.queryHaetValue(value);
        } catch (Exception e) {
            log.error("查询热度值异常", e);
            throw e;
        }
    }

    /**
     * 对单个用户发送系统通知时查询通知模板
     */
    public String querySysNoticeTemp(Map<String, Object> parammap) {
        try {
            log.info("对单个用户发送系统通知时查询通知模板");
            return systemLayoutMapper.querySysNoticeTemp(parammap);
        }catch (Exception e){
            log.error("对单个用户发送系统通知时查询通知模板失败", e);
            throw e;
        }
    }

    public List<SystemLayout> querySmsList(){
        try {
            log.info("查询Sms模板所有");
            return systemLayoutMapper.querySmsList();
        }catch (Exception e){
            log.error("查询Sms模板所有失败", e);
            throw e;
        }
    }


    public String queryTemplet(int id){
        try {
            log.info("根据id查询模板");
            return systemLayoutMapper.queryTemplet(id);
        }catch (Exception e){
            log.error("根据id查询模板失败", e);
            throw e;
        }
    }

}

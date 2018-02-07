package com.movision.mybatis.photo.service;

import com.movision.mybatis.photo.entity.Photo;
import com.movision.mybatis.photo.entity.PhotoVo;
import com.movision.mybatis.photo.mapper.PhotoMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

 import java.util.List;
 import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2018/2/1 10:43
 */
@Service
@Transactional
public class PhotoService {
    private static Logger log = LoggerFactory.getLogger(PhotoService.class);

    @Autowired
    private PhotoMapper photoMapper;

    /**
     * 插入约拍表
     * @param photo
     * @return
     */
    public int insertSelective(Photo photo){
        try {
            log.info("插入约拍表");
            return photoMapper.insertSelective(photo);
        }catch (Exception e){
            log.error("插入约拍表失败",e);
            throw e;
        }
    }

    /**
     * 查询约拍详情
     * @param id
     * @return
     */
    public PhotoVo queryPhotoDetails(Integer id){
        try {
            log.info("查询约拍详情");
            return photoMapper.queryPhotoDetails(id);
        }catch (Exception e){
            log.error("查询约拍详情失败",e);
            throw e;
        }
    }

    /**查询约拍列表
     *
     * @param map
     * @param photoVoPaging
     * @return
     */
    public List<PhotoVo> findAllPhoto(Map map,Paging<PhotoVo> photoVoPaging){
        try {
            log.info("查询约拍列表");
            return photoMapper.findAllPhoto(map,photoVoPaging.getRowBounds());
        }catch (Exception e){
            log.error("查询约拍列表失败",e);
            throw e;
        }
    }

    /**
     * 查询成交价格
     * @param id
     * @return
     */
    public  Photo selectCreditScore(int id){
        try {
            log.info("查询成交价格");
            return photoMapper.selectCreditScore(id);
        }catch (Exception e){
            log.error("查询成交价格失败",e);
            throw e;
        }
    }


    /**
     * 修改用户信用分
     * @param
     * @return
     */
    public  int updateCreditScore(Map map){
        try {
            log.info("修改用户信用分");
            return photoMapper.updateCreditScore(map);
        }catch (Exception e){
            log.error("修改用户信用分失败",e);
            throw e;
        }
    }

    /**
     * 前台修改价格
     * @param map
     * @return
     */
    public  int updatePhotoMoney(Map map){
        try {
            log.info("前台修改价格");
            return photoMapper.updatePhotoMoney(map);
        }catch (Exception e){
            log.error("前台修改价格失败",e);
            throw e;
        }
    }


    /**
     * 修改状态
     * @param
     * @return
     */
    public  int updatePhotoStatus(Map map){
        try {
            log.info("修改状态");
            return photoMapper.updatePhotoStatus(map);
        }catch (Exception e){
            log.error("修改状态失败",e);
            throw e;
        }
    }

}

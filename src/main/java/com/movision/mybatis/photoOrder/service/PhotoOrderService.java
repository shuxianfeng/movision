package com.movision.mybatis.photoOrder.service;

 import com.movision.mybatis.photoOrder.entity.PhotoOrder;
 import com.movision.mybatis.photoOrder.entity.PhotoOrderVo;
 import com.movision.mybatis.photoOrder.mapper.PhotoOrderMapper;
 import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

 import java.util.List;
 import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2018/2/1 15:43
 */
@Service
@Transactional
public class PhotoOrderService {
    private static Logger log = LoggerFactory.getLogger(PhotoOrderService.class);

    @Autowired
    private PhotoOrderMapper photoOrderMapper;

    /**查询竞拍人
     *
     * @param id
     * @return
     */
    public List<PhotoOrderVo> queryAllPhotoOrder(int id){
        try {
            log.info("查询竞拍人");
            return photoOrderMapper.queryAllPhotoOrder(id);
        }catch (Exception e){
            log.error("查询竞拍人失败",e);
            throw e;
        }
    }

    /**
     * 添加竞拍者
     * @param photoOrder
     * @return
     */
    public int insertSelective(PhotoOrder photoOrder){
        try {
            log.info("添加竞拍者");
            return photoOrderMapper.insertSelective(photoOrder);
        }catch (Exception e){
            log.error("添加竞拍者失败",e);
            throw e;
        }
    }

    /**
     * 查询接单人id
     * @param
     * @return
     */
    public int selectOrderUserid(int id){
        try {
            log.info("查询接单人id");
            return photoOrderMapper.selectOrderUserid(id);
        }catch (Exception e){
            log.error("查询接单人id失败",e);
            throw e;
        }
    }


    /**
     * 选中接单人
     * @param
     * @return
     */
    public int updateOrder(Map map){
        try {
            log.info("选中接单人");
            return photoOrderMapper.updateOrder(map);
        }catch (Exception e){
            log.error("选中接单人失败",e);
            throw e;
        }
    }

    /**
     * 查询订单
     * @param id
     * @return
     */
    public PhotoOrder selectOrder(int id){
        try {
            log.info("查询订单");
            return photoOrderMapper.selectOrder(id);
        }catch (Exception e){
            log.error("查询订单失败",e);
            throw e;
        }
    }

}

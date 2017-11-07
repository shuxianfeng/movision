package com.movision.mybatis.imSystemInform.service;

import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import com.movision.mybatis.imSystemInform.entity.ImSystemInformVo;
import com.movision.mybatis.imSystemInform.mapper.ImSystemInformMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/9 20:16
 */
@Service
@Transactional
public class ImSystemInformService {
    private static Logger log = LoggerFactory.getLogger(ImSystemInformService.class);

    @Autowired
    private ImSystemInformMapper imSystemInformMapper;

    public int add(ImSystemInform imSystemInform) {
        try {
            log.info("添加系统消息");
            return imSystemInformMapper.insertSelective(imSystemInform);
        } catch (Exception e) {
            log.error("添加系统消息失败", e);
            throw e;
        }
    }

    public List<ImSystemInformVo> queryAll(Paging<ImSystemInformVo> paging) {
        try {
            log.info("查询所有的系统通知");
            return imSystemInformMapper.findAll(paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询所有的系统通知失败", e);
            throw e;
        }
    }

    public List<ImSystemInformVo> findAllIm(Map map, Paging<ImSystemInformVo> paging) {
        try {
            log.info("查询用户收到的系统通知");
            return imSystemInformMapper.findAllIm(map, paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询用户收到的系统通知失败", e);
            throw e;
        }
    }

    public ImSystemInformVo queryMyMsgInforDetails(ImSystemInform imSystemInform) {
        try {
            log.info("查询通知详情接口");
            return imSystemInformMapper.queryMyMsgInforDetails(imSystemInform);
        } catch (Exception e) {
            log.error("查询通知详情接口异常", e);
            throw e;
        }
    }

    public ImSystemInform queryDetail(Integer id) {
        try {
            log.info("查询系统通知详情");
            return imSystemInformMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("查询系统通知详情失败", e);
            throw e;
        }
    }

    /**
     * 删除系统通知
     *
     * @param id
     * @return
     */
    public Integer deleteImSystem(Integer id) {
        try {
            log.info("删除系统通知");
            return imSystemInformMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            log.error("删除系统通知失败", e);
            throw e;
        }

    }

    /**
     * 条件搜索
     *
     * @param map
     * @param pager
     * @return
     */
    public List<ImSystemInform> findAllSystemForm(Map map, Paging<ImSystemInform> pager) {
        try {
            log.info("条件搜索");
            return imSystemInformMapper.findAllSystemInform(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件搜索失败");
            throw e;
        }
    }

    /**
     * 查询内容全部
     *
     * @param id
     * @return
     */
    public ImSystemInform queryBodyAll(Integer id) {
        try {
            log.info("查询内容全部");
            return imSystemInformMapper.queryBodyAll(id);
        } catch (Exception e) {
            log.error("查询内容全部失败", e);
            throw e;
        }
    }

    /**
     * 查询运营通知列表
     *
     * @param inform
     * @param pag
     * @return
     */
    public List<ImSystemInform> queryOperationInformList(ImSystemInform inform, Paging<ImSystemInform> pag) {
        try {
            log.info("查询运营通知列表");
            return imSystemInformMapper.findAllOperationInformList(inform, pag.getRowBounds());
        } catch (Exception e) {
            log.error("查询运用用纸列表异常", e);
            throw e;
        }
    }

    /**
     * 查询运营通知详情
     *
     * @param imSystemInform
     * @return
     */
    public ImSystemInform queryOperationInformById(ImSystemInform imSystemInform) {
        try {
            log.info("查询运营通知详情");
            return imSystemInformMapper.queryOperationInformById(imSystemInform);
        } catch (Exception e) {
            log.error("查询运营通知详情异常");
            throw e;
        }
    }

    /**
     * 更新运营通知
     *
     * @param inform
     */
    public void updateOperationInformById(ImSystemInform inform) {
        try {
            log.info("更新运营通知");
            imSystemInformMapper.updateByPrimaryKeySelective(inform);
        } catch (Exception e) {
            log.error("更新运营通知异常", e);
            throw e;
        }
    }

    /**
     * 查询活动列表
     *
     * @param
     * @return
     */
    public List<ImSystemInform> findAllActiveMessage(Map map, Paging<ImSystemInform> paging) {
        try {
            log.info("查询活动列表");
            return imSystemInformMapper.findAllActiveMessage(map, paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询活动列表失败", e);
            throw e;
        }
    }

    /**
     * 修改活动通知
     *
     * @param imSystemInform
     * @return
     */
    public int updateActiveMessage(ImSystemInform imSystemInform) {
        try {
            log.info("修改活动通知");
            return imSystemInformMapper.updateActiveMessage(imSystemInform);
        } catch (Exception e) {
            log.error("修改活动通知失败", e);
            throw e;
        }
    }

    /**
     * 活动通知回显
     *
     * @param id
     * @return
     */
    public ImSystemInform queryActiveById(int id) {
        try {
            log.info("活动通知回显");
            return imSystemInformMapper.queryActiveById(id);
        } catch (Exception e) {
            log.error("活动通知回显失败", e);
            throw e;
        }
    }


    public String queryActiveBody(int id) {
        try {
            log.info("查询活动内容");
            return imSystemInformMapper.queryActiveBody(id);
        } catch (Exception e) {
            log.error("查询活动内容失败", e);
            throw e;
        }
    }


    public Date queryDate(int userid) {
        try {
            log.info("查询时间");
            return imSystemInformMapper.queryDate(userid);
        } catch (Exception e) {
            log.info("查询时间失败", e);
            throw e;
        }
    }

    public String queryUserAccid(int userid) {
        try {
            log.info("查询用户的accid");
            return imSystemInformMapper.queryUserAccid(userid);
        } catch (Exception e) {
            log.error("查询用户的accid失败");
            throw e;
        }
    }
}

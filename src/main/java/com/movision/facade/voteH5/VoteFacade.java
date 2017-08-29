package com.movision.facade.voteH5;

import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.activeH5.entity.ActiveH5;
import com.movision.mybatis.activeH5.service.ActiveH5Service;
import com.movision.mybatis.complainsrecords.entity.Complainsrecords;
import com.movision.mybatis.complainsrecords.service.ComplainsrecordsService;
import com.movision.mybatis.take.entity.Take;
import com.movision.mybatis.take.entity.TakeVo;
import com.movision.mybatis.take.service.TakeService;
import com.movision.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/8/29 15:01
 */
@Service
public class VoteFacade {
    @Autowired
    private ActiveH5Service activeH5Service;
    @Autowired
    private ComplainsrecordsService complainsrecordsService;
    @Autowired
    private TakeService takeService;

    /**
     * 插入活动
     *
     * @param name
     * @param photo
     * @param begintime
     * @param endtime
     * @param company
     * @param activitydescription
     * @param
     * @return
     */
    public int insertSelective(String name, String photo, String begintime, String endtime, String company, String activitydescription) {
        ActiveH5 activeH5 = new ActiveH5();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtil.isNotEmpty(name)) {
            activeH5.setName(name);
        }
        if (StringUtil.isNotEmpty(photo)) {
            activeH5.setPhoto(photo);
        }
        Date begin = null;//开始时间
        if (StringUtil.isNotEmpty(begintime)) {
            try {
                begin = format.parse(begintime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Date end = null;
        if (StringUtil.isNotEmpty(endtime)) {
            try {
                end = format.parse(endtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        activeH5.setBegintime(begin);
        activeH5.setEndtime(end);
        if (StringUtil.isNotEmpty(company)) {
            activeH5.setCompany(company);
        }
        if (StringUtil.isNotEmpty(activitydescription)) {
            activeH5.setActivitydescription(activitydescription);
        }
        activeH5.setIsdel(0);
        activeH5.setIntime(new Date());
        int result = activeH5Service.insertSelective(activeH5);
        return result;
    }

    /**
     * 删除活动
     *
     * @param id
     * @return
     */
    public int deleteActive(int id) {
        return activeH5Service.deleteActive(id);
    }

    /**
     * 查询所有活动
     *
     * @param paging
     * @return
     */
    public List<ActiveH5> findAllActive(Paging<ActiveH5> paging) {
        return activeH5Service.findAllActive(paging);
    }

    /**
     * 插入投诉记录
     *
     * @param
     * @return
     */
    public int insertSelectiveCom(String message, String name, String tsid) {
        Complainsrecords complainsrecords = new Complainsrecords();
        if (StringUtil.isNotEmpty(message)) {
            complainsrecords.setMessage(message);
        }
        if (StringUtil.isNotEmpty(name)) {
            complainsrecords.setName(name);
        }
        if (StringUtil.isNotEmpty(tsid)) {
            complainsrecords.setTsid(Integer.parseInt(tsid));
        }
        complainsrecords.setIntime(new Date());
        int result = complainsrecordsService.insertSelectiveCom(complainsrecords);
        return result;
    }


    /**
     * 添加参赛人员
     *
     * @param
     * @param name
     * @param
     * @return
     */
    public int insertSelectiveTP(String activeid, String name) {
        Take take = new Take();
        if (StringUtil.isNotEmpty(activeid)) {
            take.setActiveid(Integer.parseInt(activeid));
        }
        if (StringUtil.isNotEmpty(name)) {
            take.setName(name);
        }
        take.setIsdel(0);
        take.setIntime(new Date());
        int result = takeService.insertSelectiveTP(take);
        return result;
    }


    /**
     * 删除参赛人员
     *
     * @param id
     * @return
     */
    public int deleteTakePeople(int id) {
        return takeService.deleteTakePeople(id);
    }

    /**
     * 查询所有参赛人员
     *
     * @param paging
     * @return
     */
    public List<TakeVo> findAllTake(Paging<TakeVo> paging) {
        return takeService.findAllTake(paging);
    }

}

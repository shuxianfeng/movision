package com.movision.facade.voteH5;

import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.activeH5.entity.ActiveH5;
import com.movision.mybatis.activeH5.entity.ActiveH5Vo;
import com.movision.mybatis.activeH5.service.ActiveH5Service;
import com.movision.mybatis.take.entity.Take;
import com.movision.mybatis.take.entity.TakeVo;
import com.movision.mybatis.take.service.TakeService;
import com.movision.mybatis.votingrecords.entity.Votingrecords;
import com.movision.mybatis.votingrecords.service.VotingrecordsService;
import com.movision.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/8/29 15:01
 */
@Service
public class VoteFacade {
    @Autowired
    private ActiveH5Service activeH5Service;
    @Autowired
    private TakeService takeService;
    @Autowired
    private VotingrecordsService votingrecordsService;
    /**
     * 插入活动
     *
     * @param name
     * @param photo
     * @param begintime
     * @param endtime
     * @param
     * @param activitydescription
     * @param
     * @return
     */
    public int insertSelective(String name, String photo, String begintime, String endtime, String activitydescription) {
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
        if (StringUtil.isNotEmpty(activitydescription)) {
            activeH5.setActivitydescription(activitydescription);
        }
        activeH5.setIsdel(0);
        activeH5.setIntime(new Date());
        int result = activeH5Service.insertSelective(activeH5);
        return result;
    }

    /**
     * 根据id查询活动详情
     * @param id
     * @return
     */
    public ActiveH5 queryActivityById(Integer id) {
        return activeH5Service.queryActivityById(id);
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
     * 更新活动
     * @param id
     * @param name
     * @param photo
     * @param explain
     * @param bigintime
     * @param endtime
     */
    public void updateActivity(Integer id, String name, String photo, String explain, String bigintime, String endtime) {
        ActiveH5 activeH5 = new ActiveH5();
        activeH5.setId(id);
        if (StringUtil.isNotEmpty(name)) {
            activeH5.setName(name);
        }
        if (StringUtil.isNotEmpty(photo)) {
            activeH5.setPhoto(photo);
        }
        if (StringUtil.isNotEmpty(explain)) {
            activeH5.setActivitydescription(explain);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date big = null;
        if (StringUtil.isNotEmpty(bigintime)) {
            try {
                big = format.parse(bigintime);
                activeH5.setBegintime(big);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Date end = null;
        if (StringUtil.isNotEmpty(endtime)) {
            try {
                end = format.parse(endtime);
                activeH5.setEndtime(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        activeH5Service.updateActivity(activeH5);
    }

    /**
     * 查询所有活动
     *
     * @param paging
     * @return
     */
    public List<ActiveH5> findAllActive(String name, String bigintime, String endtime, Paging<ActiveH5> paging) {
        ActiveH5 activeH5 = new ActiveH5();
        if (StringUtil.isNotEmpty(name)) {
            activeH5.setName(name);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beg = null;
        if (StringUtil.isNotEmpty(bigintime)) {
            try {
                beg = format.parse(bigintime);
                activeH5.setBegintime(beg);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Date end = null;
        if (StringUtil.isNotEmpty(endtime)) {
            try {
                end = format.parse(endtime);
                activeH5.setEndtime(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return activeH5Service.findAllActive(activeH5, paging);
    }



    /**
     * 添加参赛人员
     *
     * @param
     * @param name
     * @param
     * @return
     */
    public int insertSelectiveTP(String activeid, String name, String phone, String photo, String describe, String nickname, String banner) {
        Take take = new Take();
        if (StringUtil.isNotEmpty(activeid)) {
            take.setActiveid(Integer.parseInt(activeid));
        }
        if (StringUtil.isNotEmpty(name)) {
            take.setName(name);
        }
        take.setIsdel(0);
        take.setIntime(new Date());
        if (StringUtil.isNotEmpty(phone)) {
            take.setPhone(phone);
        }
        if (StringUtil.isNotEmpty(photo)) {
            take.setPhoto(photo);
        }
        if (StringUtil.isNotEmpty(describe)) {
            take.setDescribe(describe);
        }
        if (StringUtil.isNotEmpty(nickname)) {
            take.setNickname(nickname);
        }
        int result = takeService.insertSelectiveTP(take);
        return result;
    }

    /**
     * 编辑投稿
     *
     * @param id
     * @param activeid
     * @param name
     * @param phone
     * @param photo
     * @param describe
     * @param nickname
     * @param banner
     * @param audit
     * @param mark
     */
    public void updateTakeById(String id, String activeid, String name, String phone, String photo, String describe, String nickname, String banner, String audit, String mark) {
        Take take = new Take();
        take.setId(Integer.parseInt(id));
        if (StringUtil.isNotEmpty(activeid)) {
            take.setActiveid(Integer.parseInt(activeid));
        }
        if (StringUtil.isNotEmpty(name)) {
            take.setName(name);
        }
        if (StringUtil.isNotEmpty(phone)) {
            take.setPhoto(photo);
        }
        if (StringUtil.isNotEmpty(photo)) {
            take.setPhoto(photo);
        }
        if (StringUtil.isNotEmpty(describe)) {
            take.setDescribe(describe);
        }
        if (StringUtil.isNotEmpty(nickname)) {
            take.setNickname(nickname);
        }
        if (StringUtil.isNotEmpty(banner)) {
            take.setBanner(banner);
        }
        if (StringUtil.isNotEmpty(audit)) {
            take.setAudit(Integer.parseInt(audit));
        }
        if (StringUtil.isNotEmpty(mark)) {
            take.setMark(Integer.parseInt(mark));
        }
        takeService.updateTakeById(take);
    }


    /**
     * 投稿审核
     *
     * @param id
     * @param number
     */
    public void updateTakeByAudit(String activityid, String id, String number) {
        Take take = new Take();
        take.setId(Integer.parseInt(id));
        take.setAudit(1);
        take.setActiveid(Integer.parseInt(activityid));
        if (StringUtil.isNotEmpty(number)) {
            take.setMark(Integer.parseInt(number));
        }
        //把当前序号之后的投稿序号+1
        takeService.updateTakeByNumber(take);
        takeService.updateTakeByAudit(take);
    }


    /**
     * 查询投稿详情
     * @param id
     * @return
     */
    public Take queryTakeById(Integer id) {
        return takeService.queryTakeById(id);
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
    public List<TakeVo> findAllTake(Paging<TakeVo> paging, String name, String audit) {
        Take take = new Take();
        if (StringUtil.isNotEmpty(name)) {
            take.setName(name);
        }
        if (StringUtil.isNotEmpty(audit)) {
            take.setAudit(Integer.parseInt(audit));
        }
        return takeService.findAllTake(paging, take);
    }


    /**
     * 根据编号或名字查询
     *
     * @param paging
     * @param mark
     * @param nickname
     * @return
     */
    public List<TakeVo> findAllTakeCondition(Paging<TakeVo> paging, String mark, String nickname) {
        Map map = new HashMap();
        if (StringUtil.isNotEmpty(mark)) {
            map.put("mark", mark);
        }
        if (StringUtil.isNotEmpty(nickname)) {
            map.put("nickname", nickname);
        }
        return takeService.findAllTakeCondition(paging, map);
    }


    /**
     * 投票排行
     * @param paging
     * @return
     */
    public List<TakeVo> voteDesc(Paging<TakeVo> paging) {

        return takeService.voteDesc(paging);
    }

    /**
     * 修改访问量
     *
     * @param activeid
     * @return
     */
    public int updatePageView(int activeid) {
        return activeH5Service.updatePageView(activeid);
    }

    /**
     * 首页数据
     *
     * @param activeid
     * @return
     */
    public ActiveH5Vo querySum(int activeid) {
        return activeH5Service.querySum(activeid);
    }

    /**
     * 查询活动说明
     *
     * @param activeid
     * @return
     */
    public ActiveH5 queryH5Describe(int activeid) {
        return activeH5Service.queryH5Describe(activeid);
    }

    /**
     * 时间到期
     *
     * @param activeid
     * @return
     */
    public int isTake(int activeid) {
        //查询活动的时间
        ActiveH5 activeH5 = activeH5Service.queryActivityById(activeid);
        Date beg = activeH5.getBegintime();
        Date end = activeH5.getEndtime();
        long begintime = beg.getTime();
        long endtime = end.getTime();
        Date date = new Date();
        long str = date.getTime();
        if (str < begintime || str > endtime) {
            return 0;//灰色
        } else if (str > begintime && str < endtime) {
            return 1;//正常
        }
        return -1;
    }

    /**
     * 投票记录
     *
     * @param
     * @return
     */
    public int insertSelectiveV(String activeid, String name, String takeid, String takenumber) {
        Votingrecords votingrecords = new Votingrecords();
        //在投票记录里面有没有此用户
        int count = votingrecordsService.queryHave(name);
        int result = 0;
        if (count == 0) {
            votingrecords.setIntime(new Date());
            if (StringUtil.isNotEmpty(name)) {
                votingrecords.setName(name);
            }
            if (StringUtil.isNotEmpty(activeid)) {
                votingrecords.setActiveid(Integer.parseInt(activeid));
            }
            if (StringUtil.isNotEmpty(takeid)) {
                votingrecords.setTakeid(Integer.parseInt(takeid));
            }
            if (StringUtil.isNotEmpty(takenumber)) {
                votingrecords.setTakenumber(Integer.parseInt(takenumber));
            }
            result = votingrecordsService.insertSelective(votingrecords);
        } else {
            result = -1;
        }
        return result;
    }
}

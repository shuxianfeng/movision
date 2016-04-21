package com.zhuhuibao.mybatis.oms.service;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.OmsMemBean;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cxx on 2016/4/21 0021.
 */
@Service
@Transactional
public class OmsMemService {
    private static final Logger log = LoggerFactory.getLogger(OmsMemService.class);

    @Autowired
    private MemberMapper memberMapper;

    public JsonResult getAllMemInfo(Paging<OmsMemBean> pager, OmsMemBean member){
        JsonResult jsonResult = new JsonResult();
        List<OmsMemBean> memberList = memberMapper.findAllMemberByPager(pager.getRowBounds(),member);
        pager.result(memberList);
        jsonResult.setCode(200);
        jsonResult.setData(pager);
        return jsonResult;
    }
}

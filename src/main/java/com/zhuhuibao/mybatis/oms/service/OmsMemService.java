package com.zhuhuibao.mybatis.oms.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.pojo.OmsMemBean;
import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;
import com.zhuhuibao.mybatis.memCenter.mapper.CertificateRecordMapper;
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

    @Autowired
    private CertificateRecordMapper certificateRecordMapper;

    public Response getAllMemInfo(Paging<OmsMemBean> pager, OmsMemBean member){
        Response response = new Response();
        List<OmsMemBean> memberList = memberMapper.findAllMemberByPager(pager.getRowBounds(),member);
        pager.result(memberList);
        response.setCode(200);
        response.setData(pager);
        return response;
    }

    public Response getAllMemCertificate(Paging<OmsMemBean> pager, OmsMemBean member){
        Response response = new Response();
        List<OmsMemBean> memCertificateList = memberMapper.findAllMemCertificateByPager(pager.getRowBounds(),member);
        pager.result(memCertificateList);
        response.setCode(200);
        response.setData(pager);
        return response;
    }

    public CertificateRecord queryCertificateById(String id)
    {
        try{
            return certificateRecordMapper.queryCertificateById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

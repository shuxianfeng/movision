package com.zhuhuibao.mybatis.oms.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.pojo.OmsMemBean;
import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;
import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.mybatis.memCenter.mapper.CertificateRecordMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MemInfoCheckMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    MemInfoCheckMapper infoCheckMapper;

    @Autowired
    private CertificateRecordMapper certificateRecordMapper;

    public List<Map<String,Object>>  getAllMemInfo(Paging<Map<String,Object>> pager, OmsMemBean member){
        List<Map<String,Object>> memberList = memberMapper.findAllMemberByPager(pager.getRowBounds(),member);
        for(Map<String,Object> map : memberList){
            if(member.getType().equals("1") || StringUtils.isEmpty(member.getType())){      //资料审核
                map.put("status",map.get("infoStatus")) ;
            }else if(member.getType().equals("2")){     //实名认证
                map.put("status",map.get("realStatus")) ;
            }
        }
        return memberList;
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

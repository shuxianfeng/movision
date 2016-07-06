package com.zhuhuibao.mybatis.memCenter.service;

import com.mysql.jdbc.StringUtils;
import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.vip.entity.VipMemberPrivilege;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员中心首页处理类
 *
 * @author pl
 * @version 2016/6/29 0029
 */
@Service
@Transactional
public class IndexService {

    private static final Logger log = LoggerFactory.getLogger(IndexService.class);

    @Autowired
    ZhbService zhbService;

    @Autowired
    VipInfoService vipInfoService;

    @Autowired
    MemberService memberService;

    public Map<String,Object> getZhbInfo(Long createId)
    {
        Map<String,Object> resultMap = new HashMap<String,Object>();
    public Map<String, Object> getZhbInfo(Long createId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        ZhbAccount zhbCount = zhbService.getZhbAccount(createId);
        if (zhbCount != null) {
            BigDecimal zhbAmount = zhbCount.getAmount();
            resultMap.put("zhb", zhbAmount);
        } else {
            resultMap.put("zhb", 0);
        }
        List<VipMemberPrivilege> vipList = vipInfoService.listVipMemberPrivilege(createId);
        List<Map<String, String>> serviceList = new ArrayList<>();

        for (VipMemberPrivilege privilege : vipList) {
            Map<String, String> map = new HashMap<>();

            String pinyin = privilege.getPinyin();
            map.put("pinyin", StringUtils.isEmpty(pinyin) ? "" : pinyin);

            String name = privilege.getName();
            map.put("name", StringUtils.isEmpty(name) ? "" : name);

            String number = String.valueOf(privilege.getValue());
            map.put("number", StringUtils.isEmpty(number) ? "" : number);

            serviceList.add(map);
        }
        resultMap.put("service", serviceList);
        return resultMap;
    }

    public Map<String,Object> getMemInfo(String id){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            Member member = memberService.findMemById(id);

            if(member.getMobile()!=null || !StringUtils.isNullOrEmpty(member.getMobile())){
                resultMap.put("isBindMobile",true);
            }else {
                resultMap.put("isBindMobile",false);
            }

            if(member.getEmail()!=null || !StringUtils.isNullOrEmpty(member.getEmail())){
                resultMap.put("isBindEmail",true);
            }else{
                resultMap.put("isBindEmail",true);
            }

            CertificateRecord record = new CertificateRecord();
            record.setMem_id(id);
            record.setIs_deleted(0);
            record.setStatus("1");
            List<CertificateRecord> list = memberService.certificateSearch(record);

            resultMap.put("certificateCount",list.size());


        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return resultMap;
    }
}

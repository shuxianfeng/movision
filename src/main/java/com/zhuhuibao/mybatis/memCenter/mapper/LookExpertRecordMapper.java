package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.LookExpertRecord;

public interface LookExpertRecordMapper {
    int addRecord(LookExpertRecord record);

    LookExpertRecord selectRecordByExpertIdCompanyId(LookExpertRecord record);

}
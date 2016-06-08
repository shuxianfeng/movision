package com.zhuhuibao.mybatis.expert.mapper;

import com.zhuhuibao.mybatis.expert.entity.LookExpertRecord;

public interface LookExpertRecordMapper {
    int addRecord(LookExpertRecord record);

    LookExpertRecord selectRecordByExpertIdCompanyId(LookExpertRecord record);

}
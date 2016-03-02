package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;

public interface CertificateRecordMapper {
    //资质保存
    int saveCertificate(CertificateRecord record);

    //资质更新
    int updateCertificate(CertificateRecord record);

    //资质删除
    int deleteCertificate(int id);
}
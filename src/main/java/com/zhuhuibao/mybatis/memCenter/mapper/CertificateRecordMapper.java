package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;

import java.util.List;

public interface CertificateRecordMapper {
    //资质保存
    int saveCertificate(CertificateRecord record);

    //资质更新
    int updateCertificate(CertificateRecord record);

    //资质删除
    int deleteCertificate(CertificateRecord record);

    //资质查询
    List<CertificateRecord> certificateSearch(CertificateRecord record);

    //资质查询(已认证的)
    List<CertificateRecord> certificateSearch1(CertificateRecord record);

    CertificateRecord queryCertificateById(String id);
}
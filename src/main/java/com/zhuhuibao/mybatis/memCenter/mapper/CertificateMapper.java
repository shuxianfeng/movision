package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Certificate;

import java.util.List;

public interface CertificateMapper {

    List<Certificate> findCertificateList(String type);
}
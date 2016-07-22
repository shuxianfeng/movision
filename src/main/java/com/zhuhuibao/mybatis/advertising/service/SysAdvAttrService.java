package com.zhuhuibao.mybatis.advertising.service;

import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.advertising.mapper.SysAdvAttrMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jianglz
 * @since 16/6/20.
 */
@Service
public class SysAdvAttrService {

    private static final Logger log = LoggerFactory.getLogger(SysAdvAttrService.class);


    @Autowired
    private SysAdvAttrMapper mapper;



}

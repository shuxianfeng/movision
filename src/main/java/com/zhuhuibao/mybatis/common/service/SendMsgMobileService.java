package com.zhuhuibao.mybatis.common.service;

import com.zhuhuibao.mybatis.common.entity.Suggest;
import com.zhuhuibao.mybatis.common.mapper.SendMsgMobileMapper;
import com.zhuhuibao.mybatis.common.mapper.SuggestMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/7/21 0021.
 */
@Service
public class SendMsgMobileService {
	private static final Logger log = LoggerFactory.getLogger(SendMsgMobileService.class);

	@Autowired
	SendMsgMobileMapper sendMsgMobileMapper;

	public List<Map<String,String>> find() {
		try {
			return sendMsgMobileMapper.find();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}

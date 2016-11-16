package com.zhuhuibao.mybatis.expert.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ExpertConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.expert.entity.Answer;
import com.zhuhuibao.mybatis.expert.mapper.AnswerMapper;
import com.zhuhuibao.utils.MsgPropertiesUtils;

@Service
public class ExpertNewService {
	
	@Autowired
	ExpertService expertSV;
	
	@Autowired
	AnswerMapper answerMapper;
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";
	
	public Response addExpAnswer(Answer answer){
		Response response = new Response();
	    Long createId = ShiroUtil.getCreateID();
	    if (createId != null) {
	        answer.setCreateid(String.valueOf(createId));
	        answer.setStatus("0");
	        expertSV.answerQuestion(answer);
	    } else {
	        throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
	    }
	    return response;
	}
	
	/**
	 * 查询专家是否能回答该问题  
	 * 
	 * @param questionId
	 * @return
	 */
	public Response queryExpPrivilege(String questionId){
		
		Response response = new Response();
		Long createId = ShiroUtil.getCreateID();
		
		if (createId != null) {
			boolean isExistExpAnswer = false;
			boolean statusFlag = false;
			List<Map<String, Object>> answerList = answerMapper.queryAnswerByQuestionId(questionId);
			//共两个判断条件
			//1 专家未回答过该问题
			if(CollectionUtils.isNotEmpty(answerList)){
				for (Map<String, Object> map : answerList) {
					Long expId = (Long)map.get("expertId");
					if(expId.equals(createId)){
						//该专家已经回答过
						isExistExpAnswer = true;
					}
				}
			}
			if(isExistExpAnswer){
				response.setData(genResultMap(FAIL, "专家已经回答过了"));
				return response;
			}
			
			//2 没有关闭、没有设采纳最佳答案
			Map q = expertSV.queryQuestionById(questionId);
			String status =  String.valueOf(q.get("status"));
			if(ExpertConstant.QUESTION_NOT_CLOSE.equals(status)){
				statusFlag = true;
				
			}else if(ExpertConstant.EXPERT_QUESTION_STATUS_TWO.equals(status)){
				response.setData(genResultMap(FAIL, "该问题已关闭"));
				return response;
			}else if(ExpertConstant.EXPERT_QUESTION_STATUS_FOUR.equals(status)){
				response.setData(genResultMap(FAIL, "该问题已采纳"));
				return response;
			}
			
			//总结
			if(!isExistExpAnswer && statusFlag){
				response.setData(genResultMap(SUCCESS, "专家可以回答"));
			}
			
	    } else {
	        throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
	    }
		return response;
	}
	
	/**
	 * 生成返回的map
	 * @param status
	 * @param msg
	 * @return
	 */
	public Map genResultMap(String status, String msg){
		Map map = new HashMap<>();
		map.put("status", status);
		map.put("msg", msg);
		return map;
	}
	
	
	
}

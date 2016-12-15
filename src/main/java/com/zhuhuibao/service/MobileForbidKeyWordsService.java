package com.zhuhuibao.service;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.mybatis.memCenter.entity.ForbidKeyWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.memCenter.service.ForbidKeyWordsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 触屏端屏蔽企业相关接口实现类
 *
 * @author liyang
 * @date 2016年10月12日
 */
@Service
@Transactional
public class MobileForbidKeyWordsService {

    @Autowired
    private ForbidKeyWordsService forbidKeyWordsService;

    /**
     * 查看用户屏蔽企业信息
     * 
     * @param createId
     *            用户id
     * @return
     */
    public List<Map<String, String>> queryKeyWordsList(Long createId) {
        Map<String, Object> map = new HashMap<>();
        map.put("create_id", String.valueOf(createId));
        map.put("is_deleted", Constants.DeleteMark.NODELETE.toString());
        return forbidKeyWordsService.queryKeyWordsList(map);
    }

    /**
     * 删除屏蔽企业记录
     * 
     * @param id
     */
    public void deletleForbidKeyWords(String id) {
        ForbidKeyWords forbidKeyWords = new ForbidKeyWords();
        forbidKeyWords.setId(id);
        forbidKeyWords.setIs_deleted(Constants.DeleteMark.DELETE.toString());
        forbidKeyWordsService.deletleForbidKeyWords(forbidKeyWords);
    }

    /**
     * 查询当前用户屏蔽企业记录
     * 
     * @param createId
     * @return
     */
    public List<Map<String, String>> queryKeyWordsList(String createId) {
        Map<String, Object> map = new HashMap<>();
        map.put("create_id", String.valueOf(createId));
        map.put("is_deleted", Constants.DeleteMark.NODELETE.toString());
        return forbidKeyWordsService.queryKeyWordsList(map);
    }

    /**
     * 增加屏蔽企业记录
     * 
     * @param createId
     * @param companyId
     */
    public void addForbidKeyWords(String createId, String companyId) {
        ForbidKeyWords forbidKeyWords = new ForbidKeyWords();
        forbidKeyWords.setCreate_id(String.valueOf(createId));
        forbidKeyWords.setCompany_id(companyId);
        forbidKeyWordsService.addForbidKeyWords(forbidKeyWords);
    }
}

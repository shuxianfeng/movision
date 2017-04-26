package com.movision.facade.boss;

import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.postSensitiveWords.entity.PostSensitiveWords;
import com.movision.mybatis.postSensitiveWords.service.PostSensitiveWordsService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/4/26 10:09
 */
@Service
public class PostSensitiveWordsFacade {

    @Autowired
    private PostSensitiveWordsService postSensitiveWordsService;

    private static Logger log = LoggerFactory.getLogger(PostSensitiveWordsFacade.class);

    public List<PostSensitiveWords> findAllPostSensitiveWords(Paging<PostSensitiveWords> pager) {
        return postSensitiveWordsService.findAllPostSensitiveWords(pager);
    }

    public Map<String, Object> insert(String name) {
        Map<String, Object> map = new HashedMap();
        PostSensitiveWords postSensitiveWords = new PostSensitiveWords();
        if (StringUtil.isEmpty(name)) {
            postSensitiveWords.setName(name);
        }
        postSensitiveWords.setIntime(new Date());
        int result = postSensitiveWordsService.insert(postSensitiveWords);
        map.put("result", result);
        return map;
    }

    public Map<String, Object> updateByPrimaryKeySelective(String id, String name) {
        Map<String, Object> map = new HashedMap();
        return map;
    }
}

package com.movision.fsearch.service;

import com.movision.fsearch.pojo.spec.NormalSearchSpec;
import com.movision.fsearch.service.exception.ServiceException;
import com.movision.mybatis.post.entity.PostSearchEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/27 16:29
 */
public interface IPostSearchService {

    Map<String, Object> search(NormalSearchSpec spec) throws ServiceException;

    List<PostSearchEntity> searchForPost(NormalSearchSpec spec) throws ServiceException;
}

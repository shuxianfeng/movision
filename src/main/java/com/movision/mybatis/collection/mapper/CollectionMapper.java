package com.movision.mybatis.collection.mapper;

import com.movision.mybatis.collection.entity.Collection;

public interface CollectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Collection record);

    int insertSelective(Collection record);

    Collection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Collection record);

    int updateByPrimaryKey(Collection record);

    int checkIsHave(Collection collection);

    int checkIsHaveGoods(Collection collection);

    int collectionPost(Collection collection);

    void addCollectionSum(int postid);
}
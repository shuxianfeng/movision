package com.movision.mybatis.imFirstDialogue.mapper;

import com.movision.mybatis.imFirstDialogue.entity.ImFirstDialogue;
import com.movision.mybatis.imFirstDialogue.entity.ImFirstDialogueVo;
import com.movision.mybatis.imuser.entity.ImUser;

import java.util.List;

public interface ImFirstDialogueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImFirstDialogue record);

    int insertSelective(ImFirstDialogue record);

    ImFirstDialogue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImFirstDialogue record);

    int updateByPrimaryKey(ImFirstDialogue record);

    int isExistFirstDialogue(ImFirstDialogue imFirstDialogue);

    List<ImFirstDialogue> selectFirstDialog(ImFirstDialogue imFirstDialogue);

    ImFirstDialogueVo queryFirst(Integer userid);



}
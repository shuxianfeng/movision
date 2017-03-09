package com.movision.mybatis.imFirstDialogue.service;

import com.movision.mybatis.imFirstDialogue.entity.ImFirstDialogue;
import com.movision.mybatis.imFirstDialogue.mapper.ImFirstDialogueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/9 10:06
 */
@Service
@Transactional
public class ImFirstDialogueService {

    private static Logger log = LoggerFactory.getLogger(ImFirstDialogueService.class);

    @Autowired
    private ImFirstDialogueMapper imFirstDialogueMapper;

    public int addDialogue(ImFirstDialogue imFirstDialogue) {
        try {
            log.info("添加首次对话记录");
            return imFirstDialogueMapper.insertSelective(imFirstDialogue);
        } catch (Exception e) {
            log.error("添加首次对话记录失败", e);
            throw e;
        }
    }

    public int isExistFirstDialogue(ImFirstDialogue imFirstDialogue) {
        try {
            log.info("判断是否存在第一次对话");
            return imFirstDialogueMapper.isExistFirstDialogue(imFirstDialogue);
        } catch (Exception e) {
            log.error("判断是否存在第一次对话失败", e);
            throw e;
        }
    }

    public int isExistReply(ImFirstDialogue imFirstDialogue) {
        try {
            log.info("判断对方是否回复打招呼");
            return imFirstDialogueMapper.isExistFirstDialogue(imFirstDialogue);
        } catch (Exception e) {
            log.error("判断对方是否回复打招呼失败", e);
            throw e;
        }
    }

    public List<ImFirstDialogue> selectFirstDialog(ImFirstDialogue imFirstDialogue) {
        try {
            log.info("查询当前登录人和私信人的第一次会话");
            return imFirstDialogueMapper.selectFirstDialog(imFirstDialogue);
        } catch (Exception e) {
            log.error("查询当前登录人和私信人的第一次会话失败", e);
            throw e;
        }
    }


}

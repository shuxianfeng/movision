package com.movision.facade.msgCenter;

import com.movision.mybatis.PostZanRecord.entity.ZanRecordVo;
import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/27 9:56
 */
public class MsgCenterFacadeTest extends SpringTestCase {
    @Autowired
    private MsgCenterFacade msgCenterFacade;

    @Test
    public void findZan() throws Exception {

        List<ZanRecordVo> zanRecordVoList = msgCenterFacade.findZan(274);
        System.out.println("被赞的集合：" + zanRecordVoList.toString());
    }

}
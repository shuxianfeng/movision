package com.movision.facade.apsaraVideo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author zhuangyuhao
 * @Date 2017/5/19 14:50
 */
public class AliVideoFacadeTest {

    @Autowired
    private AliVideoFacade aliVideoFacade;

    @Test
    public void prepareParams() throws Exception {
        System.out.println(aliVideoFacade.generateRequestUrl("GetVideoPlayAuth", "910cff8ce82a4972b662271903197c81"));
    }

}
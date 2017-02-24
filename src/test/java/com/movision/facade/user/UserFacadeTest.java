package com.movision.facade.user;

import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/23 9:40
 */
public class UserFacadeTest extends SpringTestCase {

    @Autowired
    private UserFacade userFacade;

    @Test
    public void isExistAccount() throws Exception {

        System.out.println("《《《《《" + userFacade.isExistAccount("18170708080"));

    }

}
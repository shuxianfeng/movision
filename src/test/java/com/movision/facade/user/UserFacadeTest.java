package com.movision.facade.user;

import com.movision.mybatis.user.entity.LoginUser;
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

    /*@Test
    public void executecompleteImuserInfo() throws Exception{
        userFacade.completeImuserInfo();
    }*/

    @Test
    public void executecompleteImdeviceInfo() throws Exception {
        userFacade.completeImdeviceInfo();
    }

    /*@Test
    public void isExistAccount() throws Exception {

        System.out.println("《《《《《" + userFacade.isExistAccount("18170708080"));
    }*/

    /*@Test
    public void testgetLoginUserByToken() throws Exception {
        String token = "{\"username\":\"BACD9298FCD3ED19A113BE54DE6E7362\",\"password\":[\"B\",\"A\",\"C\",\"D\",\"9\",\"2\",\"9\",\"8\",\"F\",\"C\",\"D\",\"3\",\"E\",\"D\",\"1\",\"9\",\"A\",\"1\",\"1\",\"3\",\"B\",\"E\",\"5\",\"4\",\"D\",\"E\",\"6\",\"E\",\"7\",\"3\",\"6\",\"2\",\"F\",\"A\",\"F\",\"D\",\"1\",\"A\",\"B\",\"C\",\"-\",\"1\",\"C\",\"2\",\"3\",\"-\",\"4\",\"5\",\"B\",\"B\",\"-\",\"B\",\"7\",\"E\",\"2\",\"-\",\"9\",\"8\",\"1\",\"6\",\"E\",\"3\",\"0\",\"7\",\"7\",\"9\",\"C\",\"D\"],\"rememberMe\":FALSE}                                    ";
        LoginUser loginUser = userFacade.getLoginUserByToken(token);
        logger.debug(loginUser.toString());
    }*/

}
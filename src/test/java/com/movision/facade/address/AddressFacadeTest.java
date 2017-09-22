package com.movision.facade.address;

import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/22 15:41
 */
public class AddressFacadeTest extends SpringTestCase {

    @Autowired
    private AddressFacade addressFacade;

    @Test
    public void getAddressByLatAndLng() throws Exception {
        Map map = addressFacade.getAddressByLatAndLng("32.05606138741064", "118.7935942346943");
//        Map map = addressFacade.getAddressByLatAndLng("39.983424", "116.322987");
        System.out.println("返回值：" + map.toString());
    }

}
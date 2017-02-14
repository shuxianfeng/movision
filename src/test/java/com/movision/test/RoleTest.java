package com.movision.test;

import com.movision.facade.user.RoleFacade;
import com.movision.mybatis.roleMenuRelation.service.RoleMenuRelationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/14 11:33
 */
public class RoleTest extends SpringTestCase {

    @Autowired
    private RoleFacade roleFacade;

    @Autowired
    private RoleMenuRelationService roleMenuRelationService;

/*
    @Test
    public void testbatchAddByMenuid(){
        roleFacade.batchAddByMenuid("6", 2);
    }
*/

/*
    @Test
    public void testdelRelationByRoleid(){
        int[] arr = new int[1];
        arr[0] = 2;
        roleMenuRelationService.delRelationByRoleid(arr);
    }
*/
}

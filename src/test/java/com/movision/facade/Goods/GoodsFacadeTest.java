package com.movision.facade.Goods;

import com.movision.mybatis.goods.entity.Goods;
import com.movision.test.SpringTestCase;
import com.movision.utils.pagination.model.Paging;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 10:43
 */
public class GoodsFacadeTest extends SpringTestCase {
    @Autowired
    private GoodsFacade goodsFacade;

    @Test
    public void findAllMyCollectGoods() throws Exception {
        Paging<Map> paging = new Paging<>(Integer.valueOf(1), Integer.valueOf(10));
        List<Map> goodsList = goodsFacade.findAllMyCollectGoods(paging, 1);
        paging.result(goodsList);
        System.out.println(paging);
    }

}
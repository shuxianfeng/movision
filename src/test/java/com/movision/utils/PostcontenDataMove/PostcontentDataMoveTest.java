package com.movision.utils.PostcontenDataMove;

import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/6/17 10:38
 */
public class PostcontentDataMoveTest extends SpringTestCase {

    @Autowired
    private PostcontentDataMove postcontentDataMove;

    @Test
    public void dataMove() throws Exception {
        postcontentDataMove.dataMove();
    }

}
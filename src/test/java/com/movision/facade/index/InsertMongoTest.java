package com.movision.facade.index;

import com.movision.facade.robot.RobotFacade;
import com.movision.test.SpringTestCase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/10/6 0006.
 */
public class InsertMongoTest extends SpringTestCase {

    @Autowired
    private RobotFacade robotFacade;

    public void insertMongo() throws Exception {
        robotFacade.insertMongoPostView(500);
    }

}
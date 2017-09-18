package com.movision.facade.robot;

import com.movision.common.constant.PointConstant;
import com.movision.common.constant.UserConstants;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.DateUtils;
import com.movision.utils.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @Author zhuangyuhao
 * @Date 2017/9/18 10:50
 */
@Service
public class RobotFacade {
    private static Logger log = LoggerFactory.getLogger(RobotFacade.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PointRecordFacade pointRecordFacade;

    /**
     * 创建num个robot用户
     *
     * @param num
     */
    public void batchAddRobotUser(int num) {
        //1 先找到本次批次的最大的id, 在 10001-20000 之间
        Integer maxId = userService.selectMaxRobotId();
        int firstId = 10001;    //默认第一个id是10001
        if (null != maxId) {
            firstId = maxId + 1;    //如果存在最大id， 则第一个id是maxid+1
        }
//        int lastId = firstId + num;    //最后一个id
        /**
         * 2 循环新增机器人个人信息
         */
        for (int i = 0; i < num; i++) {
            //机器人id
            int uid = firstId + i;
            User robot = createRobot(uid);
            //1)新增用户（暂时不需要处理yw_im_device, yw_im_user）
            userService.insertSelective(robot);
            //2)增加新用户注册积分流水
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.new_user_register.getCode(), PointConstant.POINT.new_user_register.getCode(), uid);
            //3)增加绑定手机号积分流水
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.binding_phone.getCode(), PointConstant.POINT.binding_phone.getCode(), uid);
        }
    }


    /**
     * 创建机器人信息
     *
     * @return
     */
    private User createRobot(int uid) {
        User robot = new User();
        robot.setId(uid);   //id
        String phone = uid + "000000";  //手机号
        robot.setPhone(phone);
        robot.setInvitecode(UUIDGenerator.gen6Uuid());    //自己的邀请码
        robot.setNickname("robot_" + uid);  //昵称
        robot.setPhoto(UserConstants.DEFAULT_APPUSER_PHOTO);    //头像
        robot.setSex(0);    //性别 默认是女
        robot.setBirthday(DateUtils.getDefaultBirthday());  //1990-08-19
        robot.setProvince("上海");
        robot.setCity("上海市");
        robot.setDeviceno("robot_deviceno_" + uid);
        robot.setIntime(new Date());
        robot.setLoginTime(new Date());
        robot.setIsrecommend(0);
        robot.setHeat_value(35);
        robot.setIp_city("310100");
        return robot;
    }

}

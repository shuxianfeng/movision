package com.movision.facade.robot;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.PointConstant;
import com.movision.common.constant.UserConstants;
import com.movision.exception.BusinessException;
import com.movision.facade.collection.CollectionFacade;
import com.movision.facade.index.FacadeHeatValue;
import com.movision.facade.index.FacadePost;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserVo;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userOperationRecord.entity.UserOperationRecord;
import com.movision.mybatis.userOperationRecord.service.UserOperationRecordService;
import com.movision.utils.DateUtils;
import com.movision.utils.ListUtil;
import com.movision.utils.UUIDGenerator;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


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

    @Autowired
    private FacadeHeatValue facadeHeatValue;

    @Autowired
    private PostService postService;

    @Autowired
    private FacadePost facadePost;

    @Autowired
    private UserOperationRecordService userOperationRecordService;

    @Autowired
    private CollectionFacade collectionFacade;


    /**
     * 创建n个robot用户
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

    /**
     * 机器人帖子点赞操作
     * <p>
     * (这里不需要进行手机推送，防止骚扰到用户。
     * 因为，我们的目的，是想增加某个帖子的点赞数量！)
     *
     * @param postid
     * @param num
     */
    public void robotZanPost(int postid, int num) {
        //1 集合机器人大军
        List<User> robotArmy = assembleRobotArmy(num);

        //2 循环进行帖子点赞操作， 需要注意，点赞不能在同一个时刻
        for (int i = 0; i < robotArmy.size(); i++) {
            int userid = robotArmy.get(i).getId();
            processRobotZanPost(postid, userid);
        }
    }


    /**
     * 集合机器人大军
     *
     * @param num
     * @return
     */
    private List<User> assembleRobotArmy(int num) {
        //1 先查询机器人大军
        List<User> robots = userService.selectRobotUser();
        if (ListUtil.isEmpty(robots)) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "机器人用户数量为0");
        }
        //2 随机选取n个机器人
        List<User> randomRobots = (List<User>) ListUtil.randomList(robots);
        List<User> robotArmy = new ArrayList<>();
        int size = robots.size();
        if (num <= size) {
            robotArmy = randomRobots.subList(0, num);
        } else {
            robotArmy = randomRobots;
        }
        return robotArmy;
    }

    /**
     * 处理机器人点赞帖子
     *
     *
     * @param postid
     * @param userid
     */
    private void processRobotZanPost(int postid, int userid) {
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("postid", postid);
        parammap.put("userid", userid);
        parammap.put("intime", new Date());
        //查询当前用户是否已点赞该帖
        int count = postService.queryIsZanPost(parammap);
        if (count == 0) {
            //增加帖子热度
            facadeHeatValue.addHeatValue(postid, 3, String.valueOf(userid));

            //查看用户点赞操作行为，并记录积分流水
            UserOperationRecord entiy = userOperationRecordService.queryUserOperationRecordByUser(userid);
            facadePost.handleZanStatusAndZanPoint(String.valueOf(userid), entiy);

            //插入点赞历史记录
            postService.insertZanRecord(parammap);
            //更新帖子点赞数量字段
            postService.updatePostByZanSum(postid);
        }
    }


    /**
     * 机器人收藏帖子操作
     *
     * @param postid
     * @param num
     */
    public void robotCollectPost(int postid, int num) {
        //1 集合机器人大军
        List<User> robotArmy = assembleRobotArmy(num);

        //2 循环进行收藏帖子操作
        for (int i = 0; i < robotArmy.size(); i++) {
            int userid = robotArmy.get(i).getId();
            collectionFacade.collectionPost(String.valueOf(postid), String.valueOf(userid), String.valueOf(0));
        }
    }

    /**
     * 机器人关注用户操作
     *
     * @param userid 被关注的人
     * @param num
     */
    public void robotFollowUser(int userid, int num) {
        //1 集合机器人大军
        List<User> robotArmy = assembleRobotArmy(num);

        //2 循环进行关注作者操作
        for (int i = 0; i < robotArmy.size(); i++) {
            int robotid = robotArmy.get(i).getId();
            facadePost.concernedAuthorUser(robotid, userid);
        }
    }

    /**
     * 查询机器人列表
     *
     * @param name
     * @param pag
     * @return
     */
    public List<User> QueryRobotByList(String name, Paging<User> pag) {
        List<User> list = userService.findAllQueryRobotByList(name, pag);
        return list;
    }

    /**
     * 查询全部机器人
     *
     * @return
     */
    public List<User> QueryRobotByList() {
        return userService.selectRobotUser();
    }

    /**
     * 根据id查询机器人详情
     *
     * @param id
     * @return
     */
    public User queryRobotById(String id) {
        return userService.queryRobotById(Integer.parseInt(id));
    }


    public void updateRoboltById(String id, String email, String nickname, String phone, String photo, String sex) {
        User user = new User();
        if (StringUtil.isNotEmpty(id)) {
            user.setId(Integer.parseInt(id));
        }
        if (StringUtil.isEmpty(email)) {
            user.setEmail(email);
        }
        if (StringUtil.isNotEmpty(nickname)) {
            user.setNickname(nickname);
        }
        if (StringUtil.isNotEmpty(phone)) {
            user.setPhone(phone);
        }
        if (StringUtil.isNotEmpty(photo)) {
            user.setPhoto(photo);
        }
    }


}

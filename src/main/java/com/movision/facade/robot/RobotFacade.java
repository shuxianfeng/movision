package com.movision.facade.robot;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.PointConstant;
import com.movision.exception.BusinessException;
import com.movision.facade.collection.CollectionFacade;
import com.movision.facade.index.FacadeHeatValue;
import com.movision.facade.index.FacadePost;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.personalizedSignature.entity.PersonalizedSignature;
import com.movision.mybatis.personalizedSignature.service.PersonalizedSignatureService;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.robotComment.entity.RobotComment;
import com.movision.mybatis.robotComment.service.RobotCommentService;
import com.movision.mybatis.robotNickname.service.RobotNicknameService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userOperationRecord.entity.UserOperationRecord;
import com.movision.mybatis.userOperationRecord.service.UserOperationRecordService;
import com.movision.mybatis.userPhoto.entity.UserPhoto;
import com.movision.utils.DateUtils;
import com.movision.utils.ListUtil;
import com.movision.utils.UUIDGenerator;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private RobotCommentService robotCommentService;

    @Autowired
    private RobotNicknameService nicknameService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PersonalizedSignatureService personalizedSignatureService;

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
        //2 查询头像库 num个
        List<UserPhoto> userPhotoList = userService.queryUserPhotos(num);
        //3 查询昵称库 num个
        List<String> robotNicknameList = nicknameService.queryRoboltNickname(num);
        //4 查询签名库 num个
        List<PersonalizedSignature> personalizedSignatures = personalizedSignatureService.queryRoboltSignature(num);

        /**
         * 3 循环新增机器人个人信息
         */
        for (int i = 0; i < num; i++) {
            //机器人id
            int uid = firstId + i;
            String photo = userPhotoList.get(i).getUrl();   //头像
            String nickname = robotNicknameList.get(i); //昵称
            String sign = personalizedSignatures.get(i).getSignature();//签名

            User robot = createRobot(uid, photo, nickname, sign);
            //1)新增用户（暂时不需要处理yw_im_device, yw_im_user）
            userService.insertSelective(robot);
            //2)增加新用户注册积分流水
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.new_user_register.getCode(), PointConstant.POINT.new_user_register.getCode(), uid);
            //3)增加绑定手机号积分流水
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.binding_phone.getCode(), PointConstant.POINT.binding_phone.getCode(), uid);
        }
    }

    /**
     * 从list中随机选出几个数，并按照原来的顺序排列（比如从list中随机选出n个数）
     *
     * @param list
     * @param n
     * @return
     */
    public List<T> selectRandomList(List<T> list, int n) {
        //若list.size()大于n套，随机产生n个对象，并按照原来的顺序排列
        //若list的对象为ListObject
        if (list.size() > n) {
            Random randomId = new Random();
            //对随机的n个对象排成原来的默认顺序
            List<Integer> indexes = new ArrayList<Integer>();
            while (indexes.size() < n) {
                //对象在list里的位置
                int index = randomId.nextInt(list.size());
                if (!indexes.contains(index)) {
                    indexes.add(index);
                }
            }
            //对indexes排序
            Collections.sort(indexes);
            //取出indexes对应的list放到newList
            List<T> newList = new ArrayList<>();
            for (int index : indexes) {
                newList.add(list.get(index));
            }
            list.clear();
            list.addAll(newList);
        }
        return list;
    }


    /**
     * 创建机器人信息
     *
     * @return
     */
    private User createRobot(int uid, String photo, String nickname, String sign) {
        User robot = new User();
        robot.setId(uid);   //id
        String phone = uid + "000000";  //手机号
        robot.setPhone(phone);
        robot.setInvitecode(UUIDGenerator.gen6Uuid());    //自己的邀请码
//        robot.setNickname("robot_" + uid);  //昵称
        robot.setNickname(nickname);
//        robot.setPhoto(UserConstants.DEFAULT_APPUSER_PHOTO);    //头像
        robot.setPhoto(photo);
        robot.setSign(sign);    //签名
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
        if (num < 1) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "机器人数量至少是1个");
        }
        //1 集合机器人大军
        List<User> robotArmy = userService.queryRandomUser(num);

        //2 循环进行帖子点赞操作， 需要注意，点赞不能在同一个时刻
        for (int i = 0; i < robotArmy.size(); i++) {
            int userid = robotArmy.get(i).getId();
            processRobotZanPost(postid, userid);
        }
    }

    /**
     * 批量点赞帖子操作
     *
     * @param postids
     * @param num
     */
    public void robotZanBatchPost(String postids, int num) {
        validatePostidsAndNum(postids, num);

        String[] postidArr = postids.split(",");
        //循环对每个帖子操作点赞
        Random random = new Random();
        for (int i = 0; i < postidArr.length; i++) {
            //随机取[0,num+1) 之间的整数，最小是0，最大是num
            int n = random.nextInt(num + 1);
            //调用【机器人帖子点赞操作】
            robotZanPost(Integer.valueOf(postidArr[i]), n);
        }
    }

    /**
     * 批量帖子机器人点赞、收藏、评论操作
     *
     * @param postids
     * @param num
     */
    public void robotActionWithZanCollectComment(String postids, int num) {

        validatePostidsAndNum(postids, num);

        String[] postidArr = postids.split(",");

        Random random = new Random();
        for (int i = 0; i < postidArr.length; i++) {
            //1 调用【机器人帖子点赞操作】
            robotZanPost(Integer.valueOf(postidArr[i]), random.nextInt(num + 1));
            //2 调用【机器人帖子收藏操作】
            robotCollectPost(Integer.valueOf(postidArr[i]), random.nextInt(num + 1));
            //3 调用【机器人帖子评论操作】
            robotshuntComment(num, Integer.valueOf(postidArr[i]));
        }
    }

    /**
     * 分流操作帖子评论
     */
    public void robotshuntComment(Integer num, Integer post) {
        if (num > 1 && num <= 200) {//2:40% 4:30% 5:30% 2
            insertPostCommentByRobolt(post, num, 2);
        } else if (num > 200) {//2:60% 4:30% 5:30% 3
            insertPostCommentByRobolt(post, num, 3);
        } else if (num == 1) {//随机100% 1
            insertPostCommentByRobolt(post, num, 1);
        }
    }

    /**
     * 批量收藏帖子
     *
     * @param postids
     * @param num
     */
    public void robotCollectBatchPost(String postids, int num) {
        validatePostidsAndNum(postids, num);

        String[] postidArr = postids.split(",");
        Random random = new Random();
        for (int i = 0; i < postidArr.length; i++) {
            //随机取[0,num+1) 之间的整数，最小是0，最大是num
            int n = random.nextInt(num + 1);

            robotCollectPost(Integer.valueOf(postidArr[i]), n);
        }
    }


    /**
     * 校验传参 postids 和 num
     *
     * @param postids
     * @param num
     */
    private void validatePostidsAndNum(String postids, int num) {
        if (StringUtils.isEmpty(postids)) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "帖子id不能为空");
        }
        if (num < 1) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "机器人数量至少是1个");
        }
    }


    /**
     * 集合机器人大军（弃用，数据量是万级别的时候 性能差）
     * 随机选取指定数量的机器人
     * 如：随机选取500个机器人
     *
     * @param num
     * @return
     */
    private List<User> assembleRobotArmy(int num) {
        //1 先查询机器人大军(性能差)
        List<User> robots = userService.selectRobotUser();
        if (ListUtil.isEmpty(robots)) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "机器人用户数量为0");
        }
        //2 随机选取n个机器人
        List<User> randomRobots = (List<User>) ListUtil.randomList(robots); //打乱机器人列表
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
        List<User> robotArmy = userService.queryRandomUser(num);

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
        List<User> robotArmy = userService.queryRandomUser(num);

        //2 循环进行关注作者操作
        for (int i = 0; i < robotArmy.size(); i++) {
            int robotid = robotArmy.get(i).getId();
            facadePost.concernedAuthorUser(robotid, userid);
        }
    }

    /**
     * 批量关注用户
     *
     * @param userids
     * @param num
     */
    public void batchFollowUser(String userids, int num) {
        if (StringUtils.isEmpty(userids)) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "被关注的用户不能为空");
        }
        if (num < 1) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "机器人数量至少是1个");
        }

        String[] useridArr = userids.split(",");
        //循环对每个帖子操作点赞
        Random random = new Random();
        for (int i = 0; i < useridArr.length; i++) {
            //随机取[0,num+1) 之间的整数，最小是0，最大是num
            int n = random.nextInt(num + 1);
            //调用【机器人关注操作】
            robotFollowUser(Integer.valueOf(useridArr[i]), n);
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
        if (StringUtil.isNotEmpty(sex)) {
            user.setSex(Integer.parseInt(sex));
        }
    }

    /**
     * 查询机器人评论列表
     *
     * @param type
     * @return
     */
    public List<RobotComment> findAllQueryRoboltComment(String type, Paging<RobotComment> pag) {
        return robotCommentService.findAllQueryRoboltComment(Integer.parseInt(type), pag);
    }

    /**
     * 新增机器人评论
     *
     * @param content
     * @param type
     */
    public Map insertRoboltComment(String content, String type) {
        RobotComment robotComment = new RobotComment();
        Map map = new HashMap();
        if (StringUtil.isNotEmpty(content)) {
            robotComment.setContent(content);
        }
        if (StringUtil.isNotEmpty(type)) {
            robotComment.setType(Integer.parseInt(type));
        }
        //查询是否重复
        Integer resault = robotCommentService.queryComentMessage(robotComment);
        if (resault == 0) {
            robotCommentService.insertRoboltComment(robotComment);
            map.put("code", 200);
        } else {
            map.put("code", 400);
        }
        return map;
    }

    /**
     * 删除机器人评论
     *
     * @param id
     */
    @Transactional
    public void deleteRoboltComment(String id) {
        RobotComment robotComment = new RobotComment();
        robotComment.setId(Integer.parseInt(id));
        robotComment.setContent("该评论已经被管理员删除");
        robotCommentService.deleteRoboltComment(robotComment);
    }

    /**
     * 根据id查询机器人评论
     *
     * @param id
     * @return
     */
    public RobotComment queryRoboltCommentById(Integer id) {
        return robotCommentService.queryCommentById(id);
    }

    /**
     * 更新机器人评论
     *
     * @param id
     * @param content
     * @param type
     */
    public void updateRoboltComent(String id, String content, String type) {
        RobotComment robotComment = new RobotComment();
        if (StringUtil.isNotBlank(id)) {
            robotComment.setId(Integer.parseInt(id));
        }
        if (StringUtil.isNotEmpty(content)) {
            robotComment.setContent(content);
        }
        if (StringUtil.isNotEmpty(type)) {
            robotComment.setType(Integer.parseInt(type));
        }
        robotCommentService.updateRoboltComent(robotComment);
    }

    /**
     * 机器人评论帖子
     *
     * @param postid 帖子id
     * @param num 机器人的数量
     */
    @Transactional
    public void insertPostCommentByRobolt(Integer postid, Integer num, Integer type) {

        if (num < 1) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "机器人数量至少是1个");
        }
        //1 集合num个机器人大军
        List<User> users = userService.queryRandomUser(num);
        //2 随机查询num条评论内容
        List<RobotComment> content = randomRobotComment(type, users.size());
        //3 获取帖子发表时间
        Date date = postService.queryPostIdByDate(postid);

        for (int i = 0; i < users.size(); i++) {
            //机器人的id
            Integer userid = users.get(i).getId();
            //1 插入评论表
            System.out.println(date.getTime());
            insertPostComment(postid, content, date, i, userid);
            //2 更新帖子表的评论次数字段
            postService.updatePostBycommentsum(postid);
            //3 增加被评论的帖子热度
            facadeHeatValue.addHeatValue(postid, 4, String.valueOf(userid));
        }
    }

    /**
     * 随机查询num条评论内容
     *
     * @param type
     * @param num
     * @return
     */
    public List<RobotComment> randomRobotComment(Integer type, Integer num) {
        List<RobotComment> content = new ArrayList<>();
        List<RobotComment> contens = new ArrayList();
        if (type == 1) {
            Map map = new HashMap();
            map.put("number", num);
            map.put("type", 0);
            //随机查询num条评论内容
            content = robotCommentService.queryRoboltComment(map);
            contens.addAll(content);
        } else if (type == 2) {//<=200 2:40% 4:30% 5:30%
            //有小数向上进位
            int mm = (int) (Math.ceil(num * 0.4));
            Map map = new HashMap();
            map.put("number", mm);
            map.put("type", 2);
            content = robotCommentService.queryRoboltComment(map);
            contens.addAll(content);
            mm = (int) (Math.ceil(num * 0.3));
            map.put("number", mm);
            map.put("type", 4);
            content = robotCommentService.queryRoboltComment(map);
            contens.addAll(content);
            mm = (int) (Math.ceil(num * 0.3));
            map.put("number", mm);
            map.put("type", 5);
            content = robotCommentService.queryRoboltComment(map);
            contens.addAll(content);
        } else if (type == 3) {//>200 2:60% 4:20% 5:20%
            int mm = (int) (Math.ceil(num * 0.6));
            Map map = new HashMap();
            map.put("number", mm);
            map.put("type", 2);
            content = robotCommentService.queryRoboltComment(map);
            contens.addAll(content);
            mm = (int) (Math.ceil(num * 0.2));
            map.put("number", mm);
            map.put("type", 4);
            content = robotCommentService.queryRoboltComment(map);
            contens.addAll(content);
            mm = (int) (Math.ceil(num * 0.2));
            map.put("number", mm);
            map.put("type", 5);
            content = robotCommentService.queryRoboltComment(map);
            contens.addAll(content);
        }
        return contens;
    }

    /**
     * 批量对帖子进行机器人评论操作
     *
     * @param postids
     * @param num     最大机器人数量
     */
    public void robotCommentBatchPost(String postids, int num) {
        validatePostidsAndNum(postids, num);

        String[] postidArr = postids.split(",");
        //循环对每个帖子评论
        for (int i = 0; i < postidArr.length; i++) {
            //调用【机器人帖子评论操作】
            robotshuntComment(num, Integer.valueOf(postidArr[i]));
        }
    }

    /**
     * 插入评论表
     *
     * @param postid
     * @param content
     * @param date
     * @param i
     * @param userid
     */
    private void insertPostComment(Integer postid, List<RobotComment> content, Date date, int i, Integer userid) {
        CommentVo vo = new CommentVo();
        vo.setPostid(postid);
        vo.setContent(content.get(i).getContent());
        vo.setUserid(userid);
        vo.setZansum(0);
        vo.setIsdel("0");
        vo.setStatus(1);    //审核状态：0待审核 1审核通过 2审核不通过（iscontribute为1时不为空）
        vo.setIscontribute(0);  //是否为特邀嘉宾的评论：0否 1是
        //获取一个几万的随机数,变更评论时间
        Long d = getRandomDate(date);
        vo.setIntime(new Date(d));

        commentService.insertComment(vo);

    }

    /**
     * 获取一个随机的日期
     *
     * @param date
     * @return
     */
    private Long getRandomDate(Date date) {
        int max = 9900000;
        int min = 1000000;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return date.getTime() + s;
    }

    /**
     * 批量修改机器人的头像
     *
     * @param userids
     */
    public void batchChangeRobotPhoto(String userids) {
        if (StringUtils.isEmpty(userids)) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "请选择机器人");
        }

        String[] robotArr = userids.split(",");
        int num = robotArr.length;
        //头像列表
        List<UserPhoto> userPhotoList = userService.queryUserPhotos(num);
        for (int i = 0; i < num; i++) {
            String photo = userPhotoList.get(i).getUrl();

            User user = new User();
            user.setId(Integer.valueOf(robotArr[i]));
            user.setPhoto(photo);
            userService.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * 批量修改机器人的昵称
     *
     * @param userids
     */
    public void batchChangeRobotNickname(String userids) {
        if (StringUtils.isEmpty(userids)) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "请选择机器人");
        }

        String[] robotArr = userids.split(",");
        int num = robotArr.length;

        List<String> robotNicknameList = nicknameService.queryRoboltNickname(num);

        for (int i = 0; i < num; i++) {
            String nickname = robotNicknameList.get(i);

            User user = new User();
            user.setId(Integer.valueOf(robotArr[i]));
            user.setNickname(nickname);
            userService.updateByPrimaryKeySelective(user);
        }
    }

    public void batchChangeRobot(String userids) {
        if (StringUtils.isEmpty(userids)) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "请选择机器人");
        }

        String[] robotArr = userids.split(",");
        int num = robotArr.length;

        List<String> robotNicknameList = nicknameService.queryRoboltNickname(num);
        List<UserPhoto> userPhotoList = userService.queryUserPhotos(num);
        List<PersonalizedSignature> personalizedSignatures = personalizedSignatureService.queryRoboltSignature(num);

        for (int i = 0; i < num; i++) {
            String nickname = robotNicknameList.get(i);     //昵称
            String photo = userPhotoList.get(i).getUrl();   //头像
            String sign = personalizedSignatures.get(i).getSignature();//签名

            User user = new User();
            user.setId(Integer.valueOf(robotArr[i]));
            user.setNickname(nickname);
            user.setSign(sign);
            user.setPhoto(photo);

            userService.updateByPrimaryKeySelective(user);
        }
    }


    public void allChangeRobotInfo() {

        List<User> userList = userService.selectRobotUser();
        String ids = "";
        for (User user : userList) {
            ids += user.getId() + ",";
        }
        String userids = ids.substring(0, ids.length() - 1);

        batchChangeRobot(userids);
    }


}

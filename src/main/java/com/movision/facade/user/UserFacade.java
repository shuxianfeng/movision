package com.movision.facade.user;

import com.movision.aop.UserSaveCache;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.PointConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.exception.AuthException;
import com.movision.exception.BusinessException;
import com.movision.facade.index.FacadeHeatValue;
import com.movision.facade.index.FacadePost;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.PostZanRecord.service.PostZanRecordService;
import com.movision.mybatis.bossUser.service.BossUserService;
import com.movision.mybatis.collection.service.CollectionService;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.pointRecord.entity.PointRecord;
import com.movision.mybatis.pointRecord.service.PointRecordService;
import com.movision.mybatis.post.entity.ActiveVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.user.entity.*;
import com.movision.mybatis.user.service.UserService;
import com.movision.shiro.realm.ShiroRealm;
import com.movision.utils.DateUtils;
import com.movision.utils.IntegerUtil;
import com.movision.utils.pagination.model.Paging;
import javafx.geometry.Pos;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * APP用户 facade
 * @Author zhuangyuhao
 * @Date 2017/1/24 20:20
 */
@Service
public class UserFacade {

    private static Logger log = LoggerFactory.getLogger(UserFacade.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private AppRegisterFacade appRegisterFacade;

    @Autowired
    private PointRecordFacade pointRecordFacade;

    @Autowired
    private PointRecordService pointRecordService;

    @Autowired
    private FacadeHeatValue facadeHeatValue;

    @Autowired
    private FacadePost facadePost;
    @Autowired
    private CollectionService collectionService;
    @Autowired
    private PostZanRecordService postZanRecordService;
    /**
     * 判断是否存在该手机号的app用户
     *
     * @param phone
     * @return
     */
    public Boolean isExistAccount(String phone) {
        return userService.isExistAccount(phone);
    }


    /**
     * 注册新的app用户
     *
     * @param registerUser
     * @return
     */
    public int registerAccount(RegisterUser registerUser) {
        return userService.registerAccount(registerUser);
    }

    /**
     * 修改app用户token和设备号
     * @param registerUser
     * @return
     */
    public int updateAccount(RegisterUser registerUser) {
        return userService.updateRegisterUser(registerUser);
    }

    public UserVo queryUserInfo(String userid) {
        return userService.queryUserInfo(Integer.parseInt(userid));
    }

    public List<PostVo> personPost(Paging<PostVo> pager, String userid) {
        return userService.personPost(pager, Integer.parseInt(userid));
    }

    public List<ActiveVo> personActive(Paging<ActiveVo> pager, String userid) {

        List<ActiveVo> activeVoList = userService.personActive(pager, Integer.parseInt(userid));

        for (int i = 0; i < activeVoList.size(); i++) {
            //遍历计算活动累计参与总人数
            int postid = activeVoList.get(i).getId();//取出活动id
            int partsum = postService.queryActivePartSum(postid);
            activeVoList.get(i).setPartsum(partsum);
            //遍历计算活动距结束天数
            Date begin = activeVoList.get(i).getBegintime();//活动开始时间
            Date end = activeVoList.get(i).getEndtime();//活动结束时间
            Date now = new Date();//当前时间
            int enddays = DateUtils.activeEndDays(now, begin, end);
            activeVoList.get(i).setEnddays(enddays);
        }
        return activeVoList;
    }

    public void commetAPP(String userid) {
        //查询当前用户之前是否获得过评价APP的新手任务积分
        int count = pointRecordService.queryIsComment(Integer.parseInt(userid));

        if (count == 0) {
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.comment_app.getCode(), ShiroUtil.getAppUserID());//根据不同积分类型赠送积分的公共方法（包括总分和流水）
        }
    }

    public void shareSucNotice(String type, String userid, String channel, String postid, String goodsid, String beshareuserid) {

        if (StringUtil.isNotEmpty(userid)) {
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.share.getCode(), Integer.valueOf(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
        }

        Map<String, Object> parammap = new HashMap<>();
        if (StringUtil.isNotEmpty(userid)) {
            parammap.put("userid", Integer.parseInt(userid));
        }
        parammap.put("intime", new Date());
        log.debug("测试分享渠道值>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>!!!!!!!!!!>>>>>>>>>>>>>>>>>"+channel);
        if (channel.equals("0")) {
            parammap.put("channel", "QQ");
        } else if (channel.equals("1")) {
            parammap.put("channel", "QQ空间");
        } else if (channel.equals("2")) {
            parammap.put("channel", "微信");
        } else if (channel.equals("3")) {
            parammap.put("channel", "朋友圈");
        } else if (channel.equals("4")) {
            parammap.put("channel", "新浪微博");
        }

        if (type.equals("0")) {
            //分享帖子或活动
            parammap.put("postid", Integer.parseInt(postid));
            if (StringUtil.isNotEmpty(userid)) {
                userService.insertPostShare(parammap);//插入帖子分享记录
            }
            postService.updatePostShareNum(parammap);//增加帖子分享次数
            //增加热度
            // facadeHeatValue.addHeatValue(Integer.parseInt(postid), 5);
        } else if (type.equals("1")) {
            //分享商品
            parammap.put("goodsid", Integer.parseInt(goodsid));
            if (StringUtil.isNotEmpty(userid)){
                userService.insertGoodsShare(parammap);//插入商品分享记录(目前商品表不记录被分享的总次数)
            }
        } else if (type.equals("2")) {
            //个人主页（自己或他人的）
            //------------------------------暂时预留，后期可以存主页分享次数
        }
    }

    public User queryUserByPhone(String phone) {
        return userService.queryUserByPhone(phone);
    }

    /**
     * 根据手机获取该用户信息，以及判断是什么角色
     *
     * @param phone
     * @return
     */
    public LoginUser getLoginUserByPhone(String phone) {

        LoginUser loginUser = userService.queryLoginUserByPhone(phone);
        if (null == loginUser) {
            throw new AuthException(MsgCodeConstant.app_user_not_exist, "该手机号的用户不存在");
        }
        //若app用户同时是boss系统用户，则判断该用户是app管理员（可以管理自己的圈子）
//        BossUser bossUser = bossUserService.queryAdminUserByPhone(phone);
//        if (null == bossUser) {
//            loginUser.setRole("200");   //App普通用户
//        } else {
//            loginUser.setRole("100");   //App管理员
//        }

        return loginUser;
    }

    /**
     * 根据token获取app登录用户信息
     *
     * @param token
     * @return
     */
    public LoginUser getLoginUserByToken(String token) {

        LoginUser loginUser = userService.selectLoginUserByToken(token);
        if (null == loginUser) {
            throw new AuthException(MsgCodeConstant.app_user_not_exist_with_this_token, "该token的app用户不存在");
        }
        return loginUser;
    }

    /**
     * 根据用户id获取Loginuser
     *
     * @param userid
     * @return
     */
    public LoginUser getLoginuserByUserid(Integer userid) {
        return userService.selectLoginuserByUserid(userid);
    }

    /**
     * 测试使用
     *
     * @param user
     * @return
     */
    //@CachePut(value = "user", key = "'id_'+#user.getId()")
    /*@Caching(put = {
            @CachePut(value = "user", key = "'user_id_'+#user.id"),
            @CachePut(value = "user", key = "'user_username_'+#user.username"),
            @CachePut(value = "user", key = "'user_phone_'+#user.phone")
    })*/
    @UserSaveCache  //自定义缓存注解
    public User addUser(User user) {
        System.out.println("addUser,@CachePut注解方法被调用啦。。。。。");
        return user;
    }

    /**
     * 测试使用
     *
     * @param id
     * @return
     */
    @Cacheable(value = "user", key = "'id_'+#id") //,key="'user_id_'+#id"
    public User getUserByID(Integer id) {
        System.out.println("@Cacheable注解方法被调用啦。。。。。");
        User user = new User();
        user.setId(123);
        user.setPhone("18051989558");
        user.setNickname("zyh");
        return user;
    }

    /**
     * 测试使用
     *
     * @param user
     * @return
     */
    @CachePut(value = "user", key = "'users_'+#user.getId()")
    public List<User> getUsers(User user) {
        System.out.println("@CachePut注解方法被调用啦。。。。。");
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User temp = new User();
            temp.setNickname("username" + i);
            users.add(temp);
        }
        return users;
    }

    /**
     * 完善个人资料过程
     *
     * @param personInfo
     */
    public void finishPersonDataProcess(PersonInfo personInfo) {

        validateNicknameIsExist(personInfo);

        updatePersonInfo(personInfo);

        addPointProcess();

    }

    /**
     * 修改昵称之前，先做重复校验
     * @param personInfo
     */
    private void validateNicknameIsExist(PersonInfo personInfo) {
        String nickname = personInfo.getNickname();
        if (StringUtils.isNotBlank(nickname)) {
            Boolean isExistNickname = userService.isExistSameNickname(nickname, personInfo.getId());
            if (isExistNickname) {

                throw new BusinessException(MsgCodeConstant.app_nickname_already_exist, "该昵称已经存在，请换昵称");
            }
        }
    }

    /**
     * 判断当前用户是否需要添加【完善个人资料】的积分记录
     */
    private void addPointProcess() {
        //查询是否存在该用户的完善个人资料的积分记录
        PointRecord pointRecord = pointRecordService.selectFinishPersonDataPointRecord(ShiroUtil.getAppUserID());

        //获取当前用户信息
        User user = userService.selectByPrimaryKey(ShiroUtil.getAppUserID());
        //检查个人资料是否全部完善：生日，头像，性别，签名
        if (StringUtils.isNotBlank(String.valueOf(user.getBirthday()))
                && StringUtils.isNotBlank(user.getPhoto())
                && StringUtils.isNotBlank(String.valueOf(user.getSex()))
                && StringUtils.isNotBlank(user.getSign())
                && null == pointRecord) {

            log.debug("需要进行【完善个人资料】积分操作");
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.finish_personal_data.getCode(), ShiroUtil.getAppUserID());
        } else {
            log.debug("不需要进行【完善个人资料】积分操作");
        }

    }

    /**
     * 更新个人资料
     *
     * @param personInfo
     */
    private void updatePersonInfo(PersonInfo personInfo) {
        User user = new User();
        user.setId(personInfo.getId());

        if (StringUtils.isNotBlank(personInfo.getNickname())) {
            user.setNickname(personInfo.getNickname().trim());  //去除首尾空格
        }
        if (StringUtils.isNotBlank(personInfo.getBirthday())) {
            user.setBirthday(DateUtils.str2Date(personInfo.getBirthday()));
        }
        user.setPhoto(personInfo.getPhoto());
        user.setSex(personInfo.getSex());
        user.setSign(personInfo.getSign());
        //修改数据库中个人信息
        userService.updateByPrimaryKeySelective(user);
        //修改session 中的个人信息
        ShiroUtil.updateAppuser(user);
    }


    /**
     * 记录申请vip的时间
     */
    public void applyVip() {
        User user = new User();
        user.setId(ShiroUtil.getAppUserID());
        user.setApplydate(new Date());

        userService.updateByPrimaryKeySelective(user);
    }



    /**
     * 查询出当前用户积分剩余
     *
     * @param userid
     * @return
     */
    public int queryUserByRewarde(int userid) {
        return userService.queryUserByRewarde(userid);
    }


    /**
     * 操作用户积分
     *
     * @param integral
     */
    public int updateUserPoint(int userid, int integral, int type) {
        Map map = new HashedMap();
        map.put("userid", userid);
        map.put("integral", integral);
        map.put("type", type);
        return userService.updateUserPoint(map);
    }

    public User selectUserByThirdAccount(Map map) {
        return userService.selectUserByThirdAccount(map);
    }

    /**
     * 绑定第三方账号
     *
     * @param flag
     * @param account
     */
    public void bindThirdAccount(Integer flag, String account) {
        User appUser = userService.selectByPrimaryKey(ShiroUtil.getAppUserID());

        appRegisterFacade.setUserThirdAccount(flag, account, appUser);

        userService.updateByPrimaryKeySelective(appUser);
    }

    public User selectByPrimaryKey(Integer userid) {
        return userService.selectByPrimaryKey(userid);
    }

    /**
     * 根据当前登录的用户查询推荐的作者列表
     * @param pager
     * @param userid
     * @return
     */
    public List<Author> getHotAuthor(Paging<Author> pager, String userid){
        //首先查询推荐的作者列表（返回作者的phone字段，用于app端比对是不是通讯录好友的提示）
        Map<String, Object> map = new HashMap<>();
        map.put("userid", Integer.parseInt(userid));
        List<Author> authorList = userService.getHotAuthor(pager, map);

        //遍历查询作者发布的最新的三个帖子
        if (authorList.size() > 0) {
            authorList = queryNewThreePost(authorList);
        }
        return authorList;
    }

    /**
     * 根据当前登录的用户查询用户感兴趣的作者列表
     */
    public List<Author> getInterestAuthor(Paging<Author> pager, String userid){
        //首先查询用户感兴趣的圈子里的作者和感兴趣的标签里的作者，根据热度值倒叙
        Map<String, Object> map = new HashMap<>();
        map.put("userid", Integer.parseInt(userid));
        List<Author> authorList = userService.getInterestAuthor(pager, map);

        //遍历查询作者发布的最新的三个帖子
        if (authorList.size() > 0) {
            authorList = queryNewThreePost(authorList);
        }
        return authorList;
    }

    /**
     * 根据当前登录的用户查询附近的作者列表
     */
    public List<Author> getNearAuthor(Paging<Author> pager, String lng, String lat, String userid){
        //根据传入的经纬度查询距离当前经纬度30公里的所有作者（发过帖子的）
        Map<String, Object> map = new HashMap<>();
        map.put("lng", Double.parseDouble(lng));
        map.put("lat", Double.parseDouble(lat));
        map.put("userid", Integer.parseInt(userid));
        List<Author> authorList = userService.getNearAuthor(pager, map);

        //遍历查询作者发布的最新的三个帖子
        if (authorList.size() > 0) {
            authorList = queryNewThreePost(authorList);
        }
        return authorList;
    }

    /**
     * 根据传入的作者列表遍历查询作者发布的最新的三个帖子
     */
    public List<Author> queryNewThreePost(List<Author> authorList){
        for (int i = 0; i < authorList.size(); i++){
            Author ao = authorList.get(i);
            int id = ao.getId();//作者的用户ID
            List<Post> postList = postService.querPostListByUser(id);
            ao.setPostListByAuthor(postList);
            authorList.set(i, ao);
        }
        return authorList;
    }

    /**
     * 美番2.0我的接口上半部分(*)
     *
     * @param userid
     * @return
     */
    public UserVo queryPersonalHomepage(String userid) {
        //查询用户信息
        UserVo user = userService.queryUserInfoHompage(Integer.parseInt(userid));
        //查询发的帖子总数
        int postcount = postService.queryUserPostCount(Integer.parseInt(userid));
        user.setPostsum(postcount);
        //查询发的活动总数
        int activecount = postService.queryUserActiveCount(Integer.parseInt(userid));
        user.setActivecount(activecount);
        //收藏帖子数
        int collectioncount = collectionService.queryCollectionCount(Integer.parseInt(userid));
        user.setCollectioncount(collectioncount);
        //被赞数
        int zansum = postZanRecordService.userPostZan(Integer.parseInt(userid));
        user.setZansum(zansum);
        //被收藏数
        int collectionsum = collectionService.userPostCollection(Integer.parseInt(userid));
        user.setBecollectsum(collectionsum);
        return user;
    }

    /**
     * 美番2.0我的下半部分
     *
     * @param type
     * @param userid
     * @param paging
     * @return
     */
    public List<PostVo> mineBottle(int type, String userid, Paging<PostVo> paging) {
        List<PostVo> list = null;
        if (type == 1) {//帖子
            //查询用户发的帖子
            list = postService.findAllUserPostList(Integer.parseInt(userid), paging);
            for (int i = 0; i < list.size(); i++) {
                facadePost.countView(list);
            }
        } else if (type == 2) {//活动
            //活动帖子
            list = postService.findAllUserActive(Integer.parseInt(userid), paging);
            //遍历活动获取活动参与人数和活动剩余结束天数
            for (int i = 0; i < list.size(); i++) {
                PostVo ao = list.get(i);
                //计算距离结束时间
                Date begin = ao.getBegintime();
                Date end = ao.getEndtime();
                Date now = new Date();
                int enddays = DateUtils.activeEndDays(now, begin, end);
                ao.setEnddays(enddays);
                //查询活动参与总人数
                int partsum = postService.queryActivePartSum(ao.getId());
                ao.setPartsum(partsum);
                list.set(i, ao);
            }

         }else if (type == 3) {//收藏
            //用户收藏的帖子
            List<Integer> collection = collectionService.queryUserPost(Integer.parseInt(userid));
            //收藏的id查帖子
            if (collection.size() != 0) {
                list = postService.findAllCollectionListByIds(collection, paging);
                for (int i = 0; i < list.size(); i++) {
                    facadePost.countView(list);
                }
            }
        }
        return list;
    }

    /**
     * 我的--关注--关注的作者，点击关注调用的关注的作者列表返回接口
     */
    public List<UserVo> getMineFollowAuthor(String userid, Paging<UserVo> pager){
        //首先查询当前用户关注的作者列表
        Map<String, Object> paramap = new HashMap<>();
        paramap.put("userid", Integer.parseInt(userid));
        List<UserVo> myFollowAuthorList = userService.getMineFollowAuthor(paramap, pager);

        for (int i=0; i<myFollowAuthorList.size(); i++){
            //遍历获取作者的已发帖子数
            UserVo vo = myFollowAuthorList.get(i);
            int id = vo.getId();//查询到的作者userid
            int count = userService.queryPostNumByAuthor(id);
            vo.setPostsum(count);

            //获取当前用户是否关注过该作者
            paramap.put("id", id);
            int sum = userService.queryIsFollowAuthor(paramap);
            vo.setIsfollow(sum);

            myFollowAuthorList.set(i, vo);
        }

        return myFollowAuthorList;
    }

}

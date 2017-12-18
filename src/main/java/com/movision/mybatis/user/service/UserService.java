package com.movision.mybatis.user.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.pageHelper.entity.Datagrid;
import com.movision.mybatis.post.entity.ActiveVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.mapper.PostMapper;
import com.movision.mybatis.province.entity.ProvinceVo;
import com.movision.mybatis.user.entity.*;
import com.movision.mybatis.user.mapper.UserMapper;
import com.movision.mybatis.userPhoto.entity.UserPhoto;
import com.movision.mybatis.userPhoto.mapper.UserPhotoMapper;
import com.movision.utils.L;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.org.mozilla.javascript.internal.EcmaError;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/17 19:43
 */
@Service
@Transactional
public class UserService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserPhotoMapper userPhotoMapper;

    public LoginUser queryLoginUserByPhone(String phone) {
        try {
            log.info("根据手机号查询LoginUser用户, phone=" + phone);
            User user = new User();
            user.setPhone(phone);
            return userMapper.selectLoginUserByPhone(user);
        } catch (Exception e) {
            log.error("根据手机号查询LoginUser用户失败, phone=" + phone, e);
            throw e;
        }
    }

    public void insertPostShare(Map<String, Object> parammap) {
        try {
            log.info("插入帖子分享记录");
            userMapper.insertPostShare(parammap);
        } catch (Exception e) {
            log.error("插入帖子分享记录失败", e);
            throw e;
        }
    }

    public void insertGoodsShare(Map<String, Object> parammap) {
        try {
            log.info("插入商品分享记录");
            userMapper.insertGoodsShare(parammap);
        } catch (Exception e) {
            log.error("插入商品分享记录失败", e);
            throw e;
        }
    }

    public User queryUserByPhone(String phone) {
        try {
            log.info("根据手机号查询User用户, phone=" + phone);
            User user = new User();
            user.setPhone(phone);
            return userMapper.selectByPhone(user);
        } catch (Exception e) {
            log.error("根据手机号查询User用户失败,phone=" + phone, e);
            throw e;
        }
    }

    /**
     * 修改用户昵称
     *
     * @param user
     */
    public void updateUserByNickname(User user) {
        try {
            log.info("根据id修改用户昵称");
            userMapper.updateUserByNickname(user);
        } catch (Exception e) {
            log.error("根据用户id修改昵称异常", e);
            throw e;
        }
    }

    public int queryUserByPoints(String userid) {
        try {
            log.info("查询用户积分是否充足");
            return userMapper.queryUserByPoints(Integer.parseInt(userid));
        } catch (Exception e) {
            log.error("用户积分查询异常", e);
            throw e;
        }
    }

    public Boolean isExistAccount(String phone) {
        try {
            int n = userMapper.isExistAccount(phone);
            return n >= 1;
        } catch (Exception e) {
            log.error("is exist account ", e);
        }
        return null;
    }

    public int registerAccount(RegisterUser registerUser) {
        try {
            log.info("注册新的app用户");
            userMapper.registerAccount(registerUser);
            return registerUser.getId();
        } catch (Exception e) {
            log.error("注册新的app用户失败", e);
            throw e;
        }
    }

    public UserVo queryUserInfo(int userid) {
        try {
            log.info("查询用户个人主页--个人信息");
            return userMapper.queryUserInfo(userid);
        } catch (Exception e) {
            log.error("查询用户个人主页--个人信息失败", e);
            throw e;
        }
    }

    public List<PostVo> personPost(Paging<PostVo> pager, int userid) {
        try {
            log.info("查询个人主页中用户发布的历史帖子和用户分享的历史帖子");
            return postMapper.findAllPersonPost(pager.getRowBounds(), userid);
        } catch (Exception e) {
            log.error("查询个人主页中用户发布的历史帖子和用户分享的历史帖子失败", e);
            throw e;
        }
    }

    public List<ActiveVo> personActive(Paging<ActiveVo> pager, int userid) {
        try {
            log.info("查询个人主页中用户曾经参与过的所有活动列表");
            return postMapper.findAllPersonActive(pager.getRowBounds(), userid);
        } catch (Exception e) {
            log.error("查询个人主页中用户曾经参与过的所有活动列表失败", e);
            throw e;
        }
    }

    public User queryCircleMasterByPhone(String phone) {
        try {
            log.info("查询圈子所属圈主");
            return userMapper.queryCircleMasterByPhone(phone);
        } catch (Exception e) {
            log.error("查询圈子所属圈主失败", e);
            throw e;
        }
    }

    /**
     * 根据圈子id查询出圈子管理员
     *
     * @param circleid
     * @return
     */
    public List<User> queryCircleManagerByCircleid(Integer circleid) {
        try {
            log.info("根据圈子id查询出圈子管理员");
            return userMapper.queryCircleManagerByCircleid(circleid);
        } catch (Exception e) {
            log.error("根据圈子id查询出圈子管理员异常", e);
            throw e;
        }
    }

    /**
     * 根据圈子id查询出圈子管理员
     *
     * @param circleid
     * @return
     */
    public List<UserLike> queryCircleaAministratorListById(Integer circleid) {
        try {
            log.info("根据圈子id查询出圈子管理员");
            return userMapper.queryCircleaAministratorListById(circleid);
        } catch (Exception e) {
            log.error("根据圈子id查询出圈子管理员异常", e);
            throw e;
        }
    }

    public List<String> queryCircleMangerByUseridList(Map map) {
        try {
            log.info("根据登录用户和圈子类型查询圈子的管理员列表");
            return userMapper.queryCircleMangerByUseridList(map);
        } catch (Exception e) {
            log.error("根据登录用户和圈子类型查询圈子的管理员列表异常", e);
            throw e;
        }
    }

    /**
     * 查询圈子管理员列表
     *
     * @param circleid
     * @return
     */
    public List<String> queryCircleManagerByCircleList(Integer circleid) {
        try {
            log.info("查询圈子管理员列表");
            return userMapper.queryCircleManagerByCircleList(circleid);
        } catch (Exception e) {
            log.error("查询圈子管理员列表异常", e);
            throw e;
        }
    }

    /**
     * 查询用户微信id
     *
     * @param userid
     * @return
     */
    public String queryUserByOpenid(Integer userid) {
        try {
            log.info("查询用户微信id");
            return userMapper.queryUserByOpenid(userid);
        } catch (Exception e) {
            log.error("用户微信id查询异常", e);
            throw e;
        }
    }

    /**
     * 根据圈子id查询用户昵称
     * @param userid
     * @return
     */
    public String queryNicknameByUserid(Integer userid) {
        try {
            log.info("查询用户昵称");
            return userMapper.queryUserByNickname(userid);
        } catch (Exception e) {
            log.error("查询用户昵称异常", e);
            throw e;
        }
    }

    /**
     * 根据帖子id查询用户昵称
     *
     * @param postid
     * @return
     */
    public String queryUserByNicknameBy(Integer postid) {
        try {
            log.info("查询用户昵称");
            return userMapper.queryUserByNicknameBy(postid);
        } catch (Exception e) {
            log.error("查询用户昵称异常", e);
            throw e;
        }
    }

    /**
     * 后台管理 /-查询报名信息
     * @param id
     * @return
     */
    public User queryUserB(int id){
        try{
            log.info("查询成功");
            return  userMapper.findAllUser(id);
        }catch (Exception e){
            log.error("查询失败", e);
            throw  e;
        }
    }

    public User queryUser(String phone) {
        try {
            log.info("查询用户信息");
            return userMapper.queryUser(phone);
        } catch (Exception e) {
            log.error("查询用户信息异常", e);
            throw e;
        }
    }

    /**
     * 模糊查询发帖人
     *
     * @param name
     * @return
     */
    public List<UserLike> likeQueryPostByNickname(String name) {
        try {
            log.info("模糊查询发帖人");
            return userMapper.findAlllikeQueryPostByNickname(name);
        } catch (Exception e) {
            log.error("模糊查询发帖人异常", e);
            throw e;
        }
    }

    public int queryUserPoint(int userid) {
        try {
            log.info("根据用户id查询用户积分");
            return userMapper.queryUserPoint(userid);
        } catch (Exception e) {
            log.error("根据用户id查询用户积分失败", e);
            throw e;
        }
    }

    /**
     * 根据用户id查询手机号
     *
     * @param userid
     * @return
     */
    public java.lang.String queryUserbyPhoneByUserid(Integer userid) {
        try {
            log.info("根据用户id查询用户手机号");
            return userMapper.queryUserbyPhoneByUserid(userid);
        } catch (Exception e) {
            log.error("根据用户id查询用户手机号异常 userid=" + userid, e);
            throw e;
        }
    }


    /**
     * 修改app用户token和设备号
     *
     * @param registerUser
     * @return
     */
    public int updateRegisterUser(RegisterUser registerUser) {
        try {
            log.info("修改app用户token和设备号，传参：" + registerUser.toString());
            return userMapper.updateRegisterUser(registerUser);
        } catch (Exception e) {
            log.error("修改app用户token和设备号异常", e);
            throw e;
        }
    }


    /**
     * 查询所有申请加v用户
     *
     * @param pager
     * @return
     */
    public List<UserVo> findAllqueryUsers(Paging<UserVo> pager) {
        try {
            log.info("查询所有申请加V用户");
            return userMapper.findAllqueryUsers(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询所有申请加V用户异常", e);
            throw e;
        }
    }



    /**
     * 根据条件查看VIP列表
     *
     * @param searchUser
     * @param pager
     * @return
     */
    public List<UserVo> queryByConditionvipList(SearchUser searchUser, Paging<UserVo> pager) {
        try {
            log.info("根据条件查看VIP列表");
            return userMapper.findAllQueryByConditionvipList(searchUser, pager.getRowBounds());
        } catch (Exception e) {
            log.error("根据条件查看VIP列表异常", e);
            throw e;
        }
    }

    public int updateByPrimaryKeySelective(User user) {
        try {
            log.info("修改个人资料, 传参：" + user.toString());
            return userMapper.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            log.error("修改个人资料失败", e);
            throw e;
        }
    }

    public Boolean updateLoginappuserInfo(User user) {
        try {
            log.info("更新登录的用户信息，user:" + user.toString());
            int n = userMapper.updateByPrimaryKeySelective(user);
            return n == 1;
        } catch (Exception e) {
            log.error("更新登录的用户信息失败", e);
            throw e;
        }
    }

/*    *//**
     * 查询发帖人列表
     *
     * @param
     * @return
     *//*
    public List<BossUser> queryIssuePostManList() {
        try {
            log.info("查询发帖人列表");
            return userMapper.findAllQueryIssuePostManList();
        } catch (Exception e) {
            log.error("查询发帖人列表异常", e);
            throw e;
        }
    }*/

    /**
     * 查询圈主和管理员
     *
     * @param
     * @return
     */
    public List<User> queryCircleManList(String nickname) {
        try {
            log.info("查询圈主和管理员");
            return userMapper.findAllQueryCircleManList(nickname);
        } catch (Exception e) {
            log.error("查询圈主和管理员异常", e);
            throw e;
        }
    }

    /**
     * 对VIP列表排序
     *
     * @param map
     * @param pager
     * @return
     */
    public List<UserVo> queryAddVSortUser(Map map, Paging<UserVo> pager) {
        try {
            log.info("对VIP列表排序");
            return userMapper.findAllqueryAddVSortUser(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("对VIP列表排序异常", e);
            throw e;
        }
    }

    /**
     * 条件查询VIP申请用户列表
     *
     * @param map
     * @param pager
     * @return
     */
    public List<UserVo> queryUniteConditionByApply(Map map, Paging<UserVo> pager) {
        try {
            log.info("条件查询VIP申请用户列表");
            return userMapper.findAllQueryUniteConditionByApply(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件查询VIP申请用户列表异常", e);
            throw e;
        }
    }

    /**
     * 条件查询VIP申请用户列表
     *
     * @param map
     * @param pager
     * @return
     */
    public List<UserVo> queryUserExamineAndVerify(Map map, Paging<UserVo> pager) {
        try {
            log.info("条件查询VIP申请用户列表");
            return userMapper.findAllQueryUserExamineAndVerify(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件查询VIP申请用户列表异常", e);
            throw e;
        }
    }

    /**
     * 查询所有用户列表
     *
     * @param
     * @return
     */
    public List<UserAll> queryAllUserList(Map map, Paging<UserAll> pag) {
        try {
            log.info("查询所有用户列表");
            return userMapper.findAllqueryAllUserList(map, pag.getRowBounds());
            /*PageHelper.startPage(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
            PageHelper.orderBy("intime desc");

            List<UserAll> list = userMapper.queryAllUserList(map);

            PageInfo<UserAll> pageInfo = new PageInfo<>(list);
            Datagrid datagrid = new Datagrid(pageInfo.getTotal(), pageInfo.getList());
            return datagrid;*/
        } catch (Exception e) {
            log.error("查询所有用户列表异常", e);
            throw e;
        }
    }

    public UserAll queryUserStatistics(Integer id) {
        try {
            log.info("统计用户");
            return userMapper.queryUserStatistics(id);
        } catch (Exception e) {
            log.error("统计用户信息异常", e);
            throw e;
        }
    }

    /**
     * 查询所有用户数量
     *
     * @param map
     * @return
     */
    public Integer queryAllTotal(Map map) {
        try {
            log.info("查询所有用户数量");
            return userMapper.queryAllTotal(map);
        } catch (Exception e) {
            log.error("查询所有用户数量异常", e);
            throw e;
        }
    }

    /**
     * 对用户账号逻辑删除操作
     *
     * @param map
     * @return
     */
    public int deleteUserByid(Map map) {
        try {
            log.info("用户封号");
            return userMapper.deleteUserByid(map);
        } catch (Exception e) {
            log.error("用户封号异常", e);
            throw e;
        }
    }

    /**
     * 对用户加V去V
     *
     * @param map
     * @return
     */
    public int deleteUserLevl(Map map) {
        try {
            log.info("对用户加V去V");
            return userMapper.deleteUserLevl(map);
        } catch (Exception e) {
            log.error("对用户加V去V异常", e);
            throw e;
        }
    }

    /**
     * 查询用户是否被推荐
     *
     * @param id
     * @return
     */
    public Integer queryUserIsrecommend(Integer id) {
        try {
            log.info("查询用户是否被推荐");
            return userMapper.queryUserIsrecommend(id);
        } catch (Exception e) {
            log.error("查询用户是否被推荐异常", e);
            throw e;
        }
    }

    /**
     * 更新用户推荐
     *
     * @param user
     * @return
     */
    public Integer updateUserByIsrecommend(User user) {
        try {
            log.info("更新用户推荐");
            return userMapper.updateUserByIsrecommend(user);
        } catch (Exception e) {
            log.error("更新用户推荐异常", e);
            throw e;
        }
    }

    /**
     * 更新VIP申请
     *
     * @param map
     * @return
     */
    public Integer updateAuditByUser(Map map) {
        try {
            log.info("更新VIP申请");
            return userMapper.updateAuditByUser(map);
        } catch (Exception e) {
            log.error("更新VIP申请异常", e);
            throw e;
        }
    }
    /**
     * 查询用户详情
     *
     * @param userid
     * @return
     */
    public UserParticulars queryUserParticulars(String userid) {
        try {
            log.info("查询用户详情");
            return userMapper.queryUserParticulars(userid);
        } catch (Exception e) {
            log.error("查询用户详情异常 userid=" + userid, e);
            throw e;
        }
    }

    /**
     * 查询地址
     *
     * @param userid
     * @return
     */
    public List<ProvinceVo> queryProvinces(String userid) {
        try {
            log.info("查询地址");
            return userMapper.queryProvinces(userid);
        } catch (Exception e) {
            log.error("查询地址异常 userid=" + userid, e);
            throw e;
        }
    }

    /**
     * 根据id查询用户信息
     *
     * @param userid
     * @return
     */
    public UserAll queryUserById(Integer userid) {
        try {
            log.info("根据id查询用户信息");
            return userMapper.queryUserById(userid);
        } catch (Exception e) {
            log.error("根据id查询用户信息异常 userid=" + userid, e);
            throw e;
        }
    }

    /**
     * 根据圈子关注查询用户列表
     *
     * @param map
     * @return
     */
    public List<UserAll> queryAttentionUserList(Map map, Paging<UserAll> pager) {
        try {
            log.info("根据圈子关注查询用户列表");
            return userMapper.findAllqueryAttentionUserList(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("根据圈子关注查询用户列表异常", e);
            throw e;
        }
    }

    public void usePoints(Map<String, Object> parammap) {
        try {
            log.info("扣除用户下单使用积分");
            userMapper.deductPoints(parammap);
        } catch (Exception e) {
            log.error("扣除用户下单使用积分失败", e);
            throw e;
        }
    }

    public void updateUserPoints(Map dispointmoney) {
        try {
            log.info("更新用户积分");
            userMapper.updateUserPoints(dispointmoney);
        } catch (Exception e) {
            log.error("更新用户积分失败", e);
            throw e;
        }
    }

    /**
     * 查询用户使用积分
     *
     * @param map
     * @return
     */
    public Integer queryUserUseIntegral(Map map) {
        try {
            log.info("查询用户使用积分");
            return userMapper.queryUserUseIntegral(map);
        } catch (Exception e) {
            log.error("查询用户使用积分异常", e);
            throw e;
        }
    }

    /**
     * 获取该用户的积分
     *
     * @param userid
     * @return
     */
    public int queryUserByRewarde(int userid) {
        try {
            log.info("获取用户的积分");
            return userMapper.queryUserByRewarde(userid);
        } catch (Exception e) {
            log.error("获取该用户的积分异常", e);
            throw e;
        }
    }

    /**
     * 操作用户积分
     *
     * @param map
     */
    public int updateUserPoint(Map map) {
        try {
            log.info("操作用户积分");
            return userMapper.updateUserPoint(map);
        } catch (Exception e) {
            log.error("操作用户积分异常", e);
            throw e;
        }
    }

    public User selectUserByThirdAccount(Map map) {
        try {
            log.info("判断是否存在这个第三方账号");
            return userMapper.selectUserByThirdAccount(map);
        } catch (Exception e) {
            log.error("判断是否存在这个第三方账号失败", e);
            throw e;
        }
    }

    public Integer insertSelective(User user) {
        try {
            log.info("新增APP用户");
            userMapper.insertSelective(user);
            return user.getId();
        } catch (Exception e) {
            log.error("新增APP用户失败", e);
            throw e;
        }
    }

    public LoginUser selectLoginUserByToken(String token) {
        try {
            log.info("根据token查询用户信息");
            return userMapper.selectLoginUserByToken(token);
        } catch (Exception e) {
            log.error("根据token查询用户信息失败", e);
            throw e;
        }
    }

    public LoginUser selectLoginuserByUserid(Integer userid) {
        try {
            log.info("根据用户id查询LoginUser");
            return userMapper.selectLoginuserByUserid(userid);
        } catch (Exception e) {
            log.error("根据用户id查询LoginUser失败", e);
            throw e;
        }
    }

    public User selectByPrimaryKey(Integer userid) {
        try {
            log.info("根据id查询用户信息User");
            return userMapper.selectByPrimaryKey(userid);
        } catch (Exception e) {
            log.error("根据id查询用户信息User失败", e);
            throw e;
        }
    }

    public List<Author> findAllHotAuthor(Paging<Author> pager, Map<String, Object> map){
        try {
            log.info("查询用户未关注过的推荐作者");
            return userMapper.findAllHotAuthor(pager.getRowBounds(), map);
        }catch (Exception e){
            log.error("查询用户未关注过的推荐作者失败", e);
            throw e;
        }
    }

    public List<Author> findAllInterestAuthor(Paging<Author> pager, Map<String, Object> map){
        try {
            log.info("查询用户关注的圈子和关注的标签中的作者（并且这个用户还没关注过他的）");
            return userMapper.findAllInterestAuthor(pager.getRowBounds(), map);
        }catch (Exception e){
            log.error("查询用户关注的圈子和关注的标签中的作者（并且这个用户还没关注过他的）失败", e);
            throw e;
        }
    }

    public void updateUserAttention(Integer userid){
        try {
            log.info("增加用户关注总数");
            userMapper.updateUserAttention(userid);
        }catch (Exception e){
            log.error("增加用户关注总数失败");
            throw e;
        }
    }

    public List<Author> findAllNearAuthor(Paging<Author> pager, Map<String, Object> map){
        try {
            log.info("根据传入的手机定位经纬度查询当前用户周边30公里内的所有作者");
            return userMapper.findAllNearAuthor(pager.getRowBounds(), map);
        }catch (Exception e){
            log.error("根据传入的手机定位经纬度查询当前用户周边30公里内的所有作者失败", e);
            throw e;
        }
    }

    public Boolean isExistSameNickname(String nickname, Integer userid) {
        try {
            log.info("查看是否存在相同的昵称");
            Map map = new HashedMap();
            map.put("nickname", nickname);
            map.put("userid", userid);
            boolean flag = userMapper.countByNickname(map) > 0;
            return flag;
        } catch (Exception e) {
            log.error("查看是否存在相同的昵称失败", e);
            throw e;
        }
    }

    /**
     * 查询用户是否为VIP
     *
     * @param loginid
     * @return
     */
    public Integer queryUserIsVip(String loginid) {
        try {
            log.info("查询用户是否为VIP");
            return userMapper.queryUserIsVip(Integer.parseInt(loginid));
        } catch (Exception e) {
            log.error("查询用户是否为VIP异常", e);
            throw e;
        }
    }

    /**
     * 查询用户角色
     *
     * @param loginid
     * @return
     */
    public UserRole queryUserRole(String loginid) {
        try {
            log.info("查询用户角色");
            return userMapper.queryUserRole(Integer.parseInt(loginid));
        } catch (Exception e) {
            log.error("查询用户角色异常", e);
            throw e;
        }
    }

    public Boolean queryIsExistSameNickname(String nickname) {
        try {
            log.info("判断是否具有重名的nickname, 入参：" + nickname);
            int n = userMapper.queryCountByNickname(nickname);
            return n > 0;
        } catch (Exception e) {
            log.error("判断是否具有重名的nickname", e);
            throw e;
        }
    }

    /**
     * 查询用户对应前台用户id
     *
     * @param user
     * @return
     */
    public Integer queryUserIsCricle(Integer user) {
        try {
            log.info("查询用户对应前台用户id");
            return userMapper.queryUserIsCricle(user);
        } catch (Exception e) {
            log.error("查询用户对应前台用户id异常", e);
            throw e;
        }
    }

    /**
     * 查询当前用户登录的这个APP的最新版本号是多少
     *
     * @param type
     * @return
     */
    public String queryVersion(int type){
        try {
            log.info("判断当前手机用户所使用的APP最新版本号");
            return userMapper.queryVersion(type);
        }catch (Exception e){
            log.error("判断当前手机用户所使用的APP最新版本号失败", e);
            throw e;
        }
    }

    /**
     * 美番2.0发现页的热门作者查询
     */
    public List<UserVo> queryHotUserList(){
        try {
            log.info("2.0发现页的热门作者列表查询");
            return userMapper.queryHotUserList();
        }catch (Exception e){
            log.error("2.0发现页的热门作者列表查询失败", e);
            throw e;
        }
    }

    /**
     * 查询用户名称头像
     *
     * @param userid
     * @return
     */
    public UserLike findUser(int userid) {
        try {
            log.info("查询用户");
            return userMapper.findUser(userid);
        } catch (Exception e) {
            log.error("查询用户失败", e);
            throw e;
        }

    }


    public Integer updateUserHeatValue(Map map) {
        try {
            log.info("修改用户热度");
            return userMapper.updateUserHeatValue(map);
        } catch (Exception e) {
            log.error("修改用户热度失败", e);
            throw e;
        }
    }

    public int queryUserLevel(int userid) {
        try {
            log.info("查询用户等级");
            return userMapper.queryUserLevel(userid);
        } catch (Exception e) {
            log.error("查询用户等级失败", e);
            throw e;
        }
    }

    public int queryUserPoints(int userid) {
        try {
            log.info("查询用户等级");
            return userMapper.queryUserPoints(userid);
        } catch (Exception e) {
            log.error("查询用户等级失败", e);
            throw e;
        }
    }


    public int updateUserLevels(Map map) {
        try {
            log.info("修改用户等级");
            return userMapper.updateUserLevels(map);
        } catch (Exception e) {
            log.error("修改用户等级失败", e);
            throw e;
        }
    }


    public UserVo queryUserInfoHompage(int userid) {
        try {
            log.info("查询用户信息");
            return userMapper.queryUserInfoHompage(userid);
        } catch (Exception e) {
            log.error("查询用户信息失败", e);
            throw e;
        }
    }

    public void accusationUser(Map<String, Object> map){
        try {
            log.info("举报用户用户成功");
            userMapper.accusationUser(map);
        }catch (Exception e){
            log.error("举报用户用户失败", e);
            throw e;
        }
    }

    public List<UserVo> findAllMineFollowAuthor(Map<String, Object> paramap, Paging<UserVo> pager){
        try {
            log.info("查询当前用户关注的所有作者列表");
            return userMapper.findAllMineFollowAuthor(paramap, pager.getRowBounds());
        }catch (Exception e){
            log.error("查询当前用户关注的所有作者列表失败", e);
            throw e;
        }
    }


    /**
     * 查询当前作者有没有被当前登录的用户关注过
     *
     * @param paramap
     * @return
     */
    public int queryIsFollowAuthor(Map<String, Object> paramap){
        try {
            log.info("查询当前作者有没有被当前登录的用户关注过");
            return userMapper.queryIsFollowAuthor(paramap);
        }catch (Exception e){
            log.error("查询当前作者有没有被当前登录的用户关注过 失败", e);
            throw e;
        }
    }

    public List<UserVo> findAllMineFans(Map<String, Object> parammap, Paging<UserVo> pager){
        try {
            log.info("查询当前用户的粉丝列表");
            return userMapper.findAllMineFans(parammap, pager.getRowBounds());
        }catch (Exception e){
            log.error("查询当前用户的粉丝列表失败", e);
            throw e;
        }
    }

    public List<UserVo> findAllMostFansAuthorInAll(Paging<UserVo> paging) {
        try {
            log.info("查询粉丝数最多的作者集合");
            return userMapper.findAllMostFansAuthorInAll(paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询粉丝数最多的作者集合失败", e);
            throw e;
        }
    }

    public List<UserVo> findAllMostFansAuthorInCurrentMonth(Paging<UserVo> paging) {
        try {
            log.info("查询当月粉丝数最多的作者集合");
            return userMapper.findAllMostFansAuthorInCurrentMonth(paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询当月粉丝数最多的作者集合失败", e);
            throw e;
        }
    }

    public List<UserVo> findAllMostCommentAuthorInAll(Paging<UserVo> paging) {
        try {
            log.info("查询评论最多的作者集合");
            return userMapper.findAllMostCommentAuthorInAll(paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询评论最多的作者集合失败", e);
            throw e;
        }
    }

    public List<UserVo> findAllMostCommentAuthorInCurrentMonth(Paging<UserVo> paging) {
        try {
            log.info("查询当月评论最多的作者集合");
            return userMapper.findAllMostCommentAuthorInCurrentMonth(paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询当月评论最多的作者集合失败", e);
            throw e;
        }
    }


    public List<UserVo> findAllMostPostAuthorInAll(Paging<UserVo> paging) {
        try {
            log.info("查询发帖最多的作者集合");
            return userMapper.findAllMostPostAuthorInAll(paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询发帖最多的作者集合失败", e);
            throw e;
        }
    }


    public List<UserVo> findAllMostPostAuthorInCurrentMonth(Paging<UserVo> paging) {
        try {
            log.info("查询当月发帖最多的作者集合");
            return userMapper.findAllMostPostAuthorInCurrentMonth(paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询当月发帖最多的作者集合失败", e);
            throw e;
        }
    }

    public int queryInviteNum(int userid){
        try {
            log.info("查询当前登录用户已邀请人数");
            return userMapper.queryInviteNum(userid);
        }catch (Exception e){
            log.error("查询当前登录用户已邀请人数失败", e);
            throw e;
        }
    }

    public int queryFinishUserInfo(int userid){
        try {
            log.info("查询当前用户个人资料是否完整");
            return userMapper.queryFinishUserInfo(userid);
        }catch (Exception e){
            log.error("查询当前用户个人资料是否完整失败", e);
            throw e;
        }
    }

    public int getfootmap(int userid){
        try {
            log.info("获取当前用户的足迹点总数");
            return userMapper.getfootmap(userid);
        }catch (Exception e){
            log.error("获取当前用户的足迹点总数失败", e);
            throw e;
        }
    }

    public List<InviteUserVo> findAllMyInviteList(int userid, Paging<InviteUserVo> pager){
        try {
            log.info("查询当前用户邀请的好友列表");
            return userMapper.findAllMyInviteList(userid, pager.getRowBounds());
        }catch (Exception e){
            log.error("查询当前用户邀请的好友列表失败");
            throw e;
        }
    }

    public List<InviteUserVo> findAllInviteRank(Paging<InviteUserVo> pager){
        try {
            log.info("查询邀请好友排行榜列表");
            return userMapper.findAllInviteRank(pager.getRowBounds());
        }catch (Exception e){
            log.error("查询邀请好友排行榜列表失败");
            throw e;
        }
    }

    public List<User> queryUserByName(String name) {
        try {
            log.info("根据昵称查询用户");
            return userMapper.queryUserByName(name);
        } catch (Exception e) {
            log.error("根据昵称查询用户失败", e);
            throw e;
        }
    }

    public List<User> findAllUserByName(Paging<User> paging, Map map) {
        try {
            log.info("分页，根据名称查找用户");
            return userMapper.findAllUserByName(map, paging.getRowBounds());
        } catch (Exception e) {
            log.error("分页，根据名称查找用户失败", e);
            throw e;
        }
    }


    public String areaname(String code) {
        try {
            log.info("所在城市");
            return userMapper.areaname(code);
        } catch (Exception e) {
            log.error("所在城市失败", e);
            throw e;
        }
    }

    public String provicename(String citycode) {
        try {
            log.info("所在城市");
            return userMapper.provicename(citycode);
        } catch (Exception e) {
            log.error("所在城市失败", e);
            throw e;
        }
    }

    public Map selectIntervalBetweenLoginAndRegiste(Map map) {
        try {
            log.info("查询当前用户登录和注册之间的时间间隔秒数");
            return userMapper.selectIntervalBetweenLoginAndRegiste(map);
        } catch (Exception e) {
            log.error("查询当前用户登录和注册之间的时间间隔秒数失败", e);
            throw e;
        }
    }

    public Integer selectMaxRobotId() {
        try {
            log.info("查找最大的机器人id");
            return userMapper.selectMaxRobotId();
        } catch (Exception e) {
            log.error("查找最大的机器人id失败", e);
            throw e;
        }
    }

    public List<User> selectRobotUser() {
        try {
            log.info("查询所有机器人用户");
            return userMapper.selectRobotUser();
        } catch (Exception e) {
            log.error("查询所有机器人用户失败", e);
            throw e;
        }
    }


    /**
     * 查询机器人列表
     *
     * @param name
     * @param pag
     * @return
     */
    public List<User> findAllQueryRobotByList(String name, Paging<User> pag) {
        try {
            log.info("查询机器人列表");
            return userMapper.findAllQueryRobotByList(name, pag.getRowBounds());
        } catch (Exception e) {
            log.error("查询机器人列表失败", e);
            throw e;
        }
    }

    public User queryRobotById(Integer id) {
        try {
            log.info("根据id查询机器人详情");
            return userMapper.queryRobotById(id);
        } catch (Exception e) {
            log.error("根据id查询机器人详情异常", e);
            throw e;
        }
    }

    /**
     * 查询随机用户
     *
     * @param number
     * @return
     */
    public List<User> queryRandomUser(Integer number) {
        try {
            log.info("随机查询机器人");
            return userMapper.queryRandomUser(number);
        } catch (Exception e) {
            log.error("随机查询机器人异常", e);
            throw e;
        }
    }


    public List<User> queryNotRepeatCollectRobots(Map map) {
        try {
            log.info("查询不重复收藏帖子的机器人用户");
            return userMapper.queryNotRepeatCollectRobots(map);
        } catch (Exception e) {
            log.error("查询不重复收藏帖子的机器人用户", e);
            throw e;
        }
    }


    public List<User> queryNotRepeatZanRobots(Map map) {
        try {
            log.info("查询不重复点赞帖子的机器人用户");
            return userMapper.queryNotRepeatZanRobots(map);
        } catch (Exception e) {
            log.error("查询不重复点赞帖子的机器人用户", e);
            throw e;
        }
    }


    public List<User> queryNotRepeatFollowRandomRobots(Map map) {
        try {
            log.info("查询不重复关注的用户机器人用户");
            return userMapper.queryNotRepeatRandomRobots(map);
        } catch (Exception e) {
            log.error("查询不重复关注的用户机器人用户", e);
            throw e;
        }
    }


    /**
     * 随机查询头像
     *
     * @param number
     * @return
     */
    public List<UserPhoto> queryUserPhotos(Integer number) {
        try {
            log.info("随机查询头像");
            return userPhotoMapper.queryUserPhonts(number);
        } catch (Exception e) {
            log.error("随机查询用户头像异常", e);
            throw e;
        }
    }

    /**
     * 更新机器人信息
     *
     * @param user
     */
    public void updateUserByMessager(User user) {
        try {
            log.info("更新机器人信息");
            userMapper.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            log.error("更新机器人信息异常", e);
            throw e;
        }
    }

    public List<User> selectNotLoadPhotoUser() {
        try {
            log.info("查询未加载的图片的用户");
            return userMapper.selectNotLoadPhotoUser();
        } catch (Exception e) {
            log.error("查询未加载的图片的用户失败", e);
            throw e;
        }
    }

    /**
     * 默认查询7日活跃数
     *
     * @param map
     * @return
     */
    public Double queryUserBriskNumber(Map map) {
        try {
            log.info("默认查询7日活跃数");
            return userMapper.queryUserBriskNumber(map);
        } catch (Exception e) {
            log.error("默认查询7日活跃数异常", e);
            throw e;
        }
    }

    /**
     * 按时间查询活跃用户
     *
     * @param map
     * @return
     */
    public List<User> dauStatistic(Map map) {
        try {
            log.info("按时间查询活跃用户");
            return userMapper.dauStatistic(map);
        } catch (Exception e) {
            log.error("按时间查询活跃用户异常", e);
            throw e;
        }
    }

    public int queryFollow(int id) {
        try {
            log.info("查询该使用是否关注过圈子标签或作者");
            return userMapper.queryFollow(id);
        } catch (Exception e) {
            log.error("查询该使用是否关注过圈子标签或作者失败", e);
            throw e;
        }
    }

    public int queryPost(int id) {
        try {
            log.info("查询该用户前一天是否发过帖子");
            return userMapper.queryPost(id);
        } catch (Exception e) {
            log.error("查询该用户前一天是否发过帖子失败", e);
            throw e;
        }
    }

    public int queryZan(int id) {
        try {
            log.info("查询该用户前一天中是否点赞过评论或帖子");
            return userMapper.queryZan(id);
        } catch (Exception e) {
            log.error("查询该用户前一天中是否点赞过评论或帖子", e);
            throw e;
        }
    }

    public int queryCollect(int id) {
        try {
            log.info("查询该用户前一天中是否收藏过帖子");
            return userMapper.queryCollect(id);
        } catch (Exception e) {
            log.error("查询该用户前一天中是否收藏过帖子失败", e);
            throw e;
        }
    }

    public int queryComment(int id) {
        try {
            log.info("查询该用户前一天中是否评论过帖子或回复过评论");
            return userMapper.queryComment(id);
        } catch (Exception e) {
            log.error("查询该用户前一天中是否评论过帖子或回复过评论失败", e);
            throw e;
        }
    }

    public int queryForward(int id) {
        try {
            log.info("查询该用户前一天中是否转发过帖子");
            return userMapper.queryForward(id);
        } catch (Exception e) {
            log.error("查询该用户前一天中是否转发过帖子失败", e);
            throw e;
        }
    }
}

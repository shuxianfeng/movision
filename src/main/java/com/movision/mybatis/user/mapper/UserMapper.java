package com.movision.mybatis.user.mapper;

import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.province.entity.ProvinceVo;
import com.movision.mybatis.user.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    List<Author> findAllHotAuthor(RowBounds rowBounds, Map<String, Object> map);

    List<Author> findAllInterestAuthor(RowBounds rowBounds, Map<String, Object> map);

    void updateUserAttention(Integer userid);

    List<Author> findAllNearAuthor(RowBounds rowBounds, Map<String, Object> map);

    int updateByPrimaryKeySelective(User record);

    int updateRegisterUser(RegisterUser registerUser);

    int updateByPrimaryKey(User record);

    User selectByPhone(User user);

    void updateUserByNickname(User user);

    LoginUser selectLoginUserByPhone(User user);

    void insertPostShare(Map<String, Object> parammap);

    void insertGoodsShare(Map<String, Object> parammap);

    int queryUserByPoints(int id);

    int isExistAccount(@Param("phone") String phone);

    int registerAccount(RegisterUser registerUser);

    UserVo queryUserInfo(int userid);

    User queryCircleMasterByPhone(String phone);

    List<User> queryCircleManagerByCircleid(Integer circleid);

    List<String> queryCircleMangerByUseridList(Map map);

    List<String> queryCircleManagerByCircleList(Integer circleid);

    String queryUserByOpenid(Integer userid);

    String queryUserByNickname(Integer userid);

    String queryUserByNicknameBy(Integer postid);
    User queryUser(String phone);

    List<UserLike> findAlllikeQueryPostByNickname(String name);

    int queryUserPoint(int userid);

    User findAllUser(int id);

    String queryUserbyPhoneByUserid(Integer userid);

    List<UserVo> findAllqueryUsers(RowBounds rowBounds);

    List<BossUser> findAllQueryIssuePostManList();

    List<User> findAllQueryCircleManList(@Param(value = "nickname") String nickname);

    List<UserVo> findAllQueryByConditionvipList(SearchUser searchUser, RowBounds rowBounds);

    List<UserVo> findAllqueryAddVSortUser(Map map, RowBounds rowBounds);

    List<UserVo> findAllQueryUniteConditionByApply(Map map, RowBounds rowBounds);

    List<UserVo> findAllQueryUserExamineAndVerify(Map map, RowBounds rowBounds);

    List<UserAll> findAllqueryAllUserList(Map map, RowBounds rowBounds);

    Integer queryAllTotal(Map map);

    int deleteUserByid(Map map);

    int deleteUserLevl(Map map);

    Integer queryUserIsrecommend(Integer id);

    Integer updateUserByIsrecommend(User user);

    Integer updateAuditByUser(Map map);

    UserParticulars queryUserParticulars(String userid);

    List<ProvinceVo> queryProvinces(String userid);

    UserAll queryUserById(Integer userid);

    List<UserAll> findAllqueryAttentionUserList(Map map, RowBounds rowBounds);

    void deductPoints(Map<String, Object> parammap);

    void updateUserPoints(Map dispointmoney);

    Integer queryUserUseIntegral(Map map);

    int queryUserByRewarde(int userid);

    int updateUserPoint(Map map);

    User selectUserByThirdAccount(Map map);

    LoginUser selectLoginUserByToken(@Param("token") String token);

    LoginUser selectLoginuserByUserid(@Param("id") Integer id);

    Integer countByNickname(Map map);

    Integer queryUserIsVip(Integer loginid);

    UserRole queryUserRole(Integer loginid);

    Integer queryCountByNickname(@Param("nickname") String nickname);

    Integer queryUserIsCricle(@Param("userid") Integer userid);

    String queryVersion(Integer type);

    List<UserVo> queryHotUserList();

    UserLike findUser(int userid);

    Integer updateUserHeatValue(Map map);

    int queryUserLevel(int userid);

    int queryUserPoints(int userid);

    int updateUserLevels(Map map);

    UserVo queryUserInfoHompage(int userid);

    void accusationUser(Map<String, Object> map);

    List<UserVo> findAllMineFollowAuthor(Map<String, Object> paramap, RowBounds rowBounds);

    int queryIsFollowAuthor(Map<String, Object> paramap);

    List<UserVo> findAllMineFans(Map<String, Object> parammap, RowBounds rowBounds);

    List<UserVo> findAllMostFansAuthorInAll(RowBounds rowBounds);

    List<UserVo> findAllMostFansAuthorInCurrentMonth(RowBounds rowBounds);

    List<UserVo> findAllMostCommentAuthorInAll(RowBounds rowBounds);

    List<UserVo> findAllMostCommentAuthorInCurrentMonth(RowBounds rowBounds);

    List<UserVo> findAllMostPostAuthorInAll(RowBounds rowBounds);

    List<UserVo> findAllMostPostAuthorInCurrentMonth(RowBounds rowBounds);

    int queryInviteNum(int userid);

    int queryFinishUserInfo(int userid);

    int getfootmap(int userid);

    List<InviteUserVo> findAllMyInviteList(int userid, RowBounds rowBounds);

    List<InviteUserVo> findAllInviteRank(RowBounds rowBounds);

    List<User> queryUserByName(String nickname);

    List<User> findAllUserByName(Map<String, Object> map, RowBounds rowBounds);

    String areaname(String code);

    String provicename(String citycode);

    Map selectIntervalBetweenLoginAndRegiste(Map map);

    Integer selectMaxRobotId();

    List<User> selectRobotUser();

    List<User> findAllQueryRobotByList(String name, RowBounds rowBounds);

    User queryRobotById(Integer id);

    List<User> queryRandomUser(Integer number);

    List<User> queryNotRepeatCollectRobots(Map map);

    List<User> queryNotRepeatZanRobots(Map map);

    List<User> queryNotRepeatRandomRobots(Map map);

    List<User> selectNotLoadPhotoUser();

    UserAll queryUserStatistics(Integer id);

    Double queryUserBriskNumber(Map map);

    List<User> dauStatistic(Map map);

    int queryComment(int id);

    int queryForward(int id);

    int queryCollect(int id);

    int queryPost(int id);

    int queryZan(int id);

    int queryFollow(int id);

    List<UserLike> queryCircleaAministratorListById(Integer circleid);
}
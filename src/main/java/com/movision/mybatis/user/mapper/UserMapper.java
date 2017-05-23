package com.movision.mybatis.user.mapper;

import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.province.entity.ProvinceVo;
import com.movision.mybatis.submission.entity.SubmissionVo;
import com.movision.mybatis.user.entity.*;
import com.movision.utils.L;
import org.apache.ibatis.annotations.Param;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateRegisterUser(RegisterUser registerUser);

    int updateByPrimaryKey(User record);

    User selectByPhone(User user);

    LoginUser selectLoginUserByPhone(User user);

    void insertPostShare(Map<String, Object> parammap);

    void insertGoodsShare(Map<String, Object> parammap);

    int updateUserPointsAdd(Map mapadd);

    int updateUserPointsMinus(Map map);

    int queryUserByPoints(int id);

    int isExistAccount(@Param("phone") String phone);

    int registerAccount(RegisterUser registerUser);

    UserVo queryUserInfo(int userid);

    User queryCircleMasterByPhone(String phone);

    List<User> queryCircleManagerList(int categoryid);

    List<User> queryCircleManagerByCircleid(Integer circleid);

    List<User> queryCircleMangerByUseridList(Map map);

    List<User> queryCircleManagerByCircleList(Integer circleid);

    List<User> selectAllUser();

    int isAppAdminUser(@Param("phone") String phone);

    String queryUserByOpenid(Integer userid);

    String queryUserByNickname(Integer userid);

    String queryUserByNicknameBy(Integer postid);
    User queryUser(String phone);

    List<UserLike> findAlllikeQueryPostByNickname(Map name);

    int queryUserPoint(int userid);

    User findAllUser(int id);

    String queryUserbyPhoneByUserid(Integer userid);

    List<UserVo> findAllqueryUsers(RowBounds rowBounds);

    List<UserVo> findAllqueryUserVIPByList(RowBounds rowBounds);

    List<BossUser> findAllQueryIssuePostManList();

    List<User> findAllQueryCircleManList();

    List<UserVo> findAllQueryByConditionvipList(Map map, RowBounds rowBounds);

    List<UserVo> findAllqueryAddVSortUser(Map map, RowBounds rowBounds);

    List<UserVo> findAllQueryUniteConditionByApply(Map map, RowBounds rowBounds);

    List<UserVo> findAllQueryUserExamineAndVerify(Map map, RowBounds rowBounds);

    List<UserAll> findAllqueryAllUserList(RowBounds rowBounds, Map map);

    int deleteUserByid(Map map);

    int deleteUserLevl(Map map);

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

}
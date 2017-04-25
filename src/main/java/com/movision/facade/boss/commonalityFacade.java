package com.movision.facade.boss;

import com.movision.common.constant.JurisdictionConstants;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.bossUser.entity.BossUserVo;
import com.movision.mybatis.bossUser.service.BossUserService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于权限认证
 *
 * @Author zhurui
 * @Date 2017/4/5 11:46
 */
@Service
public class commonalityFacade {

    @Autowired
    private BossUserService bossUserService;


    /**
     * @param userid    登录用户
     * @param operation 操作(增删改查)
     * @param kind      操作种类(帖子，评论)
     * @param kindid    种类id
     * @return
     */
    public Map verifyUserJurisdiction(Integer userid, Integer operation, Integer kind, Integer kindid) {
        Map map = new HashMap();
        BossUser i = bossUserService.queryUserByAdministrator(userid);//根据登录用户id查询当前用户有哪些权限
        if (i.getIscircle().equals(JurisdictionConstants.JURISDICTION_TYPE.groupOwner.getCode())) {//圈主
            if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.add.getCode()) && kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode())) {//圈主可以在自己圈子中添加评论
                Integer resault = bossUserService.queryCircleIdToCircle(kindid);//判断需要添加的帖子是否在本圈内
                if (resault != null) {
                    if (resault.equals(userid)) {
                        map.put("resault", 1);
                        return map;
                    } else {
                        map.put("resault", -1);
                        map.put("message", "权限不足");
                        return map;
                    }
                } else {
                    map.put("resault", -1);
                    map.put("message", "权限不足");
                    return map;
                }
            } else if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.add.getCode()) && kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())) {//添加评论
                map.put("resault", 2);
                return map;
            } else if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.delete.getCode()) && (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode()) || (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())))) {//圈主可以删除该圈子的帖子和评论
                Map ma = new HashedMap();
                if (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode())) {//帖子
                    ma.put("userid", userid);
                    //根据操作类型id查询此操作是否属于该用户范畴
                    Integer list = bossUserService.queryPostByUserid(ma);
                    if (list > 0) {
                        map.put("resault", 1);
                        return map;
                    } else {
                        map.put("resault", -1);
                        map.put("message", "权限不足");
                        return map;
                    }
                } else if (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())) {//圈主删除帖子评论
                    ma.put("kindid", kindid);//评论id
                    //查出评论是否属于圈主的帖子评论
                    Integer list = bossUserService.queryCircleManageCommentByUserid(ma);
                    if (list != null) {
                        if (list.equals(userid)) {
                            map.put("resault", 1);
                            return map;
                        } else {
                            map.put("resault", -1);
                            map.put("resault", "权限不足");
                            return map;
                        }
                    } else {
                        map.put("resault", -1);
                        map.put("message", "权限不足");
                        return map;
                    }
                }
            } else if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.update.getCode()) && (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode()) || kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode()))) {//圈主可以编辑改圈子中的本人帖子
                Map ma = new HashedMap();
                if (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode())) {//帖子
                    ma.put("userid", userid);
                    ma.put("kindid", kindid);
                    //根据操作类型id查询此操作是否属于该用户范畴
                    Integer list = bossUserService.queryPostByUserid(ma);
                    if (list > 0) {
                            map.put("resault", 1);
                            return map;
                    } else {//代表最后一条也不匹配
                            map.put("resault", -1);
                            return map;
                        }
                } else if (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())) {//帖子评论
                    ma.put("kindid", kindid);
                    //查询出评论发布人
                    Integer list = bossUserService.queryCircleManageCommentByUserid(ma);
                    if (list != null) {
                        if (list.equals(userid)) {//判断发布人和登录用户是否是同一人
                            map.put("resault", 1);
                            return map;
                        } else {
                            map.put("resault", -1);
                            map.put("resault", "权限不足");
                            return map;
                        }
                    } else {
                        map.put("resault", -1);
                        map.put("message", "权限不足");
                        return map;
                    }
                } else {
                    map.put("resault", -1);
                    return map;
                }
            } else {
                map.put("resault", -1);
                return map;
            }
        } else if (i.getCirclemanagement().equals(JurisdictionConstants.JURISDICTION_TYPE.groupManage.getCode())) {//圈子管理员
            if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.add.getCode()) && kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode())) {//圈主可以在自己圈子中添加评论
                Map map1 = new HashMap();
                map1.put("userid", userid);
                map1.put("kindid", kindid);
                Integer resault = bossUserService.queryCircleManageIdToCircle(map1);//判断需要添加的帖子是否在本圈内
                if (resault != null) {
                    if (resault.equals(userid)) {
                        map.put("resault", 1);
                        return map;
                    } else {
                        map.put("resault", -1);
                        map.put("message", "权限不足");
                        return map;
                    }
                } else {
                    map.put("resault", -1);
                    map.put("message", "权限不足");
                    return map;
                }
            } else if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.add.getCode()) && kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())) {//添加评论
                map.put("resault", 2);
                return map;
            } else if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.delete.getCode()) && (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode()) || (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())))) {//圈主可以删除该圈子的帖子和评论
                Map ma = new HashedMap();
                if (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode())) {//帖子
                    ma.put("userid", userid);
                    //根据操作类型id查询此操作是否属于该用户范畴
                    Integer list = bossUserService.queryPostByUserid(ma);
                    if (list > 0) {
                        map.put("resault", 1);
                        return map;
                    } else {
                        map.put("resault", -1);
                        map.put("message", "权限不足");
                        return map;
                    }
                } else if (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())) {//圈主删除帖子评论
                    ma.put("kindid", kindid);//评论id
                    //查出评论是否属于圈主的帖子评论
                    Integer list = bossUserService.queryCircleManageCommentByUserid(ma);
                    if (list != null) {
                        if (list.equals(userid)) {
                            map.put("resault", 1);
                            return map;
                        } else {
                            map.put("resault", -1);
                            map.put("resault", "权限不足");
                            return map;
                        }
                    } else {
                        map.put("resault", -1);
                        map.put("message", "权限不足");
                        return map;
                    }
                }
            } else if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.update.getCode()) && (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode()) || kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode()))) {//圈主可以编辑改圈子中的本人帖子
                Map ma = new HashedMap();
                if (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode())) {//帖子
                    ma.put("userid", userid);
                    //根据操作类型id查询此操作是否属于该用户范畴
                    Integer list = bossUserService.queryPostByUserid(ma);
                    if (list > 0) {
                            map.put("resault", 1);
                            return map;
                    } else {//代表最后一条也不匹配
                            map.put("resault", -1);
                            return map;
                        }
                } else if (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())) {//帖子评论
                    ma.put("kindid", kindid);
                    //查询出是否属于圈主的帖子评论
                    Integer list = bossUserService.queryCircleManageCommentByUserid(ma);
                    if (list != null) {
                        if (list.equals(userid)) {
                            map.put("resault", 1);
                            return map;
                        } else {
                            map.put("resault", -1);
                            map.put("resault", "权限不足");
                            return map;
                        }
                    } else {
                        map.put("resault", -1);
                        map.put("message", "权限不足");
                        return map;
                    }
                } else {
                    map.put("resault", -1);
                    return map;
                }
            } else {
                map.put("resault", -1);
                return map;
            }
        } else if (i.getContributing().equals(JurisdictionConstants.JURISDICTION_TYPE.speciallyInvite.getCode())) {//特邀嘉宾
            //只能设置帖子为精选池和评论帖子
            if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.add.getCode()) && kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())) {//添加评论
                map.put("resault", 2);
                return map;
            } else if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.delete.getCode()) && kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())) {//删除评论
                //查询此评论是否是特邀嘉宾的评论
                Integer flg = bossUserService.querySpeciallyCommentByUserid(kindid);
                if (userid.equals(flg)) {//判断是否是当前特约嘉宾的评论
                    map.put("resault", 1);
                    return map;
                } else {
                    map.put("resault", -1);
                    map.put("message", "权限不足");
                    return map;
                }
            } else if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.update.getCode()) && kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode())) {//操作帖子加入精选池
                map.put("resault", 1);
                return map;
            } else if (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.update.getCode()) && kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())) {
                map.put("resault", 1);
                return map;
            } else {
                map.put("resault", -1);
                return map;
            }
        } else if (i.getIssuper().equals(1) || i.getCommon().equals(1) ||
                operation.equals(JurisdictionConstants.JURISDICTION_TYPE.commentAudit.getCode()) ||
                (operation.equals(JurisdictionConstants.JURISDICTION_TYPE.update.getCode()) && kind.equals(JurisdictionConstants.JURISDICTION_TYPE.circle.getCode()))) {//操作权限为最高权限,可以审核,可以修改圈子
            map.put("resault", 1);
            return map;
        } else {
            map.put("resault", -1);
            return map;
        }
        return map;
    }


    /**
     * 登录用户查询校验
     *
     * @param userid
     * @param operation
     * @param kind
     * @param kindid
     * @return
     */
    public Map verifyUserByQueryMethod(Integer userid, Integer operation, Integer kind, Integer kindid) {
        Map map = new HashMap();
        BossUser i = bossUserService.queryUserByAdministrator(userid);//根据登录用户id查询当前用户有哪些权限
        if (i.getIscircle().equals(JurisdictionConstants.JURISDICTION_TYPE.groupOwner.getCode())//圈主
                && (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode()) || kind.equals(JurisdictionConstants.JURISDICTION_TYPE.circle.getCode()) || kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())
                || kind.equals(JurisdictionConstants.JURISDICTION_TYPE.circleType.getCode()))) {//圈主可以查看
            map.put("resault", 1);
            return map;
        } else if (i.getCirclemanagement().equals(JurisdictionConstants.JURISDICTION_TYPE.groupManage.getCode())//圈子管理员
                && (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode()) || kind.equals(JurisdictionConstants.JURISDICTION_TYPE.circle.getCode()) || kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode())
                || kind.equals(JurisdictionConstants.JURISDICTION_TYPE.circleType.getCode()))) {//圈子管理员可以查看
            map.put("resault", 1);
            return map;
        } else if (i.getContributing().equals(JurisdictionConstants.JURISDICTION_TYPE.speciallyInvite.getCode())//特邀嘉宾
                && (kind.equals(JurisdictionConstants.JURISDICTION_TYPE.post.getCode()) || kind.equals(JurisdictionConstants.JURISDICTION_TYPE.comment.getCode()) || kind.equals(JurisdictionConstants.JURISDICTION_TYPE.circleType.getCode()))) {//特邀嘉宾可以查看
            map.put("resault", 0);
            return map;
        } else if (i.getIssuper().equals(1) || i.getCommon().equals(1) || kind.equals(JurisdictionConstants.JURISDICTION_TYPE.choicenesspool.getCode())) {//操作权限为最高权限
            map.put("resault", 2);
            return map;
        } else {
            map.put("resault", -1);
            return map;
        }
    }
}

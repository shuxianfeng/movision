package com.movision.common.constant;

/**
 * @Author zhurui
 * @Date 2017/4/5 14:39
 */
public class JurisdictionConstants {

    public enum JURISDICTION_TYPE {

        add(1),//添加

        update(2),//编辑

        select(3),//查看

        delete(4),//删除

        circle(1),//圈子

        post(2),//帖子

        choicenesspool(10),//精选池

        comment(3),//评论

        circleType(4),//圈子分类

        issuper(1),//超管/普管

        groupOwner(1),//圈主

        groupManage(1),//圈子管理员

        speciallyInvite(1),//特邀嘉宾

        commentAudit(5),//评论审核

        circleAudit(6);//圈子审核

        public final int code;

        JURISDICTION_TYPE(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return String.valueOf(this.code);
        }
    }
}

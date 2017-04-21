package com.movision.common.constant;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/1 13:51
 */
public class PointConstant {


    /**
     * 获得积分的类型
     */
    public enum POINT_TYPE {
        new_user_register(1),   //新用户注册：1手机注册 2第三方注册
        finish_personal_data(2),    //完善个人资料
        binding_phone(3),   //绑定手机号：1手机注册 2第三方账号登录绑定手机号
        first_focus(4), //首次关注
        first_collect(5),   //首次收藏
        //        first_share(6),
        //        first_comment(7),
        first_support(8),   //首次点赞
        //        first_post(9),
        comment_app(10),    //评价APP
        sign(11),   //签到--每人每天只能签到一次
        reward(12), //打赏
        post(13),   //发帖(指的是APP的发帖)
        comment(14),    //评论
        share(15),  //分享
        index_selected(16), //首页精选
        circle_selected(17),    //帖子精选
        place_order(18);    //下单（并成功付款）--在下单接口中处理了

        public final int code;

        POINT_TYPE(int code) {
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

    /**
     * 获得打赏积分的类型
     */
    public enum REWARD_TYPE {

        reward_10(23),
        reward_20(24),
        reward_50(25),
        reward_100(26),
        reward_233(27),
        reward_666(28);
        public final int code;

        REWARD_TYPE(int code) {
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

    /**
     * 帖子打赏对应积分
     */
    public enum REWARD_POINT {

        point_10(10),
        point_20(20),
        point_50(50),
        point_100(100),
        point_233(233),
        point_666(666);

        public final int code;

        REWARD_POINT(int code) {
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

    /**
     * 增加积分
     */
    public static Integer POINT_ADD = 0;
    /**
     * 减少积分
     */
    public static Integer POINT_DECREASE = 1;

    /**
     * 积分类型对应的积分
     */
    public enum POINT {
        new_user_register(25),
        finish_personal_data(15),
        binding_phone(10),
        first_focus(10),
        first_collect(5),
        first_share(20),
        first_comment(10),
        first_support(5),
        first_post(20),
        comment_app(100),
        sign(5),
        reward(5),
        post(5),
        comment(2),
        share(5),
        index_selected(40),
        circle_selected(20);
//        place_order();    //下单

        public final int code;

        POINT(int code) {
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


    public static void main(String[] args) {
        System.out.println(POINT_TYPE.binding_phone.getCode() == 3);
    }
}

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
        new_user_register(1),
        finish_personal_data(2),
        binding_phone(3),
        first_focus(4),
        first_collect(5),
        //        first_share(6),
//        first_comment(7),
        first_support(8),
        //        first_post(9),
        comment_app(10),
        sign(11),
        reward(12),
        post(13),
        comment(14),
        share(15),
        index_selected(16),
        circle_selected(17),
        place_order(18);    //下单

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

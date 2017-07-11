package com.movision.common.constant;

/**
 * @Author zhanglei
 * @Date 2017/7/11 10:46
 */
public class HeatValueConstant {

    /**
     * 获得热度的类型
     */
    public enum HEATVALUE_TYPE {
        home_page_selection(1),//首页精选
        post_selection(2),//帖子精选
        post_level(3),//发帖人级别
        zan_number(4),//点赞数
        comments_number(5),//评论数
        forwarding_number(6),//转发数
        collection_number(7);//收藏数

        public final int code;

        HEATVALUE_TYPE(int code) {
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
     * 热度类型对应的积分
     */
    public enum POINT {
        home_page_selection(30),//首页精选
        post_selection(20),//帖子精选
        post_level(10),//发帖人级别
        zan_number(5),//点赞数
        comments_number(5),//评论数
        forwarding_number(5),//转发数
        collection_number(5);//收藏数

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
        System.out.println(HEATVALUE_TYPE.comments_number.getCode() == 5);
    }


}

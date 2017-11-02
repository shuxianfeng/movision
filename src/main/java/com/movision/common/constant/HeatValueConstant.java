package com.movision.common.constant;

/**
 * @Author zhanglei
 * @Date 2017/7/11 10:46
 */
public class HeatValueConstant {

    /**
     * 获得热度的类型，对应yw_type_constant表type=2时的数据
     */
    public enum HEATVALUE_TYPE {
        //帖子热度操作
        home_page_selection(1),//首页精选
        circle_selection(2),//帖子精选(圈子精选)
        zan_number(3),//点赞数
        comments_number(4),//评论数
        forwarding_number(5),//转发数
        collection_number(6),//收藏数
        reward_post(11),//打赏帖子
        view_post(12),//帖子浏览数
        decay(15),  //帖子热度的每日衰减

        //用户热度
        fan_count(7),//粉丝数
        posts_count(8),//发帖数
        //评论的热度
        comment_zan_count(9),//評論的點贊數
        reply_comment_number(10),//回复评论数

        //标签热度
        attention_label(13),//关注标签
        using_label(14);//帖子中使用标签

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
        home_page_selection(400000),//首页精选
        circle_selection(50000),//帖子精选（圈子精选）
        zan_number(30),//点赞数
        comments_number(40),//评论数
        forwarding_number(80),//转发数
        collection_number(50),//收藏数
        reward_post(40),//打赏帖子
        view_post(5),//帖子浏览数

        //用户热度
        fan_count(20),//粉丝数
        posts_count(50),//发帖数
        //评论的热度
        comment_zan_count(5),//評論的點贊數
        reply_comment_number(10),//回复评论数

        //标签热度
        attention_label(50),//关注标签
        using_label(30);//发帖使用标签
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

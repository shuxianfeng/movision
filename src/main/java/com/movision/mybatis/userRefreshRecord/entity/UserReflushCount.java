package com.movision.mybatis.userRefreshRecord.entity;

import org.springframework.data.annotation.Id;

import java.util.Comparator;

/**
 * @Author zhanglei
 * @Date 2017/7/19 11:50
 */
public class UserReflushCount {

    @Id
    private Integer postid; //帖子id
    private Integer count;  //帖子浏览次数

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UserReflushCount(Integer postid, Integer count) {
        this.postid = postid;
        this.count = count;
    }

    public static Comparator countComparator = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            UserReflushCount u1 = (UserReflushCount) o1;
            UserReflushCount u2 = (UserReflushCount) o2;

            return (u2.getCount() < u1.getCount() ? -1 :
                    (u2.getCount() == u1.getCount() ? 0 : 1));
        }

    };


    @Override
    public String toString() {
        return "UserReflushCount{" +
                "postid=" + postid +
                ", count=" + count +
                '}';
    }
}

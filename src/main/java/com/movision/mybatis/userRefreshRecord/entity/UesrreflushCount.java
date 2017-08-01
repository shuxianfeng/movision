package com.movision.mybatis.userRefreshRecord.entity;

import org.springframework.data.annotation.Id;

import java.util.Comparator;

/**
 * @Author zhanglei
 * @Date 2017/7/19 11:50
 */
public class UesrreflushCount {

    @Id
    private Integer postid;
    private Integer count;

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

    public UesrreflushCount(Integer postid, Integer count) {
        this.postid = postid;
        this.count = count;
    }

    public UesrreflushCount() {
    }

    public static Comparator countComparator = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            UesrreflushCount u1 = (UesrreflushCount) o1;
            UesrreflushCount u2 = (UesrreflushCount) o2;

            return (u2.getCount() < u1.getCount() ? -1 :
                    (u2.getCount() == u1.getCount() ? 0 : 1));
        }

    };


    @Override
    public String toString() {
        return "UesrreflushCount{" +
                "postid=" + postid +
                ", count=" + count +
                '}';
    }
}

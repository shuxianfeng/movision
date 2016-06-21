package com.zhuhuibao.common.constant;

/**
 * 项目信息常量类
 * @author Administrator
 * @version 2016/5/17 0017
 */
public class ProjectConstant {

    /**
     * 隐藏星
     */
    public enum HiddenStar {
        THREE ("***"),FOUR("****"),TEN("**********");
        public final String value;

        HiddenStar(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 首页显示的项目数量 7个
     */
    public final static Integer PROJECT_HOMEPAGE_COUNT_SEVEN = 7;
}

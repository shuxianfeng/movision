package com.zhuhuibao.utils;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import java.io.File;
import java.util.Date;

/**
 * 自定义的命名策略文件
 */
public class RandomFileNamePolicy implements FileRenamePolicy {
    public File rename(File file) {
        /*
         * 假设，我们上传了一个同名文件，文件名为【abcd.ccc】
         */
        int index = file.getName().lastIndexOf("."); //获取文件名中【.】的下标
        String body = file.getName().substring(0, index); //表示文件名的主题，即【abcd】
        String postfix = ""; //表示文件名的后缀，即【.ccc】
        String timer = ""; //代表当前系统时间的数字

        //如果该文件的名字中，没有【.】的话，那么lastIndexOf(".")将返回-1
        if (index != -1) {
            timer = new Date().getTime() + ""; //在文件的名字前面，添加的表示当前系统时间的数字
            postfix = file.getName().substring(index); //获取到文件名当中的【.ccc】
        } else {
            timer = new Date().getTime() + "";
            postfix = ""; //如果lastIndexOf(".")返回-1，说明该文件名中没有【.】即没有后缀
        }

        String newName = body + timer + postfix; //构造新的文件名
        return new File(file.getParent(), newName); //返回重命名后的文件
    }
}
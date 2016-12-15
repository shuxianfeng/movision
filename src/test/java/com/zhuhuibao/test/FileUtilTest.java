package com.zhuhuibao.test;

import com.zhuhuibao.utils.file.FileUtil;
import org.junit.*;

/**
 * Created with IDEA
 * User: zhuangyuhao
 * Date: 2016/11/30
 * Time: 17:30
 */
public class FileUtilTest extends BaseSpringContext {
    @org.junit.Test
    public void test(){
        System.out.println(FileUtil.getFileNameByUrl("//image.zhuhui8.com/upload/price/img/DBgRcpns1480490323747.png"));
    }
}

package com.movision.utils.PersonPhoto;

import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/22 9:36
 */
public class PersonPhotoUtilTest extends SpringTestCase {

    @Autowired
    private PersonPhotoUtil personPhotoUtil;

    @Test
    public void addPhotoToOssAndSaveInDB() throws Exception {

        personPhotoUtil.addPhotoToOssAndSaveInDB();
    }

}
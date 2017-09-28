package com.movision.utils.PersonPhoto;

import com.movision.mybatis.userPhoto.entity.UserPhoto;
import com.movision.mybatis.userPhoto.service.UserPhotoService;
import com.movision.utils.ListUtil;
import com.movision.utils.file.FileUtil;
import com.movision.utils.oss.AliOSSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @Author zhuangyuhao
 * @Date 2017/9/21 16:51
 */
@Service
public class PersonPhotoUtil {

    private static Logger log = LoggerFactory.getLogger(PersonPhotoUtil.class);

    @Autowired
    private UserPhotoService userPhotoService;

    public void addPhotoToOssAndSaveInDB() throws IOException {
        List<String> list = FileUtil.readFilePath("D:/BaiduNetdiskDownload/70000/（精选）娇娇网络头像/A3-王者之风男头", null);
        log.debug("本地图片集合：" + list.toString());

        if (ListUtil.isNotEmpty(list)) {

            for (int i = 0; i < list.size(); i++) {

                AliOSSClient client = new AliOSSClient();
                String fileName = list.get(i);
                File file = new File(fileName);

                Map<String, Object> result = client.uploadLocalFileByPersonPhoto(file, "img", "personPhoto");

                UserPhoto userPhoto = new UserPhoto();
                userPhoto.setUrl(String.valueOf(result.get("url")));
                userPhotoService.add(userPhoto);

            }
        }
    }


}

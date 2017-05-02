package com.movision.utils.videotransfer;

import com.movision.utils.file.FileUtil;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/5/2 11:07
 * 视频转码工具类（平台暂时将非MP4格式的视频文件统一转成MP4格式播放）
 */
@Service
public class VideoTranscoder {

    private static final Logger log = LoggerFactory.getLogger(VideoTranscoder.class);

    public Map<String, Object> transfer(String videourl) throws IOException {

        Map<String, Object> resultmap = new HashMap<>();

        //根据视频上传的地址下载视频文件到服务器本地临时目录
        String tempvideodir = PropertiesLoader.getValue("post.tempvideo.domain");//下载到本地的视频临时存放目录
        log.info("本地视频临时存放目录>>>>>>>>>" + tempvideodir);

        File dirFile = new File(tempvideodir);
        if(!dirFile.exists()){//文件路径不存在时，自动创建目录
            dirFile.mkdir();
        }

        String fileName = FileUtil.getPicName(videourl);//获取视频文件名

        //从服务器上获取文件并保存
        URL theURL = new URL(videourl);//下载链接
        URLConnection connection = theURL.openConnection();
        InputStream in = connection.getInputStream();
        FileOutputStream os = new FileOutputStream(tempvideodir + fileName);
        byte[] buffer = new byte[4 * 1024];
        int read;
        while ((read = in.read(buffer)) > 0) {
            os.write(buffer, 0, read);
        }
        os.close();
        in.close();

        //对临时目录中的视频文件进行转码（文件名称不变，只更改后缀）

        //再上传转换后的视频文件到静态资源服务器中

        //上传成功后删除videourl路径下的源视频文件

        //返回新视频文件的地址


        return resultmap;
    }
}

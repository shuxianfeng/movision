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
import java.util.*;

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

        //服务器上ffmpeg的程序路径
        String ffmpeginstalldir = PropertiesLoader.getValue("ffmpeg.installdir.domain");

        log.info("本地视频临时存放目录>>>>>>>>>" + tempvideodir);

        File dirFile = new File(tempvideodir);
        if(!dirFile.exists()){//文件路径不存在时，自动创建目录
            dirFile.mkdir();
        }

        String fileName = FileUtil.getPicName(videourl);//获取视频文件名

        String PATH = tempvideodir + fileName;//服务器上下载的临时视频路径（待处理文件）

        //从服务器上获取文件并保存
        URL theURL = new URL(videourl);//下载链接
        URLConnection connection = theURL.openConnection();
        InputStream in = connection.getInputStream();
        FileOutputStream os = new FileOutputStream(PATH);
        byte[] buffer = new byte[4 * 1024];
        int read;
        while ((read = in.read(buffer)) > 0) {
            os.write(buffer, 0, read);
        }
        os.close();
        in.close();

        //对临时目录中的视频文件进行转码（转码后文件名称不变，只更改后缀）
        // 判断视频的类型
        int type = checkContentType(tempvideodir + fileName);
        //如果是ffmpeg可以转换的类型直接转码，否则先用mencoder转码成AVI
        boolean status = false;

        String fName = FileUtil.getPicName(PATH);//获取视频文件名
        String name = fName.substring(0, fileName.indexOf("."));//去除文件名后缀
        if (type == 0) {
            System.out.println("直接将文件转为mp4文件");
            status = processMP4(PATH, ffmpeginstalldir, tempvideodir, name);// 直接将文件转为mp4文件
        } else if (type == 1) {
            String avifilepath = processAVI(PATH, ffmpeginstalldir, tempvideodir, name);
            if (avifilepath == null)
                status = processMP4(avifilepath, ffmpeginstalldir, tempvideodir, name);// 将avi转为mp4
        }

        //再上传转换后的视频文件到静态资源服务器中

        //上传成功后删除videourl路径下的源视频文件

        //返回新视频文件的地址


        return resultmap;
    }

    private static int checkContentType(String PATH) {
        String type = PATH.substring(PATH.lastIndexOf(".") + 1, PATH.length())
                .toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        if (type.equals("avi")) {
            return 0;
        } else if (type.equals("mpg")) {
            return 0;
        } else if (type.equals("wmv")) {
            return 0;
        } else if (type.equals("3gp")) {
            return 0;
        } else if (type.equals("mov")) {
            return 0;
        } else if (type.equals("mp4")) {
            return 0;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("asx")) {
            return 0;
        } else if (type.equals("flv")) {
            return 0;
        }
        // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
        // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
        else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }

    // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
    private static boolean processMP4(String oldfilepath, String ffmpeginstalldir, String tempvideodir, String name) {

        if (!checkfile(oldfilepath)) {
            System.out.println(oldfilepath + " is not file");
            return false;
        }

        // 文件命名
        Calendar c = Calendar.getInstance();
        String savename = String.valueOf(c.getTimeInMillis())+ Math.round(Math.random() * 100000);
        List<String> commend = new ArrayList<>();
        commend.add(ffmpeginstalldir);
        commend.add(" -i ");
        commend.add(oldfilepath);
        commend.add(" -ab");
        commend.add(" 56");
        commend.add(" -ar");
        commend.add(" 22050");
        commend.add(" -qscale");
        commend.add(" 8");
        commend.add(" -r");
        commend.add(" 15");
        commend.add(" -s");
        commend.add(" 600x500 ");
        commend.add(oldfilepath.substring(0, oldfilepath.lastIndexOf("/")+1) + "test123" + ".mp4");

        try {
            Runtime runtime = Runtime.getRuntime();
            Process proce = null;
            //视频截图命令，封面图。  8是代表第8秒的时候截图
            String cmd = "";
            String cut = ffmpeginstalldir + " -i "
                    + oldfilepath
                    + "   -y   -f   image2   -ss   8   -t   0.001   -s   750x440 " + tempvideodir
                    + name + ".jpg";
            String cutCmd = cmd + cut;
            proce = runtime.exec(cutCmd);
            //调用线程命令进行转码
            ProcessBuilder builder = new ProcessBuilder(ffmpeginstalldir, " -i " + oldfilepath, " -ab" + " 56", " -ar" + " 22050", " -qscale" + " 8", " -r" + " 15", " -s" + " 600x500 ", oldfilepath.substring(0, oldfilepath.lastIndexOf("/")+1) + "test123" + ".mp4");
//            ProcessBuilder builder = new ProcessBuilder(commend);
//            builder.command(commend);
            builder.start();
//            Process videoproce = null;
//            videoproce = runtime.exec(sb.toString());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
    private static String processAVI(String PATH, String ffmpeginstalldir, String tempvideodir, String name) {
        List<String> commend = new ArrayList<>();
        commend.add(ffmpeginstalldir + " ");
        commend.add(PATH);
        commend.add(" -oac");
        commend.add(" lavc");
        commend.add(" -lavcopts");
        commend.add(" acodec=mp3:abitrate=64");
        commend.add(" -ovc");
        commend.add(" xvid");
        commend.add(" -xvidencopts");
        commend.add(" bitrate=600");
        commend.add(" -of");
        commend.add(" avi");
        commend.add(" -o ");
        commend.add(tempvideodir + name + ".avi");
        try {
            //调用线程命令启动转码
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.start();
            return tempvideodir + name + ".avi";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        }
        return true;
    }
}

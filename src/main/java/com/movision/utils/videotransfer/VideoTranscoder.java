package com.movision.utils.videotransfer;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;
import com.movision.utils.file.FileUtil;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
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

    public Map<String, Object> transfer(String videourl) throws IOException, EncoderException {

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

        String format = PATH.substring(PATH.lastIndexOf(".") + 1, PATH.length())
                .toLowerCase();//获取上传的视频格式

        int width = 600;//转换后的视频默认宽
        int height = 500;//转换后的视频默认高
        //如果是jdk支持的格式就直接获取原视频的宽高
        if (!format.equals("flv") && !format.equals("swf")) {
            File file = new File(PATH);
            Encoder encoder = new Encoder();
            MultimediaInfo m = encoder.getInfo(file);
            width = m.getVideo().getSize().getWidth();
            height = m.getVideo().getSize().getHeight();
        }

        if (type == 0) {
            log.info("直接将文件转为mp4文件");
            status = processMP4(PATH, ffmpeginstalldir, tempvideodir, name, width, height);// 直接将文件转为mp4文件
        } else if (type == 1) {
            String avifilepath = processAVI(PATH, ffmpeginstalldir, tempvideodir, name);
            if (avifilepath == null)
                status = processMP4(avifilepath, ffmpeginstalldir, tempvideodir, name, width, height);// 将视频文件转为mp4
        }

        //再上传转换后的视频文件到静态资源服务器中
//        String uploadpath = PropertiesLoader.getValue("post.video.domain");//新文件上传的静态资源服务器目录
//
//        log.info("新文件上传路径>>>>>>>>>>>>>>" + uploadpath + "待上传的文件路径>>>>>>>>" + tempvideodir + name + ".mp4");
//        File file = new File(tempvideodir + name + ".mp4");
//        FTPClient ftpClient = new FTPClient();
//        ftpClient.setControlEncoding("GBK");
//        String hostname = PropertiesLoader.getValue("resource.hostname.domain");//静态资源服务器IP
//        int port = Integer.parseInt(PropertiesLoader.getValue("resource.port.domain"));//FTP默认端口21   SFTP的默认端口22
//        String username = PropertiesLoader.getValue("resource.username.domain");//root用户名
//        String password = PropertiesLoader.getValue("resource.password.domain");//root密码
//        try {
//            //链接ftp服务器
//            ftpClient.connect(hostname, port);
//            //登录ftp
//            ftpClient.login(username, password);
//            int  reply = ftpClient.getReplyCode();
//            log.info("检测静态资源服务器连接状态(如果reply返回230就算成功,如果返回530密码用户名错误或当前用户无权限下面有详细的解释)>>>>>>>" + reply);
//
//            if (!FTPReply.isPositiveCompletion(reply)) {
//                ftpClient.disconnect();
//                log.info("FTP服务器连接异常");
//            }
//            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//
//            ftpClient.changeWorkingDirectory(uploadpath);//给ftp指定新文件上传到静态资源服务器的指定目录
//            String remoteFileName = name + ".mp4";
//            InputStream input = new FileInputStream(file);
//            ftpClient.storeFile(remoteFileName, input);//文件你若是不指定就会上传到root目录下
//            input.close();
//            ftpClient.logout();
//
////            110  重新启动标记应答。在这种情况下文本是确定的，它必须是：MARK   yyyy=mmmm，其中yyyy是用户进程数据流标记，mmmm是服务器标记。
////            120     服务在nnn分钟内准备好
////            125     数据连接已打开，准备传送
////            150     文件状态良好，打开数据连接
////            200     命令成功
////            202     命令未实现
////            211     系统状态或系统帮助响应
////            212     目录状态
////            213     文件状态
////            214     帮助信息，信息仅对人类用户有用
////            215     名字系统类型
////            220     对新用户服务准备好
////            221     服务关闭控制连接，可以退出登录
////            225     数据连接打开，无传输正在进行
////            226     关闭数据连接，请求的文件操作成功
////            227     进入被动模式
////            230     用户登录
////            250     请求的文件操作完成
////            257     创建 "PATHNAME "
////            331     用户名正确，需要口令
////            332     登录时需要帐户信息
////            350     请求的文件操作需要进一步命令
////            421     不能提供服务，关闭控制连接
////            425     不能打开数据连接
////            426     关闭连接，中止传输
////            450     请求的文件操作未执行
////            451     中止请求的操作：有本地错误
////            452     未执行请求的操作：系统存储空间不足
////            500     格式错误，命令不可识别
////            501     参数语法错误
////            502     命令未实现
////            503     命令顺序错误
////            504     此参数下的命令功能未实现
////            530     未登录（用户名或密码错误，1、FTP密码修改了？2、用户名/密码输入错误？先仔细检查有无输入错误   如复制的时候误复制了空格！！）
////            532     存储文件需要帐户信息
////            550     未执行请求的操作
////            551     请求操作中止：页类型未知
////            552     请求的文件操作中止，存储分配溢出
////            553     未执行请求的操作：文件名不合法
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            if (ftpClient.isConnected()) {
//                try {
//
//                    ftpClient.disconnect();
//
//                } catch (IOException ioe) {
//
//                    ioe.printStackTrace();
//                }
//            }
//        }

        //上传成功后删除videourl路径下的源视频文件

        //删除截取的封面文件和临时文件
        log.info("删除临时文件>>>>>>>>>>>>>>>>>>");
//        File videofile = new File(PATH);
//        videofile.delete();
//        File imgfile = new File(PATH.substring(0, PATH.lastIndexOf(".") +1) + "jpg");
//        imgfile.delete();

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
    private static boolean processMP4(String oldfilepath, String ffmpeginstalldir, String tempvideodir, String name, int width, int height) {

        //服务器上ffmpeg的程序路径
        String watermarkimg = PropertiesLoader.getValue("video.watermark.domain");

        if (!checkfile(oldfilepath)) {
            System.out.println(oldfilepath + " is not file");
            return false;
        }

        // 文件命名
        Calendar c = Calendar.getInstance();
        String savename = String.valueOf(c.getTimeInMillis())+ Math.round(Math.random() * 100000);
//        List<String> commend = new ArrayList<>();
//        commend.add(ffmpeginstalldir);
//        commend.add(" -i ");
//        commend.add(oldfilepath);
//        commend.add(" -ab");
//        commend.add(" 56");
//        commend.add(" -ar");
//        commend.add(" 22050");
//        commend.add(" -qscale");
//        commend.add(" 12");
//        commend.add(" -r");
//        commend.add(" 15");
//        commend.add(" -s ");
//        commend.add(String.valueOf(width) + "x" + String.valueOf(height));
//        commend.add(oldfilepath.substring(0, oldfilepath.lastIndexOf("/")+1) + name + ".mp4");

        StringBuffer sb = new StringBuffer();
        sb.append(ffmpeginstalldir);
        sb.append(" -i ");
        sb.append(oldfilepath);
        sb.append(" -ab");
        sb.append(" 56");
        sb.append(" -ar");
        sb.append(" 22050");
        sb.append(" -qscale");
        sb.append(" 12");
        sb.append(" -r");
        sb.append(" 15");
        sb.append(" -s ");
        sb.append(String.valueOf(width) + "x" + String.valueOf(height));
        sb.append(oldfilepath.substring(0, oldfilepath.lastIndexOf("/")+1) + name + ".mp4");

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
            proce.waitFor();//让程序同步（非异步，执行完所有转码才会执行下一行代码）

            //调用线程命令进行转码
//            ProcessBuilder builder = new ProcessBuilder(ffmpeginstalldir, " -i ", oldfilepath, " -ab", " 56", " -ar", " 22050", " -qscale", " 12", " -r", " 15", " -s ", String.valueOf(width) + "x" + String.valueOf(height), oldfilepath.substring(0, oldfilepath.lastIndexOf("/")+1) + name + ".mp4");
//            ProcessBuilder builder = new ProcessBuilder();
//            builder.command(commend);
//            builder.start();
            Process videoproce = runtime.exec(sb.toString());
            videoproce.waitFor();//让程序同步（非异步，执行完所有转码才会执行下一行代码）

            //调用线程进行视频水印打印
            StringBuffer str = new StringBuffer();
            str.append(ffmpeginstalldir);
            str.append(" -i ");
            str.append(oldfilepath.substring(0, oldfilepath.lastIndexOf("/")+1) + name + ".mp4");
            str.append(" -i ");
            str.append(watermarkimg);
            str.append(" -filter_complex ");
            str.append(" overlay=W-w ");
            str.append(oldfilepath.substring(0, oldfilepath.lastIndexOf("/")+1) + "test123" + ".mp4");

            Process watermarkproce = runtime.exec(str.toString());
            watermarkproce.waitFor();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
    private static String processAVI(String PATH, String ffmpeginstalldir, String tempvideodir, String name) {
//        List<String> commend = new ArrayList<>();
//        commend.add(ffmpeginstalldir + " ");
//        commend.add(PATH);
//        commend.add(" -oac");
//        commend.add(" lavc");
//        commend.add(" -lavcopts");
//        commend.add(" acodec=mp3:abitrate=64");
//        commend.add(" -ovc");
//        commend.add(" xvid");
//        commend.add(" -xvidencopts");
//        commend.add(" bitrate=600");
//        commend.add(" -of");
//        commend.add(" avi");
//        commend.add(" -o ");
//        commend.add(tempvideodir + name + ".avi");

        StringBuffer sb = new StringBuffer();
        sb.append(ffmpeginstalldir + " ");
        sb.append(PATH);
        sb.append(" -oac");
        sb.append(" lavc");
        sb.append(" -lavcopts");
        sb.append(" acodec=mp3:abitrate=64");
        sb.append(" -ovc");
        sb.append(" xvid");
        sb.append(" -xvidencopts");
        sb.append(" bitrate=600");
        sb.append(" -of");
        sb.append(" avi");
        sb.append(" -o ");
        sb.append(tempvideodir + name + ".avi");

        try {
            Runtime runtime = Runtime.getRuntime();

            //调用线程命令启动转码
//            ProcessBuilder builder = new ProcessBuilder();
//            builder.command(commend);
//            builder.start();
            Process videoproce = runtime.exec(sb.toString());
            videoproce.waitFor();

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

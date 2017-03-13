package com.movision.facade.video;

import com.movision.common.constant.ApiConstants;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.BusinessException;
import com.movision.mybatis.video.service.VideoService;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 *
 * @Author zhuangyuhao
 * @Date 2017/3/3 9:45
 */
@Service
public class VideoFacade {

    private static Logger log = LoggerFactory.getLogger(VideoFacade.class);

    @Autowired
    private VideoService videoService;

    @Autowired
    private ApiConstants apiConstants;


    /**
     * 文件上传
     * 具体步骤：
     * 1）获得磁盘文件条目工厂 DiskFileItemFactory 要导包
     * 2） 利用 request 获取 真实路径 ，供临时文件存储，和 最终文件存储 ，这两个存储位置可不同，也可相同
     * 3）对 DiskFileItemFactory 对象设置一些 属性
     * 4）上层API文件上传处理  ServletFileUpload upload = new ServletFileUpload(factory);
     * 目的是调用 parseRequest（request）方法  获得 FileItem 集合list ，
     * <p>
     * 5）在 FileItem 对象中 获取信息，   遍历， 判断 表单提交过来的信息 是否是 普通文本信息  另做处理
     * 6）
     * 第一种. 用第三方 提供的  item.write( new File(path,filename) ); 直接写到磁盘上
     * 第二种. 手动处理
     *
     * @param request
     * @param chann
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public Map newUploadVideo(HttpServletRequest request, String chann, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("utf-8");
        //获得磁盘文件条目工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //获取文件需要上传到的路径
//        String path = request.getRealPath("/upload");
        String path = apiConstants.getUploadDir() + "/" + chann + "/video";
        //如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
        //设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
        /**
         * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上，
         * 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的
         * 然后再将其真正写到 对应目录的硬盘上
         */
        factory.setRepository(new File(path));
        //设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
        factory.setSizeThreshold(1024 * 1024);

        //上层API文件上传处理
        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            Map result = new HashedMap();
            String data = null;
            //可以上传多个文件
            log.info("请求消息中的所有参数名的Enumeration对象" + request.getParameterNames() + ", 求消息中的所有参数名和值的Map对象" +
                    request.getParameterMap().toString());
            List<FileItem> list = upload.parseRequest(request);
            log.info("List<FileItem>=" + list);
            for (FileItem item : list) {

                //获取表单的属性名字
                String name = item.getFieldName();
                log.info("获取表单的属性名字=" + name);
                //如果获取的 表单信息是普通的 文本 信息
                if (item.isFormField()) {
                    //获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
                    String value = item.getString();
                    log.info("获取用户具体输入的字符串=" + value);
                    request.setAttribute(name, value);
                }
                //对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些
                else {
                    /**
                     * 以下三步，主要获取 上传文件的名字
                     */
                    //获取路径名
                    String value = item.getName();
                    //索引到最后一个反斜杠
                    int start = value.lastIndexOf("\\");
                    //截取 上传文件的 字符串名字，加1是 去掉反斜杠，
                    String filename = value.substring(start + 1);
                    log.info("上传的文件名=" + filename);
                    request.setAttribute(name, filename);

                    //真正写到磁盘上
                    //它抛出的异常 用exception 捕捉

                    //item.write( new File(path,filename) );//第三方提供的

                    //手动写的
                    OutputStream out = new FileOutputStream(new File(path, filename));
                    InputStream in = item.getInputStream();

                    int length = 0;
                    byte[] buf = new byte[10240];

                    log.info("获取上传文件的总共的容量：" + item.getSize());

                    // in.read(buf) 每次读到的数据存放在   buf 数组中
                    while ((length = in.read(buf)) != -1) {
                        //在   buf 数组中 取出数据 写到 （输出流）磁盘上
                        out.write(buf, 0, length);
                    }
                    in.close();
                    out.close();
                    String videoDomain = PropertiesLoader.getValue("upload.video.domain");
                    data = videoDomain + "/upload/" + chann + "/video/" + filename;

                }
            }

            log.info("上传成功");
            result.put("status", "success");
            result.put("data", data);
            log.info("返回的data=" + data);
            return result;

        } catch (FileUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //分发到下一个JSP页面处理
//        request.getRequestDispatcher("filedemo.jsp").forward(request, response);
        return null;
    }


    public Map uploadVideo(HttpServletRequest request, String chann, HttpServletResponse response) throws ServletException, IOException {
//        PrintWriter out = response.getWriter();
//        MediaDao mediaDao = DaoFactory.getMediaDao();
//        String message = "";

//        String uri = request.getRequestURI();
//        String path = uri.substring(uri.lastIndexOf("/"), uri.lastIndexOf("."));

        //获得磁盘文件条目工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();

        //创建一个解析器,分析InputStream,该解析器会将分析的结果封装成一个FileItem对象的集合
        //一个FileItem对象对应一个表单域
        ServletFileUpload sfu = new ServletFileUpload(factory);
        Map result = new HashedMap();
        try {

//            Video video = new Video();
            List<FileItem> items = sfu.parseRequest(request);
            boolean flag = false;    //转码成功与否的标记
            String data = null;
            for (int i = 0; i < items.size(); i++) {
                FileItem item = items.get(i);
                //要区分是上传文件还是普通的表单域
                if (item.isFormField()) {//isFormField()为true,表示这不是文件上传表单域
                    //普通表单域
//                    String paramName = item.getFieldName();
//                        /*
//                            String paramValue = item.getString();
//                            System.out.println("参数名称为:" + paramName + ", 对应的参数值为: " + paramValue);
//                        */
//                    if (paramName.equals("videoname")) {
//                        video.setVideoname(new String(item.getString().getBytes("ISO8859-1"), "UTF-8"));
//                    }
                    log.info("普通表单域");

                } else {
                    //上传文件
                    //System.out.println("上传文件" + item.getName());
//                    ServletContext sctx = this.getServletContext();
                    //获得保存文件的路径
//                    String basePath = sctx.getRealPath("videos");
                    String basePath = apiConstants.getUploadDir() + "/" + chann + "/video";
                    String videoDomain = PropertiesLoader.getValue("upload.video.domain");
                    //获得文件名
                    String fileUrl = item.getName();
                    //在某些操作系统上,item.getName()方法会返回文件的完整名称,即包括路径
                    String fileType = fileUrl.substring(fileUrl.lastIndexOf(".")); //截取文件格式
                    //自定义方式产生文件名
                    String serialName = String.valueOf(System.currentTimeMillis());
//                    String serialName = FileUtil.renameFile(file.getOriginalFilename());
                    //待转码的文件
                    File uploadFile = new File(basePath + "/temp/" + serialName + fileType);

                    //目录不存在则创建
                    log.info("【basePath】=" + basePath);
                    File dir = new File(basePath);
                    if (!dir.exists() && !dir.isDirectory()) {
                        dir.mkdirs();
                        log.info("mk dir susscess dirName = " + basePath);
                    }
                    if (item.getSize() > 5 * 1024 * 1024 * 1024) {
                        throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "文件大小超过限制");
                    }

                    item.write(uploadFile);

//                    String codcFilePath = basePath + "/" + serialName + ".flv";                //设置转换为flv格式后文件的保存路径
//                    String mediaPicPath = basePath + "/images" + File.separator + serialName + ".jpg";    //设置上传视频截图的保存路径
//                    video.setVideourl("videos/" + serialName + ".flv");
//                    video.setBannerimgurl("videos/images/" + serialName + ".jpg");

                    /**
                     * 这是外界访问该视频的地址
                     * 其中data = http://120.77.214.187:8100/upload/$chan/video/$filename
                     */
                    data = videoDomain + "/upload/" + chann + "/video/" + serialName + fileType;

                    // 获取配置的转换工具（ffmpeg.exe）的存放路径
//                    String ffmpegPath = request.getSession().getServletContext().getRealPath("/tools/ffmpeg.exe");
//                    log.info("开始转码");
//                    flag = VideoUtil.executeCodecs(ffmpegPath, uploadFile.getAbsolutePath(), codcFilePath, mediaPicPath);
//                    log.info("转码完成");
                }
            }
            log.info("上传成功");
            result.put("status", "success");
            result.put("data", data);
//            if (flag) {
//                //转码成功,向数据表中添加该视频信息
////                videoService.insertVideoById(video);
//                log.info("上传成功");
//                result.put("status", "success");
//                result.put("data", data);
//
//            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        return result;
    }


}

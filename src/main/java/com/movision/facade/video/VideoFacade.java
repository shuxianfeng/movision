//package com.movision.facade.video;
//
//import com.movision.common.constant.ApiConstants;
//import com.movision.common.constant.MsgCodeConstant;
//import com.movision.exception.BusinessException;
//import com.movision.mybatis.video.entity.Video;
//import com.movision.mybatis.video.service.VideoService;
//import com.movision.utils.PropertiesUtils;
//import com.movision.utils.VideoUtil;
//import org.apache.commons.collections.map.HashedMap;
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//import java.util.Map;
//
///**
// * \
// * 作废
// *
// * @Author zhuangyuhao
// * @Date 2017/3/3 9:45
// */
//public class VideoFacade {
//
//    private static Logger log = LoggerFactory.getLogger(VideoFacade.class);
//
//    @Autowired
//    private VideoService videoService;
//
//    @Autowired
//    private ApiConstants apiConstants;
//
//
//    public Map uploadVideo(HttpServletResponse response, HttpServletRequest request, String chann) throws ServletException, IOException {
//        PrintWriter out = response.getWriter();
////        MediaDao mediaDao = DaoFactory.getMediaDao();
//        String message = "";
//
//        String uri = request.getRequestURI();
//        String path = uri.substring(uri.lastIndexOf("/"), uri.lastIndexOf("."));
//
//        //提供解析时的一些缺省配置
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//
//        //创建一个解析器,分析InputStream,该解析器会将分析的结果封装成一个FileItem对象的集合
//        //一个FileItem对象对应一个表单域
//        ServletFileUpload sfu = new ServletFileUpload(factory);
//        Map result = new HashedMap();
//        try {
//
//            Video video = new Video();
//            List<FileItem> items = sfu.parseRequest(request);
//            boolean flag = false;    //转码成功与否的标记
//            String data = null;
//            for (int i = 0; i < items.size(); i++) {
//                FileItem item = items.get(i);
//                //要区分是上传文件还是普通的表单域
//                if (item.isFormField()) {//isFormField()为true,表示这不是文件上传表单域
//                    //普通表单域
//                    String paramName = item.getFieldName();
//                        /*
//                            String paramValue = item.getString();
//                            System.out.println("参数名称为:" + paramName + ", 对应的参数值为: " + paramValue);
//                        */
//                    if (paramName.equals("videoname")) {
//                        video.setVideoname(new String(item.getString().getBytes("ISO8859-1"), "UTF-8"));
//                    }
//
//                } else {
//                    //上传文件
//                    //System.out.println("上传文件" + item.getName());
////                    ServletContext sctx = this.getServletContext();
//                    //获得保存文件的路径
////                    String basePath = sctx.getRealPath("videos");
//                    String basePath = apiConstants.getUploadDir() + "/" + chann + "/video";
//                    String videoDomain = PropertiesUtils.getValue("upload.video.domain");
//                    //获得文件名
//                    String fileUrl = item.getName();
//                    //在某些操作系统上,item.getName()方法会返回文件的完整名称,即包括路径
//                    String fileType = fileUrl.substring(fileUrl.lastIndexOf(".")); //截取文件格式
//                    //自定义方式产生文件名
//                    String serialName = String.valueOf(System.currentTimeMillis());
////                    String serialName = FileUtil.renameFile(file.getOriginalFilename());
//                    //待转码的文件
//                    File uploadFile = new File(basePath + "/temp/" + serialName + fileType);
//
//                    //目录不存在则创建
//                    log.info("【basePath】=" + basePath);
//                    File dir = new File(basePath);
//                    if (!dir.exists() && !dir.isDirectory()) {
//                        dir.mkdirs();
//                        log.info("mk dir susscess dirName = " + basePath);
//                    }
//                    if (item.getSize() > 5 * 1024 * 1024 * 1024) {
//                        throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "文件大小超过限制");
//                    }
//
//                    item.write(uploadFile);
//
//                    String codcFilePath = basePath + "/" + serialName + ".flv";                //设置转换为flv格式后文件的保存路径
//                    String mediaPicPath = basePath + "/images" + File.separator + serialName + ".jpg";    //设置上传视频截图的保存路径
//                    video.setVideourl("videos/" + serialName + ".flv");
//                    video.setBannerimgurl("videos/images/" + serialName + ".jpg");
//
//                    /**
//                     * 这是外界访问该图片的地址
//                     * 其中data = http://120.77.214.187:8100/upload/$chan/img/$filename
//                     */
//                    data = videoDomain + "/upload/" + chann + "/img/" + serialName + ".flv";
//
//                    // 获取配置的转换工具（ffmpeg.exe）的存放路径
//                    String ffmpegPath = request.getSession().getServletContext().getRealPath("/tools/ffmpeg.exe");
//                    //转码
//                    log.info("开始转码");
//                    flag = VideoUtil.executeCodecs(ffmpegPath, uploadFile.getAbsolutePath(), codcFilePath, mediaPicPath);
//                    log.info("转码完成");
//                }
//            }
//            if (flag) {
//                //转码成功,向数据表中添加该视频信息
//                videoService.insertVideoById(video);
//                log.info("上传成功");
//
//                result.put("status", "success");
//                result.put("data", data);
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ServletException(e);
//        }
//        return result;
//    }
//
//
//}

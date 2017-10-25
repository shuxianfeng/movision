package com.movision.facade.boss;

import com.movision.controller.app.AppLoginController;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.VideoUploadUtil;
import com.movision.utils.oss.MovisionOssClient;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @Author zhurui
 * @Date 2017/10/25 16:40
 */
@Service
public class XmlParseFacade {

    @Autowired
    private MovisionOssClient movisionOssClient;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoUploadUtil videoUploadUtil;


    public Map analysisXml(MultipartFile file, String nickname, String phone) {
        Map resault = new HashMap();
        SAXReader reader = new SAXReader();
        Post post = new Post();
        try {
            queryUser(nickname, phone, post);
            Document document = reader.read(file.getInputStream());
            System.out.println(document);
            //获取跟标签
            Element element = document.getRootElement();
            List<Element> elements = element.elements();
            //存储本地图片路径，以便做空间释放操作
            List list = new ArrayList();
            boolean flg = false;
            //循环所有父节点
            /*for (Element e : elements) {
                //用于拼接帖子内容
                String content = "[";
                //获取发帖时间并转换为long类型
                Long publishTime = Long.parseLong(e.element("publishTime").getText());
                Date intime = new Date(publishTime);
                post.setIntime(intime);
                //类型
                String type = e.element("type").getText();
                //标签
                String tag = e.element("tag").getText();

                //图片内容解析
                if (type.equals("Photo")) {
                    content = getImgContentAnalysis(post, list, e, content);
                    flg = true;
                }
                //视频内容解析
                if (type.equals("Video")) {
                    //视频内容
                    content = getVideoContentAnalysis(post, e, content);
                    flg = true;
                }
                //纯文本解析
               *//* if (type.equals("Text")){
                    //文本
                    s = getTextContentAnalysis(post, e, s);
                    flg = true;
                }*//*

                if (!flg) {
                    content = "";
                }
                System.out.println("---------" + content);
                //标签操作 // TODO: 2017/10/25


            }*/

            //释放空间,删除本地图片
            /*for (int k = 0;k<list.size();k++){
                File fi = new File(list.get(k).toString());
                fi.delete();
            }*/
            resault.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            resault.put("code", 400);
        }
        return resault;
    }

    /**
     * 查询用户
     *
     * @param nickname
     * @param phone
     * @param post
     */
    private void queryUser(String nickname, String phone, Post post) {
        User user = new User();
        if (StringUtil.isNotEmpty(phone)) {
            user.setPhone(phone);
        }
        if (StringUtil.isNotEmpty(nickname)) {
            user.setNickname(nickname);
        }
        //根据手机号或昵称查询
        User userid = userService.queryUserByPhone(phone);
        if (userid != null) {
            post.setUserid(userid.getId());
        } else {
            //注册用户,调用注册接口
            //获取验证码,发起get请求
            String s = videoUploadUtil.GetHttp("http://51mofo.com/movision/app/login/get_mobile_code?mobile=" + phone);
            System.out.println(s);
        }
    }

    /**
     * 文本内容解析
     *
     * @param post
     * @param e
     * @param content
     * @return
     */
    private String getTextContentAnalysis(Post post, Element e, String content) {
        String caption = e.element("content").getText();
        String caps = caption.replace("<p>", "");
        caps = caps.replace("</p>", "");
        content += "{\"type\": 0,\"orderid\":" + 0 + ",\"value\":\"" + caps + "\",\"wh\": \"\",\"dir\": \"\"}";

        //用于内容拼接闭合
        String neirong = post.getPostcontent();
        content += "]";
        //System.out.println("---------"+s);
        post.setPostcontent(neirong);
        return content;
    }

    /**
     * 视频内容解析
     *
     * @param post
     * @param e
     * @param content
     * @return
     */
    private String getVideoContentAnalysis(Post post, Element e, String content) {
        String embed = e.element("embed").getText();
        embed = embed.replace("{", "");
        embed = embed.replace("}", "");
        embed = embed.replace("\"", "");
        String[] embeds = embed.split(",");
        int num = 0;
        for (int i = 0; i < embeds.length; i++) {
            if (embeds[i].substring(0, embeds[i].indexOf(":")).equals("originUrl")) {
                content += "{\"type\": 2,\"orderid\":" + num + ",";
                download(embeds[i].substring(embeds[i].indexOf(":") + 1, embeds[i].length()), "video");
                content += "\"value\":\"" + embeds[i].substring(embeds[i].indexOf(":") + 1, embeds[i].length()) + "\",\"wh\": \"\",\"dir\": \"\"},";
                //System.out.println(originUrl);
                num++;
            }

        }
        //文本
        Element caption = e.element("caption");
        String caps = caption.getText().replace("<p>", "");
        caps = caps.replace("</p>", "");
        content += "{\"type\": 0,\"orderid\":" + num + ",\"value\":\"" + caps + "\",\"wh\": \"\",\"dir\": \"\"}";

        //用于内容拼接闭合
        String neirong = post.getPostcontent();
        content += "]";
        //System.out.println("---------"+s);
        post.setPostcontent(neirong);
        return content;
    }

    /**
     * 图片内容解析
     *
     * @param post
     * @param list
     * @param e
     * @param content
     * @return
     */
    private String getImgContentAnalysis(Post post, List list, Element e, String content) {
        Element photoLinks = e.element("photoLinks");
        String pho = photoLinks.getText();
        //pho = pho.substring(2, pho.lastIndexOf("]")-1);
        pho = pho.replace("[", "");
        pho = pho.replace("{", "");
        pho = pho.replace("}", "");
        pho = pho.replace("]", "");
        pho = pho.replace("\"", "");
        String[] substring = pho.split(",");
        int num = 0;
        //循环子节点拼接帖子内容
        for (int i = 0; i < substring.length; i++) {

            String wh = "\"wh\":";
            if (substring[i].substring(0, substring[i].indexOf(":")).equals("orign")) {
                content += "\"type\":1,";
                Map m = download(substring[i].substring(substring[i].indexOf(":") + 1, substring[i].indexOf("?")), "img");
                list.add(m.get("oldurl"));
                content += "\"value\":\"" + m.get("newurl") + "\",\"dir\": \"\"},";
            }
            if (substring[i].substring(0, substring[i].indexOf(":")).equals("ow")) {
                content += "{\"orderid\":" + num + ",";
                num++;
                content += wh + "\"" + substring[i].substring(substring[i].indexOf(":") + 1, substring[i].length()) + "×";
            }
            if (substring[i].substring(0, substring[i].indexOf(":")).equals("oh")) {
                content += substring[i].substring(substring[i].indexOf(":"), substring[i].length()) + "\",";
            }
        }

        //文本
        Element caption = e.element("caption");
        String caps = caption.getText().replace("<p>", "");
        caps = caps.replace("</p>", "");
        content += "{\"type\": 0,\"orderid\":" + num + ",\"value\":\"" + caps + "\",\"wh\": \"\",\"dir\": \"\"}";

        //用于内容拼接闭合
        String neirong = post.getPostcontent();
        content += "]";
        //System.out.println("---------"+s);
        post.setPostcontent(neirong);
        return content;
    }

    /**
     * 下载文件
     *
     * @param str
     */
    private Map download(String str, String type) {
        InputStream is = null;
        OutputStream os = null;
        Map map = new HashMap();
        String path = "c://";
        try {
            String url = str;
            URL u = new URL(url);
            //获取文件名
            String[] filename = url.split("/");
            String s = filename[filename.length - 1];
            is = u.openStream();
            os = new FileOutputStream(path + s);
            int buff = 0;
            while ((buff = is.read()) != -1) {
                os.write(buff);
            }
            map.put("oldurl", path + s);
            if (type.equals("img")) {
                //图片上传
                Map t = movisionOssClient.uploadFileObject(new File(path + s), "img", "post");
                map.put("newurl", t.get("url"));
            } else if (type.equals("video")) {
                //视频上传
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
}

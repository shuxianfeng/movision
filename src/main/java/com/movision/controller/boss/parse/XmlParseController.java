package com.movision.controller.boss.parse;

import com.movision.common.Response;
import com.movision.facade.boss.PostFacade;
import com.movision.mybatis.post.entity.Post;
import com.movision.utils.oss.MovisionOssClient;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @Author zhurui
 * @Date 2017/10/24 11:42
 */
@RestController
@RequestMapping("/xmlparse")
public class XmlParseController {

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private MovisionOssClient movisionOssClient;

    private static Logger logger = LoggerFactory.getLogger(XmlParseController.class);


    @ApiOperation(value = "解析xml文件", notes = "用于解析xml文件", response = Response.class)
    @RequestMapping(value = "analysis_xml", method = RequestMethod.POST)
    public Response xmlParse(@ApiParam(value = "文件") @RequestParam MultipartFile file,
                             @ApiParam(value = "用户昵称") @RequestParam String nickname,
                             @ApiParam(value = "手机号") @RequestParam String phone) {
        Response response = new Response();
        analysisXml(file, nickname, phone);
        return response;
    }


    public void analysisXml(MultipartFile file, String nickname, String phone) {
        SAXReader reader = new SAXReader();
        Post post = new Post();
        try {
            Document document = reader.read(file.getInputStream());
            System.out.println(document);
            //获取跟标签
            Element element = document.getRootElement();
            List<Element> elements = element.elements();
            //存储本地图片路径，以便做空间释放操作
            List list = new ArrayList();
            boolean flg = false;
            //循环所有父节点
            for (Element e : elements) {
                //用于拼接帖子内容
                String s = "[";
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
                            s += "\"type\":1,";
                            Map m = download(substring[i].substring(substring[i].indexOf(":") + 1, substring[i].indexOf("?")), "img");
                            list.add(m.get("oldurl"));
                            s += "\"value\":\"" + m.get("newurl") + "\",\"dir\": \"\"},";
                        }
                        if (substring[i].substring(0, substring[i].indexOf(":")).equals("ow")) {
                            s += "{\"orderid\":" + num + ",";
                            num++;
                            s += wh + "\"" + substring[i].substring(substring[i].indexOf(":") + 1, substring[i].length()) + "×";
                        }
                        if (substring[i].substring(0, substring[i].indexOf(":")).equals("oh")) {
                            s += substring[i].substring(substring[i].indexOf(":"), substring[i].length()) + "\",";
                        }
                    }

                    //文本
                    Element caption = e.element("caption");
                    String caps = caption.getText().replace("<p>", "");
                    caps = caps.replace("</p>", "");
                    s += "{\"type\": 0,\"orderid\":" + num + ",\"value\":\"" + caps + "\",\"wh\": \"\",\"dir\": \"\"}";

                    //用于内容拼接闭合
                    String neirong = post.getPostcontent();
                    s += "]";
                    //System.out.println("---------"+s);
                    post.setPostcontent(neirong);
                    flg = true;
                }
                //视频内容解析
                if (type.equals("Video")) {
                    //视频内容
                    String embed = e.element("embed").getText();
                    embed = embed.replace("{", "");
                    embed = embed.replace("}", "");
                    embed = embed.replace("\"", "");
                    String[] embeds = embed.split(",");
                    int num = 0;
                    for (int i = 0; i < embeds.length; i++) {
                        if (embeds[i].substring(0, embeds[i].indexOf(":")).equals("originUrl")) {
                            s += "{\"type\": 2,\"orderid\":" + num + ",";
                            download(embeds[i].substring(embeds[i].indexOf(":") + 1, embeds[i].length()), "video");
                            s += "\"value\":\"" + embeds[i].substring(embeds[i].indexOf(":") + 1, embeds[i].length()) + "\",\"wh\": \"\",\"dir\": \"\"},";
                            //System.out.println(originUrl);
                            num++;
                        }

                    }
                    //文本
                    Element caption = e.element("caption");
                    String caps = caption.getText().replace("<p>", "");
                    caps = caps.replace("</p>", "");
                    s += "{\"type\": 0,\"orderid\":" + num + ",\"value\":\"" + caps + "\",\"wh\": \"\",\"dir\": \"\"}";

                    //用于内容拼接闭合
                    String neirong = post.getPostcontent();
                    s += "]";
                    //System.out.println("---------"+s);
                    post.setPostcontent(neirong);
                    flg = true;
                }

                //纯文本解析
                /*if (type.equals("Text")){
                    //文本
                    String caption = e.element("content").getText();
                    String caps = caption.replace("<p>","");
                    caps = caps.replace("</p>","");
                    s+="{\"type\": 0,\"orderid\":"+0+",\"value\":\""+caps+"\",\"wh\": \"\",\"dir\": \"\"}";

                    //用于内容拼接闭合
                    String neirong = post.getPostcontent();
                    s+="]";
                    //System.out.println("---------"+s);
                    post.setPostcontent(neirong);
                }*/
                if (!flg) {
                    s = "";
                }
                System.out.println("---------" + s);
                //标签操作 // TODO: 2017/10/25
            }

            //释放空间,删除本地图片
            /*for (int k = 0;k<list.size();k++){
                File fi = new File(list.get(k).toString());
                fi.delete();
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
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

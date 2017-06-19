package com.movision.utils.PostcontenDataMove;

import com.google.gson.Gson;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostContent;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postDes.service.PostDesService;
import com.movision.mybatis.video.entity.Video;
import com.movision.mybatis.video.service.VideoService;
import com.movision.utils.ListUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据处理
 * 1 解析yw_post中的postcontent字段中的数据
 * 2 插入到新的表中yw_post_des中的postcontent
 * 3 再把yw_post中的其他字段也放进去
 *
 * @Author zhuangyuhao
 * @Date 2017/6/15 19:30
 */
@Service
public class PostcontentDataMove {

    @Autowired
    private PostService postService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private PostDesService postDesService;

    private static Logger log = LoggerFactory.getLogger(PostcontentDataMove.class);

    public void dataMove() throws Exception {

        List<Post> postList = postService.selectAllPost();
        for (Post post : postList) {
            //转化postcontent字段
            String postcontent = post.getPostcontent();
            Document doc = Jsoup.parse(postcontent);
            log.debug(doc.toString());
            //封装所有img实体
            List<PostContent> postContents = parseForImg(doc);
            //封装视频（若存在）
            parseForVideo(post, postContents);

            Gson gson = new Gson();
            String finalPostContent = gson.toJson(postContents);
            log.debug("[添加转化成字符串的newPostContent]" + finalPostContent);

            //更新post对象的postcontent字段
            post.setPostcontent(finalPostContent);

            postDesService.addFromPost(post);
        }

        //最后把post集合全部插入yw_post_des表中
//        postDesService.batchAdd(postList);

    }

    private void parseForVideo(Post post, List<PostContent> postContents) {
        //判断是否有视频,
        if (1 == post.getType()) {
            //如果是原生视频贴则进行下面的操作
            List<Video> videoList = videoService.queryByPostid(post.getId());
            if (ListUtil.isNotEmpty(videoList)) {
                //如果有视频，则添加到集合中
                String vid = videoList.get(0).getVideourl();    //获取视频vid
                PostContent pc = new PostContent();
                pc.setType(2);
                pc.setOrderid(postContents.size() + 1);
                pc.setValue(vid);
                pc.setDir("");
                pc.setWh("");

                postContents.add(pc);
            }
        }
    }


    /**
     * 解析所有的img标签，封装成PostContent实体
     *
     * @param doc
     * @throws Exception
     */
    private List<PostContent> parseForImg(Document doc) throws Exception {
        Elements imgElms = doc.select("img");
        log.debug("【imgElms】:" + imgElms.toString());
        List<PostContent> imgContents = new ArrayList<>();
        if (imgElms.size() > 0) {
            for (int i = 0; i < imgElms.size(); i++) {

                PostContent postContent = new PostContent();
                postContent.setType(1);
                //获取图片的url
                String url = imgElms.get(i).attr("src");
                log.debug("url:" + url);
                postContent.setValue(url);
                //获取图片的宽高
                String wh = SimpleImageInfo.getWH(url);
                log.debug("wh:" + wh);
                postContent.setWh(wh);

                postContent.setOrderid(i);
                postContent.setDir("");

                imgContents.add(postContent);
            }
            log.debug("【img的实体列表】：" + imgContents.toString());
        }
        return imgContents;
    }

    private static void parseForP(Document doc, List<PostContent> allList) throws Exception {
        Elements pElms = doc.select("p:has(span),p:has(img)");
        if (pElms.size() > 0) {
            for (int i = 0; i < pElms.size(); i++) {

                //判断p标签包含的标签
                String innerHtml = pElms.get(i).html();
                if (innerHtml.contains("img")) {
                    //获取内部的img
                    Elements imgs = pElms.get(i).select("img");
                    //若存在多个img，则遍历处理
                    for (int j = 0; j < imgs.size(); j++) {

                        PostContent pc = new PostContent();
                        pc.setType(1);
                        String url = imgs.get(j).attr("src");
                        pc.setValue(url);
                        String wh = SimpleImageInfo.getWH(url);
                        log.debug("wh:" + wh);
                        pc.setWh(wh);

                        pc.setOrderid(allList.size() + 1);
                        log.debug(pc.toString());

                        allList.add(pc);
                    }
                } else {
                    //获取内部的span
                    Elements spans = pElms.get(i).select("span");

                    for (int j = 0; j < spans.size(); j++) {

                        PostContent pc = new PostContent();
                        pc.setType(0);
                        String text = spans.get(j).text();
                        pc.setValue(text);
                        pc.setOrderid(allList.size() + 1);
                        log.debug(pc.toString());

                        allList.add(pc);
                    }
                }
            }
        }
        log.debug("【allList的值】：" + allList.toString());
    }


}

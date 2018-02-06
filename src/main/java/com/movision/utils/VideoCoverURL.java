package com.movision.utils;

import com.alibaba.fastjson.JSONObject;
import com.movision.facade.apsaraVideo.AliVideoFacade;
import com.movision.mybatis.post.entity.PostVo;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/6/22 10:26
 * 该工具类用于所有有时效限制的视频封面的图片url获取
 */
@Service
public class VideoCoverURL {

    private static final Logger log = LoggerFactory.getLogger(VideoCoverURL.class);

    @Autowired
    private AliVideoFacade aliVideoFacade;

    public JSONArray getVideoCover(JSONArray jsonArray) throws NoSuchAlgorithmException, InvalidKeyException, IOException {

        log.info("请求帖子详情时开始轮训所有模块中的视频模块，请求最新的视频封面url--->");

        //解析JSONArray获取所有视频
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject moduleobj = JSONObject.parseObject(jsonArray.get(i).toString());
            Integer type = (Integer) moduleobj.get("type");//帖子模块类型 0 文字 1 图片 2 视频
            String value = (String) moduleobj.get("value");//模块value
            Integer orderid = (Integer) moduleobj.get("orderid");//模块排序id
            if (type == 2){
                //如果当前模块是视频的话，检测当前视频状态
                String vid = value;

                //生成请求的url，类似：GetVideoPlayAuth
                String url = aliVideoFacade.generateRequestUrl("GetVideoInfo", vid);
                Map<String, String> reMap = aliVideoFacade.doGet(url);
                String result = "";
                if (!reMap.isEmpty()) {
                    if ("200".equals(reMap.get("status"))) {
                        result = reMap.get("result").toString();
                    }
                }

                JSONObject res = JSONObject.parseObject(result);
                String str = res.get("Video").toString();// str如下：
//                        {
//                            "VideoId": "93ab850b4f6f44eab54b6e91d24d81d4",
//                                "Title": "阿里云VOD视频标题",
//                                "Description": "阿里云VOD视频描述",
//                                "Duration": 135.6,
//                                "CoverURL": "https://image.example.com/coversample.jpg",
//                                "Status": "Normal",
//                                "CreateTime": "2017-03-10 12:45:56",
//                                "ModifyTime": "2017-03-20 10:25:06",
//                                "Size": 10897890,
//                                "Snapshots": [{"https://image.example.com/snapshotsample1.jpg"}, {"https://image.example.com/snapshotsample2.jpg"}],
//                                "CateId": 78,
//                                "CateName": "分类名",
//                                "Tags": ["标签1", "标签2"]
//                        }
                JSONObject obj = JSONObject.parseObject(str);
                String status = obj.get("Status").toString();
                String CoverURL = obj.get("CoverURL").toString();

                if (status.equals("Normal")){
                    //视频转码成功，正常播放
                    //正常的视频不给flag赋1了
                    jsonArray.remove(i);//先移除
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", type);
                    map.put("value", vid);
                    map.put("wh", CoverURL);
                    map.put("dir", "");
                    map.put("orderid", orderid);
                    jsonArray.add(i, map);//再加入
                }
            }
        }

        return jsonArray;
    }

    public List<PostVo> getVideoCoverByList(List<PostVo> list) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        for (int j = 0; j < list.size(); j++) {
            PostVo vo = list.get(j);
            String postcontentstr = vo.getPostcontent();
            JSONArray jsonArray = JSONArray.fromObject(postcontentstr);

            //解析JSONArray获取所有视频
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject moduleobj = JSONObject.parseObject(jsonArray.get(i).toString());
                Integer type = (Integer) moduleobj.get("type");//帖子模块类型 0 文字 1 图片 2 视频
                String value = (String) moduleobj.get("value");//模块value
                Integer orderid = (Integer) moduleobj.get("orderid");//模块排序id
                if (type == 2) {
                    //如果当前模块是视频的话，检测当前视频状态
                    String vid = value;

                    //生成请求的url，类似：GetVideoPlayAuth
                    String url = aliVideoFacade.generateRequestUrl("GetVideoInfo", vid);
                    Map<String, String> reMap = aliVideoFacade.doGet(url);
                    String result = "";
                    if (!reMap.isEmpty()) {
                        if ("200".equals(reMap.get("status"))) {
                            result = reMap.get("result").toString();
                        }
                    }

                    JSONObject res = JSONObject.parseObject(result);
                    String str = res.get("Video").toString();// str如下：
//                        {
//                            "VideoId": "93ab850b4f6f44eab54b6e91d24d81d4",
//                                "Title": "阿里云VOD视频标题",
//                                "Description": "阿里云VOD视频描述",
//                                "Duration": 135.6,
//                                "CoverURL": "https://image.example.com/coversample.jpg",
//                                "Status": "Normal",
//                                "CreateTime": "2017-03-10 12:45:56",
//                                "ModifyTime": "2017-03-20 10:25:06",
//                                "Size": 10897890,
//                                "Snapshots": [{"https://image.example.com/snapshotsample1.jpg"}, {"https://image.example.com/snapshotsample2.jpg"}],
//                                "CateId": 78,
//                                "CateName": "分类名",
//                                "Tags": ["标签1", "标签2"]
//                        }
                    JSONObject obj = JSONObject.parseObject(str);
                    String status = obj.get("Status").toString();
                    String CoverURL = obj.get("CoverURL").toString();

                    if (status.equals("Normal")) {
                        //视频转码成功，正常播放
                        //正常的视频不给flag赋1了
                        jsonArray.remove(i);//先移除
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", type);
                        map.put("value", vid);
                        map.put("wh", CoverURL);
                        map.put("dir", "");
                        map.put("orderid", orderid);
                        jsonArray.add(i, map);//再加入
                    }
                }
            }
            vo.setPostcontent(jsonArray.toString());
            list.set(j, vo);
        }
        return list;
    }
}

package com.movision.facade.label;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.index.FacadeHeatValue;
import com.movision.facade.index.FacadePost;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.circle.entity.CircleCount;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.followLabel.entity.FollowLabel;
import com.movision.mybatis.followLabel.service.FollowLabelService;
import com.movision.mybatis.followUser.service.FollowUserService;
import com.movision.mybatis.footRank.entity.FootRank;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postLabel.entity.*;
import com.movision.mybatis.postLabel.service.PostLabelService;
import com.movision.mybatis.postLabelRelation.service.PostLabelRelationService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userDontLike.entity.UserDontLike;
import com.movision.mybatis.userDontLike.service.UserDontLikeService;
import com.movision.utils.DateUtils;
import com.movision.utils.baidu.SnCal;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Author zhanglei
 * @Date 2017/7/25 14:47
 */
@Service
public class LabelFacade {
    private static Logger log = LoggerFactory.getLogger(LabelFacade.class);
    @Autowired
    private PostLabelService postLabelService;
    @Autowired
    private PostLabelRelationService postLabelRelationService;
    @Autowired
    private FollowLabelService followLabelService;
    @Autowired
    private FacadePost facadePost;
    @Autowired
    private UserDontLikeService userDontLikeService;
    @Autowired
    private FacadeHeatValue facadeHeatValue;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private FollowUserService followUserService;

    /**
     * 我的--关注--关注的标签，点击关注调用的关注的标签列表返回接口
     */
    public List<PostLabelVo> getMineFollowLabel(String userid, Paging<PostLabelVo> pager){
        //查询当前用户所关注的所有标签列表
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        List<PostLabelVo> followLabelList = postLabelService.findAllMineFollowLabel(parammap, pager);

        //遍历查询当前用户是否关注过该标签
        for (int i=0; i<followLabelList.size(); i++){
            PostLabelVo vo = followLabelList.get(i);
            int labelid = vo.getLabelid();
            parammap.put("labelid", labelid);
            int sum = followLabelService.yesOrNo(parammap);
            vo.setIsfollow(sum);
            followLabelList.set(i, vo);
        }

        return followLabelList;
    }

    /**
     * 点击标签页上部分
     *
     * @param labelid
     * @return
     */
    public PostLabelTz labelPage(int labelid) {
        //查询头像和名称
        PostLabelTz postLabel = postLabelService.queryName(labelid);
        //根据id查询帖子数量
        int count = postLabelRelationService.labelPost(labelid);
        postLabel.setCount(count);
        //多少人关注
        int follow = postLabelRelationService.followlabel(labelid);
        postLabel.setFollow(follow);
        //该用户有没有关注该标签
        Map map = new HashMap();
        map.put("userid", ShiroUtil.getAppUserID());
        map.put("labelid", labelid);
        int isfollow = postLabelRelationService.isFollowLabel(map);
        postLabel.setIsfollow(isfollow);
        return postLabel;
    }


    /**
     * 点击标签页下部分
     *
     * @param type
     * @param paging
     * @param labelid
     * @return
     */
    public List postLabelList(int type, Paging<PostVo> paging, int labelid) {
        List list = null;
        //根据标签id查询帖子
        List<Integer> postid = postLabelRelationService.postList(labelid);
        if (type == 1) {//推荐
            //根据所有的id查询帖子按热度排序
            list = postLabelRelationService.findAllPostHeatValue(postid, paging);
            list = labelResult(list);
        } else if (type == 2) {//最新
            //根据所有的id查询帖子按时间排序
            list = postLabelRelationService.findAllPost(postid, paging);
            list = labelResult(list);
        } else if (type == 3) {//精华
            list = postLabelRelationService.findAllPostIseecen(postid, paging);
            list = labelResult(list);
        }
        return list;
    }


    /**
     * 关注标签
     *
     * @param userid
     * @param labelid
     * @return
     */
    public int attentionLabel(int userid, int labelid) {
        Map map = new HashMap();
        //查询当前用户有没有关注过该标签
        map.put("userid", userid);
        map.put("labelid", labelid);
        int result = followLabelService.yesOrNo(map);
        if (result == 0) {//没有关注
            FollowLabel followLabel = new FollowLabel();
            followLabel.setUserid(userid);
            followLabel.setLabelid(labelid);
            followLabel.setIntime(new Date());
            int count = followLabelService.insertSelective(followLabel);
            //增加关注标签数量
            followLabelService.updatePostLabel(labelid);
            //增加用户总关注数attention
            userService.updateUserAttention(userid);//关注人
            //增加标签热度
            facadeHeatValue.addLabelHeatValue(1, labelid, String.valueOf(userid));
            return 0;
        } else {//关注过了
            return 1;
        }
    }

    /**
     * 返回数据
     *
     * @param
     * @param
     * @return
     */
    public List labelResult(List<PostVo> list) {
        if (list != null) {
            facadePost.findPostLabel(list);
            facadePost.countView(list);
            facadePost.findAllCircleName(list);
        }
        return list;
    }


    /**
     * 标签达人
     *
     * @param labelid
     * @return
     */
    public List tagMan(int labelid) {
        //根据id查询用的最多的用户
        List<PostLabelCount> labelCounts = postLabelService.queryCountLabelName(labelid);
        return labelCounts;
    }


    /**
     * 圈子标签上半部分
     *
     * @param
     * @return
     */
    public CircleVo queryCircleByPostid(String circleid, String userid) {
        //根据id查询圈子所有
        List<DBObject> listmongodba;
        List<PostVo> posts = new ArrayList<>();
        CircleVo circleVo = postLabelService.queryCircleByPostid(Integer.parseInt(circleid));
        List<User> circleManager = postLabelService.queryCircleManager(Integer.parseInt(circleid));
        circleVo.setCirclemanagerlist(circleManager);
        //查询所有帖子
        List<PostVo> postVos = postService.findAllPostCrile(Integer.parseInt(circleid));
        //根据圈子查询今日这个圈子的发帖数
        if (StringUtil.isNotEmpty(userid)) {
            //不为空查询当前用户未看过的总更新数
            listmongodba = facadePost.userRefulshListMongodbs(Integer.parseInt(userid));//查询mongodb中用户看过的帖子列表
            if (listmongodba.size() != 0) {
                for (int j = 0; j < listmongodba.size(); j++) {
                    PostVo post = new PostVo();
                    post.setId(Integer.parseInt(listmongodba.get(j).get("postid").toString()));
                    posts.add(post);//把mongodb转为post实体
                }
                postVos.removeAll(posts);
                circleVo.setPostnewnum(postVos.size());
            } else {
                circleVo.setPostnewnum(postVos.size());
            }
        } else {
            circleVo.setPostnewnum(postVos.size());
        }
        //查询圈子下所有帖子用的标
        List<PostLabel> postLabels = postLabelService.queryLabelCircle(Integer.parseInt(circleid));
        circleVo.setPostLabels(postLabels);
        //圈子关注数
        int followCircle = postLabelService.followCircle(Integer.parseInt(circleid));
        circleVo.setFollownum(followCircle);
        //查询用户是否关注该圈子
        Map map = new HashMap();
        map.put("circleid", circleid);
        if (StringUtil.isNotEmpty(userid)) {
            map.put("userid", Integer.parseInt(userid));
            int isfollow = postLabelService.isFollowCircleid(map);
            circleVo.setIsfollow(isfollow);
        } else {
            circleVo.setIsfollow(0);
        }
        return circleVo;
    }

    /**
     * 圈子详情
     *
     * @param circleid
     * @return
     */
    public CircleVo queryCircleDetail(String circleid) {
        //根据id查询圈子所有
        CircleVo circleVo = postLabelService.queryCircleByPostid(Integer.parseInt(circleid));
        List<User> circleManager = postLabelService.queryCircleManager(Integer.parseInt(circleid));
        circleVo.setCirclemanagerlist(circleManager);
        return circleVo;
    }

    /**
     * 圈子标签下半部分
     *
     * @param type
     * @param paging
     * @param circleid
     * @return
     */
    public List queryCircleBotton(int type, Paging<PostVo> paging, String circleid, String labelid) {
        List<PostVo> list = null;
        Map map = new HashMap();
        map.put("circleid", circleid);
        if (type == 1) {//最热
            if (labelid == null) {
                list = postLabelService.findAllHotPost(Integer.parseInt(circleid), paging);
                list = labelResult(list);
            } else {
                map.put("labelid", labelid);
                list = postLabelService.findAllLabelHotPost(map, paging);
                list = labelResult(list);
            }
        } else if (type == 2) {//最新
            if (labelid == null) {
                list = postLabelService.findAllNewPost(Integer.parseInt(circleid), paging);
                list = labelResult(list);
            } else {
                map.put("labelid", labelid);
                list = postLabelService.findAllLabelNewPost(map, paging);
                list = labelResult(list);
            }
        } else if (type == 3) {//精华
            if (labelid == null) {
                list = postLabelService.findAllIsencePost(Integer.parseInt(circleid), paging);
                list = labelResult(list);
            } else {
                map.put("labelid", labelid);
                list = postLabelService.findAllLabelIsessenPost(map, paging);
                list = labelResult(list);
            }
        }
        return list;
    }


    /**
     * 圈子达人
     *
     * @param circleid
     * @return
     */
    public List circleOfPeople(int circleid) {
        List<CircleCount> circleCounts = postLabelService.queryCirclePeople(circleid);
        return circleCounts;
    }


    /**
     * 首页点x
     *
     * @param type
     */
    public int userDontLike(int type, int userid, int postid) {
        int count = 0;
        if (type == 1) {//内容差
            //查询该帖子的热度值
            int heat_value = postLabelService.queryPostHeatValue(postid);
            if (heat_value >= 10) {
                //根据帖子id降低热度值
                count = postLabelService.updatePostHeatValue(postid);
            } else {
                count = postLabelService.heatvale(postid);
            }
        } else if (type == 2) {//不喜欢该作者
            //根据帖子id查询作者
            int userids = postLabelService.queryUserid(postid);
            //根据id查询热度
            int heats = postLabelService.queryUserHeatValue(userids);
            if (heats >= 10) {
                //根据id降低热度值
                count = postLabelService.updateUserHeatValue(userids);
            } else {
                count = postLabelService.userHeatVale(userids);
            }
        } else if (type == 3) {//不喜欢该圈子
            //根据帖子id查询该圈子
            int circleid = postLabelService.queryCircleid(postid);
            //根据圈子id查询帖子
            List<PostVo> list = postLabelService.queryCircleByPost(circleid);
            for (int i = 0; i < list.size(); i++) {
                int post = list.get(i).getId();
                //查询该帖子的热度值
                int heat_value = postLabelService.queryPostHeatValue(post);
                if (heat_value >= 10) {
                    //根据帖子id降低热度值
                    count = postLabelService.updatePostHeatValue(post);
                } else {
                    count = postLabelService.heatvale(post);
                }
            }
        } else if (type == 4) {//就是不喜欢
            //查询该帖子的热度值
            int heat_value = postLabelService.queryPostHeatValue(postid);
            if (heat_value >= 10) {
                //根据帖子id降低热度值
                count = postLabelService.updatePostHeatValue(postid);
            } else {
                count = postLabelService.heatvale(postid);
            }
        }
        //插入mongodb
        UserDontLike userDontLike = new UserDontLike();
        userDontLike.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
        userDontLike.setIntime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        userDontLike.setPostid(postid);
        userDontLike.setUserid(userid);  //不登录的情况下，返回0
        userDontLike.setType(type);
        userDontLikeService.insert(userDontLike);
        return count;
    }


    /**
     * 取消关注标签
     *
     * @param userid
     * @param labelid
     * @return
     */
    public int cancelFollowLabel(String userid, String labelid) {
        //定义一个返回标志位
        int mark = 0;
        Map map = new HashMap();
        map.put("userid", userid);
        map.put("labelid", labelid);
        int result = followLabelService.cancleLabel(map);
        if (result == 1) {
            //标签关注-1
            followLabelService.updatePostLabelLess(Integer.parseInt(labelid));
            //用户关注数-1
            followUserService.updateUserAttention(Integer.parseInt(userid));
            mark = 1;
        } else {
            mark = -1;
        }
        return mark;
    }

    /**
     * 获取足迹地图中国内的地理标签
     */
    public Map<String, Object> getfootmap(String userid) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        Map<String, Object> map = new HashMap<>();

        //首先查询当前用户发的所有帖子中包含的地理标签列表
        List<GeographicLabel> allGeographicLabelList = postLabelService.getfootmap(Integer.parseInt(userid));

        //使用百度sdk遍历根据所有的地名换算成经纬度
        for (int i=0; i<allGeographicLabelList.size(); i++){
            GeographicLabel GLvo = allGeographicLabelList.get(i);
            String name = allGeographicLabelList.get(i).getName();//市名
            String sn = SnCal.getSn(name);
            String ak = PropertiesLoader.getValue("baidu.ak");

            //通过http的get请求url
            String baiduurl = PropertiesLoader.getValue("baidu.url");

            //拼接百度接口的请求url
            String url = baiduurl + "?address=" + name + "&output=json&ak=" + ak + "&sn=" + sn;
            String result = "";
            try {
                // 根据地址获取请求
                HttpGet httpGet = new HttpGet(url);//这里发送get请求
                // 获取当前客户端对象
                CloseableHttpClient httpclient = HttpClients.createDefault();
                // 通过请求对象获取响应对象
                CloseableHttpResponse response = httpclient.execute(httpGet);

                // 判断网络连接状态码是否正常(0--200都数正常)
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                }
                log.info("返回的json结果集>>>>>>>>>>>>>>>>>>>>>" + result);

                JSONObject jsonObject = JSONObject.parseObject(result);
                int status = (int) jsonObject.get("status");
                if (status == 0) {
                    JSONObject re = (JSONObject) JSON.toJSON(jsonObject.get("result"));
                    JSONObject location = (JSONObject) JSON.toJSON(re.get("location"));
                    BigDecimal lng = (BigDecimal) JSON.toJSON(location.get("lng"));
                    BigDecimal lat = (BigDecimal) JSON.toJSON(location.get("lat"));
                    log.info("经纬度>>>>>>>>" + lng + ">>>>>>>" + lat);
                    GLvo.setLng(lng);
                    GLvo.setLat(lat);
                } else if (status == 1) {
                    //无相关结果
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            allGeographicLabelList.set(i, GLvo);
        }
        map.put("allGeographicLabelList", allGeographicLabelList);

        //增加足迹点个数
        map.put("footsum", allGeographicLabelList.size());

        //查询并计算当前用户的足迹排名
        List<FootRank> footRankList = postLabelService.queryFootMapRank(Integer.parseInt(userid));
        int rank = 0;
        for (int i=0; i<footRankList.size(); i++){
            rank++;
            if (footRankList.get(i).getUserid() == Integer.parseInt(userid)) {
                continue;//跳出剩余所有循环
            }
        }
        map.put("rank", rank);

        return map;
    }
}

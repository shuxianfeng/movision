package com.movision.facade.index;

import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author shuxf
 * @Date 2017/1/19 15:43
 */
@Service
public class FacadePost {

    private static Logger log = LoggerFactory.getLogger(FacadePost.class);

    @Autowired
    private PostService postService;

    @Autowired
    private CircleService circleService;

    public PostVo queryPostDetail(String postid, String type) {
        PostVo vo = postService.queryPostDetail(Integer.parseInt(postid));
        if (type.equals("1")) {
            String url = postService.queryVideoUrl(Integer.parseInt(postid));
            vo.setVideourl(url);
        }
        return vo;
    }

    public Map<String, Object> queryPastPostDetail(String date) {
        Map<String, Object> parammap = new HashMap();
        Map<String, Object> map = new HashMap();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (!StringUtils.isEmpty(date)) {
            parammap.put("date", date);
        } else {
            parammap.put("date", sdf.format(d));
        }
        parammap.put("days", 0);
        List<PostVo> currentList = postService.queryPastPostList(parammap);//选择的日期这天
        parammap.put("days", 1);
        List<PostVo> dayagoList = postService.queryPastPostList(parammap);//选择的日期前一天
        parammap.put("days", 2);
        List<PostVo> twodayagoList = postService.queryPastPostList(parammap);//选择的日期前两天

        map.put("currentList", currentList);
        map.put("dayagoList", dayagoList);
        map.put("twodayagoList", twodayagoList);
        return map;
    }

    public List<PostVo> queryCircleIndex2(String pageNo, String pageSize, String circleid) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Post> pager = new Paging<Post>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));

        return postService.queryPostList(pager, circleid);
    }


    public int releasePost(HttpServletRequest request, String userid, String level, String circleid, String title,
                           String postcontent, String isactive, MultipartFile file) {

        //这里需要根据userid判断当前登录的用户是否有发帖权限
        //查询当前圈子的开放范围
        int scope = circleService.queryCircleScope(Integer.parseInt(circleid));
        //查询当前圈子的所有者(返回所有者的用户id)
        int owner = circleService.queryCircleOwner(Integer.parseInt(circleid));
        int lev = Integer.parseInt(level);//用户等级

        //拥有权限的：1.该圈所有人均可发帖 2.该用户是该圈所有者 3.所有者和大V可发时，发帖用户即为大V
        if (scope == 0 || Integer.parseInt(userid) == owner || (scope == 1 && lev >= 1)) {

            try {
                log.info("APP前端用户开始请求发普通帖");
                Post post = new Post();
                post.setCircleid(Integer.parseInt(circleid));
                post.setTitle(title);
                post.setPostcontent(postcontent);
                post.setZansum(0);//新发帖全部默认为0次
                post.setCommentsum(0);//被评论次数
                post.setForwardsum(0);//被转发次数
                post.setCollectsum(0);//被收藏次数
                post.setIsactive(Integer.parseInt(isactive));//是否为活动 0 帖子 1 活动
                post.setType(0);//帖子类型 0 普通帖 1 原生优质帖
                post.setIshot(0);//是否设为热门：默认0否
                post.setIsessence(0);//是否设为精选：默认0否
                post.setIsessencepool(0);//是否设为精选池中的帖子
                post.setIntime(new Date());//帖子发布时间
                post.setTotalpoint(0);//帖子综合评分
                post.setIsdel(0);//上架

                //上传图片到本地服务器
                String savedFileName = "";
                if (!file.isEmpty()) {
                    String fileRealName = file.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedFileName = FileId.toString().replace("-", "").concat(fileSuffix);
//                    String savedDir = request.getSession().getServletContext().getRealPath("/images/post/coverimg");
                    String savedDir = request.getSession().getServletContext().getRealPath("");

                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision-0.1.0后缀movision-0.1.0去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 15);

                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/post/coverimg";

//                    File savedFile = new File(savedDir, savedFileName);
                    File savedFile = new File(combinpath, savedFileName);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        file.transferTo(savedFile);  //转存文件
                    }
                }

                //暂定为tomcat本地tomcat服务器上的路径
                String imgurl = "http://120.77.214.187:8100/images/post/coverimg/" + savedFileName;

                post.setCoverimg(imgurl);

                return postService.releasePost(post);

            } catch (Exception e) {
                log.error("系统异常，APP普通帖发布失败");
                e.printStackTrace();
                return 0;
            }
        } else {
            log.info("该用户不具备发帖权限");
            return -1;
        }
    }

    public int updatePostByZanSum(String id) {
        int type = postService.updatePostByZanSum(Integer.parseInt(id));
        if (type == 1) {
            return postService.queryPostByZanSum(Integer.parseInt(id));
        }
        return -1;
    }

}

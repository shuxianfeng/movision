package com.movision.facade.boss;

import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.share.service.SharesService;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/7 9:16
 */
@Service
public class PostFacade {
    @Autowired
    PostService postService;

    @Autowired
    CircleService circleService;

    @Autowired
    UserService userService;

    @Autowired
    SharesService sharesService;

    @Autowired
    RewardedService rewardedService;

    @Autowired
    AccusationService accusationService;


    /**
     * 后台管理-查询帖子列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Object> queryPostByList(String pageNo, String pageSize) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Post> pager = new Paging<Post>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<Post> list = postService.queryPostByList(pager);
        List<Object> rewardeds = new ArrayList<Object>();
        for (int i = 0; i < list.size(); i++) {
            PostList postList = new PostList();
            Integer circleid = list.get(i).getCircleid();//获取到圈子id
            String phone = circleService.queryCircleByPhone(circleid);//获取圈子中的用户手机号
            String nickname = userService.queryUserByNickname(phone);//获取发帖人
            Integer share = sharesService.querysum(list.get(i).getId());//获取分享数
            Integer rewarded = rewardedService.queryRewardedBySum(list.get(i).getId());//获取打赏积分
            Integer accusation = accusationService.queryAccusationBySum(list.get(i).getId());//查询帖子举报次数
            postList.setTitle(list.get(i).getTitle());
            postList.setNickname(nickname);
            postList.setCollectsum(list.get(i).getCollectsum());
            postList.setShare(share);
            postList.setCommentsum(list.get(i).getCommentsum());
            postList.setZansum(list.get(i).getZansum());
            postList.setRewarded(rewarded);
            postList.setAccusation(accusation);
            postList.setIsessence(list.get(i).getIsessence());
            rewardeds.add(postList);
        }
        return rewardeds;
    }
}

package com.movision.fsearch.service.impl;

        import com.movision.common.util.ShiroUtil;
        import com.movision.fsearch.pojo.ProductGroup;
        import com.movision.fsearch.pojo.spec.NormalSearchSpec;
        import com.movision.fsearch.service.IPostSearchService;
        import com.movision.fsearch.service.IWordService;
        import com.movision.fsearch.service.Searcher;
        import com.movision.fsearch.service.exception.ServiceException;
        import com.movision.fsearch.utils.*;
        import com.movision.mybatis.opularSearchTerms.entity.OpularSearchTerms;
        import com.movision.mybatis.opularSearchTerms.service.OpularSearchTermsService;
        import com.movision.mybatis.post.entity.PostSearchEntity;
        import com.movision.mybatis.post.service.PostService;
        import com.movision.mybatis.postAndUserRecord.service.PostAndUserRecordService;
        import com.movision.utils.DateUtils;
        import org.apache.commons.collections.map.HashedMap;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/27 16:32
 */
@Service
public class PostSearchService implements IPostSearchService {

    private static Logger log = LoggerFactory.getLogger(PostSearchService.class);

    @Autowired
    private IWordService wordService;

    @Autowired
    private OpularSearchTermsService opularSearchTermsService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostAndUserRecordService postAndUserRecordService;
    @Override
    public Map<String, Object> search(NormalSearchSpec spec)
            throws ServiceException {
        Map<String, Map<String, Object>> query = new HashMap<String, Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("spec", spec);

        //如果搜索的关键词不为空，则入库保存
        saveKeywordsInMongoDB(spec);

        // 向query中添加新的键值对：key=_s
        spec.setQ(StringUtil.emptyToNull(spec.getQ()));
        if (spec.getQ() != null) {
            String q = spec.getQ();
            result.put("q", q);
            List<String> words = wordService.segWords(q);
            if (!words.isEmpty()) {
                //以空格为分隔符，形成新的list<String>
                String formatQ = StringUtil.join(words, " ");
                query.put("_s", CollectionUtil.arrayAsMap("type", "phrase",
                        "value", formatQ));
            }
        }
        //设置排序字段和排序顺序（正序/倒序）
        List<Map<String, Object>> sortFields = this.setSortFields(spec, result);
        /**
         *  发起搜索请求
         *  table：对应fsearch中的movision_product.ini中的name;
         *  query: 表示上面封装的query;
         *  sort: 表示排序字段；
         *  offset: 分页起始值；
         *  limit: 每页数量；
         */
        Map<?, ?> psAsMap = (Map<?, ?>) Searcher.request(
                "search",
                CollectionUtil.arrayAsMap("table", "movision_post",
                        "query", JSONUtil.toJSONString(query),
                        "sort", JSONUtil.toJSONString(sortFields),
                        "offset", spec.getOffset(),
                        "limit", spec.getLimit()));

        //解析搜索的结果, 最终获取分页结果
        List<?> list = (List<?>) psAsMap.get("items");
        Pagination<PostSearchEntity, ProductGroup> ps = null;
        if (list.isEmpty()) {
            ps = Pagination.getEmptyInstance();
        } else {

            List<PostSearchEntity> products = makeProducts(list);
            @SuppressWarnings("unchecked")
            List<ProductGroup> productGroups = (List<ProductGroup>) psAsMap.get("groups");

            ps = new Pagination<PostSearchEntity, ProductGroup>(products, productGroups,
                    FormatUtil.parseInteger(psAsMap.get("total")),
                    FormatUtil.parseInteger(psAsMap.get("offset")),
                    FormatUtil.parseInteger(psAsMap.get("limit")));

        }
        result.put("ps", ps);
        return result;
    }

    @Override
    public List<PostSearchEntity> searchForPost(NormalSearchSpec spec)
            throws ServiceException {
        Map<String, Map<String, Object>> query = new HashMap<String, Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("spec", spec);
        //如果搜索的关键词不为空，则入库保存
        saveKeywordsInMongoDB(spec);
        // 向query中添加新的键值对：key=_s
        spec.setQ(StringUtil.emptyToNull(spec.getQ()));
        putQueryParam(spec, query, result);
        //设置排序字段和排序顺序（正序/倒序）
        List<Map<String, Object>> sortFields = this.setSortFields(spec, result);

        Map<?, ?> psAsMap = (Map<?, ?>) Searcher.request(
                "search",
                CollectionUtil.arrayAsMap("table", "movision_post",
                        "query", JSONUtil.toJSONString(query),
                        "sort", JSONUtil.toJSONString(sortFields),
                        "offset", spec.getOffset(),
                        "limit", 4));   //只返回前4条

        List<?> list = (List<?>) psAsMap.get("items");
        //返回值
        List<PostSearchEntity> products = new ArrayList<>();
        if (!list.isEmpty()) {
            products = makeProducts(list);
        }
        return products;
    }

    private void putQueryParam(NormalSearchSpec spec, Map<String, Map<String, Object>> query, Map<String, Object> result) {
        if (spec.getQ() != null) {
            String q = spec.getQ();
            result.put("q", q);
            List<String> words = wordService.segWords(q);
            if (!words.isEmpty()) {
                //以空格为分隔符，形成新的list<String>
                String formatQ = StringUtil.join(words, " ");
                query.put("_s", CollectionUtil.arrayAsMap("type", "phrase",
                        "value", formatQ));
            }
        }
    }


    /**
     * 把搜索的关键词存入mongoDB
     *
     * @param spec
     */
    private void saveKeywordsInMongoDB(NormalSearchSpec spec) {
        if (StringUtil.isNotBlank(spec.getQ())) {
            OpularSearchTerms opularSearchTerms = new OpularSearchTerms();
            opularSearchTerms.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
            opularSearchTerms.setIntime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            opularSearchTerms.setIsdel(0);
            opularSearchTerms.setKeywords(spec.getQ());
            opularSearchTerms.setUserid(ShiroUtil.getAppUserID());  //不登录的情况下，返回0
            opularSearchTermsService.insert(opularSearchTerms);
        }
    }

    /**
     * 根据所搜结果，生成PostSearchEntity集合（分页的第一个参数）
     *
     * @param list
     * @return
     */
    private List<PostSearchEntity> makeProducts(List<?> list) {
        List<PostSearchEntity> products = new ArrayList<PostSearchEntity>(list.size());
        for (Object item : list) {
            Map<?, ?> itemAsMap = (Map<?, ?>) item;
            PostSearchEntity product = new PostSearchEntity();
            {
                //获取帖子/活动的id
                Integer id = FormatUtil.parseInteger(itemAsMap.get("id"));
                setProductParam(itemAsMap, product, id);
                //获取开始和结束日期
                Date begin = getBeginDate(itemAsMap, product);
                Date end = getEndDate(itemAsMap, product);
                //获取是否是活动标识
                Integer isactive = FormatUtil.parseInteger(itemAsMap.get("isactive"));
                product.setIsactive(isactive);
                //计算活动结束日期 和 活动的参与总人数
                calcActivityEnddaysAndPartSum(product, begin, end, isactive, id);
            }
            products.add(product);
        }
        return products;
    }

    private Date getEndDate(Map<?, ?> itemAsMap, PostSearchEntity product) {
        Date end = null;
        if (null == itemAsMap.get("endtime1")) {
            product.setBegintime(null);
        } else {
            end = DateUtils.str2Date(String.valueOf(itemAsMap.get("endtime1")), "yyyyMMddHHmmss");
            product.setEndtime(end);
        }
        return end;
    }

    private Date getBeginDate(Map<?, ?> itemAsMap, PostSearchEntity product) {
        Date begin = null;
        if (null == itemAsMap.get("begintime1")) {
            product.setBegintime(null);
        } else {
            begin = DateUtils.str2Date(String.valueOf(itemAsMap.get("begintime1")), "yyyyMMddHHmmss");
            product.setBegintime(begin);
        }
        return begin;
    }

    private void setProductParam(Map<?, ?> itemAsMap, PostSearchEntity postSearchEntity, Integer id) {

        postSearchEntity.setId(id);
        postSearchEntity.setTitle(FormatUtil.parseString(itemAsMap.get("title")));
        postSearchEntity.setSubtitle(FormatUtil.parseString(itemAsMap.get("subtitle")));
        postSearchEntity.setIntime(DateUtils.str2Date(String.valueOf(itemAsMap.get("intime1")), "yyyyMMddHHmmss"));
        postSearchEntity.setPostcontent(FormatUtil.parseString(itemAsMap.get("postcontent")));
        postSearchEntity.setType(FormatUtil.parseInteger(itemAsMap.get("type")));
        postSearchEntity.setActivetype(FormatUtil.parseInteger(itemAsMap.get("activetype")));
        postSearchEntity.setCircleid(FormatUtil.parseInteger(itemAsMap.get("circleid")));
        postSearchEntity.setCirclename(FormatUtil.parseString(itemAsMap.get("circlename")));
        postSearchEntity.setActivefee(FormatUtil.parseDouble(itemAsMap.get("activefee")));
        postSearchEntity.setImgurl(FormatUtil.parseString(itemAsMap.get("imgurl")));
        postSearchEntity.setCoverimg(FormatUtil.parseString(itemAsMap.get("coverimg")));
        postSearchEntity.setUserid(FormatUtil.parseInteger(itemAsMap.get("userid")));
        postSearchEntity.setNickname(FormatUtil.parseString(itemAsMap.get("nickname")));
    }

    /**
     * 计算活动结束日期
     *
     * @param product
     * @param begin
     * @param end
     * @param isactive
     */
    private void calcActivityEnddaysAndPartSum(PostSearchEntity product, Date begin, Date end, Integer isactive, Integer postid) {
        if (isactive == 1) {
            //根据活动开始时间和结束时间，计算活动距离结束的剩余天数
            Date now = new Date();//活动当前时间
            if (now.before(begin)) {
                product.setEnddays(-1);//活动还未开始
            } else if (end.before(now)) {
                product.setEnddays(0);//活动已结束
            } else if (begin.before(now) && now.before(end)) {
                try {
                    log.error("计算活动剩余结束天数");
                    Long between_days = DateUtils.getBetweenDays(now, end);
                    product.setEnddays(Integer.parseInt(String.valueOf(between_days)));
                } catch (Exception e) {
                    log.error("计算活动剩余结束天数失败");
                    e.printStackTrace();
                }
            }
            //计算活动的参与总人数
            int partsum = postService.queryActivePartSum(postid);
            product.setPartsum(partsum);
        }
    }

    /**
     * 设置排序字段和排序顺序（正序/倒序）
     *
     * @param spec
     * @param result
     * @return
     */
    private List<Map<String, Object>> setSortFields(NormalSearchSpec spec, Map<String, Object> result)
    {
        List<Map<String, Object>> sortFields = new ArrayList<Map<String, Object>>(1);
        Map<String, Object> sortField = new HashMap<String, Object>(3);

        if (StringUtil.isNotEmpty(spec.getSort())) {

            String sort = spec.getSort();
            result.put("sort", spec.getSort());
            //这里设置是正序，还是倒序
            String sortorder = "true";
            if (StringUtil.isNotEmpty(spec.getSortorder())) {
                sortorder = spec.getSortorder();
                result.put("sortorder", spec.getSortorder());
            }
            /**
             * 若指定的排序字段sort不为空，那么就按照指定的排序字段排序，
             */
            if (sort.equals("intime1")) {
                sortField.put("field", sort);
                sortField.put("type", "LONG");
                sortField.put("reverse",
                        FormatUtil.parseBoolean(sortorder));

            } else {
                sortField.put("field", "id");
                sortField.put("type", "INT");
                sortField.put("reverse", FormatUtil.parseBoolean(true));
            }

        } else {
            //默认以id排序
            sortField.put("field", "id");
            sortField.put("type", "INT");
            sortField.put("reverse", FormatUtil.parseBoolean(true));
        }

        sortFields.add(sortField);
        return sortFields;
    }
    /**
     * 获取帖子热门搜索词和搜索历史记录
     *
     * @return
     */
    public Map<String, Object> getHotwordAndHistory() {

        Map map = new HashedMap();
        //展示前20条
        // map.put("hotWordList", searchPostRecordService.selectPostSearchHotWord());
        map.put("hotWordList", opularSearchTermsService.group());
        //展示前12条
        map.put("historyList", opularSearchTermsService.histroyWords(ShiroUtil.getAppUserID()));

        return map;
    }

    /**
     * 查询用户浏览历史
     * @return
     */
    public Map<String,Object> getUserLookingHistory(int page,int pageSize){
        Map map = new HashMap();
        map.put("userLookHistory",postAndUserRecordService.UserLookingHistory(ShiroUtil.getAppUserID(),page,pageSize));
        return map;

    }

    public Integer UpdateSearchIsdel(Integer userid) {
        return opularSearchTermsService.updateColData(userid);
    }

}

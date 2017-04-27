package com.movision.fsearch.service.impl;

import com.movision.common.util.ShiroUtil;
import com.movision.fsearch.pojo.ProductGroup;
import com.movision.fsearch.pojo.spec.PostSearchSpec;
import com.movision.fsearch.service.IPostSearchService;
import com.movision.fsearch.service.IWordService;
import com.movision.fsearch.service.Searcher;
import com.movision.fsearch.service.exception.ServiceException;
import com.movision.fsearch.utils.*;
import com.movision.mybatis.post.entity.PostSearchEntity;
import com.movision.mybatis.searchPostRecord.service.SearchPostRecordService;
import com.movision.utils.DateUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/27 16:32
 */
@Service
public class PostSearchService implements IPostSearchService {

    @Autowired
    private IWordService wordService;

    @Autowired
    private SearchPostRecordService searchPostRecordService;

    @Override
    public Map<String, Object> search(PostSearchSpec spec)
            throws ServiceException {
        Map<String, Map<String, Object>> query = new HashMap<String, Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("spec", spec);

        //如果搜索的关键词不为空，则入库保存
        if (StringUtil.isNotBlank(spec.getQ())) {

            searchPostRecordService.add(spec.getQ());
        }

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

            List<PostSearchEntity> products = new ArrayList<PostSearchEntity>(list.size());
            for (Object item : list) {
                Map<?, ?> itemAsMap = (Map<?, ?>) item;

                PostSearchEntity product = new PostSearchEntity();
                {
                    //此处处理应该展示的字段
                    product.setId(FormatUtil.parseInteger(itemAsMap.get("id")));
                    product.setTitle(FormatUtil.parseString(itemAsMap.get("title")));
                    product.setSubtitle(FormatUtil.parseString(itemAsMap.get("subtitle")));

                    product.setIntime(DateUtils.str2Date(String.valueOf(itemAsMap.get("intime1")), "yyyyMMddHHmmss"));

                    if (null == itemAsMap.get("begintime1")) {
                        product.setBegintime(null);
                    } else {
                        product.setBegintime(DateUtils.str2Date(String.valueOf(itemAsMap.get("begintime1")), "yyyyMMddHHmmss"));
                    }

                    if (null == itemAsMap.get("endtime1")) {
                        product.setBegintime(null);
                    } else {
                        product.setEndtime(DateUtils.str2Date(String.valueOf(itemAsMap.get("endtime1")), "yyyyMMddHHmmss"));
                    }

                    product.setPostcontent(FormatUtil.parseString(itemAsMap.get("postcontent")));
                    product.setIsactive(FormatUtil.parseInteger(itemAsMap.get("isactive")));
                    product.setType(FormatUtil.parseInteger(itemAsMap.get("type")));
                    product.setActivetype(FormatUtil.parseInteger(itemAsMap.get("activetype")));

                    product.setCircleid(FormatUtil.parseInteger(itemAsMap.get("circleid")));
                    product.setCirclename(FormatUtil.parseString(itemAsMap.get("circlename")));

                    product.setActivefee(FormatUtil.parseDouble(itemAsMap.get("activefee")));
                    product.setImgurl(FormatUtil.parseString(itemAsMap.get("imgurl")));
                    product.setCoverimg(FormatUtil.parseString(itemAsMap.get("coverimg")));

                }
                products.add(product);
            }
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

    /**
     * 设置排序字段和排序顺序（正序/倒序）
     *
     * @param spec
     * @param result
     * @return
     */
    private List<Map<String, Object>> setSortFields(PostSearchSpec spec, Map<String, Object> result) {
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
        map.put("hotWordList", searchPostRecordService.selectPostSearchHotWord());
        //展示前12条
        map.put("historyList", searchPostRecordService.selectHistoryRecord(ShiroUtil.getAppUserID()));

        return map;
    }

    public Integer UpdateSearchIsdel(Integer userid) {
        return searchPostRecordService.UpdateSearchIsdel(userid);
    }

}

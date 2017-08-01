package com.movision.fsearch.service.impl;

import com.movision.common.util.ShiroUtil;
import com.movision.fsearch.pojo.ProductGroup;
import com.movision.fsearch.pojo.spec.NormalSearchSpec;
import com.movision.fsearch.service.ILabelSearchService;
import com.movision.fsearch.service.IWordService;
import com.movision.fsearch.service.Searcher;
import com.movision.fsearch.service.exception.ServiceException;
import com.movision.fsearch.utils.*;
import com.movision.mybatis.labelSearchTerms.entity.LabelSearchTerms;
import com.movision.mybatis.labelSearchTerms.service.LabelSearchTermsService;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/8/1 10:05
 */
@Service
public class LabelSearchService implements ILabelSearchService {

    private static Logger log = LoggerFactory.getLogger(LabelSearchService.class);

    @Autowired
    private IWordService wordService;


    @Autowired
    private LabelSearchTermsService labelSearchTermsService;

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
                CollectionUtil.arrayAsMap("table", "movision_label",
                        "query", JSONUtil.toJSONString(query),
                        "sort", JSONUtil.toJSONString(sortFields),
                        "offset", spec.getOffset(),
                        "limit", spec.getLimit()));

        //解析搜索的结果, 最终获取分页结果
        List<?> list = (List<?>) psAsMap.get("items");
        Pagination<PostLabel, ProductGroup> ps = null;
        if (list.isEmpty()) {
            ps = Pagination.getEmptyInstance();
        } else {

            List<PostLabel> labels = makeProducts(list);
            @SuppressWarnings("unchecked")
            List<ProductGroup> productGroups = (List<ProductGroup>) psAsMap.get("groups");

            ps = new Pagination<PostLabel, ProductGroup>(labels, productGroups,
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
    private List<Map<String, Object>> setSortFields(NormalSearchSpec spec, Map<String, Object> result) {
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
     * 根据所搜结果，生成PostSearchEntity集合（分页的第一个参数）
     *
     * @param list
     * @return
     */
    private List<PostLabel> makeProducts(List<?> list) {
        List<PostLabel> products = new ArrayList<PostLabel>(list.size());
        for (Object item : list) {
            Map<?, ?> itemAsMap = (Map<?, ?>) item;
            PostLabel product = new PostLabel();
            {
                //获取标签id
                Integer id = FormatUtil.parseInteger(itemAsMap.get("id"));
                //封装标签实体
                setProductParam(itemAsMap, product, id);
            }
            products.add(product);
        }
        return products;
    }

    private void setProductParam(Map<?, ?> itemAsMap, PostLabel label, Integer id) {
        label.setId(id);
        label.setName(FormatUtil.parseString(itemAsMap.get("name")));
        label.setHeatValue(FormatUtil.parseInteger(itemAsMap.get("heat_value")));
        label.setIntime(DateUtils.str2Date(String.valueOf(itemAsMap.get("intime1")), "yyyyMMddHHmmss"));
        label.setUserid(FormatUtil.parseInteger(itemAsMap.get("userid")));
        label.setType(FormatUtil.parseInteger(itemAsMap.get("type")));
        label.setPhoto(FormatUtil.parseString(itemAsMap.get("photo")));
        label.setIsdel(FormatUtil.parseInteger(itemAsMap.get("isdel")));
        label.setCitycode(FormatUtil.parseString(itemAsMap.get("citycode")));
        label.setIsrecommend(FormatUtil.parseInteger(itemAsMap.get("isrecommend")));
        label.setUseCount(FormatUtil.parseInteger(itemAsMap.get("use_count")));
        label.setFans(FormatUtil.parseInteger(itemAsMap.get("fans")));
    }

    /**
     * 把搜索的关键词存入mongoDB
     *
     * @param spec
     */
    private void saveKeywordsInMongoDB(NormalSearchSpec spec) {
        if (StringUtil.isNotBlank(spec.getQ())) {
            LabelSearchTerms labelSearchTerms = new LabelSearchTerms();

            labelSearchTerms.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
            labelSearchTerms.setIntime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            labelSearchTerms.setName(spec.getQ());
            labelSearchTerms.setUserid(ShiroUtil.getAppUserID());  //不登录的情况下，返回0

            labelSearchTermsService.insert(labelSearchTerms);
        }
    }
}

package com.movision.fsearch.service.impl;

import com.movision.fsearch.pojo.ProductGroup;
import com.movision.fsearch.pojo.spec.GoodsSearchSpec;
import com.movision.fsearch.service.IGoodsSearchService;
import com.movision.fsearch.service.IWordService;
import com.movision.fsearch.service.Searcher;
import com.movision.fsearch.service.exception.ServiceException;
import com.movision.fsearch.utils.*;
import com.movision.mybatis.goods.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/20 16:02
 */
@Service
public class GoodsSearchService implements IGoodsSearchService {

    @Autowired
    private IWordService wordService;

    @Override
    public Map<String, Object> search(GoodsSearchSpec spec)
            throws ServiceException {
        Map<String, Map<String, Object>> query = new HashMap<String, Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("spec", spec);

        int protype = spec.getProtype();
        if (protype > 0) {
            Searcher.wrapEqualQuery(query, "protype", protype);
        }
        int brandid = spec.getBrandid();
        if (brandid > 0) {
            Searcher.wrapEqualQuery(query, "brandid", brandid);
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
                CollectionUtil.arrayAsMap("table", "movision_product",
                        "query", JSONUtil.toJSONString(query),
                        "sort", JSONUtil.toJSONString(sortFields),
                        "offset", spec.getOffset(),
                        "limit", spec.getLimit()));

        //解析搜索的结果, 最终获取分页结果
        List<?> list = (List<?>) psAsMap.get("items");
        Pagination<Goods, ProductGroup> ps = null;
        if (list.isEmpty()) {
            ps = Pagination.getEmptyInstance();
        } else {

            List<Goods> products = new ArrayList<Goods>(list.size());
            for (Object item : list) {
                Map<?, ?> itemAsMap = (Map<?, ?>) item;
                //封装搜索结果中的商品参数：id,name,onlinetime,price
                Goods product = new Goods();
                {
                    product.setId(FormatUtil.parseInteger(itemAsMap.get("id")));
                    product.setName(FormatUtil.parseString(itemAsMap.get("name")));
                    product.setOnlinetime(FormatUtil.parseDate(String.valueOf(itemAsMap.get("onlinetime")), TimeZone.getDefault()));
                    product.setPrice(FormatUtil.parseDouble(itemAsMap.get("price")));
                }
                products.add(product);
            }
            @SuppressWarnings("unchecked")
            List<ProductGroup> productGroups = (List<ProductGroup>) psAsMap.get("groups");

            ps = new Pagination<Goods, ProductGroup>(products, productGroups,
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
    private List<Map<String, Object>> setSortFields(GoodsSearchSpec spec, Map<String, Object> result) {
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
            if (sort.equals("price1")) {
                sortField.put("field", sort);
                sortField.put("type", "FLOAT");
                sortField.put("reverse",
                        FormatUtil.parseBoolean(sortorder));

            } else if (sort.equals("onlinetime1")) {
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

}

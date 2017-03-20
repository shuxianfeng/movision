package com.movision.fsearch.service.impl;

import com.movision.fsearch.pojo.ProductGroup;
import com.movision.fsearch.pojo.spec.GoodsSearchSpec;
import com.movision.fsearch.service.IGoodsSearchService;
import com.movision.fsearch.service.IWordService;
import com.movision.fsearch.service.Searcher;
import com.movision.fsearch.service.exception.ServiceException;
import com.movision.fsearch.utils.*;
import com.movision.mybatis.category.service.CategoryService;
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
    @Autowired
    private CategoryService categoryService;

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
        // TODO: 2017/3/20 这个是什么东西？
        spec.setQ(StringUtil.emptyToNull(spec.getQ()));
        if (spec.getQ() != null) {
            String q = spec.getQ();
            result.put("q", q);
            List<String> words = wordService.segWords(q);
            if (!words.isEmpty()) {
                String formatQ = StringUtil.join(words, " ");
                query.put("_s", CollectionUtil.arrayAsMap("type", "phrase",
                        "value", formatQ));
            }
        }

        /**
         * 设置排序字段
         */
        List<Map<String, Object>> sortFields = this.setSortFields(spec, result);

        Map<?, ?> psAsMap = (Map<?, ?>) Searcher.request(
                "search",
                CollectionUtil.arrayAsMap("table", "movision_product", "query",
                        JSONUtil.toJSONString(query), "sort",
                        JSONUtil.toJSONString(sortFields), "offset",
                        spec.getOffset(), "limit", spec.getLimit()));
        //解析搜索的结果
        List<?> list = (List<?>) psAsMap.get("items");
        Pagination<Goods, ProductGroup> ps = null;
        if (list.isEmpty()) {
            ps = Pagination.getEmptyInstance();
        } else {
            List<Goods> products = new ArrayList<Goods>(list.size());
            for (Object item : list) {
                Map<?, ?> itemAsMap = (Map<?, ?>) item;
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
     * 设置排序字段
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

            String sortorder = "true";

            if (StringUtil.isNotEmpty(spec.getSortorder())) {
                sortorder = spec.getSortorder();
                result.put("sortorder", spec.getSortorder());
            }

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
            sortField.put("field", "id");
            sortField.put("type", "INT");
            sortField.put("reverse", FormatUtil.parseBoolean(true));
        }

        sortFields.add(sortField);
        return sortFields;
    }

}

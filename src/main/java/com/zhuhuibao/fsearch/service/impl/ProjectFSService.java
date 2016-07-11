package com.zhuhuibao.fsearch.service.impl;

import com.zhuhuibao.fsearch.pojo.Member;
import com.zhuhuibao.fsearch.pojo.ProductGroup;
import com.zhuhuibao.fsearch.pojo.spec.ProjectSearchSpec;
import com.zhuhuibao.fsearch.service.IProjectService;
import com.zhuhuibao.fsearch.service.Searcher;
import com.zhuhuibao.fsearch.service.exception.ServiceException;
import com.zhuhuibao.fsearch.utils.*;
import com.zhuhuibao.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jianglz
 * @since 16/7/10.
 */
@Service
public class ProjectFSService implements IProjectService {
    @Override
    public Map<String, Object> searchProjectPage(ProjectSearchSpec spec) throws ServiceException {
        Map<String, Map<String, Object>> query = new HashMap<String, Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("spec", spec);

        genConditions(spec, query, result);

        List<Map<String, Object>> sortFields = genSorts(spec, result);

        Map<?, ?> psAsMap = (Map<?, ?>) Searcher.request(
                "search",
                CollectionUtil.arrayAsMap("table", "project", "query",
                        JSONUtil.toJSONString(query), "sort",
                        JSONUtil.toJSONString(sortFields), "offset",
                        spec.getOffset(), "limit", spec.getLimit()));

        List<?> list = (List<?>) psAsMap.get("items");
        Pagination<Map<String, String>, ProductGroup> items;
        if (list.isEmpty()) {
            items = Pagination.getEmptyInstance();
        } else {
            List<Map<String, String>> projects = new ArrayList<>(list.size());
            for (Object item : list) {
                Map<?, ?> itemAsMap = (Map<?, ?>) item;
                Map<String, String> map = new HashMap<>();
                map.put("id", FormatUtil.parseString(itemAsMap.get("id")));
                map.put("categoryName", FormatUtil.parseString(itemAsMap.get("categoryName")));
                map.put("city", FormatUtil.parseString(itemAsMap.get("cityName")));
                String startDate = FormatUtil.parseString(itemAsMap.get("startDate"));
                Date start = DateUtils.str2Date(startDate, "yyyy-MM-dd");
                startDate = DateUtils.date2Str(start, "yyyy-MM-dd");
                map.put("startDate", startDate);
                map.put("price", FormatUtil.parseString(itemAsMap.get("price")));
                map.put("address", FormatUtil.parseString(itemAsMap.get("address")));
                map.put("name", FormatUtil.parseString(itemAsMap.get("name")));
                String updateDate = FormatUtil.parseString(itemAsMap.get("updateDate"));
                Date update = DateUtils.str2Date(updateDate,"yyyy-MM-dd");
                updateDate = DateUtils.date2Str(update,"yyyy-MM-dd");
                map.put("updateDate", updateDate);
                map.put("publishDate", FormatUtil.parseString(itemAsMap.get("publishDate")));

                projects.add(map);
            }

            items = new Pagination<>(projects, null,
                    FormatUtil.parseInteger(psAsMap.get("total")),
                    FormatUtil.parseInteger(psAsMap.get("offset")),
                    FormatUtil.parseInteger(psAsMap.get("limit")));
        }
        result.put("items", items);

        return result;
    }

    /**
     * 组装排序条件
     *
     * @param spec
     * @param result
     * @return
     */
    private List<Map<String, Object>> genSorts(ProjectSearchSpec spec, Map<String, Object> result) {
        List<Map<String, Object>> sortFields = new ArrayList<>(1);
        Map<String, Object> sortField = new HashMap<>(3);
        if (StringUtil.isNotEmpty(spec.getSort())) {
            String sort = spec.getSort();
            result.put("sort", spec.getSort());
            String sortorder = "true";
            if (StringUtil.isNotEmpty(spec.getSortorder())) {
                sortorder = spec.getSortorder();
                result.put("sortorder", spec.getSortorder());
            }
            if (sort.equals("updateDate")) {
                sortField.put("field", sort);
                sortField.put("type", "LONG");
                sortField.put("reverse",
                        FormatUtil.parseBoolean(sortorder));
            }
        } else {
            sortField.put("field", "id");
            sortField.put("type", "INT");
            sortField.put("reverse", FormatUtil.parseBoolean(true));
        }
        sortFields.add(sortField);
        return sortFields;
    }

    /**
     * 组装搜索条件
     *
     * @param spec
     * @param query
     * @param result
     */
    private void genConditions(ProjectSearchSpec spec, Map<String, Map<String, Object>> query, Map<String, Object> result) {
        String province = spec.getProvince();
        if (StringUtil.isNotEmpty(province)) {
            Searcher.wrapEqualQuery(query, "province", province);
            result.put("province", province);
        }

        String city = spec.getCity();
        if (StringUtil.isNotEmpty(city)) {
            Searcher.wrapEqualQuery(query, "city", city);
            result.put("city", city);
        }

        String category = spec.getCategory();
        if (StringUtil.isNotEmpty(category)) {
            Searcher.wrapEqualQuery(query, "category", category);
            result.put("category", category);
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        //开始日期
        String startDateA = spec.getStartDateA();
        String startDateB = spec.getStartDateB();
        if (StringUtil.isNotEmpty(startDateA) && StringUtil.isNotEmpty(startDateB)) {

            try {
                Searcher.wrapDateRangeQuery(query, "startDate", sf.parse(startDateA), sf.parse(startDateB), true, true, "DAY");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            result.put("startDateA", startDateA);
            result.put("startDateB", startDateB);
        }
        // 竣工日期
        String endDateA = spec.getEndDateA();
        String endDateB = spec.getEndDateB();
        if (StringUtil.isNotEmpty(endDateA) && StringUtil.isNotEmpty(endDateB)) {
            try {
                Searcher.wrapDateRangeQuery(query, "endDate", sf.parse(endDateA), sf.parse(endDateB), true, true, "DAY");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            result.put("endDateA", endDateA);
            result.put("endDateB", endDateB);
        }

        spec.setQ(StringUtil.emptyToNull(spec.getQ()));
        if (spec.getQ() != null) {
            String q = spec.getQ();
            query.put("_s", CollectionUtil.arrayAsMap("type", "phrase",
                    "value", q));
            result.put("q", q);
        }
    }
}

package com.movision.fsearch.service.impl;

import com.movision.fsearch.pojo.spec.NormalSearchSpec;
import com.movision.fsearch.utils.FormatUtil;
import com.movision.fsearch.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/14 14:26
 */
@Service
public class CommonSearchService {

    /**
     * 设置排序字段和排序顺序（正序/倒序）
     *
     * @param spec
     * @param result
     * @return
     */
    public List<Map<String, Object>> setSortFields(NormalSearchSpec spec, Map<String, Object> result) {
        List<Map<String, Object>> sortFields = new ArrayList<Map<String, Object>>(1);
        Map<String, Object> sortField = new HashMap<String, Object>(4);

        if (StringUtil.isNotEmpty(spec.getSort())) {
            /**
             * 若指定的排序字段sort不为空，那么就按照指定的排序字段排序，
             */
            String sort = spec.getSort();
            result.put("sort", spec.getSort());
            //这里设置是正序，还是倒序
            String sortorder = "true";
            if (StringUtil.isNotEmpty(spec.getSortorder())) {
                sortorder = spec.getSortorder();
                result.put("sortorder", spec.getSortorder());
            }

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


}

package com.movision.fsearch.service.impl;

import com.movision.common.constant.PostLabelConstants;
import com.movision.facade.index.FacadePost;
import com.movision.fsearch.pojo.ProductGroup;
import com.movision.fsearch.pojo.spec.NormalSearchSpec;
import com.movision.fsearch.service.ILabelSearchService;
import com.movision.fsearch.service.IWordService;
import com.movision.fsearch.service.Searcher;
import com.movision.fsearch.service.exception.ServiceException;
import com.movision.fsearch.utils.*;
import com.movision.mybatis.collection.entity.*;
import com.movision.mybatis.labelSearchTerms.service.LabelSearchTermsService;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabel.service.PostLabelService;
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
    private FacadePost facadePost;

    @Autowired
    private LabelSearchTermsService labelSearchTermsService;

    @Autowired
    private PostLabelService postLabelService;

    @Autowired
    private CommonSearchService commonSearchService;

    @Override
    public Map<String, Object> search(NormalSearchSpec spec)
            throws ServiceException {
        Map<String, Map<String, Object>> query = new HashMap<String, Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        List<PostLabel> cityLabels = new ArrayList<>();
        result.put("spec", spec);

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
            //获取未被使用的城市标签列表
            cityLabels = getNotUsedCityLabel(q);

            int cityLabelLength = cityLabels.size();
            if (cityLabelLength > 0 && cityLabelLength < 12) {
                spec.setLimit(12 - cityLabelLength);    //重新设置搜索服务返回的数据数量
            }
        }
        //设置排序字段和排序顺序（正序/倒序）
        List<Map<String, Object>> sortFields = commonSearchService.setSortFields(spec, result);
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
        List<PostLabel> labels = new ArrayList<>();
        if (!list.isEmpty()) {
            labels = makeList(list);
        }
        //处理未使用的标签--包括地理标签和普通标签
        handleNotUseLabel(spec, cityLabels, labels);
        @SuppressWarnings("unchecked")
        List<ProductGroup> productGroups = (List<ProductGroup>) psAsMap.get("groups");
        //生成Pagination分页对象
        ps = new Pagination<PostLabel, ProductGroup>(labels, productGroups,
                labels.size(),
                FormatUtil.parseInteger(psAsMap.get("offset")),
                FormatUtil.parseInteger(psAsMap.get("limit")));

        result.put("ps", ps);

        return result;
    }

    private void handleNotUseLabel(NormalSearchSpec spec, List<PostLabel> cityLabels, List<PostLabel> labels) {
        //把查出的城市标签加入到搜索标签集合里面
        if (CollectionUtil.isNotEmpty(cityLabels)) {
            labels.addAll(cityLabels);
        }

        //获取没有使用的同名普通标签, 并放在集合第一个位置
        if (spec.getQ() != null) {
            String q = spec.getQ();
            int sameNameNormalLabelCount = postLabelService.countSameNormalNameLabel(q);
            if (sameNameNormalLabelCount == 0) {
                //增加一个普通标签
                PostLabel postLabel = new PostLabel();
                postLabel.setType(PostLabelConstants.TYPE.normal.getCode());
                postLabel.setName(q);

                labels.add(0, postLabel);
            }
        }
    }

    /**
     * 获取yw_city城市表中的未被使用的城市标签列表
     *
     * @param q
     * @return
     */
    private List<PostLabel> getNotUsedCityLabel(String q) {
        //查询yw_city城市表的数据, 封装成PostLabel
        List<PostLabel> cityLabels = facadePost.queryCityListByCityname(q);
        //在城市表中的数据里，去除标签表中的相关数据，只留下未被使用的地理标签数据
        if (CollectionUtil.isEmpty(cityLabels)) {
            return cityLabels;
        }
        //标签表中的已经使用过的地理标签
        List<PostLabel> existPostLabels = facadePost.queryGeogLabelByName(q);

        for (Iterator<PostLabel> it = cityLabels.iterator(); it.hasNext(); ) {
            PostLabel cityLabel = it.next();
            //设置type为地理标签
            cityLabel.setType(PostLabelConstants.TYPE.geog.getCode());
            //留下未被使用的地理标签数据
            for (PostLabel existLabel : existPostLabels) {
                if (cityLabel.getName().contains(existLabel.getName())) {
                    it.remove();
                }
            }
        }
        return cityLabels;
    }



    /**
     * 根据所搜结果，生成 PostLabel 集合（分页的第一个参数）
     *
     * @param list
     * @return
     */
    private List<PostLabel> makeList(List<?> list) {
        List<PostLabel> products = new ArrayList<PostLabel>(list.size());
        for (Object item : list) {
            Map<?, ?> itemAsMap = (Map<?, ?>) item;
            PostLabel label = new PostLabel();
            {
                //获取标签id
                Integer id = FormatUtil.parseInteger(itemAsMap.get("id"));
                //封装标签实体
                setProductParam(itemAsMap, label, id);
            }
            products.add(label);
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


    public Integer UpdateSearchIsdel(Integer userid) {
        return labelSearchTermsService.updateColData(userid);
    }
}

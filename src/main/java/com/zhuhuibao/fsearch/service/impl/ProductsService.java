package com.zhuhuibao.fsearch.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.fsearch.pojo.Product;
import com.zhuhuibao.fsearch.pojo.ProductGroup;
import com.zhuhuibao.fsearch.pojo.ProductSearchSpec;
import com.zhuhuibao.fsearch.service.IProductsService;
import com.zhuhuibao.fsearch.service.IWordService;
import com.zhuhuibao.fsearch.service.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhuhuibao.fsearch.utils.CollectionUtil;
import com.zhuhuibao.fsearch.utils.FormatUtil;
import com.zhuhuibao.fsearch.utils.JSONUtil;
import com.zhuhuibao.fsearch.utils.Pagination;
import com.zhuhuibao.fsearch.utils.StringUtil;
import com.zhuhuibao.mybatis.category.service.CategoryService;
import com.zhuhuibao.fsearch.service.exception.ServiceException;

@Service
public class ProductsService implements IProductsService {

	@Autowired
	private IWordService wordService;
    @Autowired
    private CategoryService categoryService;

	@Override
	public Map<String, Object> search(ProductSearchSpec spec)
			throws ServiceException {
		Map<String, Map<String, Object>> query = new HashMap<String, Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("spec", spec);

		int fcateid = spec.getFcateid();
		if(fcateid > 0){
			Searcher.wrapEqualQuery(query, "fcateid", fcateid);
		}
		int scateid = spec.getScateid();
		if(scateid > 0){
			Searcher.wrapEqualQuery(query, "scateid", scateid);
		}
		int brandid = spec.getBrandid();
		if(brandid > 0){
			Searcher.wrapEqualQuery(query, "brandid", brandid);
		}
		int member_identify = spec.getMember_identify();
		if(member_identify > 0){
			Searcher.wrapEqualQuery(query, "member_identify", member_identify);
		}

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

		List<Map<String, Object>> sortFields = new ArrayList<Map<String, Object>>(1);
		Map<String, Object> sortField = new HashMap<String, Object>(3);
		if (StringUtil.isNotEmpty(spec.getSort())) {
			String sort = spec.getSort();
			result.put("sort", spec.getSort());
			String sortorder = "true";
			if(StringUtil.isNotEmpty(spec.getSortorder())){
				sortorder = spec.getSortorder();
				result.put("sortorder", spec.getSortorder());
			}
			if (sort.equals("price1")) {
				sortField.put("field", sort);
				sortField.put("type", "FLOAT");
				sortField.put("reverse",
						FormatUtil.parseBoolean(sortorder));
			}else if(sort.equals("publishTime1")){
				sortField.put("field", sort);
				sortField.put("type", "LONG");
				sortField.put("reverse",
						FormatUtil.parseBoolean(sortorder));
			}else {
				sortField.put("field", "id");
				sortField.put("type", "INT");
				sortField.put("reverse",FormatUtil.parseBoolean(true));
			}
		}else {
			sortField.put("field", "id");
			sortField.put("type", "INT");
			sortField.put("reverse",FormatUtil.parseBoolean(true));
		}
		
		sortFields.add(sortField);

		Map<?, ?> psAsMap = (Map<?, ?>) Searcher.request(
				"search",
				CollectionUtil.arrayAsMap("table", "product", "query",
						JSONUtil.toJSONString(query), "sort",
						JSONUtil.toJSONString(sortFields), "offset",
						spec.getOffset(), "limit", spec.getLimit()));
		List<?> list = (List<?>) psAsMap.get("items");
		Pagination<Product,ProductGroup> ps = null;
		if (list.isEmpty()) {
			ps = Pagination.getEmptyInstance();
		} else {
			List<Product> products = new ArrayList<Product>(list.size());
			for (Object item : list) {
				Map<?, ?> itemAsMap = (Map<?, ?>) item;
				Product product = new Product();
				{
					product.setId(FormatUtil.parseLong(itemAsMap.get("id")));
					product.setName(FormatUtil.parseString(itemAsMap
							.get("name")));
					product.setImgUrl(FormatUtil.parseString(itemAsMap
							.get("imgUrl")));
					product.setPublishTime(FormatUtil.parseString(itemAsMap
							.get("publishTime")));
					product.setPrice(FormatUtil.parseDouble(itemAsMap
							.get("price")));
				}
				products.add(product);
			}
			@SuppressWarnings("unchecked")
			List<ProductGroup> productGroups = (List<ProductGroup>) psAsMap.get("groups");
			
			ps = new Pagination<Product,ProductGroup>(products,productGroups,
					FormatUtil.parseInteger(psAsMap.get("total")),
					FormatUtil.parseInteger(psAsMap.get("offset")),
					FormatUtil.parseInteger(psAsMap.get("limit")));
			/*if(scateid > 0){
				ResultBean fcate = categoryService.getFcateByScate(scateid);
				result.put("fcate", fcate);
			}*/
		}
		result.put("ps", ps);
		return result;
	}
}

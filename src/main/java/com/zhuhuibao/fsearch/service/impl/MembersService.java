package com.zhuhuibao.fsearch.service.impl;


import com.zhuhuibao.fsearch.pojo.spec.ContractorSearchSpec;
import com.zhuhuibao.fsearch.pojo.Member;
import com.zhuhuibao.fsearch.pojo.ProductGroup;
import com.zhuhuibao.fsearch.pojo.spec.SupplierSearchSpec;
import com.zhuhuibao.fsearch.service.IMembersService;
import com.zhuhuibao.fsearch.service.Searcher;
import com.zhuhuibao.fsearch.service.exception.ServiceException;
import com.zhuhuibao.fsearch.utils.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MembersService implements IMembersService {

	@Override
	public Map<String, Object> searchContractors(ContractorSearchSpec spec)
			throws ServiceException {
		Map<String, Map<String, Object>> query = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		result.put("spec", spec);

		String province = spec.getProvince();
		if (StringUtil.isNotEmpty(province)) {
			Searcher.wrapEqualQuery(query, "province", province);
			result.put("province", province);
		}
		String assetlevel = spec.getAssetlevel();
		genAssetLevelQuery(query, result, assetlevel);

		spec.setQ(StringUtil.emptyToNull(spec.getQ()));
		if (spec.getQ() != null) {
			String q = spec.getQ();
			query.put("_s", CollectionUtil.arrayAsMap("type", "phrase",
					"value", q));
			result.put("q", q);
		}

		List<Map<String, Object>> sortFields =  genContractorSortFields(spec, result);

		Map<?, ?> psAsMap = (Map<?, ?>) Searcher.request(
				"search",
				CollectionUtil.arrayAsMap("table", "contractor",
						"query",JSONUtil.toJSONString(query),
						"sort",JSONUtil.toJSONString(sortFields),
						"offset",spec.getOffset(),
						"limit", spec.getLimit()));
		List<?> list = (List<?>) psAsMap.get("items");
		Pagination<Member,ProductGroup> contractors;
		contractors = assItems(psAsMap, list);
		result.put("contractors", contractors);
		return result;
	}

	@Override
	public Map<String, Object> searchSuppliers(SupplierSearchSpec spec)
			throws ServiceException {
		Map<String, Map<String, Object>> query = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		result.put("spec", spec);

		String province = spec.getProvince();
		if (StringUtil.isNotEmpty(province)) {
			Searcher.wrapEqualQuery(query, "province", province);
			result.put("province", province);
		}
		String identify = spec.getIdentify();
		if (StringUtil.isNotEmpty(identify)) {
			Searcher.wrapEqualQuery(query, identify, identify);
			result.put("identify", identify);
		}
		String assetlevel = spec.getAssetlevel();
		genAssetLevelQuery(query, result, assetlevel);
		String category = spec.getCategory();
		if (StringUtil.isNotEmpty(category)) {
			String[] categorylArr = category.split(",");
			for(String cate : categorylArr){
				Searcher.wrapEqualQuery(query, cate, cate);
			}
			result.put("category", category);
		}

		spec.setQ(StringUtil.emptyToNull(spec.getQ()));
		if (spec.getQ() != null) {
			String q = spec.getQ();
			query.put("_s", CollectionUtil.arrayAsMap("type", "phrase",
					"value", q));
			result.put("q", q);
		}

		List<Map<String, Object>> sortFields = genSupplierSortFileds(spec, result);

		Map<?, ?> psAsMap = (Map<?, ?>) Searcher.request(
				"search",
				CollectionUtil.arrayAsMap("table", "supplier",
						"query",JSONUtil.toJSONString(query),
						"sort",JSONUtil.toJSONString(sortFields),
						"offset",spec.getOffset(),
						"limit", spec.getLimit()));
		List<?> list = (List<?>) psAsMap.get("items");
		Pagination<Member,ProductGroup> suppliers;
		suppliers = assItems(psAsMap, list);

		result.put("suppliers", suppliers);
		return result;
	}

	private List<Map<String, Object>> genSupplierSortFileds(SupplierSearchSpec spec, Map<String, Object> result) {
		List<Map<String, Object>> sortFields = new ArrayList<>(1);
		Map<String, Object> sortField = new HashMap<>(3);
		if (StringUtil.isNotEmpty(spec.getSort())) {
			String sort = spec.getSort();
			result.put("sort", spec.getSort());
			String sortorder = "true";
			if(StringUtil.isNotEmpty(spec.getSortorder())){
				sortorder = spec.getSortorder();
				result.put("sortorder", spec.getSortorder());
			}
			if (sort.equals("registerTime1") || sort.equals("weightLevel")) {
//				sortField.put("field", sort);
//				sortField.put("type", "LONG");
//				sortField.put("reverse",FormatUtil.parseBoolean(sortorder));
				sortField.put("field", "weightLevel");
				sortField.put("type", "DOUBLE");
				sortField.put("reverse", FormatUtil.parseBoolean(sortorder));
			}
		}else {
			sortField.put("field", "id");
			sortField.put("type", "INT");
			sortField.put("reverse",FormatUtil.parseBoolean(true));
		}
		sortFields.add(sortField);

		return sortFields;
	}

	private List<Map<String, Object>>  genContractorSortFields(ContractorSearchSpec spec, Map<String, Object> result) {
		List<Map<String, Object>> sortFields = new ArrayList<>(1);
		Map<String, Object> sortField = new HashMap<>(3);

		if (StringUtil.isNotEmpty(spec.getSort())) {
			String sort = spec.getSort();
			result.put("sort", spec.getSort());
			String sortorder = "true";
			if(StringUtil.isNotEmpty(spec.getSortorder())){
				sortorder = spec.getSortorder();
				result.put("sortorder", spec.getSortorder());
			}
			if (sort.equals("registerTime1")) {
//                sortField.put("field", sort);
//                sortField.put("type", "LONG");
//                sortField.put("reverse", FormatUtil.parseBoolean(sortorder));
				sortField.put("field", "certLevel");
				sortField.put("type", "LONG");
				sortField.put("reverse", FormatUtil.parseBoolean(sortorder));
			}
		}else {
			sortField.put("field", "id");
			sortField.put("type", "INT");
			sortField.put("reverse",FormatUtil.parseBoolean(true));
		}
		sortFields.add(sortField);

		return sortFields;
	}

	private Pagination<Member, ProductGroup> assItems(Map<?, ?> psAsMap, List<?> list) {
		Pagination<Member, ProductGroup> items;
		if (list.isEmpty()) {
			items = Pagination.getEmptyInstance();
		} else {
			List<Member> members = new ArrayList<>(list.size());
			for (Object item : list) {
				Map<?, ?> itemAsMap = (Map<?, ?>) item;
				Member member = new Member();
				{
					member.setId(FormatUtil.parseLong(itemAsMap.get("id")));
					member.setEnterpriseName(FormatUtil.parseString(itemAsMap
							.get("enterpriseName")));
					member.setAuthinfo(FormatUtil.parseString(itemAsMap
							.get("authinfo")));
					member.setEnterpriseLogo(FormatUtil.parseString(itemAsMap
							.get("enterpriseLogo")));
					member.setEnterpriseWebSite(FormatUtil.parseString(itemAsMap
							.get("enterpriseWebSite")));
					member.setAddress(FormatUtil.parseString(itemAsMap
							.get("address")));
					member.setSaleProductDesc(FormatUtil.parseString(itemAsMap
							.get("saleProductDesc")));
					member.setViplevel(FormatUtil.parseString(itemAsMap.get("viplevel")));
				}
				members.add(member);
			}

			items = new Pagination<>(members, null,
					FormatUtil.parseInteger(psAsMap.get("total")),
					FormatUtil.parseInteger(psAsMap.get("offset")),
					FormatUtil.parseInteger(psAsMap.get("limit")));
		}
		return items;
	}

	private void genAssetLevelQuery(Map<String, Map<String, Object>> query, Map<String, Object> result, String assetlevel) {
		if (StringUtil.isNotEmpty(assetlevel)) {
			String[] assetlevelArr = assetlevel.split(",");
			for(String asset : assetlevelArr){
				Searcher.wrapEqualQuery(query, asset, asset);
			}
			result.put("assetlevel", assetlevel);
		}
	}
}

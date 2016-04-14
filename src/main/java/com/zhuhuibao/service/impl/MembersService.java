package com.zhuhuibao.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhuhuibao.utils.search.CollectionUtil;
import com.zhuhuibao.utils.search.FormatUtil;
import com.zhuhuibao.utils.search.JSONUtil;
import com.zhuhuibao.utils.search.Pagination;
import com.zhuhuibao.utils.search.StringUtil;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.pojo.ContractorSearchSpec;
import com.zhuhuibao.pojo.Member;
import com.zhuhuibao.pojo.ProductGroup;
import com.zhuhuibao.service.IMembersService;
import com.zhuhuibao.service.Searcher;
import com.zhuhuibao.service.exception.ServiceException;

@Service
public class MembersService implements IMembersService {

	@Override
	public Map<String, Object> searchContractors(ContractorSearchSpec spec)
			throws ServiceException {
		Map<String, Map<String, Object>> query = new HashMap<String, Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("spec", spec);

		String province = spec.getProvince();
		if (StringUtil.isNotEmpty(province)) {
			Searcher.wrapEqualQuery(query, "province", province);
			result.put("province", province);
		}
		String aseetlevel = spec.getAssetlevel();
        if (StringUtil.isNotEmpty(aseetlevel)) {
        	String[] assetlevelArr = aseetlevel.split(",");
			for(String asset : assetlevelArr){
				Searcher.wrapEqualQuery(query, asset, asset);
			}
			result.put("aseetlevel", aseetlevel);
        }

		spec.setQ(StringUtil.emptyToNull(spec.getQ()));
		if (spec.getQ() != null) {
			String q = spec.getQ();
				query.put("_s", CollectionUtil.arrayAsMap("type", "phrase",
						"value", q));
				result.put("q", q);
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
			if (sort.equals("registerTime1")) {
				sortField.put("field", sort);
				sortField.put("type", "LONG");
				sortField.put("reverse",
						FormatUtil.parseBoolean(sortorder));
			}
		}else {
			sortField.put("field", "id");
			sortField.put("type", "INT");
			sortField.put("reverse",FormatUtil.parseBoolean(true));
		}
		
		sortFields.add(sortField);

		Map<?, ?> psAsMap = (Map<?, ?>) Searcher.request(
				"search",
				CollectionUtil.arrayAsMap("table", "contractor", "query",
						JSONUtil.toJSONString(query), "sort",
						JSONUtil.toJSONString(sortFields), "offset",
						spec.getOffset(), "limit", spec.getLimit()));
		List<?> list = (List<?>) psAsMap.get("items");
		Pagination<Member,ProductGroup> contractors = null;
		if (list.isEmpty()) {
			contractors = Pagination.getEmptyInstance();
		} else {
			List<Member> members = new ArrayList<Member>(list.size());
			for (Object item : list) {
				Map<?, ?> itemAsMap = (Map<?, ?>) item;
				Member member = new Member();
				{
					member.setId(FormatUtil.parseLong(itemAsMap.get("id")));
					member.setEnterpriseName(FormatUtil.parseString(itemAsMap
							.get("enterpriseName")));
					member.setAuthinfo(FormatUtil.parseString(itemAsMap
							.get("authinfo")));
					member.setEnterpriseWebSite(FormatUtil.parseString(itemAsMap
							.get("enterpriseWebSite")));
					member.setAddress(FormatUtil.parseString(itemAsMap
							.get("address")));
					member.setSaleProductDesc(FormatUtil.parseString(itemAsMap
							.get("saleProductDesc")));
				}
				members.add(member);
			}
			
			contractors = new Pagination<Member,ProductGroup>(members,null,
					FormatUtil.parseInteger(psAsMap.get("total")),
					FormatUtil.parseInteger(psAsMap.get("offset")),
					FormatUtil.parseInteger(psAsMap.get("limit")));
		}
		result.put("contractors", contractors);
		return result;
	}
}

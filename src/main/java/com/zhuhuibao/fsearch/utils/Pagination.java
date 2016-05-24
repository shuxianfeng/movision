package com.zhuhuibao.fsearch.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
public class Pagination<T1,T2> implements java.io.Serializable {

	public static final int DEFAULT_PAGESIZE = 10;

	public static <T1,T2> Pagination<T1,T2> getEmptyInstance() {
		return new Pagination<T1,T2>();
	}

	public static <T1,T2> Pagination<T1,T2> wrapSingle(T1 t1, T2 t2, int limit) {
		List<T1> list1 = new ArrayList<T1>(1);
		list1.add(t1);
		List<T2> list2 = new ArrayList<T2>(1);
		list2.add(t2);
		return new Pagination<T1,T2>(list1, list2, 1, 0, limit);
	}

	public boolean empty() {
		return items == null || items.isEmpty();
	}

	private Collection<T1> items;
	private Collection<T2> groups;
	private int total;
	private int offset;
	private int limit = DEFAULT_PAGESIZE;

	public Pagination() {
		this(new ArrayList<T1>(0), new ArrayList<T2>(0), 0, 0, DEFAULT_PAGESIZE);
	}

	public Pagination(Collection<T1> items, Collection<T2> groups, int total, int offset, int limit) {
		this.items = items;
		this.groups = groups;
		this.total = total;
		this.offset = offset;
		this.limit = limit <= 0 ? DEFAULT_PAGESIZE : limit;
	}

	public Collection<T1> getItems() {
		return items;
	}
	
	public Collection<T2> getGroups() {
		return groups;
	}
	
	public int getTotal() {
		return total;
	}

	public int getOffset() {
		return offset;
	}

	public int getLimit() {
		return limit;
	}

	public int totalPage() {
		if (total % limit == 0) {
			return total / limit;
		}
		return total / limit + 1;
	}

	public int currentPage() {
		return offset / limit + 1;
	}

}

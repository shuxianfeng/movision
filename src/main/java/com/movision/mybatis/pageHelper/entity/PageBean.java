package com.movision.mybatis.pageHelper.entity;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/12/4 18:21
 * 增加物理分页插件实体
 */
public class PageBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private long total; //总记录数
    private List<T> list; //结果集
    private int pageNum; //第几页
    private int pageSize; //每页记录数
    private int pages; // 总页数
    private int size; //当前页的数量<=pageSize

    public PageBean(List<T> list){
        if (list instanceof Page){
            Page<T> page = (Page<T>) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.list = page;
            this.size = page.size();
        }
    }
    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPages() {
        return pages;
    }
    public void setPages(int pages) {
        this.pages = pages;
    }
}

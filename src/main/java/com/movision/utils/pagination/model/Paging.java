package com.movision.utils.pagination.model;

import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Date Created 2014-2-26
 *
 * @author
 * @version 1.0
 */
public class Paging<E> {
    private List<E> rows = null;
    private int total = 0;
    public final static int DEFAULT_ROW_PER_PAGE = 10;

    protected int curPage = 1;
    protected int maxPage = 0;
    protected int pageSize = DEFAULT_ROW_PER_PAGE;

    protected int firstRow = 0;

    protected int lastRow = 0;
    private static final ThreadLocal<Integer> PAGINATION_TOTAL = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void setPaginationTotal(int count) {
        PAGINATION_TOTAL.set(count);
    }

    private int getPaginationTotal() {
        return PAGINATION_TOTAL.get();
    }

    public Paging(int curPage) {
        if (curPage < 1)
            this.curPage = 1;
        else
            this.curPage = curPage;
    }

    public Paging(int curPage, int pageSize) {
        if (curPage < 1)
            this.curPage = 1;
        else
            this.curPage = curPage;
        if (pageSize < 1) {
            this.pageSize = 1;
        } else {
            this.pageSize = pageSize;
        }

    }

    public RowBounds getRowBounds() {
        return new RowBounds((this.curPage - 1) * this.pageSize, this.pageSize);
    }


    public void result(List<E> rows) {
        this.rows = rows;
        this.setTotal(getPaginationTotal());
    }

    public void pages(List<E> rows,int curPage,int pageSize,int total){
        this.rows = rows;
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.total = total;
    }

    public List<E> getRows() {
        return rows;
    }

    public int getTotal() {
        return total;
    }

    public void countMaxPage() {

        if (this.total % this.pageSize == 0) {
            this.maxPage = this.total / this.pageSize;
        } else {
            this.maxPage = this.total / this.pageSize + 1;
        }
        if (this.getMaxPage() < this.getCurPage())
            this.curPage = this.getMaxPage();

        this.firstRow = (this.curPage - 1) * this.pageSize + 1;

        int temp = this.curPage * this.pageSize;
        this.lastRow = this.total > temp ? temp : this.total;
    }

    @Override
    public String toString() {
        return "Paging{firstRow=" + firstRow + ",lastRow=" + lastRow + ",curPage=" + curPage + ",maxPage=" + maxPage + ",total=" + total + '}';
    }

    public int getCurPage() {
        return curPage;
    }

    public int getMaxPage() {
        return maxPage;
    }


    public int getRowsPerPage() {
        return pageSize;
    }

    public void setTotal(int total) {
        this.total = total;
        this.countMaxPage();// 计算其他值
    }


    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public int getPageSize() {
        return this.pageSize;
    }


}

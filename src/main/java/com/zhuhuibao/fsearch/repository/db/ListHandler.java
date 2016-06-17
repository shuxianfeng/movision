package com.zhuhuibao.fsearch.repository.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListHandler<T> implements Handler<List<T>> {

    protected RowHandler<T> rowHandler;

    public ListHandler(RowHandler<T> rowHandler) {
        this.rowHandler = rowHandler;
    }

    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        rs.setFetchSize(200);
        List<T> list = new ArrayList<T>();
        while (rs.next()) {
            list.add(rowHandler.handleRow(rs));
        }
        return list;
    }

    public static <T> ListHandler<T> newInstance(RowHandler<T> t) {
        return new ListHandler<T>(t);
    }

}

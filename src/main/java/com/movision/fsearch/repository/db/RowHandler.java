package com.movision.fsearch.repository.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class RowHandler<T> implements Handler<T> {
    @Override
    public T handle(ResultSet rs) throws SQLException {
        rs.setFetchSize(1);
        if (rs.next()) {
            return handleRow(rs);
        }
        return null;
    }

    public abstract T handleRow(ResultSet rs) throws SQLException;
}

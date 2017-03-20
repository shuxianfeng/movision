package com.movision.fsearch.repository.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PropertyHandler extends RowHandler<Object> {
    @Override
    public Object handleRow(ResultSet rs) throws SQLException {
        return rs.getObject(1);
    }

    private PropertyHandler() {
    }

    private static final PropertyHandler INSTANCE = new PropertyHandler();

    public static PropertyHandler getInstance() {
        return INSTANCE;
    }

}

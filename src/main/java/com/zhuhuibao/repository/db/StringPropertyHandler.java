package com.zhuhuibao.repository.db;

import java.sql.ResultSet;
import java.sql.SQLException;


public class StringPropertyHandler extends RowHandler<String> {
	@Override
	public String handleRow(ResultSet rs) throws SQLException {
		return rs.getString(1);
	}
	private StringPropertyHandler(){}
	private static final StringPropertyHandler INSTANCE=new StringPropertyHandler();
	public static StringPropertyHandler getInstance(){
		return INSTANCE;
	}
}

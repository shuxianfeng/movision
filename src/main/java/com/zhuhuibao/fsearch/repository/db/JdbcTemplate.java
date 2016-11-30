package com.zhuhuibao.fsearch.repository.db;

import com.zhuhuibao.fsearch.repository.RepositoryException;
import com.zhuhuibao.fsearch.utils.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class JdbcTemplate {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JdbcTemplate.class);

	private DataSource dataSource;

	private String dbType = DB_MYSQL;

	public JdbcTemplate() {
	}

	public JdbcTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public String getDbType() {
		return dbType;
	}

	private static final String DB_MYSQL = "mysql";

	private static final String DB_ORACLE = "oracle";

	// //////////////////////////////////////////////////
	protected void setParams(PreparedStatement ps, Object[] params)
			throws SQLException {
		if (params == null || params.length == 0)
			return;
		for (int i = 1; i <= params.length; i++) {
			ps.setObject(i, params[i - 1]);
		}
	}

	public String getPageDialect(String sql, int index, int count) {
		if (dbType.equals(DB_MYSQL)) {
			return getMysqlDialect(sql, index, count);
		} else if (dbType.equals(DB_ORACLE)) {
			return getOracleDialect(sql, index, count);
		}
		return null;
	}

	public static String getMysqlDialect(String sql, int index, int count) {
		return new StringBuilder(sql).append(" limit ").append(index)
				.append(",").append(count).toString();
	}

	public static String getOracleDialect(String sql, int index, int count) {
		return new StringBuilder(
				"select * from ( select row_.*, rownum rownum_ from ( ")
				.append(sql).append(" ) row_ where rownum <= ")
				.append(index + count).append(" ) where rownum_ > " + index)
				.toString();
	}

	public int update(String sql, Object[] params) throws RepositoryException {
		long t1 = 0;
		if (LOGGER.isInfoEnabled()) {
			t1 = System.currentTimeMillis();
		}
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(sql);
			this.setParams(ps, params);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new RepositoryException(e);
		} finally {
			DBUtil.closeQuietly(con, ps);
			if (LOGGER.isInfoEnabled()) {
				long take = System.currentTimeMillis() - t1;
				LOGGER.info("JDBCTemplate--->:" + sql + ", using: " + take
						+ "ms");
			}
		}
	}

	public void batchUpdate(String sql, List<Object[]> paramArrayList)
			throws RepositoryException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("JDBCTemplate--->:" + sql);
		}
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(sql);
			for (Iterator<Object[]> it = paramArrayList.iterator(); it
					.hasNext();) {
				this.setParams(ps, it.next());
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			throw new RepositoryException(e);
		} finally {
			DBUtil.closeQuietly(con, ps);
		}
	}

	// mysql适用
	public <T> T insert(String sql, Object[] params, Handler<T> handler)
			throws RepositoryException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("JDBCTemplate--->:" + sql);
		}
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			this.setParams(ps, params);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			return handler.handle(rs);
		} catch (SQLException e) {
			throw new RepositoryException(e);
		} finally {
			DBUtil.closeQuietly(con, ps, rs);
		}
	}

	public <T> T find(String sql, Object[] params, Handler<T> handler)
			throws RepositoryException {
		long t1 = 0;
		if (LOGGER.isInfoEnabled()) {
			t1 = System.currentTimeMillis();
		}
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(sql);
			this.setParams(ps, params);
			rs = ps.executeQuery();
			return handler.handle(rs);
		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		} finally {
			DBUtil.closeQuietly(con, ps, rs);
			if (LOGGER.isInfoEnabled()) {
				long take = System.currentTimeMillis() - t1;
				LOGGER.info("JDBCTemplate--->:" + sql + ", using: " + take
						+ "ms");
			}
		}
	}

	public static String camel2underline(String s) {
		StringBuilder newStr = new StringBuilder(s.length());
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isUpperCase(c)) {
				newStr.append('_');
				newStr.append(Character.toLowerCase(s.charAt(i)));
			} else {
				newStr.append(c);
			}
		}
		return newStr.toString();
	}

	private static String getInsertSql(Map<String, Object> kv,
			String tableName, Object[] params) {
		StringBuilder buf = new StringBuilder("insert into ");
		buf.append(tableName);
		buf.append('(');

		int i = -1;
		for (Entry<String, Object> entry : kv.entrySet()) {
			i++;
			if (i > 0) {
				buf.append(',');
			}
			buf.append(camel2underline(entry.getKey()));
			params[i] = entry.getValue();
		}

		buf.append(") values(");
		for (int k = 0; k < kv.size(); k++) {
			if (k > 0) {
				buf.append(',');
			}
			buf.append('?');
		}
		buf.append(')');
		return buf.toString();
	}

	public <T> T insert(String tableName, Map<String, Object> kv,
			RowHandler<T> handler) throws RepositoryException {
		Object[] params = new Object[kv.size()];
		String sql = getInsertSql(kv, tableName, params);
		return insert(sql, params, handler);
	}

	public int insert(String tableName, Map<String, Object> kv)
			throws RepositoryException {
		Object[] params = new Object[kv.size()];
		String sql = getInsertSql(kv, tableName, params);
		return update(sql, params);
	}

	public int update(String tableName, Map<String, Object> kv, String key,
			Object value) throws RepositoryException {
		StringBuilder buf = new StringBuilder("update ");
		buf.append(tableName);
		buf.append(" set ");
		Object[] params = new Object[kv.size() + 1];
		int i = -1;
		for (Entry<String, Object> entry : kv.entrySet()) {
			i++;
			if (i > 0) {
				buf.append(",");
			}
			buf.append(camel2underline(entry.getKey())).append("=?");
			params[i] = entry.getValue();
		}
		buf.append(" where ");
		buf.append(key);
		buf.append("=?");
		params[++i] = value;
		return update(buf.toString(), params);
	}

	// /////////////////////////////////////////////

	public int count(String sql, Object[] params) throws RepositoryException {
		Object obj = find(sql, params, PropertyHandler.getInstance());
		if (obj instanceof Integer) {
			return ((Integer) obj).intValue();
		} else {
			return ((Long) obj).intValue();
		}
	}

	public <T> T findOne(String sql, Object[] params, RowHandler<T> handler)
			throws RepositoryException {
		return find(getPageDialect(sql, 0, 1), params, handler);
	}

	public <T1,T2> Pagination<T1,T2> findPaged(String sql, String count_sql,
			Object[] params, final int index, final int count,
			ListHandler<T1> handler) throws RepositoryException {
		List<T1> items = this.find(this.getPageDialect(sql, index, count),
				params, handler);
		int records = items.size();
		if (records == 0) {
			return new Pagination<T1,T2>();
		}
		if (index != 0 || records == count) {
			records = this.count(count_sql, params);
		}
		return new Pagination<T1,T2>(items, null, records, index, count);
	}

	public <T> List<T> findList(String sql, Object[] params, final int offset,
			final int limit, ListHandler<T> handler) throws RepositoryException {
		if (limit > 0) {
			return this.find(this.getPageDialect(sql, offset, limit), params,
					handler);
		} else {
			return this.find(sql, params, handler);
		}
	}

	public <T> List<T> findList(String sql, Object[] params, final int offset,
			final int limit, RowHandler<T> handler) throws RepositoryException {
		return findList(sql, params, offset, limit,
				ListHandler.newInstance(handler));
	}

	public void execute(String sql, Object[] params) throws RepositoryException {
		long t1 = 0;
		if (LOGGER.isInfoEnabled()) {
			t1 = System.currentTimeMillis();
		}
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(sql);
			this.setParams(ps, params);
			ps.execute();
		} catch (SQLException e) {
			throw new RepositoryException(e);
		} finally {
			DBUtil.closeQuietly(con, ps);
			if (LOGGER.isInfoEnabled()) {
				long take = System.currentTimeMillis() - t1;
				LOGGER.info("JDBCTemplate--->:" + sql + ", using: " + take
						+ "ms");
			}
		}
	}
	
	public String executeProcOneReturn(String sql) throws RepositoryException {
		long t1 = System.currentTimeMillis();
		Connection con = null;
		CallableStatement cstmt = null;
		try {
			con = dataSource.getConnection();
			cstmt = (CallableStatement) con.prepareCall(sql);
			cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
            boolean i = cstmt.execute();
            String rtn = cstmt.getString(1);
            return rtn;
		} catch (SQLException e) {
			throw new RepositoryException(e);
		} finally {
			if(con != null){
				try {
					con.close();
				} catch (SQLException e) {
					LOGGER.error("JdbcTemplate SQLException",e);
				}
			}
			if(cstmt != null){
				try {
					cstmt.close();
				} catch (SQLException e) {
					LOGGER.error("JdbcTemplate SQLException",e);
				}
			}
			long take = System.currentTimeMillis() - t1;
			LOGGER.info("JDBCTemplate--->:" + sql + ", using: " + take
					+ "ms");
		}
	}	
	
}

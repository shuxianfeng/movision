package com.zhuhuibao.fsearch.repository.db;

import com.zhuhuibao.fsearch.utils.FormatUtil;
import com.zhuhuibao.fsearch.utils.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DBUtil {

	public static void closeQuietly(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			// quiet
		}
	}

	public static void closeQuietly(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			// quiet
		}
	}

	public static void closeQuietly(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			// quiet
		}
	}

	public static void closeQuietly(Connection conn, Statement stmt,
			ResultSet rs) {
		try {
			closeQuietly(rs);
		} finally {
			try {
				closeQuietly(stmt);
			} finally {
				closeQuietly(conn);
			}
		}
	}

	public static void closeQuietly(Connection conn, Statement stmt) {

		try {
			closeQuietly(stmt);
		} finally {
			closeQuietly(conn);
		}

	}

	public static void closeQuietly(Statement stmt, ResultSet rs) {
		try {
			closeQuietly(rs);
		} finally {
			closeQuietly(stmt);
		}

	}

	@SuppressWarnings("deprecation")
	public static String date2fixChars(Date date) {
		if (date == null) {
			return null;
		}
		long t = date.getTime();
		t += (-60 * 1000 * date.getTimezoneOffset());
		String s = String.valueOf(t);
		int padLen = 15 - s.length();
		if (padLen < 0) {
			throw new IllegalArgumentException("Bad date");
		}
		if (padLen > 0) {
			StringBuilder buf = new StringBuilder();
			if (t < 0) {
				s = s.substring(1);
				buf.append('-');
			}
			for (int i = 0; i < padLen; i++) {
				buf.append('0');
			}
			buf.append(s);
			s = buf.toString();
		}
		return s;
	}

	public static Date fixChars2date(String timestamp) {
		if (StringUtil.isEmpty(timestamp)) {
			return null;
		}
		TimeZone timezone = TimeZone.getDefault();
		long millis = Long.parseLong(timestamp);
		millis -= timezone.getRawOffset();
		Calendar cal = Calendar.getInstance(timezone);
		cal.setTimeInMillis(millis);
		return cal.getTime();
	}

	public static long date2timestamp(Date date) {
		return FormatUtil.date2timestamp(date);
	}

	public static Date timestamp2date(long timestamp) {
		return FormatUtil.timestamp2date(timestamp);
	}
}

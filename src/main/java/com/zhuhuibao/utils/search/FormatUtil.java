package com.zhuhuibao.utils.search;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FormatUtil {
	public static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
	public static final String ISO8601DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String ISO8601DATE_WITH_MILLS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String ISO8601DATE_WITH_ZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";
	public static final String ISO8601DATE_WITH_ZONE_MILLS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	public static final String ISO8601DATE_NORMAL_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final int ISO8601DATE_FORMAT_VALUE_LENGTH = ISO8601DATE_FORMAT
			.length() - 4;

	public static Number parseNumber(Object s) {
		if (s == null) {
			return null;
		}
		if (s instanceof Integer || s instanceof Float || s instanceof Double
				|| s instanceof Long) {
			return (Number) s;
		}
		s = s.toString();
		if (s instanceof String) {
			String theString = (String) s;
			if (theString.length() == 0) {
				return null;
			}
			if (theString.indexOf('.') >= 0) {
				return Double.valueOf(theString);
			} else {
				Long longObject = Long.valueOf(theString);
				long longValue = (long) longObject.longValue();
				if (longValue > Integer.MAX_VALUE) {
					return longObject;
				} else {
					return Integer.valueOf((int) longValue);
				}
			}
		}
		return null;
	}

	public static Integer parseInteger(Object s) {
		if (s == null) {
			return null;
		}
		if (s instanceof Integer) {
			return (Integer) s;
		}
		if (s instanceof Number) {
			return Integer.valueOf(((Number) s).intValue());
		}
		if (s instanceof String) {
			String theString = (String) s;
			if (theString.length() == 0) {
				return null;
			}
			return Integer.valueOf(theString);
		}
		if (s instanceof Boolean) {
			return ((Boolean) s).booleanValue() ? 1 : 0;
		}
		return null;
	}

	public static Integer parseRoundInteger(Object s) {
		if (s == null) {
			return null;
		}
		if (s instanceof Integer) {
			return (Integer) s;
		}
		if (s instanceof Number) {
			double d = ((Number) s).doubleValue();
			return (int) Math.round(d);
		}
		if (s instanceof String) {
			String theString = (String) s;
			if (theString.length() == 0) {
				return null;
			}
			double d = Double.parseDouble(theString);
			return (int) Math.round(d);
		}
		return null;
	}

	public static int parseIntValue(Object s, int defaultValue) {
		Integer v = parseInteger(s);
		return v == null ? defaultValue : v.intValue();
	}

	public static int parseRoundIntValue(Object s, int defaultValue) {
		Integer v = parseRoundInteger(s);
		return v == null ? defaultValue : v.intValue();
	}

	public static Long parseLong(Object s) {
		if (s == null) {
			return null;
		}
		if (s instanceof Long) {
			return (Long) s;
		}
		if (s instanceof Number) {
			return Long.valueOf(((Number) s).longValue());
		}
		if (s instanceof String) {
			String theString = (String) s;
			if (theString.length() == 0) {
				return null;
			}
			return Long.valueOf(theString);
		}
		return null;
	}

	public static long parseLongValue(Object s, long defaultValue) {
		Long v = parseLong(s);
		return v == null ? defaultValue : v.longValue();
	}

	public static Double parseDouble(Object s) {
		if (s == null) {
			return null;
		}
		if (s instanceof Double) {
			return (Double) s;
		}
		if (s instanceof Number) {
			return new BigDecimal(s.toString()).doubleValue();
		}
		if (s instanceof String) {
			String theString = (String) s;
			if (theString.length() == 0) {
				return null;
			}
			return Double.valueOf(theString);
		}
		return null;
	}

	public static double parseDoubleValue(Object s, double defaultValue) {
		Double v = parseDouble(s);
		return v == null ? defaultValue : v.doubleValue();
	}

	public static Float parseFloat(Object s) {
		if (s == null) {
			return null;
		}
		if (s instanceof Float) {
			return (Float) s;
		}
		if (s instanceof Number) {
			return new BigDecimal(s.toString()).floatValue();
		}
		if (s instanceof String) {
			String theString = (String) s;
			if (theString.length() == 0) {
				return null;
			}
			return Float.valueOf(theString);
		}
		return null;
	}

	public static float parseFloatValue(Object s, float defaultValue) {
		Float v = parseFloat(s);
		return v == null ? defaultValue : v.floatValue();
	}

	public static Short parseShort(Object s) {
		if (s == null) {
			return null;
		}
		if (s instanceof Short) {
			return (Short) s;
		}
		if (s instanceof Number) {
			return Short.valueOf(((Number) s).shortValue());
		}
		if (s instanceof String) {
			String theString = (String) s;
			if (theString.length() == 0) {
				return null;
			}
			return Short.valueOf(theString);
		}
		return null;
	}

	public static short parseShortValue(Object s, short defaultValue) {
		Short v = parseShort(s);
		return v == null ? defaultValue : v.shortValue();
	}

	public static Byte parseByte(Object s) {
		if (s == null) {
			return null;
		}
		if (s instanceof Byte) {
			return (Byte) s;
		}
		if (s instanceof Number) {
			return Byte.valueOf(((Number) s).byteValue());
		}
		if (s instanceof Boolean) {
			return ((Boolean) s).booleanValue() ? (byte) 1 : (byte) 0;
		}
		if (s instanceof String) {
			String theString = (String) s;
			if (theString.length() == 0) {
				return null;
			}
			return Byte.valueOf(theString);
		}
		return null;
	}

	public static byte parseByteValue(Object s, byte defaultValue) {
		Byte v = parseByte(s);
		return v == null ? defaultValue : v.byteValue();
	}

	public static Date parseISO8601Date(String s) {
		if (s == null || s.isEmpty()) {
			return null;
		}
		try {
			Date date = null;
			if (s.charAt(s.length() - 1) == 'Z') {
				String format = (s.length() == ISO8601DATE_FORMAT_VALUE_LENGTH) ? ISO8601DATE_FORMAT
						: ISO8601DATE_WITH_MILLS_FORMAT;
				DateFormat dateFormat = new SimpleDateFormat(format);
				dateFormat.setTimeZone(GMT_TIMEZONE);
				date = dateFormat.parse(s);
			} else if (s.length() == DATE_FORMAT.length()) {
				DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
				dateFormat.setTimeZone(GMT_TIMEZONE);
				date = dateFormat.parse(s);
			} else if (s.indexOf('.') > 0) {
				date = new SimpleDateFormat(ISO8601DATE_WITH_ZONE_MILLS_FORMAT)
						.parse(s);
			} else if(s.indexOf(':') > 0){
				date = new SimpleDateFormat(ISO8601DATE_NORMAL_FORMAT).parse(s);
			}else {
				date = new SimpleDateFormat(ISO8601DATE_WITH_ZONE_FORMAT)
						.parse(s);
			}
			return date;
		} catch (ParseException e) {
			throw new RuntimeException("Failed to parseISO8601Date", e);
		}
	}

	public static Date parseDate(String s, TimeZone timezone) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		dateFormat.setTimeZone(timezone);
		try {
			return dateFormat.parse(s);
		} catch (ParseException e) {
			throw new RuntimeException("Failed to parseDate", e);
		}
	}

	public static String formatISO8601Date(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(
				ISO8601DATE_WITH_MILLS_FORMAT);
		dateFormat.setTimeZone(GMT_TIMEZONE);
		return dateFormat.format(date);
	}

	public static String formatDate(Date date, TimeZone timezone) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		dateFormat.setTimeZone(timezone);
		return dateFormat.format(date);
	}

	public static Boolean parseBoolean(Object value) {
		return parseBoolean(value, Boolean.FALSE);
	}

	public static Boolean parseBoolean(Object value, Boolean defaultValue) {
		Boolean bool = parseBooleanStrictly(value);
		if (bool == null) {
			return defaultValue;
		}
		return bool;
	}

	public static Boolean parseBooleanStrictly(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Boolean) {
			return ((Boolean) value);
		}
		if (value instanceof Number) {
			value = value.toString();
		}
		if (value instanceof String) {
			String strValue = (String) value;
			if (strValue.isEmpty()) {
				return null;
			}
			if (strValue.equalsIgnoreCase("true") || strValue.equals("1")) {
				return Boolean.TRUE;
			}
			if (strValue.equalsIgnoreCase("false") || strValue.equals("0")) {
				return Boolean.FALSE;
			}
		}
		return null;
	}

	public static String parseString(Object value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	public static String parseString(Object value, String defaultValue) {
		String s = parseString(value);
		if (s == null) {
			return defaultValue;
		}
		return s;
	}

	@SuppressWarnings("deprecation")
	public static long date2timestamp(Date date) {
		if (date == null) {
			return -1;
		}
		long t = date.getTime();
		t += (-60 * 1000 * date.getTimezoneOffset());
		return t;
	}

	public static Date timestamp2date(long timestamp) {
		if (timestamp < 0) {
			return null;
		}
		TimeZone timezone = TimeZone.getDefault();
		timestamp -= timezone.getRawOffset();
		Calendar cal = Calendar.getInstance(timezone);
		cal.setTimeInMillis(timestamp);
		return cal.getTime();
	}

}

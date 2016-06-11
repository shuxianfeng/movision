package com.zhuhuibao.fsearch.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyUtils {

	// init 0 不初始化 1 产品型,banner 2 头像型
	public static List<String> getSplitvalues(String sample, int type) {
		List<String> list = StringUtil.split(sample, ";");
		if (CollectionUtil.isNotEmpty(list)) {
			return list;
		}
		if (type == 0) {
			return list == null ? new ArrayList<String>(0) : list;
		} else if (type == 1) {
			return Arrays.asList("upload/img/product.png");
		} else if (type == 2) {
			return Arrays.asList("upload/img/userface.png");
		} else {
			return new ArrayList<String>(0);
		}
	}

	public static List<String> getSplitvalues(String sample) {
		List<String> list = new ArrayList<String>();
		if (!(sample == null || sample.trim().equals(""))) {
			String[] ss = sample.split(";");
			if (!(ss == null || ss.length == 0)) {
				for (String s : ss)
					list.add(s);
			}
		}
		return list;
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

	public static String getPreMouth(Date date, int mouth) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -mouth);
		String time = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal
				.getTime());
		return time;
	}

	public static String getPreMonth10th() {
		Calendar c = Calendar.getInstance();// 今天的时间
		int dayofmonth = c.get(Calendar.DAY_OF_MONTH); // 当前日期在本月的第几天
		if (dayofmonth < 10)
			c.add(Calendar.MONTH, -1);// 今天的时间月份-1支持1月的上月
		c.set(Calendar.DAY_OF_MONTH, 10);// 设置上月10号
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	public static String getLastdayofMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.DAY_OF_MONTH, -1);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}

	public static String subStr(String s, int length, boolean addsub) {
		String result = "";
		if (s == null || s.trim().equals("") || s.length() <= length) {
			result = s;
		} else {
			result = s.substring(0, length);
			if (addsub) {
				result += "...";
			}
		}
		return result;
	}

	public static String outTags(String s) {
		return s.replaceAll("<.*?>", "");
	}

	public static String getAfterDay_string(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, day);
		String time = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return time;
	}

	// 仅供账单还款打款日期计算，如果单月天数不足当前日期在本月的天数，按最后一天计算
	@SuppressWarnings("static-access")
	public static Date getBillPayDay(int month) {
		Date nowdate = new Date();
		Calendar nowcal = Calendar.getInstance();
		nowcal.setTime(nowdate);
		int dayofmonth = nowcal.get(Calendar.DAY_OF_MONTH); // 当前日期在本月的第几天
		Calendar newcal = Calendar.getInstance();
		newcal.setTime(nowdate);
		newcal.add(Calendar.MONTH, month); // 开始日期加i个月
		int maxday = newcal.getActualMaximum(newcal.DAY_OF_MONTH); // 新日期的月份最大天数
		Date newdate = newcal.getTime(); // 新日期的默认值
		if (maxday < dayofmonth) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(newdate);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.roll(Calendar.DAY_OF_MONTH, -1);
			newdate = cal.getTime(); // 按最后一天计算
		}
		return newdate;
	}

	private static final String allnums = "0123456789";
	private static final String allstrs = "0123456789abcdefghijklmnopqrstuvwxyz";

	public static String getRandNum(int num) {
		String str = "";
		Random rd = new Random();
		int length = allnums.length();
		for (int i = 0; i < num; i++) {
			int a = (rd.nextInt(length));
			str += allnums.charAt(a);
		}
		return str;
	}

	public static String getRandStr(int num) {
		String str = "";
		Random rd = new Random();
		int length = allstrs.length();
		for (int i = 0; i < num; i++) {
			int a = (rd.nextInt(length));
			str += allstrs.charAt(a);
		}
		return str;
	}

	@SuppressWarnings("static-access")
	public static String getMD5(String s) {
		GenerateMD5 generateMD5 = new GenerateMD5();
		generateMD5.getMD5().update(s.getBytes());
		byte digest[] = generateMD5.getMD5().digest();
		StringBuilder builder = new StringBuilder();
		for (byte b : digest) {
			builder.append(String.format("%02X", b));
		}
		return builder.toString();
	}

	public static Double roundDouble(double val, int precision) {
		Double ret = null;
		try {
			double factor = Math.pow(10, precision);
			ret = Math.floor(val * factor + 0.5) / factor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static List<String> getWordsByWord(String keyword) {
		String words_temp[] = keyword.split(" ");
		List<String> words = new ArrayList<String>();
		for (String s : words_temp) {
			if (!(s.trim().equals(""))) {
				words.add(s);
			}
		}
		return words;
	}

	public static Date getNextHour(Date date, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hour);
		return cal.getTime();
	}

	public static String getNumber(String maxnumber, String prefix) {
		String number = prefix;
		// CG1401240001
		boolean reset = true;

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		number += String.valueOf(year).substring(2, 4);

		String temp_m = "00" + month;
		number += temp_m.substring(temp_m.length() - 2, temp_m.length());

		String temp_d = "00" + day;
		number += temp_d.substring(temp_d.length() - 2, temp_d.length());

		if (!(maxnumber == null || maxnumber.trim().equals(""))) {
			int num_day = Integer.parseInt(maxnumber.substring(6, 8));
			if (num_day == day) {
				reset = false;
			}
		}

		int max = 0;
		if (!reset) {
			max = Integer.parseInt(maxnumber.substring(maxnumber.length() - 4,
					maxnumber.length()));
		}

		String temp = "000" + (max + 1);
		number += temp.substring(temp.length() - 4, temp.length());

		return number;
	}

	public static void mkDirectory(String path) {

		File file;
		try {
			file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			file = null;
		}
	}

	public static boolean delFile(String filename) throws Exception {
		File file = new File(filename);
		if (!file.exists()) {
			return true;
		}
		return file.delete();
	}

	public static boolean upload(String newFilePathAndName, File uploadFile) {
		try {

			FileOutputStream fos = new FileOutputStream(newFilePathAndName);
			FileInputStream fis = new FileInputStream(uploadFile);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fis.close();
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static List<String> getDays_between(String sdate, String edate) {

		Date start = new Date();
		Date end = new Date();
		try {
			start = new SimpleDateFormat("yyyy-MM-dd").parse(sdate);
			end = new SimpleDateFormat("yyyy-MM-dd").parse(edate);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		int day = getdays(start, end);
		List<String> days = new ArrayList<String>();
		for (int i = day; i > -1; i--) {
			String s = getPreDay_string(i, end, "yyyy-MM-dd");
			days.add(s);
		}
		return days;
	}

	public static int getdays(Date s, Date e) {

		if (s == null || e == null)
			return 0;

		long days = 0;

		days = e.getTime() - s.getTime();
		days = days / 1000 / 60 / 60 / 24;

		return (int) days;
	}
	
	public static String getSurplusTime(Date s, Date e) {
		
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    // long ns = 1000;
	    // 获得两个时间的毫秒时间差异
	    long diff = s.getTime() - e.getTime();
	    // 计算差多少天
	    long day = diff / nd;
	    // 计算差多少小时
	    long hour = diff % nd / nh;
	    // 计算差多少分钟
	    long min = diff % nd % nh / nm;
	    // 计算差多少秒//输出结果
	    // long sec = diff % nd % nh % nm / ns;
	    if(diff<=0){
	    	return "0";
	    }
	    return day + "天" + hour + "小时" + min + "分钟";
	}

	public static String getPreDay_string(int day, Date edate, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(edate);
		cal.add(Calendar.DATE, -day);
		String time = new SimpleDateFormat(format).format(cal.getTime());
		return time;
	}

	public static String getPreDay_string(int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -day);
		String time = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return time;
	}

	public static String getToday() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	public static boolean isNull(Object o) {
		if (o == null || String.valueOf(o).trim().equals("")) {
			return true;
		}
		return false;
	}

}

class CompratorByLastModified_positive implements Comparator<File> {
	public int compare(File f1, File f2) {
		long diff = f1.getName().compareTo(f2.getName());
		if (diff > 0)
			return 1;
		else if (diff == 0)
			return 0;
		else
			return -1;
	}
}

class CompratorByLastModified_negative implements Comparator<File> {
	public int compare(File f1, File f2) {
		long diff = f1.getName().compareTo(f2.getName());
		if (diff > 0)
			return -1;
		else if (diff == 0)
			return 0;
		else
			return 1;
	}
}

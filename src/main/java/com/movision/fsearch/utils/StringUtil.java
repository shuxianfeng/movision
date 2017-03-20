package com.movision.fsearch.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static final String UTF8 = "UTF-8";
    public static final String EMPTY = "";

    private static final String NUMERIC = "0123456789";

    private static final Pattern EMAIL_CHECKER = Pattern
            .compile("^([a-z0-9A-Z]+[-|\\._]?)+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    private static final Pattern MOBILE_CHECKER = Pattern
            .compile("^1[3,4,5,7,8]\\d{9}$");

    public static String replace(String text, String searchString,
                                 String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    public static String replaceAll(String text, String searchString,
                                    String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    public static String replace(String text, String searchString,
                                 String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null
                || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static String randomString(String salt, int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int len = salt.length();
        for (int i = 0; i < length; i++) {
            sb.append(salt.charAt(random.nextInt(len)));
        }
        return sb.toString();
    }

    public static boolean isTargetChar(String salt, char c) {
        for (int i = 0; i < salt.length(); i++) {
            if (salt.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTargetString(String salt, String s) {
        if (s == null) {
            return false;
        }
        int len = s.length();
        if (len == 0) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (!isTargetChar(salt, s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(char c) {
        return isTargetChar(NUMERIC, c);
    }

    public static boolean isNumeric(String s) {
        return isTargetString(NUMERIC, s);
    }

    public static String randomNumeric(int length) {
        return randomString(NUMERIC, length);
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return cs != null && cs.length() > 0;
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0)
            return true;
        for (int i = 0; i < strLen; i++)
            if (!Character.isWhitespace(cs.charAt(i)))
                return false;

        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static String trim(String str) {
        return str != null ? str.trim() : null;
    }

    public static String trimAll(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Pattern.compile("(\\s|\\t|\\r|\\n)+").matcher(s)
                .replaceAll(EMPTY);
    }

    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trimToEmpty(String str) {
        return str != null ? str.trim() : EMPTY;
    }

    public static String emptyToNull(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        return str;
    }

    public static String nullToEmpty(String str) {
        if (str == null) {
            return EMPTY;
        }
        return str;
    }

    public static String[] splitAsArray(final String str,
                                        final String separatorChars) {
        return splitAsArray(str, separatorChars, -1, false);
    }

    public static String[] splitAsArray(final String str,
                                        final String separatorChars, final int max,
                                        final boolean preserveAllTokens) {
        List<String> list = split(str, separatorChars, max, preserveAllTokens);
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return new String[0];
        }
        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }

    public static List<String> split(final String str,
                                     final String separatorChars) {
        return split(str, separatorChars, -1, false);
    }

    public static List<String> split(final String str,
                                     final String separatorChars, final int max,
                                     final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return new ArrayList<String>(0);
        }
        final List<String> list = new ArrayList<String>();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list;
    }

    public static String join(Object collection, String seperator) {
        if (collection == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        if (collection.getClass().isArray()) {
            int length = Array.getLength(collection);
            if (length == 0) {
                return null;
            }
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    buf.append(seperator);
                }
                buf.append(Array.get(collection, i).toString());
            }
        } else {
            Iterable<?> iterable = (Iterable<?>) collection;
            int i = 0;
            for (Iterator<?> iter = iterable.iterator(); iter.hasNext(); ) {
                Object item = iter.next();
                if (i > 0) {
                    buf.append(seperator);
                }
                buf.append(item.toString());
                i++;
            }
            if (i == 0) {
                return null;
            }
        }

        return buf.toString();
    }

    public static String join(Object collection) {
        return join(collection, ",");
    }

    public static boolean isEmail(String input) {
        if (input == null)
            return false;
        if (input.length() > 50)
            return false;
        Matcher matcher = EMAIL_CHECKER.matcher(input);
        return matcher.matches();
    }

    public static boolean isChinaMobile(String input) {
        if (input == null)
            return false;
        if (input.length() != 11)
            return false;
        Matcher matcher = MOBILE_CHECKER.matcher(input);
        return matcher.matches();
    }

    public static String underline2camel(String s) {
        if (s.indexOf("_") < 0) {
            return s;
        }
        StringBuilder newStr = new StringBuilder(s.length());
        char c;
        for (int i = 0; i < s.length(); ) {
            c = s.charAt(i);
            if (c == '_') {
                i++;
                if (i < s.length()) {
                    newStr.append(Character.toUpperCase(s.charAt(i)));
                    i++;
                    continue;
                }
            }
            newStr.append(c);
            i++;
        }
        return newStr.toString();
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

    // public static boolean isChinese(char c) { // 0x4e00 0x9fbb
    //
    // Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    //
    // if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
    //
    // || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
    //
    // || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
    //
    // // || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
    // //
    // // || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
    // //
    // // || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
    //
    // ) {
    //
    // return true;
    //
    // }
    //
    // return false;
    // }
    //
    // public static boolean isChinese(String s) {
    // if (StringUtil.isEmpty(s)) {
    // return false;
    // }
    // for (int i = 0; i < s.length(); i++) {
    // if (!isChinese(s.charAt(i))) {
    // return false;
    // }
    // }
    // return true;
    // }

}

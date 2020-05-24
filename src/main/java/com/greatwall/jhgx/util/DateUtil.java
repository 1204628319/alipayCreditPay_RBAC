package com.greatwall.jhgx.util;

import com.google.common.collect.Lists;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通用日期工具类
 * @date 2019/6/6 14:50
 **/
public class DateUtil extends org.apache.commons.lang3.time.DateUtils{
    /**
     * 标准日期
     */
    public static final String PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";
    /**
     * 无秒标准日期
     */
    public static final String PATTERN_STANDARD_NOT_SECOND = "yyyy-MM-dd HH:mm";
    /**
     * 有斜杠的标准日期
     */
    public static final String PATTERN_STANDARD_SLANT = "yyyy/MM/dd HH:mm:ss";
    /**
     * 无秒有斜杠日期
     */
    public static final String PATTERN_SLANT_NOT_SECOND = "yyyy/MM/dd HH:mm";
    /**
     * 带斜杠的时间格式
     */
    public static final String PATTERN_SLANT = "yyyy/MM/dd";
    /**
     * 没冒号的时间格式 yyyyMMdd
     */
    public static final String PATTERN_YYYYMMDD = "yyyyMMdd";

    public static final String PATTERN_DATE_YYYYMM = "yyyyMM";

    /**
     * 年月日格式
     */
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    /**
     * str dest null
     */
    public static final String STR_DEST_NULL = "str dest null";

    /**
     * 时分秒 HH:mm:ss
     */
    public static final String PATTERN_TIME = "HH:mm:ss";

    /**
     * 没冒号的时间格式 yyyyMMddHHmmss
     */
    public static final String PATTERN_NO_COLON = "yyyyMMddHHmmss";
    /**
     * 没冒号的时间格式(月日时) MMddHH
     */
    public static final String PATTERN_MMddHH = "MMddHH";
    /**
     * 年月日 时分秒 毫秒
     */
    public static final String PATTERN_MS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 年月时间格式
     */
    public static final String PATTERN_YYYY_MM = "yyyy-MM";
    /**
     * 一天的时长,毫秒
     */
    public static final long ONE_DAY = 24 * 60 * 60 * 1000L;
    /**
     * 一小时的时长,毫秒
     */
    private static final long ONE_HOUR = 60 * 60 * 1000L;
    /**
     * 半小时的时长,毫秒
     */
    public static final long ONE_HALF_HOUR = 30 * 60 * 1000L;
    /**
     * 一分钟的时长,毫秒
     */
    public static final long ONE_MINUTE = 60 * 1000L;
    /**
     * 一秒钟的时长,毫秒
     */
    public static final long ONE_SECOND = 1000L;

    /**
     * 零点
     */
    public static final String ZERO_HOUR_STR = " 00:00:00";

    /**
     * 24点
     */
    public static final String TWENTY_FOUR_HOUR_STR = " 23:59:59";

    /**
     * 日期格式化模版
     */
    private static final String[] DATE_PATTERNS = new String[]{PATTERN_STANDARD, PATTERN_STANDARD_NOT_SECOND, PATTERN_STANDARD_SLANT,
            PATTERN_SLANT_NOT_SECOND, PATTERN_SLANT, PATTERN_YYYYMMDD, PATTERN_DATE, PATTERN_TIME, PATTERN_NO_COLON,
            PATTERN_MMddHH, PATTERN_MS };

    /**
     * 私有构造方法
     */
    private DateUtil() {

    }

    /**
     * 字符串转日期
     *
     * @param date 字符串
     * @return 日期
     */
    public static Date parseDate(String date) {
        try {
            return parseDate(date, DATE_PATTERNS);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String timestamp2String(Timestamp timestamp, String pattern) {
        if (timestamp == null) {
            throw new java.lang.IllegalArgumentException("timestamp null illegal");
        }
        String fmt = pattern == null || "".equals(pattern) ? PATTERN_STANDARD : pattern;

        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(new Date(timestamp.getTime()));
    }

    public static String date2String(java.util.Date date, String pattern) {
        if (date == null) {
            throw new java.lang.IllegalArgumentException("timestamp null illegal");
        }
        String fmt = pattern == null || "".equals(pattern) ? PATTERN_STANDARD : pattern;

        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(date);
    }

    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String currentTimestamp2String(String pattern) {
        return timestamp2String(currentTimestamp(), pattern);
    }

    public static Timestamp string2Timestamp(String strDateTime, String pattern) throws ParseException {
        if (strDateTime == null || "".equals(strDateTime)) {
            throw new java.lang.IllegalArgumentException("Date Time Null Illegal");
        }
        String fmt = pattern == null || "".equals(pattern) ? PATTERN_STANDARD : pattern;

        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        Date date = sdf.parse(strDateTime);

        return new Timestamp(date.getTime());
    }

    public static Date string2Date(String strDate, String pattern) throws ParseException {
        if (strDate == null || "".equals(strDate)) {
            throw new java.lang.IllegalArgumentException("str date null");
        }

        String fmt = pattern == null || "".equals(pattern) ? PATTERN_DATE : pattern;

        SimpleDateFormat sdf = new SimpleDateFormat(fmt);

        return sdf.parse(strDate);
    }

    public static String stringToYear(String strDest) throws ParseException {
        if (strDest == null || "".equals(strDest)) {
            throw new java.lang.IllegalArgumentException(STR_DEST_NULL);
        }

        Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return String.valueOf(c.get(Calendar.YEAR));
    }

    public static String stringToMonth(String strDest) throws ParseException {
        if (strDest == null || "".equals(strDest)) {
            throw new java.lang.IllegalArgumentException(STR_DEST_NULL);
        }

        Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int month = c.get(Calendar.MONTH);
        month = month + 1;
        if (month < 10) {
            return "0" + month;
        }
        return String.valueOf(month);
    }

    public static String stringToDay(String strDest) throws ParseException {
        if (strDest == null || "".equals(strDest)) {
            throw new java.lang.IllegalArgumentException(STR_DEST_NULL);
        }

        Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int day = c.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            return "0" + day;
        }
        return Integer.toString(day);
    }

    public static Date getFirstDayOfMonth(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = 1;
        c.set(year, month, day, 0, 0, 0);
        return c.getTime();
    }

    public static Date getLastDayOfMonth(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = 1;
        if (month > 11) {
            month = 0;
            year = year + 1;
        }
        c.set(year, month, day - 1, 0, 0, 0);
        return c.getTime();
    }

    public static String date2GregorianCalendarString(Date date) throws DatatypeConfigurationException {
        if (date == null) {
            throw new java.lang.IllegalArgumentException("Date is null");
        }
        long tmp = date.getTime();
        GregorianCalendar ca = new GregorianCalendar();
        ca.setTimeInMillis(tmp);

        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(ca);

        return xmlGregorianCalendar.normalize().toString();
    }

    public static boolean compareDate(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            return false;
        }

        String strFirstDate = date2String(firstDate, PATTERN_DATE);
        String strSecondDate = date2String(secondDate, PATTERN_DATE);

        return strFirstDate.equals(strSecondDate);
    }

    public static Date getStartTimeOfDate(Date currentDate) throws ParseException {
        String strDateTime = date2String(currentDate, PATTERN_DATE) + " 00:00:00";
        return string2Date(strDateTime, PATTERN_STANDARD);
    }

    public static Date getEndTimeOfDate(Date currentDate) throws ParseException {
        String strDateTime = date2String(currentDate, PATTERN_DATE) + " 23:59:59";
        return string2Date(strDateTime, PATTERN_STANDARD);
    }

    public static long dateAddMonthToLong(int month, int dateType) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, month);
        String fmt;
        if (dateType == 1) {
            fmt = "yyyyMMddHHmm";
        } else {
            fmt = "yyyyMMdd";
        }
        return Long.parseLong(date2String(c.getTime(), fmt));
    }

    public static Date dateAddMin(Date date, int min) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, min);

        return c.getTime();
    }

    public static Date dateAddDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, day);

        return c.getTime();
    }

    public static long getCurrtDateToLong(int dateType) {
        Date date = new Date();
        String fmt;
        if (dateType == 1) {
            fmt = "yyyyMMddHHmm";
        } else {
            fmt = "yyyyMMdd";
        }
        return Long.parseLong(date2String(date, fmt));
    }

    public static int daysOfTwo(Date sDate, Date eDate) {
        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(sDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(eDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;
    }

    /**
     * 字符串转日期
     *
     * @param date 字符串
     * @return 日期
     */
    public static Date parse(String date) {
        try {
            return new SimpleDateFormat(PATTERN_STANDARD).parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static int yearsBetween(Date beforeDate, Date afterDate){
        Calendar beforeCalendar = Calendar.getInstance();
        Calendar afterCalendar = Calendar.getInstance();
        beforeCalendar.setTime(beforeDate);
        afterCalendar.setTime(afterDate);
        int year = afterCalendar.get(Calendar.YEAR) - beforeCalendar.get(Calendar.YEAR);
        int surplus = afterCalendar.get(Calendar.DATE) - beforeCalendar.get(Calendar.DATE);
        int result = afterCalendar.get(Calendar.MONTH) - beforeCalendar.get(Calendar.MONTH);
        if(result < 0){
            result = 1;
        }else if(result == 0){
            result = surplus <= 0 ? 1 : 0;
        }else{
            result = 0;
        }
        return Math.abs(year) + result;
    }

    /**
     * 获取指定日期年份
     * @param date
     * @return
     */
    public static int year(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取当前年份
     * @return
     */
    public static int year(){
        return year(new Date());
    }

    /**
     * 计算相对于dateToCompare的年龄，长用于计算指定生日在某年的年龄
     *
     * @param birthDay 生日
     * @param dateToCompare 需要对比的日期
     * @return 年龄
     */
    public static int age(Date birthDay, Date dateToCompare) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToCompare);

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(String.format("Birthday is after date %s!", DateUtil.date2String(dateToCompare, DateUtil.PATTERN_DATE)));
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDay);
        int age = year - cal.get(Calendar.YEAR);

        int monthBirth = cal.get(Calendar.MONTH);
        if (month == monthBirth) {
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            if (dayOfMonth < dayOfMonthBirth) {
                // 如果生日在当月，但是未达到生日当天的日期，年龄减一
                age--;
            }
        } else if (month < monthBirth) {
            // 如果当前月份未达到生日的月份，年龄计算减一
            age--;
        }

        return age;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate
     *            较小的时间
     * @param bdate
     *            较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date newsmdate = sdf.parse(sdf.format(smdate));
        Date newBdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(newsmdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(newBdate);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / ONE_DAY;

        return Integer.parseInt(String.valueOf(betweenDays));
    }

    /**
     * 获取日期间的月份
     * @param minDate
     * @param maxDate
     * @return
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException{
        List<String> result = Lists.newArrayList();

        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_YYYY_MM);

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        // 设置开始月的日期为某月月1日
        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        // 设置结束月的日期为某月2日
        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            // 月份加1
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }


    /**
     * 返回开始和结束日期之间的yyyMMdd, 包括开始和结束日期
     * @param startDay
     * @param endDay
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static List<String> daysBetween(String startDay, String endDay, String pattern) throws ParseException {
        Date startDate = parseDate(startDay);
        int daysBetween = daysBetween(startDate, parseDate(endDay));
        List<String> days = Lists.newArrayList();
        while (daysBetween >= 0) {
            days.add(date2String(startDate, pattern));
            startDate = dateAddDay(startDate, 1);
            daysBetween--;
        }

        return days;
    }

    /**
     * 计算2个时间直接的小时差毫秒数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static long millisecondsBetween(Date startTime, Date endTime) {
        if ((startTime == null) || (endTime == null)) {
            return -1;
        }
        long ld1 = startTime.getTime();
        long ld2 = endTime.getTime();
        return ld2 - ld1;
    }

    /**
     * 计算2个时间直接的小时数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static long hoursBetween(Date startTime, Date endTime) {
        return millisecondsBetween(startTime, endTime) / ONE_HOUR;
    }

    /**
     * 计算2个时间直接的分钟数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static long minutesBetween(Date startTime, Date endTime) {
        return millisecondsBetween(startTime, endTime) / ONE_MINUTE;
    }

    /**
     * 计算2个时间直接的秒数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static long secondBetween(Date startTime, Date endTime) {
        return millisecondsBetween(startTime, endTime) / ONE_SECOND;
    }

    /**
     * 计算2个时间相差多少时间，返回类型为：00：03：34
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static String getSubTime(Date startTime, Date endTime) {
        long subTime = millisecondsBetween(startTime, endTime);
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_TIME);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return formatter.format(subTime);
    }

    /**
     * 计算2个时间间隔天数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static long daysBetweenDate(Date startTime, Date endTime) {
        return millisecondsBetween(startTime, endTime) / (ONE_DAY);
    }
}

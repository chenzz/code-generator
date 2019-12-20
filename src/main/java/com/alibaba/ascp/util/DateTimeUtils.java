package com.alibaba.ascp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 类 <code>DateTimeUtil</code>
 * 
 * @author inter
 * @version May 12, 2011
 */
public class DateTimeUtils {
    private static final Logger logger = Logger.getLogger(DateTimeUtils.class);

    public final static long MS_PER_DAY = 86400000L;
    public final static long MS_PER_HOUR = 3600000L;
    public final static long MS_PER_SECOND = 1000L;
    public final static long MS_PER_MINUTE = 60000L;

    public final static long SECOND_PER_DAY = 86400L;
    public final static long SECOND_PER_HOUR = 3600L;
    public final static long SECOND_PER_MINUTE = 60L;

    /**
     * 返回long类型yyyy-MM-dd HH:mm:ss格式时间
     * @param strDate yyyy-MM-dd HH:mm:ss格式时间
     * @return
     */
    public static long getTimeFromStrDate(String strDate) {
        long time = 0L;
        if (StringUtils.isEmpty(strDate)) {
            return time;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            time = simpleDateFormat.parse(strDate).getTime();
        } catch (ParseException e) {
            logger.error("input=" + strDate, e);
        }
        return time;
    }

    /**
     * 获取自定义格式字符串时间
     * @param strDate
     * @param format
     * @return
     */
    public static long getTimeFromStrDate(String strDate, String format) {
        long time = 0L;
        if (StringUtils.isEmpty(strDate)) {
            return time;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            time = simpleDateFormat.parse(strDate).getTime();
        } catch (ParseException e) {
            logger.error("input=" + strDate, e);
        }
        return time;
    }

    /**
     * 返回字符串时间
     * @param time 
     * @param dateFormat
     * @return
     */
    public static String getTimeFromLongDate(long time, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(time);
    }

    /**
     * 返回字符串yyyy-MM-dd HH:mm:ss格式时间
     * @param time
     * @return
     */
    public static String getTimeFromLongDate(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(time);
    }

    /**
     * // 时间居然这种json格式的
     * // {"time":1305190513304,"minutes":55,"seconds":13,"hours":16,"month":4,"timezoneOffset":-480,"year":111,"day":4,"date":12}
     * @param jsonDate
     * @return
     */
    public static long getTimeFromJsonDate(String jsonDate) {
        JSONObject jsonObject = JSONObject.parseObject(jsonDate);
        return Long.valueOf(jsonObject.getString("time"));
    }

    /**
     * 对应这种格式的时间：yyyymmddhhmmss
     * @param time
     * @return
     */
    public static String getStrTimeFromLongDate(long time) {
        return getTimeFromLongDate(time, "yyyyMMddHHmmss");
    }

    /**
     * 把yyyymmddhhmmss这种格式的时间转化成long型时间
     * @param time
     * @return
     */
    public static long getLongDateFromStrTime(String time) {
        return DateTimeUtils.getTimeFromStrDate(time, "yyyyMMddHHmmss");
    }

    /**
     * 获取一天的开始时间 例如：2013-07-03 00:00:00
     * @return
     */
    public static long getTodayBeginTime() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        return currentDate.getTimeInMillis();
    }

    /**
     * 安静的sleep
     * @param millis 毫秒数
     */
    public static void sleepQuietly(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            // eat that possible exception.
        }
    }

    /**
     * 现在时间
     * created by weiqiang.yang@2010-2-3
     * @return
     */
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * 过去多少时间毫秒
     * created by weiqiang.yang@2010-2-3
     * @param start
     * @return
     */
    public static long elapse(long start) {
        return DateTimeUtils.now() - start;
    }

    /**
     * 格式化
     * created by weiqiang.yang@2010-3-4
     * @param time
     * @param format
     * @return
     */
    public static String formatDate(long time, String format) {
        if (time <= 0) {
            return "N/A";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String dateStr = sdf.format(new Date(time));
            return dateStr;
        }
    }

    /**
     * 格式化 yyyy-MM-dd HH:mm:ss
     * created by weiqiang.yang@2010-3-4
     * @param time
     * @return
     */
    public static String format(long time) {
        return formatDate(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    /**
     * 根据入参获得入参所代表月份的第一天0点0分0秒的时间
     * @param time
     * */
    public static long getStartTimeOfMonth(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        Calendar start = Calendar.getInstance();
        start.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), 1, 0, 0, 0);
        return start.getTimeInMillis();
    }

    /**
     * 根据入参获得入参所代表月份的最后一天23:59:59
     * @param time
     * @return
     */
    public static long getEndTimeOfMonth(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        Calendar end = Calendar.getInstance();
        end.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.getActualMaximum(Calendar.DATE), 23, 59, 59);
        return end.getTimeInMillis();
    }

    /**
     * 获得当前时间的上个月第一天00:00:00
     * @return
     */
    public static long getFirstDayTimeOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获得当前时间的上个月最后一天23:59:59
     * @return
     */
    public static long getLastDayTimeOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1); // 往前减一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 根据入参获得入参所代表时间的前一天00:00:00的时间
     * @param time
     * @return
     */
    public static long getYesterdayStartTime(long time) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.setTimeInMillis(time);
        yesterday.add(Calendar.DATE, -1); // 往前减一天
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        return yesterday.getTimeInMillis();
    }

    /**
     * 根据入参获得入参所代表时间的前一天23:59:59的时间
     * @param time
     * @return
     */
    public static long getYesterdayEndTime(long time) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.setTimeInMillis(time);
        yesterday.add(Calendar.DATE, -1); // 往前减一天
        yesterday.set(Calendar.HOUR_OF_DAY, 23);
        yesterday.set(Calendar.MINUTE, 59);
        yesterday.set(Calendar.SECOND, 59);
        return yesterday.getTimeInMillis();
    }

    public static void main(String[] args) {
//        long current = System.currentTimeMillis();
//        long start = getStartTimeOfMonth(current);
//        System.out.println(getTimeFromLongDate(start));
        System.out.println(getTimeFromLongDate(getYesterdayStartTime(System.currentTimeMillis())));
        System.out.println(getTimeFromLongDate(getYesterdayEndTime(System.currentTimeMillis())));
    }
}

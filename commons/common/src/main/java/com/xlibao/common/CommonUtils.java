package com.xlibao.common;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     <b>部分可通用的基本方法</b>
 * </pre>
 *
 * @author chinahuangxc
 */
public class CommonUtils {

    /**
     * 日期格式 -- "年-月" 如：2001-01
     */
    public static final String Y_M = "yyyy-MM";
    /**
     * 日期格式 -- "年-月-日" 如：2001-01-01
     */
    public static final String Y_M_D = "yyyy-MM-dd";
    /**
     * 日期格式 -- "年-月-日 时" 如：2001-01-01 20
     */
    public static final String Y_M_D_HH = "yyyy-MM-dd HH";
    /**
     * 日期格式 -- "年-月-日 时:分" 如：2001-01-01 20:20
     */
    public static final String Y_M_D_HH_MM = "yyyy-MM-dd HH:mm";
    /**
     * 日期格式 -- "年-月-日 时:分:秒" 如：2001-01-01 20:20:00
     */
    public static final String Y_M_D_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式 -- "月-日 时:分" 如：01-01 20:20
     */
    public static final String M_D_H_MM = "MM-dd HH:mm";
    /**
     * 日期格式 -- "月日" 如：0101
     */
    public static final String MD = "MMdd";
    /**
     * 日期格式 -- "日" 如：01
     */
    public static final String D = "dd";
    /**
     * 日期格式(中国格式) -- "点分" 如：9点10分
     */
    public static final String HH_MM_CH = "HH点mm分";

    /**
     * 毫秒数 -- 1分钟
     */
    public static final long MINUTE_MILLISECOND_TIME = TimeUnit.MINUTES.toMillis(1);
    /**
     * 毫秒数 -- 半小时
     */
    public static final long HALF_AN_HOURS_MILLISECOND_TIME = MINUTE_MILLISECOND_TIME * 30;
    /**
     * 毫秒数 -- 1小时
     */
    public static final long HOURS_MILLISECOND_TIME = HALF_AN_HOURS_MILLISECOND_TIME * 2;
    /**
     * 毫秒数 -- 1天
     */
    public static final long DAY_MILLISECOND_TIME = HOURS_MILLISECOND_TIME * 24;
    /**
     * 毫秒数 -- 1周
     */
    public static final long WEEK_MILLISECOND_TIME = DAY_MILLISECOND_TIME * 7;

    /**
     * 校准时区
     */
    public static final long TIME_ZONE_OFFSET_MILLIS = TimeZone.getDefault().getRawOffset();

    /**
     * 一个固定内存的对象 用于某些Map场景 当value值为固定对象且没有存在意义时 可直接使用该对象 主要是为了节省内存空间
     */
    public static final Object STATIC_FINAl = new Object();

    /**
     * 分隔符 -- 空格
     */
    public static final String SPACE = " ";
    /**
     * 分隔符 -- 英文逗号
     */
    public static final String SPLIT_COMMA = ",";
    /**
     * 分隔符 -- 英文下划线
     */
    public static final String SPLIT_UNDER_LINE = "_";
    /**
     * 分隔符 -- 换行
     */
    public static final String ENTER = "\r\n";

    // 16进制数字字符集
    private static String hexString = "0123456789ABCDEF";

    private static final Map<Integer, String> WEEK_FOR_CH = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    static {
        WEEK_FOR_CH.put(1, "周一");
        WEEK_FOR_CH.put(2, "周二");
        WEEK_FOR_CH.put(3, "周三");
        WEEK_FOR_CH.put(4, "周四");
        WEEK_FOR_CH.put(5, "周五");
        WEEK_FOR_CH.put(6, "周六");
        WEEK_FOR_CH.put(7, "周日");
    }

    /**
     * 今天凌晨相对应的毫秒数
     */
    public static long getTodayEarlyMorningMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static String dateFormat(Date date) {
        return dateFormat(date.getTime());
    }

    public static String dateFormat(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    public static String dateFormatHours(long time) {
        return new SimpleDateFormat("HH:mm").format(new Date(time));
    }

    public static String nowFormat() {
        return dateFormat(System.currentTimeMillis());
    }

    public static String todayFormat() {
        return defineDateFormat(System.currentTimeMillis(), Y_M_D);
    }

    public static long dateFormatToLong(String dateTime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime).getTime();
        } catch (ParseException ex) {
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static long hoursMillisecond(float i) {
        return (long) (i * HOURS_MILLISECOND_TIME);
    }

    public static long dayMillisecond(int i) {
        return i * DAY_MILLISECOND_TIME;
    }

    public static int dayOfYear(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static String currentMonthOfCH(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int month = calendar.get(Calendar.MONTH) + 1;

        return (month < 10 ? "0" + month : month) + "月";
    }

    public static String currentDayOfCH(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return (day < 10 ? "0" + day : day) + "日";
    }

    /**
     * 指定时间的小时(24小时制)
     */
    public static int currentHours(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static long getMinuteMillisecondTime(int minute) {
        return MINUTE_MILLISECOND_TIME * minute;
    }

    public static int daysForMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * <pre>
     * 是否仍然处于冷却时间
     * 当给定的匹配时间大于当前时间减去指定分钟数时返回true(表示处于冷却时间内)
     * @param matchMillsecondTime 用于匹配的时间
     * @param minute 期望冷却的时间(单位：分钟)
     * </pre>
     */
    public static boolean isChillDownMillisecondTime(long matchMillsecondTime, int minute) {
        // 当前时间 - 5分钟
        long time = System.currentTimeMillis() - getMinuteMillisecondTime(minute);
        return matchMillsecondTime > time;
    }

    /**
     * 获取当前的年
     */
    public static int year() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前的月
     */
    public static int month() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public static int monthForCH() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前的日
     */
    public static int day() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 今天相对于本月的天数
     */
    public static int dayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int dayOfMonth(long l) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 今天相对于今年的天数
     */
    public static int dayOfYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 当前小时
     */
    public static int currentHours() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int dayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int dayOfWeekForCh() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek = 7;
        } else {
            dayOfWeek--;
        }
        return dayOfWeek;
    }

    public static String dayOfWeekForTime(long time) {
        return WEEK_FOR_CH.get(dayOfWeekForCh(time));
    }

    public static String todayForCH() {
        return defineDateFormat(System.currentTimeMillis(), Y_M_D);
    }

    public static int dayOfWeekForCh(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek = 7;
        } else {
            dayOfWeek--;
        }
        return dayOfWeek;
    }

    public static boolean isCurrentWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long weekBeginTime = calendar.getTimeInMillis();

        calendar.set(Calendar.DAY_OF_WEEK, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        long weekEndTime = calendar.getTimeInMillis();

        return time >= weekBeginTime && time <= weekEndTime;
    }

    /**
     * <pre>
     * 获取今天指定小时、分钟的毫秒数
     * <b>注意：</b>秒和毫秒都为0
     * </pre>
     */
    public static long getTimeMillisecond(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static long getTimeMillisecond(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static long getTimeMillisecond(int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static int toMouth(long timeMillisecond) {
        return (int) (timeMillisecond / 1000 / 60 / 60 / 24 / 30);
    }

    public static int toDay(long timeMillisecond) {
        return (int) (timeMillisecond / 1000 / 60 / 60 / 24);
    }

    public static int toHour(long timeMillisecond) {
        return (int) (timeMillisecond / 1000 / 60 / 60);
    }

    public static int toMinute(long timeMillisecond) {
        return (int) (timeMillisecond / 1000 / 60);
    }

    public static int getHour(long timeSecond) {
        return (int) ((timeSecond / 3600));
    }

    public static int getMinute(long timeSecond) {
        return (int) ((timeSecond % 3600) / 60);
    }

    /**
     * <pre>
     * 通过距离计算显示的字符串内容
     * 当目标距离小于1公里的时候显示结果以米为单位
     * 当目标距离大于1公里的时候显示结果以公里(千米)为单位
     * </pre>
     *
     * @param distance 目标距离(单位必须为米)
     */
    public static String formatDistance(double distance) {
        return distance < 1000 ? (((int) distance) + "米") : (CommonUtils.formatNumber(distance / 1000f, "#.##") + "公里");
    }

    /**
     * <pre>
     * 计算公里数，不足一公里按一公里算
     * </pre>
     *
     * @param distance 目标距离(单位必须为米)
     */
    public static int formatKilometer(double distance) {
        int kilometer = (int) (distance / 1000);
        if (distance % 1000 > 0) {
            kilometer += 1;
        }
        return kilometer;
    }

    public static boolean isMobileNum(String matchValue) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{1}\\d{8}$");
        Matcher m = p.matcher(matchValue.trim());
        return m.find();
    }

    public static boolean isNumber(String matchValue) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(matchValue);
        return m.matches();
    }

    public static boolean isDouble(String matchValue) {
        Pattern p = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$");
        Matcher m = p.matcher(matchValue);
        return m.matches();
    }

    public static boolean isLegalChar(String matchValue, int minLength, int maxLength) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]{" + minLength + SPLIT_COMMA + maxLength + "}$");
        Matcher m = p.matcher(matchValue);
        return m.find();
    }

    public static boolean isLegalPassword(String matchValue, int minLength, int maxLength) {
        Pattern p = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{" + minLength + SPLIT_COMMA + maxLength + "})");
        Matcher m = p.matcher(matchValue);
        return m.find();
    }

    public static boolean isLegalNumberPassword(String matchValue, int minLength, int maxLength) {
        Pattern p = Pattern.compile("^[0-9]{" + minLength + SPLIT_COMMA + maxLength + "}$");
        Matcher m = p.matcher(matchValue);
        return m.find();
    }

    public static boolean isIPAddress(String matchValue) {
        Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

        Matcher m = pattern.matcher(matchValue);

        return m.find();
    }

    public static boolean isEmail(String matchValue) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

        Matcher m = pattern.matcher(matchValue);

        return m.find();
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static String defineDateFormat(long time, String format) {
        return new SimpleDateFormat(format).format(new Date(time));
    }

    /**
     * <pre>
     * 获取过去时间里指定小时、分钟的最接近毫秒数
     * <b>如：</b>参数为05，00
     * 若当前时间是04点30分那么将返回昨天05点00的毫秒数
     * 若当前时间是05点30分那么将返回今天05点00的毫秒数
     * </pre>
     */
    public static long getLastTimeMillisecond(int hour, int minute) {
        // 获取今日的指定时间毫秒数
        long lastTimeMillisecond = CommonUtils.getTimeMillisecond(hour, minute);
        // 当前系统时间
        long currentTime = System.currentTimeMillis();
        // 当前系统时间未到今日指定的时间(仍然为未来时间)
        if (currentTime < lastTimeMillisecond) {
            // 将时间设置为昨天相应的小时、分钟时间毫秒
            lastTimeMillisecond -= CommonUtils.DAY_MILLISECOND_TIME;
        }
        return lastTimeMillisecond;
    }

    /**
     * 判断给定的匹配时间(毫秒)是否已经超过了当天凌晨5点
     */
    public static boolean isBeyondEarlyMorningFiveHour(long matchTime) {
        return isBeyondLastTimeMillisecond(5, 0, matchTime);
    }

    /**
     * <pre>
     * 给定一个用于匹配的时间(毫秒)判断是否超过了最接近过去时间中指定的对应毫秒
     * </pre>
     */
    public static boolean isBeyondLastTimeMillisecond(int lastHour, int lastMinute, long matchTimeMillisecond) {
        // 获取过去的时间中指定小时、分钟的毫秒数
        long lastTimeMillisecond = getLastTimeMillisecond(lastHour, lastMinute);
        return matchTimeMillisecond >= lastTimeMillisecond;
    }

    /**
     * 获取当前时间的分钟数
     */
    public static int currentMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    public static String currentMinutes() {
        int minute = currentMinute();
        return minute < 10 ? "0" + minute : minute + "";
    }

    /**
     * 获取指定时间的分钟数
     */
    public static String currentMinute(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int minute = calendar.get(Calendar.MINUTE);
        return minute < 10 ? "0" + minute : minute + "";
    }

    /**
     * 给定比较的时间是否为今天
     */
    public static boolean isToday(long compareTime) {
        long nowTime = System.currentTimeMillis();
        return isSameDay(nowTime, compareTime);
    }

    /**
     * 给定的两个比较时间是否为同一天
     */
    public static boolean isSameDay(long sourceTime, long targetTime) {
        sourceTime += TIME_ZONE_OFFSET_MILLIS;
        targetTime += TIME_ZONE_OFFSET_MILLIS;

        long nowDay = sourceTime / DAY_MILLISECOND_TIME;
        long compareDay = targetTime / DAY_MILLISECOND_TIME;
        return nowDay == compareDay;
    }

    public static long delayTime(int delayNumberDay, int delayNumberHour, int delayNumberMinue, int delaySecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + delayNumberDay);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + delayNumberHour);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + delayNumberMinue);
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + delaySecond);
        return calendar.getTimeInMillis();
    }

    /**
     * <pre>
     * 检查当前时间是否在指定的时间范围内；精确到分钟，秒默认为0
     * beginHour 时间范围的开始时刻(小时)
     * beginMinue 时间范围的开始时刻(分钟)
     *
     * endHour 时间范围的结束时刻(小时)
     * endMinue 时间范围的结束时刻(分钟)
     * </pre>
     */
    public static boolean isRangeTime(int beginHour, int beginMinute, int endHour, int endMinute) {
        long beginTime = getTimeMillisecond(beginHour, beginMinute);
        long endTime = getTimeMillisecond(endHour, endMinute);

        long nowTime = System.currentTimeMillis();

        return beginTime <= nowTime && nowTime <= endTime;
    }

    public static boolean isRangeTime(int beginMonth, int beginDay, int beginHour, int beginMinute, int endMonth, int endDay, int endHour, int endMinute) {
        long beginTime = getTimeMillisecond(beginMonth, beginDay, beginHour, beginMinute);
        long endTime = getTimeMillisecond(endMonth, endDay, endHour, endMinute);

        long nowTime = System.currentTimeMillis();
        return beginTime <= nowTime && nowTime <= endTime;
    }

    /**
     * <pre>
     * 检查指定时间是否在指定的范围内；精确到小时
     *
     * beginHour 时间范围的开始时刻(小时)
     * endHour 时间范围的结束时刻(小时)
     *
     * matchTime 用于匹配的时间
     * </pre>
     */
    public static boolean isRangeTime(int beginHour, int endHour, long matchTime) {
        int hour = currentHours(matchTime);

        return beginHour <= hour && endHour > hour;
    }

    public static long startOfMonth() {
        return startOfMonth(1, 0, 0, 0);
    }

    public static long endOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    public static int lastDayOfMonthForCH(int month) {
        return lastDayOfMonth(month - 1);
    }

    public static int lastDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int lastDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前月份指定日期的时间
     */
    public static long startOfMonth(int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * <pre>
     * 当second小于60时返回包含秒的数据
     * 当second小于3600时返回包含分的数据
     * 当second大于等于3600时返回包含时、分的数据
     * </pre>
     */
    public static String secondToTimeValue(long second) {
        StringBuilder timeVlue = new StringBuilder();
        if (second < 60) {
            timeVlue.append(second).append("秒");
            return timeVlue.toString();
        }
        long minute = second / 60;
        if (minute < 60) {
            timeVlue.append(minute).append("分钟");
            return timeVlue.toString();
        }
        long hour = minute / 60;
        minute = minute % 60;
        timeVlue.append(hour).append("小时").append(minute).append("分钟");

        return timeVlue.toString();
    }

    public static String formatNumber(double value, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(value);
    }

    public static String formatAmount(long amount) {
        return formatNumber(amount / 100f, "0.00");
    }

    public static boolean isNullString(String value) {
        return value == null || value.trim().length() <= 0;
    }

    public static String nullToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    public static boolean isNotNullString(String value) {
        return value != null && value.trim().length() > 0 && !value.equals("null");
    }

    public static String md5(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(str.getBytes("UTF8"));
            byte bytes[] = m.digest();

            for (byte aByte : bytes) {
                if ((aByte & 0xff) < 0x10) {
                    sb.append("0");
                }
                sb.append(Long.toString(aByte & 0xff, 16));
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static String numberToString(byte v) {
        return String.valueOf(v);
    }

    public static String numberToString(short v) {
        return String.valueOf(v);
    }

    public static String numberToString(int v) {
        return String.valueOf(v);
    }

    public static String numberToString(long v) {
        return String.valueOf(v);
    }

    public static String numberToString(float v) {
        return String.valueOf(v);
    }

    public static String numberToString(double v) {
        return String.valueOf(v);
    }

    public static String hideChar(String value, int hideStartIndex, int hideEndIndex) {
        value = nullToEmpty(value);
        if (value.length() < hideStartIndex || value.length() < hideEndIndex) {
            return value;
        }
        if (hideStartIndex > hideEndIndex) {
            int tmp = hideEndIndex;
            hideEndIndex = hideStartIndex;
            hideStartIndex = tmp;
        }
        String startSubString = value.substring(0, hideStartIndex);
        String endSubString = value.substring(hideEndIndex, value.length());

        StringBuilder hideString = new StringBuilder();
        for (int i = 0; i < hideEndIndex - (hideStartIndex); i++) {
            hideString.append("*");
        }
        return startSubString + hideString.toString() + endSubString;
    }

    public static String parameterSort(Map<String, String> params, List<String> filterParameter) {
        String[] keys = params.keySet().toArray(new String[0]);
        // 排序
        Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
        String v;

        StringBuilder builder = new StringBuilder();
        for (String key : keys) {
            if (filterParameter.contains(key)) {
                continue;
            }
            v = params.get(key);
            if (isNullString(v)) {
                continue;
            }
            builder.append(key).append("=").append(v).append("&");
        }
        return builder.toString();
    }

    public static void fillSignature(Map<String, String> parameters, String key) {
        String signature = signature(parameters, key);

        parameters.put("sign", signature);
    }

    public static boolean simpleSignature(String localSignatureParameters, String matchSignature) {
        logger.info("Before signature " + localSignatureParameters);
        // MD5加密
        localSignatureParameters = md5(localSignatureParameters).toUpperCase();
        logger.info("After signature " + localSignatureParameters + " match signature " + matchSignature);
        return matchSignature.equals(localSignatureParameters);
    }

    public static boolean matchSignature(Map<String, String> parameters, String matchSignature, String appKey) {
        String sortParameters = parameterSort(parameters, new ArrayList<>()) + "key=" + appKey;

        return simpleSignature(sortParameters, matchSignature);
    }

    public static String signature(Map<String, String> parameters, String key) {
        String sortParameter = parameterSort(parameters, new ArrayList<>());
        String signature = sortParameter + "key=" + key;
        logger.info("Before sign : " + signature);
        signature = md5(signature).toUpperCase();
        logger.info("After sign : " + signature);

        return signature;
    }

    public static String generateAccessToken(String key) {
        String value = System.currentTimeMillis() + new Random().nextInt() + key;
        // 获取数据指纹，指纹是唯一的
        return md5(value);
    }

    public static String timeInterval(Date date) {
        return timeInterval(System.currentTimeMillis() - date.getTime());
    }

    public static String timeInterval(long timeMillisecond) {
        int v = toMouth(timeMillisecond);
        if (v > 0) {
            return v + "个月前";
        }
        v = toDay(timeMillisecond);
        if (v > 0) {
            return v + "天前";
        }
        v = toHour(timeMillisecond);
        if (v > 0) {
            return v + "小时前";
        }
        v = toMinute(timeMillisecond);
        if (v > 0) {
            return v + "分钟前";
        }
        return "刚刚";
    }

    public static String shuffleString(String value) {
        char[] chars = value.toCharArray();

        List<Character> charList = new ArrayList<>();
        // 填充到list中
        for (char c : chars) {
            charList.add(c);
        }
        // 对list进行洗牌
        Collections.shuffle(charList);
        // 回写到char数组
        for (int i = 0; i < charList.size(); i++) {
            chars[i] = charList.get(i);
        }
        // 转字符串
        return String.valueOf(chars);
    }

    public static boolean luhn(String bankNumber) {
        if (!isNumber(bankNumber)) {
            return false;
        }
        if (isNullString(bankNumber)) {
            return false;
        }
        if (bankNumber.length() != 16 && bankNumber.length() != 19) {
            return false;
        }
        int length = bankNumber.length();
        // （1）保留校验位
        int lastNumber = Integer.parseInt(bankNumber.substring(bankNumber.length() - 1));
        System.out.println("校验位数字: " + lastNumber);
        // （2）去除校验位后的卡号
        bankNumber = bankNumber.substring(0, bankNumber.length() - 1);
        System.out.println("去除校验位后的卡号：" + bankNumber);

        int sum = 0;
        // （3）反方向排列后，从右往左，偶数位乘以2；
        boolean c = (bankNumber.length() % 2 == 1);
        System.out.print("从右到左的数字序列为(去除校验位)：");
        for (int i = length - 1; i > 0; i--) {
            int n = Integer.parseInt(bankNumber.substring(i - 1, i));
            System.out.print(n + "  ");
            if (i % 2 == (c ? 1 : 0)) {
                // （4） 清理两位数字，如果乘以2得到的数字是两位，即大于等于10，就把这两位相加得到一位数字；
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
        }
        System.out.println();
        // （5） 相加模10：把步骤3、4得到的各位数字相加，以10取模(求余)后，用10相减即得到校验数字。
        return (sum + lastNumber) % 10 == 0;
    }

    public static String unRounding(double value) {
        int i = (int) (value * 100);
        return String.valueOf(i / 100f);
    }

    public static int countChineseCharacter(String chineseStr) {
        int count = 0;
        char[] charArray = chineseStr.toCharArray();
        for (char aCharArray : charArray) {
            if (isChinese(aCharArray)) {
                count++;
            }
        }
        return count;
    }

    public static int totalCharacterLength(String value) {
        int count = 0;
        char[] charArray = value.toCharArray();
        for (char aCharArray : charArray) {
            count++;
            if (isChinese(aCharArray)) {
                count++;
            }
        }
        return count;
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    /**
     * 将字符串转换为16进制后获取对应的2进制字节数组
     */
    public static byte[] encodeHexString(String value) {
        return hex2byte(encodeHexParameter(value));
    }

    /**
     * 将16进制对应的2进制字节转换为字符串
     */
    public static String decodeHexBytes(byte[] bytes) {
        return decodeHexMessage(byte2Hex(bytes));
    }

    public static String byte2Hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(hexString.charAt((b & 0xf0) >> 4));
            builder.append(hexString.charAt((b & 0x0f)));
        }
        return builder.toString();
    }

    public static byte[] hex2byte(String hex) {
        hex = hex.replaceAll(CommonUtils.SPACE, "");

        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];

        byte temp;
        for (int p = 0; p < bytes.length; p++) {
            temp = (byte) (hexString.indexOf(hex2char[2 * p]) * 16);
            temp += hexString.indexOf(hex2char[2 * p + 1]);
            bytes[p] = (byte) (temp & 0xff);
        }
        return bytes;
    }

    /**
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */
    public static String encodeHexParameter(String parameter) {
        // 根据默认编码获取字节数组
        byte[] bytes = parameter.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        // 将字节数组中每个字节拆解成2位16进制整数
        for (byte b : bytes) {
            sb.append(hexString.charAt((b & 0xf0) >> 4));
            sb.append(hexString.charAt(b & 0x0f));
        }
        return sb.toString();
    }

    /**
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String decodeHexMessage(String bytes) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2) {
            byteArrayOutputStream.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    public static String toHexString(int n, int length, String prefixMark) {
        return numberToString(Integer.toHexString(n), length, prefixMark);
    }

    public static String numberToString(String number, int length, String prefixMark) {
        int c = length - number.length();
        for (int i = 0; i < c; i++) {
            number = prefixMark + number;
        }
        return number;
    }

    public static String calculationSeparator(int maxSeparatorCount, String value, String separator) {
        int needSeparatorCount = maxSeparatorCount - CommonUtils.nullToEmpty(value).length();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < needSeparatorCount; i++) {
            builder.append(separator);
        }
        return builder.toString();
    }

    public static long changeMoney(String yuan) {
        try {
            return (long)(Double.parseDouble(yuan) * 100);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static JSONObject fillPageMessage(int totalSize, int currentPageIndex, int pageSize) {
        JSONObject response = new JSONObject();
        boolean isLastPage = (currentPageIndex * pageSize + pageSize) >= totalSize;
        int nextPage = isLastPage ? currentPageIndex : currentPageIndex + 1;
        int prePage = currentPageIndex <= 1 ? 1 : currentPageIndex - 1;
        int remain = totalSize % pageSize;
        int totalPage = totalSize / pageSize + (remain > 0 ? 1 : 0);

        response.put("isLastPage", isLastPage);
        response.put("nextPage", nextPage);
        response.put("prePage", prePage);
        response.put("totalPage", totalPage);
        response.put("currentPage", currentPageIndex);
        response.put("pageSize", pageSize);
        response.put("maxSize", totalSize);

        return response;
    }

    public static void main(String[] args) {
//        System.out.println("今天是星期" + dayOfWeekForCh() + "(中国)");
//        System.out.println("今天是星期" + dayOfWeek() + "(国际)");
//
//        long time = getTimeMillisecond(20, 30);
//        System.out.println("今天20点30分 " + dateFormat(time) + "  " + new Date(time) + "  毫秒数 " + time);
//        time = getTimeMillisecond(21, 30);
//        System.out.println("今天21点30分 " + dateFormat(time) + "  " + new Date(time) + "  毫秒数 " + time);
//
//        time = Calendar.getInstance().getTimeInMillis();
//
//        System.out.println("当前时间：" + dateFormat(time));
//
//        System.out.println("今天凌晨0点 " + dateFormat(getTodayEarlyMorningMillisecond()));
//        System.out.println(month() + 1 + "月1号凌晨5点 " + CommonUtils.dateFormat(CommonUtils.startOfMonth(1, 5, 0, 0)));
//
//        System.out.println(secondToTimeValue(85568));
//
//        float f = Float.parseFloat(CommonUtils.formatNumber(246953 / 1000f, "#.##"));
//        System.out.println(f);
//
//        System.out.println("isMobileNum " + isMobileNum("15912233456"));
//
//        System.out.println(toHour(1000 * 60 * 60 * 4));
//        System.out.println(toHour(15120000));
//
//        System.out.println(isLegalChar("_1a1a2b2sfasdfab3c", 6, 12));
//
//        System.out.println(isLegalPassword("1hhD123", 6, 20));
//
//        System.out.println(isIPAddress("120.24.57.17"));
//        System.out.println(isEmail("aa@p.qq"));
//
//        System.out.println(dateFormatToLong("2015-07-13 00:00:00"));
//        System.out.println(dateFormatToLong("2015-07-13 23:59:59"));
//
//        System.out.println("");
//        System.out.println(toHour(642960000) + " ----------- ");
//
//        System.out.println(dateFormat(1435641851996L));
//        System.out.println(dateFormat(1435646717995L));
//
//        System.out.println(dateFormat(1435641851996L));
//        System.out.println(dateFormat(1435641851996L));
//
//        System.out.println(dateFormat(1435675850422L));
//
//        System.out.println(isLegalNumberPassword("12344", 4, 6));
//        System.out.println("md5 " + md5("123456"));
//
//        System.out.println("本月开始时间 " + CommonUtils.dateFormat(CommonUtils.startOfMonth()));
//
//        System.out.println("本月结束时间 " + CommonUtils.dateFormat(CommonUtils.endOfMonth()));
//
//        System.out.println(isNumber("abc"));
//        System.out.println(isNumber("987"));
//        System.out.println(isNumber("98a"));
//
//        System.out.println(isDouble("100.0"));
//
//        System.out.println(generateAccessToken("10001"));
//        System.out.println(generateAccessToken("10001"));
//        System.out.println(generateAccessToken("10001"));
//        System.out.println(generateAccessToken("10001"));
//
//        System.out.println(hideChar("13800138000", 3, 7));
//
//        System.out.println(toMouth(TimeUnit.DAYS.toMillis(30)));
//        System.out.println(toDay(TimeUnit.DAYS.toMillis(10)));
//        System.out.println(toMouth(TimeUnit.DAYS.toMillis(30)));
//
//
//        System.out.println(timeInterval(System.currentTimeMillis() - dateFormatToLong("2016-12-22 12:02:12")));
//
//        System.out.println(dateFormat(getTimeMillisecond(currentHours() + 1, 5)));
//
//        for (int i = 1; i <= 12; i++) {
//            System.out.println(lastDayOfMonthForCH(i));
//        }

//        System.out.println(daysForMonth());
        System.out.println(luhn("6225768618540437"));
//        System.out.println(luhn("6222023602013173927"));
//        System.out.println(formatNumber(1023 / 100f, "0.00"));
//        System.out.println(isLegalChar("bjhnanhai", 6, 12));
//
//        System.out.println(shuffleString("123456"));
    }
}
/**
 * Copyright (C) Alibaba Cloud Computing
 * All rights reserved.
 *
 * 版权所有 （C）阿里云计算有限公司
 */
package com.xlibao.purchase.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
 * Util class for Date.
 *
 * @author xiaoming.yin
 */
public class DateUtil {
    private static final String RFC800_DATE_FORMAT =
            "yyyy-MM-dd HH:mm:ss";
    // RFC 822 Date Format
    private static final String RFC822_DATE_FORMAT =
            "EEE, dd MMM yyyy HH:mm:ss z";

    // ISO 8601 format
    private static final String ISO8601_DATE_FORMAT =
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    // Alternate ISO 8601 format without fractional seconds
    private static final String ALTERNATIVE_ISO8601_DATE_FORMAT =
            "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * Formats Date to GMT string.
     *
     * @param date
     * @return
     */
    public static String formatRfc822Date(Date date) {
        return getRfc822DateFormat().format(date);
    }


    /**
     * Formats Date to GMT string.
     *
     * @param date
     * @return
     */
    public static String formatRfc800Date(Date date) {
        return getRfc800DateFormat().format(date);
    }
    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);

        return dateString;
    }

    /**
     * Parses a GMT-format string.
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseRfc822Date(String dateString) throws ParseException {
        return getRfc822DateFormat().parse(dateString);
    }

    private static DateFormat getRfc822DateFormat() {
        SimpleDateFormat rfc822DateFormat =
                new SimpleDateFormat(RFC822_DATE_FORMAT, Locale.US);
        rfc822DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));

        return rfc822DateFormat;
    }
    private static DateFormat getRfc800DateFormat() {
        SimpleDateFormat rfc822DateFormat =
                new SimpleDateFormat(RFC800_DATE_FORMAT, Locale.CHINA);
        rfc822DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));

        return rfc822DateFormat;
    }

    public static String formatIso8601Date(Date date) {
        return getIso8601DateFormat().format(date);
    }

    public static String formatAlternativeIso8601Date(Date date) {
        return getAlternativeIso8601DateFormat().format(date);
    }

    /**
     * Parse a date string in the format of ISO 8601.
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseIso8601Date(String dateString) throws ParseException {
        try {
            return getIso8601DateFormat().parse(dateString);
        } catch (ParseException e) {
            return getAlternativeIso8601DateFormat().parse(dateString);
        }
    }

    private static DateFormat getIso8601DateFormat() {
        SimpleDateFormat df =
                new SimpleDateFormat(ISO8601_DATE_FORMAT, Locale.US);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));

        return df;
    }

    private static DateFormat getAlternativeIso8601DateFormat() {
        SimpleDateFormat df =
                new SimpleDateFormat(ALTERNATIVE_ISO8601_DATE_FORMAT, Locale.US);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));

        return df;
    }
}

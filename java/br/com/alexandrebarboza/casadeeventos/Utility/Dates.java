package br.com.alexandrebarboza.casadeeventos.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class Dates {

    public static Date StringToDate(String str, String language, String country, boolean full) {
        Locale locale = new Locale(language, country);
        SimpleDateFormat format;
        Date result = null;
        if (full == true) {
            format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", locale);
        } else {
            format = new SimpleDateFormat("dd/MM/yyyy", locale);
        }
        try {
            result = (Date) format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String ShortDateFromString(String arg, String language, String country) {
        Locale locale = new Locale(language, country);
        DateFormat format1 = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", locale);
        DateFormat format2 = new SimpleDateFormat("dd/MM/yyyy", locale);
        Date date1         = null;
        Date date2         = null;
        String string1, string2 = null;
        try {
            date1 = (Date) format1.parse(arg);
            string1 = format2.format(date1);
            date2 = (Date) format2.parse(string1);
            string2 = format2.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return string2;
    }

    public static java.util.Date convertFromDefaultDate(java.sql.Date sqlDate) {
        java.util.Date javaDate = null;
        if (sqlDate != null) {
            javaDate = new Date(sqlDate.getTime());
        }
        return javaDate;
    }

    public static Date getDateTime(String str) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDefaultFormatFromDate(Date dt) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(dt);
    }

    public static String getDefaultFormatFromDateTime(Date dt) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(dt);
    }

    public static String getShortFormatFromDateTime(Date dt) {
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        return df.format(dt);
    }

    public static java.sql.Date getSQLDateTime(String str) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date parsed;
        try {
            parsed = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return new java.sql.Date(parsed.getTime());
    }

    public static String getDefaultFormatFromSQLDateTime(java.sql.Date sqlDate) {
        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
        return getDefaultFormatFromDateTime(utilDate);
    }
}

package com.albat.mobachir.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Ashraf on 28/09/2016.
 */
public class CalendarHelper {
    private static String TAG = "CalendarHelper";

    public static String getCurrentDateFormatted(String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);  //"yyyy-MM-dd HH:mm:ss"
        String strDate = sdf.format(c.getTime());

        return strDate;
    }

    public static String getCurrentDateFormatted(String format, Calendar c) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);  //"yyyy-MM-dd HH:mm:ss"
        String strDate = sdf.format(c.getTime());

        return strDate;
    }

    public static Calendar getDatePart(Date date) {
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }

    public static Date getNextDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    public static Date stringToDate(String stringDate, String format) {
        return stringToDate(stringDate, format, null);
    }

    public static Date stringToDate(String stringDate, String format, String timezone) {
        try {
            DateFormat formatter = new SimpleDateFormat(format);
            if (timezone != null)
                formatter.setTimeZone(TimeZone.getDefault());
            Date date = formatter.parse(stringDate);
            return date;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static Long getMilliSecsBetween(String startDateString, String endDateString) {
        Long milliSeconds = null;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date startDate = formatter.parse(startDateString);
            Date endDate = formatter.parse(endDateString);

            milliSeconds = endDate.getTime() - startDate.getTime();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return milliSeconds;
    }

    public static int getDaysBetween(String startDateString, String endDateString) {
        int days = 0;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = formatter.parse(startDateString);
            Date endDate = formatter.parse(endDateString);

            days = (int) getDaysBetween(startDate, endDate);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return days;
    }

    public static long getDaysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public static int getCurrentMonthDays(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return daysOfMonth;
    }

    public static int getPreviousMonthDays(int month, int year) {
        if (month > 0)
            month--;
        else {
            month = 11;
            year--;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return daysOfMonth;
    }

    public static String getFirstDayOfMonthName(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        Date firstDayOfMonth = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEEEEE");
        String firstDay = sdf.format(firstDayOfMonth);
        return firstDay;
    }

    public static String formatDateEn(Date date, String newFormat) {
        try {
            if (date == null)
                return "";

            SimpleDateFormat format = new SimpleDateFormat(newFormat);
            String newDateString = format.format(date);
            return newDateString;
        } catch (Exception e) {
            //Log.e(TAG, e.getMessage(), e);
        }
        return "";
    }

    public static String formatDate(Date date, String newFormat) {
        return formatDate(date, newFormat, true);
    }

    public static String formatDate(Date date, String newFormat, boolean isArabic) {
        try {
            if (date == null)
                return "";
            SimpleDateFormat format;
            if (isArabic)
                format = new SimpleDateFormat(newFormat, new Locale("ar"));
            else
                format = new SimpleDateFormat(newFormat);
            String newDateString = format.format(date);
            return newDateString;
        } catch (Exception e) {
            //Log.e(TAG, e.getMessage(), e);
        }
        return "";
    }

    public static String reformatDateString(String date, String currentFormat, String newFormat) {
        try {
            if (date == null)
                return date;
            SimpleDateFormat format = new SimpleDateFormat(currentFormat);
            format.setTimeZone(TimeZone.getTimeZone("Africa/Casablanca"));
            Date newDate = format.parse(date);

            format = new SimpleDateFormat(newFormat);
            format.setTimeZone(TimeZone.getDefault());
            String newDateString = format.format(newDate);
            return newDateString;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return date;
    }

    public static String reformatDateString(String date, String currentFormat, String newFormat, String Timezone) {
        return reformatDateString(date, currentFormat, newFormat, Timezone, false);
    }

    public static String reformatDateString(String date, String currentFormat, String newFormat, String Timezone, boolean isArabic) {
        try {
            if (date == null)
                return date;
            SimpleDateFormat format = new SimpleDateFormat(currentFormat);
            format.setTimeZone(TimeZone.getTimeZone(Timezone));
            Date newDate = format.parse(date);

            if (isArabic)
                format = new SimpleDateFormat(newFormat, new Locale("ar"));
            else
                format = new SimpleDateFormat(newFormat);
            format.setTimeZone(TimeZone.getDefault());
            String newDateString = format.format(newDate);
            return newDateString;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return date;
    }

    public static String getNormalDateYYYMMDD(int calendarYear, int calendarMonth, int calendarDay) {
        String date = calendarYear + "-" + (calendarMonth + 1 < 9 ? "0" + (calendarMonth + 1) : calendarMonth + 1) + "-" + (calendarDay + 1 < 9 ? "0" + calendarDay : calendarDay);
        return date;
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static boolean isToday(Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}

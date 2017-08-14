package com.smile.calendar.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.smile.calendar.R;
import com.smile.calendar.module.EventModel;

import org.joda.time.LocalDate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日程事件
 * Created by Jimmy on 2016/10/6 0006. modified by xwf
 */
public class CalendarUtils {

    public static String calanderURL = "content://com.android.calendar/calendars";
    public static String calanderEventURL = "content://com.android.calendar/events";
    public static String calanderRemiderURL = "content://com.android.calendar/reminders";
    public final static int EXTERNAL_CALENDAR_REQ_CODE = 10;
    public final static String EVENT = "EVENT";

    private static CalendarUtils sUtils;
    private Map<String, int[]> sAllHolidays = new HashMap<>();
    private Map<String, List<Integer>> sMonthTaskHint = new HashMap<>();

    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 返回当前月份1号位于周几
     *
     * @param year  年份
     * @param month 月份，传入系统获取的，不需要正常的
     * @return 日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获得两个日期距离几周
     *
     * @return
     */
    public static int getWeeksAgo(int lastYear, int lastMonth, int lastDay, int year, int month, int day) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(lastYear, lastMonth, lastDay);
        end.set(year, month, day);
        int week = start.get(Calendar.DAY_OF_WEEK);
        start.add(Calendar.DATE, -week);
        week = end.get(Calendar.DAY_OF_WEEK);
        end.add(Calendar.DATE, 7 - week);
        float v = (end.getTimeInMillis() - start.getTimeInMillis()) / (3600 * 1000 * 24 * 7 * 1.0f);
        return (int) (v - 1);
    }

    /**
     * 获得两个日期距离几个月
     *
     * @return
     */
    public static int getMonthsAgo(int lastYear, int lastMonth, int year, int month) {
        return (year - lastYear) * 12 + (month - lastMonth);
    }

    public static int getWeekRow(int year, int month, int day) {
        int week = getFirstDayWeek(year, month);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        int lastWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (lastWeek == 7)
            day--;
        return (day + week - 1) / 7;
    }


    public static int getMonthRows(int year, int month) {
        int size = getFirstDayWeek(year, month) + getMonthDays(year, month) - 1;
        return size % 7 == 0 ? size / 7 : (size / 7) + 1;
    }

    /**
     * 得到上一天的时间
     */
    public static LocalDate getYesterday(int year, int month, int day) {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.set(year, month - 1, day);//月份是从0开始的，所以11表示12月

        //使用roll方法进行向前回滚
        //cl.roll(Calendar.DATE, -1);
        //使用set方法直接进行设置
        int inDay = ca.get(Calendar.DATE);
        ca.set(Calendar.DATE, inDay - 1);
        return new LocalDate(ca);
    }

    /**
     * android 6.0 以上申请权限
     */
    public static boolean requestPermission(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<Activity>(activity);
        Activity useActivity = activityWeakReference.get();
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),Manifest.permission.READ_CALENDAR)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity.getApplicationContext(),Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(useActivity,
                    Manifest.permission.READ_CALENDAR) || ActivityCompat.shouldShowRequestPermissionRationale(useActivity,
                    Manifest.permission.WRITE_CALENDAR)) {
                Toast.makeText(useActivity, useActivity.getString(R.string.write_calendar), Toast.LENGTH_SHORT).show();
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(useActivity,
                        new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                        EXTERNAL_CALENDAR_REQ_CODE);
            }
            return false;
        } else {
            //申请成功，进行相应操作
            return true;
        }

    }

    /**
     * 获取某月某日的日历
     * @param year
     * @param month
     */
    public static List<EventModel> getCalendarEvent(Context context, int year, int month) throws Exception {
        List<EventModel> eventModels = new ArrayList<>();
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month-1, 00, 0, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, 00, 0, 0);
        String selection = "((dtstart >= "+beginTime.getTimeInMillis()+") AND (dtend <= "+endTime.getTimeInMillis()+"))";
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CalendarUtils.calanderEventURL), null,
                selection, null, null);
        if (eventCursor.getCount() > 0) {
            if (eventCursor.moveToFirst()) {
                do {

                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));//日程事件标题
                    String description = eventCursor.getString(eventCursor.getColumnIndex("description"));//日程事件描术
                    String dtstart = eventCursor.getString(eventCursor.getColumnIndex("dtstart"));//日程事件开始时间，是13位字符串
//                    String address = eventCursor.getString(eventCursor.getColumnIndex("event_location"));//日程事件的位置，发现没有这个字段
                    String timeStart = TimeUtil.timeFormatStr(dtstart);//将日程时间改成yyyy-MM-dd hh:mm:ss形式
                    String dtend = eventCursor.getString(eventCursor.getColumnIndex("dtend"));//日程事件结束时间
                    String timeEnd = TimeUtil.timeFormatStr(dtend);//将日程时间改成yyyy-MM-dd hh:mm:ss形式
                    String calendar_id = eventCursor.getString(eventCursor.getColumnIndex("calendar_id"));//日程事件的id
                    String startTime = timeStart.substring(11, 16);//截取日程事件的开始时间的 时和分， hh:mm
                    String endtime = timeEnd.substring(11, 16);//截取日程事件的结束时间的 时和分， hh:mm
                    int startday = Integer.parseInt(timeStart.substring(8, 10));//截取日程事件的开始时间的 day， dd
                    int endday = Integer.parseInt(timeEnd.substring(8, 10));//截取日程事件的结束时间的 day， dd
                    int startMonth = Integer.parseInt(timeStart.substring(5, 7));//截取日程事件的开始时间的 月， mm
                    int endMonth = Integer.parseInt(timeEnd.substring(5, 7));//截取日程事件的结束时间的 月， mm
                    int startYear = Integer.parseInt(timeStart.substring(0, 4));//截取日程事件的开始时间的 年， yyyy
                    int endYear = Integer.parseInt(timeEnd.substring(0, 4));//截取日程事件的结束时间的 年， yyyy
                    int day = TimeUtil.DateCompareDiffDay(timeEnd, timeStart);//比较日程事件开始和结束时间，看看是否跨日了，跨日的那些天都需要特殊处理
                    String id = String.valueOf(System.currentTimeMillis());
                    {
                        if (day > 0) {//开始和结束时间大于一天
                            if (startYear - endYear > 0) {//跨年
                                for(int cm = startMonth; cm <= 12; cm++) {//当前年
                                    int cd = startMonth == cm ? startday : 1;
                                    int monthDays = CalendarUtils.getMonthDays(startYear, cm-1);
                                    for (; cd < monthDays; cd++) {
                                        startTime = (cd == startday && cm == startMonth) ? startTime : "00:00";
                                        EventModel eventModel = new EventModel(timeEnd, startTime, "23:59", description, startYear, cm, cd, id);
                                        eventModels.add(eventModel);
                                    }
                                }

                                for(int cm = 1; cm <= endMonth; cm++) {//最后一年
                                    int monthDays = (endMonth == cm) ? endday : CalendarUtils.getMonthDays(startYear, cm-1);
                                    for (int cd = 1; cd < monthDays; cd++) {
                                        endtime = (cd == endday && cm == endMonth) ? endtime : "23:59";
                                        EventModel eventModel = new EventModel(eventTitle, "00:00", endtime, description, endYear, cm, cd, id);
                                        eventModels.add(eventModel);
                                    }
                                }

                                if (endYear - startYear > 1) {//中间年份
                                    for (int y = startYear+1; y < endYear; y++) {
                                        for(int cm = 1; cm <= 12; cm++) {//最后一年
                                            int monthDays = CalendarUtils.getMonthDays(y, cm-1);
                                            for (int cd = 1; cd < monthDays; cd++) {
                                                EventModel eventModel = new EventModel(eventTitle, "00:00", "23:59", description, y, cm, cd, id);
                                                eventModels.add(eventModel);
                                            }
                                        }


                                    }
                                }
                            } else {//没有跨年
                                if (endMonth - startMonth > 0) {//跨月
                                    for (int i = startMonth; i < endMonth; i++) {//开始那月到倒数第二个月
                                        int monthDays = CalendarUtils.getMonthDays(startYear, i-1);//计算每月天数
                                        int j = i == startMonth ? startday : 1;//如果是开始月，则从开始月的当天算
                                        for (; j <= monthDays; j++) {
                                            startTime = (i == startMonth && j == startday) ? startTime : "00:00";//如果是开始月开始日，时间要从开始时间算
                                            EventModel eventModel = new EventModel(eventTitle, startTime, "23:59", description, startYear, i, j, id);
                                            eventModels.add(eventModel);
                                        }
                                    }
                                    for (int i = 1; i <= endday; i++) {//对于最后一个月的天事件插入
                                        EventModel eventModel = new EventModel(timeEnd, "00:00", "23:59", description, startYear, endMonth, i, id);
                                        eventModel.setEndTime(i == endday ? endtime : "23:59");//最后一第以写的时间为准
                                        eventModels.add(eventModel);
                                    }
                                } else {//没有跨月，只跨日了
                                    for (int i = startday; i <= endday; i++) {
                                        EventModel eventModel = new EventModel(eventTitle, "00:00", "23:59", description, startYear, startMonth, i, id);
                                        eventModel.setStartTime(i == startday ? startTime : "00:00");
                                        eventModel.setEndTime(i == endday ? endtime : "23:59");
                                        eventModels.add(eventModel);
                                    }
                                }
                            }

                        } else {//同一天只需要插入一条
                            eventModels.add(new EventModel(eventTitle, startTime, endtime, "", year, month, startday, String.valueOf(System.currentTimeMillis()), EVENT));
                        }
                    }

                } while (eventCursor.moveToNext());
            }

        }
        eventCursor.close();
        return eventModels;
    }

}


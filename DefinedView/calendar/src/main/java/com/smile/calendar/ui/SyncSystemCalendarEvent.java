package com.smile.calendar.ui;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.smile.calendar.R;
import com.smile.calendar.baseadapter.BaseRecyclerViewAdapter;
import com.smile.calendar.databinding.ActivitySyncSystemCalendarBinding;
import com.smile.calendar.module.EventModel;
import com.smile.calendar.util.CalendarUtils;
import com.smile.calendar.util.TimeUtil;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 同步手机日程
 * Created by SmileXie on 2017/8/3.
 */

public class SyncSystemCalendarEvent extends BaseActivity<ActivitySyncSystemCalendarBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_system_calendar);
        if (CalendarUtils.requestPermission(this)) {
            getCalendarEvent(2017, 8);
        }

    }

    private void getCalendarUser() {

        Cursor userCursor = getContentResolver().query(Uri.parse(CalendarUtils.calanderURL), null,
                null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToFirst();
            String userName = userCursor.getString(userCursor.getColumnIndex("name"));
        }
    }

    /**
     * 获取某月某日的日历
     * @param year
     * @param month
     */
    private void getCalendarEvent(int year, int month) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, 00, 0, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month+1, 00, 0, 0);
        String selection = "((dtstart >= "+beginTime.getTimeInMillis()+") AND (dtend <= "+endTime.getTimeInMillis()+"))";
        Cursor eventCursor = getContentResolver().query(Uri.parse(CalendarUtils.calanderEventURL), null,
                selection, null, null);
        if (eventCursor.getCount() > 0) {
            if (eventCursor.moveToFirst()) {
                do {
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
                    String description = eventCursor.getString(eventCursor.getColumnIndex("description"));
                    String dtstart = eventCursor.getString(eventCursor.getColumnIndex("dtstart"));
                    String timeStart = TimeUtil.timeFormatStr(dtstart);
                    String dtend = eventCursor.getString(eventCursor.getColumnIndex("dtend"));
                    String timeEnd = TimeUtil.timeFormatStr(dtend);
                    String calendar_id = eventCursor.getString(eventCursor.getColumnIndex("calendar_id"));
                } while (eventCursor.moveToNext());
            }

        }
    }

    /**
     * 向手机日程中写入事件
     */
    private void writeCalendarEvent() {
        //获取要出入的gmail账户的id
        String calId = "";
        Cursor userCursor = getContentResolver().query(Uri.parse(CalendarUtils.calanderURL), null,
                null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToFirst();
            calId = userCursor.getString(userCursor.getColumnIndex("_id"));

        }
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        event.put("title", "与苍井空小姐动作交流");
        event.put("description", "Frankie受空姐邀请,今天晚上10点以后将在Sheraton动作交流.lol~");
        //插入hoohbood@gmail.com这个账户
        event.put("calendar_id", calId);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 10);
        long start = mCalendar.getTime().getTime();
        mCalendar.set(Calendar.HOUR_OF_DAY, 11);
        long end = mCalendar.getTime().getTime();

        event.put("dtstart", start);
        event.put("dtend", end);
        event.put("hasAlarm", 1);

        Uri newEvent = getContentResolver().insert(Uri.parse(CalendarUtils.calanderEventURL), event);
        long id = Long.parseLong(newEvent.getLastPathSegment());
        ContentValues values = new ContentValues();
        values.put("event_id", id);
        //提前10分钟有提醒
        values.put("minutes", 10);
        getContentResolver().insert(Uri.parse(CalendarUtils.calanderRemiderURL), values);
        Log.e("xwf", "writeCalendarEvent 插入事件成功!!!");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CalendarUtils.EXTERNAL_CALENDAR_REQ_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getCalendarUser();
                    getCalendarEvent(2017, 8);
//                    writeCalendarEvent();
//                    getCalendarEvent();
                } else {
                    // Permission Denied
                    showShortToast(getString(R.string.write_calendar));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

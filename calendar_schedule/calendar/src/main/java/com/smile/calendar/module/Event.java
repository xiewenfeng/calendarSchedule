package com.smile.calendar.module;

/**
 * Created by SmileXie on 2017/7/26.
 */

public class Event {
    public String mName;
    public int mDayOfMonth;
    public String mStartTime;
    public String mEndTime;
    public String mColor;

    public Event(String mName, int mDayOfMonth, String mStartTime, String mEndTime, String mColor) {
        this.mName = mName;
        this.mDayOfMonth = mDayOfMonth;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mColor = mColor;
    }
}

package com.smile.calendar.module;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * 日程Module
 */
@RealmClass
public class EventModel extends RealmObject{

    private String mName;//标题
    private String mStartTime;//开始时间
    private String mEndTime;//结束时间
    private String mColor;//日程背景色
    private String moduleName;//模块名称
    private Date date;//日期
    private String f1;//保留字段
    private String f2;//保留字段
    private String f3;//保留字段
    private int year;//年
    private int month;
    private int day;

    private String id;

    public EventModel() {
        new EventModel("", "", "", "", 0, 0, 0, String.valueOf(System.currentTimeMillis()));
    }

    public EventModel(String name, String startTime, String endTime, String position, int year, int month, int day, String id) {
        this.mName = name;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.f1 = position;
        this.year = year;
        this.month = month;
        this.day = day;
        this.id = id;
    }

    public EventModel(String name, String startTime, String endTime, String position, int year, int month, int day, String id, String moduleName) {
        this.mName = name;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.f1 = position;
        this.year = year;
        this.month = month;
        this.day = day;
        this.id = id;
        this.moduleName = moduleName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        this.mStartTime = startTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        this.mEndTime = endTime;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        this.mColor = color;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getF1() {
        return f1;
    }

    public void setF1(String f1) {
        this.f1 = f1;
    }

    public String getF2() {
        return f2;
    }

    public void setF2(String f2) {
        this.f2 = f2;
    }

    public String getF3() {
        return f3;
    }

    public void setF3(String f3) {
        this.f3 = f3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

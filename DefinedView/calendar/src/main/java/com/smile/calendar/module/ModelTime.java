package com.smile.calendar.module;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 保存每个模块的最早请求时间和最晚请求时间
 * Created by SmileXie on 2017/8/2.
 */

public class ModelTime extends RealmObject {
    private String earlyDate;
    private String lastDate;
    @PrimaryKey
    private String moduleName;

    public ModelTime() {

    }

    public ModelTime(String startTime, String endTime, String moduleName) {
        this.earlyDate = startTime;
        this.lastDate = endTime;
        this.moduleName = moduleName;
    }

    public String getEarlyDate() {
        return earlyDate;
    }

    public void setEarlyDate(String earlyDate) {
        this.earlyDate = earlyDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}

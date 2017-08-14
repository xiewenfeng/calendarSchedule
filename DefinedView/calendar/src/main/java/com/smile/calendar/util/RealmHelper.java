package com.smile.calendar.util;

import com.smile.calendar.module.EventModel;
import com.smile.calendar.module.ModelTime;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * 数据库操作类
 */

public class RealmHelper {
    public static final String MODULE_NOTICE = "notice";
    public static final String DB_NAME = "myRealm.realm";
    private Realm mRealm;
    private SimpleDateFormat sdf1;

    private volatile static RealmHelper realmHelperInstance;

    private RealmHelper() {
        String pat1 = "yyyy-MM-dd" ;
        sdf1 = new SimpleDateFormat(pat1) ; // 实例化模板对象
        mRealm = Realm.getDefaultInstance();
    }

    public static RealmHelper getRealmHelperInstance() {
        if (realmHelperInstance == null) {
            synchronized (RealmHelper.class) {
                if (realmHelperInstance == null) {
                    realmHelperInstance = new RealmHelper();
                }
            }
        }
        return realmHelperInstance;
    }

    /**
     * add （增）
     */
    public void addEvent(final EventModel eventModel) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(eventModel);
        mRealm.commitTransaction();

    }

    /**
     * delete （删）
     */
    public void deleteEvent(String id) {
        EventModel eventModel = mRealm.where(EventModel.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        eventModel.deleteFromRealm();
        mRealm.commitTransaction();

    }

    /**
     * update （改）
     */
    public void updateEvent(String id, String newName) {
        EventModel eventModel = mRealm.where(EventModel.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        eventModel.setName(newName);
        mRealm.commitTransaction();
    }

    /**
     * query （查询所有）
     */
    public List<EventModel> queryAllEvent() {
        RealmResults<EventModel> eventModels = mRealm.where(EventModel.class).findAll();
        /**
         * 对查询结果，按Id进行排序，只能对查询结果进行排序
         */
        //增序排列
        eventModels=eventModels.sort("mStartTime");
//        //降序排列
//        dogs=dogs.sort("id", Sort.DESCENDING);
        return mRealm.copyFromRealm(eventModels);
    }

    /**
     * query （根据标题查）
     */
    public EventModel queryEventByTitle(String title) {
        EventModel eventModel = mRealm.where(EventModel.class).like("mName", title).findFirst();
        return eventModel;
    }


    /**
     * query （根据age查）
     */
    public List<EventModel> queryEventByMonth(int year, int month) {
        RealmResults<EventModel> eventModels = mRealm.where(EventModel.class)
                .equalTo("year", year)
                .equalTo("month", month)
                .findAllSorted("day", Sort.DESCENDING);
        return mRealm.copyFromRealm(eventModels);
    }

    /**
     * query （根据age查）
     */
    public List<EventModel> queryEventByMonth(int year, int month, String moduleName) {
        RealmResults<EventModel> eventModels = mRealm.where(EventModel.class)
                .equalTo("year", year)
                .equalTo("month", month)
                .equalTo("moduleName", moduleName)
                .findAllSorted("day", Sort.DESCENDING);
        return mRealm.copyFromRealm(eventModels);
    }

    /**
     * query （根据age查）
     */
    public List<EventModel> queryEventByModule(String moduleName) {
        RealmResults<EventModel> eventModels = mRealm.where(EventModel.class)
                .equalTo("moduleName", moduleName)
                .findAllSorted("day", Sort.DESCENDING);
        return mRealm.copyFromRealm(eventModels);
    }

    /**
     * 删除某年某月的数据
     * @param year
     * @param month
     */
    public void deleteMonthEvent(int year, int month) {
        RealmResults<EventModel> eventModels=  mRealm.where(EventModel.class)
                .equalTo("moduleName", RealmHelper.MODULE_NOTICE)
                .equalTo("year", year)
                .equalTo("month", month)
                .findAll();
        if (eventModels != null && eventModels.size() > 0) {
            mRealm.executeTransaction(realm1 -> eventModels.deleteAllFromRealm());
        }
    }




    /**
     * query （根据age查）
     */
    public List<EventModel> queryEventByDay(int year, int month, int day) throws ParseException {
        RealmResults<EventModel> eventModels = mRealm.where(EventModel.class)
                .equalTo("year", year)
                .equalTo("month", month)
                .equalTo("day", day)
                .findAll();
        return mRealm.copyFromRealm(eventModels);
    }

    public List<EventModel> queryById(String id){
        RealmResults<EventModel> eventModel=mRealm.where(EventModel.class).equalTo("id",id)
                .findAllSorted(new String[]{"year", "month", "day"}, new Sort[]{Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING});
        return mRealm.copyToRealm(eventModel);
    }

    public Realm getRealm(){
        return mRealm;
    }

    public ModelTime isModlueExist(String moduleName){
        return mRealm.where(ModelTime.class).equalTo("moduleName", moduleName).findFirst();
    }

    public void delete(String moduleName){
        ModelTime eventModel = mRealm.where(ModelTime.class).equalTo("moduleName", moduleName).findFirst();
        if(eventModel != null) {
            mRealm.beginTransaction();
            eventModel.deleteFromRealm();
            mRealm.commitTransaction();
        }
    }

    /**
     * add （增）
     */
    public void addModuleTime(final ModelTime modelTime) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(modelTime);
        mRealm.commitTransaction();
    }

    /**
     * update （改）
     */
    public void updateModelTime(String moduleName, String startTime, String endTime) {
        try {
            ModelTime modelTime = mRealm.where(ModelTime.class).equalTo("moduleName", moduleName).findFirst();
            mRealm.beginTransaction();
            if (TimeUtil.DateCompare(modelTime.getEarlyDate(), startTime)) {//
                modelTime.setEarlyDate(startTime);
            }
            if (TimeUtil.DateCompare(endTime, modelTime.getLastDate())) {
                modelTime.setLastDate(endTime);
            }
            mRealm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean compare_hhmmss(LocalDate DATE1, LocalDate DATE2) throws ParseException {
        return DATE1.isAfter(DATE2);
    }

    public void close(){
        if (mRealm!=null){
            mRealm.close();
        }
    }
}

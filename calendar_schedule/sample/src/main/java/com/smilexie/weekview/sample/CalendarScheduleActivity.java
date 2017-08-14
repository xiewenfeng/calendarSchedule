package com.smilexie.weekview.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.smile.calendar.manager.CalendarManager;
import com.smile.calendar.view.CollapseCalendarView;
import com.smilexie.weekview.sample.adapter.ScheduleAdapter;
import com.smilexie.weekview.sample.apiclient.Event;
import com.smilexie.weekview.sample.apiclient.MyJsonService;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * 日程列表形式
 * Created by SmileXie on 2017/5/8.
 */

public class CalendarScheduleActivity extends AppCompatActivity implements Callback<List<Event>> {

    private LocalDate selectedDate;//当前选择的日期
    private boolean weekchanged = false;//是否切换了周号
    private CalendarManager mManager;
    private CollapseCalendarView calendarView;
    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private HashMap<Integer, List<Event>> eventMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        CollapseCalendarView.withMonthSchedule = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_schedule);
        calendarView = (CollapseCalendarView) findViewById(R.id.calendar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleAdapter = new ScheduleAdapter(this);
        recyclerView.setAdapter(scheduleAdapter);
        eventMap = new HashMap<>();
        initCalendarListener();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        loadData();
    }


    private void loadData() {
        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint("https://api.myjson.com/bins")
                .build();
        MyJsonService service = retrofit.create(MyJsonService.class);
        service.listEvents(CalendarScheduleActivity.this);
    }

    private void initCalendarListener() {
        selectedDate = LocalDate.now();
        mManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH, LocalDate.now().withYear(100),
                LocalDate.now().plusYears(60));
        //月份切换监听器
        mManager.setMonthChangeListener(new CalendarManager.OnMonthChangeListener() {

            @Override
            public void monthChange(String month, LocalDate mSelected) {
                setTitle(month);
            }

            @Override
            public void weekChange(String week, LocalDate mSelected) {//周切换

            }
        });
        calendarView.init(mManager);
        /**
         * 日期选中监听器
         */
        calendarView.setDateSelectListener(new CollapseCalendarView.OnDateSelect() {

            @Override
            public void onDateSelected(LocalDate date) {
                selectedDate = date;
                if (eventMap.containsKey(selectedDate.getDayOfMonth())) {
                    scheduleAdapter.setMSchedule(eventMap.get(selectedDate.getDayOfMonth()));
                }
            }
        });

    }

    @Override
    public void success(List<Event> events, Response response) {
        if (events != null) {
            for (int i = 0; i < events.size(); i ++) {
                int key = events.get(i).getDayOfMonth();
                if (!eventMap.containsKey(key)) {
                    eventMap.put(key, new ArrayList<Event>());
                }
                (eventMap.get(key)).add(events.get(i));
            }
            if (eventMap.containsKey(selectedDate.getDayOfMonth())) {
                scheduleAdapter.setMSchedule(eventMap.get(selectedDate.getDayOfMonth()));
            }

        }
    }

    @Override
    public void failure(RetrofitError error) {
        error.printStackTrace();
        Toast.makeText(this, R.string.async_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String json = "[{\"name\":\"Event 1\",\"dayOfMonth\":3,\"startTime\":\"01:00\",\"endTime\":\"02:00\",\"color\":\"#F57F68\"},{\"name\":\"Event 2\",\"dayOfMonth\":3,\"startTime\":\"04:00\",\"endTime\":\"05:00\",\"color\":\"#87D288\"},{\"name\":\"Event 3\",\"dayOfMonth\":3,\"startTime\":\"10:00\",\"endTime\":\"11:00\",\"color\":\"#F8B552\"},{\"name\":\"Event 4\",\"dayOfMonth\":3,\"startTime\":\"15:00\",\"endTime\":\"16:00\",\"color\":\"#59DBE0\"},{\"name\":\"Event 5\",\"dayOfMonth\":4,\"startTime\":\"04:00\",\"endTime\":\"05:00\",\"color\":\"#F57F68\"},{\"name\":\"Event 6\",\"dayOfMonth\":4,\"startTime\":\"07:00\",\"endTime\":\"08:00\",\"color\":\"#87D288\"},{\"name\":\"Event 7\",\"dayOfMonth\":4,\"startTime\":\"14:00\",\"endTime\":\"15:00\",\"color\":\"#F8B552\"},{\"name\":\"Event 8\",\"dayOfMonth\":5,\"startTime\":\"06:00\",\"endTime\":\"07:00\",\"color\":\"#59DBE0\"},{\"name\":\"Event 9\",\"dayOfMonth\":5,\"startTime\":\"11:00\",\"endTime\":\"12:00\",\"color\":\"#F57F68\"},{\"name\":\"Event 10\",\"dayOfMonth\":5,\"startTime\":\"16:00\",\"endTime\":\"17:00\",\"color\":\"#87D288\"},{\"name\":\"Event 11\",\"dayOfMonth\":4,\"startTime\":\"07:00\",\"endTime\":\"09:00\",\"color\":\"#F8B552\"},{\"name\":\"Event 12\",\"dayOfMonth\":4,\"startTime\":\"04:00\",\"endTime\":\"05:00\",\"color\":\"#59DBE0\"},{\"name\":\"Event 13\",\"dayOfMonth\":3,\"startTime\":\"01:00\",\"endTime\":\"02:00\",\"color\":\"#F57F68\"},{\"name\":\"Event 14\",\"dayOfMonth\":3,\"startTime\":\"10:00\",\"endTime\":\"11:00\",\"color\":\"#87D288\"},{\"name\":\"Event 15\",\"dayOfMonth\":4,\"startTime\":\"14:00\",\"endTime\":\"15:00\",\"color\":\"#F8B552\"},{\"name\":\"Event 16\",\"dayOfMonth\":3,\"startTime\":\"20:00\",\"endTime\":\"21:00\",\"color\":\"#59DBE0\"},{\"name\":\"Event 17\",\"dayOfMonth\":4,\"startTime\":\"19:00\",\"endTime\":\"20:00\",\"color\":\"#F57F68\"},{\"name\":\"Event 18\",\"dayOfMonth\":5,\"startTime\":\"22:00\",\"endTime\":\"23:00\",\"color\":\"#87D288\"},{\"name\":\"Event 19\",\"dayOfMonth\":5,\"startTime\":\"11:00\",\"endTime\":\"12:00\",\"color\":\"#F8B552\"},{\"name\":\"Event 20\",\"dayOfMonth\":6,\"startTime\":\"06:00\",\"endTime\":\"07:00\",\"color\":\"#59DBE0\"},{\"name\":\"Event 21\",\"dayOfMonth\":6,\"startTime\":\"09:00\",\"endTime\":\"10:00\",\"color\":\"#F57F68\"},{\"name\":\"Event 22\",\"dayOfMonth\":6,\"startTime\":\"12:00\",\"endTime\":\"13:00\",\"color\":\"#87D288\"},{\"name\":\"Event 23\",\"dayOfMonth\":6,\"startTime\":\"16:00\",\"endTime\":\"17:00\",\"color\":\"#F8B552\"},{\"name\":\"Event 24\",\"dayOfMonth\":7,\"startTime\":\"07:00\",\"endTime\":\"08:00\",\"color\":\"#59DBE0\"},{\"name\":\"Event 25\",\"dayOfMonth\":7,\"startTime\":\"14:00\",\"endTime\":\"16:00\",\"color\":\"#F57F68\"},{\"name\":\"Event 26\",\"dayOfMonth\":7,\"startTime\":\"21:00\",\"endTime\":\"22:00\",\"color\":\"#87D288\"},{\"name\":\"Event 27\",\"dayOfMonth\":8,\"startTime\":\"06:00\",\"endTime\":\"07:00\",\"color\":\"#F8B552\"},{\"name\":\"Event 28\",\"dayOfMonth\":8,\"startTime\":\"12:00\",\"endTime\":\"13:00\",\"color\":\"#59DBE0\"},{\"name\":\"Event 29\",\"dayOfMonth\":8,\"startTime\":\"16:00\",\"endTime\":\"17:00\",\"color\":\"#F57F68\"},{\"name\":\"Event 30\",\"dayOfMonth\":6,\"startTime\":\"06:00\",\"endTime\":\"08:00\",\"color\":\"#87D288\"},{\"name\":\"Event 31\",\"dayOfMonth\":6,\"startTime\":\"12:00\",\"endTime\":\"13:00\",\"color\":\"#F8B552\"},{\"name\":\"Event 32\",\"dayOfMonth\":6,\"startTime\":\"17:00\",\"endTime\":\"18:00\",\"color\":\"#59DBE0\"},{\"name\":\"Event 33\",\"dayOfMonth\":7,\"startTime\":\"21:00\",\"endTime\":\"22:00\",\"color\":\"#F57F68\"},{\"name\":\"Event 34\",\"dayOfMonth\":7,\"startTime\":\"09:00\",\"endTime\":\"10:00\",\"color\":\"#87D288\"},{\"name\":\"Event 35\",\"dayOfMonth\":6,\"startTime\":\"01:00\",\"endTime\":\"03:00\",\"color\":\"#F8B552\"}]";
}

package com.smilexie.weekview.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.smilexie.weekview.sample.adapter.ScheduleAdapter;
import com.smilexie.weekview.sample.apiclient.Event;
import com.smilexie.weekview.sample.apiclient.MyJsonService;
import com.smile.calendar.manager.CalendarManager;
import com.smile.calendar.view.CollapseCalendarView;

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
}

package com.smile.calendar.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import com.smile.calendar.R;
import com.smile.calendar.baseadapter.BaseRecyclerViewAdapter;
import com.smile.calendar.databinding.ActivityCalendarScheduleBinding;
import com.smile.calendar.databinding.ItemScheduleBinding;
import com.smile.calendar.manager.CalendarManager;
import com.smile.calendar.module.EventModel;
import com.smile.calendar.util.RealmHelper;
import com.smile.calendar.view.CollapseCalendarView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 日程列表形式
 * Created by SmileXie on 2017/5/8.
 */

public class CalendarScheduleActivity extends BaseActivity<ActivityCalendarScheduleBinding> {

    private LocalDate selectedDate;//当前选择的日期
    private boolean weekchanged = false;//是否切换了周号
    private CalendarManager mManager;
    private CollapseCalendarView calendarView;
    private RecyclerView recyclerView;
    private BaseRecyclerViewAdapter<EventModel, ItemScheduleBinding> adapter;
    private SparseArray<List<EventModel>> eventMap;
    private HashMap<String, SparseArray<List<EventModel>>> monthMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        CollapseCalendarView.withMonthSchedule = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_schedule);
        calendarView = (CollapseCalendarView) findViewById(R.id.calendar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        monthMap = new HashMap<>();
        initCalendarListener();
        setRightTitle(getString(R.string.add_schedule));
        initAdapter();
        loadData();
    }

    private void initAdapter() {
        adapter = new BaseRecyclerViewAdapter<EventModel, ItemScheduleBinding>(R.layout.item_schedule) {
            @Override
            public void onNewBindViewHolder(EventModel object, int position, ItemScheduleBinding binding) {
                binding.tvScheduleTitle.setText(object.getName() + "-" + object.getF1());
                binding.tvScheduleTime.setText(object.getStartTime() + "~" + object.getEndTime());
            }
        };
        adapter.setOnItemClickListener((view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString("eventId", adapter.getItem(position).getId());
            startActivity(AddDeleteActivity.class, bundle);
        });
        bindingView.recyclerView.setLayoutManager(new LinearLayoutManager(CalendarScheduleActivity.this));
        bindingView.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void rightClick() {
        startActivityForResult(AddDeleteActivity.class, 100);
    }

    private void loadData() {
        eventMap = new SparseArray<>();
        List<EventModel> models = RealmHelper.getRealmHelperInstance().queryEventByMonth(selectedDate.getYear(), selectedDate.getMonthOfYear());
        if (models != null && models.size() > 0) {
            try {
                for (EventModel model : models) {
                    int key = model.getDay();
                    List<EventModel> eventModels = new ArrayList<>();
                    eventModels.add(model);
                    if (eventMap.get(key) == null) {
                        eventMap.put(key, eventModels);
                    } else {
                        eventModels.addAll(eventMap.get(key));
                        eventMap.put(key, eventModels);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            monthMap.put(selectedDate.getYear()+"-" +selectedDate.getMonthOfYear(), eventMap);
        }
        changeDayEvent();
    }

    private void changeDayEvent() {
        if (adapter != null && adapter.getData() != null && adapter.getData().size() > 0) {
            adapter.clear();
        }
        List<EventModel> curretDayEvents = eventMap.get(selectedDate.getDayOfMonth());
        if (curretDayEvents != null && curretDayEvents.size() > 0) {
            adapter.addAll(curretDayEvents);
        }
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
                loadData();
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
                changeDayEvent();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            loadData();
        }
    }
}

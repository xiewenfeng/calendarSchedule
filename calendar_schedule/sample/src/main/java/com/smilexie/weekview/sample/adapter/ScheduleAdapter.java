package com.smilexie.weekview.sample.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smilexie.weekview.sample.R;
import com.smilexie.weekview.sample.apiclient.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xwf on 2017/7/19
 * 日程列表adapter适配器
 */
public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Event> mSchedules;

    public ScheduleAdapter(Context context) {
        mContext = context;
        mSchedules = new ArrayList<>();
    }

    public void setMSchedule(List<Event> events) {
        mSchedules = events;
        if (mSchedules == null || mSchedules.size() == 0) {
            mSchedules = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ScheduleViewHolder) {
            final Event schedule = mSchedules.get(position);
            final ScheduleViewHolder viewHolder = (ScheduleViewHolder) holder;
            viewHolder.tvScheduleTitle.setText(schedule.getName());
            viewHolder.tvScheduleTime.setText(schedule.getStartTime() + "~" + schedule.getEndTime());
            viewHolder.vScheduleHintBlock.setBackgroundColor(Color.parseColor(schedule.getColor()));
        }
    }

    @Override
    public int getItemCount() {
        return mSchedules == null ? 0 : mSchedules.size();
    }

    protected class ScheduleViewHolder extends RecyclerView.ViewHolder {

        protected View vScheduleHintBlock;
        protected TextView tvScheduleState;
        protected TextView tvScheduleTitle;
        protected TextView tvScheduleTime;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            vScheduleHintBlock = itemView.findViewById(R.id.vScheduleHintBlock);
            tvScheduleState = (TextView) itemView.findViewById(R.id.tvScheduleState);
            tvScheduleTitle = (TextView) itemView.findViewById(R.id.tvScheduleTitle);
            tvScheduleTime = (TextView) itemView.findViewById(R.id.tvScheduleTime);
        }

    }

}

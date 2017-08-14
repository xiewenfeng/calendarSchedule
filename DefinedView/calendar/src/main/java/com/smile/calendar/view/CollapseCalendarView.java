package com.smile.calendar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smile.calendar.R;
import com.smile.calendar.manager.CalendarManager;
import com.smile.calendar.manager.ResizeManager;
import com.smile.calendar.module.Day;
import com.smile.calendar.module.EventModel;
import com.smile.calendar.module.Month;
import com.smile.calendar.module.Week;

import org.joda.time.LocalDate;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 日历View
 *
 * @author smileXie
 */
@SuppressLint({"SimpleDateFormat", "NewApi"})
public class CollapseCalendarView extends LinearLayout implements View.OnClickListener {

    public static final String TAG = "CalendarView";
    private CalendarManager mManager;
    private TextView mTitleView;
    private ImageButton mPrev;
    private ImageButton mNext;
    private LinearLayout mWeeksView;
    private final LayoutInflater mInflater;
    private final RecycleBin mRecycleBin = new RecycleBin();
    private OnDateSelect mListener;
    private TextView mSelectionText;
    private LinearLayout mHeader;
    private ResizeManager mResizeManager;
    private ImageView mIvPre;
    private ImageView mIvNext;
    private Animation left_in;
    private Animation right_in;
    private boolean initialized;
    private String[] weeks;
    private OnTitleClickListener titleClickListener;
    private JSONObject dataObject;                            //日历数据源
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private boolean showChinaDay = true;                    //是否显示农历日期
    private String marksMap = "";
    public static boolean withMonthSchedule = true;//月视图是否带日程
    private List<EventModel> eventModels;

    public CollapseCalendarView(Context context) {
        this(context, null);
        onFinishInflate();
    }

    public void setWithMonthSchedule(boolean withSchedule) {
        this.withMonthSchedule = withSchedule;
        showChinaDay = false;
    }

    public void showChinaDay(boolean showChinaDay) {
        this.showChinaDay = showChinaDay;
        populateLayout();
    }

    /**
     * 设置标题点击监听器
     *
     * @param titleClickListener
     */
    public void setTitleClickListener(OnTitleClickListener titleClickListener) {
        this.titleClickListener = titleClickListener;
    }

    public CollapseCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.calendarViewStyle);
    }

    public void setArrayData(JSONObject jsonObject) {
        this.dataObject = jsonObject;
    }

    public interface OnTitleClickListener {
        void onTitleClick();
    }

    @SuppressLint("NewApi")
    public CollapseCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        weeks = getResources().getStringArray(R.array.weeks);
        mInflater = LayoutInflater.from(context);
        mResizeManager = new ResizeManager(this, !withMonthSchedule);
        int resourceLayout = withMonthSchedule ? R.layout.calendar_month_layout_with_schedule : R.layout.calendar_month_layout;
        inflate(context, resourceLayout, this);
        setOrientation(VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.activity_bg_color));
        mIvPre = new ImageView(getContext());
        mIvNext = new ImageView(getContext());
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
        mIvPre.setLayoutParams(param);
        mIvNext.setLayoutParams(param);
        initAnim();
    }

    /**
     * 初始化左右滑动动画
     */
    private void initAnim() {
        left_in = AnimationUtils.makeInAnimation(getContext(), true);
        right_in = AnimationUtils.makeInAnimation(getContext(), false);
    }

    /**
     * 初始化日历管理器
     *
     * @param manager 日历管理器对象
     */
    public void init(CalendarManager manager) {
        if (manager != null) {
            mManager = manager;
            if (mListener != null) {
                mListener.onDateSelected(mManager.getSelectedDay());
            }
            populateLayout();

        }
    }

    /**
     * 切换到指定某天界面
     *
     * @param date yyyy-MM-dd
     */
    public void changeDate(String date) {
        if (date.compareTo(mManager.getSelectedDay().toString()) > 0) {
            this.setAnimation(right_in);
            right_in.start();
        } else if (date.compareTo(mManager.getSelectedDay().toString()) < 0) {
            this.setAnimation(left_in);
            left_in.start();
        }
        try {
            mManager.init(LocalDate.fromDateFields(sdf.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        init(mManager);
    }


    public CalendarManager getManager() {
        return mManager;
    }

    @Override
    public void onClick(View v) {
    /*	if (mManager != null) {
            int id = v.getId();
			if (id == R.id.prev) {
				prev();
			} else if (id == R.id.next) {
				next();
			} else if (id == R.id.title) {
				if (titleClickListener != null) {
					titleClickListener.onTitleClick();
				}
			}
		}*/
    }

    @SuppressLint("WrongCall")
    @Override
    protected void dispatchDraw(Canvas canvas) {
        mResizeManager.onDraw();
        super.dispatchDraw(canvas);
    }


    public CalendarManager.State getState() {
        if (mManager != null) {
            return mManager.getState();
        } else {
            return null;
        }
    }

    public void setDateSelectListener(OnDateSelect listener) {
        mListener = listener;
    }

    /**
     * 设置日历标题
     *
     * @param text
     */
    public void setTitle(String text) {
        if (TextUtils.isEmpty(text)) {
            mHeader.setVisibility(View.VISIBLE);
            mSelectionText.setVisibility(View.GONE);
        } else {
            mHeader.setVisibility(View.GONE);
            mSelectionText.setVisibility(View.VISIBLE);
            mSelectionText.setText(text);
        }
    }

    /**
     * 显示日历自带标题
     */
    public void showHeader() {
        mHeader.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏日历自带标题
     */
    public void hideHeader() {
        mHeader.setVisibility(View.GONE);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mResizeManager.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        super.onTouchEvent(event);
        return mResizeManager.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        解决日历和pager左右滑动的冲突
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 周-月
     */
    public void weekToMonth() {
        if (mManager.getState() == CalendarManager.State.WEEK) {
            mManager.toggleView();
        }
    }

    /**
     * 月-周
     */
    public void monthToWeek() {
        if (mManager.getState() == CalendarManager.State.MONTH) {
            mManager.toggleView();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setOnClickListener(this);
        mPrev = (ImageButton) findViewById(R.id.prev);
        mNext = (ImageButton) findViewById(R.id.next);
        mWeeksView = (LinearLayout) findViewById(R.id.weeks);
        mHeader = (LinearLayout) findViewById(R.id.header);
        mSelectionText = (TextView) findViewById(R.id.selection_title);
        mPrev.setOnClickListener(this);
        mNext.setOnClickListener(this);
        populateLayout();
    }

    /**
     * 绘制日历周标题
     */
    private void populateDays() {
        if (!initialized) {
            CalendarManager manager = getManager();
            if (manager != null) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.days);
                for (int i = 0; i < 7; i++) {
                    TextView textView = (TextView) layout.getChildAt(i);
                    textView.setText(weeks[i]);
                    if (i > 4) {
                        textView.setTextColor(getResources().getColor(R.color.cal_blue_dark));
                    }
                }
                initialized = true;
            }
        }
    }

    /**
     * 刷新日历View
     */
    public synchronized void populateLayout() {
        if (mManager != null) {
            populateDays();
            if (mPrev != null) {
                mPrev.setEnabled(mManager.hasPrev());
                mNext.setEnabled(mManager.hasNext());
                mTitleView.setText(mManager.getHeaderText());
                if (mManager.getState() == CalendarManager.State.MONTH) {
                    populateMonthLayout((Month) mManager.getUnits());
                } else {
                    populateWeekLayout((Week) mManager.getUnits());
                    mManager.weekChanged();
                }
            }
        }
    }

    /**
     * 刷新日历Month View
     *
     * @param month 月份
     */
    private void populateMonthLayout(Month month) {
        List<Week> weeks = month.getWeeks();
        int cnt = weeks.size();
        for (int i = 0; i < cnt; i++) {
            populateWeekLayout(weeks.get(i), withMonthSchedule ? getWeekViewWithSchedule(i) : getWeekView(i));
        }
        int childCnt = mWeeksView.getChildCount();
        if (cnt < childCnt) {
            for (int i = cnt; i < childCnt; i++) {
                cacheView(i);
            }
        }
    }

    /**
     * 刷新日历Week View
     *
     * @param week 周
     */
    private void populateWeekLayout(Week week) {
        WeekView weekView = getWeekView(0);
        populateWeekLayout(week, weekView);

        int cnt = mWeeksView.getChildCount();
        if (cnt > 1) {
            for (int i = cnt - 1; i > 0; i--) {
                cacheView(i);
            }
        }
    }

    /**
     * 月画周 周画天
     */
    private void populateWeekLayout(Week week, ViewGroup weekView) {

        List<Day> days = week.getDays();
        for (int i = 0; i < 7; i++) {
            final Day day = days.get(i);
            LinearLayout layout = (LinearLayout) weekView.getChildAt(i);
            DayView dayView = (DayView) layout.findViewById(R.id.tvDayView);
            DayView chinaDay = (DayView) layout.findViewById(R.id.tvChina);
            if (showChinaDay) {
                chinaDay.setVisibility(View.VISIBLE);
            } else {
                chinaDay.setVisibility(View.GONE);
            }
            View viewPoint = layout.findViewById(R.id.view_point);
            TextView tvDayType = (TextView) layout.findViewById(R.id.tv_day_type);
            // 显示小蓝点标记
            if (marksMap != null && marksMap != "") {
                if (marksMap.contains(day.getDate().toString())) {
                    viewPoint.setVisibility(View.VISIBLE);
                } else {
                    viewPoint.setVisibility(View.INVISIBLE);
                }

            }
            tvDayType.setVisibility(View.INVISIBLE);
            dayView.setText(day.getText());
            if (day.getDate().getYear() == mManager.getCurrentMonthDate().getYear()
                    && day.getDate().getMonthOfYear() == mManager.getCurrentMonthDate().getMonthOfYear()
                    || mManager.getState() == CalendarManager.State.WEEK) {
                //如果日期是当前月份
                if (i > 4) {
                    //周末
                    dayView.setTextColor(getResources().getColor(R.color.text));
                } else {
                    //非周末
                    dayView.setTextColor(getResources().getColor(R.color.cal_text_normal));
                }
            } else {
                //日期不是当前月份
                if (i > 4) {
                    //周末
                    dayView.setTextColor(getResources().getColor(R.color.cal_line_grey));
                } else {
                    //非周末
                    dayView.setTextColor(getResources().getColor(R.color.cal_text_light));
                }
            }
            layout.setSelected(day.isSelected());

            if (day.getDate().equals(mManager.getToday()) && day.isSelected()) {
                //日期对象是今天，并且被选中
                if (day.getDate().getYear() == mManager.getCurrentMonthDate().getYear()
                        && day.getDate().getMonthOfYear() == mManager.getCurrentMonthDate().getMonthOfYear()) {
                    //如果日期是当前月份
                    if (i > 4) {
                        //周末
                        dayView.setTextColor(getResources().getColorStateList(R.color.text_calendar_week));
                    } else {
                        //非周末
                        dayView.setTextColor(getResources().getColorStateList(R.color.text_calendar));
                    }
                } else {
                    //日期不是当前月份
                    if (i > 4) {
                        //周末
                        dayView.setTextColor(getResources().getColorStateList(R.color.text_calendar_week_out_month));
                    } else {
                        //非周末
                        dayView.setTextColor(getResources().getColorStateList(R.color.text_calendar_out_month));
                    }
                }
                layout.setBackground(getResources().getDrawable(R.drawable.bg_btn_calendar_today_selected));
                viewPoint.setBackground(getResources().getDrawable(R.drawable.bg_calendar_point_white));
                layout.setSelected(true);
            } else if (day.getDate().equals(mManager.getToday())) {
                //日期对象今天
                viewPoint.setBackground(getResources().getDrawable(R.drawable.bg_calendar_point_blue));
                layout.setBackground(getResources().getDrawable(R.drawable.bg_btn_calendar_today));
                layout.setSelected(true);
            } else {
                //日期对象被选中
                viewPoint.setBackground(getResources().getDrawable(R.drawable.bg_calendar_point_blue));
                layout.setBackground(getResources().getDrawable(R.drawable.bg_btn_calendar));
            }
            dayView.setCurrent(day.isCurrent());
            boolean enables = day.isEnabled();
            dayView.setEnabled(enables);
            if (enables) { // 解除点击限制，所有的都可以点击
                layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalDate date = day.getDate();
                        if (mManager.getState() == CalendarManager.State.MONTH) {
                            //判断当前视图状态为月份视图
                            if (date.getYear() > mManager.getCurrentMonthDate().getYear()) {
                                //选中日期年份大于当前显示年份
                                next();
                            } else if (date.getYear() < mManager.getCurrentMonthDate().getYear()) {
                                //选中日期年份小于当前显示年份
                                prev();
                            } else {
                                //选中日期年份等于当前显示年份
                                if (date.getMonthOfYear() > mManager.getCurrentMonthDate().getMonthOfYear()) {
                                    //选中月份大于当前月份
                                    next();
                                } else if (date.getMonthOfYear() < mManager.getCurrentMonthDate().getMonthOfYear()) {
                                    //选中月份小于当前月份
                                    prev();
                                }
                            }
                        }

                        if (mManager.selectDay(date)) {
                            populateLayout();
                            if (mListener != null) {
                                //执行选中回调
                                mListener.onDateSelected(date);
                            }
                        }
                    }
                });
            } else {
                dayView.setOnClickListener(null);
            }
        }

    }

    /**
     * 翻转到前一页
     */
    public void prev() {
        if (mManager.prev()) {
            populateLayout();
        }
        if (mListener != null) {
            //执行选中回调
            mListener.onDateSelected(mManager.getSelectedDay());
        }
        this.setAnimation(left_in);
        left_in.start();
    }

    /**
     * 翻转到下一页
     */
    public void next() {
        if (mManager.next()) {
            populateLayout();
        }
        if (mListener != null) {
            //执行选中回调
            mListener.onDateSelected(mManager.getSelectedDay());
        }
        this.setAnimation(right_in);
        right_in.start();
    }


    public LinearLayout getWeeksView() {
        return mWeeksView;
    }


    private WeekView getWeekView(int index) {
        int cnt = mWeeksView.getChildCount();

        if (cnt < index + 1) {
            for (int i = cnt; i < index + 1; i++) {
                View view = getView();
                mWeeksView.addView(view);
            }
        }
        return (WeekView) mWeeksView.getChildAt(index);
    }

    private WeekViewWithSchedule getWeekViewWithSchedule(int index) {
        int cnt = mWeeksView.getChildCount();

        if (cnt < index + 1) {
            for (int i = cnt; i < index + 1; i++) {
                View view = getView();
                mWeeksView.addView(view);
            }
        }
        return (WeekViewWithSchedule) mWeeksView.getChildAt(index);
    }

    private View getView() {
        View view = mRecycleBin.recycleView();
        if (view == null) {
            int resourceLayout = withMonthSchedule ? R.layout.calendar_week_layout_with_scehdule : R.layout.calendar_week_layout;
            view = mInflater.inflate(resourceLayout, this, false);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void cacheView(int index) {
        View view = mWeeksView.getChildAt(index);
        if (view != null) {
            mWeeksView.removeViewAt(index);
            mRecycleBin.addView(view);
        }
    }

    public LocalDate getSelectedDate() {
        return mManager.getSelectedDay();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mResizeManager.recycle();
    }

    private class RecycleBin {
        private final Queue<View> mViews = new LinkedList<View>();

        public View recycleView() {
            return mViews.poll();
        }

        public void addView(View view) {
            mViews.add(view);
        }
    }

    public interface OnDateSelect {
        void onDateSelected(LocalDate date);
    }


    /**
     * 在日历上做一组标记
     *
     * @param dateString 日期
     */
    public void addMarks(String dateString) {
        marksMap = dateString;
//        刷新数据
        populateLayout();
    }

    /**
     * 在日历上做一组标记
     *
     */
    public void clearMarks() {
        marksMap = "";
//        刷新数据
        populateLayout();
    }

    public void setEvent(List<EventModel> eventModels) {
        this.eventModels = eventModels;
        populateLayout();
    }

}

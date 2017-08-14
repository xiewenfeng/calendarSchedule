package com.smile.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Blaz Solar on 24/02/14.
 */
public class WeekViewWithSchedule extends ViewGroup {

    private int extraPX;

    public WeekViewWithSchedule(Context context, AttributeSet attrs) {
        super(context, attrs);
        extraPX = px2dip(context, 30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int maxSize = widthSize / 7;
        int baseSize = 0;

        int cnt = getChildCount();
        for (int i = 0; i < cnt; i++) {

            View child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            child.measure(
                    MeasureSpec.makeMeasureSpec(maxSize, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(maxSize, MeasureSpec.AT_MOST)
            );

            baseSize = Math.max(baseSize, child.getMeasuredHeight());

        }

        for (int i = 0; i < cnt; i++) {

            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            child.measure(
                    MeasureSpec.makeMeasureSpec(baseSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(baseSize, MeasureSpec.EXACTLY)
            );

        }

        setMeasuredDimension(widthSize, getLayoutParams().height >= 0 ? getLayoutParams().height + extraPX : baseSize + getPaddingBottom() + getPaddingTop() + extraPX);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int cnt = getChildCount();

        int width = getMeasuredWidth();
        int part = width / cnt;

        for (int i = 0; i < cnt; i++) {

            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            int childWidth = child.getMeasuredWidth();

            int x = i * part + ((part - childWidth) / 2);
            child.layout(x, 0, x + childWidth, child.getMeasuredHeight() + extraPX);

        }

    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }
}

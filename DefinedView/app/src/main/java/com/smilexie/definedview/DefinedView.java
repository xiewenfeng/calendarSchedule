package com.smilexie.definedview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by SmileXie on 2017/8/11.
 */

public class DefinedView extends View {
    private TextPaint paint;
    private Paint mPaint;

    public DefinedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new TextPaint();
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        mPaint.setTypeface(Typeface.SERIF);

        String text = "Hello world\naaa bb \ncc";
        paint.setTextSize(40);
        paint.setFakeBoldText(true);
        StaticLayout staticLayout = new StaticLayout(text, paint, 600, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        canvas.save();
        canvas.translate(50, 100);
        staticLayout.draw(canvas);
        canvas.translate(0, 200);
        staticLayout.draw(canvas);
        canvas.restore();
//        canvas.drawText(text, 200, 100, paint);
    }
}

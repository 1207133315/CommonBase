package com.liningkang.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DottedLineView extends View {
    private Paint mPaint;

    public DottedLineView(Context context) {
        super(context);
        init();
    }

    public DottedLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DottedLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#E8E8E8"));
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(getHeight());
        // 绘制虚线
        canvas.drawLine(0, getHeight() / 2, getMeasuredWidth(), getHeight() / 2, mPaint);
    }
}


package com.liningkang.ui.view.button;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liningkang.ui.R;
import com.liningkang.ui.view.BoldTextView;

public class LoadingButton extends BoldTextView {
    private Matrix matrix;
    private ObjectAnimator animator;
    private int STATE_NORMAL = 0;
    private int STATE_LOADING = 1;
    private int STATE_COMPLETED = 2;
    private int state = STATE_NORMAL;
    private int mViewWidth;
    private int mViewHeight;
    private Bitmap bitmap;
    private String mText;

    public LoadingButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LoadingButton(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingButton(@NonNull Context context) {
        super(context, null, 0);
        init();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loading);
        if (!TextUtils.isEmpty(getText())){
            mText = getText().toString();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (!TextUtils.isEmpty(text)){
            mText = getText().toString();
        }
    }
    /*   @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_UP:
                startAnim();
                state = STATE_LOADING;
                postInvalidate();
                setClickable(false);
                setText("");
                break;
        }
        return true;
    }*/

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (state == STATE_LOADING) {
            canvas.translate(mViewWidth / 2, mViewHeight / 2);// 旋转的位置
            matrix.preTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);//旋转中心点
            canvas.drawBitmap(bitmap, matrix, null);
        }
    }

    public void startLoading() {
        startAnim();
        state = STATE_LOADING;
        postInvalidate();

    }

    @SuppressLint("ObjectAnimatorBinding")
    private void initAnimator() {
        matrix = new Matrix();
        animator = ObjectAnimator.ofFloat(matrix, "rotation", 0, 360);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                matrix.setRotate((float) animation.getAnimatedValue());
                invalidate();
            }
        });
    }

    /**
     * 方法描述：开启动画
     */
    private void startAnim() {
        if (animator == null) {
            initAnimator();
            animator.start();
            return;
        }
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    /**
     * 方法描述：取消动画
     */
    private void cancelAnim() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    /**
     * 方法描述：加载完成以后让SimpleLoadingButton复位
     */
    public void setCompleted() {
        state = STATE_COMPLETED;
        cancelAnim();
      //  setText(mText);
        postInvalidate();
    }

}

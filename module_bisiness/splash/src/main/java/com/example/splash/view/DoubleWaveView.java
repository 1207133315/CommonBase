package com.example.splash.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.splash.R;
import com.liningkang.utils.LogUtils;

public class DoubleWaveView extends View {

/*    波浪高度,范围 0-100
    app:dw_base_line="40"
    背景颜色
    app:dw_bg_color="#666666"
    圆形遮罩
    app:dw_circle="false"
    速度,越大越慢
    app:dw_speed="0.8"
    透明度,范围 0-255
    app:dw_wave_alpha="120"
    波浪颜色
    app:dw_wave_color="#ffffff"
    浪高
    app:dw_wave_level="20"*/
    /* 宽度 */
    private int width;
    /* 高度 */
    private int height;
    /* 画笔 */
    private Paint paint;
    /* 基线 */
    private int base;
    /* 浪高 */
    private int level;
    /* 第一个波浪偏移 */
    private int after;
    /* 第二个波浪偏移 */
    private int before;
    /* 背景色 */
    private int bgColor;
    /* 波浪颜色 */
    private int waveColor;
    /* 波浪透明度 */
    private int alpha;
    /* 波浪速度 */
    private int speed;
    /* 第一个波浪动画 */
    private ValueAnimator afterAnim;
    /* 第二个波浪动画 */
    private ValueAnimator beforeAnim;
    /* 波浪 */
    private Bitmap bitmap;
    /* 默认混合模式 */
    private PorterDuffXfermode srcMode;
    /* 裁剪混合模式 */
    private PorterDuffXfermode decMode;
    /* 圆形遮罩 */
    private boolean circle;

    public DoubleWaveView(Context context) {
        super(context);
        init();
    }

    public DoubleWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DoubleWaveView);
        base = array.getInt(R.styleable.DoubleWaveView_dw_base_line, 40);
        level = dp2px(array.getInt(R.styleable.DoubleWaveView_dw_wave_level, 40));
        bgColor = array.getColor(R.styleable.DoubleWaveView_dw_bg_color, 0xff666666);
        waveColor = array.getColor(R.styleable.DoubleWaveView_dw_wave_color, 0xffffffff);
        alpha = array.getInt(R.styleable.DoubleWaveView_dw_wave_alpha, 120);
        speed = (int) (1000 * array.getFloat(R.styleable.DoubleWaveView_dw_speed, 0.8f));
        circle = array.getBoolean(R.styleable.DoubleWaveView_dw_circle, true);
        array.recycle();
        init();
    }

    public DoubleWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DoubleWaveView);
        base = array.getInt(R.styleable.DoubleWaveView_dw_base_line, 40);
        level = dp2px(array.getInt(R.styleable.DoubleWaveView_dw_wave_level, 40));
        bgColor = array.getColor(R.styleable.DoubleWaveView_dw_bg_color, 0xff666666);
        waveColor = array.getColor(R.styleable.DoubleWaveView_dw_wave_color, 0xffffffff);
        alpha = array.getInt(R.styleable.DoubleWaveView_dw_wave_alpha, 120);
        speed = (int) (1000 * array.getFloat(R.styleable.DoubleWaveView_dw_speed, 0.8f));
        circle = array.getBoolean(R.styleable.DoubleWaveView_dw_circle, true);
        array.recycle();

        init();
    }

    private View indicatorView;
    private float indicatorViewW;
    private float indicatorViewH;
    private float centerX;
    private float indicatorX;
    private float indicatorY;
    private Bitmap indicatorBitmap;


    /**
     * 初始化
     */
    private void init() {

        /*indicatorView = View.inflate(getContext(), R.layout.wave_indicator_layout, null);
        indicatorViewH=indicatorView.getHeight();
        indicatorViewW=indicatorView.getWidth();*/

        Glide.with(getContext()).asBitmap()
                .load(com.liningkang.ui.R.drawable.logo)
                .apply(new RequestOptions().override(70,70))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        indicatorBitmap = resource;
                        indicatorViewH = indicatorBitmap.getHeight();
                        indicatorViewW = indicatorBitmap.getWidth();
                        invalidate();
                    }
                });


        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(bgColor);
        if (circle) {
            srcMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);
            decMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        }
    }

    private Path path = new Path();


    public void setWaveHeight(int height) {
        if (height < 0) {
            height = 0;
        } else if (height > 100) {
            height = 100;
        }
        this.base = height;
        invalidate();
    }

    public int getWaveHeight() {
        return base;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        width = getWidth();
        height = getHeight();

        if (afterAnim == null || beforeAnim == null) {
            initAnim();
        }
        if (bitmap == null) {
            makeWave();
        }
        /* 绘制形状 */
        if (circle) {
            paint.setXfermode(decMode);
            paint.setAlpha(255);
// 定义梯形的四个顶点坐标
            float leftTopX = 0f;
            float leftTopY = 0f;
            float rightTopX = width;
            float rightTopY = 0f;
            float leftBottomX = width * 0.1f;// 调整左下角的横坐标
            float leftBottomY = height;
            float rightBottomX = width * 0.9f; // 调整右下角的横坐标
            float rightBottomY = height;
            // 重置路径
            path.reset();
            float cornerRadius = 70f; // 圆角半径
            // 移动到左上角
            path.moveTo(leftTopX + cornerRadius, leftTopY);

            // 添加直线到右上角
            path.lineTo(rightTopX - cornerRadius, rightTopY);
            // 添加右上角的圆角
            RectF arcRectTopRight = new RectF(rightTopX - 2 * cornerRadius, 0, rightTopX, 2 * cornerRadius);
            path.arcTo(arcRectTopRight, -90, 90);
            // 添加右下角的圆角
            RectF arcRectBottomRight = new RectF(rightBottomX - 2 * cornerRadius, rightBottomY - 2 * cornerRadius, rightBottomX, rightBottomY);
            path.arcTo(arcRectBottomRight, 0, 90);
            // 添加直线到左下角
            path.lineTo(leftBottomX, leftBottomY);
            // 添加左下角的圆角
            RectF arcRectBottomLeft = new RectF(leftBottomX, leftBottomY - 2 * cornerRadius, leftBottomX + 2 * cornerRadius, leftBottomY);
            path.arcTo(arcRectBottomLeft, 90, 90);
            // 添加直线到左上角
            path.lineTo(leftTopX, leftTopY + cornerRadius);
            // 添加直线到右下角
            path.lineTo(rightBottomX, rightBottomY);
            // 添加左上角的圆角
            RectF arcRectTopLeft = new RectF(leftTopX, leftTopY, leftTopX + 2 * cornerRadius, leftTopY + 2 * cornerRadius);
            path.arcTo(arcRectTopLeft, 180, 90);
            // 闭合路径
            path.close();


            // 在Canvas上剪切出梯形的区域
            //  canvas.clipPath(path);

            canvas.drawPath(path, paint);

            //canvas.drawCircle(width / 2, height / 2, width / 2, paint);
            paint.setXfermode(srcMode);
        }

        /* 绘制第一个波浪 */
        paint.setAlpha(alpha);
        int baseLine = height - (height / 100 * (base + 10));
        canvas.drawBitmap(bitmap, after, baseLine, paint);
        canvas.drawBitmap(bitmap, (-width + after), baseLine, paint);

        /* 绘制第二个波浪 */
        paint.setAlpha((int) (alpha * 1.5f));
        canvas.drawBitmap(bitmap, (before + (width / 3)), baseLine, paint);
        canvas.drawBitmap(bitmap, (-width + before + (width / 3)), baseLine, paint);
        canvas.drawBitmap(bitmap, (-(width * 2) + before + (width / 3)), baseLine, paint);

        if (circle) {
            /* 绘制混合遮罩 */
            paint.setAlpha(255);
            canvas.drawRect(0, 0, width, height, paint);
        }

        super.onDraw(canvas);
        if (indicatorBitmap != null) {
            drawIndicator(canvas);
        }

        invalidate();

        /* 解决Android 8.0 锁屏解锁后动画休眠的问题 */
        if (!afterAnim.isRunning()) {
            afterAnim.start();
        }
        if (!beforeAnim.isRunning()) {
            beforeAnim.start();
        }
    }
    private Paint indicatorPaint=new Paint();
    private void drawIndicator(Canvas canvas) {
        centerX = width / 2;
        // 初始化内容 View 的位置为垂直居中
        if (indicatorX == 0 && indicatorY == 0) {
            indicatorX = centerX - (indicatorViewW / 2);
            // indicatorY = (height - indicatorViewH) / 2;
            indicatorY = height - (height / 100 * base + indicatorViewH );
        }

        canvas.drawBitmap(indicatorBitmap, indicatorX, indicatorY, indicatorPaint);
    }


    private float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下时的位置
                LogUtils.INSTANCE.d("waveView", "手指按下");
                break;

            case MotionEvent.ACTION_MOVE:
                LogUtils.INSTANCE.d("waveView", "手指移动");
                // 计算手指在 Y 轴上的移动距离
                float deltaY = event.getY() - lastY;

                // 更新内容 View 的位置
                indicatorY += deltaY;
                // 计算图片底部的位置
              //  int imageBottom = (int) (indicatorY + indicatorViewH);
                // 限制内容 View 只能在垂直方向上移动，并保持在 View 内部
                if (indicatorY < 0) {
                    indicatorY = 0;
                } else if (indicatorY > getHeight() - indicatorViewH) {
                    indicatorY = getHeight() - indicatorViewH;
                }
                base = 100 - (int) (100 * indicatorY / (height - indicatorViewH));
                // 计算最大允许的图片底部位置
                if (deltaY > 0) {
                    // 手指向下移动
                } else if (deltaY < 0) {
                    // 手指向上移动
                }

                Log.i("base", "onTouchEvent: base的值:"+base);

                // 重绘 View
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                // 手指抬起时的处理
                break;
        }

        // 记录上一次的触摸位置
        lastY = event.getY();
        return true;
    }

    /**
     * 创建波浪效果
     */
    private void makeWave() {
        Paint paint = new Paint();
        paint.setColor(waveColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        Path path = new Path();
        path.moveTo(0, level);
        path.cubicTo((width / 2.5f), 0, (width - width / 2.5f), (level + level), width, level);
        path.close();
        bitmap = Bitmap.createBitmap(width, height + height / 2, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvas.drawRect(0, level, width, height + height / 2, paint);
        canvas.save();
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        beforeAnim = ValueAnimator.ofInt(0, width);
        beforeAnim.addUpdateListener(animation -> before = (int) animation.getAnimatedValue());
        beforeAnim.setRepeatMode(ValueAnimator.RESTART);
        beforeAnim.setRepeatCount(-1);
        beforeAnim.setInterpolator(new LinearInterpolator());
        beforeAnim.setDuration(speed);
        beforeAnim.start();

        afterAnim = ValueAnimator.ofInt(width, 0);
        afterAnim.addUpdateListener(animation -> after = (int) animation.getAnimatedValue());
        afterAnim.setRepeatMode(ValueAnimator.RESTART);
        afterAnim.setRepeatCount(-1);
        afterAnim.setInterpolator(new LinearInterpolator());
        afterAnim.setDuration((long) (speed * 1.5f));
        afterAnim.start();
    }

    /**
     * pd转px
     *
     * @param dp dp值
     * @return 转换后的px值
     */
    private int dp2px(float dp) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}

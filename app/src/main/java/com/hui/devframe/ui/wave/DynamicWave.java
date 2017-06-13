package com.hui.devframe.ui.wave;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import hui.devframe.R;
import com.hui.devframe.util.ScreenUtil;

public class DynamicWave extends View {

    // 波纹颜色
    private static final int WAVE_PAINT_ONE_COLOR = Color.parseColor("#66c1fc");
    private static final int WAVE_PAINT_SECOND_COLOR = Color.parseColor("#32adfa");
    // y = Asin(wx+b)+h
    private float mAHeight;
    private static final int OFFSET_Y = 0;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 7;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 5;

    private float mCycleFactorW;

    private float mCurRatio = 0;
    private float mSetRatio = 0;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOneOffset;
    private int mXTwoOffset;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;

    public DynamicWave(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.wave);

        mAHeight = a.getDimension(R.styleable.wave_dynamicHeight, 20.0f);
        a.recycle();

        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = ScreenUtil.dp2px(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = ScreenUtil.dp2px(context, TRANSLATE_X_SPEED_TWO);

        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Style.FILL);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        //canvas.setDrawFilter(mDrawFilter);
        resetPositonY();
        for (int i = 0; i < mTotalWidth; i++) {

            // 减400只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
            // 绘制第一条水波纹
            mWavePaint.setColor(WAVE_PAINT_ONE_COLOR);

            float startY = (mTotalHeight - mAHeight) * (1 - mCurRatio) + mAHeight;

            canvas.drawLine(i, startY - mResetOneYPositions[i], i,
                    mTotalHeight,
                    mWavePaint);

            // 绘制第二条水波纹
            mWavePaint.setColor(WAVE_PAINT_SECOND_COLOR);
            canvas.drawLine(i, startY - mResetTwoYPositions[i], i,
                    mTotalHeight,
                    mWavePaint);
        }

        // 改变两条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;

        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }

        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        postDelayed(run, 0);
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0,
                yTwoInterval);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);
    }

    private float mAddI;
    private int mTime;

    public void updateWave(float ratio, float mI, int time) {
        mSetRatio = ratio;
        mAddI = mI;
        mCurRatio = 0;
        mTime = time;
        addRatio();
    }

    private void addRatio() {
        if (mCurRatio < mSetRatio) {
            mCurRatio += mAddI;
            if (mCurRatio > 1.0) {
                mCurRatio = 1.0f;
            }
            postInvalidate();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    addRatio();
                }
            }, mTime);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        mTotalHeight = h;
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[mTotalWidth];

        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);

        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (mAHeight * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }
}

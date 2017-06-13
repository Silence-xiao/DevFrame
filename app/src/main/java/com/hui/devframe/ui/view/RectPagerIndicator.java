package com.hui.devframe.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import hui.devframe.R;

/**
 * 指示器
 * Created by wanghui on 16/5/31.
 */
public class RectPagerIndicator extends View {
    private static final int DEFAULT_FORE_COLOR = 0xffffffff;

    private Context context;
    private int number = 2;
    private int fore_color;

    private Paint mForePaint;
    private int mPos = 0;
    private float mOffset = 0;

    public RectPagerIndicator(Context context) {
        this(context, null);
    }

    public RectPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
        intPaint();
    }

    private void init(AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.rect_indicator);
        fore_color = a.getColor(R.styleable.rect_indicator_rect_fore, DEFAULT_FORE_COLOR);
        a.recycle();
    }

    private void intPaint() {
        mForePaint = new Paint();
        mForePaint.setStyle(Paint.Style.FILL);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(fore_color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int single = width / number;
        int start = (int) (single * mPos + single * mOffset * 1.0);
        int end = start + single;
        canvas.drawRect(start, 0, end, height, mForePaint);
    }

    /**
     * 设置对应ViewPager，如果不调用则不显示
     */
    public void setViewPager(ViewPager pager) {
        if (pager != null && pager.getAdapter() != null && pager.getAdapter().getCount() > 0) {
            this.number = pager.getAdapter().getCount();
            invalidate();

            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    setOffset(position, positionOffset);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    /**
     * 设置前景小圆点位置
     *
     * @param position       当前位置
     * @param positionOffset 当前偏移的百分比
     */
    public void setOffset(int position, float positionOffset) {
        this.mPos = position;
        this.mOffset = positionOffset;
        postInvalidate();
    }
}

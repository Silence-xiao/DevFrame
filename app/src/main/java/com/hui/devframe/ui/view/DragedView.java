package com.hui.devframe.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import com.hui.devframe.util.ScreenUtil;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * 指示器
 * Created by wanghui on 16/5/31.
 */
public class DragedView extends View {
    private Paint mPaint;
    private Scroller mScroller;
    private float mLastX, mLastY, mDx, mDy;

    public DragedView(Context context) {
        this(context, null);
    }

    public DragedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        mScroller = new Scroller(context);
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                mDx = mDy = 0;
                break;
            case ACTION_MOVE:
                mDx = event.getX() - mLastX;
                mDy = event.getY() - mLastY;
                mLastX = event.getX();
                mLastY = event.getY();
                scrollBy((int) -mDx, (int) -mDy);
                break;
            case ACTION_UP:
                break;
            case ACTION_CANCEL:
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int size = ScreenUtil.dp2px(50);
        int posStartX = (width - size) / 2;
        int posStartY = (height - size) / 2;
        canvas.drawRect(posStartX, posStartY, posStartX + size, posStartY + size, mPaint);
    }
}

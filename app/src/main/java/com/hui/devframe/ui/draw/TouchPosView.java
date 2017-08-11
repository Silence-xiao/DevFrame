package com.hui.devframe.ui.draw;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.hui.devframe.util.DialogUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wanghui on 2017/8/11.
 */

public class TouchPosView extends View implements View.OnTouchListener {

    private static int ORI_NONE = 0;
    private static int ORI_HORIZONTAL = 1;
    private static int ORI_VERTICAL = 2;

    // touch
    private int mOrient;
    private float mStartPosX, mStartPosY, mLastPosX, mLastPosY;
    private boolean mIsClick;

    AtomicInteger touchCount = new AtomicInteger(0);
    private int multiTouchInterval = 300;
    private Runnable clickRunnable = null;
    private long mLastTouchTime;

    private Context mContext;
    private Handler mHandler;
    float mTouchSlop;

    public TouchPosView(Context context) {
        this(context, null);
    }

    public TouchPosView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        mHandler = new Handler();
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 初始化数据
                mStartPosX = x;
                mStartPosY = y;
                mLastPosX = x;
                mLastPosY = y;
                mOrient = ORI_NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = x - mLastPosX;
                float deltaY = y - mLastPosY;
                float absDeltaX = Math.abs(deltaX);
                float absDeltaY = Math.abs(deltaY);

                // 根据差值设置方向
                if (absDeltaX > absDeltaY) {
                    if (absDeltaX > mTouchSlop) {
                        setOrientation(ORI_HORIZONTAL);
                    } else {
                        return true;
                    }
                } else {
                    if (absDeltaY > mTouchSlop) {
                        setOrientation(ORI_VERTICAL);
                    } else {
                        return true;
                    }
                }

                // 根据方向操作
                if (mOrient == ORI_HORIZONTAL) {
                    if (deltaX > 0) {
                        forward(absDeltaX);
                    } else {
                        backward(absDeltaX);
                    }
                } else if (mOrient == ORI_VERTICAL) {
                    if (deltaY > 0) {
                        moveUp(absDeltaY);
                    } else {
                        moveDown(absDeltaY);
                    }
                }

                // 更新数据
                mLastPosX = x;
                mLastPosY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 结束滑动
                if (Math.abs(x - mStartPosX) > mTouchSlop || Math.abs(y - mStartPosY) > mTouchSlop) {
                    mIsClick = false;
                    if (mOrient == ORI_HORIZONTAL) {
                        DialogUtil.toast("水平滑动距离：" + (x - mStartPosX));
                    } else if (mOrient == ORI_VERTICAL) {
                        DialogUtil.toast("垂直滑动距离：" + (y - mStartPosY));
                    }
                }

                // 更新数据
                mLastPosX = 0;
                mLastPosY = 0;
                mStartPosX = 0;
                mStartPosY = 0;

                // 处理点击
                if (mIsClick) {
                    final long tempTime = System.currentTimeMillis();
                    mLastTouchTime = tempTime;

                    mHandler.removeCallbacks(clickRunnable);
                    touchCount.incrementAndGet();
                    clickRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (tempTime == mLastTouchTime) {
                                if (touchCount.get() == 1) {
                                    DialogUtil.toast("click 1 time");
                                } else if (touchCount.get() >= 2) {
                                    DialogUtil.toast("click multi:" + touchCount.get());
                                }

                                touchCount.set(0);
                            }
                        }
                    };
                    mHandler.postDelayed(clickRunnable, multiTouchInterval);
                }
                mIsClick = true;
                break;
        }

        return true;
    }

    private void moveUp(float delta) {

    }

    private void moveDown(float delta) {

    }

    private void backward(float delta) {

    }

    private void forward(float delta) {

    }

    private void setOrientation(int ori) {
        if (mOrient == ORI_NONE) {
            mOrient = ori;
        }
    }
}
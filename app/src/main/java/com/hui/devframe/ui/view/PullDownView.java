package com.hui.devframe.ui.view;


import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;

import com.socks.library.KLog;

import java.util.Date;

import hui.devframe.R;

/**
 * 下拉刷新控件，目前支持内嵌ListView
 *
 * @author nieyu
 */
public class PullDownView extends FrameLayout implements OnGestureListener, AnimationListener {
    protected static final int TIME_LIMIT = 1000;

    protected static final int STATE_CLOSE = 1;                     // 未下拉
    protected static final int STATE_OPEN = 2;                      // 正在下拉，没拉动到阈值
    protected static final int STATE_OPEN_RELEASE = 3;              // 下拉结束，手指松开，没拉动到阈值
    protected static final int STATE_OPEN_MAX = 4;                  // 拉动超过阈值，
    protected static final int STATE_OPEN_MAX_RELEASE = 5;          // 下拉结束，手指松开，已经超过阈值
    protected static final int STATE_UPDATE = 6;                    // 下拉刷新被触发，正在更新
    protected static final int STATE_UPDATE_SCROLL = 7;             // 下拉刷新被触发，正在更新，此时用户又在滚动

    protected static int UPDATE_HEIGHT;                             // 下拉最大高度，根据这个值判断是否可以刷新

    protected static final int ARROW_DIRECTION_UP = 1;              // 箭头的方向,向上
    protected static final int ARROW_DIRECTION_DOWN = 2;            // 箭头的方向,向下
    private boolean mIsLiveChat;

    protected int mMaxHeight;                                       // 下拉刷新条最大高度，包括广告高度
    protected View mPullDownContainer;
    protected View mUpRefreshContainer;
    protected ImageView mPullDownIcon;
    protected ImageView mUpRefreshIcon;
    protected View mRefreshContainer;

    private GestureDetector mDetector;
    private Animation mAnimationUp;
    private Animation mAnimationDown;
    private AnimatorSet mPullDownAnim = new AnimatorSet();
    boolean mIsScrollingBack;
    protected Flinger mFlinger = new Flinger();

    protected int mPadding;                                          // 当前的下拉位移
    protected int mState = STATE_CLOSE;                             // 当前的状态，默认值为未处于下拉状态

    protected UpdateHandle mUpdateHandle;                           // 更新事件监听器
    protected FlingHandle mFlingHandle;                             // 滑动事件监听器

    private IUpdateViewWatcher mUpdateViewWatcher;

    public FrameLayout mRefreshView;

    private boolean mEnable = true;

    boolean mIsShowStatusIcon = true;                       // 是否显示箭头和加载状态

    private int mArrowDirect = ARROW_DIRECTION_DOWN;
    private long startTime = 0;
    private long mLimit = TIME_LIMIT;                               // 刷新人延迟时间，默认是1秒
    private boolean canPullDown = true;                             // 是不是可以下拉
    private boolean canContentSwipeHorizontal = false;              // 内容是否可以横向滚动
    private SwipeType swipeType = SwipeType.SWIPE_TYPE_IDLE;        // 当前滚动方向，只有在canContentSwipeHorizontal被置为true时有意义

    private enum SwipeType {
        SWIPE_TYPE_IDLE,
        SWIPE_TYPE_HORIZONTAL,
        SWIPE_TYPE_VERTICAL,
    }



    // 能不能下拉设置
    protected boolean isCanPullDown() {
        return canPullDown;
    }

    public void setCanPullDown(boolean canPullDown) {
        this.canPullDown = canPullDown;
    }
















    /**
     * List滑动监听
     *
     * @author guhui
     */
    protected interface IListPullTouchListener {
        /**
         * 滑动
         *
         * @param distance 滑动的距离
         */
        public void onListPullTouch(float distance);
    }

    private IListPullTouchListener mListPullTouchListener;

    protected void setOnPullTouchListener(IListPullTouchListener listener) {
        mListPullTouchListener = listener;
    }

    protected void setShowStatusIcon(boolean isShowStatusIcon) {
        mIsShowStatusIcon = isShowStatusIcon;

        if (!mIsShowStatusIcon) {
            mPullDownContainer.setVisibility(View.GONE);
        }
    }











    // 构造 初始化
    public PullDownView(Context context) {
        super(context);
        init();
        addUpdateBar();
    }

    public PullDownView(Context context, boolean liveChat) {
        super(context);
        mIsLiveChat = liveChat;
        init();
        addUpdateBar();
    }

    public PullDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        addUpdateBar();
    }

    private void init() {
        if (mIsLiveChat) {
            UPDATE_HEIGHT = getResources().getDimensionPixelSize(R.dimen.common_listview_updatebar_height_1);
        } else {
            UPDATE_HEIGHT = getResources().getDimensionPixelSize(R.dimen.common_listview_updatebar_height);
        }
        mMaxHeight = UPDATE_HEIGHT;
        setDrawingCacheEnabled(false);
        setClipChildren(false);
        mDetector = new GestureDetector(getContext(),this);
        mDetector.setIsLongpressEnabled(true);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
    }

    /**
     * 添加下拉更新条和更新条中的view
     */
    protected void addUpdateBar() {
        mAnimationUp = AnimationUtils.loadAnimation(getContext(), R.anim.common_listview_rotate_up);
        if(mAnimationUp != null){
            mAnimationUp.setFillAfter(true);
            mAnimationUp.setFillBefore(false);
            mAnimationUp.setAnimationListener(this);
        }

        mAnimationDown = AnimationUtils.loadAnimation(getContext(), R.anim.common_listview_rotate_down);
        if(mAnimationDown != null){
            mAnimationDown.setFillAfter(true);
            mAnimationDown.setFillBefore(false);
            mAnimationDown.setAnimationListener(this);
        }

        if (mIsLiveChat) {
            mRefreshView = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.ui_pull_view_listview_vw_update_bar_1, null);
        } else {
            mRefreshView = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.ui_pull_view_listview_vw_update_bar, null);
        }
        mRefreshContainer = mRefreshView.findViewById(R.id.common_listview_refresh_content_ll);
        mRefreshView.setVisibility(View.GONE);

        addView(mRefreshView);

        mPullDownContainer = mRefreshContainer.findViewById(R.id.common_list_view_pull_down_container);
        mPullDownIcon = (ImageView) mPullDownContainer.findViewById(R.id.common_listview_refresh_pull_down_icon);

        mUpRefreshContainer = mRefreshContainer.findViewById(R.id.common_listview_refresh_up_refresh_container);
        mUpRefreshIcon = (ImageView) mUpRefreshContainer.findViewById(R.id.common_listview_refresh_up_refresh_icon);

        mUpRefreshIcon.setBackgroundResource(R.drawable.common_listview_refresh_anim);
        mPullDownIcon.setBackgroundResource(R.drawable.common_listview_pull_down_anim);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(getHandler() == null){
            return;
        }
        if (mArrowDirect == ARROW_DIRECTION_UP) {
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPullDownContainer.clearAnimation();
                    mPullDownContainer.setVisibility(mUpRefreshContainer.getVisibility() == GONE || mIsShowStatusIcon ? View.VISIBLE : View.GONE);
                }
            }, 0);

        } else if (mArrowDirect == ARROW_DIRECTION_DOWN) {
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPullDownContainer.clearAnimation();
                    mPullDownContainer.setVisibility(mUpRefreshContainer.getVisibility() == GONE || mIsShowStatusIcon ? View.VISIBLE : View.GONE);
                }
            }, 0);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }










    // 获取内容 设置回调
    protected View getContentView() {
        return getChildAt(1);
    }


    protected void setUpdateHandle(UpdateHandle handle) {
        mUpdateHandle = handle;
    }

    protected void setFlingHandle(FlingHandle handle) {
        mFlingHandle = handle;
    }



    public void setRefreshIcon(int resId) {
        mUpRefreshIcon.setBackgroundResource(resId);
    }

    public void setPullDownIcon(int resId) {
        mPullDownIcon.setBackgroundResource(resId);
    }















    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!mEnable) {
            return superDispatchTouchEvent(event);
        }
        if (mIsScrollingBack) {
            return true;
        }

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            swipeType = SwipeType.SWIPE_TYPE_IDLE;
        }

        if (canContentSwipeHorizontal) {
            if (swipeType == SwipeType.SWIPE_TYPE_IDLE) {
                // 未确定是否处理事件，继续分发
            } else if (swipeType == SwipeType.SWIPE_TYPE_HORIZONTAL) {
                // 不处理事件，按原样分发，并不再继续执行下拉刷新逻辑
                return superDispatchTouchEvent(event);
            }
        }

        //点击事件交由手势监听器处理
        boolean retValue = mDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                retValue = release();
                break;
            case MotionEvent.ACTION_DOWN:
                break;
        }

        /**
         * 在{@link STATE_UPDATE}，和{@link STATE_UPDATE_SCROLL}状态下本控件不处理事件，全部交给listView处理
         */
        if (mState == STATE_UPDATE || mState == STATE_UPDATE_SCROLL) {
            updateView();
            return superDispatchTouchEvent(event);
        }

        if ((retValue || mState == STATE_OPEN || mState == STATE_OPEN_MAX || mState == STATE_OPEN_MAX_RELEASE || mState == STATE_OPEN_RELEASE) && getContentView().getTop() != 0) {
            //当处于上面几种状态时，需要给listView发一个cancel的touch事件来防止listview出现长按操作
            event.setAction(MotionEvent.ACTION_CANCEL);
            superDispatchTouchEvent(event);
            updateView();
            return true;
        } else {
            updateView();
            return superDispatchTouchEvent(event);
        }
    }

    private boolean superDispatchTouchEvent(MotionEvent event) {
        try {
            return super.dispatchTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean release() {
        if (mPadding >= 0) {
            return false;
        }
        switch (mState) {
            case STATE_OPEN:
            case STATE_OPEN_RELEASE:
                if (Math.abs(mPadding) < UPDATE_HEIGHT) {
                    mState = STATE_OPEN_RELEASE;
                }
                scrollToClose();
                break;
            case STATE_OPEN_MAX:
            case STATE_OPEN_MAX_RELEASE:
                mState = STATE_OPEN_MAX_RELEASE;
                scrollToUpdate();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 滚动到更新位置
     */
    protected void scrollToUpdate() {
        mFlinger.startUsingDistance(-mPadding - UPDATE_HEIGHT, 300);
    }

    /**
     * 滚动到关闭位置
     */
    protected void scrollToClose() {
        mFlinger.startUsingDistance(-mPadding, 300);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        KLog.e("onDown");
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        KLog.e("onFling");
        final int FLING_MIN_DISTANCE = 50, FLING_MIN_VELOCITY = 50;

        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            if (mFlingHandle != null) {
                mFlingHandle.flingToLeft();
            }

        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            if (mFlingHandle != null) {
                mFlingHandle.flingToRight();
            }

        } else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
            if (mFlingHandle != null) {
                mFlingHandle.flingToDown();
            }

        } else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
            if (mFlingHandle != null) {
                mFlingHandle.flingToUp();
            }
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        KLog.e("onLongPress");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        KLog.e("onScroll");
        if (canContentSwipeHorizontal && swipeType == SwipeType.SWIPE_TYPE_IDLE) {
            // 内容可以横向滚动, 当前滚动方向未定
            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                // horizontal
                swipeType = SwipeType.SWIPE_TYPE_HORIZONTAL;
            } else {
                // vertical
                swipeType = SwipeType.SWIPE_TYPE_VERTICAL;
            }
            return true;
        }
        //将下拉速度降低到一半
        distanceY = (float) (distanceY * 0.5);
        AdapterView v = (AdapterView) getContentView();
        if (v == null || v.getCount() == 0 || v.getChildCount() == 0) {
            return false;
        }
        boolean isStart = v.getFirstVisiblePosition() == 0;
        if (isStart) {
            isStart = v.getChildAt(0).getTop() == 0;
        }
        //回调滑动事件
        invokeOnScrolling(distanceY);
        //isStart用来判断listView是不是滚动到最上位置，如果listview在最上位置，继续下拉则响应下拉事件，否则将事件交给listview自己处理
        if (canPullDown && ((distanceY < 0 && isStart) || mPadding < 0)) {
            if (mUpdateViewWatcher != null) {
                mUpdateViewWatcher.updateViewShow();
            }
            boolean r = move(distanceY, true);
            return r;
        } else {
            if (mUpdateViewWatcher != null) {
                mUpdateViewWatcher.updateViewHide();
            }
            return false;
        }
    }

    private void invokeOnScrolling(float distanceY) {
        if (null != mListPullTouchListener) {
            mListPullTouchListener.onListPullTouch(distanceY);
        }
    }

    /**
     * 下拉指定像素距离
     *
     * @param distanceY，下拉距离
     * @param isTriggerByUser，true - 用户手指触发; false - 自动滚回触发
     * @return
     */
    boolean move(float distanceY, boolean isTriggerByUser) {

        //正在刷新，不允许继续下拉，但可以向上滚动进入STATE_UPDATE_SCROLL状态
        if (mState == STATE_UPDATE) {
            if (distanceY < 0) {
                return true;
            } else if (isTriggerByUser == true) {
                mState = STATE_UPDATE_SCROLL;
            }
        }

        /**
         * 正在更新，下拉超过{@link UPDATE_HEIGHT}，不允许继续下拉
         */
        if (mState == STATE_UPDATE_SCROLL && distanceY < 0 && -mPadding >= UPDATE_HEIGHT) {
            return true;
        }

        //累加下拉距离，mPadding始终为负值
        mPadding += distanceY;
        if (mPadding > 0) {
            mPadding = 0;
        }

        if (!isTriggerByUser) {
            if (mState == STATE_OPEN_MAX_RELEASE) {
                //自动滚动情况下，STATE_OPEN_MAX_RELEASE自动滚动进入STATE_UPDATE状态，通知更新监听器
                mState = STATE_UPDATE;
                updateHandler();
            } else if (mState == STATE_UPDATE && mPadding == 0) {
                mState = STATE_CLOSE;
            } else if (mState == STATE_OPEN_RELEASE && mPadding == 0) {
                mState = STATE_CLOSE;
            } else if (mState == STATE_UPDATE_SCROLL && mPadding == 0) {
                mState = STATE_CLOSE;
            }
            requestLayout();
            return true;
        }

        //更具不同的状态和mPadding值来切换状态，并通知界面重绘
        switch (mState) {
            case STATE_CLOSE:
                if (mPadding < 0) {
                    startTime = System.currentTimeMillis();
                    mState = STATE_OPEN;
                    showPullDownProgress(true);
                    showProgress(false);
                    mPullDownContainer.setVisibility(View.VISIBLE);
                }
                break;
            case STATE_OPEN:
                if (Math.abs(mPadding) >= UPDATE_HEIGHT) {
                    mState = STATE_OPEN_MAX;
                    showProgress(false);
                    mPullDownContainer.setVisibility(View.VISIBLE);
                    makeArrowUp();
                } else if (mPadding == 0) {
                    mState = STATE_CLOSE;
                }
                break;
            case STATE_OPEN_MAX:
                if (Math.abs(mPadding) < UPDATE_HEIGHT) {
                    mState = STATE_OPEN;
                    showProgress(false);
                    mPullDownContainer.setVisibility(View.VISIBLE);
                    makeArrowDown();
                }
                break;
            case STATE_OPEN_RELEASE:
            case STATE_OPEN_MAX_RELEASE:
                if (isTriggerByUser) {
                    if (Math.abs(mPadding) >= UPDATE_HEIGHT) {
                        mState = STATE_OPEN_MAX;
                        showProgress(false);
                        mPullDownContainer.setVisibility(View.VISIBLE);
                        makeArrowUp();
                    } else if (Math.abs(mPadding) < UPDATE_HEIGHT) {
                        mState = STATE_OPEN;
                        showProgress(false);
                        mPullDownContainer.setVisibility(View.VISIBLE);
                        makeArrowDown();
                    } else if (mPadding == 0) {
                        mState = STATE_CLOSE;
                    }
                } else {
                    if (mPadding == 0) {
                        mState = STATE_CLOSE;
                    }
                }
                return true;
            case STATE_UPDATE:
                if (mPadding == 0) {
                    mState = STATE_CLOSE;
                }
                return true;
            default:
                break;
        }
        requestLayout();
        return true;
    }

    protected void updateHandler() {
        if (mUpdateHandle != null) {
            mUpdateHandle.onUpdate();
        }
    }

    public interface IUpdateViewWatcher {
        void updateViewShow();
        void updateViewHide();
    }

    public void setUpdateViewWatcher(IUpdateViewWatcher watcher) {
        mUpdateViewWatcher = watcher;
    }

    /**
     * 更新view状态
     */
    protected void updateView() {
        View updateBar = mRefreshView;
        //根据不同的状态绘制界面
        switch (mState) {
            case STATE_CLOSE:
                //close状态不显示更新条
                if (updateBar.getVisibility() != View.GONE) {
                    updateBar.setVisibility(View.GONE);
                }
                showPullDownProgress(false);
                resetAnim();
            case STATE_OPEN:
            case STATE_OPEN_RELEASE:
                if (updateBar.getVisibility() != View.VISIBLE) {
                    updateBar.setVisibility(View.VISIBLE);
                }
                break;
            case STATE_OPEN_MAX:
            case STATE_OPEN_MAX_RELEASE:
                if (updateBar.getVisibility() != View.VISIBLE) {
                    updateBar.setVisibility(View.VISIBLE);
                }
                break;
            case STATE_UPDATE:
            case STATE_UPDATE_SCROLL:
                if (mUpRefreshContainer.getVisibility() != View.VISIBLE && mIsShowStatusIcon) {
                    showProgress(true);
                    showPullDownProgress(false);
                }
                if (mPullDownContainer.getVisibility() != View.GONE) {
                    mPullDownContainer.setVisibility(View.GONE);
                }

                if (updateBar.getVisibility() != View.VISIBLE) {
                    updateBar.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 下拉时的动画
     * @param b
     */
    protected void showPullDownProgress(boolean b) {
        final AnimationDrawable drawable = (AnimationDrawable) mPullDownIcon.getBackground();
        if (b) {
            drawable.start();
        } else {
            drawable.stop();
        }
    }

    /**
     * 刷新时loading的动画
     * @param b
     */
    private void showProgress(boolean b) {
        final AnimationDrawable drawable = (AnimationDrawable) mUpRefreshIcon.getBackground();
        if (b) {
            drawable.start();
        } else {
             drawable.stop();
        }

        mUpRefreshContainer.setVisibility(b ? VISIBLE : GONE);
    }

    /**
     * 重写onLayout 实现view滑动
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mRefreshView.layout(0, 0, getMeasuredWidth(), -mPadding);
        mRefreshContainer.layout(0, 0, getMeasuredWidth(), -mPadding);
        try {
            getContentView().layout(0, -mPadding, getMeasuredWidth(), getMeasuredHeight() - mPadding);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {
        KLog.e("onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        KLog.e("onSingleTapUp");
        return false;
    }

    /**
     * 自动滚回时使用
     *
     * @author nieyu
     */
    private class Flinger implements Runnable {
        private Scroller mScroller;
        private int mLastFlingX;
        private boolean mIsStart;

        public Flinger() {
            mScroller = new Scroller(getContext());
        }

        private void startCommon() {
            removeCallbacks(this);
        }

        /**
         * 在制定的时间内滚动制定距离
         *
         * @param distance 滚动距离
         * @param dur      滚动时间
         */
        public void startUsingDistance(int distance, int dur) {
            if (distance == 0)
                distance--;
            //初始化参数
            startCommon();
            mLastFlingX = 0;
            //使用Scroller计算滚动的时间和位移
            mScroller.startScroll(0, 0, -distance, 0, dur);
            mIsScrollingBack = true;
            post(this);
        }

        @Override
        public void run() {

            final Scroller scroller = mScroller;
            boolean more = scroller.computeScrollOffset();
            final int x = scroller.getCurrX();
            //scroller计算本次需要滚动位移
            int delta = mLastFlingX - x;

            move(delta, false);
            updateView();
            if (more) {
                mLastFlingX = x;
                post(this);
            } else {
                mIsScrollingBack = false;
                removeCallbacks(this);
            }
        }
    }

    /**
     * 设置刷新的等待时间
     *
     * @param limit
     */
    protected void setLimit(long limit) {
        this.mLimit = limit;
    }

    protected long getLimit() {
        return mLimit;
    }

    /**
     * 结束更新
     *
     * @param date 更新时间
     */
    protected void endUpdate(final Date date) {
        if (System.currentTimeMillis() - startTime > mLimit) {
            close();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            }, mLimit - (System.currentTimeMillis() - startTime));
        }
    }

    public void close() {
        if (mPadding != 0) {
            scrollToClose();
        } else {
            mState = STATE_CLOSE;
        }
        mPullDownContainer.clearAnimation();
        mPullDownContainer.setVisibility(View.GONE);
        resetAnim();
        mArrowDirect = ARROW_DIRECTION_DOWN;
        startTime = 0;
    }

    public interface UpdateHandle {
        void onUpdate();
    }

    protected interface FlingHandle {
        void flingToLeft();
        void flingToRight();
        void flingToUp();
        void flingToDown();
    }

    /**
     * 设置状态为正在刷新
     */
    public void update() {
        startTime = System.currentTimeMillis();
        mPadding = -UPDATE_HEIGHT;
        mState = STATE_UPDATE_SCROLL;
        postDelayed(new Runnable() {

            @Override
            public void run() {
                updateView();

            }
        }, 10);
    }

    public void setEnable(boolean enable) {
        mEnable = enable;
        invalidate();
    }

    public boolean isEnable() {
        return mEnable;
    }

    /**
     * uncomment the code to enable animation
     */
    private void makeArrowUp() {
        if (mArrowDirect == ARROW_DIRECTION_UP) {
            return;
        }
        mArrowDirect = ARROW_DIRECTION_UP;
    }

    /**
     * uncomment the code to enable animation
     */
    private void makeArrowDown() {
        if (mArrowDirect == ARROW_DIRECTION_DOWN) {
            return;
        }
        mArrowDirect = ARROW_DIRECTION_DOWN;
    }

    private void resetAnim(){
        if(mPullDownAnim != null){
            mPullDownAnim.cancel();
        }
    }

    public void setCanContentSwipeHorizontal(boolean canContentSwipeHorizontal) {
        this.canContentSwipeHorizontal = canContentSwipeHorizontal;
    }

    public boolean getCanContentSwipeHorizontal() {
        return canContentSwipeHorizontal;
    }
}
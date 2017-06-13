package com.hui.devframe.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import hui.devframe.R;

/**
 * 指示器
 * Created by wanghui on 16/5/31.
 */
public class RectIndicatorView extends FrameLayout {
    private static final int BLUE_COLOR = 0xff45b7ff;
    private static final int GRAY_COLOR = 0xff666666;

    private TextView mLeft;
    private TextView mRight;
    private View mIndicator;

    public RectIndicatorView(Context context) {
        this(context, null);
    }

    public RectIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.ui_view_rect_indicator_layout, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeft = (TextView) findViewById(R.id.rect_indicator_tv_left);
        mRight = (TextView) findViewById(R.id.rect_indicator_tv_right);
        mIndicator = findViewById(R.id.rect_indicator_indicator);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        FrameLayout.LayoutParams params = (LayoutParams) mIndicator.getLayoutParams();
        params.width = width / 2;
    }

    public void setOffset(int position, float positionOffset) {
        if (position == 0) {
            mLeft.setTextColor(BLUE_COLOR);
            mRight.setTextColor(GRAY_COLOR);
        } else {
            mLeft.setTextColor(GRAY_COLOR);
            mRight.setTextColor(BLUE_COLOR);
        }

        FrameLayout.LayoutParams params = (LayoutParams) mIndicator.getLayoutParams();
        int half = getWidth() / 2;
        params.leftMargin = (int) (position * half + half * positionOffset);
        mIndicator.requestLayout();
    }
}

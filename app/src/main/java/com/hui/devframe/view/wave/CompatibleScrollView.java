package com.hui.devframe.view.wave;

import android.content.Context;

import android.util.AttributeSet;

import android.widget.ScrollView;

/**
 * Created by wanghui on 2016-11-16.
 */
public class CompatibleScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;

    public CompatibleScrollView(Context context) {
        super(context);
    }

    public CompatibleScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CompatibleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override

    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface ScrollViewListener {
        void onScrollChanged(CompatibleScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}
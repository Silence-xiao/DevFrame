package com.hui.devframe.activity.entry;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hui.devframe.base.BaseActivity;
import com.hui.devframe.util.ScreenUtil;
import com.hui.devframe.ui.wave.CompatibleScrollView;

import hui.devframe.R;

public class ScrollLearnActivity extends BaseActivity {

    private CompatibleScrollView mScroll;
    private TextView mText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mScroll = (CompatibleScrollView) findViewById(R.id.scroll_view);
        mText1 = (TextView) findViewById(R.id.scroll_text1);
        mScroll.setScrollViewListener(new CompatibleScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(CompatibleScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y > ScreenUtil.dp2px(50)) {
                    mText1.setVisibility(View.VISIBLE);
                } else {
                    mText1.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_test_scroll_top;
    }
}

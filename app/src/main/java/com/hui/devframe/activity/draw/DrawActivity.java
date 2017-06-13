package com.hui.devframe.activity.draw;

import android.os.Bundle;

import hui.devframe.R;
import com.hui.devframe.base.BaseActivity;

public class DrawActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    protected void initView() {
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_test_draw;
    }
}
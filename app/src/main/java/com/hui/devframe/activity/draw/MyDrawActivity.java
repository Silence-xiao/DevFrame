package com.hui.devframe.activity.draw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hui.devframe.base.BaseActivity;
import com.hui.devframe.ui.draw.DrawTestView;

import hui.devframe.R;

public class MyDrawActivity extends BaseActivity {

    private DrawTestView mDrawView;
    private Button mCover;
    private Button mPen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    protected void initView() {
        mDrawView = (DrawTestView) findViewById(R.id.draw_pad_view);
        mCover = (Button) findViewById(R.id.draw_pad_cover);
        mPen = (Button) findViewById(R.id.draw_pad_pen);

        mCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_test_my_draw;
    }
}
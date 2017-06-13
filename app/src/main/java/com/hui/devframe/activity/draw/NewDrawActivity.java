package com.hui.devframe.activity.draw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hui.devframe.R;
import com.hui.devframe.base.BaseActivity;
import com.hui.devframe.ui.draw.DrawPadView;

public class NewDrawActivity extends BaseActivity {

    private DrawPadView mDrawView;
    private Button mCover;
    private Button mPen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        mDrawView = (DrawPadView) findViewById(R.id.draw_pad_view);
        mCover = (Button) findViewById(R.id.draw_pad_cover);
        mPen = (Button) findViewById(R.id.draw_pad_pen);

        mCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setPenCover(true);
            }
        });
        mPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setPenCover(false);
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_test_new_draw;
    }
}
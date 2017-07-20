package com.hui.devframe.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.hui.devframe.activity.entry.MatrixLearnActivity;
import com.hui.devframe.activity.entry.PagerLearnActivity;
import com.hui.devframe.activity.entry.ScrollLearnActivity;
import com.hui.devframe.activity.fragment.FragmentLearnActivity;
import com.hui.devframe.activity.learn.TestDetectorActivity;
import com.hui.devframe.activity.learn.TestSwipeRefreshActivity;
import com.hui.devframe.base.BaseActivity;
import com.hui.devframe.util.ReturnCall;

import hui.devframe.R;

public class InitActivity extends BaseActivity {

    ViewGroup mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initButtons();
        initView();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_init;
    }

    protected void initView() {
    }

    private void initButtons() {
        mRoot = (ViewGroup) findViewById(R.id.init_root);

        createButton("测试Fragment", new ReturnCall() {
            @Override
            public void call() {
                startPage(FragmentLearnActivity.class);
            }
        });
        createButton("测试ViewPager", new ReturnCall() {
            @Override
            public void call() {
                startPage(PagerLearnActivity.class);
            }
        });
        createButton("测试Matrix", new ReturnCall() {
            @Override
            public void call() {
                startPage(MatrixLearnActivity.class);
            }
        });
        createButton("TestDetectorActivity", new ReturnCall() {
            @Override
            public void call() {
                startPage(TestDetectorActivity.class);
            }
        });
        createButton("scroll", new ReturnCall() {
            @Override
            public void call() {
                startPage(ScrollLearnActivity.class);
            }
        });
        createButton("refresh", new ReturnCall() {
            @Override
            public void call() {
                startPage(TestSwipeRefreshActivity.class);
            }
        });
    }

    private void createButton(String text, final ReturnCall call) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.call();
            }
        });
        mRoot.addView(btn);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

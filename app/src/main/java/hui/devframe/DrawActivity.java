package hui.devframe;

import android.os.Bundle;

import hui.devframe.base.BaseActivity;

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
        return R.layout.test_draw_activity;
    }
}
package hui.devframe;

import android.os.Bundle;

import hui.devframe.base.BaseActivity;

public class DrawActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_draw_activity);

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.test_draw_activity;
    }
}
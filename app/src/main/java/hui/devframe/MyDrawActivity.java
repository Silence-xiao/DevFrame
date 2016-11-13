package hui.devframe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hui.devframe.base.BaseActivity;
import hui.devframe.draw.DrawPadView;

public class MyDrawActivity extends BaseActivity {

    private DrawPadView mPad;
    private Button mCover;
    private Button mPen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
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
        return R.layout.test_my_draw_activity;
    }
}
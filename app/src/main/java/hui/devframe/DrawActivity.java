package hui.devframe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hui.devframe.base.BaseActivity;
import hui.devframe.draw.DrawPadView;

public class DrawActivity extends BaseActivity {

    private DrawPadView mPad;
    private Button mCover;
    private Button mPen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        mPad = (DrawPadView) findViewById(R.id.draw_pad_view);
        mCover = (Button) findViewById(R.id.draw_pad_cover);
        mPen = (Button) findViewById(R.id.draw_pad_pen);

        mCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPad.setPenCover(true);
            }
        });
        mPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPad.setPenCover(false);
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.test_draw_activity;
    }
}
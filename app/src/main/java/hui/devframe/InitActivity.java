package hui.devframe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import hui.devframe.base.BaseActivity;
import hui.devframe.ui.DynamicWave;
import hui.devframe.util.LogUtils;
import hui.devframe.ui.wave.WaveHelper;
import hui.devframe.ui.wave.WaveView;
import hui.devframe.util.ReturnCall;

public class InitActivity extends BaseActivity {
    private LogUtils log = LogUtils.getLog(InitActivity.class.getSimpleName());

    ViewGroup mRoot;
    DynamicWave mWave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        initView();
    }

    private void initView() {
        mRoot = (ViewGroup) findViewById(R.id.init_root);

        mRoot.addView(createButton("测试Fragment", new ReturnCall() {
            @Override
            public void call() {
                startActivity(new Intent(InitActivity.this, FragmentLearnActivity.class));
            }
        }));
        mRoot.addView(createButton("测试ViewPager", new ReturnCall() {
            @Override
            public void call() {
                startActivity(new Intent(InitActivity.this, PagerLearnActivity.class));
            }
        }));
        mRoot.addView(createButton("水增加", new ReturnCall() {
            @Override
            public void call() {
                EditText edit = (EditText) findViewById(R.id.init_edit);
                mWave.updateWave(0.5f, edit.getText().toString());
            }
        }));

        mWave = (DynamicWave) findViewById(R.id.init_wave);
    }

    private Button createButton(String text, final ReturnCall call) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.call();
            }
        });
        return btn;
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Animation mAnim = AnimationUtils.loadAnimation(this, R.anim.fudao_buy_btn_scale_amin);
//        findViewById(R.id.init_btn).startAnimation(mAnim);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

package hui.devframe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import hui.devframe.adapter.ItemAdapter;
import hui.devframe.base.BaseActivity;
import hui.devframe.view.NoScrollGridView;
import hui.devframe.util.LogUtils;
import hui.devframe.util.ReturnCall;
import hui.devframe.veex.VeexActivity;

public class InitActivity extends BaseActivity {
    private LogUtils log = LogUtils.getLog(InitActivity.class.getSimpleName());

    ViewGroup mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_activity);

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
        mRoot.addView(createButton("测试Matrix", new ReturnCall() {
            @Override
            public void call() {
                startActivity(new Intent(InitActivity.this, MatrixLearnActivity.class));
            }
        }));
        mRoot.addView(createButton("veex", new ReturnCall() {
            @Override
            public void call() {
                startActivity(new Intent(InitActivity.this, VeexActivity.class));
            }
        }));
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

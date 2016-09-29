package hui.devframe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import hui.devframe.base.BaseActivity;
import hui.devframe.util.LogUtils;
import hui.devframe.util.ReturnCall;
import hui.devframe.veex.VeexActivity;

public class InitActivity extends BaseActivity {
    private LogUtils log = LogUtils.getLog(InitActivity.class.getSimpleName());

    ViewGroup mRoot;
    private ImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_activity);

        initButtons();
        initView();
    }

    private void initView() {
        mImg = (ImageView) findViewById(R.id.init_image);
    }

    private void initButtons() {
        mRoot = (ViewGroup) findViewById(R.id.init_root);

        createButton("测试Fragment", new ReturnCall() {
            @Override
            public void call() {
                startActivity(new Intent(InitActivity.this, FragmentLearnActivity.class));
            }
        });
        createButton("测试ViewPager", new ReturnCall() {
            @Override
            public void call() {
                startActivity(new Intent(InitActivity.this, PagerLearnActivity.class));
            }
        });
        createButton("测试Matrix", new ReturnCall() {
            @Override
            public void call() {
                startActivity(new Intent(InitActivity.this, MatrixLearnActivity.class));
            }
        });
        createButton("veex", new ReturnCall() {
            @Override
            public void call() {
                startActivity(new Intent(InitActivity.this, VeexActivity.class));
            }
        });
        createButton("Picasso测试", new ReturnCall() {
            @Override
            public void call() {
                Picasso.with(InitActivity.this).load("http://pic77.nipic.com/file/20150907/7257561_095930209148_2.jpg").into(mImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        log.e("success");
                    }

                    @Override
                    public void onError() {
                        log.e("error");
                    }
                });
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

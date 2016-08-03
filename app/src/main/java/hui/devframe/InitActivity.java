package hui.devframe;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

import hui.devframe.common.adapter.SimplePagerAdapter;
import hui.devframe.common.ui.PagerIndicator;
import hui.devframe.common.util.LogUtils;

public class InitActivity extends AppCompatActivity {
    private LogUtils log = LogUtils.getLog(InitActivity.class.getSimpleName());

    private ViewPager mPager;
    private PagerIndicator mIndicator;
    private List<View> mViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        LayoutInflater inflater = LayoutInflater.from(this);
        mViews.add(inflater.inflate(R.layout.pager_view1, null));
        mViews.add(inflater.inflate(R.layout.pager_view2, null));
        mViews.add(inflater.inflate(R.layout.pager_view3, null));

        mPager = (ViewPager) findViewById(R.id.init_pager);
        mPager.setAdapter(new SimplePagerAdapter(this, mViews));

        mIndicator = (PagerIndicator) findViewById(R.id.init_indicator);
        mIndicator.setViewPager(mPager);


        // 关于计算view高度
        final View decorView = getWindow().getDecorView();
        final View rootView = decorView.findViewById(android.R.id.content);

        View xmlFileRootView = ((ViewGroup) rootView).getChildAt(0);

        log.e("decorView" + decorView.toString() + "  id:" + decorView.getId());
        log.e("rootView" + rootView.toString() + "  id:" + rootView.getId());

        mPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            // 计算高度
            @Override
            public void onGlobalLayout() {
                int heightDiff = decorView.getHeight() - rootView.getHeight();
                log.e(decorView.getHeight() + " " + rootView.getHeight() + " " + heightDiff + "");
                mPager.getViewTreeObserver().removeGlobalOnLayoutListener(this); // 会多次执行记得移除
            }
        });

        log.e(getScreenHeight(this) + "");
    }

    public int getScreenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }
}

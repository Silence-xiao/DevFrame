package hui.devframe;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

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
    }
}

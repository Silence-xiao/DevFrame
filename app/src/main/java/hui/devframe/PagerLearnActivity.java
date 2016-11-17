package hui.devframe;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hui.devframe.adapter.SimplePagerAdapter;
import hui.devframe.view.RectIndicatorView;
import hui.devframe.view.RectPagerIndicator;

public class PagerLearnActivity extends AppCompatActivity {

    private ViewPager mPager;
    private RectIndicatorView mIndicator;
    private RectPagerIndicator mIndicator2;
    private List<View> mViews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_view_pager_activity);

        LayoutInflater inflater = LayoutInflater.from(this);
        mViews.add(inflater.inflate(R.layout.ui_view_pager_view1_layout, null));
        mViews.add(inflater.inflate(R.layout.ui_view_pager_view2_layout, null));
        mViews.add(inflater.inflate(R.layout.ui_view_pager_view3_layout, null));

        mPager = (ViewPager) findViewById(R.id.init_pager);
        mPager.setAdapter(new SimplePagerAdapter(this, mViews));

        mIndicator2 = (RectPagerIndicator) findViewById(R.id.init_indicator2);
        mIndicator2.setViewPager(mPager);
        mIndicator = (RectIndicatorView) findViewById(R.id.init_indicator);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.setOffset(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

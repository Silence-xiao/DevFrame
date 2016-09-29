package hui.devframe;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hui.devframe.adapter.SimplePagerAdapter;
import hui.devframe.view.PagerIndicator;

public class PagerLearnActivity extends AppCompatActivity {

    private ViewPager mPager;
    private PagerIndicator mIndicator;
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

        mIndicator = (PagerIndicator) findViewById(R.id.init_indicator);
        mIndicator.setViewPager(mPager);
    }
}

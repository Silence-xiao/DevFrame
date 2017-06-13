package com.hui.devframe.activity.entry;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hui.devframe.adapter.ImagePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import hui.devframe.R;

public class PagerLearnActivity extends AppCompatActivity {

    int mIndex;

    private ViewPager mPager;
    private ImagePagerAdapter mRealAdapter;

    private TextView mIndicatorText;
    private List<String> mUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_view_pager_activity);

        // list
        mUrls.add("http://imgs.ebrun.com/resources/2016_04/2016_04_01/201604014051459488484468.jpg");
        mUrls.add("http://photocdn.sohu.com/20150330/mp8651837_1427725608339_4.jpeg");
        mUrls.add("http://pic77.nipic.com/file/20150907/7257561_095930209148_2.jpg");
        mUrls.add("http://img4.imgtn.bdimg.com/it/u=1150177012,2028658662&fm=21&gp=0.jpg");

        // text
        mIndicatorText = (TextView) findViewById(R.id.pager_indicator_text);


        // adapter
        mPager = (ViewPager) findViewById(R.id.init_pager);
        mRealAdapter = new ImagePagerAdapter(this, mUrls);
        mPager.setAdapter(mRealAdapter);

        // reset pos
        mIndex = 0;
        mPager.setCurrentItem(mIndex);
        setIndicatorText();

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndex = position;
                setIndicatorText();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        findViewById(R.id.pager_add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUrls.add("http://img5.imgtn.bdimg.com/it/u=2409510139,1948101242&fm=21&gp=0.jpg");
                mRealAdapter.notifyDataSetChanged();
                if (mRealAdapter.getCount() > 0) {
                    mIndex = mRealAdapter.getCount() - 1;
                    mPager.setCurrentItem(mIndex);
                }
                setIndicatorText();
            }
        });
        findViewById(R.id.pager_update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUrls.set(mIndex, "http://imgs.ebrun.com/resources/2016_04/2016_04_01/201604014051459488484468.jpg");
                mRealAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setIndicatorText() {
        if (mRealAdapter.getCount() > 0) {
            mIndicatorText.setVisibility(View.VISIBLE);
            mIndicatorText.setText((mIndex + 1) + "/" + mUrls.size());
        } else {
            mIndicatorText.setVisibility(View.GONE);
        }
    }
}

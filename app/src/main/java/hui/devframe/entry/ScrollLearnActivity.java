package hui.devframe.entry;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hui.devframe.R;
import hui.devframe.base.BaseActivity;
import hui.devframe.util.ScreenUtil;
import hui.devframe.view.RectIndicatorView;
import hui.devframe.view.RectPagerIndicator;
import hui.devframe.view.wave.CompatibleScrollView;

public class ScrollLearnActivity extends BaseActivity {

    private CompatibleScrollView mScroll;
    private TextView mText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mScroll = (CompatibleScrollView) findViewById(R.id.scroll_view);
        mText1 = (TextView) findViewById(R.id.scroll_text1);
        mScroll.setScrollViewListener(new CompatibleScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(CompatibleScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y > ScreenUtil.dp2px(50)) {
                    mText1.setVisibility(View.VISIBLE);
                } else {
                    mText1.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.test_scroll_top_activity;
    }
}

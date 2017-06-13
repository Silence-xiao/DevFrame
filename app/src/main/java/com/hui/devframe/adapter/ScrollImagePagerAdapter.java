package com.hui.devframe.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Viewpager适配器，可无限滑动
 * Created by wanghui on 15/8/14.
 */
public class ScrollImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    public List<String> mUrls;

    public ScrollImagePagerAdapter(Context mContext, List<String> urls) {
        this.mContext = mContext;
        this.mUrls = urls;
    }

    @Override
    public int getCount() {
        if (mUrls == null || mUrls.size() == 0) {
            return 0;
        } else if (mUrls.size() == 1) {
            return 1;
        } else {
            return 2000;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public int getRealPosition(int position) {
        if (mUrls.size() == 0) {
            return 0;
        } else {
            return position % mUrls.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        container.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Picasso.with(mContext).load(mUrls.get(getRealPosition(position))).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError() {
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public int calNewPosByRealPos(int realIndex) {
        int size = mUrls.size();
        if (size != 0) {
            int base = 1000 + (size - 1000 % size);
            return base + realIndex;
        } else {
            return 0;
        }
    }
}
package com.hui.devframe.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import hui.devframe.R;

/**
 * 单一的fragment的Activity
 * Created by wanghui on 16/8/4.
 */
public abstract class BaseSingleFragmentActivity extends BaseActivity {

    Fragment singleFragment;

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        singleFragment = fm.findFragmentById(R.id.fragment_container);

        if (singleFragment == null) {
            singleFragment = createFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, singleFragment);
            ft.commit();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_base_single_fragment;
    }
}
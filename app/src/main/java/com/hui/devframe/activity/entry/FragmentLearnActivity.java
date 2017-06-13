package com.hui.devframe.activity.entry;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hui.devframe.common.base.BaseSingleFragmentActivity;
import com.hui.devframe.fragment.LearnMainFragment;

public class FragmentLearnActivity extends BaseSingleFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new LearnMainFragment();
    }
}

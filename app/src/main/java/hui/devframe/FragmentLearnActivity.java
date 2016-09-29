package hui.devframe;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import hui.devframe.base.BaseSingleFragmentActivity;
import hui.devframe.util.LogUtils;
import hui.devframe.fragment.LearnMainFragment;

public class FragmentLearnActivity extends BaseSingleFragmentActivity {

    LogUtils log = LogUtils.getLog(FragmentLearnActivity.class.getSimpleName());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new LearnMainFragment();
    }
}

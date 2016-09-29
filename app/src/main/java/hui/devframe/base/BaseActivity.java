package hui.devframe.base;

import android.support.v7.app.AppCompatActivity;

import hui.devframe.util.DialogUtil;

/**
 * Created by wanghui on 16/6/12.
 */
public class BaseActivity extends AppCompatActivity {

    public void toast(String string) {
        DialogUtil.toast(this, string);
    }
}

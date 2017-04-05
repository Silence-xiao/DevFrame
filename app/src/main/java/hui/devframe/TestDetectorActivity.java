package hui.devframe;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import hui.devframe.base.BaseActivity;

public class TestDetectorActivity extends BaseActivity {

    private LinearLayout test_linear_layout;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        mGestureDetector = new GestureDetector(this, new MyOnGestureListener());
        test_linear_layout = (LinearLayout) findViewById(R.id.test_linear_layout);
        test_linear_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Logger.e("onTouch-----" + getActionName(event.getAction()));
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.test_gesture_detector_activity;
    }

    private String getActionName(int action) {
        String name = "";
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                name = "ACTION_DOWN";
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                name = "ACTION_MOVE";
                break;
            }
            case MotionEvent.ACTION_UP: {
                name = "ACTION_UP";
                break;
            }
            default:
                break;
        }
        return name;
    }

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Logger.e("onSingleTapUp-----" + getActionName(e.getAction()));
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Logger.e("onLongPress-----" + getActionName(e.getAction()));
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Logger.e("onScroll-----" + getActionName(e2.getAction()) + ",(" + e1.getX() + "," + e1.getY() + ") ,("
                    + e2.getX() + "," + e2.getY() + ")");
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Logger.e("onFling-----" + getActionName(e2.getAction()) + ",(" + e1.getX() + "," + e1.getY() + ") ,("
                    + e2.getX() + "," + e2.getY() + ")");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Logger.e("onShowPress-----" + getActionName(e.getAction()));
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Logger.e("onDown-----" + getActionName(e.getAction()));
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Logger.e("onDoubleTap-----" + getActionName(e.getAction()));
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Logger.e("onDoubleTapEvent-----" + getActionName(e.getAction()));
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Logger.e("onSingleTapConfirmed-----" + getActionName(e.getAction()));
            return false;
        }
    }
}
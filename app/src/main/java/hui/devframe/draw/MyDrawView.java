package hui.devframe.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import hui.devframe.R;
import hui.devframe.util.ScreenUtil;

public class MyDrawView extends View {

    private Paint mBitmapPaint;
    private Paint mPathPaint;

    private float mLastX;
    private float mLastY;

    private RectF rect;

    public MyDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 设置笔画刷参数
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setStyle(Style.FILL);
        mBitmapPaint.setStrokeJoin(Paint.Join.ROUND);
        mBitmapPaint.setStrokeCap(Paint.Cap.ROUND);


        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setStyle(Style.FILL);
        mPathPaint.setColor(Color.BLACK);
        mPathPaint.setStrokeWidth(ScreenUtil.dp2px(getContext(), 4));

        setDrawingCacheEnabled(true);
        rect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rect.set(ScreenUtil.dp2px(getContext(), 2), ScreenUtil.dp2px(getContext(), 2), getWidth() - ScreenUtil.dp2px(getContext(), 2), getHeight() - ScreenUtil.dp2px(getContext(), 2));
        canvas.drawRoundRect(rect, ScreenUtil.dp2px(getContext(), 10), ScreenUtil.dp2px(getContext(), 10), mPathPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
}

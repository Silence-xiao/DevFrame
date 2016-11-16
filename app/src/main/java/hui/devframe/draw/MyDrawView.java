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

    private final Paint mPaintCover;
    private Paint mBitmapPaint;
    private Paint mPathPaint;

    private float mLastX;
    private float mLastY;

    private RectF rect;
    private boolean isPen = true;

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


        mPaintCover = new Paint();
        mPaintCover.setAntiAlias(true);
        mPaintCover.setColor(Color.GRAY);
        mPaintCover.setStrokeWidth(50);
        //设置图形混合方式，这里使用PorterDuff.Mode.XOR模式，与底层重叠部分设为透明
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.XOR);
        mPaintCover.setXfermode(mode);
        mPaintCover.setStyle(Paint.Style.STROKE);
        //设置笔刷的样式，默认为BUTT，如果设置为ROUND(圆形),SQUARE(方形)，需要将填充类型Style设置为STROKE或者FILL_AND_STROKE
        mPaintCover.setStrokeCap(Paint.Cap.ROUND);
        //设置画笔的结合方式
        mPaintCover.setStrokeJoin(Paint.Join.ROUND);

        setDrawingCacheEnabled(true);
        rect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rect.set(ScreenUtil.dp2px(getContext(), 2), ScreenUtil.dp2px(getContext(), 2), getWidth() - ScreenUtil.dp2px(getContext(), 2), getHeight() - ScreenUtil.dp2px(getContext(), 2));
        canvas.drawRoundRect(rect, ScreenUtil.dp2px(getContext(), 10), ScreenUtil.dp2px(getContext(), 10), mPathPaint);
    }

    public void setPen(boolean isPen) {
        this.isPen = isPen;
    }

    private void down(float x, float y) {

    }

    private void move(float x, float y) {

    }

    private void up(float x, float y) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                down(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                move(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                up(event.getX(), event.getY());
                break;
        }
        return false;
    }
}

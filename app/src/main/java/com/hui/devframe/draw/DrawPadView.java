package com.hui.devframe.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawPadView extends View {

    private Paint mPathPaint;
    private Paint mCoverPaint;

    // 路径数据
    public SerializePath mPath;
    public ArrayList<PathObject> paths = new ArrayList<>();

    // 是否在写
    public boolean isDown = false;
    // 上一次绘制位置
    private float mLastX;
    private float mLastY;

    private boolean isCover = false;

    public void setPenCover(boolean isCover) {
        this.isCover = isCover;
    }

    public DrawPadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCoverPaint = new Paint();
        mCoverPaint.setAntiAlias(true);
        mCoverPaint.setStyle(Style.STROKE);
        mCoverPaint.setStrokeWidth(10);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mCoverPaint.setXfermode(mode);
        mCoverPaint.setFilterBitmap(false);

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Style.STROKE);
        mPathPaint.setStrokeWidth(10);
        mPathPaint.setFilterBitmap(false);

        setDrawingCacheEnabled(true);

        mPath = new SerializePath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        // 绘制笔迹
        for (PathObject p : paths) {
            if (!p.path.actions.isEmpty()) {
                if (p.isCover) {
                    canvas.drawPath(p.path, mCoverPaint);
                } else {
                    canvas.drawPath(p.path, mPathPaint);
                }
            }
        }
        if (isCover) {
            canvas.drawPath(mPath, mCoverPaint);
        } else {
            canvas.drawPath(mPath, mPathPaint);
        }
        canvas.restoreToCount(sc);
    }

    /**
     * 开始绘制路径
     */
    public void startDraw(int x, int y) {
        isDown = true;
        mPath.reset();
        mPath.moveTo(x, y);
        mLastX = x;
        mLastY = y;
        postInvalidate();
    }

    /**
     * 移动绘制路径
     */
    public void moveDraw(int x, int y) {
        // 如果未开始则自动开始绘制
        if (!isDown) {
            isDown = true;
            mPath.moveTo(x, y);
        }

        float dx = Math.abs(x - mLastX);
        float dy = Math.abs(y - mLastY);

        if (dx >= 2 || dy >= 2) {
            mPath.quadTo(mLastX, mLastY,
                    (x + mLastX) / 2, (y + mLastY) / 2);
            mLastX = x;
            mLastY = y;
        }
        postInvalidate();
    }

    /**
     * 路径绘制结束
     */
    public void upDraw(float new_x, float new_y) {
        if (!isDown) {
            return;
        }
        mPath.lineTo(new_x, new_y);
        mLastX = new_x;
        mLastY = new_y;
        paths.add(new PathObject(mPath, isCover, mPathPaint.getStrokeWidth()));

        mPath = new SerializePath();
        mPath.moveTo(new_x, new_y);
        isDown = false;
        postInvalidate();
    }

    // 笔迹对象
    class PathObject {
        SerializePath path;
        boolean isCover;
        float strokeWidth;

        public PathObject(SerializePath path, boolean isCover, float strokeWidth) {
            this.path = path;
            this.isCover = isCover;
            this.strokeWidth = strokeWidth;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDraw((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                moveDraw((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                upDraw((int) event.getX(), (int) event.getY());
                break;
        }
        return true;
    }
}

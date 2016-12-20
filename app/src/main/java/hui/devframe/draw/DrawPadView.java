package hui.devframe.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import hui.devframe.R;

public class DrawPadView extends View {

    private int width;
    private int height;

    private Bitmap mBackBitmap;
    private Bitmap mDrawBitmap;

    private Paint mBitmapPaint;
    private Paint mPathPaint;
    private Paint mCoverPaint;

    private Canvas mCanvas;
    private Matrix matrix;

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


        // 设置笔画刷参数
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setStyle(Style.FILL);
        mBitmapPaint.setStrokeJoin(Paint.Join.ROUND);
        mBitmapPaint.setStrokeCap(Paint.Cap.ROUND);

        mCoverPaint = new Paint();
        mCoverPaint.setAntiAlias(true);
        mCoverPaint.setStyle(Style.STROKE);
        mCoverPaint.setStrokeJoin(Paint.Join.ROUND);
        mCoverPaint.setStrokeCap(Paint.Cap.ROUND);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mCoverPaint.setXfermode(mode);
        mCoverPaint.setColor(Color.GRAY);
        mCoverPaint.setStrokeWidth(3);

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Style.STROKE);
        mPathPaint.setColor(Color.BLACK);
        mPathPaint.setStrokeWidth(3);
        mPathPaint.setTextSize(85);

        setDrawingCacheEnabled(true);
        mBackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_big);
        matrix = new Matrix();

        mDrawBitmap = Bitmap.createBitmap(1080, 1200, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mDrawBitmap);
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        Log.e("eeeeeeeeeeeee",isHardwareAccelerated() + "");
        Log.e("eeeeeeeeeeeee",mCanvas.isHardwareAccelerated() + "");

        mPath = new SerializePath();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        matrix.reset();
        matrix.postScale((float) width / mBackBitmap.getWidth(), (float) height / mBackBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawBitmap(mBackBitmap,matrix,null);
        canvas.drawBitmap(mDrawBitmap, 0, 0, mPathPaint);

        canvas.drawCircle(300, 300, 100, mPathPaint);
        mCanvas.drawCircle(450, 300, 100, mPathPaint);

        canvas.drawText("这是测试文字",300, 600, mPathPaint);
        mCanvas.drawText("这是测试文字",300, 800, mPathPaint);

        // 绘制笔迹
        for (PathObject p : paths) {
            if (!p.path.actions.isEmpty()) {
                if (p.isCover) {
                    mCanvas.drawPath(p.path, mCoverPaint);
                } else {
                    mCanvas.drawPath(p.path, mPathPaint);
                }
            }
        }
        if (isCover) {
            mCanvas.drawPath(mPath, mCoverPaint);
        } else {
            mCanvas.drawPath(mPath, mPathPaint);
        }
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

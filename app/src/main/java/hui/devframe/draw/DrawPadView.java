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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import hui.devframe.R;

public class DrawPadView extends View {

    // 绘制图片
    private Paint mBitmapPaint;
    private Bitmap mBackBitmap;

    // 绘制路径
    private Paint mPathPaint;
    private Paint mCoverPaint;

    // 路径数据
    public SerializePath mPath;
    public ArrayList<PathObject> paths = new ArrayList<>();

    // 是否在写
    public boolean isDown = false;
    // 是否显示画笔
    private boolean isShowPen = false;
    // 上一次绘制位置
    private float mLastX;
    private float mLastY;
    // 缩放比例
    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;

    private boolean isCover = false;

    public void setPenCover(boolean isCover) {
        this.isCover = isCover;
    }

    /**
     * 清除内容
     */
    public void resetCanvas() {
        paths.clear();
        mPath = new SerializePath();
        postInvalidate();
        isDown = false;
    }

    /**
     * 清理资源
     */
    public void clear() {
        if (mBackBitmap != null) {
            mBackBitmap.recycle();
            mBackBitmap = null;
        }
        paths.clear();
    }

    public DrawPadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialPaint();
        initialBitmap();
    }

    private void initialPaint() {
        // 设置笔画刷参数
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setStyle(Style.FILL);
        mBitmapPaint.setStrokeJoin(Paint.Join.ROUND);
        mBitmapPaint.setStrokeCap(Paint.Cap.ROUND);

        mCoverPaint = new Paint();
        mCoverPaint.setAntiAlias(true);
        mCoverPaint.setStyle(Style.FILL);
        mCoverPaint.setStrokeJoin(Paint.Join.ROUND);
        mCoverPaint.setStrokeCap(Paint.Cap.ROUND);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.XOR);
        mCoverPaint.setXfermode(mode);
        mCoverPaint.setColor(Color.GRAY);
        mCoverPaint.setStrokeWidth(5);

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setStyle(Style.STROKE);
        mPathPaint.setColor(Color.parseColor("#000000"));
        mPathPaint.setStrokeWidth(5);

        setDrawingCacheEnabled(true);
        mPath = new SerializePath();
    }

    private void initialBitmap() {
        mBackBitmap = createSizeImage(BitmapFactory.decodeResource(getResources(), R.drawable.test_big), 1080, 1080);
    }

    private Bitmap createSizeImage(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width == newWidth && height == newHeight) {
            return bitmap;
        } else {
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            return Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    matrix, true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBackBitmap, 0, 0, mPathPaint);
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
    }

    /**
     * 开始绘制路径
     */
    public void startDraw(int x, int y) {
        float new_x = x * mScaleX;
        float new_y = y * mScaleY;

        isDown = true;
        mPath.reset();
        mPath.moveTo(new_x, new_y);
        mLastX = new_x;
        mLastY = new_y;
        isShowPen = true;
        postInvalidate();
    }

    /**
     * 移动绘制路径
     */
    public void moveDraw(int x, int y) {
        float new_x = x * mScaleX;
        float new_y = y * mScaleY;

        // 如果未开始则自动开始绘制
        if (!isDown) {
            isDown = true;
            mPath.moveTo(new_x, new_y);
        }

        float dx = Math.abs(new_x - mLastX);
        float dy = Math.abs(new_y - mLastY);

        if (dx >= 2 || dy >= 2) {
            mPath.quadTo(mLastX, mLastY,
                    (new_x + mLastX) / 2, (new_y + mLastY) / 2);
            mLastX = new_x;
            mLastY = new_y;
        }
        isShowPen = true;
        postInvalidate();
    }

    /**
     * 路径绘制结束
     */
    public void upDraw(float x, float y) {
        if (!isDown) {
            return;
        }
        float new_x = x * mScaleX;
        float new_y = y * mScaleY;
        mPath.lineTo(new_x, new_y);
        mLastX = new_x;
        mLastY = new_y;
        paths.add(new PathObject(mPath, isCover, mPathPaint.getStrokeWidth()));

        mPath = new SerializePath();
        mPath.moveTo(new_x, new_y);
        isDown = false;
        isShowPen = false;
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

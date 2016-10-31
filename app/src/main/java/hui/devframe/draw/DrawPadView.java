package hui.devframe.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import hui.devframe.R;
import hui.devframe.util.ScreenUtil;

public class DrawPadView extends View {

    // 页码
    public int mPageIndex;

    // 绘制图片
    private Paint mBitmapPaint, mBitmapHalfPaint;
    private Bitmap mPenBlackBitmap;
    private Bitmap mPenRedBitmap;
    private Bitmap mPenBlueBitmap;
    private Bitmap mEraseBitmap;

    // 绘制路径
    private Paint mPathPaint;
    private Paint mTempPaint;
    private PaintType mPaintType;

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

    /**
     * 设置页码
     */
    public void setPageIndex(int mIndex) {
        mPageIndex = mIndex;
    }

    /**
     * 设置缩放比例
     */
    public void setScale(int teacherWidth, int teacherHeight, int studentWidth, int studentHeight) {
        mScaleX = studentWidth * 1.0f / teacherWidth;
        mScaleY = studentHeight * 1.0f / teacherHeight;
    }

    /**
     * 设置画笔
     */
    public void setPaintPen(float width, PaintType type) {
        mPaintType = type;
        mPathPaint.setColor(type.color);
        if (type == PaintType.Erase) {
            mPathPaint.setStrokeWidth(ScreenUtil.getScreenWidth() > 1080 ? 40.0f :
                    (ScreenUtil.getScreenWidth() >= 720 ? 20.0f : 10.0f));
        } else {
            mPathPaint.setStrokeWidth(ScreenUtil.getScreenWidth() > 720 ? 2.0f : 1.0f);
        }
    }

    /**
     * 设置不显示画笔
     */
    public void dismissPen() {
        isShowPen = false;
        postInvalidate();
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
     * 把画板保存到文件
     */
    public boolean save(File file) {
        if (file == null) {
            return false;
        } else {
            if (file.exists()) {
                file.delete();
            }

            try {
                long temp = System.currentTimeMillis();
                if (!isDrawingCacheEnabled()) {
                    setDrawingCacheEnabled(true);
                }
                destroyDrawingCache();
                buildDrawingCache();
                if (getDrawingCache() != null) {
                    FileOutputStream out = new FileOutputStream(file);
                    getDrawingCache().compress(Bitmap.CompressFormat.PNG, 70, out);
                    out.close();
                } else {
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * 清理资源
     */
    public void clear() {
        if (mPenBlackBitmap != null) {
            mPenBlackBitmap.recycle();
            mPenBlackBitmap = null;
        }
        if (mEraseBitmap != null) {
            mEraseBitmap.recycle();
            mEraseBitmap = null;
        }
        if (mPenRedBitmap != null) {
            mPenRedBitmap.recycle();
            mPenRedBitmap = null;
        }
        if (mPenBlueBitmap != null) {
            mPenBlueBitmap.recycle();
            mPenBlueBitmap = null;
        }
        paths.clear();
    }

    public DrawPadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialPaint();
        initialBitmap();

        mPageIndex = 0;
    }

    private void initialPaint() {
        // 设置笔画刷参数
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setStyle(Style.FILL);
        mBitmapPaint.setStrokeJoin(Paint.Join.ROUND);
        mBitmapPaint.setStrokeCap(Paint.Cap.ROUND);

        mBitmapHalfPaint = new Paint();
        mBitmapHalfPaint.setAntiAlias(true);
        mBitmapHalfPaint.setStyle(Style.FILL);
        mBitmapHalfPaint.setStrokeJoin(Paint.Join.ROUND);
        mBitmapHalfPaint.setStrokeCap(Paint.Cap.ROUND);
        mBitmapHalfPaint.setColor(Color.BLUE);
        mBitmapHalfPaint.setAlpha(0x30);

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setStyle(Style.STROKE);

        mTempPaint = new Paint();
        mTempPaint.setAntiAlias(true);
        mTempPaint.setStrokeCap(Paint.Cap.ROUND);
        mTempPaint.setStrokeJoin(Paint.Join.ROUND);
        mTempPaint.setStyle(Style.STROKE);

        setDrawingCacheEnabled(true);
        mPath = new SerializePath();
        mPaintType = PaintType.Black;
    }

    private void initialBitmap() {
        int width = ScreenUtil.dp2px(11);
        int height = ScreenUtil.dp2px(11);
        mEraseBitmap = createSizeImage(BitmapFactory.decodeResource(getResources(), R.drawable.canvas_view_earse_bg), ScreenUtil.dp2px(15), ScreenUtil.dp2px(14));
        mPenBlackBitmap = createSizeImage(BitmapFactory.decodeResource(getResources(), R.drawable.canvas_view_pen_bg), width, height);
        mPenRedBitmap = createSizeImage(BitmapFactory.decodeResource(getResources(), R.drawable.canvas_view_pen_red_bg), width, height);
        mPenBlueBitmap = createSizeImage(BitmapFactory.decodeResource(getResources(), R.drawable.canvas_view_pen_blue_bg), width, height);
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

        // 绘制笔迹
        for (PathObject p : paths) {
            if (!p.path.actions.isEmpty()) {
                mTempPaint.setColor(p.painttype.color);
                mTempPaint.setStrokeWidth(p.strokeWidth);
                canvas.drawPath(p.path, mTempPaint);
            }
        }
        canvas.drawPath(mPath, mPathPaint);

        // 绘制画笔
        if (!isShowPen) {
            return;
        }
        switch (mPaintType) {
            case Erase:
                if (mEraseBitmap != null && !mEraseBitmap.isRecycled()) {
                    canvas.drawBitmap(mEraseBitmap, mLastX - 10, mLastY - 10, mBitmapPaint);
                }
                break;
            case Black:
                if (mPenBlackBitmap != null && !mPenBlackBitmap.isRecycled()) {
                    canvas.drawBitmap(mPenBlackBitmap, mLastX - 2, mLastY - mPenBlackBitmap.getHeight(), !isDown ? mBitmapHalfPaint : mBitmapPaint);
                }
                break;
            case Red:
                if (mPenRedBitmap != null && !mPenRedBitmap.isRecycled()) {
                    canvas.drawBitmap(mPenRedBitmap, mLastX - 2, mLastY - mPenRedBitmap.getHeight(), !isDown ? mBitmapHalfPaint : mBitmapPaint);
                }
                break;
            case Blue:
                if (mPenBlueBitmap != null && !mPenBlueBitmap.isRecycled()) {
                    canvas.drawBitmap(mPenBlueBitmap, mLastX - 2, mLastY - mPenBlueBitmap.getHeight(), !isDown ? mBitmapHalfPaint : mBitmapPaint);
                break;
            }
        }
    }

    /**
     * 开始绘制路径
     */
    public void startDraw(int x, int y, boolean invalidate) {
        float new_x = x * mScaleX;
        float new_y = y * mScaleY;

        isDown = true;
        mPath.reset();
        mPath.moveTo(new_x, new_y);
        mLastX = new_x;
        mLastY = new_y;
        isShowPen = true;
        if (invalidate) {
            postInvalidate();
        }
    }

    /**
     * 移动绘制路径
     */
    public void moveDraw(int x, int y, boolean invalidate) {
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
        if (invalidate) {
            postInvalidate();
        }
    }

    /**
     * 路径绘制结束
     */
    public void upDraw(float x, float y, boolean invalidate) {
        upDraw(x, y, true, invalidate);
    }

    /**
     * 用最后的点结束绘制
     */
    public void upDraw(boolean invalidate) {
        paths.add(new PathObject(mPath, mPaintType, mPathPaint.getStrokeWidth()));
        mPath = new SerializePath();
        mPath.moveTo(mLastX, mLastX);
        isDown = false;
        isShowPen = false;
    }

    /**
     * 路径绘制结束
     */
    public void upDraw(float x, float y, boolean lineto, boolean invalidate) {
        if (!isDown) {
            return;
        }
        float new_x = x * mScaleX;
        float new_y = y * mScaleY;
        if (lineto) {
            mPath.lineTo(new_x, new_y);
        }
        mLastX = new_x;
        mLastY = new_y;
        paths.add(new PathObject(mPath, mPaintType, mPathPaint.getStrokeWidth()));

        mPath = new SerializePath();
        mPath.moveTo(new_x, new_y);
        isDown = false;
        isShowPen = false;
        if (invalidate) {
            postInvalidate();
        }
    }

    // 笔迹对象
    class PathObject {
        SerializePath path;
        PaintType painttype;
        float strokeWidth;

        public PathObject(SerializePath path, PaintType painttype, float strokeWidth) {
            this.path = path;
            this.painttype = painttype;
            this.strokeWidth = strokeWidth;
        }
    }

    // 绘图类型
    public enum PaintType {
        Erase(0, Color.parseColor("#ffffff")), Black(1, Color.parseColor("#000000")), Red(2, Color.parseColor("#ff0000")), Blue(3, Color.parseColor("#0077FF"));

        private int index;
        private int color;

        PaintType(int index, int color) {
            this.index = index;
            this.color = color;
        }

        public static PaintType getPaintType(int index) {
            for (PaintType t : PaintType.values()) {
                if (t.index == index) {
                    return t;
                }
            }
            return null;
        }
    }
}

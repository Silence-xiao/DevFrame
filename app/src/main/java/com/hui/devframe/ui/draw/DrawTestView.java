package com.hui.devframe.ui.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class DrawTestView extends View {

    private Bitmap mDrawBitmap;

    private Paint mPathPaint;
    private Paint mCoverPaint;

    private Canvas mCanvas;

    Path pen = new Path();
    Path clear = new Path();

    public DrawTestView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCoverPaint = new Paint();
        mCoverPaint.setAntiAlias(true);
        mCoverPaint.setStyle(Style.STROKE);
        mCoverPaint.setStrokeWidth(40);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mCoverPaint.setXfermode(mode);
        mCoverPaint.setFilterBitmap(false);

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Style.STROKE);
        mPathPaint.setStrokeWidth(40);
        mPathPaint.setFilterBitmap(false);

        mDrawBitmap = Bitmap.createBitmap(1080, 1200, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mDrawBitmap);
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        pen.moveTo(50, 50);
        pen.lineTo(500, 500);
        pen.lineTo(0, 500);

        clear.moveTo(500, 500);
        clear.lineTo(400, 400);
        clear.lineTo(0, 600);
    }


    static Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, w*3/4, h*3/4), p);
        return bm;
    }

    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
        c.drawRect(w/3, h/3, w*19/20, h*19/20, p);
        return bm;
    }

    int W = 400;
    int H = 400;
    Bitmap src = makeSrc(W,H);// 蓝色
    Bitmap dst = makeDst(W,H);// 黄色

    @Override
    protected void onDraw(Canvas canvas) {
        int sc = canvas.saveLayer(0, 0, 500, 500, null,Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        canvas.drawPath(pen, mPathPaint);
        canvas.drawPath(clear, mCoverPaint);
        canvas.restoreToCount(sc);
//        canvas.drawBitmap(dst, 0, 0, mPathPaint);
//        canvas.drawBitmap(src, 0, 0, mCoverPaint);
//        canvas.drawBitmap(src, 0, 400, mPathPaint);
    }
}

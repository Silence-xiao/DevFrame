package com.hui.devframe.ui.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by wanghui on 2017/7/14.
 */
public class ImageUtil {

    /**
     * 图片灰度化
     */
    public static class GrayBitmapTransformer {
        public Bitmap transform(Bitmap inBitmap) {
            Bitmap output = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);

            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setDither(true);
            paint.setColorFilter(f);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(inBitmap, 0, 0, paint);
            inBitmap.recycle();
            return output;
        }
    }
}
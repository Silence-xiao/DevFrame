/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hui.devframe.activity.learn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class XfermodesActivity extends Activity {

    // create a bitmap with a circle, used for the "dst" image
    static Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, w * 3 / 4, h * 3 / 4), p);
        return bm;
    }

    // create a bitmap with a rect, used for the "src" image
    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
        c.drawRect(w / 3, h / 3, w * 19 / 20, h * 19 / 20, p);
        return bm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SampleView(this));


    }


    private static class SampleView extends View {
        private static final int W = 200;
        private static final int H = 300;
        private static final int ROW_MAX = 4;   // number of samples per row
        private static final Xfermode[] sModes = {
                new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
                new PorterDuffXfermode(PorterDuff.Mode.SRC),
                new PorterDuffXfermode(PorterDuff.Mode.DST),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
                new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
                new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
                new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
                new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
                new PorterDuffXfermode(PorterDuff.Mode.XOR),
                new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
                new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
                new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
                new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
        };
        private static final String[] sLabels = {
                "Clear", "Src", "Dst", "SrcOver",
                "DstOver", "SrcIn", "DstIn", "SrcOut",
                "DstOut", "SrcATop", "DstATop", "Xor",
                "Darken", "Lighten", "Multiply", "Screen"
        };
        private Bitmap mSrcB;
        private Bitmap mDstB;
        private Shader mBG;     // background checker-board pattern

        public SampleView(Context context) {
            super(context);

            mSrcB = makeSrc(W, H);
            mDstB = makeDst(W, H);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Log.e("eeeeeeeeeeeee", isHardwareAccelerated() + "");
            Log.e("eeeeeeeeeeeee", canvas.isHardwareAccelerated() + "");
            canvas.drawColor(Color.WHITE);

            Paint paint = new Paint();
            paint.setFilterBitmap(false);

            canvas.translate(100, 100);

            int x = 0;
            int y = 0;

            // draw the border

            paint.setStyle(Paint.Style.FILL);

            // draw the src/dst example into our offscreen bitmap
            int sc = canvas.saveLayer(x, y, x + W, y + H, null,
                    Canvas.MATRIX_SAVE_FLAG |
                            Canvas.CLIP_SAVE_FLAG |
                            Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                            Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                            Canvas.CLIP_TO_LAYER_SAVE_FLAG);

            Log.e("eeeeeeeeeeeee", isHardwareAccelerated() + "");
            Log.e("eeeeeeeeeeeee", canvas.isHardwareAccelerated() + "");
            canvas.translate(x, y);
            canvas.drawBitmap(mDstB, 0, 0, paint);
            paint.setXfermode(sModes[0]);
            canvas.drawBitmap(mSrcB, 0, 0, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(sc);
        }
    }
}


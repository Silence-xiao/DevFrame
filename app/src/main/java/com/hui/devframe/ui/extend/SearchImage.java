package com.hui.devframe.ui.extend;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自动问答拍照后的图片，超过后会从上往下剪切。
 */
public class SearchImage extends android.support.v7.widget.AppCompatImageView {
    public SearchImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    public SearchImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    public SearchImage(Context context) {
        super(context);
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int frameLeft, int frameTop, int frameRight, int frameBottom) {
        //LogcatHelper.e("SearchImage.setFrame " + "frameLeft = [" + frameLeft + "], frameTop = [" + frameTop + "], frameRight = [" + frameRight + "], frameBottom = [" + frameBottom + "]");
        float frameWidth = frameRight - frameLeft;
        float frameHeight = frameBottom - frameTop;

        if (getDrawable() != null) {
            float originalImageWidth = (float) getDrawable().getIntrinsicWidth();
            float originalImageHeight = (float) getDrawable().getIntrinsicHeight();

            float usedScaleFactor = 1;

            if ((frameWidth > originalImageWidth) || (frameHeight > originalImageHeight)) {

                float fitHorizontallyScaleFactor = frameWidth / originalImageWidth;
                float fitVerticallyScaleFactor = frameHeight / originalImageHeight;

                usedScaleFactor = Math.max(fitHorizontallyScaleFactor, fitVerticallyScaleFactor);
            }

            float newImageWidth = originalImageWidth * usedScaleFactor;

            Matrix matrix = getImageMatrix();
            matrix.setScale(usedScaleFactor, usedScaleFactor, 0, 0); // Replaces the old matrix completly

            //TOP_CROP
            matrix.postTranslate((frameWidth - newImageWidth) / 2, 0);
            setImageMatrix(matrix);
        }
        return super.setFrame(frameLeft, frameTop, frameRight, frameBottom);
    }
}
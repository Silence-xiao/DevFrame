package com.hui.devframe.common.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * 生成二维码
 * Created by wanghui on 15/12/1.
 */
public class QRCodeUtil {

    /**
     * 创建带有图片的二维码，注意logo图片要小一些，否则可能扫不出来
     *
     * @param string      文本信息
     * @param mLogoBitmap logo图片
     * @param mLogoSize   logo图片尺寸
     * @param qWidth      生成二维码的宽
     * @param qHeight     生成二维码的高
     * @return 返回生成的二维码
     */
    public Bitmap createCode(String string, Bitmap mLogoBitmap, int mLogoSize, int qWidth, int qHeight) {
        // 缩放logo图片至要求大小
        int mHalfLogoSize = (int) (mLogoSize * 1.0 / 2);
        Matrix m = new Matrix();
        float sx = (float) 2 * mHalfLogoSize / mLogoBitmap.getWidth();
        float sy = (float) 2 * mHalfLogoSize / mLogoBitmap.getHeight();
        m.setScale(sx, sy);
        mLogoBitmap = Bitmap.createBitmap(mLogoBitmap, 0, 0, mLogoBitmap.getWidth(), mLogoBitmap.getHeight(), m, false);

        // 计算二维码信息
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix;
        try {
            matrix = writer.encode(string, BarcodeFormat.QR_CODE, qWidth, qHeight, hst);
        } catch (WriterException e) {
            return null;
        }

        // 输出像素信息
        int halfW = qWidth / 2;
        int halfH = qHeight / 2;
        int[] pixels = new int[qWidth * qHeight];
        for (int y = 0; y < qHeight; y++) {
            for (int x = 0; x < qWidth; x++) {
                // 有图片位置记录图片像素信息，其余位置为黑块
                if (x > halfW - mHalfLogoSize && x < halfW + mHalfLogoSize && y > halfH - mHalfLogoSize && y < halfH + mHalfLogoSize) {
                    pixels[y * qWidth + x] = mLogoBitmap.getPixel(x - halfW + mHalfLogoSize, y - halfH + mHalfLogoSize);
                } else {
                    if (matrix.get(x, y)) {
                        pixels[y * qWidth + x] = 0xff000000;
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(qWidth, qHeight,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, qWidth, 0, 0, qWidth, qHeight);
        return bitmap;
    }

    public Bitmap createQRBitmap(int qWidth, int qHeight, String text) {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, qWidth, qHeight, hints);
            int[] pixels = new int[qWidth * qHeight];
            for (int y = 0; y < qHeight; y++) {
                for (int x = 0; x < qWidth; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * qWidth + x] = 0xff000000;
                    } else {
                        pixels[y * qWidth + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(qWidth, qHeight,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, qWidth, 0, 0, qWidth, qHeight);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}

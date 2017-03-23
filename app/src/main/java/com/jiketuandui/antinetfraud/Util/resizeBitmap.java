package com.jiketuandui.antinetfraud.Util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Notzuonotdied on 2016/8/18.
 * <p/>
 * 调整Bitmap的大小
 */
public class resizeBitmap {

    private static float size;

    /**
     * 修改Bitmap的大小
     *
     * @param bitmap 要修改的图片
     * @param resize 放大或者缩小的尺寸
     */
    public static Bitmap resizeBmap(Bitmap bitmap, float resize) {
        Matrix matrix = new Matrix();
        matrix.postScale(resize, resize);
//        while ((Math.max(bitmap.getWidth() / resize, bitmap.getHeight() / resize)) > 10) {
//            resize *= 2;
//            if ((Math.min(bitmap.getWidth() / resize, bitmap.getHeight() / resize)) < 5) {
//                resize /= 2;
//                break;
//            }
//        }
        size = resize;
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    public static float getSize() {
        return size;
    }

    public static void setSize(float resize) {
        size = resize;
    }
}

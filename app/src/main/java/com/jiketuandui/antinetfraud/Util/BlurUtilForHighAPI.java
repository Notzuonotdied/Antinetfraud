package com.jiketuandui.antinetfraud.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by Notzuonotdied on 2016/8/27.
 *
 * 高版本的毛玻璃效果
 */
public class BlurUtilForHighAPI {

    //缩放系数
    public final static int SCALE = 18;

    public static Bitmap Highblur(Bitmap sentBitmap, float radius, Context context) {
        if(sentBitmap==null) {
            return null;
        }
        Bitmap bitmap = sentBitmap.createScaledBitmap(
                sentBitmap, sentBitmap.getWidth() / SCALE,
                sentBitmap.getHeight() / SCALE, false);
        if (radius <= 0.f || radius > 25.f) {
            radius = 25f;
        }
        if (radius <= 6 && Build.VERSION.SDK_INT > 16) {
            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, bitmap,
                    Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur scriptIntrinsicBlur =
                    ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            scriptIntrinsicBlur.setRadius(radius);
            scriptIntrinsicBlur.setInput(input);
            scriptIntrinsicBlur.forEach(output);
            output.copyTo(bitmap);
        } else {
            bitmap = BlurUtil.fastblur(sentBitmap, 66);
        }
        return bitmap;
    }
}

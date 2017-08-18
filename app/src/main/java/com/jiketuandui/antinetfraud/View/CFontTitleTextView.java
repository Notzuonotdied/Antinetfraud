package com.jiketuandui.antinetfraud.View;

import android.content.Context;
import android.graphics.EmbossMaskFilter;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Notzuonotdied on 2016/8/30.
 * 自定义设置字体类型的TextView
 */
public class CFontTitleTextView extends android.support.v7.widget.AppCompatTextView {

    public CFontTitleTextView(Context context) {
        super(context);
        //设置光源的方向
        float[] direction = new float[]{ 1, 1, 1 };
        //设置环境光亮度
        float light = 0.4f;
        //选择要应用的反射等级
        float specular = 18;
        //向mask应用一定级别的模糊
        float blur = 6.0f;
        EmbossMaskFilter maskfilter=new EmbossMaskFilter(direction,light,specular,blur);
        this.getPaint().setMaskFilter(maskfilter);
    }

    public CFontTitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTypeface(Typeface tf) {
        setTypeface(tf,Typeface.NORMAL);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/bamboo_t.ttf"));
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/bamboo_t.ttf"));
        }
    }
}

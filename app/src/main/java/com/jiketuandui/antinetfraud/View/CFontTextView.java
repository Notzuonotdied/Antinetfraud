package com.jiketuandui.antinetfraud.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Notzuonotdied on 2016/8/30.
 * 自定义设置字体类型的TextView
 */
public class CFontTextView extends android.support.v7.widget.AppCompatTextView {

    public CFontTextView(Context context) {
        super(context);
    }

    public CFontTextView(Context context, AttributeSet attrs) {
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

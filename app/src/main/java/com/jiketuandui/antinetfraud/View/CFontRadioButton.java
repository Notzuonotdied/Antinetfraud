package com.jiketuandui.antinetfraud.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by Notzuonotdied on 2016/8/30.
 * 自定义一个TextView用于设置字体
 */
public class CFontRadioButton extends android.support.v7.widget.AppCompatRadioButton {

    public CFontRadioButton(Context context) {
        super(context);
    }

    public CFontRadioButton(Context context, AttributeSet attrs) {
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
                    "fonts/IndicatorFont.ttf"));
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/IndicatorFont.ttf"));
        }
    }
}

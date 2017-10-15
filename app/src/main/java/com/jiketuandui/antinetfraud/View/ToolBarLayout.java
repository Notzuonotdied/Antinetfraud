package com.jiketuandui.antinetfraud.View;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiketuandui.antinetfraud.R;

/**
 * Created by wangyu on 17-10-14.
 * 自定义ToolBar。
 */

public class ToolBarLayout extends LinearLayout {

    private CFontTitleTextView title;

    public ToolBarLayout(Context context) {
        this(context, null);
    }

    public ToolBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context)
                .inflate(R.layout.toolbar_layout, this, true);
        title = (CFontTitleTextView) findViewById(R.id.TitleTextView);
        title.setOnClickListener(v -> YoYo.with(Techniques.Swing)
                .duration(333)
                .repeat(6)
                .playOn(v.findViewById(R.id.TitleTextView)));
        initAttr(context, attrs);
    }

    public void setText(String text) {
        this.title.setText(text);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        this.setOrientation(LinearLayout.HORIZONTAL);

        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.ToolBarLayout);
        try {
            String text = typedArray.getString(R.styleable.ToolBarLayout_text);
            if (!TextUtils.isEmpty(text)) {
                title.setText(text);
            }
        } finally {
            typedArray.recycle();
        }


        findViewById(R.id.back).setOnClickListener(
                v -> ((Activity) context).finish());
    }

}

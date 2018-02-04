package com.jiketuandui.antinetfraud.activity.setting;

import android.app.Activity;
import android.os.Bundle;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.StatusBarUtil;

/**
 * 关于
 *
 * @author wangyu
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        StatusBarUtil.StatusBarLightMode(this);
    }
}

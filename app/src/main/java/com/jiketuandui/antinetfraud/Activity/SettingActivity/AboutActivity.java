package com.jiketuandui.antinetfraud.Activity.SettingActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.jiketuandui.antinetfraud.R;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        FrameLayout tagsback = (FrameLayout) findViewById(R.id.tags_back);
        tagsback.setOnClickListener(view -> finish());
    }
}

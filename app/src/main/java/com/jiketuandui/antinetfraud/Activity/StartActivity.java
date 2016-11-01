package com.jiketuandui.antinetfraud.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;

/**
 * Created by Notzuonotdied on 2016/8/9.
 * 这个是软件启动时候,显示动画的Activity
 */
public class StartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.activity_start,null);
        /**
         * 自定义启动页面的特效
         * AlphaAnimation(float fromAlpha, float toAlpha)
         * 透明度从0.3f渐变到1.0f
         * */
        AlphaAnimation animation = new AlphaAnimation(0.4f,9.0f);
        /**
         * 持续时间
         * */
//        animation.setDuration(2888);
        animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override// 效果结束的时候
            public void onAnimationEnd(Animation animation) {

                gotoMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(animation);
        setContentView(view);
    }

    /**
     * 进入MainAcitvity
     * */
    private void gotoMainActivity() {
        ((MyApplication)getApplication()).initNETService();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}

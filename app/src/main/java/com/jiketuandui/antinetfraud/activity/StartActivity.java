package com.jiketuandui.antinetfraud.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jiketuandui.antinetfraud.activity.main.MainActivity;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;

/**
 * 这个是软件启动时候,显示动画的Activity
 *
 * @author Notzuonotdied
 * @date 2016/8/9
 */
public class StartActivity extends Activity {
    private static final String TAG = StartActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(
                R.layout.activity_start, null);

        Animation up2down = AnimationUtils.loadAnimation(this,
                R.anim.uptodown);
        view.findViewById(R.id.startIMG).setAnimation(up2down);
        Animation up2top = AnimationUtils.loadAnimation(this,
                R.anim.uptotop);
        view.findViewById(R.id.startTV).setAnimation(up2top);
        /* *
         * 自定义启动页面的特效
         * AlphaAnimation(float fromAlpha, float toAlpha)
         * 透明度从0.3f渐变到1.0f
         * */
        AlphaAnimation animation = new AlphaAnimation(0.4f, 1.0f);
        // 持续时间
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
     * 进入MainActivity
     */
    private void gotoMainActivity() {
        MyApplication.getInstance().recoverData();
        startActivity(new Intent(this, MainActivity.class));
    }
}

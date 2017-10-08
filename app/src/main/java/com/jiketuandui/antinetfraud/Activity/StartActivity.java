package com.jiketuandui.antinetfraud.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.jiketuandui.antinetfraud.Activity.MainActivity.MainActivity;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.SharedPManager;

/**
 * Created by Notzuonotdied on 2016/8/9.
 * 这个是软件启动时候,显示动画的Activity
 */
public class StartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(
                R.layout.activity_start, null);
        /* *
         * 自定义启动页面的特效
         * AlphaAnimation(float fromAlpha, float toAlpha)
         * 透明度从0.3f渐变到1.0f
         * */
        AlphaAnimation animation = new AlphaAnimation(0.4f, 9.0f);
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
        new UserLoginTask().execute();
        ((MyApplication) getApplication()).initLoginState();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * 在开始屏幕的时候进行重新登陆一次
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        SharedPManager sharedPManager = new SharedPManager(StartActivity.this);

        @Override
        protected Boolean doInBackground(Void... params) {
            String stringToken = null;
            String stringAccount = null;
            if (sharedPManager.isContains(MyApplication.getInstance().getToken())) {
                stringToken = sharedPManager.getString(MyApplication.getInstance().getToken(), null);
                stringAccount = sharedPManager.getString(MyApplication.getInstance().getUsername(), null);
            }
            return !TextUtils.isEmpty(stringToken) && !TextUtils.isEmpty(stringAccount) &&
                    ((MyApplication) getApplication()).instancepostAccount().postCheckLogin(
                            "token=" + stringToken + "&&username=" + stringAccount +
                                    "&&phone_id=" + ((MyApplication) getApplication()).getMAC());
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean != null && aBoolean) {
                Toast.makeText(StartActivity.this, "登陆成功~", Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setLogin(true);
            }
            super.onPostExecute(aBoolean);
        }
    }
}

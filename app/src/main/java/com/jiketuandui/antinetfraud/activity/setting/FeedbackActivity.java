package com.jiketuandui.antinetfraud.activity.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.StatusBarUtil;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.OtherService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 反馈
 *
 * @author wangyu
 */
public class FeedbackActivity extends Activity {

    private AppCompatEditText editText;
    private AppCompatButton post;
    private OtherService otherService = RetrofitServiceFactory.OTHER_SERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        StatusBarUtil.StatusBarLightMode(this);

        initView();
        initListener();
    }

    private void initListener() {
        post.setOnClickListener(v -> attemptPost());
    }

    private void attemptPost() {
        editText.setError(null);
        View focusView = null;
        boolean cancel = false;

        String what = editText.getText().toString();

        if (TextUtils.isEmpty(what)) {
            editText.setError(getString(R.string.error_feedback_empty));
            focusView = editText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            otherService.feedback(what)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .subscribe(new BaseObserver<>(this, "感谢您的反馈！"));
        }
    }

    private void initView() {
        this.editText = findViewById(R.id.feedback);
        this.post = findViewById(R.id.post);
    }
}

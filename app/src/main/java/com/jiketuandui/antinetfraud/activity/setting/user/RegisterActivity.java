package com.jiketuandui.antinetfraud.activity.setting.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;

import com.blankj.utilcode.util.RegexUtils;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.StatusBarUtil;
import com.jiketuandui.antinetfraud.entity.domain.User;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.UserService;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 注册页面
 *
 * @author wangyu
 */
public class RegisterActivity extends Activity {

    @BindView(R.id.account)
    AppCompatEditText mAccountView;
    @BindView(R.id.password)
    AppCompatEditText mPasswordView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.register)
    AppCompatButton mRegisterButton;
    @BindView(R.id.confirm)
    AppCompatEditText mConfirm;
    @BindView(R.id.email)
    AppCompatEditText mEmail;
    private UserService userService = RetrofitServiceFactory.USER_SERVICE;
    private OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.register:
                attemptLogin();
                break;
            default:
                break;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        StatusBarUtil.StatusBarLightMode(this);

        initListener();
    }

    private void initListener() {
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });
        mRegisterButton.setOnClickListener(listener);
    }

    private void attemptLogin() {
        mAccountView.setError(null);
        mPasswordView.setError(null);
        mConfirm.setError(null);
        mEmail.setError(null);

        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirm = mConfirm.getText().toString();
        String email = mEmail.getText().toString();

        final boolean[] cancel = {false};
        final View[] focusView = {null};
        if (TextUtils.isEmpty(password)) {
            // 密码不能为空
            mPasswordView.setError(getString(R.string.error_password_required));
            focusView[0] = mPasswordView;
            cancel[0] = true;
        } else if (!isPasswordValid(password)) {
            // 密码长度无效
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView[0] = mPasswordView;
            cancel[0] = true;
        }
        if (TextUtils.isEmpty(confirm)) {
            // 密码不能为空
            mConfirm.setError(getString(R.string.error_password_required));
            focusView[0] = mConfirm;
            cancel[0] = true;
        } else if (!TextUtils.equals(confirm, password)) {
            // 两次密码不匹配
            mConfirm.setError(getString(R.string.error_confirm_password));
            focusView[0] = mConfirm;
            cancel[0] = true;
        } else if (!isPasswordValid(confirm)) {
            // 密码长度无效
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView[0] = mPasswordView;
            cancel[0] = true;
        }

        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView[0] = mAccountView;
            cancel[0] = true;
        } else if (!isAccountValid(account)) {
            mAccountView.setError(getString(R.string.error_invalid_account));
            focusView[0] = mAccountView;
            cancel[0] = true;
        } else {
            userService.isNameValid(account)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new BaseObserver<User>(this) {
                        @Override
                        protected void onHandleFailure(String message) {
                            mAccountView.setError(message);
                            focusView[0] = mAccountView;
                            cancel[0] = true;
                        }
                    });
        }

        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_email_required));
            focusView[0] = mEmail;
            cancel[0] = true;
        } else if (!RegexUtils.isEmail(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView[0] = mEmail;
            cancel[0] = true;
        } else {
            userService.isEmailValid(email)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new BaseObserver<User>(this) {
                        @Override
                        protected void onHandleFailure(String message) {
                            mEmail.setError(message);
                            focusView[0] = mEmail;
                            cancel[0] = true;
                        }
                    });
        }

        if (cancel[0]) {
            focusView[0].requestFocus();
        } else {
            userService.register(account, password, confirm, email)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new BaseObserver<User>(this, "注册成功～") {
                        @Override
                        protected void onHandleSuccess(User user) {
                            MyApplication.getInstance().storageData(user);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);
                        }
                    });
        }
    }

    private boolean isAccountValid(String account) {
        return account.length() > 5;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}


package com.jiketuandui.antinetfraud.activity.setting.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.entity.domain.User;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.UserService;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 登陆界面
 *
 * @author wangyu
 * @date 2017年3月6日 20:10:26
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.account)
    AppCompatEditText mAccountView;
    @BindView(R.id.password)
    AppCompatEditText mPasswordView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.sign_in_button)
    AppCompatButton mSignInButton;
    @BindView(R.id.register_in_button)
    AppCompatButton mRegisterButton;
    private UserService userService = RetrofitServiceFactory.USER_SERVICE;
    private OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.register_in_button:
                Intent intent = new Intent();
                intent.setClass(this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.sign_in_button:
                attemptLogin();
                break;
            default:
                break;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initListener();
    }

    private void initListener() {
        mSignInButton.setOnClickListener(listener);
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

        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        } else if (!isAccountValid(account)) {
            mAccountView.setError(getString(R.string.error_invalid_account));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            userService.login(account, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new BaseObserver<User>(this, "登录成功～") {
                        @Override
                        protected void onHandleSuccess(User user) {
                            MyApplication.getInstance().storageData(user);
                            finish();
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


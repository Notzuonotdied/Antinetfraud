package com.jiketuandui.antinetfraud.Activity.UserActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jiketuandui.antinetfraud.Bean.AccountInfo;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.SharedPManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登陆界面
 * 2017年3月6日 20:10:26
 */
public class LoginActivity extends Activity {

    // UI
    @BindView(R.id.account)
    AutoCompleteTextView mAccountView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.sign_in_button)
    Button mSignInButton;
    @BindView(R.id.register_in_button)
    Button mRegisterButton;
    private UserLoginTask mAuthTask = null;
    private AccountInfo mAccountInfo;
    private OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.register_in_button:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.sign_in_button:
                attemptLogin();
                break;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initListener();
        initTagsBack();
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

    // 返回键
    private void initTagsBack() {
        FrameLayout tagsBack = (FrameLayout) findViewById(R.id.back);
        tagsBack.setOnClickListener(view -> finish());
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
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
            showProgress(true);
            mAuthTask = new UserLoginTask(account, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isAccountValid(String account) {
        return account.length() > 5;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 8;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mAccount;
        private final String mPassword;

        UserLoginTask(String account, String password) {
            mAccount = account;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mAccountInfo = ((MyApplication) getApplication()).instancepostAccount().postLogin(
                        "username=" + mAccount + "&&password=" + mPassword + "&&phone_id=" +
                                ((MyApplication) getApplication()).getMAC());
                Thread.sleep(666);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                SharedPManager sharedPManager = new SharedPManager(LoginActivity.this);
                sharedPManager.putString(MyApplication.getInstance().getmToken(), mAccountInfo.getToken());
                sharedPManager.putString(MyApplication.getInstance().getUsername(), mAccountInfo.getUser());
                sharedPManager.putString(MyApplication.getInstance().getUid(), mAccountInfo.getUid());
                sharedPManager.apply();
                Toast.makeText(LoginActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                MyApplication.getInstance().setLogin(true);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}


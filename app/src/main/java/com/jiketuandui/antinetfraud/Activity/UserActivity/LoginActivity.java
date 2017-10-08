package com.jiketuandui.antinetfraud.Activity.UserActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cjj.CircleProgressBar;
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
public class LoginActivity extends AppCompatActivity {

    // UI
    @BindView(R.id.account)
    AutoCompleteTextView mAccountView;
    @BindView(R.id.password)
    AppCompatEditText mPasswordView;
    @BindView(R.id.login_progress)
    CircleProgressBar mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.sign_in_button)
    AppCompatButton mSignInButton;
    @BindView(R.id.register_in_button)
    AppCompatButton mRegisterButton;
    private UserLoginTask mAuthTask = null;
    private AccountInfo mAccountInfo;
    private OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.register_in_button:
                Intent intent = new Intent(this, RegisterActivity.class);
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
            mAuthTask = new UserLoginTask(account, password, this);
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
        private final Context mContext;

        UserLoginTask(String account, String password, Context context) {
            mAccount = account;
            mPassword = password;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mAccountInfo = ((MyApplication) getApplication()).instancepostAccount().postLogin(
                        "username=" + mAccount + "&&password=" + mPassword + "&&phone_id=" +
                                ((MyApplication) getApplication()).getMAC());
                Log.i("what", "mAccountInfo" + mAccountInfo);
                Thread.sleep(666);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i("what", "mAccountInfo" + e.getCause());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                SharedPManager sp = new SharedPManager(mContext);
                sp.putString(MyApplication.getInstance().getToken(), mAccountInfo.getToken());
                sp.putString(MyApplication.getInstance().getUsername(), mAccountInfo.getUser());
                sp.putString(MyApplication.getInstance().getUid(), mAccountInfo.getUid());
                sp.apply();
                Toast.makeText(mContext, R.string.success, Toast.LENGTH_SHORT).show();
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


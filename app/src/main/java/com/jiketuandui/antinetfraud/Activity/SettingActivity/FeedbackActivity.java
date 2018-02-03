package com.jiketuandui.antinetfraud.Activity.SettingActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.jiketuandui.antinetfraud.R;

/**
 * 反馈
 *
 * @author wangyu
 */
public class FeedbackActivity extends Activity {

    private AppCompatEditText editText;
    private AppCompatButton post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

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
//            new AsyncFeedback().execute(what);
        }
    }

    private void initView() {
        this.editText = findViewById(R.id.feedback);
        this.post = findViewById(R.id.post);
    }

//    private class AsyncFeedback extends AsyncTask<String, Void, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(String... strings) {
//            return ((MyApplication) getApplication()).instancepostAccount().postFeedback("content="
//                    + strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            if (aBoolean) {
//                Toast.makeText(FeedbackActivity.this, "反馈成功~", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//            super.onPostExecute(aBoolean);
//        }
//    }
}

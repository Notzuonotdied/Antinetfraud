package com.jiketuandui.antinetfraud.Activity.SettingActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;

public class FeedbackActivity extends Activity {

    private EditText editText;
    private Button post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();
        initBack();
        initListener();
    }

    private void initListener() {
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptPost();
            }
        });
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
            new AsyncFeedback().execute(what);
        }
    }

    private void initBack() {
        FrameLayout tagsback = (FrameLayout) findViewById(R.id.back);
        tagsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        this.editText = (EditText) findViewById(R.id.feedback);
        this.post = (Button) findViewById(R.id.post);
    }

    private class AsyncFeedback extends AsyncTask<String,Void,Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return ((MyApplication)getApplication()).instancepostAccount().postFeedback("content="
                    + strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(FeedbackActivity.this,"反馈成功~",Toast.LENGTH_SHORT).show();
                finish();
            }
            super.onPostExecute(aBoolean);
        }
    }
}

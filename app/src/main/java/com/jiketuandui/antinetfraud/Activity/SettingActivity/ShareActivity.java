package com.jiketuandui.antinetfraud.Activity.SettingActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareActivity extends Activity {

    @BindView(R.id.tags_back)
    FrameLayout tagsBack;
    @BindView(R.id.share_title)
    EditText shareTitle;
    @BindView(R.id.share_type)
    Spinner shareType;
    @BindView(R.id.share_content)
    EditText shareContent;
    @BindView(R.id.post)
    Button post;

    private String allContent;
    private int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        initListener();
    }

    /**
     * 初始化post数据
     */
    private void initData() {
        // 重置Errors
        shareTitle.setError(null);
        shareContent.setError(null);

        boolean cancel = false;
        View focusView = null;

        String title = shareTitle.getText().toString();
        String content = shareContent.getText().toString();

        // 检查经历名称
        if (TextUtils.isEmpty(title)) {
            shareTitle.setError(getString(R.string.error_invalid_title));
            focusView = shareTitle;
            cancel = true;
        }

        // 检查案例内容
        if (TextUtils.isEmpty(content)) {
            shareContent.setError(getString(R.string.error_invalid_content));
            focusView = shareContent;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            allContent = "title=" + shareTitle.getText().toString() +
                    "&&type=" + String.valueOf(type) +
                    "&&content=" + shareContent.getText().toString();
            new postShare().execute();
        }
    }

    /**
     * 初始化响应事件
     */
    private void initListener() {
        tagsBack.setOnClickListener(view -> finish());
        shareType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.equals(shareType.getSelectedItem().toString(), "网络诈骗")) {
                    type = 1;
                } else if (TextUtils.equals(shareType.getSelectedItem().toString(), "电信诈骗")) {
                    type = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        post.setOnClickListener(v -> initData());
    }

    /**
     * post
     */
    private class postShare extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return ((MyApplication) getApplication()).getPostShareContent().post(allContent);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(ShareActivity.this, "分享成功~", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ShareActivity.this, "出错了~", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aBoolean);
        }
    }
}
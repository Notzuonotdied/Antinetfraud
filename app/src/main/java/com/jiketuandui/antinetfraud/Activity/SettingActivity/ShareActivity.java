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

public class ShareActivity extends Activity {

    private FrameLayout tagsback;
    private EditText sharetitle;
    private Spinner sharetype;
    private EditText sharecontent;
    private Button post;

    private String allContent;
    private int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        initView();
        initListener();
    }

    /**
     * 初始化post数据
     */
    private void initData() {
        // 重置Errors
        sharetitle.setError(null);
        sharecontent.setError(null);

        boolean cancel = false;
        View focusView = null;

        String title = sharetitle.getText().toString();
        String content = sharecontent.getText().toString();

        // 检查经历名称
        if (TextUtils.isEmpty(title)) {
            sharetitle.setError(getString(R.string.error_invalid_title));
            focusView = sharetitle;
            cancel = true;
        }

        // 检查案例内容
        if (TextUtils.isEmpty(content)) {
            sharecontent.setError(getString(R.string.error_invalid_content));
            focusView = sharecontent;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            allContent = "title=" + sharetitle.getText().toString() +
                    "&&type=" + String.valueOf(type) +
                    "&&content=" + sharecontent.getText().toString();
            new postShare().execute();
        }
    }

    /**
     * 初始化响应事件
     */
    private void initListener() {
        tagsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sharetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.equals(sharetype.getSelectedItem().toString(), "网络诈骗")) {
                    type = 1;
                } else if (TextUtils.equals(sharetype.getSelectedItem().toString(), "电信诈骗")) {
                    type = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    /**
     * 初始化View
     */
    private void initView() {
        this.sharecontent = (EditText) findViewById(R.id.share_content);
        this.sharetype = (Spinner) findViewById(R.id.share_type);
        this.sharetitle = (EditText) findViewById(R.id.share_title);
        this.tagsback = (FrameLayout) findViewById(R.id.tags_back);
        this.post = (Button) findViewById(R.id.post);
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

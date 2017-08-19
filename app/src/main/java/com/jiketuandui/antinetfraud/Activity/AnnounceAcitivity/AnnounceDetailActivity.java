package com.jiketuandui.antinetfraud.Activity.AnnounceAcitivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jiketuandui.antinetfraud.Bean.AnnounceContent;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnnounceDetailActivity extends AppCompatActivity {

    @BindView(R.id.announce_title)
    TextView announceTitle;
    @BindView(R.id.announce_time)
    TextView announceTime;
    @BindView(R.id.announce_content)
    TextView announceContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce_detail);
        ButterKnife.bind(this);

        LoadingArticle();
        initTagsBack();
    }

    // 返回键
    private void initTagsBack() {
        FrameLayout tagsBack = (FrameLayout) findViewById(R.id.back);
        tagsBack.setOnClickListener(view -> finish());
    }

    /**
     * 获取公告内容
     */
    private void LoadingArticle() {
        // 根据ID获取公告的内容
        new LoadArticle().execute(
                this.getIntent().getExtras().getString(MyApplication.getInstance().getANNOUNCEID())
        );
    }

    private class LoadArticle extends AsyncTask<String, Integer, AnnounceContent> {

        @Override// 在子线程执行
        protected AnnounceContent doInBackground(String... strings) {
            return ((MyApplication) getApplication()).instanceAnnouncement()
                    .getAnnounce(strings[0]);
        }

        @Override// 在主线程执行
        protected void onPostExecute(AnnounceContent articleContents) {
            if (articleContents != null) {
                announceTitle.setText(Html.fromHtml(articleContents.getTitle()));
                announceTime.setText(Html.fromHtml(articleContents.getCreated_at()));
                announceContent.setText(Html.fromHtml(articleContents.getContent()));
            }
        }
    }
}
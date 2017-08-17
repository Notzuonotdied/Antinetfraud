package com.jiketuandui.antinetfraud.Activity.AnnounceAcitivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jiketuandui.antinetfraud.Bean.AnnounceContent;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;

public class AnnounceDetailActivity extends AppCompatActivity {

    private TextView announcetitle;
    private TextView announcetime;
    private TextView announcecontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce_detail);
        this.announcecontent = (TextView) findViewById(R.id.announce_content);
        this.announcetime = (TextView) findViewById(R.id.announce_time);
        this.announcetitle = (TextView) findViewById(R.id.announce_title);

        LoadingArticle();
        inittagsback();
    }

    // 返回键
    private void inittagsback() {
        FrameLayout tagsback = (FrameLayout) findViewById(R.id.back);
        tagsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                announcetitle.setText(Html.fromHtml(articleContents.getTitle()));
                announcetime.setText(Html.fromHtml(articleContents.getCreated_at()));
                announcecontent.setText(Html.fromHtml(articleContents.getContent()));
            }
        }
    }
}

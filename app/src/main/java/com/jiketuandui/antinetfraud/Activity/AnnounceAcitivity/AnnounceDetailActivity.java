package com.jiketuandui.antinetfraud.Activity.AnnounceAcitivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;

import com.jiketuandui.antinetfraud.HTTP.Bean.AnnounceContent;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnnounceDetailActivity extends AppCompatActivity {

    @BindView(R.id.announce_title)
    AppCompatTextView announceTitle;
    @BindView(R.id.announce_time)
    AppCompatTextView announceTime;
    @BindView(R.id.announce_content)
    AppCompatTextView announceContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce_detail);
        ButterKnife.bind(this);

        loadingArticle();
    }

    /**
     * 获取公告内容
     */
    private void loadingArticle() {
        // 根据ID获取公告的内容
        new LoadArticle().execute(
                this.getIntent().getExtras().getString(MyApplication.getInstance().getANNOUNCEID())
        );
    }

    private class LoadArticle extends AsyncTask<String, Integer, AnnounceContent> {

        // 在子线程执行
        @Override
        protected AnnounceContent doInBackground(String... strings) {
            return ((MyApplication) getApplication()).instanceAnnouncement()
                    .getAnnounce(strings[0]);
        }

        // 在主线程执行
        @Override
        protected void onPostExecute(AnnounceContent articleContents) {
            if (articleContents != null) {
                announceTitle.setText(Html.fromHtml(articleContents.getTitle()));
                announceTime.setText(Html.fromHtml(articleContents.getCreated_at()));
                announceContent.setText(Html.fromHtml(articleContents.getContent()));
            }
        }
    }
}

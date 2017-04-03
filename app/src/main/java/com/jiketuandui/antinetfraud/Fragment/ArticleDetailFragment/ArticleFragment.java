package com.jiketuandui.antinetfraud.Fragment.ArticleDetailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.View.MarkdownView;

/**
 * 文章内容
 */
public class ArticleFragment extends Fragment {

    private MarkdownView article_markdownView;
    private String articleContent;

    public ArticleFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void loadArticleContent(String articleContent) {
        if (!TextUtils.isEmpty(articleContent)) {
            this.article_markdownView.loadMarkdown(articleContent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        articleContent = getArguments().getString(MyApplication.getInstance().getARTICLECONTENT());
        View view = inflater.inflate(R.layout.fragment_article_frangment, container, false);
        article_markdownView = (MarkdownView) view.findViewById(R.id.article_markdownView);
        article_markdownView.loadMarkdown(articleContent);
        return view;
    }
}

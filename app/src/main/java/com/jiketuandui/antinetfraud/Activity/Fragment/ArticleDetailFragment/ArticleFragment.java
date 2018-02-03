package com.jiketuandui.antinetfraud.Activity.Fragment.ArticleDetailFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constants;
import com.jiketuandui.antinetfraud.View.MarkdownView;

/**
 * 文章内容显示
 *
 * @author wangyu
 */
public class ArticleFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String articleContent = getArguments().getString(Constants.ARTICLE_CONTENT);
        View view = inflater.inflate(R.layout.fragment_article_frangment, container, false);
        MarkdownView articleMarkdowns = view.findViewById(R.id.article_markdownView);
        articleMarkdowns.loadMarkdown(articleContent);
        return view;
    }
}

package com.jiketuandui.antinetfraud.activity.article.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.Adapter.CommentAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constants;
import com.jiketuandui.antinetfraud.View.myRecyclerView.MyItemDecoration;
import com.jiketuandui.antinetfraud.activity.article.CommentListActivity;
import com.jiketuandui.antinetfraud.entity.domain.CommentList;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.ArticleService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 评论
 *
 * @author wangyu
 */
public class CommentFragment extends Fragment {

    private CommentAdapter myAdapter;
    private int articleID;
    private int readPage;
    private AppCompatButton lookMore;
    private ArticleService articleService = RetrofitServiceFactory.ARTICLE_SERVICE;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        readPage = 1;
        articleID = this.getArguments().getInt(Constants.ARTICLE_ID);
        View view = inflater.inflate(R.layout.fragment_comment, null);
        initView(view);
        onRefresh();
        return view;
    }

    private void onRefresh() {
        readPage = 1;
        articleService.getCommentList(articleID, readPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<CommentList>() {
                    @Override
                    protected void onHandleSuccess(CommentList commentList) {
                        myAdapter.setData(commentList.getData());
                        myAdapter.notifyDataSetChanged();
                        lookMore.setVisibility(View.VISIBLE);
                        lookMore.setOnClickListener(view -> {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), CommentListActivity.class);
                            intent.putExtra(Constants.ARTICLE_ID, articleID);
                            startActivity(intent);
                        });
                    }
                });
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        // 设置LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new MyItemDecoration());
        myAdapter = new CommentAdapter();
        recyclerView.setAdapter(myAdapter);

        lookMore = view.findViewById(R.id.look_more);
    }
}

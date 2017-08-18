package com.jiketuandui.antinetfraud.Fragment.ArticleDetailFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.Adapter.CommentAdapter;
import com.jiketuandui.antinetfraud.Bean.CommentInfo;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.View.myRecyclerView.MyItemDecoration;
import com.jiketuandui.antinetfraud.View.myRecyclerView.MyRecyclerView;

import java.util.List;

/**
 * 评论
 */
public class CommentFragment extends Fragment {

    private Handler h = new Handler();
    private MyRecyclerView recyclerView;
    private CommentAdapter myAdapter;
    private boolean isRefresh;
    private String articleID;
    private boolean isFirst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, null);
        initField();
        initView(view);
        initListener();
        if (isFirst) {
            new AsyncComment().execute();
        }
        return view;
    }

    private void initListener() {
        recyclerView.setMyRecyclerViewListener(new MyRecyclerView.MyRecyclerViewListener() {
            @Override
            public void onRefresh() {
                h.postDelayed(() -> {
                    isRefresh = true;
                    // 回复原状
                    recyclerView.setRefreshComplete();
                    new AsyncComment().execute();
                }, 2222);
            }

            @Override
            public void onLoadMore() {
                h.postDelayed(() -> {
                    isRefresh = false;
                    // 恢复原状
                    recyclerView.setLoadMoreComplete();
                    new AsyncComment().execute();
                }, 1666);
            }
        });
    }

    private void initView(View view) {
        recyclerView = (MyRecyclerView) view.findViewById(R.id.recyclerView);
        //设置LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new MyItemDecoration());
        myAdapter = new CommentAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    private void initField() {
        articleID = this.getArguments().getString(MyApplication.getInstance().getARTICLEID());
        isRefresh = true;
        isFirst = true;
    }

    private class AsyncComment extends AsyncTask<Void, Void, List<CommentInfo>> {

        @Override
        protected List<CommentInfo> doInBackground(Void... params) {
            return MyApplication.getInstance().instanceGetComment().getComment(articleID);
        }

        @Override
        protected void onPostExecute(List<CommentInfo> commentInfo) {
            if (commentInfo != null) {
                Log.i("Notzuonotdied", "commentInfo.size()" + commentInfo.size());
                if (isRefresh) {
                    myAdapter.setData(commentInfo);
                } else {
                    //myAdapter.addData(commentInfo);
                    myAdapter.setData(commentInfo);
                }
                isFirst = false;
                myAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(commentInfo);
        }
    }
}

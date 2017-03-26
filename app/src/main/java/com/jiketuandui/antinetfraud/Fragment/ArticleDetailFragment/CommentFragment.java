package com.jiketuandui.antinetfraud.Fragment.ArticleDetailFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        isRefresh = true;
        View view = inflater.inflate(R.layout.fragment_comment, null);
        recyclerView = (MyRecyclerView) view.findViewById(R.id.recyclerView);
        //设置LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new MyItemDecoration());

        myAdapter = new CommentAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setMyRecyclerViewListener(new MyRecyclerView.MyRecyclerViewListener() {
            @Override
            public void onRefresh() {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isRefresh = true;
                        // 回复原状
                        recyclerView.setRefreshComplete();
                        new AsyncComment().execute();
                    }
                }, 2222);
            }

            @Override
            public void onLoadMore() {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isRefresh = false;
                        // 恢复原状
                        recyclerView.setLoadMoreComplete();
                        new AsyncComment().execute();
                    }
                }, 1666);
            }
        });
        return view;
    }

    private class AsyncComment extends AsyncTask<Void, Void, List<CommentInfo>> {

        @Override
        protected List<CommentInfo> doInBackground(Void... params) {
            return MyApplication.getInstance()
                    .instanceGetComment()
                    .getComment(MyApplication.getInstance().getARTICLEID());
        }

        @Override
        protected void onPostExecute(List<CommentInfo> commentInfo) {
            if (commentInfo != null) {
                if (isRefresh) {
                    myAdapter.addData(commentInfo);
                } else {
                    myAdapter.setData(commentInfo);
                }
                myAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(commentInfo);
        }
    }
}

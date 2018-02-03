package com.jiketuandui.antinetfraud.Activity.Fragment.ArticleDetailFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jiketuandui.antinetfraud.Adapter.CommentAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constants;

/**
 * 评论
 */
public class CommentFragment extends Fragment {

    // private Handler h = new Handler();
    private XRecyclerView recyclerView;
    private CommentAdapter myAdapter;
    // private boolean isRefresh;
    private String articleID;
    private boolean isFirst;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, null);
        initField();
        initView(view);
        initListener();
        if (isFirst) {
//            new AsyncComment(true).execute();
        }
        return view;
    }

    private void initListener() {
//        recyclerView.setMyRecyclerViewListener(new MyRecyclerView.MyRecyclerViewListener() {
//            @Override
//            public void onRefresh() {
//                h.postDelayed(() -> {
//                    isRefresh = true;
//                    // 回复原状
//                    recyclerView.setRefreshComplete();
//                    new AsyncComment().execute();
//                }, 2222);
//            }
//
//            @Override
//            public void onLoadMore() {
//                h.postDelayed(() -> {
//                    isRefresh = false;
//                    // 恢复原状
//                    recyclerView.setLoadMoreComplete();
//                    new AsyncComment().execute();
//                }, 1666);
//            }
//        });
//        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//                new AsyncComment(true).execute();
//            }
//
//            @Override
//            public void onLoadMore() {
//                new AsyncComment(false).execute();
//            }
//        });
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        //设置LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // recyclerView.addItemDecoration(new MyItemDecoration());
        myAdapter = new CommentAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    private void initField() {
        articleID = this.getArguments().getString(Constants.ARTICLE_ID);
        // isRefresh = true;
        isFirst = true;
    }

//    private class AsyncComment extends AsyncTask<Void, Void, List<CommentInfo>> {
//
//        private boolean isRefresh;
//
//        public AsyncComment(boolean isRefresh) {
//            this.isRefresh = isRefresh;
//        }
//
//        @Override
//        protected List<CommentInfo> doInBackground(Void... params) {
//            return MyApplication.getInstance().instanceGetComment().getComment(articleID);
//        }
//
//        @Override
//        protected void onPostExecute(List<CommentInfo> commentInfo) {
//            if (commentInfo != null) {
//                Log.i("Notzuonotdied", "commentInfo.size()" + commentInfo.size());
//                if (isRefresh) {
//                    myAdapter.setData(commentInfo);
//                } else {
//                    //myAdapter.addData(commentInfo);
//                    myAdapter.setData(commentInfo);
//                }
//                isFirst = false;
//                myAdapter.notifyDataSetChanged();
//            }
//            if (isRefresh) {
//                recyclerView.refreshComplete();
//            } else {
//                recyclerView.loadMoreComplete();
//            }
//            super.onPostExecute(commentInfo);
//        }
//    }
}

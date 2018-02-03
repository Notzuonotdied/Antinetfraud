package com.jiketuandui.antinetfraud.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.Adapter.Holder.CommentHolder;
import com.jiketuandui.antinetfraud.HTTP.Bean.CommentInfo;
import com.jiketuandui.antinetfraud.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章内容显示页面的评论Fragment的Adapter
 *
 * @author wangyu
 * @date 17-3-24
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {
    private ArrayList<CommentInfo> data = new ArrayList<>();

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);

        return new CommentHolder(rootView);
    }

    /**
     * 添加到集合
     * 追加模式
     */
    public void addData(List<CommentInfo> mDatas) {
        this.data.addAll(mDatas);
    }

    /**
     * 获取集合
     */
    public List<CommentInfo> getData() {
        return this.data;
    }

    /**
     * 清空后添加
     */
    public void setData(List<CommentInfo> mDatas) {
        this.data.clear();
        this.data.addAll(mDatas);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        holder.author.setText(data.get(position).getAuthor());
        holder.content.setText(data.get(position).getContent());
        holder.time.setText(data.get(position).getCreate_at());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

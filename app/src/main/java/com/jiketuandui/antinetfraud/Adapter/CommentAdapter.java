package com.jiketuandui.antinetfraud.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.Adapter.Holder.CommentHolder;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.entity.domain.CommentList;

import java.util.List;

/**
 * 文章内容显示页面的评论Fragment的Adapter
 *
 * @author wangyu
 * @date 17-3-24
 */

public class CommentAdapter extends BaseAdapter<CommentList.DataBean, CommentHolder> {

    public CommentAdapter() {
        super();
    }

    public CommentAdapter(Context context, List<CommentList.DataBean> mListContents) {
        super(context, mListContents);
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new CommentHolder(rootView);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        holder.author.setText(mListContents.get(position).getUser_name());
        holder.content.setText(mListContents.get(position).getContent());
        holder.time.setText(mListContents.get(position).getCreated_at());
        holder.img.setText(mListContents.get(position).getUser_name().substring(0, 1));
        if (mListContents.get(position).getAuthor_reply() != null) {
            holder.reply.setVisibility(View.VISIBLE);
            holder.authorReply.setText(mListContents.get(position).getAuthor_reply());
        }
    }

}

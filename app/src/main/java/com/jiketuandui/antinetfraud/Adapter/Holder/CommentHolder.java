package com.jiketuandui.antinetfraud.Adapter.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.R;

/**
 * 文章内容显示页面的评论Fragment的Holder
 * Created by wangyu on 17-3-24.
 */

public class CommentHolder extends RecyclerView.ViewHolder {
    public TextView author;
    public TextView content;
    public TextView time;
    private SimpleDraweeView img;

    public CommentHolder(View itemView) {
        super(itemView);
        author = itemView.findViewById(R.id.comment_author);
        content = itemView.findViewById(R.id.comment_content);
        time = itemView.findViewById(R.id.comment_time);
        img = itemView.findViewById(R.id.comment_img);
    }
}

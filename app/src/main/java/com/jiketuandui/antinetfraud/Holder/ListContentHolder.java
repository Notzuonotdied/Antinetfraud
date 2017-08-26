package com.jiketuandui.antinetfraud.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.Interface.ListContentOnClickListener;
import com.jiketuandui.antinetfraud.R;

/**
 * Created by Notzuonotdied on 2016/8/1.
 * <p>
 * 这是为了MainTab_news中的RecyclerView中的MaterialRefreshLayout中的适配器中所需的Holder
 */
public class ListContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView Holder_title;
    public TextView Holder_tip;
    public SimpleDraweeView Holder_image;
    public TextView Holder_source;
    public SimpleDraweeView top_icon;
    public TextView top_tag;
    private LinearLayout bottom;
    private LinearLayout top;
    private ListContentOnClickListener listContentOnClickListener;

    public ListContentHolder(View itemView) {
        super(itemView);
        initView(itemView);
        itemView.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView(View v) {
        this.Holder_title = (TextView) v.findViewById(R.id.lc_holder_tittle);
        this.Holder_tip = (TextView) v.findViewById(R.id.lc_holder_tip);
        this.Holder_image = (SimpleDraweeView) v.findViewById(R.id.lc_holder_image);
        this.Holder_source = (TextView) v.findViewById(R.id.lc_holder_source);
        this.top_icon = (SimpleDraweeView) v.findViewById(R.id.top_icon);
        this.top_tag = (TextView) v.findViewById(R.id.top_tag);
        // 设置响应事件
        this.bottom = (LinearLayout) v.findViewById(R.id.bottom);
        this.top = (LinearLayout) v.findViewById(R.id.top);
        bottom.setOnClickListener(this);
        top.setOnClickListener(this);
    }

    /**
     * 设置响应事件
     */
    public void setItemOnClickListener(ListContentOnClickListener listContentOnClickListener) {
        this.listContentOnClickListener = listContentOnClickListener;
    }

    /**
     * 响应
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top:
                if (listContentOnClickListener != null) {
                    listContentOnClickListener.TOPOnClickListener(getAdapterPosition());
                }
                break;
            case R.id.bottom:
                if (listContentOnClickListener != null) {
                    listContentOnClickListener.OnItemClickListener(getAdapterPosition());
                }
                break;
        }
    }
}

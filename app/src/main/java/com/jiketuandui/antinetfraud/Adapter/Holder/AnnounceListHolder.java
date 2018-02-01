package com.jiketuandui.antinetfraud.Adapter.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jiketuandui.antinetfraud.Adapter.Interface.AnnounceListOnClickListener;
import com.jiketuandui.antinetfraud.R;

/**
 * Created by Notzuonotdied on 2017年3月9日 22:49:37
 */
public class AnnounceListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView announceTitle;
    public TextView announceTime;
    private AnnounceListOnClickListener announceListOnClickListener;

    public AnnounceListHolder(View itemView) {
        super(itemView);
        initView(itemView);
        itemView.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView(View v) {
        this.announceTitle = v.findViewById(R.id.announce_title);
        this.announceTime = v.findViewById(R.id.announce_time);
    }

    /**
     * 设置响应事件
     */
    public void setItemOnClickListener(AnnounceListOnClickListener announceListOnClickListener) {
        this.announceListOnClickListener = announceListOnClickListener;
    }

    /**
     * 响应
     */
    @Override
    public void onClick(View view) {
        if (announceListOnClickListener != null) {
            announceListOnClickListener.OnItemClickListener(getAdapterPosition());
        }
    }
}

package com.jiketuandui.antinetfraud.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.ListConentOnClickListener;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;

import org.w3c.dom.Text;

/**
 * Created by Notzuonotdied on 2016/8/1.
 *
 * 这是为了MainTab_news中的RecyclerView中的MaterialRefreshLayout中的适配器中所需的Holder
 */
public class ListContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView Holder_title;
    public TextView Holder_tip;
    public SimpleDraweeView Holder_image;
    public TextView Holder_source;
    private LinearLayout bottom;
    private LinearLayout top;
    public SimpleDraweeView top_icon;
    public TextView top_tag;

    public ListContentHolder(View itemView) {
        super(itemView);
        initView(itemView);
        itemView.setOnClickListener(this);
    }

    /**
     * 初始化控件
     * */
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


    public ListConentOnClickListener listConentOnClickListener;

    /**
     * 设置响应事件
     * */
    public void setItemOnClickListener(ListConentOnClickListener listConentOnClickListener) {
        this.listConentOnClickListener = listConentOnClickListener;
    }

    /**
     * 响应
     * */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top:
                if (listConentOnClickListener != null) {
                    listConentOnClickListener.TOPOnClickLinstener(getAdapterPosition());
                }
                break;
            case R.id.bottom:
                if (listConentOnClickListener != null) {
                    listConentOnClickListener.OnItemClickListener(getAdapterPosition());
                }
                break;
        }
    }
}

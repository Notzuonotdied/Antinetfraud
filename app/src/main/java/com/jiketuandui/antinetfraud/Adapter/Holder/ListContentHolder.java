package com.jiketuandui.antinetfraud.Adapter.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.Adapter.Interface.ListContentOnClickListener;
import com.jiketuandui.antinetfraud.R;

/**
 * 这是为了MainTab_news中的RecyclerView中的MaterialRefreshLayout中的适配器中所需的Holder
 *
 * @author Notzuonotdied
 * @date 2016/8/1
 */
public class ListContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView holderTitle;
    public TextView holderTip;
    public SimpleDraweeView holderImage;
    public TextView holderSource;
    public SimpleDraweeView topIcon;
    public TextView topTag;
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
        this.holderTitle = v.findViewById(R.id.lc_holder_tittle);
        this.holderTip = v.findViewById(R.id.lc_holder_tip);
        this.holderImage = v.findViewById(R.id.lc_holder_image);
        this.holderSource = v.findViewById(R.id.lc_holder_source);
        this.topIcon = v.findViewById(R.id.top_icon);
        this.topTag = v.findViewById(R.id.top_tag);
        // 设置响应事件
        LinearLayout bottom = v.findViewById(R.id.bottom);
        LinearLayout top = v.findViewById(R.id.top);
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
                    listContentOnClickListener.onTopClickListener(getAdapterPosition());
                }
                break;
            case R.id.bottom:
                if (listContentOnClickListener != null) {
                    listContentOnClickListener.onItemClickListener(getAdapterPosition());
                }
                break;
            default:
                break;
        }
    }
}

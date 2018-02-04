package com.jiketuandui.antinetfraud.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.activity.main.announce.AnnounceDetailActivity;
import com.jiketuandui.antinetfraud.Adapter.Holder.AnnounceListHolder;
import com.jiketuandui.antinetfraud.Adapter.Interface.AnnounceListOnClickListener;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constants;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.entity.domain.AnnounceList;

import java.util.List;

/**
 * @author wangyu
 * @date 2017年3月9日 20:47:21
 */
public class AnnounceAdapter extends BaseAdapter<AnnounceList.DataBean, AnnounceListHolder> {

    private LayoutInflater mLayoutInflater;
    /**
     * 设置列表项的响应事件
     */
    private AnnounceListOnClickListener mListListener = position -> {
        if (NetWorkUtils.isConnectNET(context)) {
            Intent intent = new Intent(context, AnnounceDetailActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putInt(Constants.ANNOUNCE_ID, mListContents.get(position).getId());
            intent.putExtras(mBundle);
            context.startActivity(intent);
        }
    };

    /**
     * 内容适配器
     *
     * @param mListContents 内容列表
     */
    public AnnounceAdapter(Context context, List<AnnounceList.DataBean> mListContents) {
        this.mListContents = mListContents;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    /**
     * 创建新View，被LayoutManager所调用
     */
    @Override
    public AnnounceListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.announce_list_holder, parent, false);
        return new AnnounceListHolder(view);
    }

    /**
     * 将数据与界面进行绑定的操作
     */
    @Override
    public void onBindViewHolder(AnnounceListHolder holder, int position) {
        holder.announceTitle.setText(mListContents.get(position).getTitle());
        holder.announceTime.setText(mListContents.get(position).getCreated_at());
        // 设置响应事件
        holder.setItemOnClickListener(mListListener);
    }
}

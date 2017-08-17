package com.jiketuandui.antinetfraud.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.Activity.AnnounceAcitivity.AnnounceDetailActivity;
import com.jiketuandui.antinetfraud.Bean.AnnounceContent;
import com.jiketuandui.antinetfraud.Holder.AnnounceListHolder;
import com.jiketuandui.antinetfraud.Interface.AnnounceListOnClickListener;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;

import java.util.List;

/**
 * Created by Notzuonotdied on 2017年3月9日 20:47:21
 */
public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceListHolder> {

    private List<AnnounceContent> mListContents;
    private LayoutInflater mLayoutInflater;
    private Context context;
    /**
     * 设置列表项的响应事件
     */
    private AnnounceListOnClickListener mListListener = new AnnounceListOnClickListener() {
        @Override
        public void OnItemClickListener(int position) {
            if (NetWorkUtils.isConnectNET(context)) {
                Intent intent = new Intent(context, AnnounceDetailActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString(MyApplication.getInstance().getANNOUNCEID(), mListContents.get(position).getId());
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        }

    };

    /**
     * Instantiates a new List content adapter.
     *
     * @param mListContents 内容列表
     */
    public AnnounceAdapter(Context context, List<AnnounceContent> mListContents) {
        this.mListContents = mListContents;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    /**
     * 清空博客文章摘要信息的List集合
     */
    public void clearData() {
        this.mListContents.clear();
        this.notifyDataSetChanged();
    }

    /**
     * 添加博客文章摘要信息的List集合
     * 追加模式
     */
    public void addData(List<AnnounceContent> mDatas) {
        this.mListContents.addAll(mDatas);
    }

    /**
     * 获取博客文章摘要信息的List集合
     */
    public List<AnnounceContent> getData() {
        return this.mListContents;
    }

    /**
     * 清空后添加
     */
    public void setData(List<AnnounceContent> mDatas) {
        this.mListContents.clear();
        this.mListContents.addAll(mDatas);
    }

    @Override// 创建新View，被LayoutManager所调用
    public AnnounceListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.announce_list_holder, parent, false);
        return new AnnounceListHolder(view);
    }

    @Override//将数据与界面进行绑定的操作
    public void onBindViewHolder(AnnounceListHolder holder, int position) {
        holder.announceTitle.setText(mListContents.get(position).getTitle());
        holder.announceTime.setText(mListContents.get(position).getCreated_at());
        /// 设置响应事件
        holder.setItemOnClickListener(mListListener);
    }

    @Override//获取数据的数量
    public int getItemCount() {
        return mListContents.size();
    }
}

package com.jiketuandui.antinetfraud.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.Activity.AnnounceAcitivity.AnnounceDetailActivity;
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
public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceListHolder> {

    private List<AnnounceList.DataBean> mListContents;
    private LayoutInflater mLayoutInflater;
    private Context context;
    /**
     * 设置列表项的响应事件
     */
    private AnnounceListOnClickListener mListListener = new AnnounceListOnClickListener() {
        @Override
        public void onItemClickListener(int position) {
            if (NetWorkUtils.isConnectNET(context)) {
                Intent intent = new Intent(context, AnnounceDetailActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt(Constants.ANNOUNCE_ID, mListContents.get(position).getId());
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
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
     * 添加博客文章摘要信息的List集合
     * 追加模式
     */
    public void addData(List<AnnounceList.DataBean> mDatas) {
        this.mListContents.addAll(mDatas);
    }

    /**
     * 获取List集合
     */
    public List<AnnounceList.DataBean> getData() {
        return this.mListContents;
    }

    /**
     * 清空后添加
     */
    public void setData(List<AnnounceList.DataBean> mDatas) {
        this.mListContents.clear();
        this.mListContents.addAll(mDatas);
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

    /**
     * 获取数据的数量
     */
    @Override
    public int getItemCount() {
        return mListContents.size();
    }
}

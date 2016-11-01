package com.jiketuandui.antinetfraud.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jiketuandui.antinetfraud.Activity.ArticleContentActivity;
import com.jiketuandui.antinetfraud.Activity.ToTagsListActivity;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.Holder.ListContentHolder;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constant;
import com.jiketuandui.antinetfraud.Util.ListConentOnClickListener;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;

import java.util.List;

/**
 * Created by Notzuonotdied on 2016/8/1.
 * 自定义的ViewHolder，持有每个Item的的所有界面元素
 * <p>
 * 介绍:ListView是使用ViewHolder来提升性能的,ViewHolder通过保存item中使用到的空间的引用来
 * 减少findViewById的调用,以此使得ListView更加顺畅.
 * 在任何的ViewHolder被实例化的时候-->OnCreateViewHolder被触发-->onBindViewHolder被触发
 */
public class ListContentAdapter extends RecyclerView.Adapter<ListContentHolder> {

    private List<ListContent> mListContents;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private boolean isOpenTop;
    private int category;
    /**
     * 设置列表项的响应事件
     */
    private ListConentOnClickListener mListListener = new ListConentOnClickListener() {
        @Override
        public void OnItemClickListener(int position) {
            if (NetWorkUtils.isConnectNET(context)) {
                Intent intent = new Intent(context, ArticleContentActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt(Constant.CONTENTID, Integer.valueOf(mListContents.get(position).getId()));
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        }

        @Override
        public void TOPOnClickLinstener(int position) {
            if (NetWorkUtils.isConnectNET(context)) {
                Intent intent = new Intent(context, ToTagsListActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt(Constant.TAGSID, Integer.valueOf(mListContents.get(position).getTagid()));
                if (category != 0) {
                    mBundle.putInt(Constant.CATEGORY, category);
                }
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        }
    };

    /**
     * Instantiates a new List content adapter.
     *
     * @param mListContents 内容列表
     * @param isOpenTop 是否开启顶部，true为开启，false为不开启
     * @param category 判断是那种类型的，1是正常排序，2是热门案例排序
     */
    public ListContentAdapter(Context context, List<ListContent> mListContents, boolean isOpenTop,int category) {
        this.mListContents = mListContents;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.isOpenTop = isOpenTop;
        this.category = category;
    }

    /**
     * Instantiates a new List content adapter.
     *
     * @param mListContents 内容列表
     * @param isOpenTop 是否开启顶部，true为开启，false为不开启
     */
    public ListContentAdapter(Context context, List<ListContent> mListContents, boolean isOpenTop) {
        this.mListContents = mListContents;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.isOpenTop = isOpenTop;
        this.category = 0;
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
    public void addData(List<ListContent> mDatas) {
        this.mListContents.addAll(mDatas);
    }

    /**
     * 获取博客文章摘要信息的List集合
     */
    public List<ListContent> getData() {
        return this.mListContents;
    }

    /**
     * 清空后添加
     */
    public void setData(List<ListContent> mDatas) {
        this.mListContents.clear();
        this.mListContents.addAll(mDatas);
    }

    @Override// 创建新View，被LayoutManager所调用
    public ListContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_content_holder, parent, false);
        LinearLayout m = (LinearLayout) view.findViewById(R.id.top);
        if (this.isOpenTop) {
            m.setVisibility(View.VISIBLE);
        } else {
            m.setVisibility(View.GONE);
        }
        return new ListContentHolder(view);
    }

    @Override //将数据与界面进行绑定的操作
    public void onBindViewHolder(ListContentHolder holder, int position) {
        // 设置博文的名称
        holder.Holder_title.setText(mListContents.get(position).getTitle());
        // 设置博文的信息
        holder.Holder_tip.setText(mListContents.get(position).getTip());
        // 设置博文的图片
        holder.Holder_image.setImageURI(Uri.parse(mListContents.get(position).getImagelink()));
        // 设置来源
        holder.Holder_source.setText(mListContents.get(position).getSource());
        // 设置顶部的标签
        holder.top_tag.setText(Constant.TabBigTitle[Integer.valueOf(mListContents.get(position).getTagid())]);
        // 设置响应事件
        holder.setItemOnClickListener(mListListener);
    }

    @Override//获取数据的数量
    public int getItemCount() {
        return mListContents.size();
    }
}

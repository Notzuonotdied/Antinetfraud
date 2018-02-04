package com.jiketuandui.antinetfraud.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.jiketuandui.antinetfraud.entity.domain.BaseList;

import java.util.ArrayList;
import java.util.List;

/**
 * 基适配器
 *
 * @author wangyu
 */
public abstract class BaseAdapter<T, Holder extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<Holder> {
    protected List<T> mListContents;
    protected Context context;

    /**
     * 初始化内容适配器
     *
     * @param mListContents 内容列表
     */
    public BaseAdapter(Context context, List<T> mListContents) {
        this.mListContents = mListContents;
        this.context = context;
    }

    public BaseAdapter() {
        this.mListContents = new ArrayList<>();
        this.context = null;
    }

    /**
     * 添加博客文章摘要信息的List集合
     * 追加模式
     */
    public void addData(List<T> mDates) {
        this.mListContents.addAll(mDates);
    }

    /**
     * 获取博客文章摘要信息的List集合
     */
    public List<T> getData() {
        return this.mListContents;
    }

    /**
     * 清空后添加
     */
    public void setData(List<T> mDates) {
        this.mListContents.clear();
        this.mListContents.addAll(mDates);
    }

    @Override
    public int getItemCount() {
        return mListContents.size();
    }
}

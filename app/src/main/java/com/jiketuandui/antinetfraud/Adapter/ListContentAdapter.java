package com.jiketuandui.antinetfraud.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.jiketuandui.antinetfraud.activity.article.ArticleContentActivity;
import com.jiketuandui.antinetfraud.activity.article.ArticleTagsListActivity;
import com.jiketuandui.antinetfraud.Adapter.Holder.ListContentHolder;
import com.jiketuandui.antinetfraud.Adapter.Interface.ListContentOnClickListener;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constants;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.entity.domain.ArticleList;

import java.util.List;

/**
 * 自定义的ViewHolder，持有每个Item的的所有界面元素
 * 介绍:ListView是使用ViewHolder来提升性能的,ViewHolder通过保存item中使用到的空间的引用来
 * 减少findViewById的调用,以此使得ListView更加顺畅.
 * 在任何的ViewHolder被实例化的时候-->OnCreateViewHolder被触发-->onBindViewHolder被触发
 *
 * @author Notzuonotdied
 * @date 2016/8/1
 */
public class ListContentAdapter extends BaseAdapter<ArticleList.DataBean, ListContentHolder> {
    private boolean isOpenTop;
    /**
     * 设置列表项的响应事件
     */
    private ListContentOnClickListener mListListener = new ListContentOnClickListener() {
        @Override
        public void onItemClickListener(int position) {
            if (NetWorkUtils.isConnectNET(context)) {
                Intent intent = new Intent(context, ArticleContentActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt(Constants.CONTENT_ID, mListContents.get(position).getId());
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        }

        @Override
        public void onTopClickListener(int position) {
            if (NetWorkUtils.isConnectNET(context)) {
                Intent intent = new Intent(context, ArticleTagsListActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt(Constants.TAGS_ID,
                        mListContents.get(position).getTag_id());
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        }
    };

    /**
     * 初始化内容适配器
     *
     * @param mListContents 内容列表
     * @param isOpenTop     是否开启顶部，true为开启，false为不开启
     */
    public ListContentAdapter(Context context,
                              List<ArticleList.DataBean> mListContents,
                              boolean isOpenTop) {
        this.mListContents = mListContents;
        this.context = context;
        this.isOpenTop = isOpenTop;
    }

    /**
     * 创建新View，被LayoutManager所调用
     */
    @Override
    public ListContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_content_holder, parent, false);
        LinearLayout m = view.findViewById(R.id.top);
        if (this.isOpenTop) {
            m.setVisibility(View.VISIBLE);
        } else {
            m.setVisibility(View.GONE);
        }
        return new ListContentHolder(view);
    }

    /**
     * 将数据与界面进行绑定的操作
     */
    @Override
    public void onBindViewHolder(ListContentHolder holder, int position) {
        // 设置博文的名称
        holder.holderTitle.setText(mListContents.get(position).getTitle());
        // 设置博文的信息
        holder.holderTip.setText(mListContents.get(position).getCreated_at());
        // 设置博文的图片
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(mListContents.get(position).getImage()))
                .setTapToRetryEnabled(true)
                .setOldController(holder.holderImage.getController())
                .build();
        holder.holderImage.setController(controller);
        // 设置顶部类型图标
        GenericDraweeHierarchy hierarchy = holder.topIcon.getHierarchy();
        switch (mListContents.get(position).getTag_id()) {
            case 1:
                hierarchy.setPlaceholderImage(R.mipmap.tel_fraud);
                break;
            case 2:
                hierarchy.setPlaceholderImage(R.mipmap.net_fraud);
                break;
            case 3:
                hierarchy.setPlaceholderImage(R.mipmap.booklet);
                break;
            default:
                break;
        }
        // 设置来源
        holder.holderSource.setText(mListContents.get(position).getSource());
        // 设置顶部的标签
        holder.topTag.setText(Constants.TAB_BIG_TITLE[mListContents.get(position).getTag_id()]);
        // 设置响应事件
        holder.setItemOnClickListener(mListListener);
    }
}

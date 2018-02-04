package com.jiketuandui.antinetfraud.activity.main.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.Constants;
import com.jiketuandui.antinetfraud.Util.RefreshUtil;
import com.jiketuandui.antinetfraud.View.CFontTitleTextView;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.service.ArticleService;

/**
 * @author Notzuonotdied
 * @date 2016/8/1
 * 这个是放置主页新闻ViewPage的内容的
 */
public class MainTabNews extends Fragment implements NetBroadcastReceiver.NetEventHandler {
    /**
     * 是否是热门案例，暂时通过是否有bundle来判断是热门还是正常。
     */
    private boolean isHot;
    private ArticleService articleService = RetrofitServiceFactory.ARTICLE_SERVICE;
    private RefreshUtil refreshUtil;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, R.layout.main_tab_news, false, true);
    }

    protected View initView(@NonNull LayoutInflater inflater,
                            int layoutId,
                            boolean noIndicator,
                            boolean hasBundle) {
        refreshUtil = new RefreshUtil(getContext());
        // 初始化数据
        initData(hasBundle);
        View view = inflater.inflate(layoutId, null);
        if (noIndicator) {
            CFontTitleTextView title = view.findViewById(R.id.main_header_tv);
            title.setOnClickListener(v -> YoYo.with(Techniques.Swing)
                    .duration(333)
                    .repeat(6)
                    .playOn(v.findViewById(R.id.main_header_tv)));
        }
        refreshUtil.materialRefreshLayout = view.findViewById(R.id.refresh);
        refreshUtil.tagsRecyclerView = view.findViewById(R.id.recyclerView);
        refreshUtil.initViewAttr();
        refreshUtil.setRefreshInterface(new RefreshUtil.RefreshInterface() {
            @Override
            public void refresh() {
                if (isHot) {
                    refreshUtil.onRefresh(articleService.getHotArticleList(
                            refreshUtil.getReadPage()
                    ));
                } else {
                    refreshUtil.onRefresh(articleService.getArticleList(
                            refreshUtil.getReadPage()
                    ));
                }
            }

            @Override
            public void loadMore() {
                if (isHot) {
                    refreshUtil.onLoadMore(articleService.getHotArticleList(
                            refreshUtil.getReadPage()
                    ));
                } else {
                    refreshUtil.onLoadMore(articleService.getArticleList(
                            refreshUtil.getReadPage()
                    ));
                }
            }
        });
        return view;
    }

    /**
     * 初始化必备的数据
     *
     * @param hasBundle 是否有来自其他Activity传递的参数，有传参的就不是热门案例
     */
    protected void initData(boolean hasBundle) {
        if (hasBundle) {
            // 获取当前Item的下标
            Bundle bundle = getArguments();
            assert bundle != null;
            int position = bundle.getInt(Constants.MAIN_PAGE_POSITION);
            refreshUtil.setOpenTop(position == 0);
            isHot = false;
        } else {
            refreshUtil.setOpenTop(true);
            this.isHot = true;
        }

        // 注册
        NetBroadcastReceiver.mListeners.add(this);
    }

    @Override
    public void onResume() {
        refreshUtil.onResume();
        super.onResume();
    }

    @Override
    public void onNetChange() {
        refreshUtil.onNetChange();
    }
}

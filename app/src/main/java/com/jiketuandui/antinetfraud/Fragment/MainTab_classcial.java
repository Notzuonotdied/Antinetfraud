package com.jiketuandui.antinetfraud.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiketuandui.antinetfraud.Activity.ArticleContentActivity;
import com.jiketuandui.antinetfraud.Adapter.VideoListAdapter;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.Constant;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.View.MyListView;
import com.jiketuandui.antinetfraud.View.banner.BannerBaseView;
import com.jiketuandui.antinetfraud.View.banner.BannerViewOnClickListener;
import com.jiketuandui.antinetfraud.View.banner.MainBannerView;
import com.jiketuandui.antinetfraud.View.banner.bean.BaseBannerBean;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


public class MainTab_classcial extends Fragment implements NetBroadcastReceiver.netEventHandler {

    private RelativeLayout bannerContent;
    private TextView tv_01;
    private TextView tv_02;
    private MyListView listView;
    private VideoListAdapter adapterVideoList;

    /**
     * 当前页面的各个Item的数据存放容器
     */
    private List<ListContent> bannerListContents = new ArrayList<>();
    private List<String> bannerTitle = new ArrayList<>();

    private View.OnClickListener tvListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_01:
                    Toast.makeText(getContext(), "正在抓紧开发中~", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_02:
                    Toast.makeText(getContext(), "正在抓紧开发中~", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_classcial, container, false);
        initAllView(view);
        initView();
        // 注册
        NetBroadcastReceiver.mListeners.add(this);

        return view;
    }

    private void initAllView(View view) {
        bannerContent = (RelativeLayout) view.findViewById(R.id.search_banner_cont);

        this.listView = (MyListView) view.findViewById(R.id.listview);
        this.tv_01 = (TextView) view.findViewById(R.id.tv_01);
        this.tv_02 = (TextView) view.findViewById(R.id.tv_02);

        adapterVideoList = new VideoListAdapter(getActivity());
        listView.setAdapter(adapterVideoList);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        init_banner();
        initListener();
    }

    private void init_banner() {
        new initBannerTask().execute();
    }

    private void initListener() {
        tv_01.setOnClickListener(tvListener);
        tv_02.setOnClickListener(tvListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNetChange() {
        if (MyApplication.mNetWorkState != NetWorkUtils.NET_TYPE_NO_NETWORK &&
                bannerListContents != null && bannerListContents.size() == 0) {
            init_banner();
        }
    }

    /**
     * 刷新数据
     */
    private class initBannerTask extends AsyncTask<Void, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(Void... voids) {
            return ((MyApplication) getActivity().getApplication()).instanceConnect().setContentURL(getConnect.UrlContentHead,
                    "1", "3");
        }

        @Override
        protected void onPostExecute(final List<ListContent> mListContents) {
            super.onPostExecute(mListContents);
            bannerListContents = mListContents;
            if (mListContents != null) {
                BannerBaseView banner = new MainBannerView(getActivity());
                List<BaseBannerBean> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add(new BaseBannerBean(mListContents.get(i).getImagelink()));
                    bannerTitle.add(mListContents.get(i).getTitle());
                }
                banner.setBannerViewOnClickListener(new BannerViewOnClickListener() {
                    @Override
                    public void BannerOnClickListener(int position) {
                        if (NetWorkUtils.isConnectNET(getActivity())) {
                            Intent intent = new Intent(getActivity(), ArticleContentActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putInt(Constant.CONTENTID, Integer.valueOf(mListContents
                                    .get(position).getId()));
                            intent.putExtras(mBundle);
                            startActivity(intent);
                        }
                    }
                });
                bannerContent.addView(banner);
                banner.setBannerTitle(bannerTitle);
                banner.update(list);
            }
        }
    }
}

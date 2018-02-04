package com.jiketuandui.antinetfraud.activity.main.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.MaterialDialog;
import com.jiketuandui.antinetfraud.activity.main.announce.AnnounceActivity;
import com.jiketuandui.antinetfraud.activity.main.SearchActivity;
import com.jiketuandui.antinetfraud.Adapter.MainTabAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.View.CFontTitleTextView;
import com.jiketuandui.antinetfraud.View.MyTabPageIndicator;
import com.jiketuandui.antinetfraud.entity.domain.AnnounceDetail;
import com.jiketuandui.antinetfraud.entity.domain.AnnounceList;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.AnnounceService;

import java.lang.reflect.Field;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainTab extends Fragment implements NetBroadcastReceiver.NetEventHandler {

    private boolean isAvailable;
    private MaterialDialog dialog;
    private AnnounceService announceService = RetrofitServiceFactory.ANNOUNCE_SERVICE;

    /**
     * 因为要在Fragment中嵌套使用ViewPager,所以需要进行初始化一些变量
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 注册
        NetBroadcastReceiver.mListeners.add(this);
        /* *
         * 使用LayoutInflate将main_tab.xml的加载进来之后,为TabPageIndicator,
         * ViewPager进行初始化
         * */
        View view = inflater.inflate(R.layout.main_tab, container, false);
        CFontTitleTextView title = view.findViewById(R.id.main_header_tv);
        title.setOnClickListener(v -> YoYo.with(Techniques.Swing)
                .duration(333)
                .repeat(6)
                .playOn(v.findViewById(R.id.main_header_tv)));
        initData(view);
        return view;
    }

    /**
     * 初始化一些变量
     */
    private void initData(View v) {
        isAvailable = true;
        /* *
         * 先实例化ViewPager,然后实例化TabPageIndicator，
         * 并且要设置TabPageIndicator和ViewPager关联，
         * 就是调用TabPageIndicator的setViewPager(ViewPager view)方法，
         * 这样子我们就实现了点击上面的Tab，下面的ViewPager切换，
         * 滑动ViewPager上面的Tab跟着切换，ViewPager的每一个Item我们使用的是Fragment，
         * 使用Fragment可以使我们的布局更加灵活一点
         * */
        MyTabPageIndicator myTabPageIndicator = v.findViewById(R.id.mainTab_indicator);
        ViewPager mViewPager = v.findViewById(R.id.mainTab_viewpager);
        mViewPager.setOffscreenPageLimit(2);
        MainTabAdapter mainTabAdapter = new MainTabAdapter(getChildFragmentManager());
        /*绑定*/
        mViewPager.setAdapter(mainTabAdapter);
        myTabPageIndicator.setViewPager(mViewPager, 0);


        ImageButton imageButton = v.findViewById(R.id.gotoSearch);
        imageButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
        ImageButton imageButtonA = v.findViewById(R.id.announcement);
        imageButtonA.setOnClickListener(view -> {
            showDialog();
            YoYo.with(Techniques.Swing)
                    .duration(333)
                    .repeat(6)
                    .playOn(v.findViewById(R.id.announcement));
        });
    }

    /**
     * 这段可以解决fragment嵌套fragment会崩溃的问题
     */
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            //参数是固定写法
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 显示公告
     */
    private void showDialog() {
        dialog = new MaterialDialog(getContext());
        if (isAvailable) {
            Context context = getContext().getApplicationContext();
            announceService.getAnnounceList(1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<AnnounceList>(context) {
                        @Override
                        protected void onHandleSuccess(AnnounceList announceList) {
                            announceService
                                    .getAnnounceDetail(announceList.getData().get(0).getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new BaseObserver<AnnounceDetail>(context) {
                                        @Override
                                        protected void onHandleSuccess(AnnounceDetail announceDetail) {
                                            String content = announceDetail.getContent();
                                            if (content.length() > 200) {
                                                content = content.substring(0, 200)
                                                        .concat("……");
                                            }
                                            dialog // 设置Dialog的属性
                                                    .title("网站公告")
                                                    // 设置内容
                                                    .content(Html.fromHtml(announceList.getData().get(0).getTitle() + ":" +
                                                            announceList.getData().get(0).getCreated_at() + "\n\u3000\u3000" +
                                                            content + "\n\u3000\u3000").toString())
                                                    // 设置按钮文本
                                                    .btnText("更多", "确定")
                                                    // 设置进入动画
                                                    .showAnim(new BounceTopEnter())
                                                    // 设置退出动画
                                                    .dismissAnim(new SlideBottomExit())
                                                    .show();
                                        }
                                    });
                        }
                    });
        }
        dialog.setOnBtnClickL(() -> {
                    Intent intent = new Intent(getActivity(), AnnounceActivity.class);
                    getContext().startActivity(intent);
                }, () -> dialog.dismiss()
        );
    }

    @Override
    public void onNetChange() {
        isAvailable = NetWorkUtils.getNetWorkState(getActivity()) == NetWorkUtils.NET_TYPE_NO_NETWORK;
    }

}

package com.jiketuandui.antinetfraud.activity.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.RefreshUtil;
import com.jiketuandui.antinetfraud.Util.StatusBarUtil;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.service.UserService;

/**
 * 用户收藏的内容
 *
 * @author wangyu
 */
public class CollectionDetailActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEventHandler {
    private RefreshUtil refreshUtil;
    private UserService userService = RetrofitServiceFactory.USER_SERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_collection);
        StatusBarUtil.StatusBarLightMode(this);

        refreshUtil = new RefreshUtil(this);
        refreshUtil.materialRefreshLayout = findViewById(R.id.refresh);
        refreshUtil.tagsRecyclerView = findViewById(R.id.recyclerView);
        refreshUtil.setOpenTop(true);
        refreshUtil.initViewAttr();
        refreshUtil.setRefreshInterface(new RefreshUtil.RefreshInterface() {
            @Override
            public void refresh() {
                long millis = MyApplication.getInstance().getMillis();
                refreshUtil.onUserRefresh(userService.getCollection(
                        MyApplication.getInstance().getUser().getId(),
                        MyApplication.getInstance().getSign(millis, "auth/collection"),
                        millis, refreshUtil.getReadPage()));
            }

            @Override
            public void loadMore() {
                long millis = MyApplication.getInstance().getMillis();
                refreshUtil.onUserLoadMore(userService.getCollection(
                        MyApplication.getInstance().getUser().getId(),
                        MyApplication.getInstance().getSign(millis, "auth/collection"),
                        millis, refreshUtil.getReadPage()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUtil.onResume();
    }

    @Override
    public void onNetChange() {
        refreshUtil.onNetChange();
    }

}

package com.jiketuandui.antinetfraud.activity.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.OtherService;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 投稿
 *
 * @author wangyu
 */
public class ShareActivity extends Activity {

    @BindView(R.id.share_title)
    AppCompatEditText shareTitle;
    @BindView(R.id.share_type)
    AppCompatSpinner shareType;
    @BindView(R.id.share_content)
    AppCompatEditText shareContent;
    @BindView(R.id.post)
    AppCompatButton post;

    private int type = 1;
    private OtherService otherService = RetrofitServiceFactory.OTHER_SERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        initListener();
    }

    /**
     * 初始化post数据
     */
    private void initData() {
        // 重置Errors
        shareTitle.setError(null);
        shareContent.setError(null);

        boolean cancel = false;
        View focusView = null;

        String title = shareTitle.getText().toString();
        String content = shareContent.getText().toString();

        // 检查经历名称
        if (TextUtils.isEmpty(title)) {
            shareTitle.setError(getString(R.string.error_invalid_title));
            focusView = shareTitle;
            cancel = true;
        }

        // 检查案例内容
        if (TextUtils.isEmpty(content)) {
            shareContent.setError(getString(R.string.error_invalid_content));
            focusView = shareContent;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            otherService.contribute(shareTitle.getText().toString(), type,
                    shareContent.getText().toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<>(this, "投稿成功！"));
        }
    }

    /**
     * 初始化响应事件
     */
    private void initListener() {
        shareType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.equals(shareType.getSelectedItem().toString(), "网络诈骗")) {
                    type = 1;
                } else if (TextUtils.equals(shareType.getSelectedItem().toString(), "电信诈骗")) {
                    type = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        post.setOnClickListener(v -> initData());
    }
}

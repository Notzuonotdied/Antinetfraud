package com.jiketuandui.antinetfraud.Activity.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.Activity.SettingActivity.CollectionDetailActivity;
import com.jiketuandui.antinetfraud.Activity.SettingActivity.HistoryDetailActivity;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.SharedPManager;

public class AccountActivity extends AppCompatActivity {

    private com.facebook.drawee.view.SimpleDraweeView accountimg;
    private android.widget.TextView collection;
    private android.widget.TextView history;
    private android.widget.Button button;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button:
                    LoginOrExit();
                    break;
                case R.id.account_img:
                    // 用户图片
                    break;
                case R.id.history:
                    startActivity(new Intent(AccountActivity.this, HistoryDetailActivity.class));
                    break;
                case R.id.collection:
                    startActivity(new Intent(AccountActivity.this, CollectionDetailActivity.class));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        initView();
        initListener();
        initButton();
    }

    private void initButton() {
        if (MyApplication.getInstance().getLogin()) {
            button.setText(getString(R.string.exit));
        } else {
            button.setText(getString(R.string.start_login));
        }
    }

    private void initListener() {
        button.setOnClickListener(listener);
        history.setOnClickListener(listener);
        collection.setOnClickListener(listener);
    }

    private void initView() {
        this.button = (Button) findViewById(R.id.button);
        this.history = (TextView) findViewById(R.id.history);
        this.collection = (TextView) findViewById(R.id.collection);
        this.accountimg = (SimpleDraweeView) findViewById(R.id.account_img);
    }

    private void LoginOrExit() {
        if (MyApplication.getInstance().getLogin()) {
            button.setText(getString(R.string.exit));
            SharedPManager sp = new SharedPManager(AccountActivity.this);
            sp.clear();
            sp.commit();
            MyApplication.getInstance().setLogin(false);
            button.setText(getString(R.string.start_login));
        } else {
            button.setText(getString(R.string.start_login));
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onResume() {
        if (MyApplication.getInstance().getLogin()) {
            button.setText(getString(R.string.exit));
        } else {
            button.setText(getString(R.string.start_login));
        }
        super.onResume();
    }
}

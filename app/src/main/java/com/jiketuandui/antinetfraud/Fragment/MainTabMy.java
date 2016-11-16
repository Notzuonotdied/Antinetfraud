package com.jiketuandui.antinetfraud.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.Activity.AboutActivity;
import com.jiketuandui.antinetfraud.Activity.FeedbackActivity;
import com.jiketuandui.antinetfraud.Activity.ShareActivity;
import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.HTTP.getImage;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.CacheCleanManage;

import java.io.File;

/**
 * 2016年9月10日 23:38:32
 * 设置页面的编写
 */
public class MainTabMy extends Fragment {
    private LinearLayout setting_pieces_delete;
    private TextView setting_cache_size;
    private com.facebook.drawee.view.SimpleDraweeView lcholderimage;
    private LinearLayout settingpiecesaccount;
    private TextView settingpiecescollection;
    private TextView settingpieceshistory;
    private TextView settingpiecessetting;
    private TextView settingpiecesidea;
    private TextView settingpiecesupdate;
    private TextView settingpiecesabout;
    private TextView settingcachesize;
    private LinearLayout settingpiecesdelete;
    private TextView settingshare;
    private String mUrl;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.setting_pieces_idea:
                    gotoActivity(FeedbackActivity.class);
                    break;
                case R.id.setting_pieces_about:
                    gotoActivity(AboutActivity.class);
                    break;
                case R.id.setting_pieces_delete:
                    boolean isSuccess = CacheCleanManage.CleanImageCache();
                    Toast.makeText(getActivity(), isSuccess ? "缓存清除成功" :
                            "缓存清除失败", Toast.LENGTH_SHORT).show();
                    setting_cache_size.setText(CacheCleanManage.getCacheSize(new File(getImage.photoPath)));
                    break;
                case R.id.setting_share:
                    gotoActivity(ShareActivity.class);
                    break;
                case R.id.setting_pieces_update:
                    new NeedRefreshTask().execute();
                    break;
                case R.id.setting_pieces_account:
                    Log.i("Notzuonotdied", "account");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_my, container, false);
        this.settingshare = (TextView) view.findViewById(R.id.setting_share);
        this.settingpiecesabout = (TextView) view.findViewById(R.id.setting_pieces_about);
        this.settingpiecesupdate = (TextView) view.findViewById(R.id.setting_pieces_update);
        this.settingpiecesidea = (TextView) view.findViewById(R.id.setting_pieces_idea);
        this.settingpiecessetting = (TextView) view.findViewById(R.id.setting_pieces_setting);
        this.settingpieceshistory = (TextView) view.findViewById(R.id.setting_pieces_history);
        this.settingpiecescollection = (TextView) view.findViewById(R.id.setting_pieces_collection);
        this.settingpiecesaccount = (LinearLayout) view.findViewById(R.id.setting_pieces_account);
        this.lcholderimage = (SimpleDraweeView) view.findViewById(R.id.lc_holder_image);
        this.setting_pieces_delete = (LinearLayout) view.findViewById(R.id.setting_pieces_delete);
        this.setting_cache_size = (TextView) view.findViewById(R.id.setting_cache_size);


        initLintener();
        return view;
    }

    /**
     * 初始化View的行为事件
     */
    private void initLintener() {
        setting_cache_size.setText(CacheCleanManage.getCacheSize(new File(getImage.photoPath)));
        setting_pieces_delete.setOnClickListener(listener);
        settingpiecesabout.setOnClickListener(listener);
        settingpiecesidea.setOnClickListener(listener);
        settingshare.setOnClickListener(listener);
        settingpiecesupdate.setOnClickListener(listener);
        settingpiecesaccount.setOnClickListener(listener);
    }

    /**
     * 跳转Activity
     */
    private void gotoActivity(Class<?> ActivityClass) {
        Intent intent = new Intent(getActivity(), ActivityClass);
        startActivity(intent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        setting_cache_size.setText(!hidden ? CacheCleanManage.getCacheSize(new File(getImage.photoPath))
//                : "0.0B");
    }

    //显示是否要更新的对话框
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.update);
        builder.setTitle("有最新版本");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    new getAPPTask().execute(mUrl);
                } else {
                    Toast.makeText(getActivity(), "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    /**
     * 安装App
     */
    public void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "Test.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    class NeedRefreshTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return getConnect.getUpdateInfo(getContext());
        }

        @Override
        protected void onPostExecute(String string) {
            if ("null".equals(string) ||string == null || string.length() <= 0 ||
                    string.isEmpty() || "".equals(string)) {
                Toast.makeText(getActivity(),"已经是最新版了~",Toast.LENGTH_SHORT).show();
            } else {
                mUrl = getConnect.UrlgetApp + string.split("/")[4].replace("\"", "");
                showUpdateDialog();
            }
            super.onPostExecute(string);
        }
    }

    class getAPPTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return getConnect.getAPP(strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                update();
            }
            super.onPostExecute(aBoolean);
        }
    }
}

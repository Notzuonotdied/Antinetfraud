package com.jiketuandui.antinetfraud.HTTP;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jiketuandui.antinetfraud.Bean.CommentInfo;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.SharedPManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.jiketuandui.antinetfraud.Util.NetWorkUtils.getApplication;

/**
 * 评论类
 * Created by wangyu on 17-3-22.
 */
public class getCommentInfo extends accessNetwork {
    private String post(String url, String content) {
        String str = null;
        try {
            str = doPost(url, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private String get(String url) {
        String str = null;
        try {
            str = doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取文章的评论数组,username="123"&token="123"
     */
    public List<CommentInfo> getComment(String articleId) {
        String json = get("/api/getComment/" + articleId);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CommentInfo>>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * psot评论,username="123"&token="123"&phone_id="123"&article_id="17"&user_id="12"&content="123"
     */
    private boolean postComment(final Context context, String articleId, String content, String phoneID) {
        if (context==null) {
            Log.i("Notzuonotdied", "context is null!");
            return false;
        }
        SharedPManager sp = new SharedPManager(context);
        String userName = sp.getString(MyApplication.getInstance().getUsername(), null);
        String userToken = sp.getString(MyApplication.getInstance().getmToken(), null);
        String userID = sp.getString(MyApplication.getInstance().getUid(), null);
        if (userName == null || userToken == null || userID == null) {
            Log.i("Notzuonotdied", "全部为空～");
            return false;
        }
        String commentString = "username=" + userName +"&token=" + userToken +"&phone_id="
            + phoneID +"&article_id=" + articleId + "&user_id=" + userID + "&content="
                + content;
        Log.i("Notzuonotdied", commentString);
        return TextUtils.equals(post("/api/comment/", commentString), "true");
    }

    //显示公告
    public void showCommentDialog(final Context context, final String articleID, final String phoneID) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
        Window window = alertDialog.getWindow();
        if (window == null) {
            return;
        }
        window.setContentView(R.layout.comment_input);
        final EditText editText = (EditText) window.findViewById(R.id.comment_edit);
        TextView cancel = (TextView) window.findViewById(R.id.comment_cancel);
        TextView confirm = (TextView) window.findViewById(R.id.comment_confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncComment(context, articleID, editText.getText().toString(), phoneID).execute();
            }
        });
    }

    private class AsyncComment extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private String articleID;
        private String content;
        private String phoneID;

        AsyncComment(Context context, String articleID, String content, String phoneID) {
            this.context = context;
            this.articleID = articleID;
            this.content = content;
            this.phoneID = phoneID;
        }

        @Override
        protected Boolean doInBackground(Void... Voids) {
            return postComment(context, articleID, content, phoneID);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "评论失败，请重试～", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aBoolean);
        }
    }

}

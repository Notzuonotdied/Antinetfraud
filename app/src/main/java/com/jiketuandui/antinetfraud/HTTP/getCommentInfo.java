package com.jiketuandui.antinetfraud.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jiketuandui.antinetfraud.Bean.CommentInfo;
import com.jiketuandui.antinetfraud.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
        String json = get("/api/getCommentInfo/" + articleId);
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
    private boolean postComment(String content) {
        return TextUtils.equals(post("api/comment/", content), "true");
    }

    //显示公告
    public void showCommentDialog(final Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
        Window window = alertDialog.getWindow();
        if (window == null) {
            return;
        }
        window.setContentView(R.layout.comment_input);
        TextView tv_title = (TextView) window.findViewById(R.id.comment_title);
        tv_title.setText("请输入评论～");
        final EditText editText = (EditText) window.findViewById(R.id.comment_input);
        TextView tv_cancel = (TextView) window.findViewById(R.id.tv_dialog_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        TextView tv_confirm = (TextView) window.findViewById(R.id.tv_dialog_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                new AsyncComment(context).execute(editText.getText().toString());
            }
        });
    }

    private class AsyncComment extends AsyncTask<String, Void, Boolean> {

        private Context context;

        AsyncComment(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... Strings) {
            return postComment(Strings[0]);
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

package com.jiketuandui.antinetfraud.View;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import org.markdownj.MarkdownProcessor;

/**
 * 2016年8月3日 09:35:43
 * MarkdownView是继承于WebView的,使用WebView能够浏览HTML的内容的功能,
 * 将用compile 'org.markdownj:markdownj-core:0.4'的支持库将我们从
 * 数据库获取的JSON数据中的Content内容(MarkDown格式)转换成我们能够查看
 * 的网页.
 *
 * 缺点:这里尚未实现图片点击响应放大缩小的功能
 * */
public class MarkdownView extends WebView {

    public MarkdownView(Context context) {
        super(context);
    }

    public MarkdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 读取MarkDown字符串并转换成一个Html.
     *
     * @param json MarkDown字符串
     */
    public void loadMarkdown(String json) {
        // 第二个参数是样式
        loadMarkdownToView(json, "file:///android_asset/paperwhite.css");
    }

    public void loadMarkdown(String json,String cssFileUrl) {
        // 第二个参数是样式
        loadMarkdownToView(json, cssFileUrl);
    }

    private void loadMarkdownToView(String json, String cssFileUrl) {
        MarkdownProcessor m = new MarkdownProcessor();
        String html = m.markdown(json);
        if (cssFileUrl != null) {
            html = 	"<link rel='stylesheet' type='text/css' href='"+ cssFileUrl +"' />" + html;
        }
        loadDataWithBaseURL("fake://", html, "text/html", "UTF-8", null);
    }
}

//   !\!\[.*\]\((.*?)\)!
//   !\!\[(.*?)\]\(/(.*?)\)!
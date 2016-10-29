package com.jiketuandui.antinetfraud.Util;

/**
 * Created by Notzuonotdied on 2016/8/1.
 * 这个是自定的列表项目事件监听接口类,主要是为了拟补RecycleView不能监控的缺点
 */
public interface ListConentOnClickListener {
     void OnItemClickListener(int position);
     void TOPOnClickLinstener(int position);
}

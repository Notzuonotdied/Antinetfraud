package com.jiketuandui.antinetfraud.Interface;

/**
 * Created by Notzuonotdied on 2016/8/1.
 * 这个是自定的列表项目事件监听接口类,主要是为了拟补RecycleView不能监控的缺点
 */
public interface ListContentOnClickListener {
     void OnItemClickListener(int position);
     void TOPOnClickListener(int position);
}

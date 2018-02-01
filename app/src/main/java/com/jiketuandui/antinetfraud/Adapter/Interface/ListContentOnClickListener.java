package com.jiketuandui.antinetfraud.Adapter.Interface;

/**
 * 这个是自定的列表项目事件监听接口类,主要是为了拟补RecycleView不能监控的缺点
 *
 * @author Notzuonotdied
 * @date 2016/8/1
 */
public interface ListContentOnClickListener {
    /**
     * 点击Item的时候的响应事件
     */
    void onItemClickListener(int position);

    /**
     * 点击顶部标签栏的响应事件
     */
    void onTopClickListener(int position);
}

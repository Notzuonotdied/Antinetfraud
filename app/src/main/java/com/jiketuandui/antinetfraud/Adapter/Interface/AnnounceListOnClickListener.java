package com.jiketuandui.antinetfraud.Adapter.Interface;

/**
 * 这个是自定的列表项目事件监听接口类,主要是为了拟补RecycleView不能监控的缺点
 *
 * @author Notzuonotdied
 * @date 2017年3月9日 20:51:06
 */
public interface AnnounceListOnClickListener {
    void onItemClickListener(int position);
}

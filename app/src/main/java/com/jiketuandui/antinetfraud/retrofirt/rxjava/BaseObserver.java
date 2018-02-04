package com.jiketuandui.antinetfraud.retrofirt.rxjava;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.jiketuandui.antinetfraud.entity.dto.Result;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 观察者基类
 *
 * @author wangyu
 */
public class BaseObserver<T> implements Observer<Result<T>> {

    private static final String TAG = "BaseObserver";
    private Context mContext;
    private String hint;

    public BaseObserver() {
        this.mContext = null;
        this.hint = null;
    }

    public BaseObserver(Context context) {
        this.mContext = context.getApplicationContext();
        this.hint = null;
    }

    public BaseObserver(Context context, String hint) {
        this.mContext = context.getApplicationContext();
        this.hint = hint;
    }

    @Override
    public void onSubscribe(Disposable d) {
        Log.i(TAG, "onSubscribe: 被订阅啦～");
    }

    @Override
    public void onNext(Result<T> tResult) {
        Log.i(TAG, "onNext: " + tResult.toString());
        switch (tResult.getCode()) {
            case 401:
                ToastUtils.showShort("用户身份失效，请重新登陆！");
                break;
            case 200:
                if (hint != null && mContext != null) {
                    ToastUtils.showShort(hint);
                }
                break;
            case 404:
                ToastUtils.showShort("没有数据喔～");
                break;
            case 500:
                ToastUtils.showShort("错误！");
                break;
            default:
                break;
        }

        if (tResult.isSuccess() && tResult.getData() != null) {
            T t = tResult.getData();
            onHandleSuccess(t);
        } else {
            onHandleFailure(tResult.getMessage());
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "error:" + e.toString());
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete");
    }

    /**
     * 当网络请求成功的时候回调
     *
     * @param t 回调参数
     */
    protected void onHandleSuccess(T t) {

    }

    /**
     * 当网络请求失败的时候回调
     *
     * @param message 回调参数
     */
    protected void onHandleFailure(String message) {

    }
}

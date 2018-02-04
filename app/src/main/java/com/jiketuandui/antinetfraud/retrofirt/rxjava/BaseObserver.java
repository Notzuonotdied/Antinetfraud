package com.jiketuandui.antinetfraud.retrofirt.rxjava;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiketuandui.antinetfraud.entity.dto.Result;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;


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
        if (tResult.isSuccess() && hint != null && mContext != null) {
            Toast.makeText(mContext.getApplicationContext(), hint,
                    Toast.LENGTH_SHORT).show();
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

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            Gson gson = new Gson();
            Result result = gson.fromJson(responseBody.string(), Result.class);
            return result.toErrorString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}

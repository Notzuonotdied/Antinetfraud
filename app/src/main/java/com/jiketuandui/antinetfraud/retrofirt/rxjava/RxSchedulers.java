package com.jiketuandui.antinetfraud.retrofirt.rxjava;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * <p>TODO</p>
 *
 * @author <a href="mailto:hhjian.top@qq.com">hhjian</a>
 * @since 17-12-31
 */

public class RxSchedulers {

    public static <T> ObservableTransformer<T, T> compose() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {

                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}

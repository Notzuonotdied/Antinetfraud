package com.jiketuandui.antinetfraud.Util;

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.jiketuandui.antinetfraud.entity.domain.User;

import java.util.UUID;

import static com.jiketuandui.antinetfraud.Util.Constants.USER_DATA;

/**
 * @author Notzuonotdied
 * @date 2016/8/
 */
public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getName();
    /**
     * 当前网络状态
     */
    public static int NetWorkState;
    private static MyApplication myApplication;
    /**
     * 用户信息
     */
    private User user;

    /**
     * 单例模式
     */
    public static MyApplication getInstance() {
        if (myApplication == null) {
            synchronized (MyApplication.class) {
                if (myApplication == null) {
                    myApplication = new MyApplication();
                }
            }
        }
        return myApplication;
    }

    @Override
    public void onCreate() {
        // 初始化Fresco类
        Fresco.initialize(this);
        // 初始化网络监听机制
        initNETService();
        // 初始化工具
        Utils.init(this);
        super.onCreate();
    }

    /**
     * 初始化Service进行监听网络
     */
    public void initNETService() {
        NetWorkState = NetWorkUtils.getNetWorkState(MyApplication.this);
    }

    /**
     * MAC
     */
    public String getMAC() {
        return getUniquePseudoID();
    }

    public String getUnique() {
        return "2333" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.DEVICE.length() % 10 + Build.USER.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                "2333" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.DEVICE.length() % 10 + Build.USER.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10;
    }

    public String getUniquePseudoID() {
        String serial;

        String mSzdevidshort = "233" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.DEVICE.length() % 10 + Build.USER.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10; //12 位
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(mSzdevidshort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // serial需要一个初始化
            // 随便一个初始化
            serial = "serial";
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(mSzdevidshort.hashCode(), serial.hashCode()).toString();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLogin() {
        return getUser() != null && getUser().getToken() != null;
    }

    /**
     * 保存数据
     *
     * @param user 用户数据
     */
    public void storageData(final User user) {
        Log.i(TAG, "storageData: " + new Gson().toJson(user));
        // 加密保存
//        byte[] what = EncryptUtils.encryptAES(
//                new Gson().toJson(user).getBytes(),
//                ConvertUtils.hexString2Bytes(MyApplication.getInstance().getUnique()),
//                "AES/ECB/NoPadding", null);
        SPUtils.getInstance().put(USER_DATA, new Gson().toJson(user), true);
        MyApplication.getInstance().setUser(user);
    }

    public void recoverData() {
        String string = SPUtils.getInstance().getString(USER_DATA);
        if (TextUtils.isEmpty(string)) {
            setUser(null);
        } else {
//            Log.i(TAG, "recoverData，秘钥：" + MyApplication.getInstance().getUnique());
//            Log.i(TAG, "recoverData，加密的数据：" + string);
//            byte[] json = EncryptUtils.decryptAES(ConvertUtils.hexString2Bytes(string),
//                    ConvertUtils.hexString2Bytes(MyApplication.getInstance().getUnique()),
//                    "AES/ECB/NoPadding", null);
//            if (json == null || json.length == 0) {
//                Log.i(TAG, "recoverData: 解密出来的数据为空！");
//                setUser(null);
//            } else {
            Log.i(TAG, "recoverData，解密出来的数据：" + string);
            User user = new Gson().fromJson(string, User.class);
            setUser(user);
        }
    }

    public String getSign(long millis, String auth) {
        // token+当前时间的unix时间戳+api的路由
        String string = EncryptUtils.encryptMD5ToString(getUser().getToken()
                + millis + auth);
        return EncryptUtils.encryptMD5ToString(string);
    }

    public long getMillis() {
        return TimeUtils.date2Millis(TimeUtils.getNowDate());
    }
}

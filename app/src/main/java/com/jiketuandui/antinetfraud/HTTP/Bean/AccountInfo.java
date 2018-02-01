package com.jiketuandui.antinetfraud.HTTP.Bean;

/**
 * 用户的信息类
 * Created by Notzuonotdied on 2017/3/16.
 */

public class AccountInfo {

    /**
     * uid : 5
     * user : 66666666666
     * password :
     * email :
     * token : $2y$10$tTC169z5.eHic4ezSnAAluR4vvAsUxQp3Y8p7bee1mev/WNqwFFYq
     * phone_id : 123
     * token_time : 1489575000
     */

    private String uid;
    private String user;
    private String password;
    private String email;
    private String token;
    private String phone_id;
    private int token_time;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPhone_id(String phone_id) {
        this.phone_id = phone_id;
    }

    public void setToken_time(int token_time) {
        this.token_time = token_time;
    }

    public String getUid() {
        return uid;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getPhone_id() {
        return phone_id;
    }

    public int getToken_time() {
        return token_time;
    }
}

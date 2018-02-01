package com.jiketuandui.antinetfraud.entity.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>登录输入</p>
 *
 * @author <a href="mailto:hhjian.top@qq.com">hhjian</a>
 * @since 17-11-10
 */

@Data
@NoArgsConstructor
public class LoginBO {
    private String username;
    private String password;

    public LoginBO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

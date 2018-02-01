package com.jiketuandui.antinetfraud.entity.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>用户</p>
 *
 * @author <a href="mailto:hhjian.top@qq.com">hhjian</a>
 * @since 17-11-13
 */

@Data
@NoArgsConstructor
public class User {
    private String userId;
    private String username;
    private String nickname;
}

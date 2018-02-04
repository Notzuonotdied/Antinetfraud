package com.jiketuandui.antinetfraud.entity.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyu
 * @data 18-2-4
 * @describe TODO
 */
@Data
@NoArgsConstructor
public class App {

    private int code;
    private String message;
    private AppBean app;

    @Data
    @NoArgsConstructor
    public static class AppBean {
        private int id;
        private String version;
        private String update_log;
        private String created_at;
        private String updated_at;
        private String download;
    }
}

package com.yawarSoft.interoperability.Utils;

import java.time.Duration;

public class Constants {
    private Constants() {
    }

    public static Long getTimeToken(){
        Duration expiration = Duration.ofHours(4);
        return System.currentTimeMillis() + expiration.toMillis();
    }
}

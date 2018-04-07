package com.shadowsocks.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SleepUtils {

    private SleepUtils() {
    }

    public static void sleep(int timeInMilliseconds) {
        try {
            Thread.sleep(timeInMilliseconds);
        }catch (Exception e) {
            log.error("{}", e);
        }
    }
}

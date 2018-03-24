package com.shadowsocks.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MD5Utils {

    public static String encode(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder builder = new StringBuilder();
            for(byte element : array) {
                builder.append(Integer.toHexString((element & 0xFF) | 0x100).substring(1, 3));
            }
            return builder.toString();
        } catch (Exception e) {
            log.error("{}", e);
        }
        return "";
    }
}

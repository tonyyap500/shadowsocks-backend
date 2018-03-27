package com.shadowsocks.utils;

import com.google.gson.Gson;
import com.shadowsocks.dto.entity.ShadowsocksConfig;

import java.util.Map;

public class ShadowsocksUtils {

    private ShadowsocksUtils() {
    }

    public static String buildShadowsocksConfig(Map<String, String> portPasswordMap) {
        ShadowsocksConfig shadowsocksConfig = ShadowsocksConfig.builder()
                .server("0.0.0.0")
                .localAddress("127.0.0.1")
                .localPort(1080)
                .portAndPassword("place")
                .timeout(300)
                .method("aes-256-cfb")
                .fastOpen(false)
                .build();
        String json = new Gson().toJson(shadowsocksConfig);
        json = json.replace("\"place\"", new Gson().toJson(portPasswordMap));
        return json;
    }
}

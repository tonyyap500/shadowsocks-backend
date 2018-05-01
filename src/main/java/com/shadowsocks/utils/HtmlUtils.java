package com.shadowsocks.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class HtmlUtils {

    private HtmlUtils() {
    }

    private static String buildContent(String fileName) {
        String folder = System.getProperty("user.dir");
        Path path = Paths.get(folder, fileName);

        try {
            InputStream in = Files.newInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder builder = new StringBuilder();
            String content;
            while ((content = reader.readLine()) != null) {
                builder.append(content);
            }
            in.close();
            reader.close();
            return builder.toString();
        }catch (Exception e) {
            log.error("{}", e);
        }
        return "";
    }

    public static String getActiveHtmlPattern() {
        String fileName = "active.html";
        return buildContent(fileName);
    }

    public static String getPurchaseHtmlPattern() {
        String fileName = "purchase.html";
        return buildContent(fileName);
    }

    public static String getNotifyToReloadHtml() {
        String fileName = "notify.html";
        return buildContent(fileName);
    }
}

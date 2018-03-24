package com.shadowsocks.utils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomStringUtils {

    private RandomStringUtils() {
    }

    public static String generateRandomString() {
        List<Integer> list1 = IntStream.generate(() -> new Random().nextInt(125)).filter(v -> (v >= 65 && v <= 90) || (v >= 97 && v <= 122)).limit(16).boxed().collect(Collectors.toList());
        StringBuilder builder = new StringBuilder();
        list1.forEach(v -> builder.append((char)v.intValue()));
        return MD5Utils.encode(builder.toString());
    }
}

package com.shadowsocks.utils;

import java.math.BigDecimal;

public class DecimalUtils {

    private static final int NUMBER_OF_DECIMAL = 2;

    private DecimalUtils() {
    }

    public static double halfRoundUp(double value) {
        return BigDecimal.valueOf(value).setScale(NUMBER_OF_DECIMAL, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double roundDown(double value) {
        return BigDecimal.valueOf(value).setScale(NUMBER_OF_DECIMAL, BigDecimal.ROUND_DOWN).doubleValue();
    }
}

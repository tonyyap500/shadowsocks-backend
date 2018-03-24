package com.shadowsocks.utils;


public class SessionKeyUtils {
	private static final String SESSION_KEY = "captcha";
	private static final String TOKEN_KEY = "token";
	private static final String USER_LEVEL = "level";
	private static final String TOPUP_VALUE = "topUp";
	private static final String ORDER_NO = "orderNumber";

	private SessionKeyUtils() {
	}

	public static String getKeyForCaptcha() {
		return SESSION_KEY;
	}

	public static String getKeyForToken() {
		return TOKEN_KEY;
	}

	public static String getKeyForUserLevel() {
		return USER_LEVEL;
	}

	public static String getKeyForTopUpValue() {
		return TOPUP_VALUE;
	}

	public static String getKeyForOrderNumber() {
		return ORDER_NO;
	}
}

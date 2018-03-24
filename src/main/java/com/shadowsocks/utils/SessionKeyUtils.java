package com.shadowsocks.utils;


public class SessionKeyUtils {
	private static final String KEY_FOR_CAPTCHA = "captcha";
	private static final String KEY_FOR_USER = "user";

	private SessionKeyUtils() {
	}

	public static String getKeyForCaptcha() {
		return KEY_FOR_CAPTCHA;
	}

	public static String getKeyForUser() {
		return KEY_FOR_USER;
	}
}

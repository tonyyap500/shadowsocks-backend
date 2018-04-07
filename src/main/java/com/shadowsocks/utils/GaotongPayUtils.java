package com.shadowsocks.utils;

import com.shadowsocks.dto.GaotongCallbackDto;
import com.shadowsocks.dto.GaotongPayDto;

public class GaotongPayUtils {

	private GaotongPayUtils() {
	}

	private static String buildSign(GaotongPayDto dto) {
		String signPattern = "partner=%s&banktype=%s&paymoney=%d&ordernumber=%s&callbackurl=%s%s";
		String original = String.format(signPattern, dto.getMerchantNo(), dto.getPaymentType(), dto.getMoney(), dto.getTransactionNo(), dto.getCallbackURL(), dto.getPublicKey());
		return MD5Utils.encode(original);
	}

	public static String buildPaymentURL(GaotongPayDto dto) {
		String sign = buildSign(dto);
		String gaotongURLPattern = "https://wgtj.gaotongpay.com/PayBank.aspx?banktype=%s&partner=%s&paymoney=%d&ordernumber=%s&callbackurl=%s&hrefbackurl=%s&attach=%s&sign=%s";
		return String.format(gaotongURLPattern, dto.getPaymentType(), dto.getMerchantNo(), dto.getMoney(), dto.getTransactionNo(), dto.getCallbackURL(), dto.getCallbackURL(), dto.getRemark(), sign);
	}

	public static boolean checkSign(GaotongCallbackDto callbackDto) {
		if(!callbackDto.getOrderStatus().equals("1")) {
			return false;
		}
		String sign = MD5Utils.encode(callbackDto.toString());
		return callbackDto.getSign().equals(sign);
	}
}

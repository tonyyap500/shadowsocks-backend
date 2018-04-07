package com.shadowsocks.dto;

import com.shadowsocks.dto.enums.GaotongEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GaotongPayDto {

	/**
	 * 商户号
	 * */
	private String merchantNo;

	/**
	 * 公钥
	 * */
	private String publicKey;

	/**
	 * 支付方式
	 * */
	private GaotongEnum paymentType;

	/**
	 * 支付金额
	 * */
	private int money;

	/**
	 * 订单号
	 * */
	private String transactionNo;

	/**
	 * 回调地址
	 * */
	private String callbackURL;

	/**
	 * 备注信息
	 * */
	private String remark;
}

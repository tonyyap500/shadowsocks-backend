package com.shadowsocks.dto;

import com.shadowsocks.dto.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMessageDto {
	/**
	 * 是否成功
	 * */
	private ResultEnum result;

	/**
	 * 返回消息
	 * */
	private String message;
}

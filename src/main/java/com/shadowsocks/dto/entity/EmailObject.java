package com.shadowsocks.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailObject {

	/**
	 * 收件人
	 * */
	private List<String> toList;

	/**
	 * 主题
	 * */
	private String subject;

	/**
	 * 内容
	 * */
	private String content;
}

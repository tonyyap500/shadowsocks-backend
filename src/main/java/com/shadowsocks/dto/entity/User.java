package com.shadowsocks.dto.entity;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	/**
	 * 用户id
	 * */
	private int id;

	/**
	 * 用户名
	 * */
	private String username;

	/**
	 * 密码
	 * */
	private String password;

	/**
	 * 邮箱
	 * */
	private String email;

	/**
	 * 注册时间
	 * */
	private String registerTime;

	/**
	 * 注册IP地址
	 * */
	private String registerIP;

	/**
	 * 上次登录时间
	 * */
	private String lastLoginTime;

	/**
	 * 上次登录IP地址
	 * */
	private String lastLoginIP;

	/**
	 * 累计登录次数
	 * */
	private int loginTimes;

	/**
	 * 激活状态
	 * */
	private String activeStatus;
}

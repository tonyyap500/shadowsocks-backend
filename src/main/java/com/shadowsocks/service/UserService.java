package com.shadowsocks.service;

import com.shadowsocks.dto.entity.User;


public interface UserService {

	/**
	 * 检测用户名是否被占用
	 * */
	boolean isUsernameTaken(String username);

	/**
	 * 检测邮箱是否被占用
	 * */
	boolean isEmailTaken(String email);

	/**
	 * 注册
	 * */
	int register(User user);

	/**
	 * 激活
	 * */
	int active(String username);

	/**
	 * 登录
	 * */
	User login(User user);

	/**
	 * 更新登录信息
	 * */
	int update(User user);

	/**
	 * 根据用户名查询用户id
	 * */
	Integer findUserIdByUsername(String username);
}

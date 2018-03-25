package com.shadowsocks.service;

import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.request.LoginDto;

import java.util.Optional;


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
	boolean register(User user);

	/**
	 * 根据邮件查找激活码
	 * */
	Optional<String> findActiveCodeByEmail(String email);

	/**
	 * 激活
	 * */
	boolean active(String username);

	/**
	 * 登录
	 * */
	Optional<User> login(LoginDto loginDto);

	/**
	 * 更新登录信息
	 * */
	int update(User user);

	/**
	 * 根据用户名查询用户id
	 * */
	Integer findUserIdByUsername(String username);
}

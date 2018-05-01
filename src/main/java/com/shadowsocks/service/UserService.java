package com.shadowsocks.service;

import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.request.BindBankCardRequestDto;
import com.shadowsocks.dto.request.LoginDto;
import jdk.nashorn.internal.runtime.options.Option;

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
	 * 根据激活码查询用户
	 * */
	Optional<User> findUserByActiveCode(String activeCode);

	/**
	 * 更新登录信息
	 * */
	void updateLoginInfo(User user, String ip);

	/**
	 * 根据用户名查询用户
	 * */
	Optional<User> findUserByUsername(String username);

	/**
	 * 绑定银行卡
	 * */
	boolean bindBankCard(User user, BindBankCardRequestDto bindBankCardRequestDto);

	/**
	 * 根据用户名查询用户id
	 * */
	Integer findUserIdByUsername(String username);

	/**
	 * 根据用户id查询用户
	 * */
	Optional<User> findUserById(int id);
}

package com.shadowsocks.service.impl;

import com.shadowsocks.dao.UserDao;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.request.BindBankCardRequestDto;
import com.shadowsocks.dto.request.LoginDto;
import com.shadowsocks.service.UserService;
import com.shadowsocks.utils.CacheUtils;
import jdk.nashorn.internal.runtime.options.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Resource
	private UserDao userDao;


	@Override
	public boolean isUsernameTaken(String username) {
		int result = userDao.checkUsername(username);
		return result > 0;
	}

	@Override
	public boolean isEmailTaken(String email) {
		int result = userDao.checkEmail(email);
		return result > 0;
	}

	@Override
	public boolean register(User user) {
		return userDao.register(user) == 1;
	}

	@Override
	public Optional<String> findActiveCodeByEmail(String email) {
		String activeCode = userDao.findActiveCodeByEmail(email);
		return Optional.ofNullable(activeCode);
	}

	@Override
	public boolean active(String activeCode) {
		return userDao.active(activeCode) == 1;
	}

	@Override
	public Optional<User> findUserByActiveCode(String activeCode) {
		User user = userDao.findUserByActiveCode(activeCode);
		return Optional.ofNullable(user);
	}

	public void updateLoginInfo(User user, String ip) {
		int loginTimes = user.getLoginTimes();
		loginTimes = loginTimes + 1;
		user.setLoginTimes(loginTimes);
		String time = LocalDateTime.now().format(formatter);
		userDao.updateUserById(user.getId(), loginTimes, ip, time);
	}

	@Override
	public Optional<User> findUserByUsername(String username) {
		User user = userDao.findUserByUsername(username);
		return Optional.ofNullable(user);
	}

	@Override
    public boolean bindBankCard(User user, BindBankCardRequestDto bindBankCardRequestDto) {
		user.setRealName(bindBankCardRequestDto.getRealName());
		user.setBankCardNo(bindBankCardRequestDto.getBankCardNo());
		user.setWithdrawPassword(bindBankCardRequestDto.getWithdrawPassword());

		CacheUtils.put(user.getToken(), user, 3600);
		int result = userDao.bindBankCard(user);
		return result == 1;
    }

	@Override
	public Integer findUserIdByUsername(String username) {
		return userDao.findUserIdByUsername(username);
	}
}

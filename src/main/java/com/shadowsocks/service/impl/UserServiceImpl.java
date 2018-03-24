package com.shadowsocks.service.impl;

import com.shadowsocks.dao.UserDao;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.request.LoginDto;
import com.shadowsocks.service.UserService;
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
	public boolean active(String activeCode) {
		return userDao.active(activeCode) == 1;
	}

	@Override
	public Optional<User> login(LoginDto loginDto) {
		User user = userDao.login(loginDto.getUsername().toLowerCase(), loginDto.getPassword());
		if(Objects.nonNull(user)) {
			int loginTimes = user.getLoginTimes();
			loginTimes = loginTimes + 1;
			user.setLoginTimes(loginTimes);
			String time = LocalDateTime.now().format(formatter);
			userDao.updateUserById(user.getId(), loginTimes, loginDto.getIp(), time);
		}
		return Optional.ofNullable(user);
	}

    @Override
    public int update(User user) {
        return userDao.update(user);
    }

	@Override
	public Integer findUserIdByUsername(String username) {
		return userDao.findUserIdByUsername(username);
	}
}

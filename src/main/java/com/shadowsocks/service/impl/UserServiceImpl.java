package com.shadowsocks.service.impl;

import com.shadowsocks.dao.UserDao;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

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
	public int register(User user) {
		return userDao.register(user);
	}

	@Override
	public int active(String username) {
		return userDao.active(username);
	}

	@Override
	public User login(User user) {
		return userDao.login(user);
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

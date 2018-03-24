package com.shadowsocks.dao;

import com.shadowsocks.dto.entity.User;
import org.apache.ibatis.annotations.*;
import org.mapstruct.Mapper;


@Mapper
public interface UserDao {

	String BASE_RESULT = "appBaseResult";
	String TABLE_NAME = "user";

	@Select("select count(*) from " + TABLE_NAME + " where username=#{username}")
	int checkUsername(String username);

	@Select("select count(*) from " + TABLE_NAME + " where email=#{email}")
	int checkEmail(String email);

	@Insert("insert into " + TABLE_NAME + " (username, password, email, register_ip, active_status, login_times, register_time) values(#{username}, #{password}, #{email}, #{registerIP}, #{activeStatus}, #{loginTimes}, #{registerTime})")
	int register(User user);

	@Update("update " + TABLE_NAME + " set active_status='ACTIVE' where username=#{username}")
	int active(String username);

	@Select("select id, username, password, login_times, active_status from " + TABLE_NAME + " where username=#{username}")
	@Results(
			id = BASE_RESULT,
			value = {
					@Result(property = "id", column = "id"),
					@Result(property = "username", column = "username"),
					@Result(property = "password", column = "password"),
					@Result(property = "loginTimes", column = "login_times"),
					@Result(property = "activeStatus", column = "active_status")
			}
	)
    User login(User user);

	@Select("select id from " + TABLE_NAME + " where username=#{username}")
	Integer findUserIdByUsername(String username);

	@Update("update " + TABLE_NAME + " set login_times=#{loginTimes}, last_login_ip=#{lastLoginIP}, last_login_time=#{lastLoginTime} where id=#{id}")
	int update(User user);
}

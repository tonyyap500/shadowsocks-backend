package com.shadowsocks.dao;

import com.shadowsocks.dto.entity.User;
import org.apache.ibatis.annotations.*;
import org.mapstruct.Mapper;


@Mapper
public interface UserDao {

	String BASE_RESULT = "userResult";
	String TABLE_NAME = "user";

	@Select("select count(*) from " + TABLE_NAME + " where username=#{username}")
	int checkUsername(String username);

	@Select("select count(*) from " + TABLE_NAME + " where email=#{email}")
	int checkEmail(String email);

	@Insert("insert into " + TABLE_NAME + "(username, password, email, inviter, register_ip, register_time, " +
			"login_times, last_login_time, last_login_ip, active_status, active_code)" + "values(" +
			"#{username}, #{password}, #{email}, #{inviter}, #{registerIp}, #{registerTime}, #{loginTimes}, " +
            "#{lastLoginTime}, #{lastLoginIp}, #{activeStatus}, #{activeCode})")
	int register(User user);


	@Select("select active_code from " + TABLE_NAME + " where email=#{email}")
	String findActiveCodeByEmail(@Param("email") String email);

	@Update("update " + TABLE_NAME + " set active_status='ACTIVE' where active_status='NON_ACTIVE' and active_code=#{activeCode}")
	int active(@Param("activeCode") String activeCode);

	@Select("select * from " + TABLE_NAME + " where username=#{username} or email=#{username}")
	@Results(
			id = BASE_RESULT,
			value = {
					@Result(property = "id", column = "id"),
					@Result(property = "username", column = "username"),
					@Result(property = "password", column = "password"),
					@Result(property = "email", column = "email"),
					@Result(property = "inviter", column = "inviter"),
					@Result(property = "registerIp", column = "register_ip"),
					@Result(property = "registerTime", column = "register_time"),
					@Result(property = "loginTimes", column = "login_times"),
					@Result(property = "lastLoginTime", column = "last_login_time"),
					@Result(property = "lastLoginIp", column = "last_login_ip"),
					@Result(property = "activeStatus", column = "active_status"),
					@Result(property = "activeCode", column = "activeCode"),
					@Result(property = "realName", column = "real_name"),
					@Result(property = "bankCardNo", column = "bank_card_no"),
					@Result(property = "withdrawPassword", column = "withdraw_password")
			}
	)
	User findUserByUsername(@Param("username") String username);

	@Select("select * from " + TABLE_NAME + " where active_code=#{activeCode}")
	@ResultMap(BASE_RESULT)
	User findUserByActiveCode(@Param("activeCode") String activeCode);

	@Select("select * from " + TABLE_NAME + " where id=#{id}")
	@ResultMap(BASE_RESULT)
	User findUserById(@Param("id") int id);

	@Update("update " + TABLE_NAME + " set login_times=#{loginTimes}, last_login_ip=#{lastLoginIp}, last_login_time=#{lastLoginTime} where id=#{id}")
    void updateUserById(@Param("id") int id, @Param("loginTimes") int loginTimes, @Param("lastLoginIp") String lastLoginIp, @Param("lastLoginTime") String lastLoginTime);

	@Select("select id from " + TABLE_NAME + " where username=#{username}")
	Integer findUserIdByUsername(String username);

	@Update("update " + TABLE_NAME + " set real_name=#{realName}, bank_card_no=#{bankCardNo}, withdraw_password=#{withdrawPassword} where id=#{id}")
	int bindBankCard(User user);
}

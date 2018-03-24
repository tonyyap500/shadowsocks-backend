package com.shadowsocks.dao;

import com.shadowsocks.dto.entity.EmailConfig;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EmailDao {

	String BASE_RESULT = "emailBaseResult";
	String TABLE_NAME = "emails";

	@Select("select id, username, password, smtp from " + TABLE_NAME + " where status='ACTIVE' limit 0, 20")
	@Results(
			id = BASE_RESULT,
			value = {
					@Result(property = "id", column = "id"),
					@Result(property = "username", column = "username"),
					@Result(property = "password", column = "password"),
					@Result(property = "smtpServer", column = "smtp")
			}
	)
	List<EmailConfig> findEmailConfigs();
}

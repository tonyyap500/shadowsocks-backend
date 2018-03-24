package com.shadowsocks.service;


import com.shadowsocks.dto.entity.EmailConfig;

import java.util.List;

public interface EmailService {

	/**
	 * 查找可用的邮箱
	 * */
	List<EmailConfig> findEmailConfigs();
}

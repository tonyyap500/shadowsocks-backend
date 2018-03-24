package com.shadowsocks.service.impl;

import com.google.common.collect.Lists;
import com.shadowsocks.dao.EmailDao;
import com.shadowsocks.dto.entity.EmailConfig;
import com.shadowsocks.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

	@Resource
	private EmailDao emailDao;

	public List<EmailConfig> findEmailConfigs() {
		List<EmailConfig> emailConfigList = emailDao.findEmailConfigs();
		if(!CollectionUtils.isEmpty(emailConfigList)) {
			return emailConfigList;
		}

		return Lists.newArrayList();
	}
}

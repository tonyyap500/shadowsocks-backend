package com.shadowsocks.utils;


import com.shadowsocks.dto.entity.EmailConfig;
import com.shadowsocks.dto.entity.EmailObject;
import com.shadowsocks.dto.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Slf4j
public class EmailUtils {

	private EmailUtils() {
	}

	private static Properties buildEmailProperties(EmailConfig emailConfig) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.host", emailConfig.getSmtpServer());
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", "465");
		props.put("mail.debug", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		return props;
	}

	private static Address[] buildAddresses(EmailObject emailObject) {
		return emailObject.getToList().stream().map(to -> {
			try {
				return new InternetAddress(to);
			}catch (Exception e) {
				log.error("{}", e);
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList()).toArray(new Address[0]);
	}

	private static ResultEnum sendEmail(List<EmailConfig> emailConfigList, EmailObject emailObject) {
        int index = new Random().nextInt(emailConfigList.size());
		EmailConfig emailConfig = emailConfigList.get(index);
		Properties props = buildEmailProperties(emailConfig);
		Session session = Session.getInstance(props);
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setSubject(emailObject.getSubject());
			msg.setText(emailObject.getContent(), "utf-8", "html");
			msg.setFrom(new InternetAddress("404Here"));
			Transport transport = session.getTransport();
			transport.connect(emailConfig.getUsername(), emailConfig.getPassword());
			Address[] addresses = buildAddresses(emailObject);
			transport.sendMessage(msg, addresses);
			transport.close();
		}catch (Exception e) {
            emailObject.getToList().forEach(to -> log.info("邮件发送失败， 发件人 {}， 收件人 {}", emailConfig.getUsername() + "@" + emailConfig.getSmtpServer().replace("smtp.", ""), to));
			log.error("{}", e);
			return ResultEnum.FAIL;
		}
		emailObject.getToList().forEach(to -> log.info("成功发送邮件从 {} 至 {}", emailConfig.getUsername() + "@" + emailConfig.getSmtpServer().replace("smtp.", ""), to));
		return ResultEnum.SUCCESS;
	}

	public static void sendEmailAsyc(List<EmailConfig> emailConfigList, EmailObject emailObject) {
		new Thread(() -> sendEmail(emailConfigList, emailObject)).start();
	}
}

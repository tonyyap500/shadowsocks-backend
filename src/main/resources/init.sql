CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `inviter` int(10) DEFAULT NULL,
  `register_ip` varchar(20) NOT NULL,
  `register_time` varchar(30) NOT NULL,
  `login_times` int(10) DEFAULT NULL,
  `last_login_time` varchar(30) DEFAULT NULL,
  `last_login_ip` varchar(20) DEFAULT NULL,
  `active_status` varchar(20) NOT NULL,
  `active_code` varchar(40) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_unique_key` (`username`) USING HASH,
  UNIQUE KEY `email_unique_key` (`email`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;


CREATE TABLE `emails` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `smtp` varchar(255) DEFAULT NULL,
  `pop3` varchar(255) DEFAULT NULL,
  `imap` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of emails
-- ----------------------------
INSERT INTO `emails` VALUES ('1', 'katherine00601', 'StevenMichael88', 'smtp.gmail.com', null, null, 'ACTIVE');
INSERT INTO `emails` VALUES ('2', 'katherine00604', 'StevenMichael88', 'smtp.gmail.com', null, null, 'ACTIVE');
INSERT INTO `emails` VALUES ('3', 'katherine00605', 'StevenMichael88', 'smtp.gmail.com', null, null, 'ACTIVE');
CREATE DATABASE shadowsocks CHARACTER SET utf8 COLLATE utf8_general_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE `user` ADD real_name VARCHAR(40) DEFAULT NULL;
ALTER TABLE `user` ADD bank_card_no VARCHAR(40) DEFAULT NULL;
ALTER TABLE `user` ADD withdraw_password VARCHAR(20) DEFAULT NULL;
CREATE UNIQUE INDEX `index_bank_card_no` ON `user`(bank_card_no) USING HASH;


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



CREATE TABLE `server` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `country` varchar(30) NOT NULL,
  `country_in_chinese` varchar(30) NOT NULL,
  `city` varchar(30) NOT NULL,
  `city_in_chinese` varchar(30) NOT NULL,
  `domain` varchar(40) NOT NULL,
  `port` varchar(10) NOT NULL,
  `password` varchar(40) NOT NULL,
  `status` varchar(20) NOT NULL,
  `current_owner` int(10) DEFAULT NULL,
  `update_time` varchar(30) DEFAULT NULL,
  PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `balance` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `balance` double NOT NULL,
  `create_time` varchar(30) NOT NULL,
  `update_time` varchar(30) DEFAULT NULL,
  PRIMARY KEY(`id`),
  UNIQUE KEY `user_unique_key` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `payment_order` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `transaction_id` varchar(40) NOT NULL,
  `amount` double NOT NULL,
  `channel` varchar(30) NOT NULL,
  `status` varchar(10) NOT NULL,
  `remark` varchar(255) NOT NULL,
  `create_time` varchar(30) NOT NULL,
  `update_time` varchar(30) DEFAULT NULL,
  PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `admin` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `last_login_time` varchar(30) DEFAULT NULL,
  `last_login_ip` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `admin_user_unique_key` (`username`) USING HASH,
  UNIQUE KEY `admin_email_unique_key` (`email`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
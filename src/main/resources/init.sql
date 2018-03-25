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

-- ----------------------------
-- Records of server
-- ----------------------------
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('MALAYSIA', '马来西亚',  'KUALA LUMPUR', '吉隆坡',  '47.254.202.96', '8001', 'pwd8001', 'AVALIABLE', 0);
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('MALAYSIA', '马来西亚',  'KUALA LUMPUR', '吉隆坡',  '47.254.202.96', '8002', 'pwd8002', 'AVALIABLE', 0);
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('MALAYSIA', '马来西亚',  'KUALA LUMPUR', '吉隆坡',  '47.254.202.96', '8003', 'pwd8003', 'AVALIABLE', 0);
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('MALAYSIA', '马来西亚',  'KUALA LUMPUR', '吉隆坡',  '47.254.202.96', '8004', 'pwd8004', 'AVALIABLE', 0);
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('MALAYSIA', '马来西亚',  'KUALA LUMPUR', '吉隆坡',  '47.254.202.96', '8005', 'pwd8005', 'AVALIABLE', 0);
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('MALAYSIA', '马来西亚',  'KUALA LUMPUR', '吉隆坡',  '47.254.202.96', '8006', 'pwd8006', 'AVALIABLE', 0);
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('MALAYSIA', '马来西亚',  'KUALA LUMPUR', '吉隆坡',  '47.254.202.96', '8007', 'pwd8007', 'AVALIABLE', 0);
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('MALAYSIA', '马来西亚',  'KUALA LUMPUR', '吉隆坡',  '47.254.202.96', '8008', 'pwd8008', 'AVALIABLE', 0);
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('MALAYSIA', '马来西亚',  'KUALA LUMPUR', '吉隆坡',  '47.254.202.96', '8009', 'pwd8009', 'AVALIABLE', 0);
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('MALAYSIA', '马来西亚',  'KUALA LUMPUR', '吉隆坡',  '47.254.202.96', '8010', 'pwd8010', 'AVALIABLE', 0);
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('SINGAPORE', '新加坡', 'SINGAPORE', '新加坡', '47.88.225.133', '8001', 'pwd8001', 'AVALIABLE', 0);
insert into server(country, country_in_chinese,  city, city_in_chinese,  domain, port, password, status, current_owner) values('SINGAPORE', '新加坡', 'SINGAPORE', '新加坡', '47.88.225.133', '8002', 'pwd8002', 'AVALIABLE', 0);
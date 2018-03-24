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
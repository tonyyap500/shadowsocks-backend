package com.shadowsocks.dto.entity;

import lombok.*;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User implements Serializable{

	private static final long serialVersionUID = 5816735320574806348L;

    private int id;
	private String username;
	private String password;
	private String email;
	private int inviter;
	private String registerIp;
	private String  registerTime;
	private int loginTimes;
	private String lastLoginTime;
	private String lastLoginIp;
	private String activeStatus;
	private String activeCode;
	private String realName;
	private String bankCardNo;
	private String withdrawPassword;
	private boolean isAdmin;
}

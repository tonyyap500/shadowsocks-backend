package com.shadowsocks.dto.entity;

import lombok.Data;

@Data
public class Admin {
    private int id;
    private String username;
    private String password;
    private String email;
    private String lastLoginTime;
    private String lastLoginIp;
    private boolean isAdmin;
}

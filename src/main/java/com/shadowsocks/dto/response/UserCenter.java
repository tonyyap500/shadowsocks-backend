package com.shadowsocks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCenter {
    private String username;
    private String email;
    private int loginTimes;
    private String lastLoginTime;
    private String lastLoginIp;
    private double balance;
}

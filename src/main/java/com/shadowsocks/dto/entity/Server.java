package com.shadowsocks.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Server {
    private int id;
    private String country;
    private String countryInChinese;
    private String city;
    private String cityInChinese;
    private String domain;
    private String port;
    private String password;
    private String currentOwner;
    private String updateTime;
}

package com.shadowsocks.dto.request;

import lombok.Data;

@Data
public class ServerRequestDto {
    private String country;
    private String countryInChinese;
    private String city;
    private String cityInChinese;
    private String domain;
    private String port;
}

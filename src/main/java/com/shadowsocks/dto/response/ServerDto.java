package com.shadowsocks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerDto {
    private String domain;
    private String port;
    private String password;
    private String encryption;
    private String country;
    private String city;
}

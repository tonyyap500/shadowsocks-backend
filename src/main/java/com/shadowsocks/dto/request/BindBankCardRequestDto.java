package com.shadowsocks.dto.request;

import lombok.Data;

@Data
public class BindBankCardRequestDto {
    private String realName;
    private String bankCardNo;
    private String withdrawPassword;
}

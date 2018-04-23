package com.shadowsocks.dto.request;

import lombok.Data;

@Data
public class WithdrawDto {
    double amount;
    private String withdrawPassword;
}

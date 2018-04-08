package com.shadowsocks.dto.request;

import lombok.Data;

@Data
public class UserBalanceRequestDto {
    private String orderId;
    private String amount;

}
